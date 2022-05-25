package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.capabilities.BzCapabilities;
import com.telepathicgrunt.the_bumblezone.capabilities.EntityPositionAndDimension;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzDimension;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzWorldSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntityTeleportationHookup {

    ////////////////////////////////////////////////////////////
    // Methods that setup and call PlayerTeleportationBackend //

    //Living Entity ticks
    public static void entityTick(LivingEvent.LivingUpdateEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();

        //Makes it so player does not get killed for falling into the void
        if (livingEntity.level.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID)) {
            if (livingEntity.getY() < -2) {
                if(livingEntity instanceof ServerPlayer && livingEntity.fallDistance > 100 && livingEntity.getDeltaMovement().y() < -1) {
                    BzCriterias.TELEPORT_OUT_OF_BUMBLEZONE_FALL_TRIGGER.trigger((ServerPlayer) livingEntity);
                }

                if(BzDimensionConfigs.enableExitTeleportation.get()) {
                    if (livingEntity.getY() < -4) {
                        livingEntity.moveTo(livingEntity.getX(), -4, livingEntity.getZ());
                        livingEntity.absMoveTo(livingEntity.getX(), -4, livingEntity.getZ());
                    }
                    livingEntity.fallDistance = 0;

                    if (!livingEntity.level.isClientSide()) {
                        teleportOutOfBz(livingEntity);
                    }
                }
            }
            else if (livingEntity.getY() > 255) {
                if(BzDimensionConfigs.enableExitTeleportation.get()) {
                    if (livingEntity.getY() > 257) {
                        livingEntity.moveTo(livingEntity.getX(), 257, livingEntity.getZ());
                        livingEntity.absMoveTo(livingEntity.getX(), 257, livingEntity.getZ());
                    }

                    if (!livingEntity.level.isClientSide()) {
                        teleportOutOfBz(livingEntity);
                    }
                }
            }
        }
    }


    public static void teleportOutOfBz(LivingEntity livingEntity) {
        if (!livingEntity.level.isClientSide()) {
            checkAndCorrectStoredDimension(livingEntity);
            MinecraftServer minecraftServer = livingEntity.getServer(); // the server itself
            ResourceKey<Level> worldKey;

            if (livingEntity.getControllingPassenger() == null) {
                EntityPositionAndDimension capability = livingEntity.getCapability(BzCapabilities.ENTITY_POS_AND_DIM_CAPABILITY).orElseThrow(RuntimeException::new);
                worldKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, capability.getNonBZDim());
            }
            else {
                if(livingEntity.getControllingPassenger() instanceof LivingEntity livingEntity2) {
                    checkAndCorrectStoredDimension(livingEntity2);
                }
                EntityPositionAndDimension capability = livingEntity.getControllingPassenger().getCapability(BzCapabilities.ENTITY_POS_AND_DIM_CAPABILITY).orElseThrow(RuntimeException::new);
                worldKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, capability.getNonBZDim());
            }

            ServerLevel serverWorld = minecraftServer.getLevel(worldKey);
            if(serverWorld == null) {
                serverWorld = minecraftServer.getLevel(Level.OVERWORLD);
            }
            BzWorldSavedData.queueEntityToTeleport(livingEntity, serverWorld.dimension());
        }
    }

    // Enderpearl
    public static boolean runEnderpearlImpact(Vec3 hitPos, Entity thrower, Entity pearl){
        Level world = thrower.level; // world we threw in

        // Make sure we are on server by checking if thrower is ServerPlayer and that we are not in bumblezone.
        // If onlyOverworldHivesTeleports is set to true, then only run this code in Overworld.
        if (BzDimensionConfigs.enableEntranceTeleportation.get() &&
            !world.isClientSide() && thrower instanceof ServerPlayer playerEntity &&
            !world.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) &&
            (!BzDimensionConfigs.onlyOverworldHivesTeleports.get() || world.dimension().equals(Level.OVERWORLD)))
        {
            // get nearby hives
            BlockPos hivePos;
            hivePos = getNearbyHivePos(hitPos, world);

            // if fail, move the hit pos one step based on pearl velocity and try again
            if(hivePos == null) {
                hitPos = hitPos.add(pearl.getDeltaMovement());
                hivePos = getNearbyHivePos(hitPos, world);
            }

            // no hive hit, exit early
            if(hivePos == null) {
                return false;
            }

            //checks if block under hive is correct if config needs one
            boolean validBelowBlock = false;
            Optional<HolderSet.Named<Block>> blockTag = Registry.BLOCK.getTag(BzTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT);
            if(blockTag.isPresent() && blockTag.get().size() != 0) {
                if(world.getBlockState(hivePos.below()).is(BzTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT)) {
                    validBelowBlock = true;
                }
                else if(BzDimensionConfigs.warnPlayersOfWrongBlockUnderHive.get())
                {
                    //failed. Block below isn't the required block
                    Bumblezone.LOGGER.log(org.apache.logging.log4j.Level.INFO, "Bumblezone: the_bumblezone:required_blocks_under_hive_to_teleport tag does not have the block below the hive.");
                    Component message = new TextComponent("the_bumblezone:required_blocks_under_hive_to_teleport tag does not have the block below the hive.");
                    playerEntity.displayClientMessage(message, true);
                    return false;
                }
            }
            else {
                validBelowBlock = true;
            }

            //if the pearl hit a beehive, begin the teleportation.
            if (validBelowBlock) {
                BzCriterias.TELEPORT_TO_BUMBLEZONE_PEARL_TRIGGER.trigger(playerEntity);
                BzWorldSavedData.queueEntityToTeleport(playerEntity, BzDimension.BZ_WORLD_KEY);
                return true;
            }
        }
        return false;
    }

    private static BlockPos getNearbyHivePos(Vec3 hitBlockPos, Level world) {
        double checkRadius = 0.5D;
        //check with offset in all direction as the position of exact hit point could barely be outside the hive block
        //even through the pearl hit the block directly.
        for(double offset = -checkRadius; offset <= checkRadius; offset += checkRadius) {
            for(double offset2 = -checkRadius; offset2 <= checkRadius; offset2 += checkRadius) {
                for (double offset3 = -checkRadius; offset3 <= checkRadius; offset3 += checkRadius) {
                    BlockPos offsettedHitPos = new BlockPos(hitBlockPos.add(offset, offset2, offset3));
                    BlockState block = world.getBlockState(offsettedHitPos);
                    if(EntityTeleportationBackend.isValidBeeHive(block)) {
                        return offsettedHitPos;
                    }
                }
            }
        }
        return null;
    }


    public static void runPistonPushed(Direction direction, LivingEntity pushedEntity) {
        ServerLevel world = (ServerLevel) pushedEntity.level;

        // If onlyOverworldHivesTeleports is set to true, then only run this code in Overworld.
        if (BzDimensionConfigs.enableEntranceTeleportation.get() &&
            !world.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) &&
            (!BzDimensionConfigs.onlyOverworldHivesTeleports.get() || world.dimension().equals(Level.OVERWORLD)))
        {
            if(BzWorldSavedData.isEntityQueuedToTeleportAlready(pushedEntity)) return; // Skip checks if entity is teleporting already to Bz.

            BlockPos.MutableBlockPos entityPos = new BlockPos.MutableBlockPos().set(pushedEntity.blockPosition());
            BlockPos[] blockPositions = new BlockPos[]{
                    entityPos,
                    entityPos.relative(direction),
                    entityPos.relative(Direction.UP),
                    entityPos.relative(Direction.UP).relative(direction)
            };
            List<BlockState> belowHiveBlocks = new ArrayList<>();

            // Checks if entity is pushed into hive block
            boolean isPushedIntoBeehive = false;
            for(BlockPos pos : blockPositions) {
                if(EntityTeleportationBackend.isValidBeeHive(world.getBlockState(pos))) {
                    isPushedIntoBeehive = true;
                    belowHiveBlocks.add(world.getBlockState(pos.below()));
                }
            }

            if (isPushedIntoBeehive) {
                //checks if block under hive is correct if config needs one
                boolean validBelowBlock = false;
                Optional<HolderSet.Named<Block>> blockTag = Registry.BLOCK.getTag(BzTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT);
                if(blockTag.isPresent() && blockTag.get().size() != 0) {

                    for(BlockState belowBlock : belowHiveBlocks) {
                        if(belowBlock.is(BzTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT)) {
                            validBelowBlock = true;
                        }
                    }

                    if(!validBelowBlock && BzDimensionConfigs.warnPlayersOfWrongBlockUnderHive.get()) {
                        if(pushedEntity instanceof Player playerEntity) {
                            //failed. Block below isn't the required block
                            Bumblezone.LOGGER.log(org.apache.logging.log4j.Level.INFO, "Bumblezone: the_bumblezone:required_blocks_under_hive_to_teleport tag does not have the block below the hive.");
                            Component message = new TextComponent("the_bumblezone:required_blocks_under_hive_to_teleport tag does not have the block below the hive.");
                            playerEntity.displayClientMessage(message, true);
                        }
                        return;
                    }
                }
                else {
                    validBelowBlock = true;
                }

                //if the entity was pushed into a beehive, begin the teleportation.
                if (validBelowBlock) {
                    if(pushedEntity instanceof ServerPlayer) {
                        BzCriterias.TELEPORT_TO_BUMBLEZONE_PISTON_TRIGGER.trigger((ServerPlayer) pushedEntity);
                    }
                    BzWorldSavedData.queueEntityToTeleport(pushedEntity, BzDimension.BZ_WORLD_KEY);
                }
            }
        }
    }


    /**
     * Looks at stored non-bz dimension and changes it to Overworld if it is
     * BZ dimension or the config forces going to Overworld.
     */
    private static void checkAndCorrectStoredDimension(LivingEntity livingEntity) {
        //Error. This shouldn't be. We aren't leaving the bumblezone to go to the bumblezone.
        //Go to Overworld instead as default. Or go to Overworld if config is set.
        EntityPositionAndDimension capability = livingEntity.getCapability(BzCapabilities.ENTITY_POS_AND_DIM_CAPABILITY).orElseThrow(RuntimeException::new);
        if (capability.getNonBZDim().equals(Bumblezone.MOD_DIMENSION_ID) || BzDimensionConfigs.forceExitToOverworld.get()) {
            // go to overworld by default
            //update stored dimension
            capability.setNonBZDim(Level.OVERWORLD.location());
        }
    }
}
