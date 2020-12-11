package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.capabilities.IPlayerPosAndDim;
import com.telepathicgrunt.the_bumblezone.capabilities.PlayerPositionAndDimension;
import com.telepathicgrunt.the_bumblezone.dimension.BzPlayerPlacement;
import com.telepathicgrunt.the_bumblezone.modCompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modCompat.ProductiveBeesRedirection;
import com.telepathicgrunt.the_bumblezone.modCompat.ResourcefulBeesRedirection;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;

public class PlayerTeleportation {

    public static final ITag.INamedTag<Block> REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT_TAG = BlockTags.makeWrapperTag(Bumblezone.MODID+":required_blocks_under_hive_to_teleport");
    private static final ITag.INamedTag<Block> BLACKLISTED_TELEPORTATION_HIVES_TAG = BlockTags.makeWrapperTag(Bumblezone.MODID+":blacklisted_teleportable_hive_blocks");

    @CapabilityInject(IPlayerPosAndDim.class)
    public static Capability<IPlayerPosAndDim> PAST_POS_AND_DIM = null;

    //Player ticks
    public static void playerTick(PlayerEntity playerEntity){
        //Bumblezone.LOGGER.log(Level.INFO, "started");
        //grabs the capability attached to player for dimension hopping

        //Makes it so player does not get killed for falling into the void
        PlayerPositionAndDimension cap = (PlayerPositionAndDimension) playerEntity.getCapability(PAST_POS_AND_DIM).orElseThrow(RuntimeException::new);
        if (playerEntity.getEntityWorld().getRegistryKey().getValue().equals(Bumblezone.MOD_DIMENSION_ID)) {
            if (playerEntity.getY() < -3) {
                playerEntity.setPos(playerEntity.getX(), -3.01D, playerEntity.getZ());
                playerEntity.setPosition(playerEntity.getX(), -3.01D, playerEntity.getZ());

                teleportOutOfBz(playerEntity);
            } else if (playerEntity.getY() > 255) {
                teleportOutOfBz(playerEntity);
            }
        }
        //teleport to bumblezone
        else if (cap.getTeleporting()) {
            BzPlayerPlacement.enteringBumblezone(playerEntity);
            cap.setTeleporting(false);
            reAddStatusEffect(playerEntity);
        }
    }

    private static void teleportOutOfBz(PlayerEntity playerEntity) {
        if (!playerEntity.getEntityWorld().isRemote) {
            checkAndCorrectStoredDimension(playerEntity);
            PlayerPositionAndDimension cap = (PlayerPositionAndDimension) playerEntity.getCapability(PAST_POS_AND_DIM).orElseThrow(RuntimeException::new);
            RegistryKey<World> world_key = RegistryKey.of(Registry.DIMENSION, cap.getNonBZDim());
            ServerWorld destination = playerEntity.getEntityWorld().getServer().getWorld(world_key);
            if(destination == null){
                destination = playerEntity.getEntityWorld().getServer().getWorld(World.OVERWORLD);
            }
            BzPlayerPlacement.exitingBumblezone(playerEntity, destination);
            reAddStatusEffect(playerEntity);
        }
    }

    /**
     * Temporary fix until Mojang patches the bug that makes potion effect icons disappear when changing dimension.
     * To fix it ourselves, we remove the effect and re-add it to the player.
     */
    private static void reAddStatusEffect(PlayerEntity playerEntity) {
        //re-adds potion effects so the icon remains instead of disappearing when changing dimensions due to a bug
        ArrayList<EffectInstance> effectInstanceList = new ArrayList<>(playerEntity.getActivePotionEffects());
        for (int i = effectInstanceList.size() - 1; i >= 0; i--) {
            EffectInstance effectInstance = effectInstanceList.get(i);
            if (effectInstance != null) {
                playerEntity.removePotionEffect(effectInstance.getPotion());
                playerEntity.addPotionEffect(
                        new EffectInstance(
                                effectInstance.getPotion(),
                                effectInstance.getDuration(),
                                effectInstance.getAmplifier(),
                                effectInstance.isAmbient(),
                                effectInstance.doesShowParticles(),
                                effectInstance.isShowIcon()));
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
            cap.setNonBZDim(World.OVERWORLD.getValue());
        }
    }


    // Enderpearl
    public static boolean runEnderpearlImpact(RayTraceResult hitResult, EnderPearlEntity pearlEntity){
        World world = pearlEntity.world; // world we threw in

        //Make sure we are on server by checking if thrower is ServerPlayerEntity
        if (!world.isRemote && pearlEntity.getOwner() instanceof ServerPlayerEntity) {
            ServerPlayerEntity playerEntity = (ServerPlayerEntity) pearlEntity.getOwner(); // the thrower
            Vector3d hitBlockPos = hitResult.getHitVec(); //position of the collision
            BlockPos hivePos = new BlockPos(0,0,0);
            boolean hitHive = false;

            //check with offset in all direction as the position of exact hit point could barely be outside the hive block
            //even through the pearl hit the block directly.
            for(double offset = -0.1D; offset <= 0.1D; offset += 0.2D) {
                BlockState block = world.getBlockState(new BlockPos(hitBlockPos.add(offset, 0, 0)));
                if(isValidBeeHive(block)) {
                    hitHive = true;
                    hivePos = new BlockPos(hitBlockPos.add(offset, 0, 0));
                    break;
                }

                block = world.getBlockState(new BlockPos(hitBlockPos.add(0, offset, 0)));
                if(isValidBeeHive(block)) {
                    hitHive = true;
                    hivePos = new BlockPos(hitBlockPos.add(0, offset, 0));
                    break;
                }

                block = world.getBlockState(new BlockPos(hitBlockPos.add(0, 0, offset)));
                if(isValidBeeHive(block)) {
                    hitHive = true;
                    hivePos = new BlockPos(hitBlockPos.add(0, 0, offset));
                    break;
                }
            }

            //checks if block under hive is correct if config needs one
            boolean validBelowBlock = false;
            if(!REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT_TAG.values().isEmpty()) {
                if(REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT_TAG.contains(world.getBlockState(hivePos.down()).getBlock())) {
                    validBelowBlock = true;
                }
                else if(Bumblezone.BzDimensionConfig.warnPlayersOfWrongBlockUnderHive.get()) {
                    //failed. Block below isn't the required block
                    Bumblezone.LOGGER.log(Level.INFO, "Bumblezone: the_bumblezone:required_blocks_under_hive_to_teleport tag does not have the block below the hive.");
                    ITextComponent message = new StringTextComponent("the_bumblezone:required_blocks_under_hive_to_teleport tag does not have the block below the hive.");
                    playerEntity.sendStatusMessage(message, true);
                    return false;
                }
            }
            else {
                validBelowBlock = true;
            }


            //if the pearl hit a beehive and is not in our bee dimension, begin the teleportation.
            if (hitHive && validBelowBlock && !playerEntity.getEntityWorld().getRegistryKey().getValue().equals(Bumblezone.MOD_DIMENSION_ID)) {
                PlayerPositionAndDimension cap = (PlayerPositionAndDimension) playerEntity.getCapability(PAST_POS_AND_DIM).orElseThrow(RuntimeException::new);
                cap.setTeleporting(true);
                pearlEntity.remove(false);
                return true;
            }
        }
        return false;
    }


    private static boolean isValidBeeHive(BlockState block) {
        if(BLACKLISTED_TELEPORTATION_HIVES_TAG.contains(block.getBlock())) return false;

        if(BlockTags.BEEHIVES.contains(block.getBlock()) || block.getBlock() instanceof BeehiveBlock) {
            if(Bumblezone.BzDimensionConfig.allowTeleportationWithModdedBeehives.get() ||
                Registry.BLOCK.getKey(block.getBlock()).getNamespace().equals("minecraft")) {

                return true;
            }
        }

        if(Bumblezone.BzDimensionConfig.allowTeleportationWithModdedBeehives.get()) {
            if(ModChecker.productiveBeesPresent && ProductiveBeesRedirection.PBIsExpandedBeehiveBlock(block))
                return true;

            return ModChecker.resourcefulBeesPresent && ResourcefulBeesRedirection.RBIsApairyBlock(block);
        }

        return false;
    }
}
