package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.entities.mobs.VariantBeeEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Animal.class)
public class AnimalMixin {

    @Inject(method = "canMate(Lnet/minecraft/world/entity/animal/Animal;)Z",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void bumblezone$beeBreedWithVariantBee(Animal animal, CallbackInfoReturnable<Boolean> cir) {
        if (this.getClass().equals(Bee.class) && animal.getClass().equals(VariantBeeEntity.class)) {
            if (((Animal)(Object)this).isInLove() && animal.isInLove()) {
                cir.setReturnValue(true);
            }
        }
    }
}