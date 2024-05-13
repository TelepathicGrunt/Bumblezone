package com.telepathicgrunt.the_bumblezone.neoforge;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.BumblezoneClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = Bumblezone.MODID, dist = Dist.CLIENT)
public class BumblezoneNeoForgeClient {

    public BumblezoneNeoForgeClient(IEventBus modEventBus, IEventBus eventBus) {
        BumblezoneClient.init();
        NeoForgeClientEventManager.init(modEventBus, eventBus);
    }
 }
