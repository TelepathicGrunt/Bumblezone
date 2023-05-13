package com.telepathicgrunt.the_bumblezone.loot;

import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public interface LootParamsBzVisitedLootInterface {

    Set<ResourceLocation> getVisitedBzVisitedLootRL();

    void addVisitedBzVisitedLootRL(ResourceLocation bzVisitedLootRL);
}
