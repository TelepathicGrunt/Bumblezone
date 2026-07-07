package com.telepathicgrunt.the_bumblezone.mixin.neoforge.items;

import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemExtension.class)
public interface ItemExtensionMixin extends IItemExtension {

    @Shadow
    boolean bz$canPerformAction(ItemInstance itemInstance, String toolAction);

    @Override
    default boolean canPerformAction(ItemInstance itemInstance, ItemAbility itemAbility) {
        return this.bz$canPerformAction(itemInstance, itemAbility.name());
    }
}
