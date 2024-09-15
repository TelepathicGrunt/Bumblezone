package com.telepathicgrunt.the_bumblezone.blocks;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.bossbars.ServerEssenceEvent;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminEntity;
import com.telepathicgrunt.the_bumblezone.entities.mobs.RootminState;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzSounds;
import com.telepathicgrunt.the_bumblezone.modinit.BzStats;
import com.telepathicgrunt.the_bumblezone.packets.MusicPacketFromServer;
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
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;


public class EssenceBlockGreen extends EssenceBlock {

    public static final MapCodec<EssenceBlockGreen> CODEC = Block.simpleCodec(EssenceBlockGreen::new);

    private static final int ROOTMIN_HEALTH = 30;
    private static final float STAGE_2_THRESHOLD = 0.75f;
    private static final float STAGE_3_THRESHOLD = 0.575f;
    private static final float STAGE_4_THRESHOLD = 0.3f;
    private static final float STAGE_5_THRESHOLD = 0.15f;

    public EssenceBlockGreen() {
        this(Properties.of()
                .mapColor(MapColor.COLOR_GREEN)
                .strength(-1.0f, 3600000.8f)
                .lightLevel((blockState) -> 15)
                .noLootTable()
                .forceSolidOn()
                .isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false)
                .isViewBlocking((blockState, blockGetter, blockPos) -> false)
                .pushReaction(PushReaction.BLOCK));
    }

    public EssenceBlockGreen(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends EssenceBlockGreen> codec() {
        return CODEC;
    }

    @Override
    public ResourceLocation getArenaNbt() {
        return ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "essence/green_arena");
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
    public ResourceLocation getEssenceItemReward() {
        return ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "gameplay/rewards/green_arena_victory");
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
    public void performUniqueArenaTick(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, EssenceBlockEntity essenceBlockEntity) {

        BlockPos rootminPos = blockPos.offset(9, -3, 0);
        List<EssenceBlockEntity.EventEntities> eventEntitiesInArena = essenceBlockEntity.getEventEntitiesInArena();

        if (eventEntitiesInArena.isEmpty()) {
            spawnRootmin(serverLevel, essenceBlockEntity, rootminPos, eventEntitiesInArena);

            if (eventEntitiesInArena.size() == 1) {
                List<HangingEntity> frames = serverLevel.getEntitiesOfClass(HangingEntity.class, new AABB(
                        blockPos.getX() - (essenceBlockEntity.getArenaSize().getX() * 0.5f),
                        blockPos.getY() - (essenceBlockEntity.getArenaSize().getY() * 0.5f),
                        blockPos.getZ() - (essenceBlockEntity.getArenaSize().getZ() * 0.5f),
                        blockPos.getX() + (essenceBlockEntity.getArenaSize().getX() * 0.5f),
                        blockPos.getY() + (essenceBlockEntity.getArenaSize().getY() * 0.5f),
                        blockPos.getZ() + (essenceBlockEntity.getArenaSize().getZ() * 0.5f)
                ));
                frames.forEach(frame -> eventEntitiesInArena.add(new EssenceBlockEntity.EventEntities(frame.getUUID())));
            }
        }

        if (!eventEntitiesInArena.isEmpty()) {
            Entity entity = null;

            for (EssenceBlockEntity.EventEntities eventEntity : eventEntitiesInArena) {
                entity = serverLevel.getEntity(eventEntity.uuid());
                if (entity instanceof RootminEntity) {
                    break;
                }
                else {
                    entity = null;
                }
            }

            float progress = essenceBlockEntity.getEventBar().getProgress();
            if (progress == 0 && entity == null) {
                EssenceBlockEntity.EndEvent(serverLevel, blockPos, blockState, essenceBlockEntity, true);
                return;
            }

            if (entity == null) {
                entity = spawnRootmin(serverLevel, essenceBlockEntity, rootminPos, eventEntitiesInArena);
            }

            if (entity == null ||
                !(entity instanceof RootminEntity rootminEntity && rootminEntity.getEssenceController().equals(essenceBlockEntity.getUUID())))
            {
                eventEntitiesInArena.remove(0);
                return;
            }

            int hitsLeft = Math.round(progress * ROOTMIN_HEALTH);
            RootminState rootminState = rootminEntity.getRootminPose();
            if (!rootminEntity.isDeadOrDying()) {
                rootminEntity.setHealth(hitsLeft);
            }
            float rootminHealthPercent = rootminEntity.getHealth() / ROOTMIN_HEALTH;

            if (rootminState == RootminState.SHOCK || rootminState == RootminState.ANGRY || rootminState == RootminState.CURSE) {
                if (rootminEntity.getLastHurtByMob() != null) {
                    if (rootminState == RootminState.SHOCK) {
                        if (rootminHealthPercent > STAGE_2_THRESHOLD && rootminHealthPercent < STAGE_2_THRESHOLD + 0.05f) {
                            rootminEntity.runAngry();
                        }
                        else if (rootminHealthPercent > STAGE_3_THRESHOLD && rootminHealthPercent < STAGE_3_THRESHOLD + 0.05f) {
                            rootminEntity.runAngry();
                        }
                        else if (rootminHealthPercent > STAGE_4_THRESHOLD && rootminHealthPercent < STAGE_4_THRESHOLD + 0.05f) {
                            rootminEntity.runCurse();
                        }
                        else if (rootminHealthPercent <= STAGE_4_THRESHOLD) {
                            rootminEntity.setRootminPose(RootminState.NONE);
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
                        rootminEntity.setRootminShield(false);

                        // Do behavior of shooting and stuff
                        boolean isHoming = rootminHealthPercent > STAGE_4_THRESHOLD && rootminHealthPercent <= STAGE_3_THRESHOLD;

                        if (isHoming) {
                            RandomSource randomSource = rootminEntity.getRandom();
                            rootminEntity.lookAt(
                                EntityAnchorArgument.Anchor.FEET,
                                players.getFirst().position().add(
                                    randomSource.nextDouble() * 21 - 10,
                                    randomSource.nextDouble() * 21 - 10,
                                    randomSource.nextDouble() * 21 - 10
                                ));
                        }
                        else {
                            rootminEntity.lookAt(EntityAnchorArgument.Anchor.FEET, players.getFirst().position());
                        }

                        if (rootminHealthPercent > STAGE_3_THRESHOLD && rootminHealthPercent <= STAGE_2_THRESHOLD) {
                            rootminEntity.runMultiShoot(
                                players.getFirst(),
                                (float)Mth.lerp(rootminHealthPercent, 1.1D, 0.8D),
                                3
                            );
                        }
                        else {
                            rootminEntity.runShoot(
                                players.getFirst(),
                                isHoming ? 0.8F : (float)Mth.lerp(rootminHealthPercent, 1.7D, 0.85D),
                                isHoming
                            );
                        }
                    }
                    else {
                        rootminEntity.lookAt(EntityAnchorArgument.Anchor.FEET, Vec3.atLowerCornerOf(Direction.WEST.getNormal()).add(rootminEntity.position()));
                        rootminEntity.setRootminShield(true);
                    }
                }
            }

            float newProgress = ((float)hitsLeft) / ROOTMIN_HEALTH;
            essenceBlockEntity.getEventBar().setProgress(newProgress);
            essenceBlockEntity.setChanged();
        }
    }

    private static Entity spawnRootmin(ServerLevel serverLevel, EssenceBlockEntity essenceBlockEntity, BlockPos rootminPos, List<EssenceBlockEntity.EventEntities> eventEntitiesInArena) {
        Entity entity = BzEntities.ROOTMIN.get().spawn(serverLevel, rootminPos, MobSpawnType.TRIGGERED);
        if (entity instanceof RootminEntity rootminEntity) {
            rootminEntity.setRootminShield(true);
            rootminEntity.setEssenceController(essenceBlockEntity.getUUID());
            rootminEntity.setEssenceControllerBlockPos(essenceBlockEntity.getBlockPos());
            rootminEntity.setEssenceControllerDimension(serverLevel.dimension());

            AttributeInstance livingEntityAttributeHealth = rootminEntity.getAttribute(Attributes.MAX_HEALTH);
            if (livingEntityAttributeHealth != null) {
                float extraHealth = ROOTMIN_HEALTH - rootminEntity.getMaxHealth();
                livingEntityAttributeHealth.addPermanentModifier(new AttributeModifier(
                        ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "essence_arena_health_boost"),
                        extraHealth,
                        AttributeModifier.Operation.ADD_VALUE));
                rootminEntity.heal(extraHealth + rootminEntity.getMaxHealth());
            }

            AttributeInstance knockbackResistanceAttribute = rootminEntity.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
            if (knockbackResistanceAttribute != null) {
                knockbackResistanceAttribute.addPermanentModifier(new AttributeModifier(
                        ResourceLocation.fromNamespaceAndPath(Bumblezone.MODID, "essence_arena_knockback_resistance_boost"),
                        0.25d,
                        AttributeModifier.Operation.ADD_VALUE));
            }

            entity.lookAt(EntityAnchorArgument.Anchor.EYES, Vec3.atLowerCornerOf(Direction.SOUTH.getNormal()));
            eventEntitiesInArena.add(new EssenceBlockEntity.EventEntities(entity.getUUID()));
        }
        return entity;
    }

    @Override
    public void onPlayerEnter(ServerLevel serverLevel, ServerPlayer serverPlayer, EssenceBlockEntity essenceBlockEntity) {
        MusicPacketFromServer.sendToClient(serverPlayer, BzSounds.LIFE_EVENT.get().getLocation(), true);
        super.onPlayerEnter(serverLevel, serverPlayer, essenceBlockEntity);
    }

    @Override
    public void onPlayerLeave(ServerLevel serverLevel, ServerPlayer serverPlayer, EssenceBlockEntity essenceBlockEntity) {
        MusicPacketFromServer.sendToClient(serverPlayer, BzSounds.LIFE_EVENT.get().getLocation(), false);
        super.onPlayerLeave(serverLevel, serverPlayer, essenceBlockEntity);
    }
}
