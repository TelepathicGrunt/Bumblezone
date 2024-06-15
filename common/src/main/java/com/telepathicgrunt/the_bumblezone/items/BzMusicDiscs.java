package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class BzMusicDiscs extends Item {

    private final boolean hasDownload;

    public BzMusicDiscs(Properties settings, boolean hasDownload) {
        super(settings); // This uses the pickup sound as a placeholder
        this.hasDownload = hasDownload;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
        if (this.hasDownload) {
            List<MutableComponent> componentList = GeneralUtilsClient.autoWrappedTooltip(
                    this.getDescriptionId() + ".desc",
                    this.getDescriptionId() + ".download");

            componentList.forEach(component -> list.add(
                component.withStyle(tooltipFlag.isAdvanced() ? ChatFormatting.DARK_PURPLE : ChatFormatting.DARK_GRAY)
            ));
        }
    }
}
