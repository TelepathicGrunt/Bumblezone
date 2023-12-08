package com.telepathicgrunt.the_bumblezone.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.utils.CodecUtils;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;


public class BlockStateSpecificTrigger extends SimpleCriterionTrigger<BlockStateSpecificTrigger.TriggerInstance> {

    public BlockStateSpecificTrigger() {}

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer serverPlayer, BlockPos pos) {
        super.trigger(serverPlayer, (instance) -> instance.matches(serverPlayer.level().getBlockState(pos)));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, CodecUtils.BlockMatcher blockMatcher) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<BlockStateSpecificTrigger.TriggerInstance> CODEC =
                RecordCodecBuilder.create(instance -> instance.group(
                        ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(BlockStateSpecificTrigger.TriggerInstance::player),
                        CodecUtils.BlockMatcher.CODEC.fieldOf("block").forGetter(BlockStateSpecificTrigger.TriggerInstance::blockMatcher)
                ).apply(instance, BlockStateSpecificTrigger.TriggerInstance::new));

        public boolean matches(BlockState blockState) {
            return this.blockMatcher().blockMatched(blockState);
        }
    }
}
