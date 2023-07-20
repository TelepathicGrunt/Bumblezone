package com.telepathicgrunt.the_bumblezone.blocks.blockentities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.EssenceBlock;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.screens.ServerEssenceEvent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EssenceBlockEntity extends BlockEntity {
    private static final String UUID_TAG = "uuid";
    private static final String EVENT_TIMER_TAG = "eventTimer";
    private static final String PROGRESS_TAG = "eventBarProgress";
    private static final String PLAYERS_IN_ARENA_TAG = "playersInArena";
    private static final String EVENT_ENTITIES_IN_ARENA_TAG = "eventEntitiesInArena";
    private static final String EXTRA_EVENT_TRACKING_PROGRESS_TAG = "extraEventTrackingProgress";
    private static final String ARENA_SIZE_TAG = "arenaSize";

    private UUID uuid = null;
    private int eventTimer = 0;
    private List<UUID> playerInArena = new ArrayList<>();
    private ServerEssenceEvent eventBar = null;
    // Add object to hold all spawned entities of this event and wipe when done
    public record EventEntities(UUID uuid) {}
    private List<EventEntities> eventEntitiesInArena = Collections.synchronizedList(new ArrayList<>());
    private int extraEventTrackingProgress = 0;
    private BlockPos arenaSize = BlockPos.ZERO;

    protected EssenceBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        if (blockState.getBlock() instanceof EssenceBlock essenceBlock) {
            eventBar = essenceBlock.getServerEssenceEvent();
        }
    }

    public EssenceBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(BzBlockEntities.ESSENCE_BLOCK.get(), blockPos, blockState);
        if (blockState.getBlock() instanceof EssenceBlock essenceBlock) {
            eventBar = essenceBlock.getServerEssenceEvent();
        }
    }

    public int getEventTimer() {
        return this.eventTimer;
    }

    public void setEventTimer(int eventTimer) {
        this.eventTimer = eventTimer;
    }

    public UUID getUUID() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
        return this.uuid;
    }

    public List<UUID> getPlayerInArena() {
        return playerInArena;
    }

    public void setPlayerInArena(List<UUID> playerInArena) {
        this.playerInArena = playerInArena;
    }

    public ServerEssenceEvent getEventBar() {
        return eventBar;
    }

    public void setEventBar(ServerEssenceEvent eventBar) {
        this.eventBar = eventBar;
    }

    public List<EventEntities> getEventEntitiesInArena() {
        return eventEntitiesInArena;
    }

    public void setEventEntitiesInArena(List<EventEntities> eventEntitiesInArena) {
        this.eventEntitiesInArena = eventEntitiesInArena;
    }

    public int getExtraEventTrackingProgress() {
        return extraEventTrackingProgress;
    }

    public void setExtraEventTrackingProgress(int extraEventTrackingProgress) {
        this.extraEventTrackingProgress = extraEventTrackingProgress;
    }

    public BlockPos getArenaSize() {
        return arenaSize;
    }

    public void setArenaSize(BlockPos arenaSize) {
        this.arenaSize = arenaSize;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        if (compoundTag != null) {
            this.eventBar.setProgress(compoundTag.getFloat(PROGRESS_TAG));
            this.extraEventTrackingProgress = compoundTag.getInt(EXTRA_EVENT_TRACKING_PROGRESS_TAG);
            this.eventTimer = compoundTag.getInt(EVENT_TIMER_TAG);
            this.eventBar.setEndEventTimer(this.eventTimer);
            if (compoundTag.contains(UUID_TAG)) {
                this.uuid = compoundTag.getUUID(UUID_TAG);
            }
            else {
                this.uuid = UUID.randomUUID();
            }

            if (compoundTag.contains(PLAYERS_IN_ARENA_TAG)) {
                this.playerInArena.clear();
                for (Tag tag : compoundTag.getList(PLAYERS_IN_ARENA_TAG, Tag.TAG_INT_ARRAY)) {
                    this.playerInArena.add(NbtUtils.loadUUID(tag));
                }
            }

            if (compoundTag.contains(EVENT_ENTITIES_IN_ARENA_TAG)) {
                this.eventEntitiesInArena.clear();
                for (Tag tag : compoundTag.getList(EVENT_ENTITIES_IN_ARENA_TAG, Tag.TAG_INT_ARRAY)) {
                    this.eventEntitiesInArena.add(new EventEntities(NbtUtils.loadUUID(tag)));
                }
            }

            if (compoundTag.contains(ARENA_SIZE_TAG)) {
                this.arenaSize = NbtUtils.readBlockPos(compoundTag.getCompound(ARENA_SIZE_TAG));
            }
        }

        if (this.level instanceof ClientLevel) {
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 8);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        saveFieldsToTag(compoundTag);

        // In case player teleports away and chunk unloads this block
        if (this.getLevel() != null) {
            for (int i = this.getPlayerInArena().size() - 1; i >= 0; i--) {
                UUID playerUUID = this.getPlayerInArena().get(i);
                ServerPlayer serverPlayer = (ServerPlayer) this.getLevel().getPlayerByUUID(playerUUID);
                if (serverPlayer != null) {
                    if (serverPlayer.isDeadOrDying() ||
                        (Math.abs(serverPlayer.blockPosition().getX() - this.getBlockPos().getX()) > ((this.getArenaSize().getX() + 1) / 2) ||
                        Math.abs(serverPlayer.blockPosition().getY() - this.getBlockPos().getY()) > ((this.getArenaSize().getY() + 1) / 2) ||
                        Math.abs(serverPlayer.blockPosition().getZ() - this.getBlockPos().getZ()) > ((this.getArenaSize().getZ() + 1) / 2)))
                    {
                        this.getPlayerInArena().remove(playerUUID);
                        this.setChanged();
                        this.getEventBar().removePlayer(serverPlayer);
                    }
                }
            }
        }
    }

    private void saveFieldsToTag(CompoundTag compoundTag) {
        compoundTag.put(UUID_TAG, NbtUtils.createUUID(this.getUUID()));
        compoundTag.putInt(EVENT_TIMER_TAG, this.eventTimer);
        compoundTag.putInt(EXTRA_EVENT_TRACKING_PROGRESS_TAG, this.extraEventTrackingProgress);
        compoundTag.putFloat(PROGRESS_TAG, this.eventBar.getProgress());

        ListTag players = new ListTag();
        for (UUID target : this.playerInArena) {
            players.add(NbtUtils.createUUID(target));
        }
        compoundTag.put(PLAYERS_IN_ARENA_TAG, players);

        ListTag eventEntities = new ListTag();
        for (EventEntities target : this.eventEntitiesInArena) {
            eventEntities.add(NbtUtils.createUUID(target.uuid()));
        }
        compoundTag.put(EVENT_ENTITIES_IN_ARENA_TAG, eventEntities);
        compoundTag.put(ARENA_SIZE_TAG, NbtUtils.writeBlockPos(this.arenaSize));
    }

    public ResourceLocation getSavedNbt() {
        return new ResourceLocation(Bumblezone.MODID, "essence/saved_area/" +
                this.getBlockPos().getX() + "_" +
                this.getBlockPos().getY() + "_" +
                this.getBlockPos().getZ() + "_" +
                this.getUUID());
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveFieldsToTag(tag);
        return tag;
    }

    public boolean shouldDrawSide(Direction direction) {
        return Block.shouldRenderFace(this.getBlockState(), this.getLevel(), this.getBlockPos(), direction, this.getBlockPos().relative(direction));
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, EssenceBlockEntity essenceBlockEntity) {
        if (level instanceof ServerLevel serverLevel) {
            if (!essenceBlockEntity.getPlayerInArena().isEmpty()) {
                performArenaTick(serverLevel, blockPos, blockState, essenceBlockEntity);
            }
            else if (essenceBlockEntity.getEventTimer() > 0) {
                EndEvent(serverLevel, blockPos, blockState, essenceBlockEntity, false);
            }
        }
    }

    private static void performArenaTick(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, EssenceBlockEntity essenceBlockEntity) {
        boolean endEvent = false;

        if (serverLevel.getGameTime() % 20 == 0) {
            for (int i = essenceBlockEntity.getPlayerInArena().size() - 1; i >= 0; i--) {
                UUID playerUUID = essenceBlockEntity.getPlayerInArena().get(i);
                ServerPlayer serverPlayer = (ServerPlayer) serverLevel.getPlayerByUUID(playerUUID);
                if (serverPlayer != null) {
                    if (serverPlayer.isDeadOrDying() ||
                        (Math.abs(serverPlayer.blockPosition().getX() - blockPos.getX()) > ((essenceBlockEntity.getArenaSize().getX() + 1) / 2) ||
                        Math.abs(serverPlayer.blockPosition().getY() - blockPos.getY()) > ((essenceBlockEntity.getArenaSize().getY() + 1) / 2) ||
                        Math.abs(serverPlayer.blockPosition().getZ() - blockPos.getZ()) > ((essenceBlockEntity.getArenaSize().getZ() + 1) / 2)))
                    {
                        if (essenceBlockEntity.getBlockState().getBlock() instanceof EssenceBlock essenceBlock) {
                            essenceBlock.onEventEnd(serverLevel, essenceBlockEntity);
                        }
                        essenceBlockEntity.getPlayerInArena().remove(playerUUID);
                        essenceBlockEntity.setChanged();
                        essenceBlockEntity.getEventBar().removePlayer(serverPlayer);
                    }
                    else {
                        essenceBlockEntity.getEventBar().addPlayer(serverPlayer);
                    }
                }
            }
            if (essenceBlockEntity.getPlayerInArena().isEmpty()) {
                endEvent = true;
            }
        }

        if (essenceBlockEntity.getEventTimer() <= 0) {
            endEvent = true;
        }

        if (endEvent) {
            EndEvent(serverLevel, blockPos, blockState, essenceBlockEntity, false);
        }
        else {
            essenceBlockEntity.setEventTimer(essenceBlockEntity.getEventTimer() - 1);
            essenceBlockEntity.getEventBar().setEndEventTimer(essenceBlockEntity.getEventTimer());
            if (blockState.getBlock() instanceof EssenceBlock essenceBlock) {
                essenceBlock.performUniqueArenaTick(serverLevel, blockPos, blockState, essenceBlockEntity);
            }
        }
    }

    public static void EndEvent(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, EssenceBlockEntity essenceBlockEntity, boolean won) {
        Optional<StructureTemplate> optionalStructureTemplate = serverLevel.getStructureManager().get(essenceBlockEntity.getSavedNbt());
        optionalStructureTemplate.ifPresentOrElse(structureTemplate -> {
            Vec3i size = structureTemplate.getSize();
            BlockPos negativeHalfLengths = new BlockPos(-size.getX() / 2, -size.getY() / 2, -size.getZ() / 2);

            //reset area
            structureTemplate.placeInWorld(
                    serverLevel,
                    blockPos.offset(negativeHalfLengths),
                    blockPos.offset(negativeHalfLengths),
                    EssenceBlock.PLACEMENT_SETTINGS.getOrFillFromInternal(),
                    serverLevel.getRandom(),
                    Block.UPDATE_CLIENTS
            );

            for (UUID playerUUID : essenceBlockEntity.getPlayerInArena()) {
                ServerPlayer serverPlayer = (ServerPlayer) serverLevel.getPlayerByUUID(playerUUID);
                if (serverPlayer != null) {
                    if ((blockPos.getX() + size.getX()) > serverPlayer.blockPosition().getX() &&
                        (blockPos.getY() + size.getY()) > serverPlayer.blockPosition().getY() &&
                        (blockPos.getZ() + size.getZ()) > serverPlayer.blockPosition().getZ() &&
                        (blockPos.getX() - size.getX()) < serverPlayer.blockPosition().getX() &&
                        (blockPos.getY() - size.getY()) < serverPlayer.blockPosition().getY() &&
                        (blockPos.getZ() - size.getZ()) < serverPlayer.blockPosition().getZ())
                    {
                        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(serverLevel.getRandom());
                        serverPlayer.setDeltaMovement(0, 0, 0);
                        serverPlayer.teleportTo(
                                blockPos.getX() + (direction.getStepX() * ((negativeHalfLengths.getX() + 1) / -2f)) + 0.5f,
                                blockPos.getY() + negativeHalfLengths.getY() + 2,
                                blockPos.getZ() + (direction.getStepZ() * ((negativeHalfLengths.getZ() + 1) / -2f)) + 0.5f
                        );
                        serverPlayer.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atCenterOf(blockPos));
                        EssenceBlock.spawnParticles(serverLevel, serverPlayer.position(), serverPlayer.getRandom());

                        if (won) {
                            if (blockState.getBlock() instanceof EssenceBlock essenceBlock) {
                                if (!serverPlayer.addItem(essenceBlock.getEssenceItemReward())) {
                                    ItemStack reward = essenceBlock.getEssenceItemReward();
                                    BzCriterias.ESSENCE_EVENT_REWARD_TRIGGER.trigger(serverPlayer, reward);
                                    serverPlayer.drop(reward, false);
                                }
                                serverPlayer.giveExperiencePoints(essenceBlock.getEssenceXpReward());
                            }
                        }
                    }
                }
            }

            List<Entity> entities = serverLevel.getEntities(null, new AABB(
                    blockPos.getX() + size.getX(),
                    blockPos.getY() + size.getY(),
                    blockPos.getZ() + size.getZ(),
                    blockPos.getX() - size.getX(),
                    blockPos.getY() - size.getY(),
                    blockPos.getZ() - size.getZ())
            );

            // Remove lingering enemies in arena
            for (Entity entity : entities) {
                if ((entity instanceof NeutralMob neutralMob && neutralMob.getTarget() instanceof Player) ||
                    (entity instanceof Mob mob && mob.getTarget() instanceof Player))
                {
                    entity.remove(Entity.RemovalReason.DISCARDED);
                }
            }

            // remove missed entities
            for (EventEntities eventEntities : essenceBlockEntity.getEventEntitiesInArena()) {
                Entity entity = serverLevel.getEntity(eventEntities.uuid());
                if (entity != null) {
                    entity.remove(Entity.RemovalReason.DISCARDED);
                }
            }

            if (!essenceBlockEntity.getPlayerInArena().isEmpty() && won) {
                serverLevel.setBlock(blockPos, BzBlocks.HEAVY_AIR.get().defaultBlockState(), 3);
            }
        }, () -> Bumblezone.LOGGER.warn("Bumblezone Essence Block failed to restore area from saved NBT - {} - {}", essenceBlockEntity, blockState));

        essenceBlockEntity.getEventBar().removeAllPlayers();
        essenceBlockEntity.getPlayerInArena().clear();
        essenceBlockEntity.getEventEntitiesInArena().clear();
        essenceBlockEntity.setExtraEventTrackingProgress(0);
        essenceBlockEntity.setEventTimer(0);
        essenceBlockEntity.setArenaSize(BlockPos.ZERO);
        essenceBlockEntity.setChanged();
    }

    public void setRemoved() {
        super.setRemoved();
        this.getEventBar().removeAllPlayers();
    }
}