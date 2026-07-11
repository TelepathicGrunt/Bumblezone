package com.telepathicgrunt.the_bumblezone.loot;

import net.minecraft.resources.Identifier;

import java.util.Set;

public interface LootParamsBzVisitedLootInterface {

    Set<Identifier> theBumblezone$getVisitedLootId();

    void theBumblezone$addVisitedLootId(Identifier bzVisitedLootId);
}
