package com.telepathicgrunt.bumblezone.entities;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.tags.BzBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class EntityTeleportationHookup {

    ////////////////////////////////////////////////////////////
    // Methods that setup and call PlayerTeleportationBackend //

    //Living Entity ticks
    public static void entityTick(LivingEntity livingEntity){
        //Makes it so player does not get killed for falling into the void
        if (livingEntity.getCommandSenderWorld().dimension().location().equals(Bumblezone.MOD_DIMENSION_ID)) {
            if (livingEntity.getY() < -3) {
                livingEntity.moveTo(livingEntity.getX(), -3.01D, livingEntity.getZ());
                livingEntity.absMoveTo(livingEntity.getX(), -3.01D, livingEntity.getZ());
                livingEntity.fallDistance = 0;

                if(!livingEntity.level.isClientSide()){
                    Bumblezone.ENTITY_COMPONENT.get(livingEntity).setIsTeleporting(false);
                    teleportOutOfBz(livingEntity);
                }
            } else if (livingEntity.getY() > 255) {
                livingEntity.moveTo(livingEntity.getX(), 255.01D, livingEntity.getZ());
                livingEntity.absMoveTo(livingEntity.getX(), 255.01D, livingEntity.getZ());

                if(!livingEntity.level.isClientSide()){
                    Bumblezone.ENTITY_COMPONENT.get(livingEntity).setIsTeleporting(false);
                    teleportOutOfBz(livingEntity);
                }
            }
        }
        //teleport to bumblezone
        else if (!livingEntity.level.isClientSide() && Bumblezone.ENTITY_COMPONENT.get(livingEntity).getIsTeleporting()) {
            Bumblezone.ENTITY_COMPONENT.get(livingEntity).setIsTeleporting(false);
            EntityTeleportationBackend.enteringBumblezone(livingEntity);
            reAddStatusEffect(livingEntity);
        }
    }


    public static void teleportOutOfBz(LivingEntity livingEntity) {
        if (!livingEntity.level.isClientSide()) {
            checkAndCorrectStoredDimension(livingEntity);
            MinecraftServer minecraftServer = livingEntity.getServer(); // the server itself
            ResourceKey<net.minecraft.world.level.Level> world_key = ResourceKey.create(Registry.DIMENSION_REGISTRY, Bumblezone.ENTITY_COMPONENT.get(livingEntity).getNonBZDimension());
            ServerLevel serverWorld = minecraftServer.getLevel(world_key);
            if(serverWorld == null){
                serverWorld = minecraftServer.getLevel(net.minecraft.world.level.Level.OVERWORLD);
            }
            EntityTeleportationBackend.exitingBumblezone(livingEntity, serverWorld);
            reAddStatusEffect(livingEntity);
        }
    }

    // Enderpearl
    public static boolean runEnderpearlImpact(HitResult hitResult, Projectile pearlEntity){
        net.minecraft.world.level.Level world = pearlEntity.level; // world we threw in

        // Make sure we are on server by checking if thrower is ServerPlayerEntity and that we are not in bumblezone.
        // If onlyOverworldHivesTeleports is set to true, then only run this code in Overworld.
        if (!world.isClientSide() && pearlEntity.getOwner() instanceof ServerPlayer &&
            !world.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) &&
            (!Bumblezone.BZ_CONFIG.BZDimensionConfig.onlyOverworldHivesTeleports || world.dimension().equals(Level.OVERWORLD)))
        {
            ServerPlayer playerEntity = (ServerPlayer) pearlEntity.getOwner(); // the thrower
            Vec3 hitBlockPos = hitResult.getLocation(); //position of the collision
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
                else if(Bumblezone.BZ_CONFIG.BZDimensionConfig.warnPlayersOfWrongBlockUnderHive)
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
                Bumblezone.ENTITY_COMPONENT.get(playerEntity).setIsTeleporting(true);
                pearlEntity.discard();
                return true;
            }
        }
        return false;
    }


    public static void runPistonPushed(Direction direction, LivingEntity livingEntity) {
        ServerLevel world = (ServerLevel) livingEntity.level;

        // If onlyOverworldHivesTeleports is set to true, then only run this code in Overworld.
        if (!world.dimension().location().equals(Bumblezone.MOD_DIMENSION_ID) &&
                (!Bumblezone.BZ_CONFIG.BZDimensionConfig.onlyOverworldHivesTeleports || world.dimension().equals(net.minecraft.world.level.Level.OVERWORLD)))
        {
            // Skip checks if entity is teleporting already to Bz.
            if(Bumblezone.ENTITY_COMPONENT.get(livingEntity).getIsTeleporting()) return;

            BlockPos hivePos = new BlockPos(0,0,0);
            BlockPos.MutableBlockPos entityPos = new BlockPos.MutableBlockPos().set(livingEntity.blockPosition());

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
                    else if(Bumblezone.BZ_CONFIG.BZDimensionConfig.warnPlayersOfWrongBlockUnderHive) {
                        if(livingEntity instanceof Player playerEntity){
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
                    Bumblezone.ENTITY_COMPONENT.get(livingEntity).setIsTeleporting(true);
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
        ArrayList<MobEffectInstance> effectInstanceList = new ArrayList<MobEffectInstance>(livingEntity.getActiveEffects());
        for (int i = effectInstanceList.size() - 1; i >= 0; i--) {
            MobEffectInstance effectInstance = effectInstanceList.get(i);
            if (effectInstance != null) {
                livingEntity.removeEffect(effectInstance.getEffect());
                livingEntity.addEffect(
                        new MobEffectInstance(
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
        if (Bumblezone.ENTITY_COMPONENT.get(livingEntity).getNonBZDimension().equals(Bumblezone.MOD_DIMENSION_ID) ||
                Bumblezone.BZ_CONFIG.BZDimensionConfig.forceExitToOverworld)
        {
            // go to overworld by default
            //update stored dimension
            Bumblezone.ENTITY_COMPONENT.get(livingEntity).setNonBZDimension(net.minecraft.world.level.Level.OVERWORLD.location());
        }
    }
}
