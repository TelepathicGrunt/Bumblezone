package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.bossbars.ServerEssenceEvent;
import com.telepathicgrunt.the_bumblezone.client.rendering.cosmiccrystal.CosmicCrystalState;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.entities.living.CosmicCrystalEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzStats;
import com.telepathicgrunt.the_bumblezone.packets.MusicPacketFromServer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class EssenceBlockWhite extends EssenceBlock {
    public EssenceBlockWhite() {
        super(Properties.of().mapColor(MapColor.SNOW));
    }

    @Override
    public ResourceLocation getArenaNbt() {
        return new ResourceLocation(Bumblezone.MODID, "essence/white_arena");
    }

    @Override
    public int getEventTimeFrame() {
        return 10000;
    }

    @Override
    public ServerEssenceEvent getServerEssenceEvent() {
        return (ServerEssenceEvent) new ServerEssenceEvent(
                "essence.the_bumblezone.white_essence_event",
                BossEvent.BossBarColor.WHITE,
                BossEvent.BossBarOverlay.NOTCHED_6
        ).setDarkenScreen(true);
    }

    @Override
    public ItemStack getEssenceItemReward() {
        return BzItems.ESSENCE_CONTINUITY.get().getDefaultInstance();
    }

    @Override
    public int getEssenceXpReward() {
        return 3000;
    }

    @Override
    public boolean hasMiningFatigue() {
        return true;
    }

    @Override
    public void awardPlayerWinStat(ServerPlayer serverPlayer) {
        serverPlayer.awardStat(BzStats.CONTINUITY_EVENT_DEFEATED_RL.get());
    }

    @Override
    public void performUniqueArenaTick(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, EssenceBlockEntity essenceBlockEntity) {
        if (essenceBlockEntity.getPlayerInArena().size() == 0) {
            return;
        }

        if (essenceBlockEntity.getEventTimer() > this.getEventTimeFrame() - 50) {
            return;
        }

        List<EssenceBlockEntity.EventEntities> eventEntitiesInArena = essenceBlockEntity.getEventEntitiesInArena();
        int totalCrystals = eventEntitiesInArena.size();
        int totalhealth = 0;
        float totalMaxHealth = 6 * BzGeneralConfigs.cosmicCrystalHealth;
        boolean respawnedACrystal = false;

        if (totalCrystals == 0 && essenceBlockEntity.getEventTimer() > this.getEventTimeFrame() - 100) {
            SpawnNewCrystal(serverLevel, blockPos, essenceBlockEntity, 0, 1, eventEntitiesInArena);
            SpawnNewCrystal(serverLevel, blockPos, essenceBlockEntity, 60, 1, eventEntitiesInArena);
            SpawnNewCrystal(serverLevel, blockPos, essenceBlockEntity, 120, 1, eventEntitiesInArena);
            SpawnNewCrystal(serverLevel, blockPos, essenceBlockEntity, 180, 1, eventEntitiesInArena);
            SpawnNewCrystal(serverLevel, blockPos, essenceBlockEntity, 240, 1, eventEntitiesInArena);
            SpawnNewCrystal(serverLevel, blockPos, essenceBlockEntity, 300, 1, eventEntitiesInArena);
            totalCrystals = 6;
        }
        else {
            List<CosmicCrystalEntity> crystals = new ArrayList<>();
            boolean crystalsAreIdleLongEnough = true;

            // update how many entities are alive
            for (int i = eventEntitiesInArena.size() - 1; i >= 0; i--) {
                UUID entityToCheck = eventEntitiesInArena.get(i).uuid();
                Entity entity = serverLevel.getEntity(entityToCheck);

                if (entity == null) {
                    List<CosmicCrystalEntity> nearbyCosmicCrystalEntities = serverLevel.getEntitiesOfClass(
                            CosmicCrystalEntity.class,
                            new AABB(
                                blockPos.getX() - (essenceBlockEntity.getArenaSize().getX() * 0.5f),
                                blockPos.getY() - (essenceBlockEntity.getArenaSize().getY() * 0.5f),
                                blockPos.getZ() - (essenceBlockEntity.getArenaSize().getZ() * 0.5f),
                                blockPos.getX() + (essenceBlockEntity.getArenaSize().getX() * 0.5f),
                                blockPos.getY() + (essenceBlockEntity.getArenaSize().getY() * 0.5f),
                                blockPos.getZ() + (essenceBlockEntity.getArenaSize().getZ() * 0.5f)
                        ));

                    for (CosmicCrystalEntity nearbyCrystal : nearbyCosmicCrystalEntities) {
                        if (nearbyCrystal.getUUID().equals(entityToCheck) && nearbyCrystal.getEssenceController().equals(essenceBlockEntity.getUUID())) {
                            entity = nearbyCrystal;
                            break;
                        }
                    }
                }

                if (entity == null) {
                    eventEntitiesInArena.remove(i);
                    SpawnNewCrystal(serverLevel, blockPos, essenceBlockEntity, 60 * i, 1, eventEntitiesInArena);
                    respawnedACrystal = true;
                }

                if (entity instanceof CosmicCrystalEntity cosmicCrystalEntity) {
                    if (blockState.getBlock() instanceof EssenceBlock essenceBlock &&
                        essenceBlockEntity.getEventTimer() > essenceBlock.getEventTimeFrame() - 70)
                    {
                        return;
                    }
                    totalhealth += cosmicCrystalEntity.getHealth();

                    if (cosmicCrystalEntity.getCosmicCrystalState() != CosmicCrystalState.NORMAL ||
                        cosmicCrystalEntity.currentStateTimeTick < 50)
                    {
                        crystalsAreIdleLongEnough = false;
                    }

                    crystals.add(cosmicCrystalEntity);
                }
            }

            // Set commands here
            if (crystalsAreIdleLongEnough && !crystals.isEmpty()) {
                CosmicCrystalState chosenAttack;

                do {
                    chosenAttack = CosmicCrystalState.values()[serverLevel.getRandom().nextInt(CosmicCrystalState.values().length)];
                }
                while (crystals.get(0).pastStates.contains(chosenAttack) || chosenAttack == CosmicCrystalState.NORMAL);

                CosmicCrystalState finalChosenAttack = chosenAttack;
                crystals.forEach(c -> c.setCosmicCrystalState(finalChosenAttack));
                //crystals.forEach(c -> c.setCosmicCrystalState(CosmicCrystalState.TRACKING_SPINNING_ATTACK)); // for debugging
            }

            if (!crystals.isEmpty()) {
                boolean missingCrystal = respawnedACrystal;

                for (int i = 0; i < crystals.size(); i++) {
                    CosmicCrystalEntity crystalEntity = crystals.get(i);
                    int orbitOffset = crystalEntity.getOrbitOffsetDegrees();

                    if (orbitOffset % (360 / crystals.size()) != 0) {
                        missingCrystal = true;
                        break;
                    }

                    float healthPercent = Math.round((totalhealth / totalMaxHealth) * 10f) / 10f;
                    float threshold;
                    if (healthPercent > 0.75f) {
                        threshold = 0;
                    }
                    else if (healthPercent > 0.5f) {
                        threshold = 0.333f;
                    }
                    else if (healthPercent > 0.25f) {
                        threshold = 0.666f;
                    }
                    else {
                        threshold = 1;
                    }
                    float difficultyBuff = (float) (1 + (Math.pow(threshold, 2) * 0.35f));
                    float newDifficulty = (1 + (0.025f * (6 - totalCrystals))) * difficultyBuff;
                    if (newDifficulty != crystalEntity.getDifficultyBoost()) {
                        crystalEntity.setDifficultyBoost(newDifficulty);
                        crystalEntity.setCosmicCrystalState(CosmicCrystalState.NORMAL);
                    }
                }

                if (missingCrystal) {
                    for (int i = 0; i < crystals.size(); i++) {
                        CosmicCrystalEntity crystalEntity = crystals.get(i);
                        crystalEntity.setOrbitOffsetDegrees(i * (360 / crystals.size()));
                        crystalEntity.setCosmicCrystalState(CosmicCrystalState.NORMAL);
                        crystalEntity.currentTickCount = 0;
                    }
                }
            }
        }

        // Check if any crystal was removed too early

        if (totalCrystals == 0) {
            EssenceBlockEntity.EndEvent(serverLevel, blockPos, blockState, essenceBlockEntity, true);
        }

        essenceBlockEntity.getEventBar().setProgress(totalhealth / totalMaxHealth);
    }

    private Entity SpawnNewCrystal(ServerLevel serverLevel, BlockPos blockPos, EssenceBlockEntity essenceBlockEntity, int orbitOffset, float difficultyBoost, List<EssenceBlockEntity.EventEntities> eventEntitiesInArena) {
        CosmicCrystalEntity entity = BzEntities.COSMIC_CRYSTAL_ENTITY.get().spawn(serverLevel, blockPos, MobSpawnType.TRIGGERED);
        if (entity != null) {
            entity.setEssenceControllerDimension(serverLevel.dimension());
            entity.setEssenceController(essenceBlockEntity.getUUID());
            entity.setEssenceControllerBlockPos(essenceBlockEntity.getBlockPos());
            entity.setOrbitOffsetDegrees(orbitOffset);
            entity.setDifficultyBoost(difficultyBoost);
            eventEntitiesInArena.add(new EssenceBlockEntity.EventEntities(entity.getUUID()));
        }
        return entity;
    }

    public void crystalKilled(CosmicCrystalEntity cosmicCrystalEntity, EssenceBlockEntity essenceBlockEntity) {
        essenceBlockEntity.getEventEntitiesInArena().removeIf(e -> e.uuid().equals(cosmicCrystalEntity.getUUID()));
    }

    @Override
    public void onPlayerEnter(ServerLevel serverLevel, ServerPlayer serverPlayer, EssenceBlockEntity essenceBlockEntity) {
        MusicPacketFromServer.sendToClient(serverPlayer, BzSounds.CONTINUITY_EVENT.get().getLocation(), true);
        super.onPlayerEnter(serverLevel, serverPlayer, essenceBlockEntity);
    }

    @Override
    public void onPlayerLeave(ServerLevel serverLevel, ServerPlayer serverPlayer, EssenceBlockEntity essenceBlockEntity) {
        MusicPacketFromServer.sendToClient(serverPlayer, BzSounds.CONTINUITY_EVENT.get().getLocation(), false);
        super.onPlayerLeave(serverLevel, serverPlayer, essenceBlockEntity);
    }
}
