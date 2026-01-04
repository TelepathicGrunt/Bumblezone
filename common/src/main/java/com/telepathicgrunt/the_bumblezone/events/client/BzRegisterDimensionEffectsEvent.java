package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.Identifier;

import java.util.function.BiConsumer;

public record BzRegisterDimensionEffectsEvent(BiConsumer<Identifier, DimensionSpecialEffects> effects) {

    public static final EventHandler<BzRegisterDimensionEffectsEvent> EVENT = new EventHandler<>();

    public void register(Identifier dimension, DimensionSpecialEffects effect) {
        effects.accept(dimension, effect);
    }
}
