package com.telepathicgrunt.the_bumblezone.world.dimension;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.capabilities.EntityPositionAndDimension;
import com.telepathicgrunt.the_bumblezone.capabilities.IEntityPosAndDim;
import com.telepathicgrunt.the_bumblezone.entities.EntityTeleportationBackend;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.TickEvent;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BzWorldSavedData extends WorldSavedData {
    private static final String TELEPORTATION_DATA = Bumblezone.MODID + "teleportation";
    private static final BzWorldSavedData CLIENT_DUMMY = new BzWorldSavedData();
    private static final List<Pair<Entity, RegistryKey<World>>> QUEUED_ENTITIES_TO_TELEPORT = new ArrayList<>();

    @CapabilityInject(IEntityPosAndDim.class)
    public static Capability<IEntityPosAndDim> PAST_POS_AND_DIM = null;

    public BzWorldSavedData() {
        super(TELEPORTATION_DATA);
    }

    public static BzWorldSavedData get(World world) {
        if (!(world instanceof ServerWorld)) {
            return CLIENT_DUMMY;
        }

        DimensionSavedDataManager storage = ((ServerWorld) world).getDataStorage();
        return storage.get(BzWorldSavedData::new, TELEPORTATION_DATA);
    }

    @Override
    public void load(CompoundNBT data) {
    }

    @Override
    public CompoundNBT save(CompoundNBT data) {
        return null;
    }

    public static void queueEntityToTeleport(Entity entity, RegistryKey<World> destination) {
        if(!isEntityQueuedToTeleportAlready(entity)) {
            QUEUED_ENTITIES_TO_TELEPORT.add(Pair.of(entity, destination));
        }
    }

    public static boolean isEntityQueuedToTeleportAlready(Entity entity) {
        return QUEUED_ENTITIES_TO_TELEPORT.stream().anyMatch(entry -> entry.getFirst().equals(entity));
    }

    public static void worldTick(TickEvent.WorldTickEvent event){
        if(event.phase == TickEvent.Phase.END && !event.world.isClientSide()){
            BzWorldSavedData.tick((ServerWorld) event.world);
        }
    }

    public static void tick(ServerWorld world) {
        if(QUEUED_ENTITIES_TO_TELEPORT.size() == 0) return;

        Set<Entity> teleportedEntities = new HashSet<>();
        for (Pair<Entity, RegistryKey<World>> entry : QUEUED_ENTITIES_TO_TELEPORT) {
            // Skip entities that were already teleported due to riding a vehicle that teleported
            Entity entity = entry.getFirst();
            if(teleportedEntities.contains(entity)) continue;

            RegistryKey<World> destinationKey = entry.getSecond();
            ServerWorld destination = world.getWorldServer().getServer().getLevel(destinationKey);

            // Teleport the entity's root vehicle and its passengers to the desired dimension.
            // Also updates teleportedEntities to keep track of which entity was teleported.
            if (destinationKey.equals(BzDimension.BZ_WORLD_KEY)) {
                enteringBumblezone(entity, teleportedEntities);
            } else {
                exitingBumblezone(entity, destination, teleportedEntities);
            }
        }

        // remove all entities that were teleported from the queue
        QUEUED_ENTITIES_TO_TELEPORT.removeIf(entry -> teleportedEntities.contains(entry.getFirst()));
    }

    public static void enteringBumblezone(Entity entity, Set<Entity> teleportedEntities) {
        //Note, the player does not hold the previous dimension oddly enough.
        Vector3d destinationPosition;

        if (!entity.level.isClientSide()) {
            EntityPositionAndDimension cap = (EntityPositionAndDimension) entity.getCapability(PAST_POS_AND_DIM).orElseThrow(RuntimeException::new);
            cap.setNonBZDim(entity.getCommandSenderWorld().dimension().location());
            cap.setNonBZPos(entity.position());

            MinecraftServer minecraftServer = entity.getServer(); // the server itself
            ServerWorld bumblezoneWorld = minecraftServer.getLevel(BzDimension.BZ_WORLD_KEY);

            // Prevent crash due to mojang bug that makes mod's json dimensions not exist upload first creation of world on server. A restart fixes this.
            if(bumblezoneWorld == null){
                if(entity instanceof ServerPlayerEntity){
                    ServerPlayerEntity playerEntity = ((ServerPlayerEntity) entity);
                    Bumblezone.LOGGER.log(Level.INFO, "Bumblezone: Please restart the server. The Bumblezone dimension hasn't been made yet due to this bug: https://bugs.mojang.com/browse/MC-195468. A restart will fix this.");
                    ITextComponent message = new StringTextComponent("Please restart the server. The Bumblezone dimension hasn't been made yet due to this bug: §6https://bugs.mojang.com/browse/MC-195468§f. A restart will fix this.");
                    playerEntity.displayClientMessage(message, true);
                }
                return;
            }

            destinationPosition = EntityTeleportationBackend.getBzCoordinate(entity, (ServerWorld)entity.level, bumblezoneWorld);
            Entity baseVehicle = entity.getRootVehicle();
            teleportEntityAndAssignToVehicle(baseVehicle, null, bumblezoneWorld, destinationPosition, teleportedEntities);
            ((ServerWorld) entity.level).resetEmptyTime();
            bumblezoneWorld.resetEmptyTime();
        }
    }

    public static void exitingBumblezone(Entity entity, ServerWorld destination, Set<Entity> teleportedEntities) {
        boolean upwardChecking = entity.getY() > 0;
        Vector3d destinationPosition;
        destinationPosition = EntityTeleportationBackend.destPostFromOutOfBoundsTeleport(entity, destination, upwardChecking, false);
        if(destinationPosition == null) {
            ((ServerPlayerEntity)entity).displayClientMessage(new StringTextComponent("Error teleporting out of Bumblezone. destinationPosition is null. Report to Bumblezone dev pls."), true);
        }
        Entity baseVehicle = entity.getRootVehicle();
        teleportEntityAndAssignToVehicle(baseVehicle, null, destination, destinationPosition, teleportedEntities);
        ((ServerWorld) entity.level).resetEmptyTime();
        destination.resetEmptyTime();
    }


    private static void teleportEntityAndAssignToVehicle(Entity entity, Entity vehicle, ServerWorld destination, Vector3d destinationPosition, Set<Entity> teleportedEntities) {
        net.minecraft.entity.Entity teleportedEntity;
        List<net.minecraft.entity.Entity> passengers = entity.getPassengers();

        if (entity instanceof ServerPlayerEntity) {
            if(destination.dimension().equals(BzDimension.BZ_WORLD_KEY)) {
                ((ServerPlayerEntity) entity).displayClientMessage(new StringTextComponent("Teleporting into the Bumblezone..."), true);
            }
            else {
                ((ServerPlayerEntity) entity).displayClientMessage(new StringTextComponent("Teleporting out of Bumblezone..."), true);
            }

            if (((ServerPlayerEntity)entity).isSleeping()) {
                ((ServerPlayerEntity) entity).stopSleepInBed(true, true);
            }
            ((ServerPlayerEntity) entity).teleportTo(destination, destinationPosition.x, destinationPosition.y, destinationPosition.z, entity.yRot, entity.xRot);
            teleportedEntity = destination.getPlayerByUUID(entity.getUUID());
        }
        else {
            net.minecraft.entity.Entity newEntity = entity;
            newEntity = newEntity.getType().create(destination);
            if (newEntity == null) {
                return;
            }

            newEntity.restoreFrom(entity);
            newEntity.moveTo(destinationPosition.x, destinationPosition.y, destinationPosition.z, entity.yRot, entity.xRot);
            destination.addFromAnotherDimension(newEntity);
            teleportedEntity = newEntity;
            entity.remove();
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
        ArrayList<EffectInstance> effectInstanceList = new ArrayList<>(livingEntity.getActiveEffects());
        for (int i = effectInstanceList.size() - 1; i >= 0; i--) {
            EffectInstance effectInstance = effectInstanceList.get(i);
            if (effectInstance != null) {
                livingEntity.removeEffect(effectInstance.getEffect());
                livingEntity.addEffect(
                        new EffectInstance(
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