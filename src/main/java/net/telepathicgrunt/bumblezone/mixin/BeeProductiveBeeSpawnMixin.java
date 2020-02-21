package net.telepathicgrunt.bumblezone.mixin;

import io.github.alloffabric.beeproductive.init.BeeProdNectars;
import net.minecraft.entity.*;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SpawnHelper.class)
public class BeeProductiveBeeSpawnMixin
{
    @Redirect(method = "spawnEntitiesInChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private static boolean spawnEntitiesInChunk(ServerWorld serverWorld, Entity entity) {

        if(serverWorld.dimension.getType() == BzDimensionType.BUMBLEZONE_TYPE){
            if(entity.getType() == EntityType.BEE){
                // If BeeProduction is on, add a rare change to spawn their bees too
                if(Bumblezone.PRODUCTIVE_BEE != null ){
                    float choosenChance = serverWorld.random.nextFloat();
                    float thresholdRange = 0.001f; //total chance of 0.9% to spawn a BeeProductive bee.

                    if(choosenChance < thresholdRange){
                        BeeProdNectars.GAY_SKIN.onApply((BeeEntity)entity, null);
                    }
                    else if(choosenChance < thresholdRange*2){
                        BeeProdNectars.BI_SKIN.onApply((BeeEntity)entity, null);
                    }
                    else if(choosenChance < thresholdRange*3){
                        BeeProdNectars.WEATHERPROOF.onApply((BeeEntity)entity, null);
                    }
                    else if(choosenChance < thresholdRange*4){
                        BeeProdNectars.LESBIAN_SKIN.onApply((BeeEntity)entity, null);
                    }
                    else if(choosenChance < thresholdRange*5){
                        BeeProdNectars.NOCTURNAL.onApply((BeeEntity)entity, null);
                    }
                    else if(choosenChance < thresholdRange*6){
                        BeeProdNectars.NONBINARY_SKIN.onApply((BeeEntity)entity, null);
                    }
                    else if(choosenChance < thresholdRange*7){
                        BeeProdNectars.PACIFIST.onApply((BeeEntity)entity, null);
                    }
                    else if(choosenChance < thresholdRange*8){
                        BeeProdNectars.PAN_SKIN.onApply((BeeEntity)entity, null);
                    }
                    else if(choosenChance < thresholdRange*9){
                        BeeProdNectars.TRANS_SKIN.onApply((BeeEntity)entity, null);
                    }
                }

                ((BeeEntityAccessor)entity).setBeeFlag(8 ,true);
            }
        }

        return serverWorld.spawnEntity(entity);
    }
}