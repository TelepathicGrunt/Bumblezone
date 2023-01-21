package com.telepathicgrunt.the_bumblezone.mixins.fabric;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.telepathicgrunt.the_bumblezone.events.entity.EntitySpawnEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.NaturalSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NaturalSpawner.class)
public class NaturalSpawnerMixin {


    @WrapOperation(
            method = "spawnCategoryForPosition(Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;Lnet/minecraft/world/level/NaturalSpawner$AfterSpawnCallback;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/NaturalSpawner;isValidPositionForMob(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Mob;D)Z"
            )
    )
    private static boolean bumblezone$onEntitySpawn(ServerLevel serverLevel, Mob mob, double d, Operation<Boolean> operation) {
        if (EntitySpawnEvent.EVENT.invoke(new EntitySpawnEvent(mob, serverLevel, mob.isBaby(), MobSpawnType.NATURAL))) {
            return false;
        }
        return operation.call(serverLevel, mob, d);
    }

    @WrapOperation(
            method = "spawnMobsForChunkGeneration",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Mob;checkSpawnRules(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/entity/MobSpawnType;)Z"
            )
    )
    private static boolean bumbelzone$onCheckEntitySpawn(Mob instance, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, Operation<Boolean> operation) {
        if (EntitySpawnEvent.EVENT.invoke(new EntitySpawnEvent(instance, levelAccessor, instance.isBaby(), mobSpawnType))) {
            return false;
        }
        return operation.call(instance, levelAccessor, mobSpawnType);
    }
}
