package com.telepathicgrunt.the_bumblezone.mixin.containers;

import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(WorkbenchContainer.class)
public class WorkbenchContainerMixin {

    // makes RecipeDiscoveredTrigger work.
    @Inject(method = "slotChangedCraftingGrid(ILnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/inventory/CraftingInventory;Lnet/minecraft/inventory/CraftResultInventory;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/CraftResultInventory;setRecipeUsed(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/ServerPlayerEntity;Lnet/minecraft/item/crafting/IRecipe;)Z"),
            locals = LocalCapture.CAPTURE_FAILSOFT, require = 0)
    private static void thebumblezone_recipeDiscoveredTrigger1(int slotID, World world,
                                                               PlayerEntity playerEntity, CraftingInventory craftingInventory,
                                                               CraftResultInventory craftResultInventory, CallbackInfo ci,
                                                               ServerPlayerEntity serverPlayerEntity, ItemStack itemstack,
                                                               Optional<ICraftingRecipe> optional, ICraftingRecipe craftingRecipe)
    {
        BzCriterias.RECIPE_DISCOVERED_TRIGGER.trigger(serverPlayerEntity, craftingRecipe.getId());
    }
}