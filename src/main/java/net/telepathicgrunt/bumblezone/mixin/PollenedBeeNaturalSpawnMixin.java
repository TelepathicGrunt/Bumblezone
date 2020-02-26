package net.telepathicgrunt.bumblezone.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionType;
import net.telepathicgrunt.bumblezone.entities.BeeProductiveIntegration;
import org.apache.logging.log4j.core.jmx.Server;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpawnHelper.class)
public class PollenedBeeNaturalSpawnMixin
{
    //spawns bees with chance to bee full of pollen or be a BeeProductive mob if that mod is on
    @ModifyArg(method = "spawnEntitiesInChunk",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z"),
            index = 0)
    private static Entity spawnEntitiesInChunk(Entity entity) {
        if(!(entity.world.isClient()))
        {
            ServerWorld serverWorld = (ServerWorld)entity.world;

            if(serverWorld.dimension.getType() == BzDimensionType.BUMBLEZONE_TYPE){
                if(entity.getType() == EntityType.BEE){
                    //20% chance of being full of pollen
                    if(serverWorld.random.nextFloat() < 0.2f){
                        ((BeeEntityAccessor)entity).callSetBeeFlag(8 ,true);
                    }

                    // If BeeProduction is on, add a rare chance to spawn their bees too
//                if(FabricLoader.getInstance().isModLoaded("beeproductive")){
//                    entity = BeeProductiveIntegration.spawnBeeProductiveBee(serverWorld.getRandom(), entity);
//                }
                }
            }
        }


        return entity;
    }
}