package com.telepathicgrunt.the_bumblezone.items;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.RecordItem;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class BzMusicDiscs extends RecordItem {
    private final IntSupplier timeSupplier;

    public BzMusicDiscs(int comparatorOutput, Supplier<SoundEvent> sound, Properties settings, IntSupplier musicTimeLength) {
        super(comparatorOutput, sound, settings, 0);

        this.timeSupplier = musicTimeLength;
    }

    public int getLengthInTicks() {
        return this.timeSupplier.getAsInt() * 20;
    }
}
