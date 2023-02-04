package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.client.rendering.MobEffectRenderer;
import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.world.effect.MobEffect;

public record RegisterEffectRenderersEvent() {

    public static final RegisterEffectRenderersEvent INSTANCE = new RegisterEffectRenderersEvent();
    public static final EventHandler<RegisterEffectRenderersEvent> EVENT = new EventHandler<>();

    public void register(MobEffect effect, MobEffectRenderer renderer) {
        MobEffectRenderer.RENDERERS.put(effect, renderer);
    }
}
