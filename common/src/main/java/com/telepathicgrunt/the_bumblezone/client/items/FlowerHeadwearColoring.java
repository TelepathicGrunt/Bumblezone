package com.telepathicgrunt.the_bumblezone.client.items;

import com.telepathicgrunt.the_bumblezone.events.client.RegisterItemColorEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.world.item.DyeableLeatherItem;

public class FlowerHeadwearColoring {
    public static final int DEFAULT_COLOR = 0xE65439;

    public static void registerItemColors(RegisterItemColorEvent event) {
        event.register(
            (stack, tintIndex) -> ((DyeableLeatherItem)stack.getItem()).hasCustomColor(stack) ? ((DyeableLeatherItem)stack.getItem()).getColor(stack) : DEFAULT_COLOR,
            BzItems.FLOWER_HEADWEAR.get()
        );
    }
}
