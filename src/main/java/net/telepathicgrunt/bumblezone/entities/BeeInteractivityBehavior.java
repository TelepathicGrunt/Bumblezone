package net.telepathicgrunt.bumblezone.entities;

import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.effects.BzEffects;
import net.telepathicgrunt.bumblezone.effects.WrathOfTheHiveEffect;
import net.telepathicgrunt.bumblezone.items.BzItems;


@Mod.EventBusSubscriber(modid = Bumblezone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BeeInteractivityBehavior
{

	@Mod.EventBusSubscriber(modid = Bumblezone.MODID)
	private static class ForgeEvents
	{

		//heal bees with sugar water bottle or honey bottle
		@SubscribeEvent
		public static void BeeFeedingEvent(PlayerInteractEvent.EntityInteract event)
		{
			World world = event.getWorld();

			if (!world.isRemote && event.getTarget() instanceof BeeEntity)
			{
				BeeEntity beeEntity = (BeeEntity) event.getTarget();
				PlayerEntity playerEntity = event.getPlayer();
				ItemStack itemstack = playerEntity.getHeldItem(event.getHand());
				
				if (itemstack.getItem() == Items.HONEY_BOTTLE || itemstack.getItem() == BzItems.SUGAR_WATER_BOTTLE.get())
				{
					world.playSound(playerEntity, playerEntity.getPosX(), playerEntity.getPosY(), playerEntity.getPosZ(), SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.NEUTRAL, 1.0F, 1.0F);
					
					if(itemstack.getItem() == Items.HONEY_BOTTLE) {
						//Heal bee a lot
						beeEntity.addPotionEffect(new EffectInstance(Effects.INSTANT_HEALTH, 1, 1, false, false, false));
						
						//high chance to remove wrath of the hive from player
						boolean calmed = world.rand.nextFloat() < 0.3f;
						if(calmed) 
						{
							playerEntity.removePotionEffect(BzEffects.WRATH_OF_THE_HIVE);
							WrathOfTheHiveEffect.calmTheBees(playerEntity.world, playerEntity);
						}
						
						if(!beeEntity.isAngry() || calmed)
							((ServerWorld)world).spawnParticle(ParticleTypes.HEART, beeEntity.getPosX(), beeEntity.getPosY(), beeEntity.getPosZ(), 3, world.rand.nextFloat()*0.5-1f, world.rand.nextFloat()*0.2f+0.2f, world.rand.nextFloat()*0.5-1f, world.rand.nextFloat()*0.4+0.2f);	
					}
					else {
						//Heal bee slightly but they remain angry
						beeEntity.addPotionEffect(new EffectInstance(Effects.INSTANT_HEALTH, 1, 0, false, false, false));

						//very low chance to remove wrath of the hive from player
						boolean calmed = world.rand.nextFloat() < 0.07f;
						if(calmed) 
						{
							playerEntity.removePotionEffect(BzEffects.WRATH_OF_THE_HIVE);
							WrathOfTheHiveEffect.calmTheBees(playerEntity.world, playerEntity);
						}

						if(!beeEntity.isAngry() || calmed)
							((ServerWorld)world).spawnParticle(ParticleTypes.HEART, beeEntity.getPosX(), beeEntity.getPosY(), beeEntity.getPosZ(), 1, world.rand.nextFloat()*0.5-0.25f, world.rand.nextFloat()*0.2f+0.2f, world.rand.nextFloat()*0.5-0.25f, world.rand.nextFloat()*0.4+0.2f);	
					}
					
					
					if (!playerEntity.isCreative())
					{
						itemstack.shrink(1); // remove current honey bottle

						if (itemstack.isEmpty())
						{
							playerEntity.setHeldItem(event.getHand(), new ItemStack(Items.GLASS_BOTTLE)); // places empty bottle in hand
						}
						else if (!playerEntity.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE))) // places empty bottle in inventory
						{
							playerEntity.dropItem(new ItemStack(Items.GLASS_BOTTLE), false); // drops empty bottle if inventory is full
						}
					}
				}
			}
		}
	}
}
