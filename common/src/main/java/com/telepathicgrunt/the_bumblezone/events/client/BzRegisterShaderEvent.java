package com.telepathicgrunt.the_bumblezone.events.client;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.Identifier;

import java.util.function.Consumer;

public record BzRegisterShaderEvent(Registrar registrar) {

    public static final EventHandler<BzRegisterShaderEvent> EVENT = new EventHandler<>();

    public void register(Identifier name, VertexFormat vertexFormat, Consumer<ShaderInstance> onLoaded) {
        registrar.register(name, vertexFormat, onLoaded);
    }

    @FunctionalInterface
    public interface Registrar {
        void register(Identifier name, VertexFormat vertexFormat, Consumer<ShaderInstance> onLoaded);
    }
}
