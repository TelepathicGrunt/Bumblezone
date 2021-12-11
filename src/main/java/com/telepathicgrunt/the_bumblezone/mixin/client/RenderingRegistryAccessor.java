package com.telepathicgrunt.the_bumblezone.mixin.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(EntityRenderers.class)
public interface RenderingRegistryAccessor {
    @Accessor(value = "PROVIDERS", remap = false)
    static Map<EntityType<? extends Entity>, EntityRendererProvider<? extends Entity>> getEntityRenderers() {
        throw new RuntimeException();
    }
}
