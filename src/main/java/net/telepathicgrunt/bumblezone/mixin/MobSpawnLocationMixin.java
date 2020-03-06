package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SpawnHelper.class)
public class MobSpawnLocationMixin
{
    //Prevents mobs from spawning above y = 256.
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/SpawnHelper;method_24932(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/MobEntity;D)Z", ordinal = 0),
            method = "method_24930",
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private static void spawnEntitiesInChunk(EntityCategory entityCategory, ServerWorld serverWorld, Chunk chunk, BlockPos blockPos, CallbackInfo ci, ChunkGenerator chunkGenerator, int i, BlockPos.Mutable mutable, int j, int k, int l, int m, int n, Biome.SpawnEntry spawnEntry, EntityData entityData, int o, int p, int q, float f, float g, PlayerEntity playerEntity, double d, MobEntity mobEntity) {

        //No mobs allowed to spawn on roof of Bumblezone
        if(playerEntity.dimension == BzDimensionType.BUMBLEZONE_TYPE && mutable.getY() > 255){
            //Bumblezone.LOGGER.log(Level.INFO, "canceled spawn");
            ci.cancel();
        }
    }

}