package com.telepathicgrunt.the_bumblezone.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class CodecUtils {

    public record BlockMatcher(HolderSet<Block> blocks, Optional<StatePropertiesPredicate> state) {

        public static final Codec<BlockMatcher> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(BlockMatcher::blocks),
                StatePropertiesPredicate.CODEC.optionalFieldOf("state").forGetter(BlockMatcher::state)
        ).apply(builder, BlockMatcher::new));

        public boolean blockMatched(BlockState blockState) {
            if (blocks.stream().noneMatch(blockHolder -> blockState.is(blockHolder.value()))) {
                return false;
            }

            if (state.isPresent() && !state.get().matches(blockState)) {
                return false;
            }

            return true;
        }
    }
}
