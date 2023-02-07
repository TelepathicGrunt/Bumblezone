package com.telepathicgrunt.the_bumblezone.client.forge;

import com.telepathicgrunt.the_bumblezone.client.armor.ArmorModelProvider;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterArmorProviderEvent;
import net.minecraft.world.item.Item;

public class ForgeArmorProviders {
    private static boolean initialized = false;

    public static void setupArmor() {
        if (initialized) return;
        initialized = true;
        RegisterArmorProviderEvent.EVENT.invoke(new RegisterArmorProviderEvent(ArmorModelProvider::register));
    }

    public static ArmorModelProvider get(Item item) {
        setupArmor();
        return ArmorModelProvider.get(item);
    }
}
