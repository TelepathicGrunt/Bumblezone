package com.telepathicgrunt.the_bumblezone.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class GenericTrigger extends SimpleCriterionTrigger<GenericTrigger.TriggerInstance> {

    public GenericTrigger() {}

    @Override
    public Codec<GenericTrigger.TriggerInstance> codec() {
        return GenericTrigger.TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer serverPlayer) {
        super.trigger(serverPlayer, (e) -> true);
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<GenericTrigger.TriggerInstance> CODEC =
                RecordCodecBuilder.create(instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(GenericTrigger.TriggerInstance::player)
                ).apply(instance, GenericTrigger.TriggerInstance::new));

        public boolean matches(boolean passthrough) {
            return passthrough;
        }
    }
}
