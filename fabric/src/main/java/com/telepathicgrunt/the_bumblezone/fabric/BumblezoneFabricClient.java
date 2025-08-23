package com.telepathicgrunt.the_bumblezone.fabric;

import com.telepathicgrunt.the_bumblezone.client.BumblezoneClient;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.fabricbase.FabricClientBaseEventManager;
import com.telepathicgrunt.the_bumblezone.items.StinglessBeeHelmet;
import com.telepathicgrunt.the_bumblezone.modcompat.fabric.FabricModChecker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class BumblezoneFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BumblezoneClient.init();
        FabricClientBaseEventManager.init();
        ClientTickEvents.END_CLIENT_TICK.register((mc) -> StinglessBeeHelmet.decrementHighlightingCounter(GeneralUtilsClient.getClientPlayer()));

        // Mod compat has to run after all mod's main init.
        FabricModChecker.setupModCompat();
    }
}
