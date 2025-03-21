package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.loot;

import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(LootDataManager.class)
public interface LootDataManagerAccessor {
    @Accessor("elements")
    Map<LootDataId<?>, ?> bumblezone$getElements();
}
