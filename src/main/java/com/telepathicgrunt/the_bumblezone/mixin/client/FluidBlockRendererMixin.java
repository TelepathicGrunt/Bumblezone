package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluid;
import com.telepathicgrunt.the_bumblezone.tags.BzFluidTags;
import net.minecraft.client.renderer.FluidBlockRenderer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidBlockRenderer.class)
public class FluidBlockRendererMixin {

    // make honey fluid flow downward slower
    @ModifyVariable(method = "tesselate(Lnet/minecraft/world/IBlockDisplayReader;Lnet/minecraft/util/math/BlockPos;Lcom/mojang/blaze3d/vertex/IVertexBuilder;Lnet/minecraft/fluid/FluidState;)Z",
            at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/texture/TextureAtlasSprite.getU0 ()F",
                    ordinal = 1, shift = At.Shift.BY, by = -6),
            ordinal = 13)
    private float thebumblezone_changeFluidHeight(float fluidBottomHeight, IBlockDisplayReader blockDisplayReader, BlockPos blockPos, IVertexBuilder vertexBuilder, FluidState fluidState) {
        if(fluidState.is(BzFluidTags.BZ_HONEY_FLUID)) {
            return fluidState.isSource() ? 0f : fluidState.getValue(HoneyFluidBlock.BOTTOM_LEVEL) / 8f;
        }
        return fluidBottomHeight;
    }


    // make honey fluid not cull faces
    @Inject(method = "isNeighborSameFluid(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/Direction;Lnet/minecraft/fluid/FluidState;)Z",
            at = @At(value = "HEAD"), cancellable = true)
    private static void thebumblezone_honeyFluidCulling(IBlockReader world, BlockPos blockPos, Direction direction, FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
        if(fluidState.getType().is(BzFluidTags.BZ_HONEY_FLUID)) {
            if(HoneyFluid.shouldNotCullSide(world, blockPos, direction, fluidState)) {
                cir.setReturnValue(false);
            }
        }
    }

    // make honey fluid have correct height when falling
    @Inject(method = "getWaterHeight(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/fluid/Fluid;)F",
            at = @At(value = "HEAD"), cancellable = true)
    private void thebumblezone_honeyFluidHeight(IBlockReader world, BlockPos blockPos, Fluid fluid, CallbackInfoReturnable<Float> cir) {
        if(fluid.is(BzFluidTags.BZ_HONEY_FLUID)) {
            cir.setReturnValue(HoneyFluid.getHoneyFluidHeight(world, blockPos, fluid));
        }
    }
}