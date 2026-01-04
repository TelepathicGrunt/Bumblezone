package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.util.TriConsumer;

public record BzRegisterItemPropertiesEvent(TriConsumer<Item, Identifier, ClampedItemPropertyFunction> registrar) {

    public static final EventHandler<BzRegisterItemPropertiesEvent> EVENT = new EventHandler<>();

    public void register(Item item, Identifier id, ClampedItemPropertyFunction function) {
        registrar.accept(item, id, function);
    }
}
