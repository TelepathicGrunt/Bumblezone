package net.telepathicgrunt.bumblezone.items;

import net.minecraft.item.Item;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;

public class BzMusicDiscs extends MusicDiscItem {
    public BzMusicDiscs(int comparatorOutput, SoundEvent sound, Item.Settings settings) {
        super(comparatorOutput, sound, settings);
    }
}
