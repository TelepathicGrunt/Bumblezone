package com.telepathicgrunt.the_bumblezone.forge;

import com.telepathicgrunt.the_bumblezone.client.BumblezoneClient;
import com.telepathicgrunt.the_bumblezone.events.client.BlockRenderedOnScreenEvent;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterEffectRenderersEvent;
import net.minecraftforge.client.event.RenderBlockScreenEffectEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class BumblezoneForgeClient {


    public static void init() {
        BumblezoneClient.init();

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        forgeBus.addListener(BumblezoneForgeClient::onBlockScreen);
        modBus.addListener(BumblezoneForgeClient::onClientSetup);
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        RegisterEffectRenderersEvent.EVENT.invoke(RegisterEffectRenderersEvent.INSTANCE);
    }

    public static void onBlockScreen(RenderBlockScreenEffectEvent event) {
        BlockRenderedOnScreenEvent.Type type = switch (event.getOverlayType()) {
            case BLOCK -> BlockRenderedOnScreenEvent.Type.BLOCK;
            case FIRE -> BlockRenderedOnScreenEvent.Type.FIRE;
            case WATER -> BlockRenderedOnScreenEvent.Type.WATER;
        };
        BlockRenderedOnScreenEvent.EVENT.invoke(new BlockRenderedOnScreenEvent(
                event.getPlayer(), event.getPoseStack(), type, event.getBlockState(), event.getBlockPos()));
    }
}
