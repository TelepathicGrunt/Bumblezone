package net.telepathicgrunt.bumblezone.modCompat;

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
		 * Check for if potion of bee's item was thrown and impacted empty honeycomb block to revive it
		 */
		@SubscribeEvent
		public static void ProjectileImpactEvent(net.minecraftforge.event.entity.ProjectileImpactEvent.Throwable event)
		{
			if(ModChecker.potionOfBeesPresent && Bumblezone.BzConfig.allowPotionOfBeesCompat.get())
			{
				PotionOfBeesRedirection.POBReviveLarvaBlockEvent(event);
			}
		}
	}
}
