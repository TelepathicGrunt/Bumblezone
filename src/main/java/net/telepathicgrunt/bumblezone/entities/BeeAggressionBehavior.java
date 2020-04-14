package net.telepathicgrunt.bumblezone.entities;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.config.BzConfig;
import net.telepathicgrunt.bumblezone.dimension.BzDimension;
import net.telepathicgrunt.bumblezone.dimension.BzWorldProvider;
import net.telepathicgrunt.bumblezone.effects.BzEffects;
import net.telepathicgrunt.bumblezone.effects.WrathOfTheHiveEffect;

@Mod.EventBusSubscriber(modid = Bumblezone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BeeAggressionBehavior
{
	
	@Mod.EventBusSubscriber(modid = Bumblezone.MODID)
	private static class ForgeEvents
	{

		//bees attack player that picks up honey blocks
		@SubscribeEvent
		public static void HoneyPickupEvent(ItemPickupEvent event)
		{
			PlayerEntity playerEntity = event.getPlayer();

			//if player picks up a honey block, bees gets very mad...
			//Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
			//Also checks to make sure we are in dimension and that player isn't in creative or spectator
			if (!playerEntity.world.isRemote && 
				 event.getStack().getItem() == Items.HONEY_BLOCK &&
				(playerEntity.dimension == BzDimension.bumblezone() || BzConfig.allowWrathOfTheHiveOutsideBumblezone) && 
				!playerEntity.isCreative() && 
				!playerEntity.isSpectator() &&
				 BzConfig.aggressiveBees)
			{
				playerEntity.addPotionEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE, BzConfig.howLongWrathOfTheHiveLasts, 2, false, BzConfig.showWrathOfTheHiveParticles, true));
			}
		}

		
		//bees attack player that drinks honey.
		@SubscribeEvent
		public static void HoneyDrinkEvent(LivingEntityUseItemEvent.Finish event)
		{
			//if player drinks honey, bees gets very mad...
			if(!event.getEntity().world.isRemote && 
				event.getItem().getItem() == Items.HONEY_BOTTLE && 
				event.getEntity() instanceof PlayerEntity)
			{
				PlayerEntity playerEntity = (PlayerEntity)event.getEntity();
				
				//Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
				//Also checks to make sure we are in dimension and that player isn't in creative or spectator
				if ((playerEntity.dimension == BzDimension.bumblezone() || BzConfig.allowWrathOfTheHiveOutsideBumblezone) && 
					!playerEntity.isCreative() && 
					!playerEntity.isSpectator() &&
					 BzConfig.aggressiveBees)
				{
					playerEntity.addPotionEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE, BzConfig.howLongWrathOfTheHiveLasts, 2, false, BzConfig.showWrathOfTheHiveParticles, true));
				}
			}
		}
		
		
		//Bees hit by a mob or player will inflict Wrath of the Hive onto the attacker.
		@SubscribeEvent
		public static void MobHitEvent(LivingHurtEvent event)
		{
			Entity entity = event.getEntity();
			Entity attackerEntity = event.getSource().getTrueSource();

			//Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
			//Also checks to make sure we are in dimension and that if it is a player, that they aren't in creative or spectator
			if (!entity.world.isRemote && 
				(entity.dimension == BzDimension.bumblezone() || BzConfig.allowWrathOfTheHiveOutsideBumblezone) && 
				BzConfig.aggressiveBees && 
				entity instanceof BeeEntity &&
				attackerEntity != null)
			{
				if(attackerEntity instanceof PlayerEntity && 
					!((PlayerEntity)attackerEntity).isCreative() && 
					!((PlayerEntity)attackerEntity).isSpectator())
				{
					((PlayerEntity)attackerEntity).addPotionEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE, BzConfig.howLongWrathOfTheHiveLasts, 2, false, BzConfig.showWrathOfTheHiveParticles, true));
				}
				else if(attackerEntity instanceof MobEntity) 
				{
					((MobEntity)attackerEntity).addPotionEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE, BzConfig.howLongWrathOfTheHiveLasts, 2, false, true));
				}
			}
		}
		
		//bees attacks bear mobs that is in the dimension
		@SubscribeEvent
		public static void MobUpdateEvent(LivingUpdateEvent event)
		{
			Entity entity = event.getEntity();

			//Also checks to make sure we are in the dimension.
			if (!entity.world.isRemote && 
				entity.dimension == BzDimension.bumblezone() && 
				BzConfig.aggressiveBees && 
				entity instanceof MobEntity)
			{
				MobEntity mobEntity = (MobEntity)entity;
				String mobName = mobEntity.getType().getRegistryName().toString();
				
				//must be a bear or insect animal with no wrath of the hive effect on
				if(((mobEntity.getCreatureAttribute() == CreatureAttribute.ARTHROPOD && !mobName.contains("bee")) ||
					 mobEntity instanceof PandaEntity || 
					 mobName.contains("bear")) && 
						!mobEntity.isPotionActive(BzEffects.WRATH_OF_THE_HIVE))
				{
					((MobEntity)entity).addPotionEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE, BzConfig.howLongWrathOfTheHiveLasts, 1, false, true));
				}
			}
		}
		

		@SubscribeEvent
		public static void playerTick(PlayerTickEvent event)
		{
			//grabs the capability attached to player for dimension hopping
			PlayerEntity playerEntity = event.player;
			
			//removes the wrath of the hive if it is disallowed outside dimension
			if(!playerEntity.world.isRemote &&
				playerEntity.isPotionActive(BzEffects.WRATH_OF_THE_HIVE) && 
				!(BzConfig.allowWrathOfTheHiveOutsideBumblezone || playerEntity.dimension == BzDimension.bumblezone()))
			{
				playerEntity.removePotionEffect(BzEffects.WRATH_OF_THE_HIVE);
				WrathOfTheHiveEffect.calmTheBees(playerEntity.world, playerEntity);
			}

			//Makes the fog redder when this effect is active
			if(BzWorldProvider.ACTIVE_WRATH == false && playerEntity.isPotionActive(BzEffects.WRATH_OF_THE_HIVE))
			{
				BzWorldProvider.ACTIVE_WRATH = true;
			}
			else if(BzWorldProvider.ACTIVE_WRATH == true)
			{
				WrathOfTheHiveEffect.calmTheBees(playerEntity.world, playerEntity);
				BzWorldProvider.ACTIVE_WRATH = false;
			}
		}
	}
	
	
}
