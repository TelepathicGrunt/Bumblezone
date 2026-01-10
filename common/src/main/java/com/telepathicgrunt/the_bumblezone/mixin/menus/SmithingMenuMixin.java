package com.telepathicgrunt.the_bumblezone.mixin.menus;

import com.telepathicgrunt.the_bumblezone.advancements.helpers.BeehiveCraftedHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingMenu.class)
public class SmithingMenuMixin {

    @Inject(method = "onTake(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ResultContainer;awardUsedRecipes(Lnet/minecraft/world/entity/player/Player;Ljava/util/List;)V"))
    private void bumblezone$recipeCraftedCheck2(Player player, ItemStack carried, CallbackInfo ci) {
        BeehiveCraftedHelper.checkAndIncrementBeehiveCraftedCount(player, carried);
    }
}