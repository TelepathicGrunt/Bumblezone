package com.telepathicgrunt.the_bumblezone.advancements;

import com.google.gson.JsonObject;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

public class HoneyCrystalInWaterTrigger extends AbstractCriterionTrigger<HoneyCrystalInWaterTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "honey_crystal_in_water");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public HoneyCrystalInWaterTrigger.Instance createInstance(JsonObject jsonObject, EntityPredicate.AndPredicate predicate, ConditionArrayParser conditionArrayParser) {
        return new HoneyCrystalInWaterTrigger.Instance(predicate);
    }

    public void trigger(ServerPlayerEntity serverPlayerEntity) {
        super.trigger(serverPlayerEntity, (e) -> true);
    }

    public static class Instance extends CriterionInstance {
        public Instance(EntityPredicate.AndPredicate predicate) {
            super(HoneyCrystalInWaterTrigger.ID, predicate);
        }
    }
}
