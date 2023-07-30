package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.client.rendering.rootmin.RootminPose;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzStats;
import com.telepathicgrunt.the_bumblezone.screens.ServerEssenceEvent;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;


public class EssenceBlockGreen extends EssenceBlock {
    private static final int ROOTMIN_HEALTH = 30;
    private static final float STAGE_2_THRESHOLD = 0.75f;
    private static final float STAGE_3_THRESHOLD = 0.575f;
    private static final float STAGE_4_THRESHOLD = 0.3f;
    private static final float STAGE_5_THRESHOLD = 0.15f;

    public EssenceBlockGreen() {
        super(Properties.of().mapColor(MapColor.COLOR_GREEN));
    }

    @Override
    public ResourceLocation getArenaNbt() {
        return new ResourceLocation(Bumblezone.MODID, "essence/green_arena");
    }

    @Override
    public int getEventTimeFrame() {
        return 6000;
    }

    @Override
    public void awardPlayerWinStat(ServerPlayer serverPlayer) {
        serverPlayer.awardStat(BzStats.LIFE_EVENT_DEFEATED_RL.get());
    }

    @Override
    public ServerEssenceEvent getServerEssenceEvent() {
        return (ServerEssenceEvent) new ServerEssenceEvent(
                "essence.the_bumblezone.green_essence_event",
                BossEvent.BossBarColor.GREEN,
                BossEvent.BossBarOverlay.NOTCHED_6
        ).setDarkenScreen(true);
    }

    @Override
    public ItemStack getEssenceItemReward() {
        return BzItems.ESSENCE_LIFE.get().getDefaultInstance();
    }

    @Override
    public int getEssenceXpReward() {
        return 3000;
    }

    @Override
    public void performUniqueArenaTick(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, EssenceBlockEntity essenceBlockEntity) {

        BlockPos rootminPos = blockPos.offset(9, -3, 0);
        List<EssenceBlockEntity.EventEntities> eventEntitiesInArena = essenceBlockEntity.getEventEntitiesInArena();

        if (eventEntitiesInArena.isEmpty()) {
            Entity entity = BzEntities.ROOTMIN.get().spawn(serverLevel, rootminPos, MobSpawnType.TRIGGERED);
            if (entity instanceof RootminEntity rootminEntity) {
                rootminEntity.setEssenceController(essenceBlockEntity.getUUID());
                rootminEntity.setEssenceControllerBlockPos(essenceBlockEntity.getBlockPos());
                rootminEntity.setEssenceControllerDimension(serverLevel.dimension());

                AttributeInstance livingEntityAttributeHealth = rootminEntity.getAttribute(Attributes.MAX_HEALTH);
                if (livingEntityAttributeHealth != null) {
                    float extraHealth = ROOTMIN_HEALTH - rootminEntity.getMaxHealth();
                    livingEntityAttributeHealth.addPermanentModifier(new AttributeModifier(
                            UUID.fromString("03c85bd0-09eb-11ee-be56-0242ac120002"),
                            "Essence Arena Health Boost",
                            extraHealth,
                            AttributeModifier.Operation.ADDITION));
                    rootminEntity.heal(extraHealth + rootminEntity.getMaxHealth());
                }

                entity.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atLowerCornerOf(Direction.SOUTH.getNormal()));
                eventEntitiesInArena.add(new EssenceBlockEntity.EventEntities(entity.getUUID()));
                essenceBlockEntity.setChanged();
            }
        }

        if (!eventEntitiesInArena.isEmpty()) {
            EssenceBlockEntity.EventEntities eventEntity = eventEntitiesInArena.get(0);
            Entity entity = serverLevel.getEntity(eventEntity.uuid());

            float progress = essenceBlockEntity.getEventBar().getProgress();
            if (progress == 0 && entity == null) {
                EssenceBlockEntity.EndEvent(serverLevel, blockPos, blockState, essenceBlockEntity, true);
                return;
            }

            if (entity == null ||
                !(entity instanceof RootminEntity rootminEntity && rootminEntity.getEssenceController().equals(essenceBlockEntity.getUUID())))
            {
                eventEntitiesInArena.remove(0);
                return;
            }

            int hitsLeft = Math.round(progress * ROOTMIN_HEALTH);
            RootminPose rootminPose = rootminEntity.getRootminPose();
            if (!rootminEntity.isDeadOrDying()) {
                rootminEntity.setHealth(hitsLeft);
            }
            float rootminHealthPercent = rootminEntity.getHealth() / ROOTMIN_HEALTH;

            if (rootminPose == RootminPose.SHOCK || rootminPose == RootminPose.ANGRY || rootminPose == RootminPose.CURSE) {
                if (rootminEntity.getLastHurtByMob() != null) {
                    if (rootminPose == RootminPose.SHOCK) {
                        if (rootminHealthPercent > STAGE_3_THRESHOLD) {
                            if (rootminEntity.getHealth() % 3 == 2) {
                                rootminEntity.runAngry();
                            }
                        }
                        else if (rootminHealthPercent > STAGE_4_THRESHOLD) {
                            rootminEntity.runCurse();
                        }
                        else {
                            rootminEntity.setRootminPose(RootminPose.NONE);
                        }
                        hitsLeft--;
                        rootminEntity.setLastHurtByMob(null);
                    }
                }
            }
            else {
                Vec3 desiredRootminSpot = Vec3.atCenterOf(rootminPos).add(0, -0.5d, 0);

                if (!rootminEntity.position().equals(desiredRootminSpot)) {
                    Vec3 diff = desiredRootminSpot.subtract(rootminEntity.position());

                    if (diff.length() <= 3 && diff.length() > 0.05d) {
                        Vec3 moveDirection = diff.scale(0.1d);
                        rootminEntity.setDeltaMovement(moveDirection.x(), moveDirection.y(), moveDirection.z());
                    }
                    else if (diff.length() > 3) {
                        rootminEntity.moveTo(desiredRootminSpot);
                    }
                }

                int interval = rootminHealthPercent > STAGE_4_THRESHOLD && rootminHealthPercent <= STAGE_3_THRESHOLD ? Mth.lerpInt(rootminHealthPercent, 15, 35) : Mth.lerpInt(rootminHealthPercent, 10, 45);
                boolean fire = rootminEntity.tickCount % interval == 0;

                if (!fire && rootminHealthPercent <= STAGE_4_THRESHOLD) {
                    fire = (rootminEntity.tickCount + 4) % interval == 0;
                    if (!fire && rootminHealthPercent <= STAGE_5_THRESHOLD) {
                        fire = (rootminEntity.tickCount + 7) % interval == 0;
                    }
                }

                if (fire && !rootminEntity.isDeadOrDying()) {
                    BlockPos playerArea = blockPos.offset(-9, -3, 0);
                    List<Player> players = serverLevel.getEntitiesOfClass(
                        Player.class,
                        new AABB(
                            playerArea.getX() - 1,
                            playerArea.getY() - 1,
                            playerArea.getZ() - 1,
                            playerArea.getX() + 2,
                            playerArea.getY() + 3,
                            playerArea.getZ() + 2
                        )
                    );

                    if (essenceBlockEntity.getEventTimer() < getEventTimeFrame() - 100 && !players.isEmpty()) {
                        // Do behavior of shooting and stuff
                        boolean isHoming = rootminHealthPercent > STAGE_4_THRESHOLD && rootminHealthPercent <= STAGE_3_THRESHOLD;

                        if (isHoming) {
                            RandomSource randomSource = rootminEntity.getRandom();
                            rootminEntity.lookAt(
                                EntityAnchorArgument.Anchor.FEET,
                                players.get(0).position().add(
                                    randomSource.nextDouble() * 21 - 10,
                                    randomSource.nextDouble() * 21 - 10,
                                    randomSource.nextDouble() * 21 - 10
                                ));
                        }
                        else {
                            rootminEntity.lookAt(EntityAnchorArgument.Anchor.FEET, players.get(0).position());
                        }

                        if (rootminHealthPercent > STAGE_3_THRESHOLD && rootminHealthPercent <= STAGE_2_THRESHOLD) {
                            rootminEntity.runMultiShoot(
                                players.get(0),
                                (float)Mth.lerp(rootminHealthPercent, 1.1D, 0.8D),
                                3
                            );
                        }
                        else {
                            rootminEntity.runShoot(
                                players.get(0),
                                isHoming ? 0.8F : (float)Mth.lerp(rootminHealthPercent, 1.7D, 0.85D),
                                isHoming
                            );
                        }
                    }
                    else {
                        rootminEntity.lookAt(EntityAnchorArgument.Anchor.FEET, Vec3.atLowerCornerOf(Direction.WEST.getNormal()).add(rootminEntity.position()));
                    }
                }
            }

            float newProgress = ((float)hitsLeft) / ROOTMIN_HEALTH;
            essenceBlockEntity.getEventBar().setProgress(newProgress);
            essenceBlockEntity.setChanged();
        }
    }
}
