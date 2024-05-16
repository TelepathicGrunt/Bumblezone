package com.telepathicgrunt.the_bumblezone.events;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import org.apache.logging.log4j.util.TriConsumer;

public record RegisterBrewingRecipeEvent(TriConsumer<Holder<Potion>, Item, Holder<Potion>> registrator) {

    public static final EventHandler<RegisterBrewingRecipeEvent> EVENT = new EventHandler<>();
}
