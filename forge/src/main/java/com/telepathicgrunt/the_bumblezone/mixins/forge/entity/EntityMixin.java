package com.telepathicgrunt.the_bumblezone.mixins.forge.entity;

import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract FluidType getEyeInFluidType();

    // Stop Forge breaking everything fluid with my Sugar Water fluid.
    @Inject(method = "isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z",
            at = @At(value = "HEAD"),
            cancellable = true,
            require = 0)
    private void thebumblezone_applyAllWaterBehaviorToSugarWaterFluid(TagKey<Fluid> fluidTagKey, CallbackInfoReturnable<Boolean> cir) {
        if(fluidTagKey == FluidTags.WATER &&
            (this.getEyeInFluidType() == BzFluids.SUGAR_WATER_FLUID_TYPE.get().flowing().getFluidType() ||
            this.getEyeInFluidType() == BzFluids.SUGAR_WATER_FLUID_TYPE.get().source().getFluidType()))
        {
            cir.setReturnValue(true);
        }
    }
}