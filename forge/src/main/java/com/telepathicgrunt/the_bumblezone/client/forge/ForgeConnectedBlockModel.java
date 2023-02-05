package com.telepathicgrunt.the_bumblezone.client.forge;

import com.google.common.collect.Maps;
import com.telepathicgrunt.the_bumblezone.client.bakedmodel.ConnectedBlockModel;
import com.telepathicgrunt.the_bumblezone.client.bakedmodel.ConnectedBlockModel.Texture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.NamedRenderTypeManager;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class ForgeConnectedBlockModel implements IDynamicBakedModel {

    private static final BlockElementFace FULL_FACE = new BlockElementFace(null, -1, "", new BlockFaceUV(new float[]{0f, 0f, 16f, 16f}, 0));
    public static final List<ForgeConnectedBlockModel> INSTANCES = new ArrayList<>();
    public static final ModelProperty<EnumMap<Direction, Set<Texture>>> DATA = new ModelProperty<>();

    private final ConnectedBlockModel model;
    private final EnumMap<Texture, Material> sprites;
    private final ResourceLocation id;

    private EnumMap<Direction, EnumMap<Texture, BakedModel>> quads;
    private BakedModel baseModel;
    private BakedModel facingModel;

    public ForgeConnectedBlockModel(ResourceLocation id, EnumMap<Texture, Material> sprites, Predicate<BlockState> predicate) {
        this.model = new ConnectedBlockModel(predicate);
        this.sprites = sprites;
        this.id = id;

        INSTANCES.add(this);
    }

    private void lateInit() {
        this.baseModel = createFullBlock(sprites.get(Texture.BASE).sprite(), id);
        this.facingModel = sprites.containsKey(Texture.FRONT) ? createFullBlock(sprites.get(Texture.FRONT).sprite(), id) : null;
        this.quads = buildFaces(id, sprites.getOrDefault(Texture.PARTICLE, sprites.get(Texture.BASE)).sprite(), new TextureAtlasSprite[] {
                sprites.get(Texture.TOP_LEFT).sprite(), sprites.get(Texture.TOP_RIGHT).sprite(),
                sprites.get(Texture.BOTTOM_LEFT).sprite(), sprites.get(Texture.BOTTOM_RIGHT).sprite()
        });
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, @NotNull RandomSource arg3, @NotNull ModelData modelData, @Nullable RenderType arg4) {
        final List<BakedQuad> finalQuads = new ArrayList<>();
        if (state != null && state.hasProperty(BlockStateProperties.FACING) && state.getValue(BlockStateProperties.FACING).getOpposite() == direction) {
            finalQuads.addAll(facingModel.getQuads(state, direction, arg3, modelData, arg4));
        } else {
            finalQuads.addAll(baseModel.getQuads(state, direction, arg3, modelData, arg4));
        }

        if (direction != null && modelData.has(DATA)) {
            final EnumMap<Direction, Set<Texture>> connections = modelData.get(DATA);
            if (connections != null) {
                final EnumMap<Texture, BakedModel> models = quads.get(direction);
                for (Texture connection : connections.get(direction)) {
                    finalQuads.addAll(models.get(connection).getQuads(state, direction, arg3, modelData, arg4));
                }
            }
        }

        return finalQuads;
    }

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData) {
        final EnumMap<Direction, Set<Texture>> connections = new EnumMap<>(Direction.class);
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

    private static BakedModel createFullBlock(TextureAtlasSprite sprite, ResourceLocation modelLocation) {
        final BlockModel dummy = new BlockModel(null, List.of(), Map.of(), false, BlockModel.GuiLight.FRONT, ItemTransforms.NO_TRANSFORMS, new ArrayList<>());
        final Map<Direction, BlockElementFace> faces = Maps.newEnumMap(Direction.class);
        for (Direction direction : Direction.values()) {
            faces.put(direction, FULL_FACE);
        }
        final BlockElement element = new BlockElement(new Vector3f(0f, 0f, 0f), new Vector3f(16f, 16f, 16f), faces, null, true);
        final SimpleBakedModel.Builder builder = new SimpleBakedModel.Builder(dummy, ItemOverrides.EMPTY, false).particle(sprite);

        element.faces.forEach((direction, face) -> builder.addCulledFace(direction,
                new FaceBakery().bakeQuad(
                        element.from, element.to, face,
                        sprite, direction,
                        BlockModelRotation.X0_Y0, element.rotation,
                        true, modelLocation
                ))
        );
        return builder.build(NamedRenderTypeManager.get(new ResourceLocation("solid")));
    }

    private static EnumMap<Direction, EnumMap<Texture, BakedModel>> buildFaces(ResourceLocation modelLocation, TextureAtlasSprite particle, TextureAtlasSprite[] cornerLocations) {
        final EnumMap<Direction, EnumMap<Texture, BakedModel>> map = new EnumMap<>(Direction.class);
        for (Direction d : Direction.values()) map.put(d, new EnumMap<>(Texture.class));
        for (Texture connection : Texture.BASELESS) {
            put(map, Direction.WEST, connection, bakedModel(new Vector3f(-0.01f, 0f, 0f), new Vector3f(-0.01f, 16f, 16f), Direction.WEST, connection, modelLocation, particle, cornerLocations));
            put(map, Direction.EAST, connection, bakedModel(new Vector3f(16.01f, 0f, 0f), new Vector3f(16.01f, 16f, 16f), Direction.EAST, connection, modelLocation, particle, cornerLocations));
            put(map, Direction.NORTH, connection, bakedModel(new Vector3f(0f, 0f, -0.01f), new Vector3f(16f, 16f, -0.01f), Direction.NORTH, connection, modelLocation, particle, cornerLocations));
            put(map, Direction.SOUTH, connection, bakedModel(new Vector3f(0f, 0f, 16.01f), new Vector3f(16f, 16f, 16.01f), Direction.SOUTH, connection, modelLocation, particle, cornerLocations));
            put(map, Direction.DOWN, connection, bakedModel(new Vector3f(0f, -0.01f, 0f), new Vector3f(16f, -0.01f, 16f), Direction.DOWN, connection.vFlip(), modelLocation, particle, cornerLocations));
            put(map, Direction.UP, connection, bakedModel(new Vector3f(0f, 16.01f, 0f), new Vector3f(16f, 16.01f, 16f), Direction.UP, connection, modelLocation, particle, cornerLocations));
        }
        return map;
    }

    private static BakedModel bakedModel(Vector3f from, Vector3f to, Direction direction, Texture corner, ResourceLocation modelLocation, TextureAtlasSprite material, TextureAtlasSprite[] textures) {
        final BlockModel dummy = new BlockModel(null, List.of(), Map.of(), false, BlockModel.GuiLight.FRONT, ItemTransforms.NO_TRANSFORMS, new ArrayList<>());
        return new SimpleBakedModel.Builder(dummy, ItemOverrides.EMPTY, false)
                .particle(material)
                .addCulledFace(direction, new FaceBakery()
                        .bakeQuad(from, to,
                                FULL_FACE, textures[corner.ordinal() - 3],
                                direction, BlockModelRotation.X0_Y0,
                                null, true, modelLocation)
                )
                .build(NamedRenderTypeManager.get(new ResourceLocation("cutout")));
    }

    private static void put(EnumMap<Direction, EnumMap<Texture, BakedModel>> map, Direction d, Texture corner, BakedModel model) {
        map.get(d).put(corner, model);
    }

    public static void onBakingCompleted(ModelEvent.BakingCompleted event) {
        INSTANCES.forEach(ForgeConnectedBlockModel::lateInit);
    }

}
