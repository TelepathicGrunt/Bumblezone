package com.telepathicgrunt.the_bumblezone.world.features.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.List;

public class NbtOreConfiguration implements FeatureConfiguration {
    public static final Codec<NbtOreConfiguration> CODEC = RecordCodecBuilder.create(
        (instance) -> instance.group(
            Codec.list(TargetBlockState.CODEC).fieldOf("targets").forGetter((arg) -> arg.targetStates),
            Codec.intRange(0, 64).fieldOf("size").forGetter((arg) -> arg.size),
            Codec.floatRange(0.0F, 1.0F).fieldOf("discard_chance_on_air_exposure").forGetter((arg) -> arg.discardChanceOnAirExposure)
        ).apply(instance, NbtOreConfiguration::new));

    public final List<NbtOreConfiguration.TargetBlockState> targetStates;
    public final int size;
    public final float discardChanceOnAirExposure;

    public NbtOreConfiguration(List<NbtOreConfiguration.TargetBlockState> targetStates, int size, float discardChanceOnAirExposure) {
        this.size = size;
        this.targetStates = targetStates;
        this.discardChanceOnAirExposure = discardChanceOnAirExposure;
    }

    public static class TargetBlockState {
        public static final Codec<NbtOreConfiguration.TargetBlockState> CODEC = RecordCodecBuilder.create(
            (instance) -> instance.group(
                RuleTest.CODEC.fieldOf("target").forGetter((arg) -> arg.target),
                BlockState.CODEC.fieldOf("state").forGetter((arg) -> arg.state),
                CompoundTag.CODEC.fieldOf("state_nbt").forGetter((arg) -> arg.stateNbt)
            ).apply(instance, TargetBlockState::new));

        public final RuleTest target;
        public final BlockState state;
        public final CompoundTag stateNbt;

        TargetBlockState(RuleTest ruleTest, BlockState blockState, CompoundTag stateNbt) {
            this.target = ruleTest;
            this.state = blockState;
            this.stateNbt = stateNbt;
        }
    }
}
