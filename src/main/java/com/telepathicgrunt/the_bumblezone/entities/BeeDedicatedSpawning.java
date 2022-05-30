package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Set;

public final class BeeDedicatedSpawning {
    private BeeDedicatedSpawning() {}

    public static void specialSpawnBees(ServerLevel world) {
        int despawnDistance = 80;
        int entityCountChange = 0;
        Set<Bee> allWildBees = GeneralUtils.getAllWildBees();
        List<ServerPlayer> serverPlayers = world.players();

        // Remove all wild bees too far from a player.
        for(Bee wildBee : allWildBees) {
            for (ServerPlayer serverPlayer : serverPlayers) {
                if(isFakePlayer(serverPlayer)) {
                    continue;
                }

                if(wildBee.position().subtract(serverPlayer.position()).length() > despawnDistance) {
                    wildBee.remove(Entity.RemovalReason.DISCARDED);
                    entityCountChange--;
                }
            }
        }

        int beesPerPlayer = Bumblezone.BZ_CONFIG.BZGeneralConfig.nearbyBeesPerPlayerInBz;
        int maxWildBeeLimit = beesPerPlayer * serverPlayers.size();
        if(allWildBees.size() <= maxWildBeeLimit) {
            for(ServerPlayer serverPlayer : serverPlayers) {
                if(isFakePlayer(serverPlayer)) {
                    continue;
                }

                int nearbyBees = 0;
                for (Entity entity : world.getEntities(serverPlayer, serverPlayer.getBoundingBox().inflate(despawnDistance, despawnDistance, despawnDistance))) {
                    if (entity instanceof Bee) {
                        nearbyBees++;
                    }
                }

                for(int i = nearbyBees; i <= beesPerPlayer; i++) {
                    BlockPos newBeePos = GeneralUtils.getRandomBlockposWithinRange(world, serverPlayer, 50, 25);
                    if(world.getBlockState(newBeePos).getMaterial() != Material.AIR) {
                        continue;
                    }

                    Bee newBee = EntityType.BEE.create(world);
                    newBee.setPos(Vec3.atCenterOf(newBeePos));
                    newBee.setDeltaMovement(new Vec3(0, 1D, 0));
                    newBee.setSpeed(0);
                    newBee.finalizeSpawn(world, world.getCurrentDifficultyAt(newBee.blockPosition()), MobSpawnType.NATURAL, null, null);
                    world.addFreshEntity(newBee);
                    entityCountChange++;
                }
            }
        }

        GeneralUtils.adjustEntityCountInBz(entityCountChange);
    }

    private static boolean isFakePlayer(Player player) {
        return player instanceof ServerPlayer && player.getClass() != ServerPlayer.class;
    }
}
