package com.telepathicgrunt.the_bumblezone.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;


public class ItemSpecificTrigger extends SimpleCriterionTrigger<ItemSpecificTrigger.TriggerInstance> {

    public ItemSpecificTrigger() {}

    @Override
    public Codec<ItemSpecificTrigger.TriggerInstance> codec() {
        return ItemSpecificTrigger.TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer serverPlayer, ItemStack itemStack) {
        super.trigger(serverPlayer, (currentItemStack) -> currentItemStack.matches(itemStack));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> item) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<ItemSpecificTrigger.TriggerInstance> CODEC =
                RecordCodecBuilder.create(instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(ItemSpecificTrigger.TriggerInstance::player),
                        ItemPredicate.CODEC.optionalFieldOf("item").forGetter(ItemSpecificTrigger.TriggerInstance::item)
                ).apply(instance, ItemSpecificTrigger.TriggerInstance::new));

        public boolean matches(ItemStack itemStack) {
            return this.item.isEmpty() || this.item.get().test(itemStack);
        }
    }
}
