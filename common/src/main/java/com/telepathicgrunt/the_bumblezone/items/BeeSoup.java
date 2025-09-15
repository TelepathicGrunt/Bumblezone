package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.modinit.BzFoodAndConsumables;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class BeeSoup extends Item {
    public BeeSoup(Properties properties) {
        super(properties
                .craftRemainder(Items.BOWL)
                .usingConvertsTo(Items.BOWL)
                .food(BzFoodAndConsumables.BEE_SOUP_FOOD, BzFoodAndConsumables.BEE_BREAD_CONSUME));
    }

}