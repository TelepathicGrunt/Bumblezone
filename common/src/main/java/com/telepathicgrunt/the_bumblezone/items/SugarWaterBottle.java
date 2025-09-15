package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.modinit.BzFoodAndConsumables;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class SugarWaterBottle extends Item {

    public SugarWaterBottle(Item.Properties properties) {
        super(properties
                .stacksTo(16)
                .craftRemainder(Items.GLASS_BOTTLE)
                .usingConvertsTo(Items.GLASS_BOTTLE)
                .food(BzFoodAndConsumables.SUGAR_WATER_FOOD, BzFoodAndConsumables.SUGAR_WATER_CONSUME));
    }

}