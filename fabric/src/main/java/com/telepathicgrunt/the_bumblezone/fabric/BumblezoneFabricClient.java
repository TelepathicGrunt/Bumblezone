package com.telepathicgrunt.the_bumblezone.fabric;

import com.telepathicgrunt.the_bumblezone.client.BumblezoneClient;
import com.telepathicgrunt.the_bumblezone.modcompat.fabric.FabricModChecker;
import net.fabricmc.api.ClientModInitializer;

public class BumblezoneFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BumblezoneClient.init();
        FabricClientEventManager.init();

        // Mod compat has to run after all mod's main init.
        FabricModChecker.setupModCompat();
    }
}
