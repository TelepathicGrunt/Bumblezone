package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.client.rendering.boundlesscrystal.BoundlessCrystalState;
import com.telepathicgrunt.the_bumblezone.entities.living.BoundlessCrystalEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzStats;
import com.telepathicgrunt.the_bumblezone.screens.ServerEssenceEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

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
    public void awardPlayerWinStat(ServerPlayer serverPlayer) {
        serverPlayer.awardStat(BzStats.CONTINUITY_EVENT_DEFEATED_RL.get());
    }

    @Override
    public void performUniqueArenaTick(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, EssenceBlockEntity essenceBlockEntity) {
        if (essenceBlockEntity.getPlayerInArena().size() == 0) {
            return;
        }

        if (blockState.getBlock() instanceof EssenceBlock essenceBlock &&
            essenceBlockEntity.getEventTimer() > essenceBlock.getEventTimeFrame() - 50)
        {
            return;
        }

        List<EssenceBlockEntity.EventEntities> eventEntitiesInArena = essenceBlockEntity.getEventEntitiesInArena();
        int totalCrystals = eventEntitiesInArena.size();
        int totalhealth = 0;

        if (totalCrystals == 0) {
            SpawnNewCrystal(serverLevel, blockPos, essenceBlockEntity, 0, 1, eventEntitiesInArena);
            SpawnNewCrystal(serverLevel, blockPos, essenceBlockEntity, 60, 1, eventEntitiesInArena);
            SpawnNewCrystal(serverLevel, blockPos, essenceBlockEntity, 120, 1, eventEntitiesInArena);
            SpawnNewCrystal(serverLevel, blockPos, essenceBlockEntity, 180, 1, eventEntitiesInArena);
            SpawnNewCrystal(serverLevel, blockPos, essenceBlockEntity, 240, 1, eventEntitiesInArena);
            SpawnNewCrystal(serverLevel, blockPos, essenceBlockEntity, 300, 1, eventEntitiesInArena);
            totalCrystals = 6;
        }
        else {
            List<BoundlessCrystalEntity> crystals = new ArrayList<>();
            boolean crystalsAreIdleLongEnough = true;

            // update how many entities are alive
            for (int i = eventEntitiesInArena.size() - 1; i >= 0; i--) {
                UUID entityToCheck = eventEntitiesInArena.get(i).uuid();
                Entity entity = serverLevel.getEntity(entityToCheck);
                if (entity == null) {
                    eventEntitiesInArena.remove(i);
                    totalCrystals--;
                }
                else if (entity instanceof BoundlessCrystalEntity boundlessCrystalEntity) {
                    if (blockState.getBlock() instanceof EssenceBlock essenceBlock &&
                        essenceBlockEntity.getEventTimer() > essenceBlock.getEventTimeFrame() - 70)
                    {
                        return;
                    }
                    totalhealth += boundlessCrystalEntity.getHealth();

                    if (boundlessCrystalEntity.getBoundlessCrystalState() != BoundlessCrystalState.NORMAL ||
                        boundlessCrystalEntity.currentStateTimeTick < 50)
                    {
                        crystalsAreIdleLongEnough = false;
                    }

                    crystals.add(boundlessCrystalEntity);
                }
            }

            // Set commands here
            if (crystalsAreIdleLongEnough && !crystals.isEmpty()) {
                BoundlessCrystalState chosenAttack;

                do {
                    chosenAttack = BoundlessCrystalState.values()[serverLevel.getRandom().nextInt(BoundlessCrystalState.values().length)];
                }
                while (crystals.get(0).getPreviousBoundlessCrystalState() == chosenAttack || chosenAttack == BoundlessCrystalState.NORMAL);

                BoundlessCrystalState finalChosenAttack = chosenAttack;
                crystals.forEach(c -> c.setBoundlessCrystalState(finalChosenAttack));
            }

            if (!crystals.isEmpty()) {
                boolean missingCrystal = false;

                for (int i = 0; i < crystals.size(); i++) {
                    BoundlessCrystalEntity crystalEntity = crystals.get(i);
                    int orbitOffset = crystalEntity.getOrbitOffsetDegrees();

                    if (orbitOffset % (360 / crystals.size()) != 0) {
                        missingCrystal = true;
                        break;
                    }
                }

                if (missingCrystal) {
                    for (int i = 0; i < crystals.size(); i++) {
                        BoundlessCrystalEntity crystalEntity = crystals.get(i);
                        crystalEntity.setOrbitOffsetDegrees(i * (360 / crystals.size()));
                        crystalEntity.setDifficultyBoost(1 + (0.12f * (6 - totalCrystals)));
                        crystalEntity.setBoundlessCrystalState(BoundlessCrystalState.NORMAL);
                    }
                }
            }
        }

        // Check if any crystal was removed too early

        if (totalCrystals == 0) {
            EssenceBlockEntity.EndEvent(serverLevel, blockPos, blockState, essenceBlockEntity, true);
        }

        float totalMaxHealth = 6 * BoundlessCrystalEntity.MAX_HEALTH;
        essenceBlockEntity.getEventBar().setProgress(totalhealth / totalMaxHealth);
    }

    private void SpawnNewCrystal(ServerLevel serverLevel, BlockPos blockPos, EssenceBlockEntity essenceBlockEntity, int orbitOffset, float difficultyBoost, List<EssenceBlockEntity.EventEntities> eventEntitiesInArena) {
        BoundlessCrystalEntity entity = BzEntities.BOUNDLESS_CRYSTAL_ENTITY.get().spawn(serverLevel, blockPos, MobSpawnType.TRIGGERED);
        if (entity != null) {
            entity.setEssenceControllerDimension(serverLevel.dimension());
            entity.setEssenceController(essenceBlockEntity.getUUID());
            entity.setEssenceControllerBlockPos(essenceBlockEntity.getBlockPos());
            entity.setOrbitOffsetDegrees(orbitOffset);
            entity.setDifficultyBoost(difficultyBoost);
            eventEntitiesInArena.add(new EssenceBlockEntity.EventEntities(entity.getUUID()));
        }
    }
}
