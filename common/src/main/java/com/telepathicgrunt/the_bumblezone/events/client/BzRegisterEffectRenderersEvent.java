package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.client.rendering.MobEffectRenderer;
import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

public record BzRegisterEffectRenderersEvent() {

    public static final BzRegisterEffectRenderersEvent INSTANCE = new BzRegisterEffectRenderersEvent();
    public static final EventHandler<BzRegisterEffectRenderersEvent> EVENT = new EventHandler<>();

    public void register(Holder<MobEffect> effect, MobEffectRenderer renderer) {
        MobEffectRenderer.RENDERERS.put(effect, renderer);
    }
}
