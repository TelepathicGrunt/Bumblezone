package com.telepathicgrunt.the_bumblezone.mixin.blocks;


import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(value = PointedDripstoneBlock.class, priority = 1200)
public class PointedDripstoneBlockMixin {

    @ModifyReturnValue(method = "getFluidAboveStalactite(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Ljava/util/Optional;",
            at = @At(value = "RETURN"),
            require = 0)
    private static Optional<PointedDripstoneBlock.FluidInfo> bumblezone$dripstoneSupportSugarWater(Optional<PointedDripstoneBlock.FluidInfo> original) {
        if(original.isPresent()) {
            PointedDripstoneBlock.FluidInfo fluidInfo = original.get();
            if (fluidInfo.fluid().is(BzTags.SUGAR_WATER_FLUID)) {
                return Optional.of(new PointedDripstoneBlock.FluidInfo(fluidInfo.pos(), Fluids.WATER, fluidInfo.sourceState()));
            }
        }
        return original;
    }
}
