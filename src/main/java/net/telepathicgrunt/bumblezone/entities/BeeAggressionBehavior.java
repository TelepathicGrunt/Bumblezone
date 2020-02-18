package net.telepathicgrunt.bumblezone.entities;

public class BeeAggressionBehavior
{
	
//	@Mod.EventBusSubscriber(modid = Bumblezone.MODID)
//	private static class ForgeEvents
//	{
//
//		//bees attack player that picks up honey blocks
//		@SubscribeEvent
//		public static void HoneyPickupEvent(ItemPickupEvent event)
//		{
//			PlayerEntity playerEntity = event.getPlayer();
//			World world = playerEntity.world;
//			
//			//Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
//			//Also checks to make sure we are in dimension and that player isn't in creative or spectator
//			if (!world.isRemote && 
//				(playerEntity.dimension == BzDimension.bumblezone() || BzConfig.allowWrathOfTheHiveOutsideBumblezone) && 
//				!playerEntity.isCreative() && 
//				!playerEntity.isSpectator())
//			{
//				//if player picks up a honey block, bees gets very mad...
//				if(event.getStack().getItem() == Items.HONEY_BLOCK && BzConfig.aggressiveBees)
//				{
//					playerEntity.addPotionEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE, BzConfig.howLongWrathOfTheHiveLasts, 2, false, BzConfig.showWrathOfTheHiveParticles, true));
//				}
//			}
//		}
//
//		
//		//bees attack player that drinks honey.
//		@SubscribeEvent
//		public static void HoneyDrinkEvent(LivingEntityUseItemEvent.Finish event)
//		{
//			if(event.getEntity() instanceof PlayerEntity)
//			{
//				PlayerEntity playerEntity = (PlayerEntity)event.getEntity();
//				World world = playerEntity.world;
//				
//				//Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
//				//Also checks to make sure we are in dimension and that player isn't in creative or spectator
//				if (!world.isRemote && 
//					(playerEntity.dimension == BzDimension.bumblezone() || BzConfig.allowWrathOfTheHiveOutsideBumblezone) && 
//					!playerEntity.isCreative() && 
//					!playerEntity.isSpectator())
//				{
//					//if player drinks honey, bees gets very mad...
//					if(event.getItem().getItem() == Items.field_226638_pX_ && BzConfig.aggressiveBees)
//					{
//						playerEntity.addPotionEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE, BzConfig.howLongWrathOfTheHiveLasts, 2, false, BzConfig.showWrathOfTheHiveParticles, true));
//					}
//				}
//			}
//		}
//		
//		
//		//bees attacks bear mobs that is in the dimension
//		@SubscribeEvent
//		public static void MobUpdateEvent(LivingUpdateEvent event)
//		{
//			Entity bearEntity = event.getEntity();
//			
//			//must be a bear animal
//			if(bearEntity instanceof PolarBearEntity || bearEntity instanceof PandaEntity)
//			{
//				World world = bearEntity.world;
//
//				//Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
//				//Also checks to make sure we are in dimension and that player isn't in creative or spectator
//				if (!world.isRemote && bearEntity.dimension == BzDimension.bumblezone() && BzConfig.aggressiveBees)
//				{
//					((MobEntity)bearEntity).addPotionEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE, BzConfig.howLongWrathOfTheHiveLasts, 1, false, true));
//				}
//			}
//		}
//		
//
//		@SubscribeEvent
//		public static void playerTick(PlayerTickEvent event)
//		{
//			//grabs the capability attached to player for dimension hopping
//			PlayerEntity playerEntity = event.player;
//			
//			//removes the wrath of the hive if it is disallowed outside dimension
//			if(!(BzConfig.allowWrathOfTheHiveOutsideBumblezone || playerEntity.dimension == BzDimension.bumblezone()) &&
//				playerEntity.isPotionActive(BzEffects.WRATH_OF_THE_HIVE))
//			{
//				playerEntity.removePotionEffect(BzEffects.WRATH_OF_THE_HIVE);
//			}
//			
//			//Makes it so player does not get killed for falling into the void
//			if(playerEntity.dimension == BzDimension.bumblezone() && playerEntity.getY() < -3)
//			{
//				playerEntity.setPosition(playerEntity.getX(), -3, playerEntity.getZ());
//			}
//
//			//Makes the fog redder when this effect is active
//			if(playerEntity.isPotionActive(BzEffects.WRATH_OF_THE_HIVE))
//			{
//				BzWorldProvider.ACTIVE_WRATH = true;
//			}
//			else
//			{
//				BzWorldProvider.ACTIVE_WRATH = false;
//			}
//		}
//	}
	
	
}
