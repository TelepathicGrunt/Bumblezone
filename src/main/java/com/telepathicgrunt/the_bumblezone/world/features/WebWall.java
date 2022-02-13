package com.telepathicgrunt.the_bumblezone.world.features;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyWeb;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFeatures;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class WebWall extends Feature<NoneFeatureConfiguration> {

    public static final Map<Direction.Axis, Set<Direction>> AXIS_TO_FACINGS = ImmutableMap.copyOf(Util.make(Maps.newEnumMap(Direction.Axis.class), (map) -> {
        map.put(Direction.Axis.X, ImmutableSet.of(Direction.NORTH, Direction.SOUTH, Direction.UP, Direction.DOWN));
        map.put(Direction.Axis.Z, ImmutableSet.of(Direction.EAST, Direction.WEST, Direction.UP, Direction.DOWN));
        map.put(Direction.Axis.Y, ImmutableSet.of(Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH));
    }));

    public WebWall(Codec<NoneFeatureConfiguration> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos blockPos = new ChunkPos(context.origin()).getMiddleBlockPosition(context.origin().getY());
        WorldGenLevel level = context.level();

        if(level.getBlockState(blockPos).is(Blocks.CAVE_AIR)) {
            for(Direction.Axis axis : Direction.Axis.values()) {
                Set<BlockPos> validSpaces = new HashSet<>();
                validSpaces.add(blockPos);
                boolean isInBounds = setIfValidSpace(level, axis, blockPos, blockPos, validSpaces);
                if(isInBounds) {
                    for(BlockPos validPos : validSpaces) {
                        for(int attempt = 0; attempt < 12; attempt++) {
                            BzFeatures.STICKY_HONEY_RESIDUE_FEATURE.get().place(new FeaturePlaceContext<>(
                                context.topFeature(),
                                context.level(),
                                context.chunkGenerator(),
                                context.random(),
                                validPos.offset(
                                        context.random().nextInt(9) - 4,
                                        context.random().nextInt(9) - 4,
                                        context.random().nextInt(9) - 4),
                                context.config()
                            ));
                        }

                        BlockState state = level.getBlockState(validPos);
                        if (state.is(BzBlocks.HONEY_WEB.get())) {
                            level.setBlock(validPos, state.setValue(HoneyWeb.AXIS_TO_PROP.get(axis), true), 3);
                        }
                        else {
                            level.setBlock(validPos, BzBlocks.HONEY_WEB.get().defaultBlockState().setValue(HoneyWeb.AXIS_TO_PROP.get(axis), true), 3);
                        }
                    }

                    return true;
                }
            }
        }
        return false;
    }

    private boolean setIfValidSpace(WorldGenLevel level, Direction.Axis axis, BlockPos originPos, BlockPos currentPos, Set<BlockPos> validSpaces) {
        int maxDistance = 14;
        for(Direction direction : AXIS_TO_FACINGS.get(axis)) {
            BlockPos newBlockPos = currentPos.relative(direction);

            if(Math.abs(newBlockPos.getX() - originPos.getX()) > maxDistance ||
                Math.abs(newBlockPos.getY() - originPos.getY()) > maxDistance ||
                Math.abs(newBlockPos.getZ() - originPos.getZ()) > maxDistance)
            {
                return false;
            }

            if(!validSpaces.contains(newBlockPos)) {
                BlockState state = level.getBlockState(newBlockPos);
                if(state.isAir() || state.is(BzBlocks.PILE_OF_POLLEN.get()) || state.is(BzBlocks.HONEY_WEB.get())) {
                    validSpaces.add(newBlockPos);
                    if(!setIfValidSpace(level, axis, originPos, newBlockPos, validSpaces)) {
                        return false;
                    }
                }
                else if (state.is(BzBlocks.HONEY_WEB.get())) {
                    validSpaces.add(newBlockPos);
                }
            }
        }

        return true;
    }
}