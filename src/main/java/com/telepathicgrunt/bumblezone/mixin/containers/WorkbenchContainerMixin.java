package com.telepathicgrunt.bumblezone.mixin.containers;

import com.telepathicgrunt.bumblezone.modinit.BzCriterias;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(CraftingMenu.class)
public class WorkbenchContainerMixin {

    // makes RecipeDiscoveredTrigger work.
    @Inject(method = "slotChangedCraftingGrid(Lnet/minecraft/world/inventory/AbstractContainerMenu;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/world/inventory/ResultContainer;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ResultContainer;setRecipeUsed(Lnet/minecraft/world/level/Level;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/crafting/Recipe;)Z"),
            locals = LocalCapture.CAPTURE_FAILSOFT, require = 0)
    private static void thebumblezone_recipeDiscoveredTrigger1(AbstractContainerMenu abstractContainerMenu, Level level, Player player,
                                                               CraftingContainer craftingContainer, ResultContainer resultContainer,
                                                               CallbackInfo ci, ServerPlayer serverPlayer, ItemStack itemStack,
                                                               Optional<CraftingRecipe> optional, CraftingRecipe craftingRecipe)
    {
        BzCriterias.RECIPE_DISCOVERED_TRIGGER.trigger(serverPlayer, craftingRecipe.getId());
    }
}