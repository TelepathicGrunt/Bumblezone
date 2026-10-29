
package com.telepathicgrunt.the_bumblezone.mixin.neoforge.entities;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.world.entity.EntityFluidInteraction;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityFluidInteraction.class)
public abstract class EntityFluidInteractionMixin {
    @WrapOperation(method = "getFallDistanceModifier(Lnet/minecraft/world/entity/Entity;)F",
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/fluids/FluidType;getIsWaterLike()Z"))
    private boolean bumblezone$fluidMotions(FluidType instance, Operation<Boolean> original) {
        boolean org = original.call(instance);
        if (!org) {
            return instance == NeoForgeRegistries.FLUID_TYPES.get(BzFluids.HONEY_FLUID_TYPE.get().id()).get().value() ||
                    instance == NeoForgeRegistries.FLUID_TYPES.get(BzFluids.ROYAL_JELLY_FLUID_TYPE.get().id()).get().value();
        }
        return true;
    }
}
