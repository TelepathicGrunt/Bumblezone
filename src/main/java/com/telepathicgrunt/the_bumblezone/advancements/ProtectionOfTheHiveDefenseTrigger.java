package com.telepathicgrunt.the_bumblezone.advancements;

import com.google.gson.JsonObject;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

public class ProtectionOfTheHiveDefenseTrigger extends SimpleCriterionTrigger<ProtectionOfTheHiveDefenseTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "protection_of_the_hive_defense");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance createInstance(JsonObject jsonObject, EntityPredicate.Composite predicate, DeserializationContext deserializationContext) {
        EntityPredicate.Composite entityPredicate = EntityPredicate.Composite.fromJson(jsonObject, "entity", deserializationContext);
        return new Instance(predicate, entityPredicate);
    }

    public void trigger(ServerPlayer serverPlayer, Entity entity) {
        LootContext lootcontext = EntityPredicate.createContext(serverPlayer, entity);
        this.trigger(serverPlayer, (instance) -> instance.matches(lootcontext));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final EntityPredicate.Composite attackerEntityPredicate;

        public Instance(EntityPredicate.Composite predicate, EntityPredicate.Composite attackerEntityPredicate) {
            super(ProtectionOfTheHiveDefenseTrigger.ID, predicate);
            this.attackerEntityPredicate = attackerEntityPredicate;
        }

        public boolean matches(LootContext lootContext) {
            return this.attackerEntityPredicate.matches(lootContext);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext) {
            JsonObject jsonobject = super.serializeToJson(serializationContext);
            jsonobject.add("entity", this.attackerEntityPredicate.toJson(serializationContext));
            return jsonobject;
        }
    }
}
