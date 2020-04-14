package net.telepathicgrunt.bumblezone.modcompatibility;

import net.minecraft.entity.MobEntity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.config.BzConfig;
import net.telepathicgrunt.bumblezone.dimension.BzDimension;

@Mod.EventBusSubscriber(modid = Bumblezone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BeesourcefulOreBeesSpawning
{
	@Mod.EventBusSubscriber(modid = Bumblezone.MODID)
	private static class ForgeEvents
	{
		/*
		 * Manual spawning of Beesourceful Bees so it can be disabled real time by config.
		 * works by making 1/10th of bees spawning also spawn honey slime
		 */
		@SubscribeEvent
		public static void MobSpawnEvent(LivingSpawnEvent.CheckSpawn event)
		{
			if(ModChecking.beesourcefulPresent) 
			{
				MobEntity entity = (MobEntity)event.getEntity();
				if(BzConfig.spawnBeesourcefulBeesMob && entity.dimension == BzDimension.bumblezone()) 
				{
					BeesourcefulRedirection.BSMobSpawnEvent(event);
				}
			}
		}
	}
}
