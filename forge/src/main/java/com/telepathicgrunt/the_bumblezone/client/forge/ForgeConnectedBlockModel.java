package com.telepathicgrunt.the_bumblezone.client.forge;

import com.google.common.collect.Maps;
import com.telepathicgrunt.the_bumblezone.client.bakedmodel.ConnectedBlockModel;
import com.telepathicgrunt.the_bumblezone.client.bakedmodel.ConnectedBlockModel.Connection;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.NamedRenderTypeManager;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Predicate;

public class ForgeConnectedBlockModel implements IDynamicBakedModel {

    private static final BlockElementFace FULL_FACE = new BlockElementFace(null, -1, "", new BlockFaceUV(new float[]{0f, 0f, 16f, 16f}, 0));
    public static final ModelProperty<EnumMap<Direction, Set<Connection>>> DATA = new ModelProperty<>();

    private final EnumMap<Direction, EnumMap<Connection, BakedQuad>> quads = null;
    private final BakedModel baseModel = null;
    private final BakedModel facingModel = null;
    private final ConnectedBlockModel model;

    public ForgeConnectedBlockModel(EnumMap<Connection, TextureAtlasSprite> sprites, Predicate<BlockState> predicate) {
        this.model = new ConnectedBlockModel(predicate);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState arg, @Nullable Direction arg2, @NotNull RandomSource arg3, @NotNull ModelData modelData, @Nullable RenderType arg4) {
        return null;
    }

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData) {
        final EnumMap<Direction, Set<Connection>> connections = new EnumMap<>(Direction.class);
        for (Direction direction : Direction.values()) {
            connections.put(direction, model.getSprites(level, pos, direction));
        }
        return modelData.derive().with(DATA, connections).build();
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
        return true;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return this.baseModel.getParticleIcon(ModelData.EMPTY);
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    private static BakedModel createFullBlock(Material texture, ResourceLocation modelLocation) {
        final BlockModel dummy = new BlockModel(null, List.of(), Map.of(), false, BlockModel.GuiLight.FRONT, ItemTransforms.NO_TRANSFORMS, new ArrayList<>());
        final Map<Direction, BlockElementFace> faces = Maps.newEnumMap(Direction.class);
        for (Direction direction : Direction.values()) {
            faces.put(direction, FULL_FACE);
        }
        final BlockElement element = new BlockElement(new Vector3f(0f, 0f, 0f), new Vector3f(16f, 16f, 16f), faces, null, true);
        final SimpleBakedModel.Builder builder = new SimpleBakedModel.Builder(dummy, ItemOverrides.EMPTY, false).particle(texture.sprite());

        element.faces.forEach((direction, face) -> builder.addCulledFace(direction,
                new FaceBakery().bakeQuad(
                        element.from, element.to, face,
                        texture.sprite(), direction,
                        BlockModelRotation.X0_Y0, element.rotation,
                        true, modelLocation
                ))
        );
        return builder.build(NamedRenderTypeManager.get(new ResourceLocation("solid")));
    }

    private static Map<Direction, BakedModel[]> buildFaces(ResourceLocation modelLocation, Material particle, Material[] cornerLocations) {
        final Map<Direction, BakedModel[]> map = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) map.put(d, new BakedModel[4]);
        for (Connection connection : Connection.BASELESS) {
            put(map, Direction.WEST, connection, bakedModel(new Vector3f(-0.01f, 0f, 0f), new Vector3f(-0.01f, 16f, 16f), Direction.WEST, connection.hFlip(), modelLocation, particle, cornerLocations));
            put(map, Direction.EAST, connection, bakedModel(new Vector3f(16.01f, 0f, 0f), new Vector3f(16.01f, 16f, 16f), Direction.EAST, connection, modelLocation, particle, cornerLocations));
            put(map, Direction.NORTH, connection, bakedModel(new Vector3f(0f, 0f, -0.01f), new Vector3f(16f, 16f, -0.01f), Direction.NORTH, connection, modelLocation, particle, cornerLocations));
            put(map, Direction.SOUTH, connection, bakedModel(new Vector3f(0f, 0f, 16.01f), new Vector3f(16f, 16f, 16.01f), Direction.SOUTH, connection.hFlip(), modelLocation, particle, cornerLocations));
            put(map, Direction.DOWN, connection, bakedModel(new Vector3f(0f, -0.01f, 0f), new Vector3f(16f, -0.01f, 16f), Direction.DOWN, connection.hFlip(), modelLocation, particle, cornerLocations));
            put(map, Direction.UP, connection, bakedModel(new Vector3f(0f, 16.01f, 0f), new Vector3f(16f, 16.01f, 16f), Direction.UP, connection.vFlip().hFlip(), modelLocation, particle, cornerLocations));
        }
        return map;
    }

    private static BakedModel bakedModel(Vector3f from, Vector3f to, Direction direction, Connection corner, ResourceLocation modelLocation, Material material, Material[] textures) {
        final BlockModel dummy = new BlockModel(null, List.of(), Map.of(), false, BlockModel.GuiLight.FRONT, ItemTransforms.NO_TRANSFORMS, new ArrayList<>());
        return new SimpleBakedModel.Builder(dummy, ItemOverrides.EMPTY, false)
                .particle(material.sprite())
                .addCulledFace(direction, new FaceBakery()
                        .bakeQuad(from, to,
                                FULL_FACE, textures[corner.ordinal()].sprite(),
                                direction, BlockModelRotation.X0_Y0,
                                null, true, modelLocation)
                )
                .build(NamedRenderTypeManager.get(new ResourceLocation("cutout")));
    }

    private static void put(Map<Direction, BakedModel[]> map, Direction d, Connection corner, BakedModel model) {
        map.get(d)[corner.ordinal()] = model;
    }

}
