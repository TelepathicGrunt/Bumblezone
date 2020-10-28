package com.telepathicgrunt.the_bumblezone.mixin;

import com.telepathicgrunt.the_bumblezone.fluids.BzFluids;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.WaterFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterFluid.class)
public class WaterEquivalenceMixin {

    @Inject(method = "isEquivalentTo",
            at = @At(value = "TAIL"),
            cancellable = true)
    private void isEquivalentToSugarWater(Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
        if(fluid == BzFluids.SUGAR_WATER_FLUID.get() || fluid == BzFluids.SUGAR_WATER_FLUID_FLOWING.get())
            cir.setReturnValue(true);
    }

}