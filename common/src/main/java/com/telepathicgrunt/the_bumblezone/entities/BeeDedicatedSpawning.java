package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.services.PlatformService;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class BeeDedicatedSpawning {
    private BeeDedicatedSpawning() {}

    private static int ACTIVE_ENTITIES = 0;
    private static final Set<Bee> BEE_SET = new HashSet<>();

    public static void updateEntityCount(ServerLevel world) {
        BEE_SET.clear();
        int counter = 0;
        for (Entity entity : world.getAllEntities()) {
            if (entity.isAlive() && entity instanceof LivingEntity) {
                counter++;
            }

            if(entity instanceof Bee) {
                BEE_SET.add((Bee)entity);
            }
        }

        ACTIVE_ENTITIES = counter;
        BEE_SET.removeIf(bee ->
                bee.isPersistenceRequired()
                        || bee.hasHive()
                        || bee.hasCustomName()
                        || bee.isLeashed()
                        || bee.isVehicle()
                        || bee.isNoAi());
    }

    public static int getNearbyActiveEntitiesInDimension(ServerLevel level, BlockPos position) {
        if (level.dimension().equals(BzDimension.BZ_WORLD_KEY)) {
            return ACTIVE_ENTITIES;
        }
        else {
            return level.getEntitiesOfClass(
                    Bee.class,
                    new AABB(
                            Vec3.atLowerCornerOf(position.offset(-16, -16,-16)),
                            Vec3.atLowerCornerOf(position.offset(16, 16,16))
                    )
            ).size();
        }
    }

    public static void adjustEntityCountInBz(int adjust) {
        ACTIVE_ENTITIES += adjust;
    }

    public static Set<Bee> getAllWildBees() {
        return BEE_SET;
    }

    public static void specialSpawnBees(ServerLevel level) {
        int despawnDistance = 80;
        int entityCountChange = 0;
        Set<Bee> allWildBees = getAllWildBees();
        List<ServerPlayer> serverPlayers = level.players();

        // Remove all wild bees too far from a player.
        for (Bee wildBee : allWildBees) {
            boolean isTooFar = true;
            
            for (ServerPlayer serverPlayer : serverPlayers) {
                if (PlatformService.INSTANCE.isFakePlayer(serverPlayer)) {
                    continue;
                }

                if (wildBee.position().subtract(serverPlayer.position()).length() <= despawnDistance) {
                    isTooFar = false;
                    break;
                }
            }

            if (isTooFar) {
                wildBee.remove(Entity.RemovalReason.DISCARDED);
                entityCountChange--;
            }
        }

        int beesPerPlayer = BzGeneralConfigs.nearbyBeesPerPlayerInBz;
        int maxWildBeeLimit = beesPerPlayer * serverPlayers.size();
        if (allWildBees.size() <= maxWildBeeLimit) {
            for (ServerPlayer serverPlayer : serverPlayers) {
                if (PlatformService.INSTANCE.isFakePlayer(serverPlayer)) {
                    continue;
                }

                int nearbyBees = 0;
                for (Entity entity : level.getEntities(serverPlayer, serverPlayer.getBoundingBox().inflate(despawnDistance, despawnDistance, despawnDistance))) {
                    if (entity instanceof Bee) {
                        nearbyBees++;
                    }
                }

                for (int i = nearbyBees; i <= beesPerPlayer; i++) {
                    BlockPos newBeePos = GeneralUtils.getRandomBlockposWithinRange(serverPlayer, 45, 20);

                    if (!level.shouldTickBlocksAt(newBeePos) || !level.getBlockState(newBeePos).isAir()) {
                        continue;
                    }

                    Bee newBee = (BzGeneralConfigs.variantBeeTypes.size() > 0 && level.getRandom().nextFloat() < BzGeneralConfigs.variantBeeAfterWorldgenSpawnRate) ?
                            BzEntities.VARIANT_BEE.get().create(level, EntitySpawnReason.NATURAL) : EntityType.BEE.create(level, EntitySpawnReason.NATURAL);

                    newBee.setPos(Vec3.atCenterOf(newBeePos));
                    newBee.setDeltaMovement(new Vec3(0, 1D, 0));
                    newBee.setSpeed(0);
                    newBee.finalizeSpawn(level, level.getCurrentDifficultyAt(newBee.blockPosition()), EntitySpawnReason.NATURAL, null);

                    PlatformService.INSTANCE.finalizeSpawn(newBee, level, null, EntitySpawnReason.NATURAL);
                    level.addFreshEntity(newBee);
                    entityCountChange++;
                }
            }
        }

        adjustEntityCountInBz(entityCountChange);
    }
}
