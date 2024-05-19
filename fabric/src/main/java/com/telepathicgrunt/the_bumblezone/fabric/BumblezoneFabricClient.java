package com.telepathicgrunt.the_bumblezone.fabric;

import com.telepathicgrunt.the_bumblezone.client.BumblezoneClient;
import net.fabricmc.api.ClientModInitializer;

public class BumblezoneFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BumblezoneClient.init();
        FabricClientEventManager.init();
    }
}
