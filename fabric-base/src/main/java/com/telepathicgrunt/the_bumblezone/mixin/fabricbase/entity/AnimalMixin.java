package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.entity;

import com.telepathicgrunt.the_bumblezone.events.entity.BabySpawnEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Animal.class)
public class AnimalMixin {

    @Inject(
        method = "spawnChildFromBreeding",
        at = @At(
                value = "INVOKE_ASSIGN",
                target = "Lnet/minecraft/world/entity/animal/Animal;getBreedOffspring(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/AgeableMob;)Lnet/minecraft/world/entity/AgeableMob;"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        cancellable = true
    )
    public void bumblezone$onSpawnChildFromBreeding(ServerLevel serverLevel, Animal animal, CallbackInfo ci, AgeableMob baby) {
        Animal us = (Animal)(Object)this;
        if (BabySpawnEvent.EVENT.invoke(new BabySpawnEvent(us, animal, null, baby))) {
            us.setAge(6000);
            animal.setAge(6000);
            us.resetLove();
            animal.resetLove();
            ci.cancel();
        }
    }
}
