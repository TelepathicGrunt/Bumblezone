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

public class BeeDropPollenPuffTrigger extends AbstractCriterionTrigger<BeeDropPollenPuffTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "bee_drop_pollen_puff");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public BeeDropPollenPuffTrigger.Instance createInstance(JsonObject jsonObject, EntityPredicate.AndPredicate predicate, ConditionArrayParser conditionArrayParser) {
        return new BeeDropPollenPuffTrigger.Instance(predicate, ItemPredicate.fromJson(jsonObject.get("item")));
    }

    public void trigger(ServerPlayerEntity serverPlayerEntity, ItemStack itemStack) {
        super.trigger(serverPlayerEntity, (currentItemStack) -> currentItemStack.matches(itemStack));
    }

    public static class Instance extends CriterionInstance {
        private final ItemPredicate itemPredicate;

        public Instance(EntityPredicate.AndPredicate predicate, ItemPredicate itemPredicate) {
            super(BeeDropPollenPuffTrigger.ID, predicate);
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
