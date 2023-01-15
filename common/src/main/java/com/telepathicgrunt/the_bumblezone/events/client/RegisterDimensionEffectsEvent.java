package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record RegisterDimensionEffectsEvent(Map<ResourceLocation, DimensionSpecialEffects> effects) {

    public static final EventHandler<RegisterDimensionEffectsEvent> EVENT = new EventHandler<>();

    public void register(ResourceLocation dimension, DimensionSpecialEffects effect) {
        effects.put(dimension, effect);
    }
}
