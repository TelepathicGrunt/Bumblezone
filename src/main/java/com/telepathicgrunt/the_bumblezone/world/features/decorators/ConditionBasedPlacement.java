package com.telepathicgrunt.the_bumblezone.world.features.decorators;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.telepathicgrunt.the_bumblezone.modinit.BzPlacements;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.placement.RepeatingPlacement;
import net.minecraftforge.common.util.Lazy;

public class ConditionBasedPlacement extends RepeatingPlacement {

    public static final Codec<ConditionBasedPlacement> CODEC = Codec.BOOL.fieldOf("condition")
            .flatXmap(
                    (bool) -> DataResult.success(new ConditionBasedPlacement(bool)),
                    (placement) ->  DataResult.success(placement.condition.get())
            ).codec();

    private final Lazy<Boolean> condition;

    private ConditionBasedPlacement(boolean condition) {
        this.condition = Lazy.of(() -> condition);
    }

    private ConditionBasedPlacement(Lazy<Boolean> condition) {
        this.condition = condition;
    }

    public static ConditionBasedPlacement of(Lazy<Boolean> condition) {
        return new ConditionBasedPlacement(condition);
    }

    @Override
    public PlacementModifierType<?> type() {
        return BzPlacements.CONDITION_BASED_PLACEMENT.get();
    }

    @Override
    protected int count(RandomSource random, BlockPos blockPos) {
        return condition.get() ? 1 : 0;
    }
}
