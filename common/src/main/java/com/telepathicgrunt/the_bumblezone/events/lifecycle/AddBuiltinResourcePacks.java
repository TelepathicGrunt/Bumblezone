package com.telepathicgrunt.the_bumblezone.events.lifecycle;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record AddBuiltinResourcePacks(Registrar registrar) {

    public static final EventHandler<AddBuiltinResourcePacks> EVENT = new EventHandler<>();

    public void add(ResourceLocation id, Component displayName, PackMode mode) {
        registrar.register(id, displayName, mode);
    }


    @FunctionalInterface
    public interface Registrar {
        void register(ResourceLocation id, Component displayName, PackMode mode);
    }

    public enum PackMode {
        USER_CONTROLLED,
        ENABLED_BY_DEFAULT,
        FORCE_ENABLED
    }
}
