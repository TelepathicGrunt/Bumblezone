package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SpawnHelper.class)
public class MobSpawnLocationMixin {
//    //Prevents mobs from spawning above y = 256.
//    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/SpawnHelper;method_24932(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/MobEntity;D)Z", ordinal = 0),
//            method = "Lnet/minecraft/world/SpawnHelper;spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
//            locals = LocalCapture.CAPTURE_FAILSOFT,
//            cancellable = true)
//    private static void spawnEntitiesInChunk(EntityCategory entityCategory, ServerWorld serverWorld, Chunk chunk, BlockPos blockPos, CallbackInfo ci, ChunkGenerator chunkGenerator, int i, BlockPos.Mutable mutable, int j, int k, int l, int m, int n, Biome.SpawnEntry spawnEntry, EntityData entityData, int o, int p, int q, float f, float g, PlayerEntity playerEntity, double d, MobEntity mobEntity) {
//
//        //No mobs allowed to spawn on roof of Bumblezone
//        if (playerEntity.dimension == BzDimensionType.BUMBLEZONE_TYPE && mutable.getY() > 255) {
//            //Bumblezone.LOGGER.log(Level.INFO, "canceled spawn");
//            ci.cancel();
//        }
//    }

}