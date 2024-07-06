package com.telepathicgrunt.the_bumblezone.mixin.neoforge.item;

import com.telepathicgrunt.the_bumblezone.items.recipes.PotionCandleRecipe;
import net.minecraft.world.item.crafting.CraftingInput;
import net.neoforged.neoforge.common.crafting.IShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PotionCandleRecipe.class)
public abstract class PotionCandleRecipeMixin implements IShapedRecipe<CraftingInput> {

    @Shadow
    public abstract int getWidth();

    @Shadow
    public abstract int getHeight();
}
