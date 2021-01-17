package net.telepathicgrunt.bumblezone.mixin.entities;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.chunk.Chunk;
import net.telepathicgrunt.bumblezone.Bumblezone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpawnHelper.class)
public class MobSpawnLocationMixin {
    //Prevents mobs from spawning above y = 256.
    @Inject(method = "Lnet/minecraft/world/SpawnHelper;spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
            at = @At(value = "HEAD"),
            cancellable = true)
    private static void spawnEntitiesInChunk(SpawnGroup group, ServerWorld world, Chunk chunk, BlockPos pos, SpawnHelper.Checker checker, SpawnHelper.Runner runner, CallbackInfo ci) {

        //No mobs allowed to spawn on roof of Bumblezone
        if (pos.getY() > 255 && world.getRegistryKey().getValue().equals(Bumblezone.MOD_DIMENSION_ID)) {
            //Bumblezone.LOGGER.log(Level.INFO, "canceled spawn");
            ci.cancel();
        }
    }

}