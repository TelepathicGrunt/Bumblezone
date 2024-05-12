package com.telepathicgrunt.the_bumblezone.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.events.player.PlayerGrantAdvancementEvent;
import com.telepathicgrunt.the_bumblezone.mixin.entities.PlayerAdvancementsAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

import java.util.Map;
import java.util.Optional;

public class TargetAdvancementDoneTrigger extends SimpleCriterionTrigger<TargetAdvancementDoneTrigger.TriggerInstance> {

    public TargetAdvancementDoneTrigger() {}

    @Override
    public Codec<TargetAdvancementDoneTrigger.TriggerInstance> codec() {
        return TargetAdvancementDoneTrigger.TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer serverPlayer) {
        super.trigger(serverPlayer, (trigger) -> trigger.matches(serverPlayer));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, ResourceLocation targetAdvancement) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<TargetAdvancementDoneTrigger.TriggerInstance> CODEC =
                RecordCodecBuilder.create(instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TargetAdvancementDoneTrigger.TriggerInstance::player),
                        ResourceLocation.CODEC.fieldOf("target_advancement").forGetter(TargetAdvancementDoneTrigger.TriggerInstance::targetAdvancement)
                ).apply(instance, TargetAdvancementDoneTrigger.TriggerInstance::new));

        public boolean matches(ServerPlayer serverPlayer) {
            AdvancementHolder advancementHolder = serverPlayer.server.getAdvancements().get(targetAdvancement);
            Map<AdvancementHolder, AdvancementProgress> advancementsProgressMap = ((PlayerAdvancementsAccessor)serverPlayer.getAdvancements()).getProgress();
            return advancementHolder != null &&
                    advancementsProgressMap.containsKey(advancementHolder) &&
                    advancementsProgressMap.get(advancementHolder).isDone();
        }
    }

    public static void OnAdvancementGiven(PlayerGrantAdvancementEvent event) {
        if (event.player() instanceof ServerPlayer serverPlayer) {
            BzCriterias.TARGET_ADVANCEMENT_DONE_TRIGGER.get().trigger(serverPlayer);
        }
    }
}
