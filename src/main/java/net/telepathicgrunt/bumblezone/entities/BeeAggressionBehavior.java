package net.telepathicgrunt.bumblezone.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.config.BzConfig;
import net.telepathicgrunt.bumblezone.dimension.BzDimension;
import net.telepathicgrunt.bumblezone.effects.BzEffects;

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
			World world = playerEntity.world;
			
			//Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
			//Also checks to make sure we are in dimension and that player isn't in creative or spectator
			if (!world.isRemote && 
				(playerEntity.dimension == BzDimension.bumblezone() || BzConfig.allowWrathOfTheHiveOutsideBumblezone) && 
				!playerEntity.isCreative() && 
				!playerEntity.isSpectator())
			{
				//if player picks up a honey block, bees gets very mad...
				if(event.getStack().getItem() == Items.HONEY_BLOCK && BzConfig.aggressiveBees)
				{
					playerEntity.addPotionEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE, BzConfig.howLongWrathOfTheHiveLasts, 2, false, BzConfig.showWrathOfTheHiveParticles, true));
				}
			}
		}

		
		//bees attack player that drinks honey.
		@SubscribeEvent
		public static void HoneyPickupEvent(LivingEntityUseItemEvent.Finish event)
		{
			if(event.getEntity() instanceof PlayerEntity)
			{
				PlayerEntity playerEntity = (PlayerEntity)event.getEntity();
				World world = playerEntity.world;
				
				//Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
				//Also checks to make sure we are in dimension and that player isn't in creative or spectator
				if (!world.isRemote && 
					(playerEntity.dimension == BzDimension.bumblezone() || BzConfig.allowWrathOfTheHiveOutsideBumblezone) && 
					!playerEntity.isCreative() && 
					!playerEntity.isSpectator())
				{
					//if player drinks honey, bees gets very mad...
					if(event.getItem().getItem() == Items.field_226638_pX_ && BzConfig.aggressiveBees)
					{
						playerEntity.addPotionEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE, BzConfig.howLongWrathOfTheHiveLasts, 2, false, BzConfig.showWrathOfTheHiveParticles, true));
					}
				}
			}
		}
		
		
		//bees attacks bear mobs that is in the dimension
		@SubscribeEvent
		public static void MobUpdateEvent(LivingUpdateEvent event)
		{
			Entity bearEntity = event.getEntity();
			
			//must be a bear animal
			if(bearEntity instanceof PolarBearEntity || bearEntity instanceof PandaEntity)
			{
				World world = bearEntity.world;

				//Make sure we are on actual player's computer and not a dedicated server. Vanilla does this check too.
				//Also checks to make sure we are in dimension and that player isn't in creative or spectator
				if (!world.isRemote && bearEntity.dimension == BzDimension.bumblezone() && BzConfig.aggressiveBees)
				{
					((MobEntity)bearEntity).addPotionEffect(new EffectInstance(BzEffects.WRATH_OF_THE_HIVE, BzConfig.howLongWrathOfTheHiveLasts, 1, false, true));
				}
			}
		}
	}
	
	
}
