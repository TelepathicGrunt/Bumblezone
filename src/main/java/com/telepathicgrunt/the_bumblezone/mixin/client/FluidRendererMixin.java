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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LiquidBlockRenderer.class)
public class FluidRendererMixin {
    
    @Unique
    BlockPos bz_fluidBlockPos = new BlockPos(0, 0, 0);
    
    @Unique
    FluidState bz_fluidState = null;
    
    @Inject(method = "tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/material/FluidState;)Z",
            at = @At(value = "HEAD"))
    private void thebumblezone_storeParamInfo(BlockAndTintGetter blockAndTintGetter, BlockPos blockPos, VertexConsumer vertexConsumer, FluidState fluidState, CallbackInfoReturnable<Boolean> cir) {
        bz_fluidBlockPos = blockPos;
        bz_fluidState = fluidState;
    }

    // make honey fluid render with correct height for bottom layer amount
    @ModifyArg(method = "tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/material/FluidState;)Z",
            index = 2,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;vertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDFFFFFFI)V", ordinal = 0),
            slice = @Slice(from = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;)I", ordinal = 1),
                    to = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;)I", ordinal = 2)))
    private double thebumblezone_changeFluidHeight1(double bottomY) {
        return HoneyFluid.setBottomFluidHeight(bottomY, bz_fluidBlockPos, bz_fluidState);
    }

    @ModifyArg(method = "tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/material/FluidState;)Z",
            index = 2,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;vertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDFFFFFFI)V", ordinal = 1),
            slice = @Slice(from = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;)I", ordinal = 1),
                    to = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;)I", ordinal = 2)))
    private double thebumblezone_changeFluidHeight2(double bottomY) {
        return HoneyFluid.setBottomFluidHeight(bottomY, bz_fluidBlockPos, bz_fluidState);
    }

    @ModifyArg(method = "tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/material/FluidState;)Z",
            index = 2,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;vertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDFFFFFFI)V", ordinal = 2),
            slice = @Slice(from = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;)I", ordinal = 1),
                    to = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;)I", ordinal = 2)))
    private double thebumblezone_changeFluidHeight3(double bottomY) {
        return HoneyFluid.setBottomFluidHeight(bottomY, bz_fluidBlockPos, bz_fluidState);
    }

    @ModifyArg(method = "tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/material/FluidState;)Z",
            index = 2,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;vertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDFFFFFFI)V", ordinal = 3),
            slice = @Slice(from = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;)I", ordinal = 1),
                    to = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;)I", ordinal = 2)))
    private double thebumblezone_changeFluidHeight4(double bottomY) {
        return HoneyFluid.setBottomFluidHeight(bottomY, bz_fluidBlockPos, bz_fluidState);
    }

    @ModifyArg(method = "tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/material/FluidState;)Z",
            index = 2,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;vertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDFFFFFFI)V", ordinal = 2),
            slice = @Slice(from = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/BlockPos;relative(Lnet/minecraft/core/Direction;)Lnet/minecraft/core/BlockPos;")))
    private double thebumblezone_changeFluidHeight5(double bottomY) {
        return HoneyFluid.setBottomFluidHeight(bottomY, bz_fluidBlockPos, bz_fluidState);
    }

    @ModifyArg(method = "tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/material/FluidState;)Z",
            index = 2,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;vertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDFFFFFFI)V", ordinal = 3),
            slice = @Slice(from = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/BlockPos;relative(Lnet/minecraft/core/Direction;)Lnet/minecraft/core/BlockPos;")))
    private double thebumblezone_changeFluidHeight6(double bottomY) {
        return HoneyFluid.setBottomFluidHeight(bottomY, bz_fluidBlockPos, bz_fluidState);
    }

    @ModifyArg(method = "tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/material/FluidState;)Z",
            index = 2,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;vertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDFFFFFFI)V", ordinal = 4),
            slice = @Slice(from = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/BlockPos;relative(Lnet/minecraft/core/Direction;)Lnet/minecraft/core/BlockPos;")))
    private double thebumblezone_changeFluidHeight7(double bottomY) {
        return HoneyFluid.setBottomFluidHeight(bottomY, bz_fluidBlockPos, bz_fluidState);
    }

    @ModifyArg(method = "tesselate(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/level/material/FluidState;)Z",
            index = 2,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/block/LiquidBlockRenderer;vertex(Lcom/mojang/blaze3d/vertex/VertexConsumer;DDDFFFFFFI)V", ordinal = 5),
            slice = @Slice(from = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/BlockPos;relative(Lnet/minecraft/core/Direction;)Lnet/minecraft/core/BlockPos;")))
    private double thebumblezone_changeFluidHeight8(double bottomY) {
        return HoneyFluid.setBottomFluidHeight(bottomY, bz_fluidBlockPos, bz_fluidState);
    }


    //////////////////////////////////////////



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