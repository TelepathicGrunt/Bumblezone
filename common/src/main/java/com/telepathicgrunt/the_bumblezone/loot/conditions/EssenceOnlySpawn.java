package com.telepathicgrunt.the_bumblezone.loot.conditions;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class EssenceOnlySpawn implements LootItemCondition {
    static final EssenceOnlySpawn INSTANCE = new EssenceOnlySpawn();
    public static final MapCodec<EssenceOnlySpawn> CODEC = MapCodec.unit(() -> INSTANCE);

    public EssenceOnlySpawn() {
        super();
    }

    @Override
    public boolean test(LootContext lootContext) {
        if (lootContext.hasParameter(LootContextParams.THIS_ENTITY)) {
            Entity entity = lootContext.getParameter(LootContextParams.THIS_ENTITY);
            if (entity instanceof ServerPlayer serverPlayer) {
                return EssenceOfTheBees.hasEssence(serverPlayer);
            }
        }
        return false;
    }

    @Override
    public MapCodec<EssenceOnlySpawn> codec() {
        return CODEC;
    }
}