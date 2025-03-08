package com.telepathicgrunt.the_bumblezone.mixin;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.texture.TextureAtlas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ParticleEngine.class)
public interface ParticleEngineAccessor {
    @Accessor
    TextureAtlas getTextureAtlas();
}
