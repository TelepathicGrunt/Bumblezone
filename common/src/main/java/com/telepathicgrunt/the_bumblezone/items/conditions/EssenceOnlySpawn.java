package com.telepathicgrunt.the_bumblezone.items.conditions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.modinit.BzLootConditionTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class EssenceOnlySpawn implements LootItemCondition {
    static final EssenceOnlySpawn INSTANCE = new EssenceOnlySpawn();

    public EssenceOnlySpawn() {
        super();
    }

    @Override
    public boolean test(LootContext lootContext) {
        Entity entity = lootContext.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (entity instanceof ServerPlayer serverPlayer) {
            return EssenceOfTheBees.hasEssence(serverPlayer);
        }
        return false;
    }

    @Override
    public LootItemConditionType getType() {
        return BzLootConditionTypes.ESSENCE_ONLY_SPAWN.get();
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<EssenceOnlySpawn> {
        @Override
        public void serialize(JsonObject jsonObject, EssenceOnlySpawn object, JsonSerializationContext jsonSerializationContext) {}

        @Override
        public EssenceOnlySpawn deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            return INSTANCE;
        }
    }
}