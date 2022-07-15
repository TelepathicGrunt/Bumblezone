package com.telepathicgrunt.the_bumblezone.advancements;

import com.google.gson.JsonObject;
import com.telepathicgrunt.the_bumblezone.mixin.PlayerAdvancementsAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.AdvancementEvent;

import java.util.Map;

public class CounterTrigger extends SimpleCriterionTrigger<CounterTrigger.Instance> {
    private final ResourceLocation id;

    public CounterTrigger(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public CounterTrigger.Instance createInstance(JsonObject jsonObject, EntityPredicate.Composite predicate, DeserializationContext deserializationContext) {
        return new CounterTrigger.Instance(predicate, jsonObject.get("target_count").getAsInt());
    }

    public void trigger(ServerPlayer serverPlayer, int currentCount) {
        super.trigger(serverPlayer, (trigger) -> trigger.matches(currentCount));
    }

    public class Instance extends AbstractCriterionTriggerInstance {
        private final int targetCount;

        public Instance(EntityPredicate.Composite predicate, int targetCount) {
            super(id, predicate);
            this.targetCount = targetCount;
        }

        public boolean matches(int currentCount) {
            return currentCount >= targetCount;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext) {
            JsonObject jsonobject = super.serializeToJson(serializationContext);
            jsonobject.addProperty("target_count", this.targetCount);
            return jsonobject;
        }
    }
}
