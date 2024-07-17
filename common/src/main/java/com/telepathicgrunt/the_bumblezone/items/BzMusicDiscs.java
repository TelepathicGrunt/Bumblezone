package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.Optional;

public class BzMusicDiscs extends Item {

    private final boolean hasDownload;

    public BzMusicDiscs(Properties settings, boolean hasDownload) {
        super(settings); // This uses the pickup sound as a placeholder
        this.hasDownload = hasDownload;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);
        if (this.hasDownload && tooltipContext.registries() != null) {
            Optional<Integer> songDescLength = itemStack.get(DataComponents.JUKEBOX_PLAYABLE)
                    .song()
                    .unwrap(tooltipContext.registries())
                    .map(holder -> holder.value().description().getString().length());

            int length = songDescLength.orElse(20);

            List<MutableComponent> componentList = GeneralUtilsClient.autoWrappedTooltip(
                    length,
                    this.getDescriptionId() + ".download");

            componentList.forEach(component -> list.add(
                component.withStyle(tooltipFlag.isAdvanced() ? ChatFormatting.DARK_PURPLE : ChatFormatting.DARK_GRAY)
            ));
        }
    }
}
