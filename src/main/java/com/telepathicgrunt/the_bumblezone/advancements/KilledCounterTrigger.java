package com.telepathicgrunt.the_bumblezone.advancements;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.telepathicgrunt.the_bumblezone.items.StinglessBeeHelmet;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class KilledCounterTrigger extends SimpleCriterionTrigger<KilledCounterTrigger.Instance> {
    private final ResourceLocation id;

    public KilledCounterTrigger(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public KilledCounterTrigger.Instance createInstance(JsonObject jsonObject, EntityPredicate.Composite predicate, DeserializationContext deserializationContext) {
        JsonElement beeArmorJson = jsonObject.get("bee_armor_required");
        return new KilledCounterTrigger.Instance(
                predicate,
                new ResourceLocation(jsonObject.get("target_entity").getAsString()),
                jsonObject.get("target_count").getAsInt(),
                beeArmorJson != null && beeArmorJson.getAsBoolean());
    }

    public void trigger(ServerPlayer serverPlayer, ResourceLocation currentEntity, int currentCount) {
        super.trigger(serverPlayer, (trigger) -> trigger.matches(serverPlayer, currentEntity, currentCount));
    }

    public class Instance extends AbstractCriterionTriggerInstance {
        private final int targetCount;
        private final ResourceLocation targetEntity;
        private final boolean beeArmorRequired;

        public Instance(EntityPredicate.Composite predicate, ResourceLocation targetEntity, int targetCount, boolean beeArmorRequired) {
            super(id, predicate);
            this.targetCount = targetCount;
            this.targetEntity = targetEntity;
            this.beeArmorRequired = beeArmorRequired;
        }

        public boolean matches(ServerPlayer serverPlayer, ResourceLocation currentEntity, int currentCount) {
            return (!beeArmorRequired || StinglessBeeHelmet.isAllBeeArmorOn(serverPlayer)) &&
                    currentEntity.equals(this.targetEntity) &&
                    currentCount >= this.targetCount;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext) {
            JsonObject jsonobject = super.serializeToJson(serializationContext);
            jsonobject.addProperty("target_count", this.targetCount);
            jsonobject.addProperty("target_entity", this.targetEntity.toString());
            jsonobject.addProperty("bee_armor_required", this.beeArmorRequired);
            return jsonobject;
        }
    }
}
