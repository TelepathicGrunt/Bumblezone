package net.telepathicgrunt.bumblezone.entities;

import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.effects.BzEffects;
import net.telepathicgrunt.bumblezone.effects.WrathOfTheHiveEffect;
import net.telepathicgrunt.bumblezone.items.BzItems;
import net.telepathicgrunt.bumblezone.modcompatibility.ModChecking;

@Mod.EventBusSubscriber(modid = Bumblezone.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BeeInteractivityBehavior {

	@Mod.EventBusSubscriber(modid = Bumblezone.MODID)
	private static class ForgeEvents {

		// heal bees with sugar water bottle or honey bottle
		@SubscribeEvent
		public static void BeeFeedingEvent(PlayerInteractEvent.EntityInteract event) {
			World world = event.getWorld();

			if (!world.isRemote && event.getTarget() instanceof BeeEntity) {

				BeeEntity beeEntity = (BeeEntity) event.getTarget();
				PlayerEntity playerEntity = event.getPlayer();
				ItemStack itemstack = playerEntity.getHeldItem(event.getHand());
				Item itemInhand = itemstack.getItem();

				if (itemInhand == Items.HONEY_BOTTLE || itemInhand == BzItems.SUGAR_WATER_BOTTLE.get()) {

					world.playSound(
							playerEntity,
							playerEntity.getPosX(), 
							playerEntity.getPosY(), 
							playerEntity.getPosZ(),
							SoundEvents.ITEM_BOTTLE_EMPTY,
							SoundCategory.NEUTRAL,
							1.0F, 
							1.0F);

					if (itemInhand == Items.HONEY_BOTTLE) {

						// Heal bee a lot
						beeEntity.addPotionEffect(new EffectInstance(Effects.INSTANT_HEALTH, 1, 1, false, false, false));

						// high chance to remove wrath of the hive from player
						calmAndSpawnHearts(world, playerEntity, beeEntity, 0.3f, 3);

					} 
					else {
						// Heal bee slightly but they remain angry
						beeEntity.addPotionEffect(new EffectInstance(Effects.INSTANT_HEALTH, 1, 0, false, false, false));

						// very low chance to remove wrath of the hive from player
						calmAndSpawnHearts(world, playerEntity, beeEntity, 0.07f, 1);
					}

					consumeItem(playerEntity, event, itemstack, Items.GLASS_BOTTLE);
				} 
				
				else if (ModChecking.productiveBeesPresent && 
						 itemInhand.getRegistryName().toString().equals("productivebees:honey_treat")) {

					// Heal bee a ton
					beeEntity.addPotionEffect(new EffectInstance(Effects.INSTANT_HEALTH, 2, 1, false, false, false));

					// very high chance to remove wrath of the hive from player
					calmAndSpawnHearts(world, playerEntity, beeEntity, 0.4f, 5);
					
					playerEntity.swing(event.getHand(), true);
				} 
				
				else if (ModChecking.buzzierBeesPresent && 
						(itemInhand.getRegistryName().toString().equals("buzzierbees:bee_soup") || 
						itemInhand.getRegistryName().toString().equals("buzzierbees:sticky_honey_wand"))) {

					// Heal bee a bit
					beeEntity.addPotionEffect(new EffectInstance(Effects.INSTANT_HEALTH, 1, 1, false, false, false));

					// neutral chance to remove wrath of the hive from player
					calmAndSpawnHearts(world, playerEntity, beeEntity, 0.3f, 3);

					
					if (itemInhand.getRegistryName().toString().equals("buzzierbees:bee_soup")) {
						consumeItem(playerEntity, event, itemstack, Items.BOWL);
					}
					else if (itemInhand.getRegistryName().toString().equals("buzzierbees:sticky_honey_wand")) {
						consumeItem(playerEntity, event, itemstack, ForgeRegistries.ITEMS.getValue(new ResourceLocation("buzzierbees:honey_wand")));
					}
				}
			}
		}
	}

	private static void consumeItem(PlayerEntity playerEntity, PlayerInteractEvent.EntityInteract event, ItemStack handItemstack, Item replacementItem) {
		if (!playerEntity.isCreative()) {

			// remove current bee soup
			handItemstack.shrink(1);

			if (handItemstack.isEmpty()) {
				// places empty bottle in hand
				playerEntity.setHeldItem(event.getHand(), new ItemStack(replacementItem));
			}
			// places empty bottle in inventory
			else if (!playerEntity.inventory.addItemStackToInventory(new ItemStack(replacementItem))) {
				// drops empty bottle if inventory is full
				playerEntity.dropItem(new ItemStack(replacementItem), false);
			}
		}
		else {
			playerEntity.swing(event.getHand(), true);
		}
	}

	private static void calmAndSpawnHearts(World world, PlayerEntity playerEntity, BeeEntity beeEntity, float calmChance, int hearts) {
		boolean calmed = world.rand.nextFloat() < calmChance;
		if (calmed) {
			playerEntity.removePotionEffect(BzEffects.WRATH_OF_THE_HIVE);
			WrathOfTheHiveEffect.calmTheBees(playerEntity.world, playerEntity);
		}

		if (!beeEntity.isAngry() || calmed)
			((ServerWorld) world).spawnParticle(ParticleTypes.HEART, beeEntity.getPosX(), beeEntity.getPosY(),
					beeEntity.getPosZ(), hearts, world.rand.nextFloat() * 0.5 - 1f,
					world.rand.nextFloat() * 0.2f + 0.2f, world.rand.nextFloat() * 0.5 - 1f,
					world.rand.nextFloat() * 0.4 + 0.2f);
	}
}
