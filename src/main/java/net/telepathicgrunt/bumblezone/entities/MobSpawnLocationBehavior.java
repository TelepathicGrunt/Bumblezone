package net.telepathicgrunt.bumblezone.entities;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.world.dimension.BumblezoneDimension;

@Mod.EventBusSubscriber(modid = Bumblezone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobSpawnLocationBehavior
{
	@Mod.EventBusSubscriber(modid = Bumblezone.MODID)
	private static class ForgeEvents
	{
		@SubscribeEvent
		public static void MobSpawnEvent(LivingSpawnEvent.CheckSpawn event)
		{
			Entity entity = event.getEntity();
			World world = entity.world;
			
			//Make sure we are on client I think. Vanilla does this check too
			if (!world.isRemote)
			{
				//NO SPAWNING ON MY DIMENSION'S ROOF!!!
				if(entity.dimension == BumblezoneDimension.bumblezone() && entity.getPosition().getY() >= 256) 
				{
					//STOP SPAWNING!!!!!!!!
					event.setResult(Result.DENY);
				}
			}
		}
	}
}
