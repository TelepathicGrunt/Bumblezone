package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.capabilities.BzCapabilities;
import com.telepathicgrunt.the_bumblezone.capabilities.IEntityPosAndDim;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.tags.BzBlockTags;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzDimension;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzWorldSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;

public class EntityTeleportationHookup {

    ////////////////////////////////////////////////////////////
    // Methods that setup and call PlayerTeleportationBackend //

    //Living Entity ticks
    public static void entityTick(LivingEvent.LivingUpdateEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();

        //Makes it so player does not get killed for falling into the void
        if (livingEntity.getCommandSenderWorld().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID)) {
            if (livingEntity.getY() < -2) {
                if(livingEntity instanceof ServerPlayer && livingEntity.fallDistance > 100 && livingEntity.getDeltaMovement().y() < -1) {
                    BzCriterias.TELEPORT_OUT_OF_BUMBLEZONE_FALL_TRIGGER.trigger((ServerPlayer) livingEntity);
                }

                if (livingEntity.getY() < -4) {
                    livingEntity.moveTo(livingEntity.getX(), -4, livingEntity.getZ());
                    livingEntity.absMoveTo(livingEntity.getX(), -4, livingEntity.getZ());
                }
                livingEntity.fallDistance = 0;

                if(!livingEntity.level.isClientSide()) {
                    teleportOutOfBz(livingEntity);
                }
            }
            else if (livingEntity.getY() > 255) {
                if (livingEntity.getY() > 257) {
                    livingEntity.moveTo(livingEntity.getX(), 257, livingEntity.getZ());
                    livingEntity.absMoveTo(livingEntity.getX(), 257, livingEntity.getZ());
                }

                if(!livingEntity.level.isClientSide()) {
                    //TODO: fix null cap on spectator player teleporting by command to bumblezone.
                    //teleportOutOfBz(livingEntity);
                }
            }
        }
    }


    public static void teleportOutOfBz(LivingEntity livingEntity) {
        if (!livingEntity.level.isClientSide()) {
            checkAndCorrectStoredDimension(livingEntity);
            MinecraftServer minecraftServer = livingEntity.getServer(); // the server itself
            IEntityPosAndDim capability = livingEntity.getCapability(BzCapabilities.ENTITY_POS_AND_DIM_CAPABILITY).orElseThrow(RuntimeException::new);
            ResourceKey<Level> worldKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, capability.getNonBZDim());
            ServerLevel serverWorld = minecraftServer.getLevel(worldKey);
            if(serverWorld == null) {
                serverWorld = minecraftServer.getLevel(Level.OVERWORLD);
            }
            BzWorldSavedData.queueEntityToTeleport(livingEntity, serverWorld.dimension());
        }
    }

    // Enderpearl
    public static boolean runEnderpearlImpact(Vec3 hitBlockPos, Entity thrower){
        Level world = thrower.level; // world we threw in

        // Make sure we are on server by checking if thrower is ServerPlayer and that we are not in bumblezone.
        // If onlyOverworldHivesTeleports is set to true, then only run this code in Overworld.
        if (!world.isClientSide() && thrower instanceof ServerPlayer playerEntity &&
            !world.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) &&
            (!BzDimensionConfigs.onlyOverworldHivesTeleports.get() || world.dimension().equals(Level.OVERWORLD)))
        {
            // the thrower
            BlockPos hivePos = new BlockPos(0,0,0);
            boolean hitHive = false;

            //check with offset in all direction as the position of exact hit point could barely be outside the hive block
            //even through the pearl hit the block directly.
            for(double offset = -0.99D; offset <= 0.99D; offset += 0.99D) {
                for(double offset2 = -0.99D; offset2 <= 0.99D; offset2 += 0.99D) {
                    for (double offset3 = -0.99D; offset3 <= 0.99D; offset3 += 0.99D) {
                        BlockPos offsettedHitPos = new BlockPos(hitBlockPos.add(offset, offset2, offset3));
                        BlockState block = world.getBlockState(offsettedHitPos);
                        if(EntityTeleportationBackend.isValidBeeHive(block)) {
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
            if(!BzBlockTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT.getValues().isEmpty()) {
                if(BzBlockTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT.contains(world.getBlockState(hivePos.below()).getBlock())) {
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
            if (hitHive && validBelowBlock) {
                BzCriterias.TELEPORT_TO_BUMBLEZONE_PEARL_TRIGGER.trigger(playerEntity);
                BzWorldSavedData.queueEntityToTeleport(playerEntity, BzDimension.BZ_WORLD_KEY);
                return true;
            }
        }
        return false;
    }


    public static void runPistonPushed(Direction direction, LivingEntity pushedEntity) {
        ServerLevel world = (ServerLevel) pushedEntity.level;

        // If onlyOverworldHivesTeleports is set to true, then only run this code in Overworld.
        if (!world.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) &&
                (!BzDimensionConfigs.onlyOverworldHivesTeleports.get() || world.dimension().equals(Level.OVERWORLD)))
        {
            if(BzWorldSavedData.isEntityQueuedToTeleportAlready(pushedEntity)) return; // Skip checks if entity is teleporting already to Bz.

            BlockPos hivePos = new BlockPos(0,0,0);
            BlockPos.MutableBlockPos entityPos = new BlockPos.MutableBlockPos().set(pushedEntity.blockPosition());

            // Checks if entity is pushed into hive block (the mutable is moved for each check and enters early if any is true)
            if (EntityTeleportationBackend.isValidBeeHive(world.getBlockState(entityPos)) ||
                    EntityTeleportationBackend.isValidBeeHive(world.getBlockState(entityPos.move(Direction.UP))) ||
                    EntityTeleportationBackend.isValidBeeHive(world.getBlockState(entityPos.move(direction))) ||
                    EntityTeleportationBackend.isValidBeeHive(world.getBlockState(entityPos.move(Direction.DOWN))))
            {
                //checks if block under hive is correct if config needs one
                boolean validBelowBlock = false;
                if(!BzBlockTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT.getValues().isEmpty()) {
                    if(BzBlockTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT.contains(world.getBlockState(hivePos.below()).getBlock())) {
                        validBelowBlock = true;
                    }
                    else if(BzDimensionConfigs.warnPlayersOfWrongBlockUnderHive.get()) {
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
        IEntityPosAndDim capability = livingEntity.getCapability(BzCapabilities.ENTITY_POS_AND_DIM_CAPABILITY).orElseThrow(RuntimeException::new);
        if (capability.getNonBZDim().equals(Bumblezone.MOD_DIMENSION_ID) ||
                BzDimensionConfigs.forceExitToOverworld.get())
        {
            // go to overworld by default
            //update stored dimension
            capability.setNonBZDim(Level.OVERWORLD.location());
        }
    }
}
