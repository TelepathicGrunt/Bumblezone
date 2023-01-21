package com.telepathicgrunt.the_bumblezone.mixins.fabric;

import com.telepathicgrunt.the_bumblezone.events.entity.EntitySpawnEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PatrolSpawner.class)
public class PatrolSpawnerMixin {


    @Inject(
            method = "spawnPatrolMember",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/PatrollingMonster;finalizeSpawn(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/world/entity/SpawnGroupData;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/entity/SpawnGroupData;"
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void bumblezone$onSpawnEntity(ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, boolean bl, CallbackInfoReturnable<Boolean> cir, BlockState blockState, PatrollingMonster patrollingMonster) {
        if (EntitySpawnEvent.EVENT.invoke(new EntitySpawnEvent(patrollingMonster, serverLevel, patrollingMonster.isBaby(), MobSpawnType.PATROL))) {
            cir.setReturnValue(false);
        }
    }
}
