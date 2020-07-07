package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.WaterFluid;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterFluid.class)
public class WaterEquivalenceMixin {

    @Inject(method = "matchesType",
            at = @At(value = "TAIL"),
            cancellable = true)
    private void isEquivalentToSugarWater(Fluid fluid, CallbackInfoReturnable<Boolean> cir) {
        if(fluid == BzBlocks.SUGAR_WATER_FLUID || fluid == BzBlocks.SUGAR_WATER_FLUID_FLOWING)
            cir.setReturnValue(true);
    }

}