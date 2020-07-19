package net.telepathicgrunt.bumblezone.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionRegistration;

@Mod.EventBusSubscriber(modid = Bumblezone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobSpawnBehavior
{
	@Mod.EventBusSubscriber(modid = Bumblezone.MODID)
	private static class ForgeEvents
	{
		@SubscribeEvent
		public static void MobSpawnEvent(LivingSpawnEvent.CheckSpawn event)
		{
			Entity entity = event.getEntity();
			World world = entity.world;
			
			//Make sure we are on server. Vanilla does this check too
			if (!world.isRemote)
			{
				if(entity.dimension == BzDimensionRegistration.bumblezone()) {
					//NO SPAWNING ON MY DIMENSION'S ROOF!!!
					if(entity.getPosition().getY() >= 256) 
					{
						//STOP SPAWNING!!!!!!!!
						event.setResult(Result.DENY);
					}

					else if ((entity.getType() == EntityType.PHANTOM && Bumblezone.BzConfig.phantomSpawnrate.get() > world.getRandom().nextFloat()) ||
							 (entity.getType() == EntityType.ENDERMAN && Bumblezone.BzConfig.endermanSpawnrate.get() > world.getRandom().nextFloat()) ||
							 (entity.getType() == EntityType.CAVE_SPIDER && Bumblezone.BzConfig.spiderSpawnrate.get() > world.getRandom().nextFloat()) ||
							 (entity.getType() == EntityType.SPIDER && Bumblezone.BzConfig.caveSpiderSpawnrate.get() > world.getRandom().nextFloat())) {
						event.setResult(Result.DENY);
					}
				}
			}
		}
	}
}
