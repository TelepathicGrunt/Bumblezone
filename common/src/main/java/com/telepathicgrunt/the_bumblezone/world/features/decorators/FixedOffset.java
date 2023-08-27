package com.telepathicgrunt.the_bumblezone.world.features.decorators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.mixin.world.WorldGenRegionAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzPlacements;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import java.util.ArrayList;
import java.util.List;
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
