package com.telepathicgrunt.the_bumblezone.utils;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;

public class LevellessWeightedStateProvider {
    public static final MapCodec<LevellessWeightedStateProvider> CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(WeightedList.nonEmptyCodec(BlockState.CODEC).fieldOf("entries").forGetter(o -> o.weightedList)
        ).apply(i, LevellessWeightedStateProvider::new)
    );
    private final WeightedList<BlockState> weightedList;

    public LevellessWeightedStateProvider(WeightedList<BlockState> weightedList) {
        if (weightedList.isEmpty()) {
            throw new IllegalArgumentException("Weighted list must have at least one entry");
        } else {
            this.weightedList = weightedList;
        }
    }

    public LevellessWeightedStateProvider(WeightedList.Builder<BlockState> weightedList) {
        this(weightedList.build());
    }

    protected BlockStateProviderType<?> type() {
        return BlockStateProviderType.WEIGHTED_STATE_PROVIDER;
    }

    public BlockState getState(RandomSource random, BlockPos pos) {
        return this.weightedList.getRandomOrThrow(random);
    }
}