package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.mixin.BeeEntityInvoker;
import net.minecraft.entity.EntityType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.telepathicgrunt.the_bumblezone.Bumblezone;

@Mod.EventBusSubscriber(modid = Bumblezone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BeeSpawning {

    @Mod.EventBusSubscriber(modid = Bumblezone.MODID)
    private static class ForgeEvents
    {
        //Makes bees spawn pollinated or spawn other mod's bees
        @SubscribeEvent
        public static void MobSpawnEvent(LivingSpawnEvent.SpecialSpawn event)
        {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();

            if (serverWorld.getRegistryKey().getValue().equals(Bumblezone.MOD_DIMENSION_ID)) {
                if (event.getEntityLiving().getType() == EntityType.BEE) {
                    //20% chance of being full of pollen
                    if (serverWorld.rand.nextFloat() < 0.2f) {
                        ((BeeEntityInvoker) event.getEntityLiving()).callSetBeeFlag(8, true);
                    }

                    // If BeeProduction is on, add a rare chance to spawn their bees too
//                if(FabricLoader.getInstance().isModLoaded("beeproductive")){
//                    entity = BeeProductiveIntegration.spawnBeeProductiveBee(serverWorld.getRandom(), entity);
//                }
                }
            }
        }
    }
}