package com.telepathicgrunt.the_bumblezone.client.rendering.fluids;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluid;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
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
            VertexConsumer vertexConsumer,
            BlockState blockState,
            FluidState fluidState,
            TextureAtlasSprite[] sprites)
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
            float downShade = level.getShade(Direction.DOWN, true);
            float upShade = level.getShade(Direction.UP, true);
            float northShade = level.getShade(Direction.NORTH, true);
            float westShade = level.getShade(Direction.WEST, true);
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

            double renderX = blockPos.getX() & 15;
            double renderY = blockPos.getY() & 15;
            double renderZ = blockPos.getZ() & 15;
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
                    textureAtlasSprite = sprites[0];
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
                        textureAtlasSprite = sprites[3];
                        if (Math.abs(vec3.x()) == Math.abs(vec3.z())) {
                            sizing = 1.415f;
                        }
                        else {
                            sizing = 1.215f;
                        }
                    }
                    else {
                        textureAtlasSprite = sprites[1];
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


                float al = (u1 + u2 + u3 + u4) / 4.0F;
                ah = (v1 + v2 + v3 + v4) / 4.0F;
                ai = sprites[0].uvShrinkRatio();

                u1 = Mth.lerp(ai, u1, al);
                u2 = Mth.lerp(ai, u2, al);
                u3 = Mth.lerp(ai, u3, al);
                u4 = Mth.lerp(ai, u4, al);
                v1 = Mth.lerp(ai, v1, ah);
                v2 = Mth.lerp(ai, v2, ah);
                v3 = Mth.lerp(ai, v3, ah);
                v4 = Mth.lerp(ai, v4, ah);
                int uv2 = getLightColor(level, blockPos);

                vertex(vertexConsumer, renderX + 0.0, renderY + (double)p, renderZ + 0.0, upShade, upShade, upShade, u1, v1, uv2);
                vertex(vertexConsumer, renderX + 0.0, renderY + (double)r, renderZ + 1.0, upShade, upShade, upShade, u2, v2, uv2);
                vertex(vertexConsumer, renderX + 1.0, renderY + (double)q, renderZ + 1.0, upShade, upShade, upShade, u3, v3, uv2);
                vertex(vertexConsumer, renderX + 1.0, renderY + (double)o, renderZ + 0.0, upShade, upShade, upShade, u4, v4, uv2);

                if (fluidState.shouldRenderBackwardUpFace(level, blockPos.above())) {
                    vertex(vertexConsumer, renderX + 0.0, renderY + (double)p, renderZ + 0.0, upShade, upShade, upShade, u1, v1, uv2);
                    vertex(vertexConsumer, renderX + 1.0, renderY + (double)o, renderZ + 0.0, upShade, upShade, upShade, u4, v4, uv2);
                    vertex(vertexConsumer, renderX + 1.0, renderY + (double)q, renderZ + 1.0, upShade, upShade, upShade, u3, v3, uv2);
                    vertex(vertexConsumer, renderX + 0.0, renderY + (double)r, renderZ + 1.0, upShade, upShade, upShade, u2, v2, uv2);
                }
            }

            if (renderDown) {
                u1 = sprites[0].getU0();
                u2 = sprites[0].getU1();
                u3 = sprites[0].getV0();
                u4 = sprites[0].getV1();
                int ap = getLightColor(level, blockPos.below());
                v2 = downShade;
                v3 = downShade;
                v4 = downShade;
                downVertex(vertexConsumer, renderX, renderY + (double)y, renderZ + 1.0, v2, v3, v4, u1, u4, ap);
                downVertex(vertexConsumer, renderX, renderY + (double)y, renderZ, v2, v3, v4, u1, u3, ap);
                downVertex(vertexConsumer, renderX + 1.0, renderY + (double)y, renderZ, v2, v3, v4, u2, u3, ap);
                downVertex(vertexConsumer, renderX + 1.0, renderY + (double)y, renderZ + 1.0, v2, v3, v4, u2, u4, ap);
            }

            int aq = getLightColor(level, blockPos);

            for(Direction direction : Direction.Plane.HORIZONTAL) {
                double ar;
                double at;
                double as;
                double au;
                boolean renderside;

                switch (direction) {
                    case NORTH -> {
                        u4 = p;
                        v1 = o;
                        ar = renderX;
                        as = renderX + 1.0;
                        at = renderZ + 0.0010000000474974513;
                        au = renderZ + 0.0010000000474974513;
                        renderside = renderNorth;
                    }
                    case SOUTH -> {
                        u4 = q;
                        v1 = r;
                        ar = renderX + 1.0;
                        as = renderX;
                        at = renderZ + 1.0 - 0.0010000000474974513;
                        au = renderZ + 1.0 - 0.0010000000474974513;
                        renderside = renderSouth;
                    }
                    case WEST -> {
                        u4 = r;
                        v1 = p;
                        ar = renderX + 0.0010000000474974513;
                        as = renderX + 0.0010000000474974513;
                        at = renderZ + 1.0;
                        au = renderZ;
                        renderside = renderWest;
                    }
                    default -> {
                        u4 = o;
                        v1 = q;
                        ar = renderX + 1.0 - 0.0010000000474974513;
                        as = renderX + 1.0 - 0.0010000000474974513;
                        at = renderZ;
                        au = renderZ + 1.0;
                        renderside = renderEast;
                    }
                }

                if (renderside &&
                    !isFaceOccludedByNeighbor(level, blockPos, direction, Math.max(u4, v1), level.getBlockState(blockPos.relative(direction))))
                {
                    BlockPos blockPos2 = blockPos.relative(direction);
                    TextureAtlasSprite textureAtlasSprite2 = sprites[1];
                    Block block = level.getBlockState(blockPos2).getBlock();
                    if (block instanceof HalfTransparentBlock || block instanceof LeavesBlock) {
                        textureAtlasSprite2 = sprites[0];
                    }

                    float av = textureAtlasSprite2.getU(0.0F);
                    float aw = textureAtlasSprite2.getU(0.5f);
                    float ax = textureAtlasSprite2.getV((1.0F - u4) * 0.5F);
                    float ay = textureAtlasSprite2.getV((1.0F - v1) * 0.5F);
                    float az = textureAtlasSprite2.getV(y == 0 ? 0.5F : 0.5F * (1 - y));
                    float ba = direction.getAxis() == Direction.Axis.Z ? northShade : westShade;
                    float bb = upShade * ba;
                    float bc = upShade * ba;
                    float bd = upShade * ba;

                    vertex(vertexConsumer, ar, renderY + (double)u4, at, bb, bc, bd, av, ax, aq);
                    vertex(vertexConsumer, as, renderY + (double)v1, au, bb, bc, bd, aw, ay, aq);
                    vertex(vertexConsumer, as, renderY + (double)y, au, bb, bc, bd, aw, az, aq);
                    vertex(vertexConsumer, ar, renderY + (double)y, at, bb, bc, bd, av, az, aq);
                    if (textureAtlasSprite2 != sprites[0]) {
                        vertex(vertexConsumer, ar, renderY + (double)y, at, bb, bc, bd, av, az, aq);
                        vertex(vertexConsumer, as, renderY + (double)y, au, bb, bc, bd, aw, az, aq);
                        vertex(vertexConsumer, as, renderY + (double)v1, au, bb, bc, bd, aw, ay, aq);
                        vertex(vertexConsumer, ar, renderY + (double)u4, at, bb, bc, bd, av, ax, aq);
                    }
                }
            }
        }
    }

    private static void vertex(VertexConsumer vertexConsumer, double x, double y, double z, float r, float g, float b, float u, float v, int uv2) {
        vertexConsumer.addVertex((float) x, (float) y, (float) z).setColor(r, g, b, 1.0F).setUv(u, v).setLight(uv2).setNormal(0.0F, 1.0F, 0.0F);
    }


    private static void downVertex(VertexConsumer vertexConsumer, double d, double e, double f, float g, float h, float i, float j, float k, int l) {
        vertexConsumer.addVertex((float) d, (float) e, (float) f).setColor(g, h, i, 1.0F).setUv(j, k).setLight(l).setNormal(0.0F, -1.0F, 0.0F);
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
            VoxelShape voxelShape2 = blockState.getOcclusionShape(blockGetter, blockPos);
            return Shapes.blockOccudes(voxelShape, voxelShape2, direction);
        }
        else {
            return blockState.getBlock() instanceof HoneyFluidBlock honeyFluidBlock && honeyFluidBlock.getFluidState(blockState).is(BzTags.SPECIAL_HONEY_LIKE);
        }
    }

    private static int getLightColor(BlockAndTintGetter blockAndTintGetter, BlockPos blockPos) {
        int i = LevelRenderer.getLightColor(blockAndTintGetter, blockPos);
        int j = LevelRenderer.getLightColor(blockAndTintGetter, blockPos.above());
        int k = i & 255;
        int l = j & 255;
        int m = i >> 16 & 255;
        int n = j >> 16 & 255;
        return (Math.max(k, l)) | (Math.max(m, n)) << 16;
    }

    private static float calculateAverageHeight(BlockAndTintGetter blockAndTintGetter, Fluid fluid, float f, float g, float h, BlockPos blockPos) {
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

    private static float getHeight(BlockAndTintGetter blockAndTintGetter, Fluid fluid, BlockPos blockPos) {
        BlockState blockState = blockAndTintGetter.getBlockState(blockPos);
        return getHeight(blockAndTintGetter, fluid, blockPos, blockState, blockState.getFluidState());
    }

    private static float getHeight(BlockAndTintGetter blockAndTintGetter, Fluid fluid, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
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
