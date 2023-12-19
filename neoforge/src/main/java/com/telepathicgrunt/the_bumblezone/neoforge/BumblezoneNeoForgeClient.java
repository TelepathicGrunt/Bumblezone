package com.telepathicgrunt.the_bumblezone.neoforge;

import com.telepathicgrunt.the_bumblezone.client.BumblezoneClient;
import net.neoforged.bus.api.IEventBus;

public class BumblezoneNeoForgeClient {
    public static void init(IEventBus modEventBus, IEventBus eventBus) {
        BumblezoneClient.init();
        NeoForgeClientEventManager.init(modEventBus, eventBus);
    }
 }
