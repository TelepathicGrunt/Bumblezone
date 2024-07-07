package com.telepathicgrunt.the_bumblezone.events.client;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public record BzRegisterShaderEvent(Registrar registrar) {

    public static final EventHandler<BzRegisterShaderEvent> EVENT = new EventHandler<>();

    public void register(ResourceLocation name, VertexFormat vertexFormat, Consumer<ShaderInstance> onLoaded) {
        registrar.register(name, vertexFormat, onLoaded);
    }

    @FunctionalInterface
    public interface Registrar {
        void register(ResourceLocation name, VertexFormat vertexFormat, Consumer<ShaderInstance> onLoaded);
    }
}
