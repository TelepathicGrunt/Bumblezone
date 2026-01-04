package com.telepathicgrunt.the_bumblezone.mixin.client;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(RenderStateShard.TextureStateShard.class)
public interface TextureStateShardAccessor {
    @Accessor("texture")
    Optional<Identifier> bumblezone$getTexture();
}
