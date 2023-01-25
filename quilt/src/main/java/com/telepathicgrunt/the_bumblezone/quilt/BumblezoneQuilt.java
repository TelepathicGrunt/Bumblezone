package com.telepathicgrunt.the_bumblezone.quilt;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.AddBuiltinResourcePacks;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.RegisterDataSerializersEvent;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.entity.networking.api.tracked_data.QuiltTrackedDataHandlerRegistry;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType;

public class BumblezoneQuilt implements ModInitializer {

    @Override
    public void onInitialize(ModContainer mod) {
        Bumblezone.init();

        QuiltEventManager.init();

        AddBuiltinResourcePacks.EVENT.invoke(new AddBuiltinResourcePacks((id, displayName, mode) ->
                ResourceLoader.registerBuiltinResourcePack(
                        id,
                        QuiltLoader.getModContainer(id.getNamespace()).orElseThrow(),
                        toType(mode),
                        displayName
                )
        ));

        RegisterDataSerializersEvent.EVENT.invoke(new RegisterDataSerializersEvent(QuiltTrackedDataHandlerRegistry::register));
    }

    private static ResourcePackActivationType toType(AddBuiltinResourcePacks.PackMode mode) {
        return switch (mode) {
            case USER_CONTROLLED -> ResourcePackActivationType.NORMAL;
            case ENABLED_BY_DEFAULT -> ResourcePackActivationType.DEFAULT_ENABLED;
            case FORCE_ENABLED -> ResourcePackActivationType.ALWAYS_ENABLED;
        };
    }
}
