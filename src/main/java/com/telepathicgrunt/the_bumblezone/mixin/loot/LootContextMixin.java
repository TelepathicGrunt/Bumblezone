package com.telepathicgrunt.the_bumblezone.mixin.loot;

import com.telepathicgrunt.the_bumblezone.loot.LootContextBzVisitedLootInterface;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashSet;
import java.util.Set;

@Mixin(LootContext.class)
public class LootContextMixin implements LootContextBzVisitedLootInterface {

    @Unique
    Set<ResourceLocation> bumblezone_visitedBzLootRLs = new HashSet<>();

    @Override
    public Set<ResourceLocation> getVisitedBzVisitedLootRL() {
        return bumblezone_visitedBzLootRLs;
    }

    @Override
    public void addVisitedBzVisitedLootRL(ResourceLocation bzVisitedLootRL) {
        bumblezone_visitedBzLootRLs.add(bzVisitedLootRL);
    }
}