package com.telepathicgrunt.the_bumblezone.world.dimension;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.capabilities.BzCapabilities;
import com.telepathicgrunt.the_bumblezone.capabilities.EntityPositionAndDimension;
import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationBackend;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BzWorldSavedData extends SavedData {
	private static final String TELEPORTATION_DATA = Bumblezone.MODID + "teleportation";
	private static final BzWorldSavedData CLIENT_DUMMY = new BzWorldSavedData(null);
	private static final List<Pair<Entity, ResourceKey<Level>>> QUEUED_ENTITIES_TO_TELEPORT = new ArrayList<>();

	public BzWorldSavedData(CompoundTag tag) {}

	public static BzWorldSavedData get(Level world) {
		if (!(world instanceof ServerLevel)) {
			return CLIENT_DUMMY;
		}

		DimensionDataStorage storage = ((ServerLevel)world).getDataStorage();
		return storage.get(BzWorldSavedData::new, TELEPORTATION_DATA);
	}

	@Override
	public CompoundTag save(CompoundTag data)
	{
		return null;
	}

	public static void queueEntityToTeleport(Entity entity, ResourceKey<Level> destination) {
		if(!isEntityQueuedToTeleportAlready(entity)) {
			QUEUED_ENTITIES_TO_TELEPORT.add(Pair.of(entity, destination));
		}
	}

	public static boolean isEntityQueuedToTeleportAlready(Entity entity) {
		return QUEUED_ENTITIES_TO_TELEPORT.stream().anyMatch(entry -> entry.getFirst().equals(entity));
	}

	public static void worldTick(TickEvent.WorldTickEvent event){
		if(event.phase == TickEvent.Phase.END && !event.world.isClientSide()){
			BzWorldSavedData.tick((ServerLevel) event.world);
		}
	}

	public static void tick(ServerLevel world) {
		if(QUEUED_ENTITIES_TO_TELEPORT.size() == 0) return;

		Set<Entity> teleportedEntities = new HashSet<>();
		for (Pair<Entity, ResourceKey<Level>> entry : QUEUED_ENTITIES_TO_TELEPORT) {
			// Skip entities that were already teleported due to riding a vehicle that teleported
			Entity entity = entry.getFirst();
			if(teleportedEntities.contains(entity)) continue;

			ResourceKey<Level> destinationKey = entry.getSecond();
			ServerLevel destination = world.getLevel().getServer().getLevel(destinationKey);

			// Teleport the entity's root vehicle and its passengers to the desired dimension.
			// Also updates teleportedEntities to keep track of which entity was teleported.
			if (destinationKey.equals(BzDimension.BZ_WORLD_KEY)) {
				enteringBumblezone(entity, teleportedEntities);
			}
			else {
				exitingBumblezone(entity, destination, teleportedEntities);
			}
		}

		// remove all entities that were teleported from the queue
		QUEUED_ENTITIES_TO_TELEPORT.removeIf(entry -> teleportedEntities.contains(entry.getFirst()));
	}

	public static void enteringBumblezone(Entity entity, Set<Entity> teleportedEntities) {
		//Note, the player does not hold the previous dimension oddly enough.
		Vec3 destinationPosition;

		if (!entity.level.isClientSide()) {
			EntityPositionAndDimension capability = entity.getCapability(BzCapabilities.ENTITY_POS_AND_DIM_CAPABILITY).orElseThrow(RuntimeException::new);
			capability.setNonBZPos(entity.position());
			capability.setNonBZDim(entity.level.dimension().location());

			MinecraftServer minecraftServer = entity.getServer(); // the server itself
			ServerLevel bumblezoneWorld = minecraftServer.getLevel(BzDimension.BZ_WORLD_KEY);

			// Prevent crash due to mojang bug that makes mod's json dimensions not exist upload first creation of world on server. A restart fixes this.
			if(bumblezoneWorld == null) {
				if(entity instanceof ServerPlayer playerEntity) {
					Bumblezone.LOGGER.log(org.apache.logging.log4j.Level.INFO, "Bumblezone: Please restart the server. The Bumblezone dimension hasn't been made yet due to this bug: https://bugs.mojang.com/browse/MC-195468. A restart will fix this.");
					TextComponent message = new TextComponent("Please restart the server. The Bumblezone dimension hasn't been made yet due to this bug: §6https://bugs.mojang.com/browse/MC-195468§f. A restart will fix this.");
					playerEntity.displayClientMessage(message, true);
				}
				return;
			}

			destinationPosition = EntityTeleportationBackend.getBzCoordinate(entity, (ServerLevel)entity.level, bumblezoneWorld);
			Entity baseVehicle = entity.getRootVehicle();
			teleportEntityAndAssignToVehicle(baseVehicle, null, bumblezoneWorld, destinationPosition, teleportedEntities);
			((ServerLevel) entity.level).resetEmptyTime();
			bumblezoneWorld.resetEmptyTime();
		}
	}

	public static void exitingBumblezone(Entity entity, ServerLevel destination, Set<Entity> teleportedEntities) {
		boolean upwardChecking = entity.getY() > 0;
		Vec3 destinationPosition;
		destinationPosition = EntityTeleportationBackend.destPostFromOutOfBoundsTeleport(entity, destination, upwardChecking, false);
		if(destinationPosition == null) {
			((ServerPlayer)entity).displayClientMessage(new TextComponent("Error teleporting out of Bumblezone. destinationPosition is null. Report to Bumblezone dev pls."), true);
		}
		Entity baseVehicle = entity.getRootVehicle();
		teleportEntityAndAssignToVehicle(baseVehicle, null, destination, destinationPosition, teleportedEntities);
		((ServerLevel) entity.level).resetEmptyTime();
		destination.resetEmptyTime();
	}


	private static void teleportEntityAndAssignToVehicle(Entity entity, Entity vehicle, ServerLevel destination, Vec3 destinationPosition, Set<Entity> teleportedEntities) {
		Entity teleportedEntity;
		List<Entity> passengers = entity.getPassengers();
		entity.ejectPassengers();
		entity.setPortalCooldown();

		if (entity instanceof ServerPlayer) {
			if(destination.dimension().equals(BzDimension.BZ_WORLD_KEY)) {
				((ServerPlayer) entity).displayClientMessage(new TextComponent("Teleporting into the Bumblezone..."), true);
			}
			else {
				((ServerPlayer) entity).displayClientMessage(new TextComponent("Teleporting out of Bumblezone..."), true);
			}

			if (((ServerPlayer) entity).isSleeping()) {
				((ServerPlayer) entity).stopSleepInBed(true, true);
			}
			((ServerPlayer) entity).teleportTo(destination, destinationPosition.x, destinationPosition.y, destinationPosition.z, entity.getYRot(), entity.getXRot());
			teleportedEntity = destination.getPlayerByUUID(entity.getUUID());
		}
		else {
			Entity newEntity = entity;
			newEntity = newEntity.getType().create(destination);
			if (newEntity == null) {
				return;
			}
			entity.moveTo(destinationPosition.x, destinationPosition.y, destinationPosition.z, entity.getYRot(), entity.getXRot());
			newEntity.restoreFrom(entity);
			newEntity.moveTo(destinationPosition.x, destinationPosition.y, destinationPosition.z, entity.getYRot(), entity.getXRot());
			destination.addDuringTeleport(newEntity);
			teleportedEntity = newEntity;
			entity.remove(Entity.RemovalReason.CHANGED_DIMENSION);
		}

		if(teleportedEntity != null) {
			// update set to keep track of entities teleported
			teleportedEntities.add(entity);

			ChunkPos chunkpos = new ChunkPos(new BlockPos(destinationPosition.x, destinationPosition.y, destinationPosition.z));
			destination.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chunkpos, 1, entity.getId());

			if(vehicle != null) {
				teleportedEntity.startRiding(vehicle);
			}
			if(teleportedEntity instanceof LivingEntity) {
				reAddStatusEffect((LivingEntity) teleportedEntity);
			}

			passengers.forEach(passenger -> teleportEntityAndAssignToVehicle(passenger, teleportedEntity, destination, destinationPosition, teleportedEntities));
		}
	}


	///////////
	// Utils //

	/**
	 * Temporary fix until Mojang patches the bug that makes potion effect icons disappear when changing dimension.
	 * To fix it ourselves, we remove the effect and re-add it to the player.
	 */
	private static void reAddStatusEffect(LivingEntity livingEntity) {
		//re-adds potion effects so the icon remains instead of disappearing when changing dimensions due to a bug
		ArrayList<MobEffectInstance> effectInstanceList = new ArrayList<>(livingEntity.getActiveEffects());
		for (int i = effectInstanceList.size() - 1; i >= 0; i--) {
			MobEffectInstance effectInstance = effectInstanceList.get(i);
			if (effectInstance != null) {
				livingEntity.removeEffect(effectInstance.getEffect());
				livingEntity.addEffect(
						new MobEffectInstance(
								effectInstance.getEffect(),
								effectInstance.getDuration(),
								effectInstance.getAmplifier(),
								effectInstance.isAmbient(),
								effectInstance.isVisible(),
								effectInstance.showIcon()));
			}
		}
	}

}