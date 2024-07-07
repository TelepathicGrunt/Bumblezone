package com.telepathicgrunt.the_bumblezone.client.neoforge;

import com.telepathicgrunt.the_bumblezone.client.armor.ArmorModelProvider;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterArmorProviderEvent;
import net.minecraft.world.item.Item;

public class NeoforgeArmorProviders {
    private static boolean initialized = false;

    public static void setupArmor() {
        if (initialized) return;
        initialized = true;
        BzRegisterArmorProviderEvent.EVENT.invoke(new BzRegisterArmorProviderEvent(ArmorModelProvider::register));
    }

    public static ArmorModelProvider get(Item item) {
        setupArmor();
        return ArmorModelProvider.get(item);
    }
}
