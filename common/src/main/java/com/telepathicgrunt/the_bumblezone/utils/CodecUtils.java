package com.telepathicgrunt.the_bumblezone.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class CodecUtils {

    public record BlockMatcher(ResourceLocation blockRL, boolean isTag, Optional<StatePropertiesPredicate> state) {

        public static final MapCodec<BlockMatcher> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
                ResourceLocation.CODEC.fieldOf("resourcelocation").forGetter(BlockMatcher::blockRL),
                Codec.BOOL.fieldOf("is_tag").forGetter(BlockMatcher::isTag),
                StatePropertiesPredicate.CODEC.optionalFieldOf("state").forGetter(BlockMatcher::state)
        ).apply(builder, BlockMatcher::new));

        public boolean blockMatched(BlockState blockState) {
            if (isTag()) {
                 if (!blockState.is(TagKey.create(Registries.BLOCK, blockRL()))) {
                     return false;
                 }
            }
            else if (!blockState.is(ResourceKey.create(Registries.BLOCK, blockRL()))) {
                return false;
            }

            return state.isEmpty() || state.get().matches(blockState);
        }
    }
}
