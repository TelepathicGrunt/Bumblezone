package com.telepathicgrunt.the_bumblezone.mixins.forge.item;

import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.extensions.IForgeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemExtension.class)
public interface ItemExtensionMixin extends IForgeItem {

    @Shadow
    boolean bz$canPerformAction(ItemStack stack, String toolAction);

    @Override
    default boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return this.bz$canPerformAction(stack, toolAction.name());
    }
}
