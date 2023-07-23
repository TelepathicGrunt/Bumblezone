package com.telepathicgrunt.the_bumblezone.events.client;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;
import java.util.function.Function;

public record RegisterShaderEvent(Registrar registrar) {

    public static final EventHandler<RegisterShaderEvent> EVENT = new EventHandler<>();

    public void register(ResourceLocation name, VertexFormat vertexFormat, Consumer<ShaderInstance> onLoaded) {
        registrar.register(name, vertexFormat, onLoaded);
    }

    @FunctionalInterface
    public interface Registrar {
        void register(ResourceLocation name, VertexFormat vertexFormat, Consumer<ShaderInstance> onLoaded);
    }
}
