package com.telepathicgrunt.the_bumblezone.mixin.neoforge.item;

import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemExtension.class)
public interface ItemExtensionMixin extends IItemExtension {

    @Shadow
    boolean bz$canPerformAction(ItemStack stack, String toolAction);

    @Override
    default boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return this.bz$canPerformAction(stack, itemAbility.name());
    }
}
