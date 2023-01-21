package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.utils.SuppliedMap;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.RecordItem;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class BzMusicDiscs extends RecordItem {

    private static final SuppliedMap<SoundEvent, RecordItem> MUSIC_DISCS = new SuppliedMap<>(new IdentityHashMap<>(), new HashMap<>());

    private final Supplier<SoundEvent> sound;
    private final IntSupplier timeSupplier;

    public BzMusicDiscs(int comparatorOutput, Supplier<SoundEvent> sound, Properties settings, IntSupplier musicTimeLength) {
        super(comparatorOutput, SoundEvents.ITEM_PICKUP, settings, 0); // This uses the pickup sound as a placeholder
        this.sound = sound;
        this.timeSupplier = musicTimeLength;
        MUSIC_DISCS.put(sound, () -> this);
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
