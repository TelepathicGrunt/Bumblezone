package com.telepathicgrunt.the_bumblezone.client.bakemodel;

import com.telepathicgrunt.the_bumblezone.client.bakedmodel.ConnectedBlockModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FabricConnectedBlockModel implements BakedModel, FabricBakedModel {

    private final EnumMap<ConnectedBlockModel.Connection, TextureAtlasSprite> textures;
    private final ConnectedBlockModel model;

    public FabricConnectedBlockModel(Map<String, TextureAtlasSprite> textures, Predicate<BlockState> connectionPredicate) {
        this.model = new ConnectedBlockModel(connectionPredicate);
        this.textures = createTextures(textures);
    }

    private static EnumMap<ConnectedBlockModel.Connection, TextureAtlasSprite> createTextures(Map<String, TextureAtlasSprite> textures) {
        EnumMap<ConnectedBlockModel.Connection, TextureAtlasSprite> map = new EnumMap<>(ConnectedBlockModel.Connection.class);
        textures.forEach((key, value) ->
                ConnectedBlockModel.Connection.tryParse(key)
                .ifPresent(connection -> map.put(connection, value))
        );
        return map;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter level, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        emitQuads(context.getEmitter(), level, state, pos);
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
        emitQuads(context.getEmitter(), null, null, null);
    }

    private void emitQuads(QuadEmitter emitter, @Nullable BlockAndTintGetter level, @Nullable BlockState state, @Nullable BlockPos pos) {
        for (Direction value : Direction.values()) {

            if (state != null && state.hasProperty(BlockStateProperties.FACING) && value.getOpposite() == state.getValue(BlockStateProperties.FACING)) {
                emitter.square(value, 0, 0, 1, 1, 0);
                emitter.spriteBake(0, textures.get(ConnectedBlockModel.Connection.FRONT), MutableQuadView.BAKE_LOCK_UV);
                emitter.spriteColor(0, -1, -1, -1, -1);
                emitter.emit();
            } else {
                emitter.square(value, 0, 0, 1, 1, 0);
                emitter.spriteBake(0, textures.get(ConnectedBlockModel.Connection.BASE), MutableQuadView.BAKE_LOCK_UV);
                emitter.spriteColor(0, -1, -1, -1, -1);
                emitter.emit();
            }

            if (level != null && state != null && pos != null) {
                for (ConnectedBlockModel.Connection connection : model.getSprites(level, pos, value)) {

                    if (connection != null) {
                        TextureAtlasSprite sprite = textures.get(connection);
                        emitter.square(value, 0, 0, 1, 1, 0);
                        emitter.spriteBake(0, sprite, MutableQuadView.BAKE_LOCK_UV);
                        emitter.spriteColor(0, -1, -1, -1, -1);
                        emitter.emit();
                    }
                }
            }

        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, @NotNull RandomSource random) {
        return List.of();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return textures.get(ConnectedBlockModel.Connection.BASE);
    }

    @Override
    public ItemTransforms getTransforms() {
        return ItemTransforms.NO_TRANSFORMS;
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }
}
