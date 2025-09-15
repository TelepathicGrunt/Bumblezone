package com.telepathicgrunt.the_bumblezone.mixin.fabric.entities;

import com.llamalad7.mixinextras.sugar.Local;
import com.telepathicgrunt.the_bumblezone.events.entity.BzBabySpawnEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Animal.class)
public class AnimalMixin {

    @Inject(method = "spawnChildFromBreeding",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/AgeableMob;setBaby(Z)V"),
            cancellable = true)
    public void bumblezone$onSpawnChildFromBreeding(ServerLevel serverLevel, Animal otherParent, CallbackInfo ci, @Local(ordinal = 0) AgeableMob baby) {
        Animal parent = (Animal)(Object)this;
        if (BzBabySpawnEvent.EVENT.invoke(new BzBabySpawnEvent(parent, otherParent, parent.getLoveCause(), baby))) {
            parent.setAge(6000);
            otherParent.setAge(6000);
            parent.resetLove();
            otherParent.resetLove();
            ci.cancel();
        }
    }
}
