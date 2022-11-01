package com.telepathicgrunt.the_bumblezone.client.bakedmodel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public class PorousHoneycombBlockModel implements IDynamicBakedModel {
    public static final ModelProperty<Map<Direction, HashSet<CORNERS>>> DIRECTION_OF_HONEY_MERGERS = new ModelProperty<>();
    protected final BakedModel mainModel;
    private final Map<List<CORNERS>, BakedModel> cache = Maps.newHashMap();
    private enum CORNERS {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

    private PorousHoneycombBlockModel(BakedModel mainModel) {
        this.mainModel = mainModel;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData extraData, RenderType renderType) {
        List<BakedQuad> quads = Lists.newArrayList();
        quads.addAll(mainModel.getQuads(state, side, rand, extraData, renderType));

        if (quads.size() > 0 &&
            side != null &&
            state != null &&
            extraData.has(DIRECTION_OF_HONEY_MERGERS) &&
            extraData.get(DIRECTION_OF_HONEY_MERGERS) != null)
        {
            Map<Direction, HashSet<CORNERS>> directionsOfHoney = extraData.get(DIRECTION_OF_HONEY_MERGERS);

            // For sides for face to connect texture to
            for (CORNERS corners : directionsOfHoney.get(side)) {

                TextureAtlasSprite textureAtlasSprite = Minecraft.getInstance().getModelManager()
                        .getAtlas(TextureAtlas.LOCATION_BLOCKS)
                        .getSprite(new ResourceLocation(Bumblezone.MODID, "textures/block/porous_honeycomb_block_corner_overlays/"+corners.name().toLowerCase(Locale.ROOT)+".png"));

                // draw connecting texture on top of previous quad?
                BakedQuad newBakedQuad = new BakedQuad(
                        quads.get(0).getVertices(),
                        0,
                        side,
                        textureAtlasSprite,
                        true
                    );

                quads.add(newBakedQuad);
            }
        }

        return quads;
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {
        ModelData currentData = modelData;
        if (state != null) {
            if (!currentData.has(DIRECTION_OF_HONEY_MERGERS) || currentData.get(DIRECTION_OF_HONEY_MERGERS) == null) {
                currentData = ModelData.builder()
                        .with(DIRECTION_OF_HONEY_MERGERS, new HashMap<>())
                        .build();
            }

            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            for (Direction direction : Direction.values()) {
                currentData.get(DIRECTION_OF_HONEY_MERGERS).put(direction, new HashSet<>());
            }

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        int absSum = Math.abs(x) + Math.abs(y) + Math.abs(z);
                        if (absSum == 3 || absSum == 0) {
                            continue;
                        }

                        mutableBlockPos.set(pos).move(x, y, z);
                        BlockState neighborState = level.getBlockState(mutableBlockPos);
                        if (neighborState.is(BzBlocks.FILLED_POROUS_HONEYCOMB.get()) || neighborState.is(BzBlocks.HONEYCOMB_BROOD.get())) {
                            for (Direction direction : Direction.values()) {
                                if (Math.abs(direction.getStepX()) == Math.abs(x) &&
                                    Math.abs(direction.getStepY()) == Math.abs(y) &&
                                    Math.abs(direction.getStepZ()) == Math.abs(z))
                                {
                                    continue;
                                }

                                if (x == -1 && z == -1) {
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.BOTTOM_LEFT);
                                }
                                else if (x == -1 && z == 0) {
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.BOTTOM_LEFT);
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.TOP_LEFT);
                                }
                                else if (x == -1 && z == 1) {
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.TOP_LEFT);
                                }
                                else if (x == 1 && z == -1) {
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.BOTTOM_RIGHT);
                                }
                                else if (x == 1 && z == 0) {
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.BOTTOM_RIGHT);
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.TOP_RIGHT);
                                }
                                else if (x == 1 && z == 1) {
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.TOP_RIGHT);
                                }
                                else if (x == -1 && y == -1) {
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.BOTTOM_LEFT);
                                }
                                else if (x == -1 && y == 0) {
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.BOTTOM_LEFT);
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.TOP_LEFT);
                                }
                                else if (x == -1 && y == 1) {
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.TOP_LEFT);
                                }
                                else if (x == 1 && y == -1) {
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.BOTTOM_RIGHT);
                                }
                                else if (x == 1 && y == 0) {
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.BOTTOM_RIGHT);
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.TOP_RIGHT);
                                }
                                else if (x == 1 && y == 1) {
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.TOP_RIGHT);
                                }
                                else if (y == -1 && z == -1) {
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.BOTTOM_LEFT);
                                }
                                else if (y == -1 && z == 0) {
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.BOTTOM_LEFT);
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.TOP_LEFT);
                                }
                                else if (y == -1 && z == 1) {
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.TOP_LEFT);
                                }
                                else if (y == 1 && z == -1) {
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.BOTTOM_RIGHT);
                                }
                                else if (y == 1 && z == 0) {
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.BOTTOM_RIGHT);
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.TOP_RIGHT);
                                }
                                else if (y == 1 && z == 1) {
                                    currentData.get(DIRECTION_OF_HONEY_MERGERS).get(direction).add(CORNERS.TOP_RIGHT);
                                }
                            }
                        }
                    }
                }
            }
        }

        return currentData;
    }

    @Override
    public boolean usesBlockLight() {
        return mainModel.usesBlockLight();
    }

    @Override
    public boolean useAmbientOcclusion () {
        return mainModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d () {
        return mainModel.isGui3d();
    }

    @Override
    public boolean isCustomRenderer () {
        return mainModel.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon () {
        return mainModel.getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides () {
        return mainModel.getOverrides();
    }

    public static void onModelBake(ModelEvent.BakingCompleted event) {
        override(event, BzBlocks.POROUS_HONEYCOMB.get(), PorousHoneycombBlockModel::new);
    }

    private static void override(ModelEvent.BakingCompleted event, Block block, Function<BakedModel, PorousHoneycombBlockModel> f) {
        for (BlockState state : block.getStateDefinition().getPossibleStates()) {
            ModelResourceLocation loc = BlockModelShaper.stateToModelLocation(state);
            BakedModel model = event.getModels().get(loc);
            if (model != null) {
                event.getModels().put(loc, f.apply(model));
            }
        }
    }
}