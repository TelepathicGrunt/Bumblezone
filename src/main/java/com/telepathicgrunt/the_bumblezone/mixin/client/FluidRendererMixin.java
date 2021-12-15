package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluid;
import com.telepathicgrunt.the_bumblezone.tags.BzFluidTags;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LiquidBlockRenderer.class)
public class FluidRendererMixin {

    // make honey fluid flow downward slower
    @ModifyVariable(method = "tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/material/FluidState;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;getU0()F",
            ordinal = 1, shift = At.Shift.BY, by = -6),
            ordinal = 13)
    private float thebumblezone_changeFluidHeight(float fluidBottomHeight, BlockAndTintGetter blockDisplayReader, BlockPos blockPos, VertexConsumer vertexBuilder, FluidState fluidState) {
        if(fluidState.is(BzFluidTags.BZ_HONEY_FLUID)) {
            return fluidState.isSource() ? 0f : fluidState.getValue(HoneyFluidBlock.BOTTOM_LEVEL) / 8f;
        }
        return fluidBottomHeight;
    }


    // make honey fluid not cull faces
    @Inject(method = "isNeighborSameFluid(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/world/level/material/FluidState;)Z",
            at = @At(value = "HEAD"), cancellable = true)
    private static void thebumblezone_honeyFluidCulling(BlockGetter world, BlockPos blockPos, Direction direction, FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
        if(fluidState.getType().is(BzFluidTags.BZ_HONEY_FLUID)) {
            if(HoneyFluid.shouldNotCullSide(world, blockPos, direction, fluidState)) {
                cir.setReturnValue(false);
            }
        }
    }

    // make honey fluid have correct height when falling
    @Inject(method = "getWaterHeight(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/material/Fluid;)F",
            at = @At(value = "HEAD"), cancellable = true)
    private void thebumblezone_honeyFluidHeight(BlockGetter world, BlockPos blockPos, Fluid fluid, CallbackInfoReturnable<Float> cir) {
        if(fluid.is(BzFluidTags.BZ_HONEY_FLUID)) {
            cir.setReturnValue(HoneyFluid.getHoneyFluidHeight(world, blockPos, fluid));
        }
    }
}