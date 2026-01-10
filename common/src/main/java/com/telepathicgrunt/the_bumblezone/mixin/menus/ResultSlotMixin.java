package com.telepathicgrunt.the_bumblezone.mixin.menus;

import com.telepathicgrunt.the_bumblezone.advancements.helpers.BeehiveCraftedHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResultSlot.class)
public class ResultSlotMixin {

    @Final
    @Shadow
    private Player player;

    @Inject(method = "checkTakeAchievements(Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/RecipeCraftingHolder;awardUsedRecipes(Lnet/minecraft/world/entity/player/Player;Ljava/util/List;)V"))
    private void bumblezone$recipeCraftedCheck1(ItemStack carried, CallbackInfo ci) {
        BeehiveCraftedHelper.checkAndIncrementBeehiveCraftedCount(player, carried);
    }
}