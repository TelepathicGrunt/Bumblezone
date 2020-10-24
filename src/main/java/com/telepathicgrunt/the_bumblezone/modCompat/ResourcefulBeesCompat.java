package com.telepathicgrunt.the_bumblezone.modCompat;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import cy.jdkdigital.productivebees.entity.bee.ConfigurableBeeEntity;
import cy.jdkdigital.productivebees.init.ModBlocks;
import cy.jdkdigital.productivebees.init.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IServerWorld;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import org.apache.logging.log4j.Level;

import java.util.*;

public class ResourcefulBeesCompat {

	private static final String RESOURCEFUL_BEES_NAMESPACE = "resourcefulbees";
	private static final List<EntityType<?>> RESOURCEFUL_BEES_LIST = new ArrayList<>();

	public static void setupResourcefulBees() {
		ModChecker.resourcefulBeesPresent = true;

		for(Map.Entry<RegistryKey<EntityType<?>>, EntityType<?>> entry : Registry.ENTITY_TYPE.getEntries()){
			if(entry.getKey().getValue().getNamespace().equals(RESOURCEFUL_BEES_NAMESPACE)){
				RESOURCEFUL_BEES_LIST.add(entry.getValue());
			}
		}
		Bumblezone.LOGGER.log(Level.WARN, RESOURCEFUL_BEES_LIST);
	}


	/**
	 * 1/15th of bees spawning will also spawn Resourceful Bees' bees
	 */
	public static void RBMobSpawnEvent(LivingSpawnEvent.CheckSpawn event) {

		if (RESOURCEFUL_BEES_LIST.size() == 0) {
			Bumblezone.LOGGER.warn(
					"Error! List of Resourceful bees is empty! Cannot spawn their bees. " +
					"Please let TelepathicGrunt (The Bumblezone dev) know about this!");
			return;
		}

		MobEntity entity = (MobEntity) event.getEntity();
		IServerWorld world = (IServerWorld) event.getWorld();

		// randomly pick a Resourceful bee (the nbt determines the bee)
		MobEntity resourcefulBeeEntity = (MobEntity) RESOURCEFUL_BEES_LIST.get(world.getRandom().nextInt(RESOURCEFUL_BEES_LIST.size())).create(entity.world);
		if(resourcefulBeeEntity == null) return;

		BlockPos.Mutable blockpos = new BlockPos.Mutable().setPos(entity.getBlockPos());
		resourcefulBeeEntity.setLocationAndAngles(
				blockpos.getX(),
				blockpos.getY(),
				blockpos.getZ(),
				world.getRandom().nextFloat() * 360.0F,
				0.0F);

		resourcefulBeeEntity.onInitialSpawn(
				world,
				world.getDifficultyForLocation(resourcefulBeeEntity.getBlockPos()),
				event.getSpawnReason(),
				null,
				null);

		world.addEntity(resourcefulBeeEntity);
	}
}
