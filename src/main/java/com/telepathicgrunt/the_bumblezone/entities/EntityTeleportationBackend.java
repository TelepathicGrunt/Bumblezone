package com.telepathicgrunt.the_bumblezone.entities;

import com.google.common.primitives.Doubles;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzConfig;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.BzPlacingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class EntityTeleportationBackend {
    
    private static final int SEARCH_RADIUS = 48;

    public static Vec3 destPostFromOutOfBoundsTeleport(Entity entity, ServerLevel destination, boolean checkingUpward) {
        //converts the position to get the corresponding position in non-bumblezone dimension
        Entity player = entity.getPassengers().stream().filter(e -> e instanceof Player).findFirst().orElse(null);
        if(player != null) entity = player;
        double coordinateScale = entity.level.dimensionType().coordinateScale() / destination.dimensionType().coordinateScale();
        BlockPos finalSpawnPos;

        if (BzConfig.forceBumblezoneOriginMobToOverworldCenter &&
            Bumblezone.ENTITY_COMPONENT.get(entity).getNonBZPos() == null)
        {
            destination.getChunk(BlockPos.ZERO);
            int heightMapY = destination.getHeight(Heightmap.Types.MOTION_BLOCKING, 0, 0);
            if (heightMapY > destination.getMinBuildHeight() && heightMapY < destination.getMaxBuildHeight()) {
                return new Vec3(0, heightMapY + 0.5d, 0);
            }
            else {
                return new Vec3(0, (destination.getMinBuildHeight() + destination.getMaxBuildHeight()) / 2f, 0);
            }
        }

        Vec3 entitySavedPastPos = null;

        if (capOptional.isPresent()) {
            entitySavedPastPos = Bumblezone.ENTITY_COMPONENT.get(entity).getNonBZPos();
        }

        BlockPos finalSpawnPos = entity.blockPosition();
        if(entitySavedPastPos != null) {
            finalSpawnPos = new BlockPos(entitySavedPastPos);
        }

        //use found location
        //teleportation spot finding complete. return spot
        return new Vec3(
                finalSpawnPos.getX() + 0.5D,
                finalSpawnPos.getY() + 1,
                finalSpawnPos.getZ() + 0.5D
        );
    }

    public static Vec3 getBzCoordinate(Entity entity, ServerLevel originalWorld, ServerLevel bumblezoneWorld) {
        //converts the position to get the corresponding position in bumblezone dimension
        double coordinateScale = originalWorld.dimensionType().coordinateScale() / bumblezoneWorld.dimensionType().coordinateScale();

        BlockPos blockpos = new BlockPos(
                Doubles.constrainToRange(entity.position().x() * coordinateScale, -29999936D, 29999936D),
                Doubles.constrainToRange(entity.position().y(), 45, 200),
                Doubles.constrainToRange(entity.position().z() * coordinateScale, -29999936D, 29999936D));


        //gets valid space in other world
        BlockPos validBlockPos = validPlayerSpawnLocation(bumblezoneWorld, blockpos, 10);


        //No valid space found around destination. Begin secondary valid spot algorithms
        if (validBlockPos == null) {
            //go down to first solid land with air above.
            validBlockPos = new BlockPos(
                    blockpos.getX(),
                    BzPlacingUtils.topOfSurfaceBelowHeightThroughWater(bumblezoneWorld, blockpos.getY(), 0, blockpos) + 1,
                    blockpos.getZ());

            //No solid land was found. Who digs out an entire chunk?!
            if (validBlockPos.getY() == 0) {
                validBlockPos = blockpos;
            }
            //checks if spot is not two water blocks with air block able to be reached above
            else if (bumblezoneWorld.getBlockState(validBlockPos).getMaterial() == Material.WATER &&
                    bumblezoneWorld.getBlockState(validBlockPos.above()).getMaterial() == Material.WATER) {
                BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(validBlockPos.getX(), validBlockPos.getY(), validBlockPos.getZ());

                //moves upward looking for air block while not interrupted by a solid block
                while (mutable.getY() < 255 && !bumblezoneWorld.isEmptyBlock(mutable) || bumblezoneWorld.getBlockState(mutable).getMaterial() == Material.WATER) {
                    mutable.move(Direction.UP);
                }
                if (bumblezoneWorld.getBlockState(mutable).getMaterial() != Material.AIR) {
                    validBlockPos = blockpos; // No air found. Let's not place player here where they could drown
                } else {
                    validBlockPos = mutable; // Set player to top of water level
                }
            }
            //checks if spot is not a non-solid block with air block above
            else if ((!bumblezoneWorld.isEmptyBlock(validBlockPos) && bumblezoneWorld.getBlockState(validBlockPos).getMaterial() != Material.WATER) &&
                    bumblezoneWorld.getBlockState(validBlockPos.above()).getMaterial() != Material.AIR) {
                validBlockPos = blockpos;
            }
        }

        // place hive block below player if they would've fallen out of dimension
        // because there's air all the way down to y = 0 below player
        int heightCheck = 0;
        while(heightCheck <= validBlockPos.getY() && bumblezoneWorld.getBlockState(validBlockPos.below(heightCheck)).isAir()) {
            heightCheck++;
        }
        if(heightCheck >= validBlockPos.getY()) {
            bumblezoneWorld.setBlockAndUpdate(validBlockPos.getY() == 0 ? validBlockPos : validBlockPos.below(), Blocks.HONEYCOMB_BLOCK.defaultBlockState());
        }

        // teleportation spot finding complete. return spot
        return new Vec3(
                validBlockPos.getX() + 0.5D,
                validBlockPos.getY(),
                validBlockPos.getZ() + 0.5D
        );
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Util

    private static BlockPos validPlayerSpawnLocation(Level world, BlockPos position, int maximumRange) {
        //Try to find 2 non-solid spaces around it that the player can spawn at
        int radius;
        int outerRadius;
        int distanceSq;
        BlockPos.MutableBlockPos currentPos = new BlockPos.MutableBlockPos(position.getX(), position.getY(), position.getZ());

        //checks for 2 non-solid blocks with solid block below feet
        //checks outward from center position in both x, y, and z.
        //The x2, y2, and z2 is so it checks at center of the range box instead of the corner.
        for (int range = 0; range < maximumRange; range++) {
            radius = range * range;
            outerRadius = (range + 1) * (range + 1);

            for (int y = 0; y <= range * 2; y++) {
                int y2 = y > range ? -(y - range) : y;


                for (int x = 0; x <= range * 2; x++) {
                    int x2 = x > range ? -(x - range) : x;


                    for (int z = 0; z <= range * 2; z++) {
                        int z2 = z > range ? -(z - range) : z;

                        distanceSq = x2 * x2 + z2 * z2 + y2 * y2;
                        if (distanceSq >= radius && distanceSq < outerRadius) {
                            currentPos.set(position.offset(x2, y2, z2));
                            if (world.getBlockState(currentPos.below()).canOcclude() &&
                                world.getBlockState(currentPos).isAir() &&
                                world.getBlockState(currentPos.above()).isAir())
                            {
                                //valid space for player is found
                                return currentPos;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    public static boolean isValidBeeHive(BlockState block) {
        if(block.is(BzTags.FORCED_ALLOWED_TELEPORTABLE_BLOCK)) return true;

        if(block.is(BzTags.DISALLOWED_TELEPORTABLE_BEEHIVE)) return false;

        if(block.is(BlockTags.BEEHIVES) || block.getBlock() instanceof BeehiveBlock) {
            return true;
        }

        return false;
    }

    // Player exiting Bumblezone dimension
    public static void entityChangingDimension(ResourceLocation dimensionEntering, Entity entity) {
        //Updates the non-BZ dimension that the player is leaving
        if (dimensionEntering.equals(Bumblezone.MOD_DIMENSION_ID) && entity instanceof LivingEntity) {
            Bumblezone.ENTITY_COMPONENT.get(entity).setNonBZDimension(entity.level.dimension().location());
            Bumblezone.ENTITY_COMPONENT.get(entity).setNonBZPos(entity.position());
        }
    }
}
