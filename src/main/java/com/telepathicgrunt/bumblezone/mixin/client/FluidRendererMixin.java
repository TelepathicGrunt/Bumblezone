package com.telepathicgrunt.bumblezone.mixin.client;

import com.telepathicgrunt.bumblezone.blocks.HoneyFluidBlock;
import com.telepathicgrunt.bumblezone.fluids.HoneyFluid;
import com.telepathicgrunt.bumblezone.tags.BzFluidTags;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidRenderer.class)
public class FluidRendererMixin {

    // make honey fluid flow downward slower
    @ModifyVariable(method = "render(Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/fluid/FluidState;)Z",
            at = @At(value = "INVOKE", target = "net/minecraft/client/texture/Sprite.getMinU()F",
            ordinal = 1, shift = At.Shift.BY, by = -6),
            ordinal = 11)
    private float thebumblezone_changeFluidHeight(float fluidBottomHeight, BlockRenderView blockDisplayReader, BlockPos blockPos, VertexConsumer vertexBuilder, FluidState fluidState) {
        if(fluidState.isIn(BzFluidTags.BZ_HONEY_FLUID)) {
            return fluidState.isStill() ? 0f : fluidState.get(HoneyFluidBlock.BOTTOM_LEVEL) / 8f;
        }
        return fluidBottomHeight;
    }


    // make honey fluid not cull faces
    @Inject(method = "isSameFluid(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Lnet/minecraft/fluid/FluidState;)Z",
            at = @At(value = "HEAD"), cancellable = true)
    private static void thebumblezone_honeyFluidCulling(BlockView world, BlockPos blockPos, Direction direction, FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
        if(fluidState.getFluid().isIn(BzFluidTags.BZ_HONEY_FLUID)) {
            if(HoneyFluid.shouldNotCullSide(world, blockPos, direction, fluidState)) {
                cir.setReturnValue(false);
            }
        }
    }

    // make honey fluid have correct height when falling
    @Inject(method = "getNorthWestCornerFluidHeight(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/fluid/Fluid;)F",
            at = @At(value = "HEAD"), cancellable = true)
    private void thebumblezone_honeyFluidHeight(BlockView world, BlockPos blockPos, Fluid fluid, CallbackInfoReturnable<Float> cir) {
        if(fluid.isIn(BzFluidTags.BZ_HONEY_FLUID)) {
            cir.setReturnValue(HoneyFluid.getHoneyFluidHeight(world, blockPos, fluid));
        }
    }
}