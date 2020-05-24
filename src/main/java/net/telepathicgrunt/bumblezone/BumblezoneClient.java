package net.telepathicgrunt.bumblezone;

import net.fabricmc.api.ClientModInitializer;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;


@SuppressWarnings("deprecation")
public class BumblezoneClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BzBlocks.registerRenderLayers();
    }
}