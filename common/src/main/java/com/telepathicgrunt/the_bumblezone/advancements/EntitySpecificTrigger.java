package com.telepathicgrunt.the_bumblezone.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Optional;

public class EntitySpecificTrigger extends SimpleCriterionTrigger<EntitySpecificTrigger.TriggerInstance> {

    public EntitySpecificTrigger() {}

    @Override
    public Codec<EntitySpecificTrigger.TriggerInstance> codec() {
        return EntitySpecificTrigger.TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer serverPlayer, Entity entity) {
        LootContext lootcontext = EntityPredicate.createContext(serverPlayer, entity);
        this.trigger(serverPlayer, (instance) -> instance.matches(lootcontext));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> entity) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<EntitySpecificTrigger.TriggerInstance> CODEC =
                RecordCodecBuilder.create(instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(EntitySpecificTrigger.TriggerInstance::player),
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("entity").forGetter(EntitySpecificTrigger.TriggerInstance::entity)
                ).apply(instance, EntitySpecificTrigger.TriggerInstance::new));

        public boolean matches(LootContext context) {
            return matches(entity(), context);
        }

        private static boolean matches(Optional<ContextAwarePredicate> optional, LootContext arg) {
            return optional.isEmpty() || optional.get().matches(arg);
        }
    }
}
