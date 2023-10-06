package com.telepathicgrunt.the_bumblezone.worldgen.features.decorators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzPlacements;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.stream.Stream;

public class FixedOffset extends PlacementModifier {
    private final BlockPos offset;

    public static final Codec<FixedOffset> CODEC = RecordCodecBuilder.create((configInstance) -> configInstance.group(
            BlockPos.CODEC.fieldOf("offset").forGetter(fixedOffset -> fixedOffset.offset)
    ).apply(configInstance, FixedOffset::new));

    private FixedOffset(BlockPos offset) {
        this.offset = offset;
    }

    @Override
    public PlacementModifierType<?> type() {
        return BzPlacements.FIXED_OFFSET.get();
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext, RandomSource random, BlockPos blockPos) {
        return Stream.of(blockPos.offset(this.offset));
    }
}
