package com.telepathicgrunt.the_bumblezone.mixin.client;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(RenderPipelines.class)
public interface RenderPipelinesAccessor {
    @Accessor("PIPELINES_BY_LOCATION")
    static Map<Identifier, RenderPipeline> bumblezone$getPIPELINES_BY_LOCATION() {
        throw new UnsupportedOperationException();
    }
}
