package net.telepathicgrunt.bumblezone.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;
import net.telepathicgrunt.bumblezone.Bumblezone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SpawnHelper.class)
public class PollenedBeeNaturalSpawnMixin {

    //spawns bees with chance to bee full of pollen or be a BeeProductive mob if that mod is on

    @ModifyArg(method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
                at = @At(value = "INVOKE", target ="Lnet/minecraft/server/world/ServerWorld;spawnEntityAndPassengers(Lnet/minecraft/entity/Entity;)V"),
                index = 0)
    private static Entity spawnEntitiesInChunk(Entity entity) {
        ServerWorld serverWorld = (ServerWorld) entity.world;

        if (serverWorld.getRegistryKey().getValue() == Bumblezone.MOD_DIMENSION_ID) {
            if (entity.getType() == EntityType.BEE) {
                //20% chance of being full of pollen
                if (serverWorld.random.nextFloat() < 0.2f) {
                    ((BeeEntityInvoker) entity).callSetBeeFlag(8, true);
                }

                // If BeeProduction is on, add a rare chance to spawn their bees too
//                if(FabricLoader.getInstance().isModLoaded("beeproductive")){
//                    entity = BeeProductiveIntegration.spawnBeeProductiveBee(serverWorld.getRandom(), entity);
//                }
            }
        }
        return entity;
    }
}