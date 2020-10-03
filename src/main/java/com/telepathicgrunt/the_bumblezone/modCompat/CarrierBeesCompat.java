package com.telepathicgrunt.the_bumblezone.modCompat;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.registries.ForgeRegistries;
import noobanidus.mods.carrierbees.entities.AppleBeeEntity;
import noobanidus.mods.carrierbees.entities.CarrierBeeEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CarrierBeesCompat {

	private static List<EntityType<?>> CB_BEE_LIST = new ArrayList<>();

	public static void setupProductiveBees() {
		ModChecker.productiveBeesPresent = true;

		for(EntityType<?> cb_entity : ForgeRegistries.ENTITIES){
			if(cb_entity.getRegistryName().getNamespace().equals("carrierbees")){
				CB_BEE_LIST.add(cb_entity);
			}
		}
	}

	/**
	 * 1/15th of bees spawning will also spawn Productive Bees' bees
	 */
	public static void CBMobSpawnEvent(LivingSpawnEvent.CheckSpawn event) {

		if (CB_BEE_LIST.size() == 0) {
			Bumblezone.LOGGER.warn(
					"Error! List of productive bees is empty! Cannot spawn their bees. " +
					"Please let TelepathicGrunt (The Bumblezone dev) know about this!");
			return;
		}

		MobEntity orgininalEntity = (MobEntity) event.getEntity();
		IServerWorld world = (IServerWorld) event.getWorld();

		Entity entity = CB_BEE_LIST.get(world.getRandom().nextInt(CB_BEE_LIST.size())).create(orgininalEntity.world);
		if(!(entity instanceof AppleBeeEntity)) return;

		// try and make CB bee not mad no matter what
		AppleBeeEntity cbEntity = ((AppleBeeEntity)entity);
		cbEntity.setRevengeTarget(null);
		cbEntity.setAttackTarget(null);
		try {
			Field angerField = AppleBeeEntity.class.getDeclaredField("anger");
			Field dataManagerField = Entity.class.getDeclaredField("dataManager");
			angerField.setAccessible(true);
			dataManagerField.setAccessible(true);

			((EntityDataManager)dataManagerField.get(cbEntity))
					.set((DataParameter<Integer>)angerField.get(cbEntity), 0);

			angerField.setAccessible(false);
			dataManagerField.setAccessible(false);
		}
		catch (Exception x) {
			x.printStackTrace();
		}

		BlockPos.Mutable blockpos = new BlockPos.Mutable().setPos(orgininalEntity.getBlockPos());
		cbEntity.setLocationAndAngles(
				blockpos.getX(),
				blockpos.getY(),
				blockpos.getZ(),
				world.getRandom().nextFloat() * 360.0F,
				0.0F);

		((MobEntity)cbEntity).onInitialSpawn(
				world,
				world.getDifficultyForLocation(cbEntity.getBlockPos()),
				event.getSpawnReason(),
				null,
				null);

		// If it is a carrier bee, set a random item.
		if(cbEntity instanceof CarrierBeeEntity){
			cbEntity.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.NETHERITE_SWORD));
		}

		world.addEntity(cbEntity);
	}
}
