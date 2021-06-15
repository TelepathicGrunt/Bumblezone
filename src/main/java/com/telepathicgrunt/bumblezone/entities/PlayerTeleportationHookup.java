package com.telepathicgrunt.bumblezone.entities;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.tags.BZBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;

public class PlayerTeleportationHookup {

    ////////////////////////////////////////////////////////////
    // Methods that setup and call PlayerTeleportationBackend //

    //Player ticks
    public static void playerTick(PlayerEntity playerEntity){
        //Makes it so player does not get killed for falling into the void
        if (playerEntity.getEntityWorld().getRegistryKey().getValue().equals(Bumblezone.MOD_DIMENSION_ID)) {
            if (playerEntity.getY() < -3) {
                playerEntity.refreshPositionAfterTeleport(playerEntity.getX(), -3.01D, playerEntity.getZ());
                playerEntity.fallDistance = 0;

                if(!playerEntity.world.isClient)
                    teleportOutOfBz(playerEntity);
            } else if (playerEntity.getY() > 255) {
                playerEntity.refreshPositionAfterTeleport(playerEntity.getX(), 255.01D, playerEntity.getZ());
                if(!playerEntity.world.isClient)
                    teleportOutOfBz(playerEntity);
            }
        }
        //teleport to bumblezone
        else if (!playerEntity.world.isClient && Bumblezone.PLAYER_COMPONENT.get(playerEntity).getIsTeleporting()) {
            PlayerTeleportationBackend.enteringBumblezone(playerEntity);
            Bumblezone.PLAYER_COMPONENT.get(playerEntity).setIsTeleporting(false);
            reAddStatusEffect(playerEntity);
        }
    }

    private static void teleportOutOfBz(PlayerEntity playerEntity) {
        if (!playerEntity.world.isClient) {
            checkAndCorrectStoredDimension(playerEntity);
            MinecraftServer minecraftServer = playerEntity.getServer(); // the server itself
            RegistryKey<World> world_key = RegistryKey.of(Registry.WORLD_KEY, Bumblezone.PLAYER_COMPONENT.get(playerEntity).getNonBZDimension());
            ServerWorld serverWorld = minecraftServer.getWorld(world_key);
            if(serverWorld == null){
                serverWorld = minecraftServer.getWorld(World.OVERWORLD);
            }
            PlayerTeleportationBackend.exitingBumblezone(playerEntity, serverWorld);
            reAddStatusEffect(playerEntity);
        }
    }

    // Enderpearl
    public static boolean runEnderpearlImpact(HitResult hitResult, EnderPearlEntity pearlEntity){
        World world = pearlEntity.world; // world we threw in

        // Make sure we are on server by checking if thrower is ServerPlayerEntity and that we are not in bumblezone.
        // If onlyOverworldHivesTeleports is set to true, then only run this code in Overworld.
        if (!world.isClient && pearlEntity.getOwner() instanceof ServerPlayerEntity &&
            !world.getRegistryKey().getValue().equals(Bumblezone.MOD_DIMENSION_ID) &&
            (!Bumblezone.BZ_CONFIG.BZDimensionConfig.onlyOverworldHivesTeleports || world.getRegistryKey().equals(World.OVERWORLD)))
        {
            ServerPlayerEntity playerEntity = (ServerPlayerEntity) pearlEntity.getOwner(); // the thrower
            Vec3d hitBlockPos = hitResult.getPos(); //position of the collision
            BlockPos hivePos = new BlockPos(0,0,0);
            boolean hitHive = false;

            //check with offset in all direction as the position of exact hit point could barely be outside the hive block
            //even through the pearl hit the block directly.
            for(double offset = -0.99D; offset <= 0.99D; offset += 0.99D) {
                for(double offset2 = -0.99D; offset2 <= 0.99D; offset2 += 0.99D) {
                    for (double offset3 = -0.99D; offset3 <= 0.99D; offset3 += 0.99D) {
                        BlockPos offsettedHitPos = new BlockPos(hitBlockPos.add(offset, offset2, offset3));
                        BlockState block = world.getBlockState(offsettedHitPos);
                        if(PlayerTeleportationBackend.isValidBeeHive(block)) {
                            hitHive = true;
                            hivePos = offsettedHitPos;
                            offset = 1;
                            offset2 = 1;
                            break;
                        }
                    }
                }
            }

            //checks if block under hive is correct if config needs one
            boolean validBelowBlock = false;
            if(!BZBlockTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT_TAG.values().isEmpty()) {
                if(BZBlockTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT_TAG.contains(world.getBlockState(hivePos.down()).getBlock())) {
                    validBelowBlock = true;
                }
                else if(Bumblezone.BZ_CONFIG.BZDimensionConfig.warnPlayersOfWrongBlockUnderHive)
                {
                    //failed. Block below isn't the required block
                    Bumblezone.LOGGER.log(Level.INFO, "Bumblezone: the_bumblezone:required_blocks_under_hive_to_teleport tag does not have the block below the hive.");
                    Text message = new LiteralText("the_bumblezone:required_blocks_under_hive_to_teleport tag does not have the block below the hive.");
                    playerEntity.sendMessage(message, true);
                    return false;
                }
            }
            else {
                validBelowBlock = true;
            }


            //if the pearl hit a beehive, begin the teleportation.
            if (hitHive && validBelowBlock) {
                Bumblezone.PLAYER_COMPONENT.get(playerEntity).setIsTeleporting(true);
                pearlEntity.discard();
                return true;
            }
        }
        return false;
    }

    ///////////
    // Utils //

    /**
     * Temporary fix until Mojang patches the bug that makes potion effect icons disappear when changing dimension.
     * To fix it ourselves, we remove the effect and re-add it to the player.
     */
    private static void reAddStatusEffect(PlayerEntity playerEntity) {
        //re-adds potion effects so the icon remains instead of disappearing when changing dimensions due to a bug
        ArrayList<StatusEffectInstance> effectInstanceList = new ArrayList<StatusEffectInstance>(playerEntity.getStatusEffects());
        for (int i = effectInstanceList.size() - 1; i >= 0; i--) {
            StatusEffectInstance effectInstance = effectInstanceList.get(i);
            if (effectInstance != null) {
                playerEntity.removeStatusEffect(effectInstance.getEffectType());
                playerEntity.addStatusEffect(
                        new StatusEffectInstance(
                                effectInstance.getEffectType(),
                                effectInstance.getDuration(),
                                effectInstance.getAmplifier(),
                                effectInstance.isAmbient(),
                                effectInstance.shouldShowParticles(),
                                effectInstance.shouldShowIcon()));
            }
        }
    }

    /**
     * Looks at stored non-bz dimension and changes it to Overworld if it is
     * BZ dimension or the config forces going to Overworld.
     */
    private static void checkAndCorrectStoredDimension(PlayerEntity playerEntity) {
        //Error. This shouldn't be. We aren't leaving the bumblezone to go to the bumblezone.
        //Go to Overworld instead as default. Or go to Overworld if config is set.
        if (Bumblezone.PLAYER_COMPONENT.get(playerEntity).getNonBZDimension().equals(Bumblezone.MOD_DIMENSION_ID) ||
                Bumblezone.BZ_CONFIG.BZDimensionConfig.forceExitToOverworld)
        {
            // go to overworld by default
            //update stored dimension
            Bumblezone.PLAYER_COMPONENT.get(playerEntity).setNonBZDimension(World.OVERWORLD.getValue());
        }
    }
}
