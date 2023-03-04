package com.telepathicgrunt.the_bumblezone.mixin.fabricbase.container;

import com.telepathicgrunt.the_bumblezone.utils.fabricbase.PlatformSharedData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResultSlot.class)
public class ResultSlotMixin {

    // makes RecipeDiscoveredTrigger work.
    @Inject(method = "onTake(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/crafting/RecipeManager;getRemainingItemsFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Lnet/minecraft/core/NonNullList;"),
            require = 0)
    private void thebumblezone_setCraftingPlayer(Player player, ItemStack itemStack, CallbackInfo ci) {
        PlatformSharedData.craftingPlayer = player;
    }

    // makes RecipeDiscoveredTrigger work.
    @Inject(method = "onTake(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/crafting/RecipeManager;getRemainingItemsFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/Container;Lnet/minecraft/world/level/Level;)Lnet/minecraft/core/NonNullList;"),
            require = 0)
    private void thebumblezone_clearCraftingPlayer(Player player, ItemStack itemStack, CallbackInfo ci) {
        PlatformSharedData.craftingPlayer = null;
    }
}