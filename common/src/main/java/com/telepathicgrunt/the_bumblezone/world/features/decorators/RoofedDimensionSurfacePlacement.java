package com.telepathicgrunt.the_bumblezone.world.features.decorators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzPlacements;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.stream.Stream;

public class RoofedDimensionSurfacePlacement extends PlacementModifier {

    private final int minHeight;
    private final int maxWaterDepth;

    public static final Codec<RoofedDimensionSurfacePlacement> CODEC = RecordCodecBuilder.create((configInstance) -> configInstance.group(
            Codec.INT.fieldOf("min_height").orElse(0).forGetter(nbtFeatureConfig -> nbtFeatureConfig.minHeight),
            Codec.INT.fieldOf("max_water_depth").orElse(0).forGetter(nbtFeatureConfig -> nbtFeatureConfig.maxWaterDepth)
    ).apply(configInstance, RoofedDimensionSurfacePlacement::new));

    private RoofedDimensionSurfacePlacement(int minHeight, int maxWaterDepth) {
        this.minHeight = minHeight;
        this.maxWaterDepth = maxWaterDepth;
    }

    @Override
    public PlacementModifierType<?> type() {
        return BzPlacements.ROOFED_DIMENSION_SURFACE_PLACEMENT.get();
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext, RandomSource random, BlockPos blockPos) {

        int maxY = placementContext.generator().getGenDepth();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().set(blockPos.getX(), this.minHeight, blockPos.getZ());
        while (mutable.getY() < maxY) {
            BlockState state = placementContext.getBlockState(mutable);
            if (state.isAir()) {
                return Stream.of(mutable.immutable());
            }
            else if (!state.getFluidState().isEmpty()) {
                BlockPos currentPos = mutable.immutable();
                mutable.move(Direction.UP);
                int currentFluidDepth = 1;

                if (currentFluidDepth > this.maxWaterDepth) {
                    return Stream.of();
                }

                while (mutable.getY() < maxY) {
                    BlockState secondState = placementContext.getBlockState(mutable);
                    if (secondState.isAir()) {
                        return Stream.of(currentPos);
                    }
                    else if (secondState.getFluidState().isEmpty()) {
                        break;
                    }

                    mutable.move(Direction.UP);
                    currentFluidDepth++;

                    if (currentFluidDepth > this.maxWaterDepth) {
                        return Stream.of();
                    }
                }
            }

            mutable.move(Direction.UP);
        }

        return Stream.of();
    }
}
