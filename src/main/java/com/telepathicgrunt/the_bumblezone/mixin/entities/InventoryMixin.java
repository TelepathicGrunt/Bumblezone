package com.telepathicgrunt.the_bumblezone.mixin.entities;

import com.telepathicgrunt.the_bumblezone.items.TickingArmorItem;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public class InventoryMixin {

    @Shadow
    @Final
    public NonNullList<ItemStack> armor;

    @Shadow
    @Final
    public Player player;

    @Inject(method = "tick()V",
            at = @At(value = "TAIL"))
    private void thebumblezone_armorTick(CallbackInfo ci) {
        armor.forEach(itemStack -> {
            if(itemStack.getItem() instanceof TickingArmorItem tickingArmorItem) {
                tickingArmorItem.onArmorTick(itemStack, player.level, player);
            }
        });
    }
}