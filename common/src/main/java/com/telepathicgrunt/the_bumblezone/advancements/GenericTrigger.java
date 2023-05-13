package com.telepathicgrunt.the_bumblezone.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class GenericTrigger extends SimpleCriterionTrigger<GenericTrigger.Instance> {
    private final ResourceLocation id;

    public GenericTrigger(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public Instance createInstance(JsonObject jsonObject, ContextAwarePredicate predicate, DeserializationContext deserializationContext) {
        return new Instance(predicate);
    }

    public void trigger(ServerPlayer serverPlayer) {
        super.trigger(serverPlayer, (e) -> true);
    }

    public class Instance extends AbstractCriterionTriggerInstance {
        public Instance(ContextAwarePredicate predicate) {
            super(id, predicate);
        }
    }
}
