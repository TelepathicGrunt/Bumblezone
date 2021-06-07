package com.telepathicgrunt.bumblezone.mixin.blocks;

import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterFluid.class)
public class WaterEquivalenceMixin {

    @Inject(method = "matchesType",
            at = @At(value = "TAIL"),
            cancellable = true)
    private void thebumblezone_isEquivalentToSugarWater(Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
        if(fluid == BzBlocks.SUGAR_WATER_FLUID || fluid == BzBlocks.SUGAR_WATER_FLUID_FLOWING)
            cir.setReturnValue(true);
    }

}