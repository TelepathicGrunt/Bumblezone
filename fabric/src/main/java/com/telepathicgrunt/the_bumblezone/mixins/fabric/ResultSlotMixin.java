package com.telepathicgrunt.the_bumblezone.mixins.fabric;

import com.telepathicgrunt.the_bumblezone.events.player.PlayerCraftedItemEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
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


    @Shadow @Final private Player player;

    @Shadow @Final private CraftingContainer craftSlots;

    @Inject(
            method = "checkTakeAchievements",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;onCraftedBy(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;I)V",
                    shift = At.Shift.AFTER
            )
    )
    private void bumblezone$onItemCrafted(ItemStack itemStack, CallbackInfo ci) {
        PlayerCraftedItemEvent.EVENT.invoke(new PlayerCraftedItemEvent(this.player, itemStack, this.craftSlots));
    }
}
