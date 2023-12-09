package com.telepathicgrunt.the_bumblezone.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.items.BeeArmor;
import com.telepathicgrunt.the_bumblezone.modules.PlayerDataModule;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public class KilledCounterTrigger extends SimpleCriterionTrigger<KilledCounterTrigger.TriggerInstance> {
    public KilledCounterTrigger() {}

    @Override
    public Codec<KilledCounterTrigger.TriggerInstance> codec() {
        return KilledCounterTrigger.TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer serverPlayer, Entity currentEntity, PlayerDataModule module) {
        super.trigger(serverPlayer, (trigger) -> trigger.matches(serverPlayer, currentEntity, module));
    }

    public record TriggerInstance(
        Optional<ContextAwarePredicate> player,
        int targetCount,
        ResourceLocation targetEntity,
        boolean isTargetTag,
        boolean beeArmorRequired
    ) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<KilledCounterTrigger.TriggerInstance> CODEC =
                RecordCodecBuilder.create(instance -> instance.group(
                        ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(KilledCounterTrigger.TriggerInstance::player),
                        ExtraCodecs.POSITIVE_INT.fieldOf("target_count").forGetter(KilledCounterTrigger.TriggerInstance::targetCount),
                        ResourceLocation.CODEC.fieldOf("target_entity").forGetter(KilledCounterTrigger.TriggerInstance::targetEntity),
                        Codec.BOOL.fieldOf("is_target_tag").orElse(false).forGetter(KilledCounterTrigger.TriggerInstance::isTargetTag),
                        Codec.BOOL.fieldOf("bee_armor_required").orElse(false).forGetter(KilledCounterTrigger.TriggerInstance::beeArmorRequired)
                ).apply(instance, KilledCounterTrigger.TriggerInstance::new));

        public boolean matches(ServerPlayer serverPlayer, Entity currentEntity, PlayerDataModule module) {
            boolean entityMatch;
            if (this.isTargetTag) {
                entityMatch = currentEntity.getType().is(TagKey.create(Registries.ENTITY_TYPE, this.targetEntity));
            }
            else {
                entityMatch = BuiltInRegistries.ENTITY_TYPE.getKey(currentEntity.getType()).equals(this.targetEntity);
            }

            int currentCount = 0;
            if (entityMatch) {
                module.mobsKilledTracker.merge(this.targetEntity, 1, Integer::sum);
                currentCount = module.mobsKilledTracker.get(this.targetEntity);
            }

            return entityMatch &&
                    (!this.beeArmorRequired || BeeArmor.getBeeThemedWearablesCount(serverPlayer) >= 4) &&
                    currentCount >= this.targetCount;
        }
    }
}
