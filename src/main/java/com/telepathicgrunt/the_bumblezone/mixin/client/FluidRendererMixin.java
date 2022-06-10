package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluid;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LiquidBlockRenderer.class)
public class FluidRendererMixin {

    // REPLACE WITH modifyArgs mixins WHEN IT WORKS AGAIN IN FORGE.
    // DO NOT USE modifyArg WITH STORING VALUES IN FIELDS AS THAT ISN'T THREADSAFE AND WILL MESS UP RENDERING HONEY FLUID
    // DO NOT ASK HOW I KNOW.
    // make honey fluid flow downward slower
    @ModifyVariable(method = "tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;isFaceOccludedByNeighbor(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;FLnet/minecraft/world/level/block/state/BlockState;)Z",
                    ordinal = 1, shift = At.Shift.BY, by = -13),
            ordinal = 14)
    private float thebumblezone_changeFluidHeight(float fluidBottomHeight, BlockAndTintGetter blockDisplayReader, BlockPos blockPos, VertexConsumer vertexBuilder, BlockState blockState, FluidState fluidState) {
        if(fluidState.is(BzTags.BZ_HONEY_FLUID)) {
            return fluidState.isSource() ? 0f : fluidState.getValue(HoneyFluidBlock.BOTTOM_LEVEL) / 8f;
        }
        return fluidBottomHeight;
    }

    @ModifyVariable(method = "tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;shouldRenderFace(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/world/level/material/FluidState;)Z",
                    ordinal = 1, shift = At.Shift.BEFORE),
            ordinal = 2)
    private boolean thebumblezone_cullBottom(boolean showBottom, BlockAndTintGetter blockDisplayReader, BlockPos blockPos, VertexConsumer vertexBuilder, BlockState blockState, FluidState fluidState) {
        if(fluidState.is(BzTags.BZ_HONEY_FLUID) && !fluidState.isSource()) {
            return showBottom || fluidState.getValue(HoneyFluidBlock.BOTTOM_LEVEL) != 8;
        }
        return showBottom;
    }


    //////////////////////////////////////////

    // make honey fluid not cull faces
    @Inject(method = "shouldRenderFace(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/world/level/material/FluidState;)Z",
            at = @At(value = "HEAD"), cancellable = true)
    private static void thebumblezone_honeyFluidCulling(BlockAndTintGetter world, BlockPos blockPos, FluidState fluidState, BlockState blockState, Direction direction, FluidState fluidState2, CallbackInfoReturnable<Boolean> cir) {
        if(fluidState.getType().is(BzTags.BZ_HONEY_FLUID)) {
            if(HoneyFluid.shouldNotCullSide(world, blockPos, direction, fluidState)) {
                cir.setReturnValue(false);
            }
        }
    }
}