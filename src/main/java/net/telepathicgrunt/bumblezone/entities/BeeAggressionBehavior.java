package net.telepathicgrunt.bumblezone.entities;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
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
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.dimension.BzDimensionRegistration;
import net.telepathicgrunt.bumblezone.dimension.BzDimension;
import net.telepathicgrunt.bumblezone.effects.BzEffects;
import net.telepathicgrunt.bumblezone.effects.WrathOfTheHiveEffect;

@Mod.EventBusSubscriber(modid = Bumblezone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BeeAggressionBehavior
{
	
	
	@Mod.EventBusSubscriber(modid = Bumblezone.MODID)
	private static class ForgeEvents
	{
		private static Set<EntityType<?>> SetOfBeeHatedEntities = new HashSet<EntityType<?>>();
		
		/*
		 * Have to run this code at world startup because the only way to check a CreatureAttribute
		 * from an EntityType is the make an Entity but you cannot pass null into Entitytype.create(null)
		 * because some mobs will crash the game. Thus, that's why this code runs here instead of in FMLCommonSetupEvent.
		 * 
		 *  gg. Mojang. gg.
		 *  
		 *  But yeah, this sets up the list of entitytype of mobs for bees to always attack. Making
		 *  the list can be expensive which is why we make it at start of world rather than every tick.
		 */
		@SubscribeEvent
		public static void SetupBeeHatingList(WorldEvent.Load event)
		{
			if(SetOfBeeHatedEntities.size() != 0) return;
			
			for(EntityType<?> entityType : ForgeRegistries.ENTITIES) 
			{
				if(entityType.getClassification() == EntityClassification.MONSTER || 
						entityType.getClassification() == EntityClassification.CREATURE ||
						entityType.getClassification() == EntityClassification.AMBIENT ) 
				{
					Entity entity = entityType.create(event.getWorld().getWorld());
					if(entity instanceof MobEntity) 
					{
						String mobName = entityType.getRegistryName().toString();
						MobEntity mobEntity = (MobEntity) entity;
						
						if((mobEntity.getCreatureAttribute() == CreatureAttribute.ARTHROPOD && !mobName.contains("bee")) ||
								 mobEntity instanceof PandaEntity || 
								 mobName.contains("bear")) 
						{
							SetOfBeeHatedEntities.add(entityType);
						}
					}
				}
			}
		}
		
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
				(playerEntity.dimension == BzDimensionRegistration.bumblezone() || Bumblezone.BzConfig.allowWrathOfTheHiveOutsideBumblezone.get()) && 
				!playerEntity.isCreative() && 
				!playerEntity.isSpectator() &&
				 Bumblezone.BzConfig.aggressiveBees.get())
			{
				playerEntity.addPotionEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE, Bumblezone.BzConfig.howLongWrathOfTheHiveLasts.get(), 2, false, Bumblezone.BzConfig.showWrathOfTheHiveParticles.get(), true));
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
				if ((playerEntity.dimension == BzDimensionRegistration.bumblezone() || Bumblezone.BzConfig.allowWrathOfTheHiveOutsideBumblezone.get()) && 
					!playerEntity.isCreative() && 
					!playerEntity.isSpectator() &&
					 Bumblezone.BzConfig.aggressiveBees.get())
				{
					playerEntity.addPotionEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE, Bumblezone.BzConfig.howLongWrathOfTheHiveLasts.get(), 2, false, Bumblezone.BzConfig.showWrathOfTheHiveParticles.get(), true));
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
				(entity.dimension == BzDimensionRegistration.bumblezone() || Bumblezone.BzConfig.allowWrathOfTheHiveOutsideBumblezone.get()) && 
				Bumblezone.BzConfig.aggressiveBees.get() && 
				entity instanceof BeeEntity &&
				attackerEntity != null)
			{
				if(attackerEntity instanceof PlayerEntity && 
					!((PlayerEntity)attackerEntity).isCreative() && 
					!((PlayerEntity)attackerEntity).isSpectator())
				{
					((PlayerEntity)attackerEntity).addPotionEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE, Bumblezone.BzConfig.howLongWrathOfTheHiveLasts.get(), 2, false, Bumblezone.BzConfig.showWrathOfTheHiveParticles.get(), true));
				}
				else if(attackerEntity instanceof MobEntity) 
				{
					((MobEntity)attackerEntity).addPotionEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE, Bumblezone.BzConfig.howLongWrathOfTheHiveLasts.get(), 2, false, true));
				}
			}
		}
		
		
		//bees attacks bear and insect mobs that are in the dimension
		@SubscribeEvent
		public static void MobUpdateEvent(LivingUpdateEvent event)
		{
			Entity entity = event.getEntity();

			//Also checks to make sure we are in the dimension.
			if (!entity.world.isRemote && 
				entity.dimension == BzDimensionRegistration.bumblezone() && 
				Bumblezone.BzConfig.aggressiveBees.get() && 
				entity instanceof MobEntity)
			{
				MobEntity mobEntity = (MobEntity)entity;
				
				//must be a bear or insect animal with no wrath of the hive effect on
				if(SetOfBeeHatedEntities.contains(entity.getType()) && !mobEntity.isPotionActive(BzEffects.WRATH_OF_THE_HIVE))
				{
					((MobEntity)entity).addPotionEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE, Bumblezone.BzConfig.howLongWrathOfTheHiveLasts.get(), 1, false, true));
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
				!(Bumblezone.BzConfig.allowWrathOfTheHiveOutsideBumblezone.get() || playerEntity.dimension == BzDimensionRegistration.bumblezone()))
			{
				playerEntity.removePotionEffect(BzEffects.WRATH_OF_THE_HIVE);
				WrathOfTheHiveEffect.calmTheBees(playerEntity.world, playerEntity);
			}

			//Makes the fog redder when this effect is active
			Boolean wrathEffect = playerEntity.isPotionActive(BzEffects.WRATH_OF_THE_HIVE);
			if(BzDimension.ACTIVE_WRATH == false && wrathEffect)
			{
				BzDimension.ACTIVE_WRATH = true;
			}
			else if(BzDimension.ACTIVE_WRATH == true && !wrathEffect)
			{
				WrathOfTheHiveEffect.calmTheBees(playerEntity.world, playerEntity);
				BzDimension.ACTIVE_WRATH = false;
			}
//			else if(!wrathEffect) {
//				WrathOfTheHiveEffect.calmTheBees(playerEntity.world, playerEntity);
//			}
		}
	}
}
