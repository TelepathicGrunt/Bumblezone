package com.telepathicgrunt.the_bumblezone.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Optional;

public class EntitySpecificTrigger extends SimpleCriterionTrigger<EntitySpecificTrigger.Instance> {

    public EntitySpecificTrigger() {}

    @Override
    public Instance createInstance(JsonObject jsonObject, Optional<ContextAwarePredicate> predicate, DeserializationContext deserializationContext) {
        Optional<ContextAwarePredicate> entityPredicate = EntityPredicate.fromJson(jsonObject, "entity", deserializationContext);
        return new Instance(predicate, entityPredicate);
    }

    public void trigger(ServerPlayer serverPlayer, Entity entity) {
        LootContext lootcontext = EntityPredicate.createContext(serverPlayer, entity);
        this.trigger(serverPlayer, (instance) -> instance.matches(lootcontext));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final ContextAwarePredicate attackerEntityPredicate;

        public Instance(Optional<ContextAwarePredicate> predicate, Optional<ContextAwarePredicate> attackerEntityPredicate) {
            super(predicate);
            this.attackerEntityPredicate = attackerEntityPredicate.get();
        }

        public boolean matches(LootContext lootContext) {
            return this.attackerEntityPredicate.matches(lootContext);
        }

        @Override
        public JsonObject serializeToJson() {
            JsonObject jsonobject = super.serializeToJson();
            jsonobject.add("entity", this.attackerEntityPredicate.toJson());
            return jsonobject;
        }
    }
}
