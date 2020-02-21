package net.telepathicgrunt.bumblezone.mixin;

import io.github.alloffabric.beeproductive.init.BeeProdNectars;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SpawnHelper.class)
public class BeeProductiveBeeSpawnMixin
{
    //Prevents mobs from spawning above y = 256.
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;initialize(Lnet/minecraft/world/IWorld;Lnet/minecraft/world/LocalDifficulty;Lnet/minecraft/entity/SpawnType;Lnet/minecraft/entity/EntityData;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/entity/EntityData;", ordinal = 0),
            method = "spawnEntitiesInChunk",
            locals = LocalCapture.CAPTURE_FAILSOFT,
            cancellable = true)
    private static void spawnEntitiesInChunk(EntityCategory category, ServerWorld serverWorld, WorldChunk chunk, BlockPos spawnPos, CallbackInfo ci, ChunkGenerator chunkGenerator, int i, BlockPos blockPos, int j, int k, int l, BlockPos.Mutable mutable, int m, int n, int o, int p, Biome.SpawnEntry spawnEntry, EntityData entityData, int q, int r, int s, float f, float g, PlayerEntity playerEntity, double d, EntityType entityType, SpawnRestriction.Location location, MobEntity mobEntity2) {

        if(playerEntity.dimension == BzDimensionType.BUMBLEZONE_TYPE){

            // If BeeProduction is on, add a rare change to spawn their bees too
            if(Bumblezone.PRODUCTIVE_BEE != null && mobEntity2.getType() == EntityType.BEE){
                float choosenChance = serverWorld.random.nextFloat();
                float thresholdRange = 1f; //total chance of 0.9% to spawn a BeeProductive bee.

                if(choosenChance < thresholdRange){
                    BeeProdNectars.GAY_SKIN.onApply((BeeEntity)mobEntity2, null);
                }
                else if(choosenChance < thresholdRange*2){
                    BeeProdNectars.BI_SKIN.onApply((BeeEntity)mobEntity2, null);
                }
                else if(choosenChance < thresholdRange*3){
                    BeeProdNectars.WEATHERPROOF.onApply((BeeEntity)mobEntity2, null);
                }
                else if(choosenChance < thresholdRange*4){
                    BeeProdNectars.LESBIAN_SKIN.onApply((BeeEntity)mobEntity2, null);
                }
                else if(choosenChance < thresholdRange*5){
                    BeeProdNectars.NOCTURNAL.onApply((BeeEntity)mobEntity2, null);
                }
                else if(choosenChance < thresholdRange*6){
                    BeeProdNectars.NONBINARY_SKIN.onApply((BeeEntity)mobEntity2, null);
                }
                else if(choosenChance < thresholdRange*7){
                    BeeProdNectars.PACIFIST.onApply((BeeEntity)mobEntity2, null);
                }
                else if(choosenChance < thresholdRange*8){
                    BeeProdNectars.PAN_SKIN.onApply((BeeEntity)mobEntity2, null);
                }
                else if(choosenChance < thresholdRange*9){
                    BeeProdNectars.TRANS_SKIN.onApply((BeeEntity)mobEntity2, null);
                }
            }
        }
    }

}