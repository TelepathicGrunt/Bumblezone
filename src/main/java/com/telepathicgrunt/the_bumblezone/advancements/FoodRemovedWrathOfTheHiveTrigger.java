package com.telepathicgrunt.the_bumblezone.advancements;

import com.google.gson.JsonObject;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

public class FoodRemovedWrathOfTheHiveTrigger extends AbstractCriterionTrigger<FoodRemovedWrathOfTheHiveTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "food_removed_wrath_of_the_hive");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public FoodRemovedWrathOfTheHiveTrigger.Instance createInstance(JsonObject jsonObject, EntityPredicate.AndPredicate predicate, ConditionArrayParser conditionArrayParser) {
        return new FoodRemovedWrathOfTheHiveTrigger.Instance(predicate, ItemPredicate.fromJson(jsonObject.get("item")));
    }

    public void trigger(ServerPlayerEntity serverPlayerEntity, ItemStack itemStack) {
        super.trigger(serverPlayerEntity, (currentItemStack) -> currentItemStack.matches(itemStack));
    }

    public static class Instance extends CriterionInstance {
        private final ItemPredicate itemPredicate;

        public Instance(EntityPredicate.AndPredicate predicate, ItemPredicate itemPredicate) {
            super(FoodRemovedWrathOfTheHiveTrigger.ID, predicate);
            this.itemPredicate = itemPredicate;
        }

        public boolean matches(ItemStack itemStack) {
            return this.itemPredicate.matches(itemStack);
        }

        @Override
        public JsonObject serializeToJson(ConditionArraySerializer conditionArraySerializer) {
            JsonObject jsonobject = super.serializeToJson(conditionArraySerializer);
            jsonobject.add("item", this.itemPredicate.serializeToJson());
            return jsonobject;
        }
    }
}
