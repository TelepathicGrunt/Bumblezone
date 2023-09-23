package com.telepathicgrunt.the_bumblezone.client.blocks;

import com.google.gson.JsonObject;
import earth.terrarium.athena.api.client.models.AthenaBlockModel;
import earth.terrarium.athena.api.client.models.AthenaModelFactory;
import earth.terrarium.athena.api.client.models.AthenaQuad;
import earth.terrarium.athena.api.client.utils.AppearanceAndTintGetter;
import earth.terrarium.athena.api.client.utils.CtmState;
import earth.terrarium.athena.api.client.utils.CtmUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConnectedBlockModel implements AthenaBlockModel {

    public static final AthenaModelFactory FACTORY = new Factory();

    private static final List<AthenaQuad> BASE_QUAD =  List.of(AthenaQuad.withSprite(0));
    private static final List<AthenaQuad> FRONT_QUAD =  List.of(AthenaQuad.withSprite(1));
    private static final AthenaQuad TOP_LEFT_QUAD = AthenaQuad.withSprite(2);
    private static final AthenaQuad TOP_RIGHT_QUAD = AthenaQuad.withSprite(3);
    private static final AthenaQuad BOTTOM_LEFT_QUAD = AthenaQuad.withSprite(4);
    private static final AthenaQuad BOTTOM_RIGHT_QUAD = AthenaQuad.withSprite(5);

    private final EnumMap<ConnectedTexture, Material> materials;
    private final BiPredicate<BlockState, BlockState> connectTo;

    public ConnectedBlockModel(EnumMap<ConnectedTexture, Material> materials, BiPredicate<BlockState, BlockState> connectTo) {
        this.materials = materials;
        this.connectTo = connectTo;
    }

    @Override
    public List<AthenaQuad> getQuads(AppearanceAndTintGetter level, BlockState state, BlockPos pos, Direction direction) {
        if (CtmUtils.checkRelative(level, state, pos, direction)) {
            return List.of();
        }

        final CtmState ctm = CtmState.from(level, state, pos, direction, CtmUtils.check(level, state, pos, direction, connectTo));

        final List<AthenaQuad> quads = new ArrayList<>(isFacing(state, direction) ? FRONT_QUAD : BASE_QUAD);
        if (ctm.up() || ctm.upLeft() || ctm.left()) quads.add(TOP_LEFT_QUAD);
        if (ctm.up() || ctm.upRight() || ctm.right()) quads.add(TOP_RIGHT_QUAD);
        if (ctm.down() || ctm.downLeft() || ctm.left()) quads.add(BOTTOM_LEFT_QUAD);
        if (ctm.down() || ctm.downRight() || ctm.right()) quads.add(BOTTOM_RIGHT_QUAD);

        return quads;
    }

    private boolean isFacing(BlockState state, Direction direction) {
        return state.hasProperty(BlockStateProperties.FACING) &&
                state.getValue(BlockStateProperties.FACING).getOpposite() == direction &&
                materials.containsKey(ConnectedTexture.FRONT);
    }

    @Override
    public Map<Direction, List<AthenaQuad>> getDefaultQuads(@Nullable Direction direction) {
        return direction == null ? Map.of() : Map.of(direction, BASE_QUAD);
    }

    @Override
    public Int2ObjectMap<TextureAtlasSprite> getTextures(Function<Material, TextureAtlasSprite> getter) {
        Int2ObjectMap<TextureAtlasSprite> textures = new Int2ObjectArrayMap<>();
        for (ConnectedTexture value : ConnectedTexture.values()) {
            textures.put(value.ordinal(), getter.apply(materials.get(value)));
        }
        return textures;
    }

    private static class Factory implements AthenaModelFactory {

        @Override
        public Supplier<AthenaBlockModel> create(JsonObject json) {
            final BiPredicate<BlockState, BlockState> conditions = CtmUtils.parseCondition(json);

            final EnumMap<ConnectedTexture, Material> materials = new EnumMap<>(ConnectedTexture.class);
            final JsonObject textures = GsonHelper.getAsJsonObject(json, "textures");
            ConnectedTexture.list().forEach(texture -> texture.read(materials, textures));

            return () -> new ConnectedBlockModel(materials, conditions);
        }
    }
}
