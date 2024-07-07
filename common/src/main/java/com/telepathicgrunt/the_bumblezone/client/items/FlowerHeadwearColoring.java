package com.telepathicgrunt.the_bumblezone.client.items;

import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterItemColorEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.world.item.component.DyedItemColor;

public class FlowerHeadwearColoring {
    public static final int DEFAULT_COLOR = 0xFFE65439;

    public static void registerItemColors(BzRegisterItemColorEvent event) {
        event.register(
            (stack, tintIndex) -> DyedItemColor.getOrDefault(stack, DEFAULT_COLOR),
            BzItems.FLOWER_HEADWEAR.get()
        );
    }
}
