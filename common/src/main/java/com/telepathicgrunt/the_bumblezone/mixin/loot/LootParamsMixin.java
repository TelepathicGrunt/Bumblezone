package com.telepathicgrunt.the_bumblezone.mixin.loot;

import com.telepathicgrunt.the_bumblezone.loot.LootParamsBzVisitedLootInterface;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashSet;
import java.util.Set;

@Mixin(LootParams.class)
public class LootParamsMixin implements LootParamsBzVisitedLootInterface {

    @Unique
    Set<Identifier> bumblezone_visitedBzLootIds = new HashSet<>();

    @Override
    public Set<Identifier> theBumblezone$getVisitedLootId() {
        return bumblezone_visitedBzLootIds;
    }

    @Override
    public void theBumblezone$addVisitedLootId(Identifier bzVisitedLootId) {
        bumblezone_visitedBzLootIds.add(bzVisitedLootId);
    }
}