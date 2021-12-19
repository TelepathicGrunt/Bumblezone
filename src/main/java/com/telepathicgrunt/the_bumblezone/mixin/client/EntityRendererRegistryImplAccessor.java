package com.telepathicgrunt.the_bumblezone.mixin.client;

import net.fabricmc.fabric.impl.client.rendering.EntityRendererRegistryImpl;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.HashMap;

@Mixin(EntityRendererRegistryImpl.class)
public interface EntityRendererRegistryImplAccessor {
    @Accessor(value = "map", remap = false)
    static HashMap<EntityType<?>, EntityRendererProvider<?>> getMap() {
        throw new UnsupportedOperationException();
    }
}
