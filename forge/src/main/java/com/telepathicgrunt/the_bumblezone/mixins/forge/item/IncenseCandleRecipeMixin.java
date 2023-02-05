package com.telepathicgrunt.the_bumblezone.mixins.forge.item;

import com.telepathicgrunt.the_bumblezone.items.recipes.IncenseCandleRecipe;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraftforge.common.crafting.IShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(IncenseCandleRecipe.class)
public abstract class IncenseCandleRecipeMixin implements IShapedRecipe<CraftingContainer> {

    @Shadow
    public abstract int getWidth();

    @Shadow
    public abstract int getHeight();

    @Override
    public int getRecipeWidth() {
        return getWidth();
    }

    @Override
    public int getRecipeHeight() {
        return getHeight();
    }
}
