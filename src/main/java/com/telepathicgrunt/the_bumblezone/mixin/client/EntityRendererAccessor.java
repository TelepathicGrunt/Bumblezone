package com.telepathicgrunt.the_bumblezone.mixin.client;

import net.minecraft.client.renderer.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityRenderer.class)
public interface EntityRendererAccessor {
    @Accessor("shadowRadius")
    float getShadowRadius();

    @Accessor("shadowStrength")
    float getShadowStrength();
}
