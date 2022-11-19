package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluid;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LiquidBlockRenderer.class)
public class FluidRendererMixin {

    @ModifyVariable(method = "tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)V",
            at = @At(value = "STORE", ordinal = 1),
            ordinal = 13)
    private float thebumblezone_honeyLikeFluidChangeFluidHeight(float fluidBottomHeight, BlockAndTintGetter blockDisplayReader, BlockPos blockPos, VertexConsumer vertexBuilder, BlockState blockState, FluidState fluidState) {
        if(fluidState.is(BzTags.SPECIAL_HONEY_LIKE)) {
            return fluidState.isSource() ? 0f : fluidState.getValue(HoneyFluidBlock.BOTTOM_LEVEL) / 8f;
        }
        return fluidBottomHeight;
    }

    @ModifyExpressionValue(method = "tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;isFaceOccludedByNeighbor(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;FLnet/minecraft/world/level/block/state/BlockState;)Z", ordinal = 0))
    private boolean thebumblezone_honeyLikeFluidCullBottomFace(boolean willCullSide, BlockAndTintGetter blockDisplayReader, BlockPos blockPos, VertexConsumer vertexBuilder, BlockState blockState, FluidState fluidState) {
        if(fluidState.is(BzTags.SPECIAL_HONEY_LIKE)) {
            return willCullSide && !HoneyFluid.shouldRenderSide(blockDisplayReader, blockPos, Direction.DOWN, fluidState);
        }
        return willCullSide;
    }

    @ModifyExpressionValue(method = "tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;isNeighborSameFluid(Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/world/level/material/FluidState;)Z", ordinal = 0))
    private boolean thebumblezone_honeyLikeFluidRenderAboveFace1(boolean willCullAbove, BlockAndTintGetter blockDisplayReader, BlockPos blockPos, VertexConsumer vertexBuilder, BlockState blockState, FluidState fluidState) {
        if(fluidState.is(BzTags.SPECIAL_HONEY_LIKE)) {
            return willCullAbove && !HoneyFluid.shouldRenderSide(blockDisplayReader, blockPos, Direction.UP, fluidState);
        }
        return willCullAbove;
    }

    @ModifyExpressionValue(method = "tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/material/FluidState;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;isFaceOccludedByNeighbor(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;FLnet/minecraft/world/level/block/state/BlockState;)Z", ordinal = 1))
    private boolean thebumblezone_honeyLikeFluidRenderAboveFace2(boolean willCullAbove, BlockAndTintGetter blockDisplayReader, BlockPos blockPos, VertexConsumer vertexBuilder, BlockState blockState, FluidState fluidState) {
        if(fluidState.is(BzTags.SPECIAL_HONEY_LIKE)) {
            return willCullAbove && !HoneyFluid.shouldRenderSide(blockDisplayReader, blockPos, Direction.UP, fluidState);
        }
        return willCullAbove;
    }

    //////////////////////////////////////////

    // make honey fluid and royal jelly have proper face culling
    @ModifyReturnValue(method = "shouldRenderFace(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/material/FluidState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;Lnet/minecraft/world/level/material/FluidState;)Z",
            at = @At(value = "RETURN"))
    private static boolean thebumblezone_honeyLikeFluidCulling(boolean cullFace, BlockAndTintGetter world, BlockPos blockPos, FluidState fluidState, BlockState blockState, Direction direction, FluidState fluidState2) {
        if(fluidState.is(BzTags.SPECIAL_HONEY_LIKE)) {
            return HoneyFluid.shouldRenderSide(world, blockPos, direction, fluidState);
        }
        return cullFace;
    }
}