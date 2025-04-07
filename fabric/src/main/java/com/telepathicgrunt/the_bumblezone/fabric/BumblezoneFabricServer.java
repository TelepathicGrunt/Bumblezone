package com.telepathicgrunt.the_bumblezone.fabric;

import com.telepathicgrunt.the_bumblezone.modcompat.fabric.FabricModChecker;
import net.fabricmc.api.DedicatedServerModInitializer;

public class BumblezoneFabricServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        // Mod compat has to run after all mod's main init.
        FabricModChecker.setupModCompat();
    }
}
