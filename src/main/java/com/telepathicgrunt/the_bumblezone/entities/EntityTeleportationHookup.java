package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.capabilities.EntityPositionAndDimension;
import com.telepathicgrunt.the_bumblezone.capabilities.IEntityPosAndDim;
import com.telepathicgrunt.the_bumblezone.tags.BzBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Direction;
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
import net.minecraftforge.event.entity.living.LivingEvent;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;

public class EntityTeleportationHookup {

    @CapabilityInject(IEntityPosAndDim.class)
    public static Capability<IEntityPosAndDim> PAST_POS_AND_DIM = null;

    ////////////////////////////////////////////////////////////
    // Methods that setup and call PlayerTeleportationBackend //

    //Entity ticks
    public static void entityTick(LivingEvent.LivingUpdateEvent event){
        //Bumblezone.LOGGER.log(Level.INFO, "started");
        //grabs the capability attached to player for dimension hopping
        LivingEntity livingEntity = event.getEntityLiving();

        //Makes it so player does not get killed for falling into the void
        if (livingEntity.getCommandSenderWorld().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID)) {
            if (livingEntity.getY() < -3) {
                livingEntity.setPosAndOldPos(livingEntity.getX(), -3.01D, livingEntity.getZ());
                livingEntity.moveTo(livingEntity.getX(), -3.01D, livingEntity.getZ());
                livingEntity.fallDistance = 0;

                if(!livingEntity.level.isClientSide()){
                    LazyOptional<IEntityPosAndDim> lazyOptionalCap = livingEntity.getCapability(PAST_POS_AND_DIM);
                    if (lazyOptionalCap.isPresent()) {
                        EntityPositionAndDimension cap = (EntityPositionAndDimension) lazyOptionalCap.orElseThrow(RuntimeException::new);
                        cap.setTeleporting(false);
                    }

                    teleportOutOfBz(livingEntity);
                }
            }
            else if (livingEntity.getY() > 255) {
                livingEntity.setPosAndOldPos(livingEntity.getX(), 255.01D, livingEntity.getZ());
                livingEntity.moveTo(livingEntity.getX(), 255.01D, livingEntity.getZ());

                if(!livingEntity.level.isClientSide()){
                    LazyOptional<IEntityPosAndDim> lazyOptionalCap = livingEntity.getCapability(PAST_POS_AND_DIM);
                    if (lazyOptionalCap.isPresent()) {
                        EntityPositionAndDimension cap = (EntityPositionAndDimension) lazyOptionalCap.orElseThrow(RuntimeException::new);
                        cap.setTeleporting(false);
                    }

                    teleportOutOfBz(livingEntity);
                }
            }
        }
        //teleport to bumblezone
        else if(!livingEntity.level.isClientSide()){
            LazyOptional<IEntityPosAndDim> lazyOptionalCap = livingEntity.getCapability(PAST_POS_AND_DIM);
            if (lazyOptionalCap.isPresent()) {
                EntityPositionAndDimension cap = (EntityPositionAndDimension) lazyOptionalCap.orElseThrow(RuntimeException::new);
                if (cap.getTeleporting()) {
                    cap.setTeleporting(false);
                    EntityTeleportationBackend.enteringBumblezone(livingEntity);
                    reAddStatusEffect(livingEntity);
                }
            }
        }
    }

    private static void teleportOutOfBz(LivingEntity livingEntity) {
        if (!livingEntity.getCommandSenderWorld().isClientSide()) {
            checkAndCorrectStoredDimension(livingEntity);
            EntityPositionAndDimension cap = (EntityPositionAndDimension) livingEntity.getCapability(PAST_POS_AND_DIM).orElseThrow(RuntimeException::new);
            RegistryKey<World> world_key = RegistryKey.create(Registry.DIMENSION_REGISTRY, cap.getNonBZDim());
            ServerWorld destination = livingEntity.getCommandSenderWorld().getServer().getLevel(world_key);
            if(destination == null){
                destination = livingEntity.getCommandSenderWorld().getServer().getLevel(World.OVERWORLD);
            }
            EntityTeleportationBackend.exitingBumblezone(livingEntity, destination);
            reAddStatusEffect(livingEntity);
        }
    }


    // Enderpearl
    public static boolean runEnderpearlImpact(Vector3d hitBlockPos, Entity thrower){
        World world = thrower.level; // world we threw in

        // Make sure we are on server by checking if thrower is ServerPlayerEntity and that we are not in bumblezone.
        // If onlyOverworldHivesTeleports is set to true, then only run this code in Overworld.
        if (!world.isClientSide() && thrower instanceof ServerPlayerEntity &&
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
                        if(EntityTeleportationBackend.isValidBeeHive(block)) {
                            hitHive = true;
                            hivePos = offsettedHitPos;

                            // break out of all 3 loops
                            offsetX = 2;
                            offsetY = 2;
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
                EntityPositionAndDimension cap = (EntityPositionAndDimension) playerEntity.getCapability(PAST_POS_AND_DIM).orElseThrow(RuntimeException::new);
                cap.setTeleporting(true);
                return true;
            }
        }
        return false;
    }


    // PistonPushed
    public static void runPistonPushed(Direction direction, LivingEntity pushedEntity){
        ServerWorld world = (ServerWorld) pushedEntity.level;

        // If onlyOverworldHivesTeleports is set to true, then only run this code in Overworld.
        if (!world.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) &&
                (!Bumblezone.BzDimensionConfig.onlyOverworldHivesTeleports.get() || world.dimension().equals(World.OVERWORLD)))
        {
            EntityPositionAndDimension cap = (EntityPositionAndDimension) pushedEntity.getCapability(PAST_POS_AND_DIM).orElseThrow(RuntimeException::new);
            if(cap.getTeleporting()) return; // Skip checks if entity is teleporting already to Bz.

            BlockPos hivePos = new BlockPos(0,0,0);
            BlockPos.Mutable entityPos = new BlockPos.Mutable().set(pushedEntity.blockPosition());

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
                    else if(Bumblezone.BzDimensionConfig.warnPlayersOfWrongBlockUnderHive.get()) {
                        if(pushedEntity instanceof PlayerEntity){
                            //failed. Block below isn't the required block
                            Bumblezone.LOGGER.log(Level.INFO, "Bumblezone: the_bumblezone:required_blocks_under_hive_to_teleport tag does not have the block below the hive.");
                            ITextComponent message = new StringTextComponent("the_bumblezone:required_blocks_under_hive_to_teleport tag does not have the block below the hive.");
                            ((PlayerEntity)pushedEntity).displayClientMessage(message, true);
                        }
                        return;
                    }
                }
                else {
                    validBelowBlock = true;
                }

                //if the entity was pushed into a beehive, begin the teleportation.
                if (validBelowBlock) {
                    cap.setTeleporting(true);
                }
            }
        }
    }

    ///////////
    // Utils //

    /**
     * Temporary fix until Mojang patches the bug that makes potion effect icons disappear when changing dimension.
     * To fix it ourselves, we remove the effect and re-add it to the player.
     */
    private static void reAddStatusEffect(LivingEntity livingEntity) {
        //re-adds potion effects so the icon remains instead of disappearing when changing dimensions due to a bug
        ArrayList<EffectInstance> effectInstanceList = new ArrayList<>(livingEntity.getActiveEffects());
        for (int i = effectInstanceList.size() - 1; i >= 0; i--) {
            EffectInstance effectInstance = effectInstanceList.get(i);
            if (effectInstance != null) {
                livingEntity.removeEffect(effectInstance.getEffect());
                livingEntity.addEffect(
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
    private static void checkAndCorrectStoredDimension(LivingEntity livingEntity) {
        //Error. This shouldn't be. We aren't leaving the bumblezone to go to the bumblezone.
        //Go to Overworld instead as default. Or go to Overworld if config is set.
        EntityPositionAndDimension cap = (EntityPositionAndDimension) livingEntity.getCapability(PAST_POS_AND_DIM).orElseThrow(RuntimeException::new);
        if (cap.getNonBZDim().equals(Bumblezone.MOD_DIMENSION_ID) ||
                Bumblezone.BzDimensionConfig.forceExitToOverworld.get())
        {
            // go to overworld by default
            //update stored dimension
            cap.setNonBZDim(World.OVERWORLD.location());
        }
    }
}
