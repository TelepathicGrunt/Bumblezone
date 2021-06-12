package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.capabilities.IPlayerPosAndDim;
import com.telepathicgrunt.the_bumblezone.capabilities.PlayerPositionAndDimension;
import com.telepathicgrunt.the_bumblezone.tags.BZBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;

public class PlayerTeleportationHookup {

    @CapabilityInject(IPlayerPosAndDim.class)
    public static Capability<IPlayerPosAndDim> PAST_POS_AND_DIM = null;

    ////////////////////////////////////////////////////////////
    // Methods that setup and call PlayerTeleportationBackend //

    //Player ticks
    public static void playerTick(PlayerEntity playerEntity){
        //Bumblezone.LOGGER.log(Level.INFO, "started");
        //grabs the capability attached to player for dimension hopping

        //Makes it so player does not get killed for falling into the void
        if (playerEntity.getCommandSenderWorld().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID)) {
            if (playerEntity.getY() < -3) {
                playerEntity.setPosAndOldPos(playerEntity.getX(), -3.01D, playerEntity.getZ());
                playerEntity.fallDistance = 0;

                if(!playerEntity.level.isClientSide)
                    teleportOutOfBz(playerEntity);
            } else if (playerEntity.getY() > 255) {
                playerEntity.setPosAndOldPos(playerEntity.getX(), 255.01D, playerEntity.getZ());
                if(!playerEntity.level.isClientSide)
                    teleportOutOfBz(playerEntity);
            }
        }
        //teleport to bumblezone
        else if(!playerEntity.level.isClientSide){
            LazyOptional<IPlayerPosAndDim> lazyOptionalCap = playerEntity.getCapability(PAST_POS_AND_DIM);
            if (lazyOptionalCap.isPresent()) {
                PlayerPositionAndDimension cap = (PlayerPositionAndDimension) lazyOptionalCap.orElseThrow(RuntimeException::new);
                if (cap.getTeleporting()) {
                    PlayerTeleportationBackend.enteringBumblezone(playerEntity);
                    cap.setTeleporting(false);
                    reAddStatusEffect(playerEntity);
                }
            }
        }
    }

    private static void teleportOutOfBz(PlayerEntity playerEntity) {
        if (!playerEntity.getCommandSenderWorld().isClientSide) {
            checkAndCorrectStoredDimension(playerEntity);
            PlayerPositionAndDimension cap = (PlayerPositionAndDimension) playerEntity.getCapability(PAST_POS_AND_DIM).orElseThrow(RuntimeException::new);
            RegistryKey<World> world_key = RegistryKey.create(Registry.DIMENSION_REGISTRY, cap.getNonBZDim());
            ServerWorld destination = playerEntity.getCommandSenderWorld().getServer().getLevel(world_key);
            if(destination == null){
                destination = playerEntity.getCommandSenderWorld().getServer().getLevel(World.OVERWORLD);
            }
            PlayerTeleportationBackend.exitingBumblezone(playerEntity, destination);
            reAddStatusEffect(playerEntity);
        }
    }


    // Enderpearl
    public static boolean runEnderpearlImpact(Vector3d hitBlockPos, Entity thrower){
        World world = thrower.level; // world we threw in

        // Make sure we are on server by checking if thrower is ServerPlayerEntity and that we are not in bumblezone.
        // If onlyOverworldHivesTeleports is set to true, then only run this code in Overworld.
        if (!world.isClientSide && thrower instanceof ServerPlayerEntity &&
                !world.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) &&
                (!Bumblezone.BzDimensionConfig.onlyOverworldHivesTeleports.get() || world.dimension().equals(World.OVERWORLD)))
        {
            ServerPlayerEntity playerEntity = (ServerPlayerEntity) thrower; // the thrower
            BlockPos hivePos = new BlockPos(0,0,0);
            boolean hitHive = false;

            //check with offset in all direction as the position of exact hit point could barely be outside the hive block
            //even through the pearl hit the block directly.
            for(double offsetX = -0.99D; offsetX <= 0.99D; offsetX += 0.99D) {
                for(double offsetY = -0.99D; offsetY <= 0.99D; offsetY += 0.99D) {
                    for (double offsetZ = -0.99D; offsetZ <= 0.99D; offsetZ += 0.99D) {
                        BlockPos offsettedHitPos = new BlockPos(hitBlockPos.add(offsetX, offsetY, offsetZ));
                        BlockState block = world.getBlockState(offsettedHitPos);
                        if(PlayerTeleportationBackend.isValidBeeHive(block)) {
                            hitHive = true;
                            hivePos = offsettedHitPos;

                            // break out of all 3 loops
                            offsetX = 1;
                            offsetY = 1;
                            break;
                        }
                    }
                }
            }

            //checks if block under hive is correct if config needs one
            boolean validBelowBlock = false;
            if(!BZBlockTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT.getValues().isEmpty()) {
                if(BZBlockTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT.contains(world.getBlockState(hivePos.below()).getBlock())) {
                    validBelowBlock = true;
                }
                else if(Bumblezone.BzDimensionConfig.warnPlayersOfWrongBlockUnderHive.get()) {
                    //failed. Block below isn't the required block
                    Bumblezone.LOGGER.log(Level.INFO, "Bumblezone: the_bumblezone:required_blocks_under_hive_to_teleport tag does not have the block below the hive.");
                    ITextComponent message = new StringTextComponent("the_bumblezone:required_blocks_under_hive_to_teleport tag does not have the block below the hive.");
                    playerEntity.displayClientMessage(message, true);
                    return false;
                }
            }
            else {
                validBelowBlock = true;
            }


            //if the pearl hit a beehive, begin the teleportation.
            if (hitHive && validBelowBlock) {
                PlayerPositionAndDimension cap = (PlayerPositionAndDimension) playerEntity.getCapability(PAST_POS_AND_DIM).orElseThrow(RuntimeException::new);
                cap.setTeleporting(true);
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
        ArrayList<EffectInstance> effectInstanceList = new ArrayList<>(playerEntity.getActiveEffects());
        for (int i = effectInstanceList.size() - 1; i >= 0; i--) {
            EffectInstance effectInstance = effectInstanceList.get(i);
            if (effectInstance != null) {
                playerEntity.removeEffect(effectInstance.getEffect());
                playerEntity.addEffect(
                        new EffectInstance(
                                effectInstance.getEffect(),
                                effectInstance.getDuration(),
                                effectInstance.getAmplifier(),
                                effectInstance.isAmbient(),
                                effectInstance.isVisible(),
                                effectInstance.showIcon()));
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
        PlayerPositionAndDimension cap = (PlayerPositionAndDimension) playerEntity.getCapability(PAST_POS_AND_DIM).orElseThrow(RuntimeException::new);
        if (cap.getNonBZDim().equals(Bumblezone.MOD_DIMENSION_ID) ||
                Bumblezone.BzDimensionConfig.forceExitToOverworld.get())
        {
            // go to overworld by default
            //update stored dimension
            cap.setNonBZDim(World.OVERWORLD.location());
        }
    }
}
