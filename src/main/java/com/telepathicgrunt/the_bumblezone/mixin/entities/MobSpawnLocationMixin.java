package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NaturalSpawner.class)
public class MobSpawnLocationMixin {
    //Prevents mobs from spawning above y = 256.
    @Inject(method = "spawnCategoryForPosition(Lnet/minecraft/world/entity/MobCategory;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkAccess;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/NaturalSpawner$SpawnPredicate;Lnet/minecraft/world/level/NaturalSpawner$AfterSpawnCallback;)V",
            at = @At(value = "HEAD"),
            cancellable = true)
    private static void thebumblezone_spawnEntitiesInChunk(MobCategory group, ServerLevel world, ChunkAccess chunk, BlockPos pos, NaturalSpawner.SpawnPredicate checker, NaturalSpawner.AfterSpawnCallback runner, CallbackInfo ci) {

        //No mobs allowed to spawn on roof of Bumblezone
        if (pos.getY() > 255 && world.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID)) {
            //Bumblezone.LOGGER.log(Level.INFO, "canceled spawn");
            ci.cancel();
        }
    }

}