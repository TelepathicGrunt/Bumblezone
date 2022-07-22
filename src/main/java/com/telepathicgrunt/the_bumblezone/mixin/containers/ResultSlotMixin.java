package com.telepathicgrunt.the_bumblezone.mixin.containers;

import com.telepathicgrunt.the_bumblezone.components.MiscComponent;
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
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;onCraftedBy(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;I)V"))
    private void thebumblezone_craftedItem(ItemStack itemStack, CallbackInfo ci) {
        MiscComponent.onItemCrafted(itemStack, player);
    }
}
