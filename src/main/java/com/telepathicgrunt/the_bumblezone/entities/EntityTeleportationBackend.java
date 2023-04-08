package com.telepathicgrunt.the_bumblezone.entities;

import com.google.common.primitives.Doubles;
import com.telepathicgrunt.the_bumblezone.capabilities.BzCapabilities;
import com.telepathicgrunt.the_bumblezone.capabilities.EntityPositionAndDimension;
import com.telepathicgrunt.the_bumblezone.configs.BzDimensionConfigs;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ProductiveBeesCompat;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.BzPlacingUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityTeleportationBackend {

    public static Vec3 destPostFromOutOfBoundsTeleport(Entity entity, ServerLevel destination) {
        //converts the position to get the corresponding position in non-bumblezone dimension
        Entity player = entity.getPassengers().stream().filter(e -> e instanceof Player).findFirst().orElse(null);
        if(player != null) entity = player;

        LazyOptional<EntityPositionAndDimension> capOptional = entity.getCapability(BzCapabilities.ENTITY_POS_AND_DIM_CAPABILITY);

        if (BzDimensionConfigs.forceBumblezoneOriginMobToOverworldCenter.get() &&
            capOptional.isPresent() &&
            capOptional.orElseThrow(RuntimeException::new).getNonBZPos() == null)
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
            EntityPositionAndDimension capability = capOptional.orElseThrow(RuntimeException::new);
            entitySavedPastPos = capability.getNonBZPos();
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
        BlockPos validBlockPos = validPlayerSpawnLocation(bumblezoneWorld, blockpos, 32);


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
                }
                else {
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
        ChunkAccess chunk = null;

        //checks for 2 non-solid blocks with solid block below feet
        //checks outward from center position in both x, y, and z.
        //The x2, y2, and z2 is so it checks at center of the range box instead of the corner.
        for (int range = 0; range <= maximumRange; range++) {
            radius = range * range;
            outerRadius = (range + 1) * (range + 1);

            for (int y = -range; y <= range; y += 4) {
                for (int x = -range; x <= range; x += 4) {
                    for (int z = -range; z <= range; z += 4) {

                        distanceSq = x * x + z * z + y * y;
                        if (distanceSq >= radius && distanceSq < outerRadius) {

                            currentPos.set(position.offset(x, y, z));
                            if (currentPos.getY() > 250 || currentPos.getY() < 45) {
                                continue;
                            }

                            chunk = getChunkForSpot(world, chunk, currentPos);
                            boolean isCurrentPosAir = chunk.getBlockState(currentPos).isAir();
                            if (!isCurrentPosAir) {
                                continue;
                            }

                            boolean isBelowSolid = chunk.getBlockState(currentPos.below()).canOcclude();
                            if (isBelowSolid) {
                                if (chunk.getBlockState(currentPos.above(2)).isAir()) {
                                    //valid space for player is found
                                    return currentPos;
                                }
                                else {
                                    continue;
                                }
                            }

                            while (!isBelowSolid && currentPos.getY() >= 45) {
                                currentPos.move(Direction.DOWN);
                                BlockState belowState = chunk.getBlockState(currentPos.below());
                                isBelowSolid = belowState.canOcclude() || !belowState.getFluidState().isEmpty();
                            }

                            if (isBelowSolid) {
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

    private static ChunkAccess getChunkForSpot(Level world, ChunkAccess chunkAccess, BlockPos blockPos) {
        if (chunkAccess == null || chunkAccess.getPos().x != blockPos.getX() >> 4 || chunkAccess.getPos().z != blockPos.getZ() >> 4) {
            return world.getChunk(blockPos);
        }
        return chunkAccess;
    }

    public static boolean isValidBeeHive(BlockState blockState) {
        if(blockState.is(BzTags.FORCED_ALLOWED_TELEPORTABLE_BLOCK)) return true;

        if(blockState.is(BzTags.DISALLOWED_TELEPORTABLE_BEEHIVE)) return false;

        if(blockState.is(BlockTags.BEEHIVES) || blockState.getBlock() instanceof BeehiveBlock) {
            if(ForgeRegistries.BLOCKS.getKey(blockState.getBlock()).getNamespace().equals("minecraft") || BzDimensionConfigs.allowTeleportationWithModdedBeehives.get()) {
                return true;
            }
        }

        if(BzDimensionConfigs.allowTeleportationWithModdedBeehives.get()) {
            if(ModChecker.productiveBeesPresent && ProductiveBeesCompat.PBIsExpandedBeehiveBlock(blockState)) {
                return true;
            }
        }

        return false;
    }
}
