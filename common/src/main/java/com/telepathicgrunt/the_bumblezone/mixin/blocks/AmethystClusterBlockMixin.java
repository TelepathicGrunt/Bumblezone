package com.telepathicgrunt.the_bumblezone.mixin.blocks;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = AmethystClusterBlock.class, priority = 1200)
public class AmethystClusterBlockMixin {

    @ModifyExpressionValue(method = "getStateForPlacement(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;getFluidState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/material/FluidState;"),
            require = 1)
    private FluidState bumblezone$waterlogWhenPlacedIntoSugarWater(FluidState fluid) {
        if(fluid.is(BzTags.SUGAR_WATER_FLUID) && GeneralUtils.isBlockAllowedForSugarWaterWaterlogging(((AmethystClusterBlock)(Object)this).defaultBlockState())) {
            return Fluids.WATER.defaultFluidState();
        }
        return fluid;
    }
}
