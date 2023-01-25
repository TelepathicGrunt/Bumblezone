package com.telepathicgrunt.the_bumblezone.quilt;

import com.telepathicgrunt.the_bumblezone.client.BumblezoneClient;
import com.telepathicgrunt.the_bumblezone.events.client.ClientTickEvent;
import com.telepathicgrunt.the_bumblezone.fabricbase.FabricClientBaseEventManager;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;

public class BumblezoneQuiltClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        BumblezoneClient.init();
        FabricClientBaseEventManager.init();
        ClientTickEvents.START.register((mc) -> ClientTickEvent.EVENT.invoke(ClientTickEvent.START));
        ClientTickEvents.END.register((mc) -> ClientTickEvent.EVENT.invoke(ClientTickEvent.END));
    }

}
