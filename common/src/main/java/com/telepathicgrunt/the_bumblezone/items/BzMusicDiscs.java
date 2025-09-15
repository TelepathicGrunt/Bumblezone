package com.telepathicgrunt.the_bumblezone.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class BzMusicDiscs extends Item {
    public BzMusicDiscs(Properties properties) {
        super(properties
                .stacksTo(1)
                .rarity(Rarity.RARE));
    }

}