package net.telepathicgrunt.bumblezone.mixin;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.lang.reflect.Type;

import static net.telepathicgrunt.bumblezone.Bumblezone.*;

@Mixin(SpawnHelper.class)
public class MobSpawnLocationMixin
{
    //Prevents mobs from spawning above y = 256.
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/SpawnRestriction;getLocation(Lnet/minecraft/entity/EntityType;)Lnet/minecraft/entity/SpawnRestriction$Location;", ordinal = 0),
            method = "spawnEntitiesInChunk",
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private static void spawnEntitiesInChunk(EntityCategory category, ServerWorld serverWorld, WorldChunk chunk, BlockPos spawnPos, CallbackInfo ci, ChunkGenerator chunkGenerator, int i, BlockPos blockPos, int j, int k, int l, BlockPos.Mutable mutable, int m, int n, int o, int p, Biome.SpawnEntry spawnEntry, EntityData entityData, int q, int r, int s, float f, float g, PlayerEntity playerEntity, double d, EntityType entityType) {

        //No mobs allowed to spawn on roof of Bumblezone
        if(playerEntity.dimension == BzDimensionType.BUMBLEZONE_TYPE && mutable.getY() > 255){
            //Bumblezone.LOGGER.log(Level.INFO, "canceled spawn");
            ci.cancel();
        }
    }

}