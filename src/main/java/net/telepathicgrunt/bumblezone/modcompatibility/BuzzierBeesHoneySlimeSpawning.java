package net.telepathicgrunt.bumblezone.modcompatibility;

import net.minecraft.entity.MobEntity;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.dimension.BzDimension;

@Mod.EventBusSubscriber(modid = Bumblezone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BuzzierBeesHoneySlimeSpawning
{
	@Mod.EventBusSubscriber(modid = Bumblezone.MODID)
	private static class ForgeEvents
	{
		/*
		 * Manual spawning on honey slime to bypass their heightmap checks and light checks.
		 * Does so by converting 1/10th of bees to honey slime
		 */
		@SubscribeEvent
		public static void MobSpawnEvent(LivingSpawnEvent.CheckSpawn event)
		{
			if(ModChecking.buzzierBeesPresent) 
			{
				MobEntity entity = (MobEntity)event.getEntity();
				if(entity.dimension == BzDimension.bumblezone()) 
				{
					BuzzierBeesCompat.BBMobSpawnEvent(event);
				}
			}
		}
	}
}
