package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.utils.SuppliedMap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class BzMusicDiscs extends RecordItem {

    private static final SuppliedMap<SoundEvent, RecordItem> MUSIC_DISCS = new SuppliedMap<>(new IdentityHashMap<>(), new HashMap<>());

    private final Supplier<SoundEvent> sound;
    private final IntSupplier timeSupplier;
    private final boolean hasDownload;

    public BzMusicDiscs(int comparatorOutput, Supplier<SoundEvent> sound, Properties settings, IntSupplier musicTimeLength, boolean hasDownload) {
        super(comparatorOutput, SoundEvents.ITEM_PICKUP, settings, 0); // This uses the pickup sound as a placeholder
        this.sound = sound;
        this.timeSupplier = musicTimeLength;
        this.hasDownload = hasDownload;
        MUSIC_DISCS.put(sound, () -> this);
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

    @Override
    public SoundEvent getSound() {
        return this.sound.get();
    }

    @Override
    public int getLengthInTicks() {
        return this.timeSupplier.getAsInt() * 20;
    }

    public static RecordItem get(SoundEvent sound) {
        return MUSIC_DISCS.get(sound);
    }
}
