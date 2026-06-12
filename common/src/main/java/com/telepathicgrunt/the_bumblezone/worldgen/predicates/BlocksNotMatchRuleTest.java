package com.telepathicgrunt.the_bumblezone.worldgen.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzPredicates;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

import java.util.List;
import java.util.Optional;

public class BlocksNotMatchRuleTest extends RuleTest {
    public static final Codec<BlocksNotMatchRuleTest> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().listOf().optionalFieldOf("blocks_to_not_match").forGetter(config -> config.blocksToNotMatch),
            TagKey.codec(Registries.BLOCK).optionalFieldOf("block_tag_to_not_match").forGetter(config -> config.blockTagToNotMatch)
    ).apply(instance, BlocksNotMatchRuleTest::new));

    private final Optional<List<Block>> blocksToNotMatch;
    private final Optional<TagKey<Block>> blockTagToNotMatch;

    public BlocksNotMatchRuleTest(Optional<List<Block>> blocksToNotMatch, Optional<TagKey<Block>> blockTagToNotMatch) {
        this.blocksToNotMatch = blocksToNotMatch;
        this.blockTagToNotMatch = blockTagToNotMatch;
        if (this.blocksToNotMatch.isEmpty() && this.blockTagToNotMatch.isEmpty()) {
            throw new IllegalArgumentException("the_bumblezone:blocks_not_match_rule_test processor rule type MUST have either blocks_to_not_match or block_tag_to_not_match specified.");
        }
    }

    @Override
    public boolean test(BlockState blockState, RandomSource randomSource) {
        // Don't turn into capturing lambda.
        if (this.blocksToNotMatch.isPresent()) {
            return !this.blocksToNotMatch.get().contains(blockState.getBlock());
        }

        return !blockState.is(this.blockTagToNotMatch.get());
    }

    @Override
    protected RuleTestType<?> getType() {
        return BzPredicates.BLOCKS_NOT_MATCH_RULE_TEST.get();
    }
}

