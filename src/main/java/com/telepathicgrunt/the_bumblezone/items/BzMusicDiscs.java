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
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class BzMusicDiscs extends RecordItem {
    private final ForgeConfigSpec.IntValue timeSupplier;
    private final boolean hasDownload;

    public BzMusicDiscs(int comparatorOutput, Supplier<SoundEvent> sound, Properties settings, ForgeConfigSpec.IntValue musicTimeLength, boolean hasDownload) {
        super(comparatorOutput, sound, settings, 0);

        this.hasDownload = hasDownload;
        this.timeSupplier = musicTimeLength;
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

    public int getLengthInTicks() {
        return this.timeSupplier.get() * 20;
    }
}
