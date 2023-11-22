package com.telepathicgrunt.the_bumblezone.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;


public class ItemSpecificTrigger extends SimpleCriterionTrigger<ItemSpecificTrigger.Instance> {

    public ItemSpecificTrigger() {}

    @Override
    public Instance createInstance(JsonObject jsonObject, Optional<ContextAwarePredicate> predicate, DeserializationContext deserializationContext) {
        return new Instance(predicate, ItemPredicate.fromJson(jsonObject.get("item")).get());
    }

    public void trigger(ServerPlayer serverPlayer, ItemStack itemStack) {
        super.trigger(serverPlayer, (currentItemStack) -> currentItemStack.matches(itemStack));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate itemPredicate;

        public Instance(Optional<ContextAwarePredicate> predicate, ItemPredicate itemPredicate) {
            super(predicate);
            this.itemPredicate = itemPredicate;
        }

        public boolean matches(ItemStack itemStack) {
            return this.itemPredicate.matches(itemStack);
        }

        @Override
        public JsonObject serializeToJson() {
            JsonObject jsonobject = super.serializeToJson();
            jsonobject.add("item", this.itemPredicate.serializeToJson());
            return jsonobject;
        }
    }
}
