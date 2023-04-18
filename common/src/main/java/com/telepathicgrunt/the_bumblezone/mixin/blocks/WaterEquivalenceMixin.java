package com.telepathicgrunt.the_bumblezone.mixin.blocks;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WaterFluid.class)
public class WaterEquivalenceMixin {

    @ModifyReturnValue(method = "isSame",
            at = @At(value = "RETURN"))
    private boolean bumblezone$isEquivalentToSugarWater(boolean isSame, Fluid fluid) {
        if(!isSame && !fluid.defaultFluidState().isEmpty() && fluid.defaultFluidState().is(BzTags.VISUAL_WATER_FLUID)) {
            return true;
        }
        return isSame;
    }
}