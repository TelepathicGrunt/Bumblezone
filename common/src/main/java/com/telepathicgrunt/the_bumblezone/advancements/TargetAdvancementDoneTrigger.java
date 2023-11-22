package com.telepathicgrunt.the_bumblezone.advancements;

import com.google.gson.JsonObject;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerGrantAdvancementEvent;
import com.telepathicgrunt.the_bumblezone.mixin.entities.PlayerAdvancementsAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.Optional;

public class TargetAdvancementDoneTrigger extends SimpleCriterionTrigger<TargetAdvancementDoneTrigger.Instance> {

    public TargetAdvancementDoneTrigger() {}

    @Override
    public TargetAdvancementDoneTrigger.Instance createInstance(JsonObject jsonObject, Optional<ContextAwarePredicate> predicate, DeserializationContext deserializationContext) {
        return new TargetAdvancementDoneTrigger.Instance(predicate, new ResourceLocation(jsonObject.get("target_advancement").getAsString()));
    }

    public void trigger(ServerPlayer serverPlayer) {
        super.trigger(serverPlayer, (trigger) -> trigger.matches(serverPlayer));
    }

    public class Instance extends AbstractCriterionTriggerInstance {
        private final ResourceLocation targetAdvancement;

        public Instance(Optional<ContextAwarePredicate> predicate, ResourceLocation targetAdvancement) {
            super(predicate);
            this.targetAdvancement = targetAdvancement;
        }

        public boolean matches(ServerPlayer serverPlayer) {
            AdvancementHolder advancement = serverPlayer.server.getAdvancements().get(targetAdvancement);
            Map<Advancement, AdvancementProgress> advancementsProgressMap = ((PlayerAdvancementsAccessor)serverPlayer.getAdvancements()).getProgress();
            return advancement != null &&
                    advancementsProgressMap.containsKey(advancement.value()) &&
                    advancementsProgressMap.get(advancement.value()).isDone();
        }

        @Override
        public JsonObject serializeToJson() {
            JsonObject jsonobject = super.serializeToJson();
            jsonobject.addProperty("target_advancement", this.targetAdvancement.toString());
            return jsonobject;
        }
    }

    public static void OnAdvancementGiven(PlayerGrantAdvancementEvent event) {
        if (event.player() instanceof ServerPlayer serverPlayer) {
            BzCriterias.TARGET_ADVANCEMENT_DONE_TRIGGER.trigger(serverPlayer);
        }
    }
}
