package com.telepathicgrunt.the_bumblezone.neoforge;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.BumblezoneClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = Bumblezone.MODID, dist = Dist.CLIENT)
public class BumblezoneNeoForgeClient {

    public BumblezoneNeoForgeClient(ModContainer container, IEventBus modEventBus) {
        BumblezoneClient.init();

        IEventBus eventBus = NeoForge.EVENT_BUS;
        NeoForgeClientEventManager.init(modEventBus, eventBus);

        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
 }
