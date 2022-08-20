package com.telepathicgrunt.the_bumblezone.entities.goals.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;


public class ItemSpecificTrigger extends SimpleCriterionTrigger<ItemSpecificTrigger.Instance> {
    private final ResourceLocation id;

    public ItemSpecificTrigger(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public Instance createInstance(JsonObject jsonObject, EntityPredicate.Composite predicate, DeserializationContext deserializationContext) {
        return new Instance(predicate, ItemPredicate.fromJson(jsonObject.get("item")));
    }

    public void trigger(ServerPlayer serverPlayer, ItemStack itemStack) {
        super.trigger(serverPlayer, (currentItemStack) -> currentItemStack.matches(itemStack));
    }

    public class Instance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate itemPredicate;

        public Instance(EntityPredicate.Composite predicate, ItemPredicate itemPredicate) {
            super(id, predicate);
            this.itemPredicate = itemPredicate;
        }

        public boolean matches(ItemStack itemStack) {
            return this.itemPredicate.matches(itemStack);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext) {
            JsonObject jsonobject = super.serializeToJson(serializationContext);
            jsonobject.add("item", this.itemPredicate.serializeToJson());
            return jsonobject;
        }
    }
}
