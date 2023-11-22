package com.telepathicgrunt.the_bumblezone.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class CounterTrigger extends SimpleCriterionTrigger<CounterTrigger.Instance> {

    public CounterTrigger() {}

    public void trigger(ServerPlayer serverPlayer, int currentCount) {
        super.trigger(serverPlayer, (trigger) -> trigger.matches(currentCount));
    }

    @Override
    protected Instance createInstance(JsonObject jsonObject, Optional<ContextAwarePredicate> predicate, DeserializationContext deserializationContext) {
        return new Instance(predicate, jsonObject.get("target_count").getAsInt());
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final int targetCount;

        public Instance(Optional<ContextAwarePredicate> predicate, int targetCount) {
            super(predicate);
            this.targetCount = targetCount;
        }

        public boolean matches(int currentCount) {
            return currentCount >= targetCount;
        }

        @Override
        public JsonObject serializeToJson() {
            JsonObject jsonobject = super.serializeToJson();
            jsonobject.addProperty("target_count", this.targetCount);
            return jsonobject;
        }
    }
}
