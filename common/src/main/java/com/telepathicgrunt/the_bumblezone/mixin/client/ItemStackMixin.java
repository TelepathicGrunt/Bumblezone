package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.telepathicgrunt.the_bumblezone.items.BeeArmor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Fired on clientside only (Registered to client mixins json)
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "inventoryTick(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/EquipmentSlot;)V",
            at = @At(value = "RETURN")
    )
    private void bumblezone$runClientArmorTickForBzArmor(Level level, Entity owner, EquipmentSlot slot, CallbackInfo ci) {
        if (level instanceof ClientLevel clientLevel &&
            owner instanceof Player player &&
            this.getItem() instanceof BeeArmor beeArmor)
        {
            beeArmor.bz$onArmorTickWrapper((ItemStack) (Object) this, clientLevel, player, slot);
        }
    }

    @Shadow
    public Item getItem() {
        return null;
    }
}