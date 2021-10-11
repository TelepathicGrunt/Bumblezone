package com.telepathicgrunt.bumblezone.items;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.RecordItem;

public class BzMusicDiscs extends RecordItem {
    public BzMusicDiscs(int comparatorOutput, SoundEvent sound, Item.Properties settings) {
        super(comparatorOutput, sound, settings);
    }
}
