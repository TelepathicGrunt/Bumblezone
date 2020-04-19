package net.telepathicgrunt.bumblezone.modcompatibility;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.telepathicgrunt.bumblezone.Bumblezone;

@Mod.EventBusSubscriber(modid = Bumblezone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PotionOfBeesBeeSplashPotionProjectile
{
	@Mod.EventBusSubscriber(modid = Bumblezone.MODID)
	private static class ForgeEvents
	{
		/*
		 * Manual spawning on honey slime to bypass their heightmap checks and light checks.
		 * works by making 1/10th of bees spawning also spawn honey slime
		 */
		@SubscribeEvent
		public static void ProjectileImpactEvent(net.minecraftforge.event.entity.ProjectileImpactEvent.Throwable event)
		{
			if(ModChecking.potionOfBeesPresent) 
			{
				PotionOfBeesRedirection.POBReviveLarvaBlockEvent(event);
			}
		}
	}
}
