package com.telepathicgrunt.the_bumblezone.client.rendering.fluids;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluid;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CardinalLighting;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import static com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidBlock.BOTTOM_LEVEL;

public class HoneyFluidRendering {

    public static void renderSpecialHoneyFluid(
            BlockPos blockPos,
            BlockAndTintGetter level,
            VertexConsumer builder,
            BlockState blockState,
            FluidState fluidState,
            BzDiagonalClientFluidProperties clientFluidProperties)
    {
        BlockState aboveState = level.getBlockState(blockPos.relative(Direction.UP));
        BlockState northState = level.getBlockState(blockPos.relative(Direction.NORTH));
        BlockState southState = level.getBlockState(blockPos.relative(Direction.SOUTH));
        BlockState westState = level.getBlockState(blockPos.relative(Direction.WEST));
        BlockState eastState = level.getBlockState(blockPos.relative(Direction.EAST));

        FluidState aboveFluid = aboveState.getFluidState();
        FluidState northFluid = northState.getFluidState();
        FluidState southFluid = southState.getFluidState();
        FluidState westFluid = westState.getFluidState();
        FluidState eastFluid = eastState.getFluidState();

        boolean isNotSameFluidAbove = !isNeighborSameFluid(fluidState, aboveFluid);
        boolean shouldRenderUp = HoneyFluid.shouldRenderSide(level, blockPos, Direction.UP, fluidState);

        boolean renderNorth = HoneyFluid.shouldRenderSide(level, blockPos, Direction.NORTH, fluidState);
        boolean renderSouth = HoneyFluid.shouldRenderSide(level, blockPos, Direction.SOUTH, fluidState);
        boolean renderWest = HoneyFluid.shouldRenderSide(level, blockPos, Direction.WEST, fluidState);
        boolean renderEast = HoneyFluid.shouldRenderSide(level, blockPos, Direction.EAST, fluidState);
        boolean renderDown = HoneyFluid.shouldRenderSide(level, blockPos, Direction.DOWN, fluidState);
        
        if (isNotSameFluidAbove || shouldRenderUp || renderDown || renderEast || renderWest || renderNorth || renderSouth) {
            CardinalLighting cardinalLighting = level.cardinalLighting();
            AtlasManager atlasManager = Minecraft.getInstance().getAtlasManager();
            Fluid fluid = fluidState.getType();
            float n = getHeight(level, fluid, blockPos, blockState, fluidState);
            float o;
            float p;
            float q;
            float r;

            if (n >= 1.0F) {
                o = 1.0F;
                p = 1.0F;
                q = 1.0F;
                r = 1.0F;
            }
            else {
                float s = getHeight(level, fluid, blockPos.north(), northState, northFluid);
                float t = getHeight(level, fluid, blockPos.south(), southState, southFluid);
                float u = getHeight(level, fluid, blockPos.east(), eastState, eastFluid);
                float v = getHeight(level, fluid, blockPos.west(), westState, westFluid);
                o = calculateAverageHeight(level, fluid, n, s, u, blockPos.relative(Direction.NORTH).relative(Direction.EAST));
                p = calculateAverageHeight(level, fluid, n, s, v, blockPos.relative(Direction.NORTH).relative(Direction.WEST));
                q = calculateAverageHeight(level, fluid, n, t, u, blockPos.relative(Direction.SOUTH).relative(Direction.EAST));
                r = calculateAverageHeight(level, fluid, n, t, v, blockPos.relative(Direction.SOUTH).relative(Direction.WEST));
            }

            float renderX = blockPos.getX() & 15;
            float renderY = blockPos.getY() & 15;
            float renderZ = blockPos.getZ() & 15;
            float y = fluidState.isSource() ? 0f : fluidState.getValue(HoneyFluidBlock.BOTTOM_LEVEL) / 8f;
            float u1;
            float u2;
            float u3;
            float u4;
            float v1;
            float v2;
            float v3;
            float v4;

            if ((isNotSameFluidAbove && !isFaceOccludedByNeighbor(level, blockPos, Direction.UP, Math.min(Math.min(p, r), Math.min(q, o)), aboveState))
                || shouldRenderUp)
            {
                p -= 0.001F;
                r -= 0.001F;
                q -= 0.001F;
                o -= 0.001F;
                Vec3 vec3 = fluidState.getFlow(level, blockPos);
                TextureAtlasSprite textureAtlasSprite;
                float ah;
                float ai;
                if (vec3.x == 0.0 && vec3.z == 0.0) {
                    textureAtlasSprite = atlasManager.get(clientFluidProperties.stillSpriteId());
                    u1 = textureAtlasSprite.getU(0.0F);
                    v1 = textureAtlasSprite.getV(0.0F);
                    u2 = u1;
                    v2 = textureAtlasSprite.getV(1.0F);
                    u3 = textureAtlasSprite.getU(1.0F);
                    v3 = v2;
                    u4 = u3;
                    v4 = v1;
                }
                else {
                    boolean isDiagonal = vec3.x % 1.0 != 0.0 || vec3.z % 1.0 != 0.0;
                    float sizing = 1;
                    if (isDiagonal) {
                        textureAtlasSprite = atlasManager.get(clientFluidProperties.flowingDiagonalSpriteId());
                        if (Math.abs(vec3.x()) == Math.abs(vec3.z())) {
                            sizing = 1.415f;
                        }
                        else {
                            sizing = 1.215f;
                        }
                    }
                    else {
                        textureAtlasSprite = atlasManager.get(clientFluidProperties.flowingSpriteId());
                    }

                    ah = (float) Mth.atan2(vec3.z, vec3.x) - 1.5707964F;
                    ai = Mth.sin(ah) * 0.25F * sizing;
                    float aj = Mth.cos(ah) * 0.25F * sizing;
                    u1 = textureAtlasSprite.getU(0.5F + (-aj - ai));
                    v1 = textureAtlasSprite.getV(0.5F - aj + ai);
                    u2 = textureAtlasSprite.getU(0.5F - aj + ai);
                    v2 = textureAtlasSprite.getV(0.5F + aj + ai);
                    u3 = textureAtlasSprite.getU(0.5F + aj + ai);
                    v3 = textureAtlasSprite.getV(0.5F + (aj - ai));
                    u4 = textureAtlasSprite.getU(0.5F + (aj - ai));
                    v4 = textureAtlasSprite.getV(0.5F + (-aj - ai));
                }

                int topLightCoords = getLightCoords(level, blockPos);
                int topColor = ARGB.scaleRGB(-1, cardinalLighting.up());
                addFace(builder,
                        renderX + 0.0F,
                        renderY + p,
                        renderZ + 0.0F,
                        u1,
                        v1,
                        renderX + 0.0F,
                        renderY + r,
                        renderZ + 1.0F,
                        u2,
                        v2,
                        renderX + 1.0F,
                        renderY + q,
                        renderZ + 1.0F,
                        u3,
                        v3,
                        renderX + 1.0F,
                        renderY + o,
                        renderZ + 0.0F,
                        u4,
                        v4,
                        topColor,
                        topLightCoords,
                        fluidState.shouldRenderBackwardUpFace(level, blockPos.above())
                );
            }

            if (renderDown) {
                TextureAtlasSprite stillSprite = atlasManager.get(clientFluidProperties.stillSpriteId());
                u1 = stillSprite.getU0();
                u2 = stillSprite.getU1();
                u3 = stillSprite.getV0();
                u4 = stillSprite.getV1();
                int belowLightCoords = getLightCoords(level, blockPos.below());
                int belowColor = ARGB.scaleRGB(-1, cardinalLighting.down());
                addFace(builder,
                        renderX + 0.0F,
                        renderY + y,
                        renderZ + 1.0F,
                        u1,
                        u4,
                        renderX + 0.0F,
                        renderY + y,
                        renderZ + 0.0F,
                        u1,
                        u3,
                        renderX + 1.0F,
                        renderY + y,
                        renderZ + 0.0F,
                        u2,
                        u3,
                        renderX + 1.0F,
                        renderY + y,
                        renderZ + 1.0F,
                        u2,
                        u4,
                        belowColor,
                        belowLightCoords,
                        false
                );
            }

            int sideLightCoords = getLightCoords(level, blockPos);

            for(Direction direction : Direction.Plane.HORIZONTAL) {
                float ar;
                float at;
                float as;
                float au;
                boolean renderSide;

                switch (direction) {
                    case NORTH -> {
                        u4 = p;
                        v1 = o;
                        ar = renderX;
                        as = renderX + 1.0F;
                        at = renderZ + 0.001F;
                        au = renderZ + 0.001F;
                        renderSide = renderNorth;
                    }
                    case SOUTH -> {
                        u4 = q;
                        v1 = r;
                        ar = renderX + 1.0F;
                        as = renderX;
                        at = renderZ + 1.0F - 0.001F;
                        au = renderZ + 1.0F - 0.001F;
                        renderSide = renderSouth;
                    }
                    case WEST -> {
                        u4 = r;
                        v1 = p;
                        ar = renderX + 0.001F;
                        as = renderX + 0.001F;
                        at = renderZ + 1.0F;
                        au = renderZ;
                        renderSide = renderWest;
                    }
                    default -> {
                        u4 = o;
                        v1 = q;
                        ar = renderX + 1.0F - 0.001F;
                        as = renderX + 1.0F - 0.001F;
                        at = renderZ;
                        au = renderZ + 1.0F;
                        renderSide = renderEast;
                    }
                }

                if (renderSide) {
                    BlockPos blockPos2 = blockPos.relative(direction);
                    TextureAtlasSprite textureAtlasSprite2 = atlasManager.get(clientFluidProperties.flowingSpriteId());
                    Block block = level.getBlockState(blockPos2).getBlock();
                    boolean stillSide = false;
                    if (block instanceof HalfTransparentBlock || block instanceof LeavesBlock) {
                        textureAtlasSprite2 = atlasManager.get(clientFluidProperties.stillSpriteId());
                        stillSide = true;
                    }

                    float av = textureAtlasSprite2.getU(0.0F);
                    float aw = textureAtlasSprite2.getU(stillSide ? 1f : 0.5f);
                    float ax = textureAtlasSprite2.getV((1.0F - u4) * 0.5F);
                    float ay = textureAtlasSprite2.getV((1.0F - v1) * 0.5F);
                    float az = textureAtlasSprite2.getV((y == 0 ? 0.5F : 0.5F * (1 - y)) * (stillSide ? 2f : 1f));
                    float shadeSide = direction.getAxis() == Direction.Axis.Z ? cardinalLighting.north() : cardinalLighting.west();
                    int faceColor = ARGB.scaleRGB(-1, cardinalLighting.up() * shadeSide);
                    addFace(builder,
                            ar,
                            renderY + u4,
                            at,
                            av,
                            ax,
                            as,
                            renderY + v1,
                            au,
                            aw,
                            ay,
                            as,
                            renderY + y,
                            au,
                            aw,
                            az,
                            ar,
                            renderY + y,
                            at,
                            av,
                            az,
                            faceColor,
                            sideLightCoords,
                            !stillSide
                    );
                }
            }
        }
    }

    private static void addFace(
            final VertexConsumer builder,
            final float x0,
            final float y0,
            final float z0,
            final float u0,
            final float v0,
            final float x1,
            final float y1,
            final float z1,
            final float u1,
            final float v1,
            final float x2,
            final float y2,
            final float z2,
            final float u2,
            final float v2,
            final float x3,
            final float y3,
            final float z3,
            final float u3,
            final float v3,
            final int color,
            final int lightCoords,
            final boolean addBackFace
    ) {
        vertex(builder, x0, y0, z0, color, u0, v0, lightCoords);
        vertex(builder, x1, y1, z1, color, u1, v1, lightCoords);
        vertex(builder, x2, y2, z2, color, u2, v2, lightCoords);
        vertex(builder, x3, y3, z3, color, u3, v3, lightCoords);
        if (addBackFace) {
            vertex(builder, x3, y3, z3, color, u3, v3, lightCoords);
            vertex(builder, x2, y2, z2, color, u2, v2, lightCoords);
            vertex(builder, x1, y1, z1, color, u1, v1, lightCoords);
            vertex(builder, x0, y0, z0, color, u0, v0, lightCoords);
        }
    }

    private static void vertex(final VertexConsumer builder, final float x, final float y, final float z, final int color, final float u, final float v, final int lightCoords)
    {
        builder.addVertex(x, y, z, color, u, v, OverlayTexture.NO_OVERLAY, lightCoords, 0.0F, 1.0F, 0.0F);
    }

    private static boolean isNeighborSameFluid(FluidState fluidState, FluidState fluidState2) {
        return fluidState2.getType().isSame(fluidState.getType());
    }

    private static boolean isFaceOccludedByNeighbor(BlockGetter blockGetter, BlockPos blockPos, Direction direction, float f, BlockState blockState) {
        return isFaceOccludedByState(blockGetter, direction, f, blockPos.relative(direction), blockState);
    }

    private static boolean isFaceOccludedByState(BlockGetter blockGetter, Direction direction, float f, BlockPos blockPos, BlockState blockState) {
        if (blockState.canOcclude()) {
            VoxelShape voxelShape = Shapes.box(0.0, 0.0, 0.0, 1.0, f, 1.0);
            VoxelShape voxelShape2 = blockState.getOcclusionShape();
            return Shapes.blockOccludes(voxelShape, voxelShape2, direction);
        }
        else {
            return blockState.getBlock() instanceof HoneyFluidBlock honeyFluidBlock && honeyFluidBlock.getFluidState(blockState).is(BzTags.SPECIAL_HONEY_LIKE);
        }
    }

    private static int getLightCoords(final BlockAndTintGetter level, final BlockPos pos) {
        return LightCoordsUtil.max(LevelRenderer.getLightCoords(level, pos), LevelRenderer.getLightCoords(level, pos.above()));
    }

    private static float calculateAverageHeight(BlockAndLightGetter blockAndTintGetter, Fluid fluid, float f, float g, float h, BlockPos blockPos) {
        if (!(h >= 1.0F) && !(g >= 1.0F)) {
            float[] fs = new float[2];
            if (h > 0.0F || g > 0.0F) {
                float i = getHeight(blockAndTintGetter, fluid, blockPos);
                if (i >= 1.0F) {
                    return 1.0F;
                }

                addWeightedHeight(fs, i);
            }

            addWeightedHeight(fs, f);
            addWeightedHeight(fs, h);
            addWeightedHeight(fs, g);
            return fs[0] / fs[1];
        }
        else {
            return 1.0F;
        }
    }

    private static void addWeightedHeight(float[] fs, float f) {
        if (f >= 0.8F) {
            fs[0] += f * 10.0F;
            fs[1] += 10.0F;
        }
        else if (f >= 0.0F) {
            fs[0] += f;
            fs[1]++;
        }
    }

    private static float getHeight(BlockAndLightGetter blockAndTintGetter, Fluid fluid, BlockPos blockPos) {
        BlockState blockState = blockAndTintGetter.getBlockState(blockPos);
        return getHeight(blockAndTintGetter, fluid, blockPos, blockState, blockState.getFluidState());
    }

    private static float getHeight(BlockAndLightGetter blockAndTintGetter, Fluid fluid, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        if (fluid.isSame(fluidState.getType())) {
            FluidState aboveFluidState = blockAndTintGetter.getFluidState(blockPos.above());

            boolean aboveFluidIsThisFluid =
                    !aboveFluidState.isEmpty() &&
                        aboveFluidState.getType().isSame(fluid) &&
                        (aboveFluidState.isSource() || !aboveFluidState.is(BzTags.SPECIAL_HONEY_LIKE) || aboveFluidState.getValue(BOTTOM_LEVEL) == 0);

            return aboveFluidIsThisFluid ? 1.0f : fluidState.getOwnHeight();
        }
        else {
            return !blockState.isSolid() ? 0.0F : -1.0F;
        }
    }
}
