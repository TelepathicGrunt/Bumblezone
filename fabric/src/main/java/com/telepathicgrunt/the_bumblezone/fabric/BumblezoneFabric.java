package com.telepathicgrunt.the_bumblezone.fabric;

import com.telepathicgrunt.the_bumblezone.events.lifecycle.AddBuiltinResourcePacks;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;

public class BumblezoneFabric implements ModInitializer {

    @Override
    public void onInitialize() {


        AddBuiltinResourcePacks.EVENT.invoke(new AddBuiltinResourcePacks((id, displayName, mode) ->
                ResourceManagerHelper.registerBuiltinResourcePack(
                        id,
                        FabricLoader.getInstance().getModContainer(id.getNamespace()).orElseThrow(),
                        displayName,
                        toType(mode)
                ))
        );
    }

    private static ResourcePackActivationType toType(AddBuiltinResourcePacks.PackMode mode) {
        return switch (mode) {
            case USER_CONTROLLED -> ResourcePackActivationType.NORMAL;
            case ENABLED_BY_DEFAULT -> ResourcePackActivationType.DEFAULT_ENABLED;
            case FORCE_ENABLED -> ResourcePackActivationType.ALWAYS_ENABLED;
        };
    }
}
