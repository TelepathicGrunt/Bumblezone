package com.telepathicgrunt.the_bumblezone.fluids.fabric;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluid;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.fluids.base.ClientFluidProperties;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.impl.client.rendering.fluid.FluidRenderHandlerRegistryImpl;
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

public class HoneyFluidRenderHandler extends SimpleFluidRenderHandler {

    private final ClientFluidProperties properties;

    public HoneyFluidRenderHandler(ClientFluidProperties properties) {
        super(properties.still(), properties.flowing(), properties.overlay());
        this.properties = properties;
    }

    @Override
    public void renderFluid(BlockPos blockPos, BlockAndTintGetter level, VertexConsumer vertexConsumer, BlockState blockState, FluidState fluidState) {
        if (fluidState.is(BzTags.SPECIAL_HONEY_LIKE)) {
            TextureAtlasSprite[] textureAtlasSprites = sprites;

            BlockState blockState2 = level.getBlockState(blockPos.relative(Direction.DOWN));
            BlockState blockState3 = level.getBlockState(blockPos.relative(Direction.UP));
            FluidState fluidState3 = blockState3.getFluidState();
            BlockState blockState4 = level.getBlockState(blockPos.relative(Direction.NORTH));
            FluidState fluidState4 = blockState4.getFluidState();
            BlockState blockState5 = level.getBlockState(blockPos.relative(Direction.SOUTH));
            FluidState fluidState5 = blockState5.getFluidState();
            BlockState blockState6 = level.getBlockState(blockPos.relative(Direction.WEST));
            FluidState fluidState6 = blockState6.getFluidState();
            BlockState blockState7 = level.getBlockState(blockPos.relative(Direction.EAST));
            FluidState fluidState7 = blockState7.getFluidState();

            boolean sameFluidAbove = !isNeighborSameFluid(fluidState, fluidState3) ||
                    HoneyFluid.shouldRenderSide(level, blockPos, Direction.UP, fluidState);

            boolean renderNorth = HoneyFluid.shouldRenderSide(level, blockPos, Direction.NORTH, fluidState);
            boolean renderSouth = HoneyFluid.shouldRenderSide(level, blockPos, Direction.SOUTH, fluidState);
            boolean renderWest = HoneyFluid.shouldRenderSide(level, blockPos, Direction.WEST, fluidState);
            boolean renderEast = HoneyFluid.shouldRenderSide(level, blockPos, Direction.EAST, fluidState);
            boolean renderDown = HoneyFluid.shouldRenderSide(level, blockPos, Direction.DOWN, fluidState) ||
                    !isFaceOccludedByNeighbor(level, blockPos, Direction.DOWN, 0.8888889F, blockState2);

            if (sameFluidAbove || renderDown || renderEast || renderWest || renderNorth || renderSouth) {
                float j = level.getShade(Direction.DOWN, true);
                float k = level.getShade(Direction.UP, true);
                float l = level.getShade(Direction.NORTH, true);
                float m = level.getShade(Direction.WEST, true);
                Fluid fluid = fluidState.getType();
                float n = this.getHeight(level, fluid, blockPos, blockState, fluidState);
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
                    float s = this.getHeight(level, fluid, blockPos.north(), blockState4, fluidState4);
                    float t = this.getHeight(level, fluid, blockPos.south(), blockState5, fluidState5);
                    float u = this.getHeight(level, fluid, blockPos.east(), blockState7, fluidState7);
                    float v = this.getHeight(level, fluid, blockPos.west(), blockState6, fluidState6);
                    o = this.calculateAverageHeight(level, fluid, n, s, u, blockPos.relative(Direction.NORTH).relative(Direction.EAST));
                    p = this.calculateAverageHeight(level, fluid, n, s, v, blockPos.relative(Direction.NORTH).relative(Direction.WEST));
                    q = this.calculateAverageHeight(level, fluid, n, t, u, blockPos.relative(Direction.SOUTH).relative(Direction.EAST));
                    r = this.calculateAverageHeight(level, fluid, n, t, v, blockPos.relative(Direction.SOUTH).relative(Direction.WEST));
                }

                double d = blockPos.getX() & 15;
                double e = blockPos.getY() & 15;
                double w = blockPos.getZ() & 15;
                float y = fluidState.isSource() ? 0f : fluidState.getValue(HoneyFluidBlock.BOTTOM_LEVEL) / 8f;
                float z;
                float ab;
                float ad;
                float af;
                float aa;
                float ac;
                float ae;
                float ag;

                if (sameFluidAbove &&
                        (!isFaceOccludedByNeighbor(level, blockPos, Direction.UP, Math.min(Math.min(p, r), Math.min(q, o)), blockState3) ||
                        HoneyFluid.shouldRenderSide(level, blockPos, Direction.UP, fluidState)))
                {
                    p -= 0.001F;
                    r -= 0.001F;
                    q -= 0.001F;
                    o -= 0.001F;
                    Vec3 vec3 = fluidState.getFlow(level, blockPos);
                    TextureAtlasSprite textureAtlasSprite;
                    float ah;
                    float ai;
                    float ak;
                    if (vec3.x == 0.0 && vec3.z == 0.0) {
                        textureAtlasSprite = textureAtlasSprites[0];
                        z = textureAtlasSprite.getU(0.0);
                        aa = textureAtlasSprite.getV(0.0);
                        ab = z;
                        ac = textureAtlasSprite.getV(16.0);
                        ad = textureAtlasSprite.getU(16.0);
                        ae = ac;
                        af = ad;
                        ag = aa;
                    }
                    else {
                        textureAtlasSprite = textureAtlasSprites[1];
                        ah = (float) Mth.atan2(vec3.z, vec3.x) - 1.5707964F;
                        ai = Mth.sin(ah) * 0.25F;
                        float aj = Mth.cos(ah) * 0.25F;
                        z = textureAtlasSprite.getU(8.0F + (-aj - ai) * 16.0F);
                        aa = textureAtlasSprite.getV(8.0F + (-aj + ai) * 16.0F);
                        ab = textureAtlasSprite.getU(8.0F + (-aj + ai) * 16.0F);
                        ac = textureAtlasSprite.getV(8.0F + (aj + ai) * 16.0F);
                        ad = textureAtlasSprite.getU(8.0F + (aj + ai) * 16.0F);
                        ae = textureAtlasSprite.getV(8.0F + (aj - ai) * 16.0F);
                        af = textureAtlasSprite.getU(8.0F + (aj - ai) * 16.0F);
                        ag = textureAtlasSprite.getV(8.0F + (-aj - ai) * 16.0F);
                    }

                    float al = (z + ab + ad + af) / 4.0F;
                    ah = (aa + ac + ae + ag) / 4.0F;
                    ai = textureAtlasSprites[0].uvShrinkRatio();
                    z = Mth.lerp(ai, z, al);
                    ab = Mth.lerp(ai, ab, al);
                    ad = Mth.lerp(ai, ad, al);
                    af = Mth.lerp(ai, af, al);
                    aa = Mth.lerp(ai, aa, ah);
                    ac = Mth.lerp(ai, ac, ah);
                    ae = Mth.lerp(ai, ae, ah);
                    ag = Mth.lerp(ai, ag, ah);
                    int am = this.getLightColor(level, blockPos);
                    ak = k;

                    this.vertex(vertexConsumer, d + 0.0, e + (double)p, w + 0.0, ak, k, k, z, aa, am);
                    this.vertex(vertexConsumer, d + 0.0, e + (double)r, w + 1.0, ak, k, k, ab, ac, am);
                    this.vertex(vertexConsumer, d + 1.0, e + (double)q, w + 1.0, ak, k, k, ad, ae, am);
                    this.vertex(vertexConsumer, d + 1.0, e + (double)o, w + 0.0, ak, k, k, af, ag, am);

                    if (fluidState.shouldRenderBackwardUpFace(level, blockPos.above())) {
                        this.vertex(vertexConsumer, d + 0.0, e + (double)p, w + 0.0, ak, k, k, z, aa, am);
                        this.vertex(vertexConsumer, d + 1.0, e + (double)o, w + 0.0, ak, k, k, af, ag, am);
                        this.vertex(vertexConsumer, d + 1.0, e + (double)q, w + 1.0, ak, k, k, ad, ae, am);
                        this.vertex(vertexConsumer, d + 0.0, e + (double)r, w + 1.0, ak, k, k, ab, ac, am);
                    }
                }

                if (renderDown) {
                    z = textureAtlasSprites[0].getU0();
                    ab = textureAtlasSprites[0].getU1();
                    ad = textureAtlasSprites[0].getV0();
                    af = textureAtlasSprites[0].getV1();
                    int ap = this.getLightColor(level, blockPos.below());
                    ac = j;
                    ae = j;
                    ag = j;
                    this.vertex(vertexConsumer, d, e + (double)y, w + 1.0, ac, ae, ag, z, af, ap);
                    this.vertex(vertexConsumer, d, e + (double)y, w, ac, ae, ag, z, ad, ap);
                    this.vertex(vertexConsumer, d + 1.0, e + (double)y, w, ac, ae, ag, ab, ad, ap);
                    this.vertex(vertexConsumer, d + 1.0, e + (double)y, w + 1.0, ac, ae, ag, ab, af, ap);
                }

                int aq = this.getLightColor(level, blockPos);

                for(Direction direction : Direction.Plane.HORIZONTAL) {
                    double ar;
                    double at;
                    double as;
                    double au;
                    boolean renderside;

                    switch (direction) {
                        case NORTH -> {
                            af = p;
                            aa = o;
                            ar = d;
                            as = d + 1.0;
                            at = w + 0.0010000000474974513;
                            au = w + 0.0010000000474974513;
                            renderside = renderNorth;
                        }
                        case SOUTH -> {
                            af = q;
                            aa = r;
                            ar = d + 1.0;
                            as = d;
                            at = w + 1.0 - 0.0010000000474974513;
                            au = w + 1.0 - 0.0010000000474974513;
                            renderside = renderSouth;
                        }
                        case WEST -> {
                            af = r;
                            aa = p;
                            ar = d + 0.0010000000474974513;
                            as = d + 0.0010000000474974513;
                            at = w + 1.0;
                            au = w;
                            renderside = renderWest;
                        }
                        default -> {
                            af = o;
                            aa = q;
                            ar = d + 1.0 - 0.0010000000474974513;
                            as = d + 1.0 - 0.0010000000474974513;
                            at = w;
                            au = w + 1.0;
                            renderside = renderEast;
                        }
                    }

                    if (renderside &&
                        !isFaceOccludedByNeighbor(level, blockPos, direction, Math.max(af, aa), level.getBlockState(blockPos.relative(direction))))
                    {
                        BlockPos blockPos2 = blockPos.relative(direction);
                        TextureAtlasSprite textureAtlasSprite2 = textureAtlasSprites[1];
                        Block block = level.getBlockState(blockPos2).getBlock();
                        if (block instanceof HalfTransparentBlock || block instanceof LeavesBlock) {
                            textureAtlasSprite2 = sprites[0];
                        }

                        float av = textureAtlasSprite2.getU(0.0);
                        float aw = textureAtlasSprite2.getU(8.0);
                        float ax = textureAtlasSprite2.getV((1.0F - af) * 16.0F * 0.5F);
                        float ay = textureAtlasSprite2.getV((1.0F - aa) * 16.0F * 0.5F);
                        float az = textureAtlasSprite2.getV(8.0);
                        float ba = direction.getAxis() == Direction.Axis.Z ? l : m;
                        float bb = k * ba;
                        float bc = k * ba;
                        float bd = k * ba;

                        this.vertex(vertexConsumer, ar, e + (double)af, at, bb, bc, bd, av, ax, aq);
                        this.vertex(vertexConsumer, as, e + (double)aa, au, bb, bc, bd, aw, ay, aq);
                        this.vertex(vertexConsumer, as, e + (double)y, au, bb, bc, bd, aw, az, aq);
                        this.vertex(vertexConsumer, ar, e + (double)y, at, bb, bc, bd, av, az, aq);
                        if (textureAtlasSprite2 != sprites[0]) {
                            this.vertex(vertexConsumer, ar, e + (double)y, at, bb, bc, bd, av, az, aq);
                            this.vertex(vertexConsumer, as, e + (double)y, au, bb, bc, bd, aw, az, aq);
                            this.vertex(vertexConsumer, as, e + (double)aa, au, bb, bc, bd, aw, ay, aq);
                            this.vertex(vertexConsumer, ar, e + (double)af, at, bb, bc, bd, av, ax, aq);
                        }
                    }
                }
            }
        }
        else {
            ((FluidRenderHandlerRegistryImpl) FluidRenderHandlerRegistry.INSTANCE).renderFluid(blockPos, level, vertexConsumer, blockState, fluidState);
        }
    }

    private void vertex(VertexConsumer vertexConsumer, double d, double e, double f, float g, float h, float i, float j, float k, int l) {
        vertexConsumer.vertex(d, e, f).color(g, h, i, 1.0F).uv(j, k).uv2(l).normal(0.0F, 1.0F, 0.0F).endVertex();
    }

    private static boolean isNeighborSameFluid(FluidState fluidState, FluidState fluidState2) {
        return fluidState2.getType().isSame(fluidState.getType());
    }

    private static boolean isFaceOccludedByNeighbor(BlockGetter blockGetter, BlockPos blockPos, Direction direction, float f, BlockState blockState) {
        return isFaceOccludedByState(blockGetter, direction, f, blockPos.relative(direction), blockState);
    }

    private static boolean isFaceOccludedByState(BlockGetter blockGetter, Direction direction, float f, BlockPos blockPos, BlockState blockState) {
        if (blockState.canOcclude()) {
            VoxelShape voxelShape = Shapes.box(0.0, 0.0, 0.0, 1.0, (double)f, 1.0);
            VoxelShape voxelShape2 = blockState.getOcclusionShape(blockGetter, blockPos);
            return Shapes.blockOccudes(voxelShape, voxelShape2, direction);
        } else {
            return false;
        }
    }

    private int getLightColor(BlockAndTintGetter blockAndTintGetter, BlockPos blockPos) {
        int i = LevelRenderer.getLightColor(blockAndTintGetter, blockPos);
        int j = LevelRenderer.getLightColor(blockAndTintGetter, blockPos.above());
        int k = i & 255;
        int l = j & 255;
        int m = i >> 16 & 255;
        int n = j >> 16 & 255;
        return (Math.max(k, l)) | (Math.max(m, n)) << 16;
    }

    private float calculateAverageHeight(BlockAndTintGetter blockAndTintGetter, Fluid fluid, float f, float g, float h, BlockPos blockPos) {
        if (!(h >= 1.0F) && !(g >= 1.0F)) {
            float[] fs = new float[2];
            if (h > 0.0F || g > 0.0F) {
                float i = this.getHeight(blockAndTintGetter, fluid, blockPos);
                if (i >= 1.0F) {
                    return 1.0F;
                }

                this.addWeightedHeight(fs, i);
            }

            this.addWeightedHeight(fs, f);
            this.addWeightedHeight(fs, h);
            this.addWeightedHeight(fs, g);
            return fs[0] / fs[1];
        } else {
            return 1.0F;
        }
    }

    private void addWeightedHeight(float[] fs, float f) {
        if (f >= 0.8F) {
            fs[0] += f * 10.0F;
            fs[1] += 10.0F;
        }
        else if (f >= 0.0F) {
            fs[0] += f;
            fs[1]++;
        }
    }

    private float getHeight(BlockAndTintGetter blockAndTintGetter, Fluid fluid, BlockPos blockPos) {
        BlockState blockState = blockAndTintGetter.getBlockState(blockPos);
        return this.getHeight(blockAndTintGetter, fluid, blockPos, blockState, blockState.getFluidState());
    }

    private float getHeight(BlockAndTintGetter blockAndTintGetter, Fluid fluid, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        if (fluid.isSame(fluidState.getType())) {
            BlockState blockState2 = blockAndTintGetter.getBlockState(blockPos.above());
            return fluid.isSame(blockState2.getFluidState().getType()) ? 1.0F : fluidState.getOwnHeight();
        } else {
            return !blockState.isSolid() ? 0.0F : -1.0F;
        }
    }
}
