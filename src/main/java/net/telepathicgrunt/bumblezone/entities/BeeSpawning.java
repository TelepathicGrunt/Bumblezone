package net.telepathicgrunt.bumblezone.entities;

import net.minecraft.entity.*;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.effects.BzEffects;
import net.telepathicgrunt.bumblezone.effects.WrathOfTheHiveEffect;
import net.telepathicgrunt.bumblezone.mixin.BeeEntityInvoker;

import java.util.HashSet;
import java.util.Set;

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