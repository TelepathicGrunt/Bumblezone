package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public record BzAddBuiltinDataPacks(Registrar registrar) {

    public static final EventHandler<BzAddBuiltinDataPacks> EVENT = new EventHandler<>();

    public void add(Identifier id, Component displayName, PackMode mode) {
        registrar.register(id, displayName, mode);
    }


    @FunctionalInterface
    public interface Registrar {
        void register(Identifier id, Component displayName, PackMode mode);
    }

    public enum PackMode {
        USER_CONTROLLED,
        ENABLED_BY_DEFAULT,
        FORCE_ENABLED
    }
}
