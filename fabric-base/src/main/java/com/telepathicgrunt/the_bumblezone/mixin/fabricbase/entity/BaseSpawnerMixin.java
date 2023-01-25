package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.telepathicgrunt.the_bumblezone.events.entity.EntitySpawnEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BaseSpawner.class)
public class BaseSpawnerMixin {


    @WrapOperation(
            method = "serverTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Mob;checkSpawnObstruction(Lnet/minecraft/world/level/LevelReader;)Z"
            )
    )
    private boolean bumblezone$onEntitySpawn(Mob instance, LevelReader level, Operation<Boolean> operation) {
        if (level instanceof Level theLevel && EntitySpawnEvent.EVENT.invoke(new EntitySpawnEvent(instance, theLevel, instance.isBaby(), MobSpawnType.SPAWNER))) {
            // Return false makes it so the if statement is true to then skip spawning the mob
            return false;
        }
        return operation.call(instance, level);
    }
}
