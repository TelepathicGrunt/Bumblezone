package com.telepathicgrunt.bumblezone.entities;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.tags.BZBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;

public class EntityTeleportationHookup {

    ////////////////////////////////////////////////////////////
    // Methods that setup and call PlayerTeleportationBackend //

    //Living Entity ticks
    public static void entityTick(LivingEntity livingEntity){
        //Makes it so player does not get killed for falling into the void
        if (livingEntity.getEntityWorld().getRegistryKey().getValue().equals(Bumblezone.MOD_DIMENSION_ID)) {
            if (livingEntity.getY() < -3) {
                livingEntity.refreshPositionAfterTeleport(livingEntity.getX(), -3.01D, livingEntity.getZ());
                livingEntity.updatePosition(livingEntity.getX(), -3.01D, livingEntity.getZ());
                livingEntity.fallDistance = 0;

                if(!livingEntity.world.isClient){
                    Bumblezone.ENTITY_COMPONENT.get(livingEntity).setIsTeleporting(false);
                    teleportOutOfBz(livingEntity);
                }
            } else if (livingEntity.getY() > 255) {
                livingEntity.refreshPositionAfterTeleport(livingEntity.getX(), 255.01D, livingEntity.getZ());
                livingEntity.updatePosition(livingEntity.getX(), 255.01D, livingEntity.getZ());

                if(!livingEntity.world.isClient){
                    Bumblezone.ENTITY_COMPONENT.get(livingEntity).setIsTeleporting(false);
                    teleportOutOfBz(livingEntity);
                }
            }
        }
        //teleport to bumblezone
        else if (!livingEntity.world.isClient && Bumblezone.ENTITY_COMPONENT.get(livingEntity).getIsTeleporting()) {
            Bumblezone.ENTITY_COMPONENT.get(livingEntity).setIsTeleporting(false);
            EntityTeleportationBackend.enteringBumblezone(livingEntity);
            reAddStatusEffect(livingEntity);
        }
    }


    public static void teleportOutOfBz(LivingEntity livingEntity) {
        if (!livingEntity.world.isClient) {
            checkAndCorrectStoredDimension(livingEntity);
            MinecraftServer minecraftServer = livingEntity.getServer(); // the server itself
            RegistryKey<World> world_key = RegistryKey.of(Registry.WORLD_KEY, Bumblezone.ENTITY_COMPONENT.get(livingEntity).getNonBZDimension());
            ServerWorld serverWorld = minecraftServer.getWorld(world_key);
            if(serverWorld == null){
                serverWorld = minecraftServer.getWorld(World.OVERWORLD);
            }
            EntityTeleportationBackend.exitingBumblezone(livingEntity, serverWorld);
            reAddStatusEffect(livingEntity);
        }
    }

    // Enderpearl
    public static boolean runEnderpearlImpact(HitResult hitResult, ProjectileEntity pearlEntity){
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
            if(!BZBlockTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT.values().isEmpty()) {
                if(BZBlockTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT.contains(world.getBlockState(hivePos.down()).getBlock())) {
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
                Bumblezone.ENTITY_COMPONENT.get(playerEntity).setIsTeleporting(true);
                pearlEntity.discard();
                return true;
            }
        }
        return false;
    }


    public static void runPistonPushed(Direction direction, LivingEntity livingEntity) {
        ServerWorld world = (ServerWorld) livingEntity.world;

        // If onlyOverworldHivesTeleports is set to true, then only run this code in Overworld.
        if (!world.getRegistryKey().getValue().equals(Bumblezone.MOD_DIMENSION_ID) &&
                (!Bumblezone.BZ_CONFIG.BZDimensionConfig.onlyOverworldHivesTeleports || world.getRegistryKey().equals(World.OVERWORLD)))
        {
            // Skip checks if entity is teleporting already to Bz.
            if(Bumblezone.ENTITY_COMPONENT.get(livingEntity).getIsTeleporting()) return;

            BlockPos hivePos = new BlockPos(0,0,0);
            BlockPos.Mutable entityPos = new BlockPos.Mutable().set(livingEntity.getBlockPos());

            // Checks if entity is pushed into hive block (the mutable is moved for each check and enters early if any is true)
            if (EntityTeleportationBackend.isValidBeeHive(world.getBlockState(entityPos)) ||
                    EntityTeleportationBackend.isValidBeeHive(world.getBlockState(entityPos.move(Direction.UP))) ||
                    EntityTeleportationBackend.isValidBeeHive(world.getBlockState(entityPos.move(direction))) ||
                    EntityTeleportationBackend.isValidBeeHive(world.getBlockState(entityPos.move(Direction.DOWN))))
            {
                //checks if block under hive is correct if config needs one
                boolean validBelowBlock = false;
                if(!BZBlockTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT.values().isEmpty()) {
                    if(BZBlockTags.REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT.contains(world.getBlockState(hivePos.down()).getBlock())) {
                        validBelowBlock = true;
                    }
                    else if(Bumblezone.BZ_CONFIG.BZDimensionConfig.warnPlayersOfWrongBlockUnderHive) {
                        if(livingEntity instanceof PlayerEntity playerEntity){
                            //failed. Block below isn't the required block
                            Bumblezone.LOGGER.log(Level.INFO, "Bumblezone: the_bumblezone:required_blocks_under_hive_to_teleport tag does not have the block below the hive.");
                            Text message = new LiteralText("the_bumblezone:required_blocks_under_hive_to_teleport tag does not have the block below the hive.");
                            playerEntity.sendMessage(message, true);
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
        ArrayList<StatusEffectInstance> effectInstanceList = new ArrayList<StatusEffectInstance>(livingEntity.getStatusEffects());
        for (int i = effectInstanceList.size() - 1; i >= 0; i--) {
            StatusEffectInstance effectInstance = effectInstanceList.get(i);
            if (effectInstance != null) {
                livingEntity.removeStatusEffect(effectInstance.getEffectType());
                livingEntity.addStatusEffect(
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
    private static void checkAndCorrectStoredDimension(LivingEntity livingEntity) {
        //Error. This shouldn't be. We aren't leaving the bumblezone to go to the bumblezone.
        //Go to Overworld instead as default. Or go to Overworld if config is set.
        if (Bumblezone.ENTITY_COMPONENT.get(livingEntity).getNonBZDimension().equals(Bumblezone.MOD_DIMENSION_ID) ||
                Bumblezone.BZ_CONFIG.BZDimensionConfig.forceExitToOverworld)
        {
            // go to overworld by default
            //update stored dimension
            Bumblezone.ENTITY_COMPONENT.get(livingEntity).setNonBZDimension(World.OVERWORLD.getValue());
        }
    }
}
