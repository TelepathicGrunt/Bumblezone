package com.telepathicgrunt.the_bumblezone.items;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Supplier;

public class BzMusicDiscs extends RecordItem {
    private final ForgeConfigSpec.IntValue timeSupplier;

    public BzMusicDiscs(int comparatorOutput, Supplier<SoundEvent> sound, Properties settings, ForgeConfigSpec.IntValue musicTimeLength) {
        super(comparatorOutput, sound, settings, 0);

        this.timeSupplier = musicTimeLength;
    }

    public int getLengthInTicks() {
        return this.timeSupplier.get() * 20;
    }
}
