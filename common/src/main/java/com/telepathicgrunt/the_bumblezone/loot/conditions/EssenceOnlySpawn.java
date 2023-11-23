package com.telepathicgrunt.the_bumblezone.loot.conditions;

import com.mojang.serialization.Codec;
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
    public static final Codec<EssenceOnlySpawn> CODEC = Codec.unit(() -> INSTANCE);

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

}