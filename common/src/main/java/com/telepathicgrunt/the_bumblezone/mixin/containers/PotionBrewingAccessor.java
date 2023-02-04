package com.telepathicgrunt.the_bumblezone.mixin.containers;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PotionBrewing.class)
public interface PotionBrewingAccessor {
    @Invoker("addMix")
    static void callAddMix(Potion potion, Item item, Potion potion1) {
        throw new UnsupportedOperationException();
    }
}
