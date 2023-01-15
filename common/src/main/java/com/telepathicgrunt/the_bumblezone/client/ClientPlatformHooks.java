package com.telepathicgrunt.the_bumblezone.client;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.Nullable;

public class ClientPlatformHooks {

    @ExpectPlatform
    public static Fluid getFluidInEyes(Entity entity) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static void registerRenderType(RenderType type, Block... blocks) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static void registerRenderType(RenderType type, Fluid... fluids) {
        throw new NotImplementedException();
    }
}
