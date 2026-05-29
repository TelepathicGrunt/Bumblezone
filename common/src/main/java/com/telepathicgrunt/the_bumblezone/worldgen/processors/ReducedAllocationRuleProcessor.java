package com.telepathicgrunt.the_bumblezone.worldgen.processors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;

public class ReducedAllocationRuleProcessor extends StructureProcessor {

    public static final MapCodec<ReducedAllocationRuleProcessor> CODEC = ProcessorRule.CODEC.listOf().fieldOf("rules")
            .xmap(ReducedAllocationRuleProcessor::new, ruleProcessor -> ruleProcessor.rules);
    private final ImmutableList<ProcessorRule> rules;

    public ReducedAllocationRuleProcessor(List<? extends ProcessorRule> rules) {
        this.rules = ImmutableList.copyOf(rules);
    }

    // Micro-optimized to reduce object allocation
    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(
            LevelReader level,
            BlockPos offset,
            BlockPos pos,
            StructureTemplate.StructureBlockInfo blockInfo,
            StructureTemplate.StructureBlockInfo relativeBlockInfo,
            StructurePlaceSettings settings
    ) {
        RandomSource randomsource = settings.getRandom(relativeBlockInfo.pos());
        BlockState blockstate = level.getBlockState(relativeBlockInfo.pos());

        for (int i = 0; i < this.rules.size(); i++) {
            ProcessorRule processorrule = this.rules.get(i);
            if (processorrule.test(relativeBlockInfo.state(), blockstate, blockInfo.pos(), relativeBlockInfo.pos(), pos, randomsource)) {
                return new StructureTemplate.StructureBlockInfo(
                        relativeBlockInfo.pos(), processorrule.getOutputState(), processorrule.getOutputTag(randomsource, relativeBlockInfo.nbt())
                );
            }
        }

        return relativeBlockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.REDUCED_ALLOCATION_RULE_PROCESSOR.get();
    }
}