package com.telepathicgrunt.bumblezone.modCompat;

import com.telepathicgrunt.bumblezone.Bumblezone;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Bumblezone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PotionOfBeesBeeSplashPotionProjectile
{
	@Mod.EventBusSubscriber(modid = Bumblezone.MODID)
	private static class ForgeEvents
	{
		/*
		 * Check for if potion of bee's item was thrown and impacted empty honeycomb block to revive it
		 */
		@SubscribeEvent
		public static void ProjectileImpactEvent(net.minecraftforge.event.entity.ProjectileImpactEvent.Throwable event)
		{
			if(ModChecker.potionOfBeesPresent && Bumblezone.BzModCompatibilityConfig.allowPotionOfBeesCompat.get())
			{
				PotionOfBeesRedirection.POBReviveLarvaBlockEvent(event);
			}
		}
	}
}
