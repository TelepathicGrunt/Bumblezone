package com.telepathicgrunt.the_bumblezone.modCompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import noobanidus.mods.carrierbees.entities.AppleBeeEntity;
import noobanidus.mods.carrierbees.entities.CarrierBeeEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CarrierBeesCompat {

	private static final List<EntityType<?>> CB_BEE_LIST = new ArrayList<>();
	private static EntityType<?> BOMBLE_BEE = null;
	private static EntityType<?> CARRIER_BEE = null;
	private static final List<Item> CARRIER_BEES_ITEMS = new ArrayList<>(Arrays.asList(
			Items.DANDELION,
			Items.CORNFLOWER,
			Items.SUNFLOWER,
			Items.LILY_OF_THE_VALLEY,
			Items.LILAC,
			Items.ROSE_BUSH,
			Items.BLUE_ORCHID,
			Items.PEONY,
			Items.POPPY,
			Items.ALLIUM,
			Items.AZURE_BLUET,
			Items.OXEYE_DAISY,
			Items.ORANGE_TULIP,
			Items.PINK_TULIP,
			Items.RED_TULIP,
			Items.WHITE_TULIP,
			Items.YELLOW_TERRACOTTA,
			Items.YELLOW_GLAZED_TERRACOTTA,
			Items.YELLOW_CONCRETE,
			Items.YELLOW_CONCRETE_POWDER,
			Items.YELLOW_BANNER,
			Items.YELLOW_BED,
			Items.YELLOW_CARPET,
			Items.YELLOW_WOOL,
			Items.YELLOW_DYE,
			Items.BLAZE_ROD,
			Items.ORANGE_TERRACOTTA,
			Items.ORANGE_GLAZED_TERRACOTTA,
			Items.ORANGE_CONCRETE,
			Items.ORANGE_CONCRETE_POWDER,
			Items.ORANGE_BANNER,
			Items.ORANGE_BED,
			Items.ORANGE_CARPET,
			Items.ORANGE_WOOL,
			Items.ORANGE_DYE,
			Items.HONEYCOMB,
			Items.GLOWSTONE_DUST,
			Items.MAGMA_CREAM,
			Items.LANTERN,
			Items.JACK_O_LANTERN,
			Items.APPLE,
			Items.WHEAT,
			Items.HAY_BLOCK,
			Items.HORN_CORAL,
			Items.HORN_CORAL_FAN,
			Items.HORN_CORAL_BLOCK,
			Items.PUFFERFISH,
			Items.BLAZE_POWDER,
			Items.BLAZE_ROD,
			Items.CHORUS_FLOWER,
			Items.FLOWER_POT,
			Items.BRICK,
			Items.CLAY_BALL,
			Items.SPONGE,
			Items.LEAD,
			Items.BELL,
			Items.SPECTRAL_ARROW,
			Items.MAP,
			Items.MUSIC_DISC_11,
			Items.MUSIC_DISC_13,
			Items.MUSIC_DISC_BLOCKS,
			Items.GOLDEN_APPLE,
			Items.GOLDEN_CARROT,
			Items.GLISTERING_MELON_SLICE,
			Items.GOLD_NUGGET,
			Items.GOLDEN_HORSE_ARMOR,
			Items.GOLDEN_HOE,
			Items.GOLDEN_SHOVEL,
			Items.GOLDEN_LEGGINGS,
			Items.DIAMOND_HORSE_ARMOR,
			Items.DIAMOND_HOE,
			Items.DIAMOND_SHOVEL,
			Items.NETHERITE_SCRAP,
			Items.NETHERITE_HOE,
			Items.NETHERITE_SHOVEL,
			Items.TURTLE_EGG
	));

	public static void setupCarrierBees() {
		for(EntityType<?> cb_entity : ForgeRegistries.ENTITIES){
			ResourceLocation rl = cb_entity.getRegistryName();
			if(rl != null &&
				rl.getNamespace().equals("carrierbees") &&
				!rl.getPath().contains("comb") &&
				!rl.getPath().contains("projectile"))
			{
				if(rl.getPath().equals("bomble_bee")){
					BOMBLE_BEE = cb_entity;
					CB_BEE_LIST.add(cb_entity);
				}
				else if(rl.getPath().equals("carrier_bee")){
					CARRIER_BEE = cb_entity;
				}
				else{
					// All other bees including future ones
					CB_BEE_LIST.add(cb_entity);
				}
			}
		}

		// Keep at end so it is only set to true if no exceptions was thrown during setup
		ModChecker.carrierBeesPresent = true;
	}

	/**
	 * Spawn Carrier Bees near player who has Wrath of the Hive
	 */
	public static void CBMobSpawn(LivingEntity entity) {
		if(entity.world.isRemote()) return;

		ServerWorld world = (ServerWorld) entity.world;
		Entity beeEntity = null;

		// Must be very low as this method is fired every tick for
		// status effects. We don't want to spawn millions of bees
		if(world.rand.nextFloat() > 0.0125f){
			return;
		}

		if (CB_BEE_LIST.size() == 0) {
			Bumblezone.LOGGER.warn(
					"Error! List of Carrier Bees is somehow empty! Cannot spawn their bees. " +
					"Please let TelepathicGrunt (The Bumblezone dev) know about this!");
			return;
		}

		// Grab a nearby air materialposition not in the player's field of view
		BlockPos spawnBlockPos = new BlockPos(
				entity.getPosX() + (world.rand.nextInt(30) + 10) * (world.rand.nextBoolean() ? 1 : -1),
				entity.getPosY() + (world.rand.nextInt(30) + 10) * (world.rand.nextBoolean() ? 1 : -1),
				entity.getPosZ() + (world.rand.nextInt(30) + 10) * (world.rand.nextBoolean() ? 1 : -1));
		if(world.getBlockState(spawnBlockPos).getMaterial() != Material.AIR){
			return;
		}

		// Increase chance of bomb bee spawn if player are blocking bee attacks
		if(BOMBLE_BEE != null && entity.isActiveItemStackBlocking() && world.rand.nextFloat() < 0.55f){
			beeEntity = BOMBLE_BEE.create(world);
		}

		// Set and spawn the carrier bee with an entity
		if(beeEntity == null && CARRIER_BEE != null && world.rand.nextFloat() < 0.75f){
			beeEntity = CARRIER_BEE.create(world);
			if(beeEntity != null){
				CarrierBeeEntity carrierBeeEntity = (CarrierBeeEntity) beeEntity;
				carrierBeeEntity.setHeldItem(Hand.MAIN_HAND, new ItemStack(
						CARRIER_BEES_ITEMS.get(world.rand.nextInt(world.rand.nextInt(CARRIER_BEES_ITEMS.size()) + 1))
				));
			}
		}

		// Pick a random bee left. (Will be Fumble bee and Bomblebee until CarrierBees adds more bees)
		if(beeEntity == null){
			beeEntity = CB_BEE_LIST.get(world.getRandom().nextInt(CB_BEE_LIST.size())).create(world);
			if(beeEntity == null) return; // if still null, just wtf
		}

		// try and make CB bee not mad no matter what
		beeEntity.setLocationAndAngles(
				spawnBlockPos.getX(),
				spawnBlockPos.getY(),
				spawnBlockPos.getZ(),
				world.getRandom().nextFloat() * 360.0F,
				0.0F);

		((MobEntity)beeEntity).onInitialSpawn(
				world,
				world.getDifficultyForLocation(beeEntity.getPosition()),
				SpawnReason.EVENT,
				null,
				null);

		if(beeEntity instanceof AppleBeeEntity){
			((AppleBeeEntity) beeEntity).setAttackTarget(entity);
			((AppleBeeEntity) beeEntity).setRevengeTarget(entity);
		}

		world.addEntity(beeEntity);
	}
}
