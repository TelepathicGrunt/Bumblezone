package com.telepathicgrunt.the_bumblezone.mixin.enchantments;

import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import com.telepathicgrunt.the_bumblezone.utils.PlatformHooks;
import com.telepathicgrunt.the_bumblezone.utils.TriState;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

    @Inject(method = "canEnchant(Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void bumblezone$customAllowDisallowEnchantments1(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack.getItem() instanceof ItemExtension itemExtension) {
            Registry<Enchantment> enchantmentRegistry = PlatformHooks.getCurrentRegistryAccess().registry(Registries.ENCHANTMENT).get();
            TriState result = itemExtension.bz$canEnchant(itemStack, enchantmentRegistry.createIntrusiveHolder((Enchantment) (Object) this));
            if (result != TriState.PASS) {
                cir.setReturnValue(result == TriState.ALLOW);
            }
        }
    }

    @Inject(method = "isPrimaryItem(Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void bumblezone$customAllowDisallowEnchantments2(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        if (itemStack.getItem() instanceof ItemExtension itemExtension) {
            Registry<Enchantment> enchantmentRegistry = PlatformHooks.getCurrentRegistryAccess().registry(Registries.ENCHANTMENT).get();
            TriState result = itemExtension.bz$canEnchant(itemStack, enchantmentRegistry.createIntrusiveHolder((Enchantment) (Object) this));
            if (result != TriState.PASS) {
                cir.setReturnValue(result == TriState.ALLOW);
            }
        }
    }
}