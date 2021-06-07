package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldEntitySpawner.class)
public class MobSpawnLocationMixin {
    //Prevents mobs from spawning above y = 256.
    @Inject(method = "spawnCategoryForPosition(Lnet/minecraft/entity/EntityClassification;Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/chunk/IChunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/spawner/WorldEntitySpawner$IDensityCheck;Lnet/minecraft/world/spawner/WorldEntitySpawner$IOnSpawnDensityAdder;)V",
            at = @At(value = "HEAD"),
            cancellable = true)
    private static void thebumblezone_spawnEntitiesInChunk(EntityClassification group, ServerWorld world, IChunk chunk, BlockPos pos, WorldEntitySpawner.IDensityCheck checker, WorldEntitySpawner.IOnSpawnDensityAdder runner, CallbackInfo ci) {

        //No mobs allowed to spawn on roof of Bumblezone
        if (pos.getY() > 255 && world.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID)) {
            //Bumblezone.LOGGER.log(Level.INFO, "canceled spawn");
            ci.cancel();
        }
    }

}