package com.telepathicgrunt.bumblezone.advancements;

import com.google.gson.JsonObject;
import com.telepathicgrunt.bumblezone.Bumblezone;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class TeleportToBumblezoneByPistonTrigger extends SimpleCriterionTrigger<TeleportToBumblezoneByPistonTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(Bumblezone.MODID, "teleport_to_bumblezone_piston");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance createInstance(JsonObject jsonObject, EntityPredicate.Composite predicate, DeserializationContext deserializationContext) {
        return new Instance(predicate);
    }

    public void trigger(ServerPlayer serverPlayer) {
        super.trigger(serverPlayer, (e) -> true);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(EntityPredicate.Composite predicate) {
            super(TeleportToBumblezoneByPistonTrigger.ID, predicate);
        }
    }
}
