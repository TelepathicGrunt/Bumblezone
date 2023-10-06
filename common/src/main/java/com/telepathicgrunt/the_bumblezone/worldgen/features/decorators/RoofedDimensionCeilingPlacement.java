package com.telepathicgrunt.the_bumblezone.worldgen.features.decorators;

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

public class RoofedDimensionCeilingPlacement extends PlacementModifier {

    private final int maxHeight;

    public static final Codec<RoofedDimensionCeilingPlacement> CODEC = RecordCodecBuilder.create((configInstance) -> configInstance.group(
            Codec.INT.fieldOf("max_height").orElse(0).forGetter(nbtFeatureConfig -> nbtFeatureConfig.maxHeight)
    ).apply(configInstance, RoofedDimensionCeilingPlacement::new));

    private RoofedDimensionCeilingPlacement(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    public PlacementModifierType<?> type() {
        return BzPlacements.ROOFED_DIMENSION_CEILING_PLACEMENT.get();
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext, RandomSource random, BlockPos blockPos) {

        int minY = placementContext.generator().getMinY();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().set(blockPos.getX(), this.maxHeight, blockPos.getZ());
        while (mutable.getY() > minY) {
            BlockState state = placementContext.getBlockState(mutable);
            if (state.isAir()) {
                return Stream.of(mutable.immutable());
            }

            mutable.move(Direction.DOWN);
        }

        return Stream.of();
    }
}
