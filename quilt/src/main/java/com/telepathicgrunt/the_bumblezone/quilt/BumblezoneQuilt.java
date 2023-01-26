package com.telepathicgrunt.the_bumblezone.quilt;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.fabricbase.BzConfig;
import com.telepathicgrunt.the_bumblezone.modcompat.quilt.QuiltModChecker;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class BumblezoneQuilt implements ModInitializer {

    @Override
    public void onInitialize(ModContainer mod) {
        BzConfig.setup();
        Bumblezone.init();

        QuiltEventManager.init();
        QuiltModChecker.setupModCompat();
    }
}
