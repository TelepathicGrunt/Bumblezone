package com.telepathicgrunt.the_bumblezone.fabric;

import com.telepathicgrunt.the_bumblezone.client.BumblezoneClient;
import com.telepathicgrunt.the_bumblezone.events.client.ClientTickEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class BumblezoneFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BumblezoneClient.init();
        FabricClientEventManager.init();
    }
}
