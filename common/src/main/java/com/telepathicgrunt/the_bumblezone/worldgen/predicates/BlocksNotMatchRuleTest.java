package com.telepathicgrunt.the_bumblezone.worldgen.predicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzPredicates;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

import java.util.List;

public class BlocksNotMatchRuleTest extends RuleTest {
    public static final MapCodec<BlocksNotMatchRuleTest> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().listOf().fieldOf("blocks_to_not_match").forGetter(config -> config.blocksToNotMatch)
    ).apply(instance, BlocksNotMatchRuleTest::new));

    private final List<Block> blocksToNotMatch;

    public BlocksNotMatchRuleTest(List<Block> blocksToNotMatch) {
        this.blocksToNotMatch = blocksToNotMatch;
    }

    @Override
    public boolean test(BlockState blockState, RandomSource randomSource) {
        return !this.blocksToNotMatch.contains(blockState.getBlock());
    }

    @Override
    protected RuleTestType<?> getType() {
        return BzPredicates.BLOCKS_NOT_MATCH_RULE_TEST.get();
    }
}

