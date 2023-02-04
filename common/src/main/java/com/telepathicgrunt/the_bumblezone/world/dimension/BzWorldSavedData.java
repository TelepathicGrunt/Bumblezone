package com.telepathicgrunt.the_bumblezone.world.dimension;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationBackend;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.ServerLevelTickEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHelper;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistry;
import com.telepathicgrunt.the_bumblezone.utils.ThreadExecutor;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.phys.Vec3;

import java.util.*;


public class BzWorldSavedData extends SavedData {
	private static final String TELEPORTATION_DATA = Bumblezone.MODID + "teleportation";
	private static final BzWorldSavedData CLIENT_DUMMY = new BzWorldSavedData(null);
	private static final List<QueuedEntityData> QUEUED_ENTITIES_TO_TELEPORT = new ArrayList<>();

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
			QUEUED_ENTITIES_TO_TELEPORT.add(new QueuedEntityData(entity, destination));
		}
	}

	public static boolean isEntityQueuedToTeleportAlready(Entity entity) {
		return QUEUED_ENTITIES_TO_TELEPORT.stream().anyMatch(entry -> entry.getEntity().equals(entity));
	}

	public static void worldTick(ServerLevelTickEvent event){
		if(event.end()){
			BzWorldSavedData.tick((ServerLevel) event.getLevel());
		}
	}

	public static void tick(ServerLevel world) {
		if(QUEUED_ENTITIES_TO_TELEPORT.size() == 0) return;

		Set<Entity> teleportedEntities = new HashSet<>();
		for (QueuedEntityData entry : QUEUED_ENTITIES_TO_TELEPORT) {
			if (!entry.getIsCurrentTeleporting()) {
				entry.setIsCurrentTeleporting(true);
				ResourceKey<Level> destinationKey = entry.getDestination();
				if (destinationKey.equals(BzDimension.BZ_WORLD_KEY)) {
					if (entry.getEntity() instanceof ServerPlayer serverPlayer) {
						serverPlayer.displayClientMessage(Component.translatable("system.the_bumblezone.teleporting_into_bz"), true);
					}

					ThreadExecutor.dimensionDestinationSearch(world.getServer(), () -> {
							try {
								ServerLevel bumblezoneWorld = world.getServer().getLevel(BzDimension.BZ_WORLD_KEY);
								return Optional.of(EntityTeleportationBackend.getBzCoordinate(entry.getEntity(), world, bumblezoneWorld));
							}
							catch (Throwable e){
								Bumblezone.LOGGER.error("Bumblezone: Failed to teleport entity. Error:", e);
								return Optional.empty();
							}
						})
						.thenOnServerThread(entry::setDestinationPosFound);
				}
				else {
					if (entry.getEntity() instanceof ServerPlayer serverPlayer) {
						serverPlayer.displayClientMessage(Component.translatable("system.the_bumblezone.teleporting_out_of_bz"), true);
					}

					ThreadExecutor.dimensionDestinationSearch(world.getServer(), () -> {
							try {
								boolean upwardChecking = entry.getEntity().getY() > 0;
								ServerLevel destination = world.getLevel().getServer().getLevel(destinationKey);
								return Optional.of(EntityTeleportationBackend.destPostFromOutOfBoundsTeleport(entry.getEntity(), destination, upwardChecking));
							}
							catch (Throwable e){
								Bumblezone.LOGGER.error("Bumblezone: Failed to teleport entity. Error:", e);
								return Optional.empty();
							}
						})
						.thenOnServerThread(entry::setDestinationPosFound);
				}
			}
			else if (entry.getDestinationPosFound() != null) {
				// Skip entities that were already teleported due to riding a vehicle that teleported
				Entity entity = entry.getEntity();
				if(teleportedEntities.contains(entity)) continue;

				ResourceKey<Level> destinationKey = entry.getDestination();
				ServerLevel destination = world.getLevel().getServer().getLevel(destinationKey);

				if (entry.getDestinationPosFound().isPresent()) {
					Vec3 destinationPos = entry.getDestinationPosFound().get();
					// Teleport the entity's root vehicle and its passengers to the desired dimension.
					// Also updates teleportedEntities to keep track of which entity was teleported.
					if (destinationKey.equals(BzDimension.BZ_WORLD_KEY)) {
						enteringBumblezone(entity, destinationPos, teleportedEntities);
					}
					else {
						if (entity.getControllingPassenger() != null) {
							exitingBumblezone(entity.getControllingPassenger(), destinationPos, destination, teleportedEntities);
						}
						else {
							exitingBumblezone(entity, destinationPos, destination, teleportedEntities);
						}
					}
				}
				else {
					teleportedEntities.add(entity);
					if (entity instanceof ServerPlayer serverPlayer) {
						serverPlayer.displayClientMessage(Component.translatable("system.the_bumblezone.failed_teleporting"), false);
						Bumblezone.LOGGER.error("Bumblezone: Failed to teleport entity. Aborting teleportation. Please retry. Entity: {}-{} Pos: {} Destination: {}", entity.getClass().getSimpleName(), entity.getName(), entity.position(), destinationKey);
					}
				}
			}
		}
		// remove all entities that were teleported from the queue
		QUEUED_ENTITIES_TO_TELEPORT.removeIf(entry -> teleportedEntities.contains(entry.getEntity()));
	}

	public static void enteringBumblezone(Entity entity, Vec3 destinationPosFound, Set<Entity> teleportedEntities) {
		//Note, the player does not hold the previous dimension oddly enough.
		if (!entity.level.isClientSide()) {
			MinecraftServer minecraftServer = entity.getServer(); // the server itself
			ServerLevel bumblezoneWorld = minecraftServer.getLevel(BzDimension.BZ_WORLD_KEY);
			BlockPos blockPos = new BlockPos(destinationPosFound);

			if (bumblezoneWorld != null && bumblezoneWorld.getBlockState(blockPos.above()).isSuffocating(bumblezoneWorld, blockPos.above())) {
				//We are going to spawn player at exact spot of scaled coordinates by placing air at the spot with honeycomb bottom
				//and honeycomb walls to prevent drowning
				//This is the last resort
				bumblezoneWorld.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
				bumblezoneWorld.setBlockAndUpdate(blockPos.above(), Blocks.AIR.defaultBlockState());

				bumblezoneWorld.setBlockAndUpdate(blockPos.below(), Blocks.HONEYCOMB_BLOCK.defaultBlockState());
				bumblezoneWorld.setBlockAndUpdate(blockPos.above().above(), Blocks.HONEYCOMB_BLOCK.defaultBlockState());

				bumblezoneWorld.setBlockAndUpdate(blockPos.north(), Blocks.HONEYCOMB_BLOCK.defaultBlockState());
				bumblezoneWorld.setBlockAndUpdate(blockPos.west(), Blocks.HONEYCOMB_BLOCK.defaultBlockState());
				bumblezoneWorld.setBlockAndUpdate(blockPos.east(), Blocks.HONEYCOMB_BLOCK.defaultBlockState());
				bumblezoneWorld.setBlockAndUpdate(blockPos.south(), Blocks.HONEYCOMB_BLOCK.defaultBlockState());
				bumblezoneWorld.setBlockAndUpdate(blockPos.north().above(), Blocks.HONEYCOMB_BLOCK.defaultBlockState());
				bumblezoneWorld.setBlockAndUpdate(blockPos.west().above(), Blocks.HONEYCOMB_BLOCK.defaultBlockState());
				bumblezoneWorld.setBlockAndUpdate(blockPos.east().above(), Blocks.HONEYCOMB_BLOCK.defaultBlockState());
				bumblezoneWorld.setBlockAndUpdate(blockPos.south().above(), Blocks.HONEYCOMB_BLOCK.defaultBlockState());
			}

			ModuleHelper.getModule(entity, ModuleRegistry.ENTITY_POS_AND_DIM).ifPresent(capability -> {
				capability.setNonBZPos(entity.position());
				capability.setNonBZDim(entity.level.dimension().location());

				// Prevent crash due to mojang bug that makes mod's json dimensions not exist upload first creation of world on server. A restart fixes this.
				if (bumblezoneWorld == null) {
					if (entity instanceof ServerPlayer playerEntity) {
						Bumblezone.LOGGER.info("Bumblezone: Please restart the server. The Bumblezone dimension hasn't been made yet due to this bug: https://bugs.mojang.com/browse/MC-195468. A restart will fix this.");
						MutableComponent message = Component.translatable("system.the_bumblezone.missing_dimension", Component.translatable("system.the_bumblezone.missing_dimension_link").withStyle(ChatFormatting.RED));
						playerEntity.displayClientMessage(message, false);
					}
					teleportedEntities.add(entity);
					return;
				}

				Entity baseVehicle = entity.getRootVehicle();
				teleportEntityAndAssignToVehicle(baseVehicle, null, bumblezoneWorld, destinationPosFound, teleportedEntities);
				((ServerLevel) entity.level).resetEmptyTime();
				bumblezoneWorld.resetEmptyTime();
			});
		}
	}

	public static void exitingBumblezone(Entity entity, Vec3 destinationPosition, ServerLevel destination, Set<Entity> teleportedEntities) {
		BlockPos destBlockPos = new BlockPos(destinationPosition);
		if (destination.getBlockState(destBlockPos.above()).isSuffocating(destination, destBlockPos.above())) {
			destination.setBlock(destBlockPos, Blocks.AIR.defaultBlockState(), 3);
			destination.setBlock(destBlockPos.above(), Blocks.AIR.defaultBlockState(), 3);
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

		if(destination.dimension().equals(BzDimension.BZ_WORLD_KEY)) {
			ModuleHelper.getModule(entity, ModuleRegistry.ENTITY_POS_AND_DIM).ifPresent(capability -> {
				capability.setNonBZPos(entity.position());
				capability.setNonBZDim(entity.level.dimension().location());
			});
		}

		if (entity instanceof ServerPlayer serverPlayer) {

			if (serverPlayer.isSleeping()) {
				serverPlayer.stopSleepInBed(true, true);
			}

			//serverPlayer.moveTo(destinationPosition.x, destinationPosition.y, destinationPosition.z);
			serverPlayer.teleportTo(destination, destinationPosition.x, destinationPosition.y, destinationPosition.z, serverPlayer.getYRot(), serverPlayer.getXRot());
			teleportedEntity = destination.getPlayerByUUID(serverPlayer.getUUID());
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

	private static final class QueuedEntityData {
		private final Entity entity;
		private final ResourceKey<Level> destination;
		private boolean isCurrentTeleporting = false;
		private Optional<Vec3> destinationPosFound = null;

		public QueuedEntityData(Entity entity, ResourceKey<Level> destination) {
			this.entity = entity;
			this.destination = destination;
		}

		public Entity getEntity() {
			return entity;
		}

		public ResourceKey<Level> getDestination() {
			return destination;
		}

		public Optional<Vec3> getDestinationPosFound() {
			return destinationPosFound;
		}

		public void setDestinationPosFound(Optional<Vec3> destinationPosFound) {
			this.destinationPosFound = destinationPosFound;
		}

		public boolean getIsCurrentTeleporting() {
			return isCurrentTeleporting;
		}

		public void setIsCurrentTeleporting(boolean isCurrentTeleporting) {
			this.isCurrentTeleporting = isCurrentTeleporting;
		}
	}
}