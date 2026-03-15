package com.telepathicgrunt.the_bumblezone.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class HoneyCocoonBlockItem extends BzBlockItem {
    public HoneyCocoonBlockItem(Block block, Properties properties, boolean fitInContainers) {
        super(block, properties, fitInContainers);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        if (itemStack.has(DataComponents.CONTAINER_LOOT)) {
            return;
        }

        int i = 0;
        int j = 0;

        for(ItemStackTemplate itemStack2 : itemStack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).nonEmptyItems()) {
            ++j;
            if (i <= 4) {
                ++i;
                builder.accept(Component.translatable("container.the_bumblezone.honey_cocoon.item_count", itemStack2.create().getHoverName(), itemStack2.count()));
            }
        }

        if (j - i > 0) {
            builder.accept(Component.translatable("container.the_bumblezone.honey_cocoon.more", j - i).withStyle(ChatFormatting.ITALIC));
        }
    }
}
