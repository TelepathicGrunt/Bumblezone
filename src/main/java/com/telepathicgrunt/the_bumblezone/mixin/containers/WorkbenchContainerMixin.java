package com.telepathicgrunt.the_bumblezone.mixin.containers;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CraftingMenu.class)
public class WorkbenchContainerMixin {

    // makes RecipeDiscoveredTrigger work.
    @ModifyVariable(method = "slotChangedCraftingGrid(Lnet/minecraft/world/inventory/AbstractContainerMenu;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/world/inventory/ResultContainer;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ResultContainer;setRecipeUsed(Lnet/minecraft/world/level/Level;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/crafting/Recipe;)Z"),
            require = 0)
    private static CraftingRecipe thebumblezone_recipeDiscoveredTrigger(CraftingRecipe recipe, AbstractContainerMenu handler, Level world, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            BzCriterias.RECIPE_DISCOVERED_TRIGGER.trigger(serverPlayer, recipe.getId());
        }
        return recipe;
    }
}