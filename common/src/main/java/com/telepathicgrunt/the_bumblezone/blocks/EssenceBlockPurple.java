package com.telepathicgrunt.the_bumblezone.blocks;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.bossbars.ServerEssenceEvent;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.PurpleSpikeEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzStats;
import com.telepathicgrunt.the_bumblezone.packets.MusicPacketFromServer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.UUID;
import java.util.function.BiPredicate;


public class EssenceBlockPurple extends EssenceBlock {

    public static final MapCodec<EssenceBlockPurple> CODEC = Block.simpleCodec(EssenceBlockPurple::new);

    public static final int INTERVALS = 36;

    public EssenceBlockPurple() {
        this(Properties.of()
                .mapColor(MapColor.COLOR_PURPLE)
                .strength(-1.0f, 3600000.8f)
                .lightLevel((blockState) -> 15)
                .noLootTable()
                .forceSolidOn()
                .isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false)
                .isViewBlocking((blockState, blockGetter, blockPos) -> false)
                .pushReaction(PushReaction.BLOCK));
    }

    public EssenceBlockPurple(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends EssenceBlockPurple> codec() {
        return CODEC;
    }

    @Override
    public ResourceLocation getArenaNbt() {
        return new ResourceLocation(Bumblezone.MODID, "essence/purple_arena");
    }

    @Override
    public int getEventTimeFrame() {
        return 5020;
    }

    @Override
    public ServerEssenceEvent getServerEssenceEvent() {
        return (ServerEssenceEvent) new ServerEssenceEvent(
                "essence.the_bumblezone.purple_essence_event",
                BossEvent.BossBarColor.PURPLE,
                BossEvent.BossBarOverlay.PROGRESS
        ).setDarkenScreen(true);
    }

    @Override
    public ItemStack getEssenceItemReward() {
        return BzItems.ESSENCE_KNOWING.get().getDefaultInstance();
    }

    @Override
    public int getEssenceXpReward() {
        return 3000;
    }

    @Override
    public void awardPlayerWinStat(ServerPlayer serverPlayer) {
        serverPlayer.awardStat(BzStats.KNOWING_EVENT_DEFEATED_RL.get());
    }

    @Override
    public void performUniqueArenaTick(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, EssenceBlockEntity essenceBlockEntity) {
        if (essenceBlockEntity.getPlayerInArena().size() == 0) return;
        int timeRemaining = essenceBlockEntity.getEventTimer();

        List<EssenceBlockEntity.EventEntities> eventEntitiesInArena = essenceBlockEntity.getEventEntitiesInArena();
        if (eventEntitiesInArena.isEmpty()) {
            spawnEntities(serverLevel, blockPos, essenceBlockEntity, eventEntitiesInArena, false);
        }
        else {
            for (int i = eventEntitiesInArena.size() - 1; i >= 0; i--) {
                UUID entityToCheck = eventEntitiesInArena.get(i).uuid();
                Entity entity = serverLevel.getEntity(entityToCheck);
                if (entity == null) {
                    List<PurpleSpikeEntity> nearbyRings = serverLevel.getEntitiesOfClass(
                            PurpleSpikeEntity.class,
                            new AABB(
                                    blockPos.getX() - (essenceBlockEntity.getArenaSize().getX() * 0.5f),
                                    blockPos.getY() - (essenceBlockEntity.getArenaSize().getY() * 0.5f),
                                    blockPos.getZ() - (essenceBlockEntity.getArenaSize().getZ() * 0.5f),
                                    blockPos.getX() + (essenceBlockEntity.getArenaSize().getX() * 0.5f),
                                    blockPos.getY() + (essenceBlockEntity.getArenaSize().getY() * 0.5f),
                                    blockPos.getZ() + (essenceBlockEntity.getArenaSize().getZ() * 0.5f)
                            ));

                    for (PurpleSpikeEntity nearbySpike : nearbyRings) {
                        if (nearbySpike.getUUID().equals(entityToCheck) && nearbySpike.getEssenceController().equals(essenceBlockEntity.getUUID())) {
                            entity = nearbySpike;
                            break;
                        }
                    }
                }

                // Someone removed a spike somehow. Clear grid of spikes and reset. Also set all spikes to on mode to punish.
                if (entity == null) {
                    for (int k = eventEntitiesInArena.size() - 1; k >= 0; k--) {
                        UUID entityUUIDToRemove = eventEntitiesInArena.remove(k).uuid();
                        Entity entityEntityToRemove = serverLevel.getEntity(entityUUIDToRemove);
                        if (entityEntityToRemove != null) {
                            entityEntityToRemove.remove(Entity.RemovalReason.DISCARDED);
                        }
                    }

                    // reset arena spikes
                    spawnEntities(serverLevel, blockPos, essenceBlockEntity, eventEntitiesInArena, true);
                    break;
                }
            }

            // Perform the rave!
            ravingTime(serverLevel, timeRemaining, getEventTimeFrame(), essenceBlockEntity, eventEntitiesInArena);
        }

        if (timeRemaining == 0) {
            EssenceBlockEntity.EndEvent(serverLevel, blockPos, blockState, essenceBlockEntity, true);
        }

        essenceBlockEntity.getEventBar().setProgress((float) essenceBlockEntity.getEventTimer() / getEventTimeFrame());
    }

    private static void spawnEntities(ServerLevel serverLevel,
                                      BlockPos blockPos,
                                      EssenceBlockEntity essenceBlockEntity,
                                      List<EssenceBlockEntity.EventEntities> eventEntitiesInArena,
                                      boolean punish
    ) {
        BlockPos arenaSize = essenceBlockEntity.getArenaSize();

        int rowLength = arenaSize.getX() - 2;
        int columnLength = arenaSize.getZ() - 2;
        for (int x = 0; x < rowLength; x++) {
            for (int z = 0; z < columnLength; z++) {
                PurpleSpikeEntity spikeEntity = BzEntities.PURPLE_SPIKE_ENTITY.get().create(serverLevel);

                if (spikeEntity != null) {
                    spikeEntity.setEssenceController(essenceBlockEntity.getUUID());
                    spikeEntity.setEssenceControllerBlockPos(essenceBlockEntity.getBlockPos());
                    spikeEntity.setEssenceControllerDimension(serverLevel.dimension());

                    spikeEntity.setPos(
                        blockPos.getX() - Math.floor(arenaSize.getX() / 2d) + 1 + x + 0.5d,
                        blockPos.getY() - Math.floor(arenaSize.getY() / 2d) + 1,
                        blockPos.getZ() - Math.floor(arenaSize.getZ() / 2d) + 1 + z + 0.5d
                    );
                    eventEntitiesInArena.add(new EssenceBlockEntity.EventEntities(spikeEntity.getUUID()));
                    if (punish) {
                        spikeEntity.setSpikeTimer(50);
                    }
                    serverLevel.addFreshEntityWithPassengers(spikeEntity);
                }
            }
        }
    }

    private static void ravingTime(ServerLevel serverLevel,
                                   int timeRemaining,
                                   int eventTimeFrame,
                                   EssenceBlockEntity essenceBlockEntity,
                                   List<EssenceBlockEntity.EventEntities> eventEntitiesInArena
    ) {
        int currentTime = (eventTimeFrame - timeRemaining) + 5;

        if (currentTime % INTERVALS == 0) {
            BlockPos arenaSize = essenceBlockEntity.getArenaSize();
            int rowLength = arenaSize.getX() - 2;
            int columnLength = arenaSize.getZ() - 2;
            int xRadius = (rowLength/2);
            int zRadius = (columnLength/2);

            int interval = currentTime / INTERVALS;

            if (interval < 8 && interval >= 0) {
                int intervalOffset = interval - 2;
                int arenaRadiusSq = xRadius * xRadius + zRadius * zRadius;

                ring(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, arenaRadiusSq, intervalOffset, 0, false);
                ring(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, arenaRadiusSq, intervalOffset, -3, false);
            }
            if (interval < 19 && interval >= 8) {
                bars(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, arenaSize.getX() / 2, interval - 8, 2, 6, 0);
                bars(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, arenaSize.getX() / 2, interval - 8, 2, 6, -3);
                bars(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, arenaSize.getX() / 2, interval - 8, 2, 6, -6);
                bars(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, arenaSize.getX() / 2, interval - 8, 2, 6, -9);
                bars(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, arenaSize.getX() / 2, interval - 8, 2, 6, -12);
                bars(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, arenaSize.getX() / 2, interval - 8, 2, 6, -15);
            }
            if (interval < 28 && interval >= 20 && interval % 2 == 0) {
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, false,
                        (x, z) -> {
                            int miniX = x / 2;
                            int miniZ = z / 2;
                            if (interval % 4 == 0) {
                                return miniX % 2 == 0;
                            }
                            else {
                                return miniZ % 2 == 0;
                            }
                        });
            }
            if (interval < 32 && interval >= 28 && interval % 2 == 0) {
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, false,
                        (x, z) -> {
                            if (interval % 4 == 0) {
                                return Math.abs((x + z) % 6) < 2;
                            }
                            else {
                                return Math.abs((x + z) % 6) >= 2;
                            }
                        });
            }
            if (interval < 36 && interval >= 32 && interval % 2 == 0) {
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, false,
                        (x, z) -> {
                            if (interval % 4 == 0) {
                                return Math.abs((x - z) % 6) < 2;
                            }
                            else {
                                return Math.abs((x - z) % 6) >= 2;
                            }
                        });
            }
            if (interval < 41 && interval >= 36 && interval % 2 == 0) {
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, false,
                        (x, z) -> {
                            boolean crossLinked = Math.abs((x - z) % 6) < 2 || Math.abs((x + z) % 6) < 2;
                            if (interval % 4 == 0) {
                                return crossLinked;
                            }
                            else {
                                return !crossLinked;
                            }
                        });
            }
            if (interval < 63 && interval >= 42) {
                int intervalOffset = interval - 42;
                int arenaRadiusSq = xRadius * xRadius + zRadius * zRadius;
                ring(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, arenaRadiusSq, intervalOffset, 0, true);
                ring(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, arenaRadiusSq, intervalOffset, -3, true);
                ring(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, arenaRadiusSq, intervalOffset, -6, true);
                ring(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, arenaRadiusSq, intervalOffset, -9, true);
                ring(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, arenaRadiusSq, intervalOffset, -12, true);
                ring(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, arenaRadiusSq, intervalOffset, -15, true);
                ring(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, arenaRadiusSq, intervalOffset, -18, true);
                ring(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, arenaRadiusSq, intervalOffset, -21, true);
                ring(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, arenaRadiusSq, intervalOffset, -24, true);

                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, true,
                        (x, z) -> {
                            int outerRadius = arenaRadiusSq / 20;
                            int currentRadiusSq = x * x + z * z;
                            return currentRadiusSq < outerRadius + (intervalOffset * 4);
                        });
            }
            if (interval < 81 && interval >= 64) {
                int intervalOffset = interval - 64;
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, true,
                        (x, z) -> {
                            float newX = x * Mth.cos(intervalOffset * 5) - z * Mth.sin(intervalOffset * 5);
                            float newZ = x * Mth.sin(intervalOffset * 5) + z * Mth.cos(intervalOffset * 5);
                            float sinResult = Mth.sin((newX * newZ) * Mth.DEG_TO_RAD);
                            sinResult = Math.abs(sinResult) * Math.abs(sinResult) * Math.abs(sinResult) * Math.abs(sinResult) * Math.abs(sinResult);
                            return sinResult < 0.1f;
                        });

                if (interval > 71 && interval % 5 == 0) {
                    patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, true,
                            (x, z) -> {
                                int miniX = x / 2;
                                int miniZ = z / 2;
                                if (interval % 4 == 0) {
                                    return miniX % 2 == 0;
                                }
                                else {
                                    return miniZ % 2 == 0;
                                }
                            });
                }
            }
            if (interval < 90 && interval >= 82 && interval % 2 == 0) {
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, false,
                        (x, z) -> {
                            int miniX = x / 6;
                            int miniZ = z / 6;
                            if (interval % 4 == 0) {
                                return Math.abs((miniX + miniZ) % 2) == 0;
                            }
                            else {
                                return Math.abs((miniX + miniZ) % 2) == 1;
                            }
                        });
            }
            if (interval < 94 && interval >= 90 && interval % 2 == 0) {
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, false,
                        (x, z) -> {
                            int miniX = x / 8;
                            int miniZ = z / 8;
                            if (interval % 4 == 0) {
                                return Math.abs((miniX + miniZ) % 2) == 0;
                            }
                            else {
                                return Math.abs((miniX + miniZ) % 2) == 1;
                            }
                        });
            }
            if (interval == 95) {
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, INTERVALS * 2, INTERVALS, true,
                        (x, z) -> {
                            float newX = x * Mth.cos(0) - z * Mth.sin(0);
                            float newZ = x * Mth.sin(0) + z * Mth.cos(0);
                            float sinResult = Mth.sin(-(newX * newZ) * Mth.DEG_TO_RAD);
                            sinResult = Math.abs(sinResult) * Math.abs(sinResult) * Math.abs(sinResult) * Math.abs(sinResult) * Math.abs(sinResult);
                            return sinResult < 0.5f;
                        });
            }
            if (interval < 105 && interval >= 97) {
                int intervalOffset = interval - 95;
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, true,
                        (x, z) -> {
                            float newX = x * Mth.cos(intervalOffset * 5) - z * Mth.sin(intervalOffset * 5);
                            float newZ = x * Mth.sin(intervalOffset * 5) + z * Mth.cos(intervalOffset * 5);
                            float sinResult = Mth.sin(-(newX * newZ) * Mth.DEG_TO_RAD);
                            sinResult = Math.abs(sinResult) * Math.abs(sinResult) * Math.abs(sinResult) * Math.abs(sinResult) * Math.abs(sinResult);
                            return sinResult < 0.5f;
                        });
            }
            if (interval < 110 && interval >= 106 && interval % 2 == 0) {
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, false,
                        (x, z) -> {
                            int miniX = x / 2;
                            int miniZ = z / 2;
                            if (interval % 4 == 0) {
                                return miniX % 2 == 0;
                            }
                            else {
                                return miniZ % 2 == 0;
                            }
                        });
            }
            if (interval < 114 && interval >= 110 && interval % 2 == 0) {
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, false,
                        (x, z) -> {
                            if (interval % 4 == 0) {
                                return Math.abs((x + z) % 6) < 4;
                            }
                            else {
                                return Math.abs((x + z) % 6) >= 4;
                            }
                        });
            }
            if (interval < 118 && interval >= 114 && interval % 2 == 0) {
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, false,
                        (x, z) -> {
                            if (interval % 4 == 0) {
                                return Math.abs((x - z) % 6) < 4;
                            }
                            else {
                                return Math.abs((x - z) % 6) >= 4;
                            }
                        });
            }
            if (interval < 122 && interval >= 118 && interval % 2 == 0) {
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, false,
                        (x, z) -> {
                            int miniX = x / 3;
                            int miniZ = z / 3;
                            if (interval % 4 == 0) {
                                return Math.abs((miniX + miniZ) % 2) == 0;
                            }
                            else {
                                return Math.abs((miniX + miniZ) % 2) == 1;
                            }
                        });
            }
            if (interval < 128 && interval >= 122 && interval % 2 == 0) {
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, false,
                        (x, z) -> {
                            int miniX = x / 2;
                            int miniZ = z / 2;
                            if (interval % 4 == 0) {
                                return Math.abs((miniX + miniZ) % 2) == 0;
                            }
                            else {
                                return Math.abs((miniX + miniZ) % 2) == 1;
                            }
                        });
            }
            if (interval < 132 && interval >= 128 && interval % 2 == 0) {
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, false,
                        (x, z) -> {
                            if (interval % 4 == 0) {
                                return x % 2 == 0;
                            }
                            else {
                                return z % 2 == 0;
                            }
                        });
            }
            if (interval >= 132 && interval % 2 == 0) {
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, false,
                        (x, z) -> {
                            if (interval % 4 == 0) {
                                return Math.abs((x + z) % 2) == 0;
                            }
                            else {
                                return Math.abs((x + z) % 2) == 1;
                            }
                        });
            }
        }
    }

    private static void bars(ServerLevel serverLevel, EssenceBlockEntity essenceBlockEntity, List<EssenceBlockEntity.EventEntities> eventEntitiesInArena, int xRadius, int zRadius, int arenaRadius, int interval, int height, int width, int offset) {
        patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, false,
                (x, z) -> {
                    int barTop = arenaRadius - ((interval + offset) * height);
                    int barBottom = (arenaRadius - height) - ((interval + offset) * height);

                    if (Math.abs(x) <= barTop && Math.abs(x) > barBottom) {
                        if (interval % 2 == 0) {
                            if (z < 0) {
                                return z % (width * 2) >= -width;
                            }
                            else {
                                return z % (width * 2) >= width;
                            }
                        }
                        else {
                            if (z < 0) {
                                return z % (width * 2) < -width;
                            }
                            else {
                                return z % (width * 2) < width;
                            }
                        }
                    }
                    return false;
                });
    }

    private static void ring(ServerLevel serverLevel, EssenceBlockEntity essenceBlockEntity, List<EssenceBlockEntity.EventEntities> eventEntitiesInArena, int xRadius, int zRadius, int arenaRadiusSq, int intervalOffset, int delay, boolean addToExistingSpike) {
        patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius, addToExistingSpike,
                (x, z) -> {
                    int innerRadius = (arenaRadiusSq / 20) * (intervalOffset + delay) * (intervalOffset + delay);
                    int outerRadius = (arenaRadiusSq / 20) * (intervalOffset + delay + 1) * (intervalOffset + delay + 1);

                    if (intervalOffset % 2 == 0) {
                        if (Math.abs(x) <= Math.sqrt(outerRadius) / 3) {
                            return false;
                        }
                    }
                    else {
                        if (Math.abs(z) <= Math.sqrt(outerRadius) / 3) {
                            return false;
                        }
                    }

                    int currentRadiusSq = x * x + z * z;
                    return currentRadiusSq >= innerRadius &&
                            currentRadiusSq < outerRadius;
                });
    }

    private static void patternFunction(ServerLevel serverLevel,
                                        EssenceBlockEntity essenceBlockEntity,
                                        List<EssenceBlockEntity.EventEntities> eventEntitiesInArena,
                                        int xRadius,
                                        int zRadius,
                                        boolean addToExistingSpike,
                                        BiPredicate<Integer, Integer> patternCondition
    ) {
        for (int x = -xRadius; x <= xRadius; x++) {
            for (int z = -zRadius; z <= zRadius; z++) {
                if (patternCondition.test(x, z)) {
                    activateSpike(serverLevel, x, z, INTERVALS, INTERVALS, essenceBlockEntity, eventEntitiesInArena, addToExistingSpike);
                }
            }
        }
    }

    private static void patternFunction(ServerLevel serverLevel,
                                        EssenceBlockEntity essenceBlockEntity,
                                        List<EssenceBlockEntity.EventEntities> eventEntitiesInArena,
                                        int xRadius,
                                        int zRadius,
                                        int chargeTime,
                                        int spikeTime,
                                        boolean addToExistingSpike,
                                        BiPredicate<Integer, Integer> patternCondition
    ) {
        for (int x = -xRadius; x <= xRadius; x++) {
            for (int z = -zRadius; z <= zRadius; z++) {
                if (patternCondition.test(x, z)) {
                    activateSpike(serverLevel, x, z, chargeTime, spikeTime, essenceBlockEntity, eventEntitiesInArena, addToExistingSpike);
                }
            }
        }
    }

    private static void activateSpike(ServerLevel serverLevel,
                                      int x,
                                      int z,
                                      int chargeTime,
                                      int spikeTime,
                                      EssenceBlockEntity essenceBlockEntity,
                                      List<EssenceBlockEntity.EventEntities> eventEntitiesInArena,
                                      boolean addToExistingSpike
    ) {
        BlockPos arenaSize = essenceBlockEntity.getArenaSize();
        int rowLength = arenaSize.getX() - 2;
        int columnLength = arenaSize.getZ() - 2;
        int newX = x + (rowLength/2);
        int newZ = z + (columnLength/2);
        if (newX > rowLength || newZ > columnLength || newX + (newZ * columnLength) >= eventEntitiesInArena.size()) {
            Bumblezone.LOGGER.warn("Bumblezone: Detected invalid {} {} ({} {}) coordinate for trying to activate a spike. Grid size {} and row length {} - column length {}",
                    x, z, newX, newZ, eventEntitiesInArena.size(), rowLength, columnLength);
        }
        else {
            EssenceBlockEntity.EventEntities eventEntities = eventEntitiesInArena.get(newX + (newZ * columnLength));
            Entity entity = serverLevel.getEntity(eventEntities.uuid());
            if (entity instanceof PurpleSpikeEntity purpleSpikeEntity) {
                if (addToExistingSpike && !purpleSpikeEntity.hasSpikeCharge() && purpleSpikeEntity.hasSpike()) {
                    purpleSpikeEntity.setSpikeChargeTimer(0);
                    purpleSpikeEntity.setSpikeTimer(spikeTime + chargeTime);
                }
                else {
                    purpleSpikeEntity.setSpikeChargeTimer(chargeTime);
                    purpleSpikeEntity.setSpikeTimer(spikeTime);
                }
            }
        }
    }

    @Override
    public void onPlayerEnter(ServerLevel serverLevel, ServerPlayer serverPlayer, EssenceBlockEntity essenceBlockEntity) {
        MusicPacketFromServer.sendToClient(serverPlayer, BzSounds.KNOWING_EVENT.get().getLocation(), true);
        super.onPlayerEnter(serverLevel, serverPlayer, essenceBlockEntity);
    }

    @Override
    public void onPlayerLeave(ServerLevel serverLevel, ServerPlayer serverPlayer, EssenceBlockEntity essenceBlockEntity) {
        MusicPacketFromServer.sendToClient(serverPlayer, BzSounds.KNOWING_EVENT.get().getLocation(), false);
        super.onPlayerLeave(serverLevel, serverPlayer, essenceBlockEntity);
    }
}
