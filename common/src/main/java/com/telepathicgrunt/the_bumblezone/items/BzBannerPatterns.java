package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.modinit.BzFoodAndConsumables;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;

public class BzBannerPatterns extends Item {
    public BzBannerPatterns(Properties properties) {
        super(properties
                .stacksTo(1)
                .rarity(Rarity.UNCOMMON));
    }

}