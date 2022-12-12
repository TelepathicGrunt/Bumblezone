package com.telepathicgrunt.the_bumblezone.client.bakedmodel;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.blocks.EmptyHoneycombBrood;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.NamedRenderTypeManager;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EmptyHoneycombBroodBlockModel implements IDynamicBakedModel {

    private static final ChunkRenderTypeSet CUTOUT_CHUNK_RENDER_TYPE = ChunkRenderTypeSet.of(RenderType.cutout());
    public static final ModelProperty<Map<Direction, Set<CORNERS>>> DIRECTION_OF_HONEY_MERGERS = new ModelProperty<>();
    public static final List<EmptyHoneycombBroodBlockModel> INSTANCES = new ArrayList<>();
    public static final Direction[] DIRECTIONS = Direction.values();
    public static final Direction.AxisDirection[] AXIS_DIRECTIONS = Direction.AxisDirection.values();

    public static final ModelResourceLocation BACKING_DIRT_MODEL = new ModelResourceLocation("minecraft", "dirt", "inventory");

    @SuppressWarnings("deprecation")
    public static final ResourceLocation BLOCK_ATLAS = TextureAtlas.LOCATION_BLOCKS;
    private final ResourceLocation modelLocation;
    private final ResourceLocation topLeft;
    private final ResourceLocation topRight;
    private final ResourceLocation botLeft;
    private final ResourceLocation botRight;
    private final ResourceLocation particle;
    private final ResourceLocation side;
    private final ResourceLocation front;
    private TextureAtlasSprite particleTex;
    private Map<Direction, BakedModel[]> cache;
    private BakedModel[] mainModels;

    private enum CORNERS {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

    private enum CHECKS {
        TLX(Direction.Axis.X, CORNERS.TOP_LEFT, 0, 1, 1),
        TLY(Direction.Axis.Y, CORNERS.TOP_LEFT, 1, 0, 1),
        TLZ(Direction.Axis.Z, CORNERS.TOP_LEFT, 1, 1, 0),
        TRX(Direction.Axis.X, CORNERS.TOP_RIGHT, 0, 1, -1),
        TRY(Direction.Axis.Y, CORNERS.TOP_RIGHT, -1, 0, 1),
        TRZ(Direction.Axis.Z, CORNERS.TOP_RIGHT, -1, 1, 0),
        BLX(Direction.Axis.X, CORNERS.BOTTOM_LEFT, 0, -1, 1),
        BLY(Direction.Axis.Y, CORNERS.BOTTOM_LEFT, 1, 0, -1),
        BLZ(Direction.Axis.Z, CORNERS.BOTTOM_LEFT, 1, -1, 0),
        BRX(Direction.Axis.X, CORNERS.BOTTOM_RIGHT, 0, -1, -1),
        BRY(Direction.Axis.Y, CORNERS.BOTTOM_RIGHT, -1, 0, -1),
        BRZ(Direction.Axis.Z, CORNERS.BOTTOM_RIGHT, -1, -1, 0);

        public static final CHECKS[] VALUES = values();

        private final Direction.Axis axis;
        private final CORNERS corner;
        private final int dx;
        private final int dy;
        private final int dz;

        CHECKS(Direction.Axis axis, CORNERS corner, int dx, int dy, int dz) {
            this.axis = axis;
            this.corner = corner;
            this.dx = dx;
            this.dy = dy;
            this.dz = dz;
        }

        void set(BlockPos.MutableBlockPos mutable, BlockPos origin) {
            mutable.setWithOffset(origin, dx, dy, dz);
        }

        void put(Map<Direction, Set<CORNERS>> map, CHECKS check) {
            for (Direction.AxisDirection axisDir : AXIS_DIRECTIONS) {
                final Direction d = Direction.fromAxisAndDirection(axis, axisDir);
                map.get(d).add(check.corner);
            }
        }
    }

    private void checkAdjacent(BlockAndTintGetter level, BlockPos.MutableBlockPos mutable, BlockPos origin, Map<Direction, Set<CORNERS>> map) {
        for (Direction d : Direction.Plane.HORIZONTAL) {
            if (needsConnection(level, mutable.setWithOffset(origin, d))) {
                final Set<CORNERS> ccw = map.get(d.getCounterClockWise());
                final Set<CORNERS> cw = map.get(d.getClockWise());
                final Set<CORNERS> up = map.get(Direction.UP);
                final Set<CORNERS> down = map.get(Direction.DOWN);
                if (d == Direction.EAST) {
                    ccw.add(CORNERS.TOP_LEFT);
                    ccw.add(CORNERS.BOTTOM_LEFT);
                    cw.add(CORNERS.TOP_LEFT);
                    cw.add(CORNERS.BOTTOM_LEFT);
                    up.add(CORNERS.TOP_LEFT);
                    up.add(CORNERS.BOTTOM_LEFT);
                    down.add(CORNERS.TOP_LEFT);
                    down.add(CORNERS.BOTTOM_LEFT);
                }
                else if (d == Direction.WEST) {
                    cw.add(CORNERS.TOP_RIGHT);
                    cw.add(CORNERS.BOTTOM_RIGHT);
                    ccw.add(CORNERS.TOP_RIGHT);
                    ccw.add(CORNERS.BOTTOM_RIGHT);
                    down.add(CORNERS.TOP_RIGHT);
                    down.add(CORNERS.BOTTOM_RIGHT);
                    up.add(CORNERS.TOP_RIGHT);
                    up.add(CORNERS.BOTTOM_RIGHT);
                }
                else if (d == Direction.SOUTH) {
                    cw.add(CORNERS.BOTTOM_LEFT);
                    cw.add(CORNERS.TOP_LEFT);
                    ccw.add(CORNERS.BOTTOM_LEFT);
                    ccw.add(CORNERS.TOP_LEFT);
                    down.add(CORNERS.TOP_RIGHT);
                    down.add(CORNERS.TOP_LEFT);
                    up.add(CORNERS.TOP_RIGHT);
                    up.add(CORNERS.TOP_LEFT);
                }
                else if (d == Direction.NORTH) {
                    cw.add(CORNERS.BOTTOM_RIGHT);
                    cw.add(CORNERS.TOP_RIGHT);
                    ccw.add(CORNERS.BOTTOM_RIGHT);
                    ccw.add(CORNERS.TOP_RIGHT);
                    down.add(CORNERS.BOTTOM_RIGHT);
                    down.add(CORNERS.BOTTOM_LEFT);
                    up.add(CORNERS.BOTTOM_RIGHT);
                    up.add(CORNERS.BOTTOM_LEFT);
                }
            }
        }
        if (needsConnection(level, mutable.setWithOffset(origin, 0, 1, 0))) {
            for (Direction d : Direction.Plane.HORIZONTAL) {
                map.get(d).add(CORNERS.TOP_LEFT);
                map.get(d).add(CORNERS.TOP_RIGHT);
            }
        }
        if (needsConnection(level, mutable.setWithOffset(origin, 0, -1, 0))) {
            for (Direction d : Direction.Plane.HORIZONTAL) {
                map.get(d).add(CORNERS.BOTTOM_LEFT);
                map.get(d).add(CORNERS.BOTTOM_RIGHT);
            }
        }

    }

    public EmptyHoneycombBroodBlockModel(ResourceLocation modelLocation, ResourceLocation botLeft, ResourceLocation botRight, ResourceLocation topLeft, ResourceLocation topRight, ResourceLocation particle, ResourceLocation side, ResourceLocation front) {
        this.particle = particle;
        this.modelLocation = modelLocation;
        this.botLeft = botLeft;
        this.botRight = botRight;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.side = side;
        this.front = front;
        INSTANCES.add(this);
    }

    public void init()
    {
        this.cache = buildCache(modelLocation, particle, new ResourceLocation[] {topLeft, topRight, botLeft, botRight});
        this.mainModels = buildBlocks(side, front, modelLocation);
        this.particleTex = getTexture(particle);
    }

    private static Map<Direction, BakedModel[]> buildCache(ResourceLocation modelLocation, ResourceLocation particle, ResourceLocation[] cornerLocations) {
        final Map<Direction, BakedModel[]> map = new EnumMap<>(Direction.class);
        for (Direction d : DIRECTIONS) map.put(d, new BakedModel[4]);
        for (CORNERS corner : CORNERS.values()) {
            put(map, Direction.WEST, corner, bakedModel(new Vector3f(-0.01f, 0f, 0f), new Vector3f(-0.01f, 16f, 16f), Direction.WEST, flipLR(corner), modelLocation, particle, cornerLocations));
            put(map, Direction.EAST, corner, bakedModel(new Vector3f(16.01f, 0f, 0f), new Vector3f(16.01f, 16f, 16f), Direction.EAST, corner, modelLocation, particle, cornerLocations));
            put(map, Direction.NORTH, corner, bakedModel(new Vector3f(0f, 0f, -0.01f), new Vector3f(16f, 16f, -0.01f), Direction.NORTH, corner, modelLocation, particle, cornerLocations));
            put(map, Direction.SOUTH, corner, bakedModel(new Vector3f(0f, 0f, 16.01f), new Vector3f(16f, 16f, 16.01f), Direction.SOUTH, flipLR(corner), modelLocation, particle, cornerLocations));
            put(map, Direction.DOWN, corner, bakedModel(new Vector3f(0f, -0.01f, 0f), new Vector3f(16f, -0.01f, 16f), Direction.DOWN, flipLR(corner), modelLocation, particle, cornerLocations));
            put(map, Direction.UP, corner, bakedModel(new Vector3f(0f, 16.01f, 0f), new Vector3f(16f, 16.01f, 16f), Direction.UP, flipLR(flipUD(corner)), modelLocation, particle, cornerLocations));
        }
        return map;
    }

    private static CORNERS flipLR(CORNERS corner) {
        return switch (corner) {
            case TOP_LEFT -> CORNERS.TOP_RIGHT;
            case TOP_RIGHT -> CORNERS.TOP_LEFT;
            case BOTTOM_LEFT -> CORNERS.BOTTOM_RIGHT;
            case BOTTOM_RIGHT -> CORNERS.BOTTOM_LEFT;
        };
    }

    private static CORNERS flipUD(CORNERS corner) {
        return switch (corner) {
            case TOP_LEFT -> CORNERS.BOTTOM_LEFT;
            case TOP_RIGHT -> CORNERS.BOTTOM_RIGHT;
            case BOTTOM_LEFT -> CORNERS.TOP_LEFT;
            case BOTTOM_RIGHT -> CORNERS.TOP_RIGHT;
        };
    }

    private static void put(Map<Direction, BakedModel[]> map, Direction d, CORNERS corner, BakedModel model) {
        map.get(d)[corner.ordinal()] = model;
    }

    private static BakedModel[] buildBlocks(ResourceLocation textureLocation, ResourceLocation textureFrontLocation, ResourceLocation modelLocation) {
        BakedModel[] models = new BakedModel[6];
        for (Direction facing : DIRECTIONS) {
            final BlockModel dummy = new BlockModel(null, new ArrayList<>(), new HashMap<>(), false, BlockModel.GuiLight.FRONT, ItemTransforms.NO_TRANSFORMS, new ArrayList<>());
            final TextureAtlasSprite tex = getTexture(textureLocation);
            final TextureAtlasSprite tex2 = getTexture(textureFrontLocation);
            final Map<Direction, BlockElementFace> mapFacesIn = Maps.newEnumMap(Direction.class);
            for (Direction d : DIRECTIONS) {
                mapFacesIn.put(d, new BlockElementFace(null, -1, "", new BlockFaceUV(new float[] {0f, 0f, 16f, 16f}, 0)));
            }
            final BlockElement part = new BlockElement(new Vector3f(0f, 0f, 0f), new Vector3f(16f, 16f, 16f), mapFacesIn, null, true);
            final SimpleBakedModel.Builder builder = new SimpleBakedModel.Builder(dummy, ItemOverrides.EMPTY, false).particle(tex);

            for (Map.Entry<Direction, BlockElementFace> e : part.faces.entrySet()) {
                Direction d = e.getKey();
                if (d == facing.getOpposite()) {
                    builder.addCulledFace(d, makeBakedQuad(part, e.getValue(), tex2, d, BlockModelRotation.X0_Y0, modelLocation));
                }
                else {
                    builder.addCulledFace(d, makeBakedQuad(part, e.getValue(), tex, d, BlockModelRotation.X0_Y0, modelLocation));
                }
            }
            models[facing.ordinal()] = builder.build(NamedRenderTypeManager.get(new ResourceLocation("solid")));
        }
        return models;
    }

    private static BakedModel bakedModel(Vector3f from, Vector3f to, Direction direction, CORNERS corner, ResourceLocation modelLocation, ResourceLocation particleLocation, ResourceLocation[] textures) {
        final BlockElement element = blockElement(from, to, direction);
        final BlockModel dummy = new BlockModel(null, new ArrayList<>(), new HashMap<>(), false, BlockModel.GuiLight.FRONT, ItemTransforms.NO_TRANSFORMS, new ArrayList<>());
        final SimpleBakedModel.Builder builder = new SimpleBakedModel.Builder(dummy, ItemOverrides.EMPTY, false).particle(getTexture(particleLocation));
        builder.addCulledFace(direction, makeBakedQuad(element, element.faces.get(direction), getTexture(textures[corner.ordinal()]), direction, BlockModelRotation.X0_Y0, modelLocation));
        return builder.build(NamedRenderTypeManager.get(new ResourceLocation("cutout")));
    }

    private static TextureAtlasSprite getTexture(ResourceLocation id) {
        return Minecraft.getInstance().getTextureAtlas(BLOCK_ATLAS).apply(id);
    }

    private static BakedQuad makeBakedQuad(BlockElement BlockElement, BlockElementFace partFace, TextureAtlasSprite atlasSprite, Direction dir, BlockModelRotation modelRotation, ResourceLocation modelResLoc) {
        return new FaceBakery().bakeQuad(BlockElement.from, BlockElement.to, partFace, atlasSprite, dir, modelRotation, BlockElement.rotation, true, modelResLoc);
    }

    private static BlockElement blockElement(Vector3f from, Vector3f to, Direction direction) {
        final Map<Direction, BlockElementFace> mapFacesIn = new EnumMap<>(Direction.class);
        mapFacesIn.put(direction, new BlockElementFace(null, -1, "", new BlockFaceUV(new float[] {0f, 0f, 16f, 16f}, 0)));
        return new BlockElement(from, to, mapFacesIn, null, true);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData extraData, RenderType renderType) {
        int facingIndex = 0;
        if (state != null && state.hasProperty(EmptyHoneycombBrood.FACING)) {
            facingIndex = state.getValue(EmptyHoneycombBrood.FACING).ordinal();
        }
        final List<BakedQuad> quads = new ArrayList<>(mainModels[facingIndex].getQuads(state, side, rand, extraData, renderType));

        if (quads.size() > 0 && side != null && extraData.has(DIRECTION_OF_HONEY_MERGERS))
        {
            final Map<Direction, Set<CORNERS>> directionsOfHoney = extraData.get(DIRECTION_OF_HONEY_MERGERS);
            if (directionsOfHoney != null) {
                for (CORNERS corners : directionsOfHoney.get(side)) {
                    quads.addAll(cache.get(side)[corners.ordinal()].getQuads(state, side, rand, extraData, renderType));
                }
            }
        }
        return quads;
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {
        final Map<Direction, Set<CORNERS>> currentData = new EnumMap<>(Direction.class);
        if (state != null) {
            for (Direction direction : DIRECTIONS) {
                currentData.put(direction, new HashSet<>());
            }
            final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
            for (CHECKS check : CHECKS.VALUES) {
                check.set(cursor, pos);
                if (needsConnection(level, cursor)) {
                    check.put(currentData, check);
                }
            }
            checkAdjacent(level, cursor, pos, currentData);
        }
        return modelData.derive().with(DIRECTION_OF_HONEY_MERGERS, ImmutableMap.copyOf(currentData)).build();
    }


    private boolean needsConnection(BlockAndTintGetter level, BlockPos pos){
        BlockState neighborState = level.getBlockState(pos);
        return neighborState.is(BzBlocks.FILLED_POROUS_HONEYCOMB.get()) || neighborState.is(BzBlocks.HONEYCOMB_BROOD.get());
    }

    @Override
    public boolean usesBlockLight() {
        return true;
    }

    @Override
    public boolean useAmbientOcclusion () {
        return true;
    }

    @Override
    public boolean isGui3d () {
        return false;
    }

    @Override
    public boolean isCustomRenderer () {
        return false;
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(BlockState state, RandomSource rand, ModelData data) {
        return CUTOUT_CHUNK_RENDER_TYPE;
    }

    @Override
    public List<RenderType> getRenderTypes(ItemStack itemStack, boolean fabulous) {
        return List.of(RenderType.cutout());
    }

    @Override
    public TextureAtlasSprite getParticleIcon () {
        return particleTex;
    }

    @Override
    public ItemOverrides getOverrides () {
        return ItemOverrides.EMPTY;
    }

    @Override
    public BakedModel applyTransform(ItemTransforms.TransformType transformType, PoseStack poseStack, boolean applyLeftHandTransform)
    {
        Minecraft.getInstance().getModelManager().getModel(BACKING_DIRT_MODEL).applyTransform(transformType, poseStack, applyLeftHandTransform);
        return this;
    }

    public static void registerModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register("empty_honeycomb_brood", new EmptyHoneycombBroodLoader());
    }

    public static void onBakingCompleted(ModelEvent.BakingCompleted event) {
        INSTANCES.forEach(EmptyHoneycombBroodBlockModel::init);
    }

}