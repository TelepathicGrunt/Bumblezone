package com.telepathicgrunt.the_bumblezone.fabric;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.fabric.BzConfig;
import com.telepathicgrunt.the_bumblezone.modcompat.fabric.FabricModChecker;
import com.telepathicgrunt.the_bumblezone.modules.fabric.FabricModuleInitializer;
import net.fabricmc.api.ModInitializer;

public class BumblezoneFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        BzConfig.setup();
        Bumblezone.init();

        FabricEventManager.init();
        FabricModChecker.setupModCompat();
        FabricEventManager.lateInit();
        FabricModuleInitializer.init();
    }
}
