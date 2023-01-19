package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.client.armor.ArmorModelProvider;
import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.item.Item;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public record RegisterArmorProviderEvent(BiConsumer<Item, ArmorModelProvider> registrar) {

    public static final EventHandler<RegisterArmorProviderEvent> EVENT = new EventHandler<>();

    public void register(Item item, Supplier<ArmorModelProvider> provider) {
        registrar.accept(item, provider.get());
    }

    public void register(Item item, ArmorModelProvider provider) {
        registrar.accept(item, provider);
    }
}
