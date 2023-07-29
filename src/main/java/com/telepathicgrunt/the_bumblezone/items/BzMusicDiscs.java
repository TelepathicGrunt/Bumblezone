package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.utils.GeneralUtilsClient;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BzMusicDiscs extends RecordItem {
    private final boolean hasDownload;

    public BzMusicDiscs(int comparatorOutput, SoundEvent sound, Properties settings, int musicTimeLength, boolean hasDownload) {
        super(comparatorOutput, sound, settings, musicTimeLength);
        this.hasDownload = hasDownload;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(this.getDisplayName().withStyle(ChatFormatting.GRAY));
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
