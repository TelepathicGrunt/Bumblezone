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

public class TargetAdvancementDoneTrigger extends SimpleCriterionTrigger<TargetAdvancementDoneTrigger.Instance> {
    private final ResourceLocation id;

    public TargetAdvancementDoneTrigger(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public TargetAdvancementDoneTrigger.Instance createInstance(JsonObject jsonObject, EntityPredicate.Composite predicate, DeserializationContext deserializationContext) {
        return new TargetAdvancementDoneTrigger.Instance(predicate, new ResourceLocation(jsonObject.get("target_advancement").getAsString()));
    }

    public void trigger(ServerPlayer serverPlayer) {
        super.trigger(serverPlayer, (trigger) -> trigger.matches(serverPlayer));
    }

    public class Instance extends AbstractCriterionTriggerInstance {
        private final ResourceLocation targetAdvancement;

        public Instance(EntityPredicate.Composite predicate, ResourceLocation targetAdvancement) {
            super(id, predicate);
            this.targetAdvancement = targetAdvancement;
        }

        public boolean matches(ServerPlayer serverPlayer) {
            Advancement advancement = serverPlayer.createCommandSourceStack().getAdvancement(targetAdvancement);
            Map<Advancement, AdvancementProgress> advancementsProgressMap = ((PlayerAdvancementsAccessor)serverPlayer.getAdvancements()).getAdvancements();
            return advancement != null &&
                    advancementsProgressMap.containsKey(advancement) &&
                    advancementsProgressMap.get(advancement).isDone();
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext) {
            JsonObject jsonobject = super.serializeToJson(serializationContext);
            jsonobject.addProperty("target_advancement", this.targetAdvancement.toString());
            return jsonobject;
        }
    }

    public static void OnAdvancementGiven(AdvancementEvent event) {
        if (event.getPlayer() instanceof ServerPlayer serverPlayer) {
            BzCriterias.TARGET_ADVANCEMENT_DONE_TRIGGER.trigger(serverPlayer);
        }
    }
}
