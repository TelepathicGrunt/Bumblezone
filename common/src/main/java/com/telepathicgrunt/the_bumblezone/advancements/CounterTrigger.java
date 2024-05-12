package com.telepathicgrunt.the_bumblezone.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public class CounterTrigger extends SimpleCriterionTrigger<CounterTrigger.TriggerInstance> {

    public CounterTrigger() {}

    @Override
    public Codec<CounterTrigger.TriggerInstance> codec() {
        return CounterTrigger.TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer serverPlayer, int currentCount) {
        super.trigger(serverPlayer, (trigger) -> trigger.matches(currentCount));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, int targetCount) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<CounterTrigger.TriggerInstance> CODEC =
                RecordCodecBuilder.create(instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(CounterTrigger.TriggerInstance::player),
                        ExtraCodecs.POSITIVE_INT.fieldOf("target_count").forGetter(CounterTrigger.TriggerInstance::targetCount)
                ).apply(instance, CounterTrigger.TriggerInstance::new));

        public boolean matches(int count) {
            return count == targetCount();
        }
    }
}
