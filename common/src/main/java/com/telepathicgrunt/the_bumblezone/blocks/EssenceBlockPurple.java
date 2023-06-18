package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.entities.nonliving.PurpleSpikeEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.screens.ServerEssenceEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import java.util.List;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Predicate;


public class EssenceBlockPurple extends EssenceBlock {
    public EssenceBlockPurple() {
        super(Properties.of().mapColor(MapColor.COLOR_PURPLE));
    }

    @Override
    public ResourceLocation getArenaNbt() {
        return new ResourceLocation(Bumblezone.MODID, "essence/purple_arena");
    }

    @Override
    public int getEventTimeFrame() {
        return 4000;
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
        return BzItems.ESSENCE_PURPLE.get().getDefaultInstance();
    }

    @Override
    public int getEssenceXpReward() {
        return 3000;
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

                // Someone removed a spike somehow. Clear grid of spikes and reset. Also set all spikes to on to punish.
                if (entity == null) {
                    eventEntitiesInArena.remove(i);
                    for (int k = eventEntitiesInArena.size() - 1; k >= 0; k--) {
                        UUID entityUUIDToRemove = eventEntitiesInArena.remove(i).uuid();
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
        int currentTime = eventTimeFrame - timeRemaining - 1;

        if (currentTime % 100 == 0) {
            BlockPos arenaSize = essenceBlockEntity.getArenaSize();
            int rowLength = arenaSize.getX() - 2;
            int columnLength = arenaSize.getZ() - 2;
            int xRadius = (rowLength/2);
            int zRadius = (columnLength/2);

            if (currentTime % 600 == 0) {
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius,
                        (x, z) -> x % 2 == 0);
            }
            else if (currentTime % 600 == 100) {
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius,
                        (x, z) -> z % 2 == 0);
            }
            else if (currentTime % 600 == 200) {
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius,
                        (x, z) -> Math.abs((x + z) % 2) == 0);
            }
            else if (currentTime % 600 == 300) {
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius,
                        (x, z) ->Math.abs((x + z) % 2) == 1);
            }
            else if (currentTime % 600 == 400) {
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius,
                        (x, z) -> x * x + z * z < (xRadius * xRadius + zRadius * zRadius) / 6);
            }
            else if (currentTime % 600 == 500) {
                patternFunction(serverLevel, essenceBlockEntity, eventEntitiesInArena, xRadius, zRadius,
                        (x, z) -> x * x + z * z > (xRadius * xRadius + zRadius * zRadius) / 6);
            }
        }
    }

    private static void patternFunction(ServerLevel serverLevel,
                                        EssenceBlockEntity essenceBlockEntity,
                                        List<EssenceBlockEntity.EventEntities> eventEntitiesInArena,
                                        int xRadius,
                                        int zRadius,
                                        BiPredicate<Integer, Integer> patternCondition
    ) {
        for (int x = -xRadius; x <= xRadius; x++) {
            for (int z = -zRadius; z <= zRadius; z++) {
                if (patternCondition.test(x, z)) {
                    activateSpike(serverLevel, x, z, 55, 45, essenceBlockEntity, eventEntitiesInArena);
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
                                      List<EssenceBlockEntity.EventEntities> eventEntitiesInArena
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
                //if (!purpleSpikeEntity.hasSpike()) {
                    purpleSpikeEntity.setSpikeChargeTimer(chargeTime);
                    purpleSpikeEntity.addSpikeTimer(spikeTime);
//                }
//                else {
//                    purpleSpikeEntity.addSpikeTimer(spikeTime + chargeTime);
//                }
            }
        }
    }
}
