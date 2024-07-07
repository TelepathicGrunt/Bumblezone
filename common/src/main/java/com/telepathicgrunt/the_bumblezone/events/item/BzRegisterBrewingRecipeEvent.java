package com.telepathicgrunt.the_bumblezone.events.item;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import org.apache.logging.log4j.util.TriConsumer;

public record BzRegisterBrewingRecipeEvent(TriConsumer<Holder<Potion>, Item, Holder<Potion>> registrator) {

    public static final EventHandler<BzRegisterBrewingRecipeEvent> EVENT = new EventHandler<>();
}
