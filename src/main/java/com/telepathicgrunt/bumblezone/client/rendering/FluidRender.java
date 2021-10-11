package com.telepathicgrunt.bumblezone.client.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class FluidRender {
    public static void setupFluidRendering(final Fluid still, final Fluid flowing, final ResourceLocation stillTextureFluidId, final ResourceLocation flowTextureFluidId, boolean waterColored)
    {
        // If they're not already present, add the sprites to the block atlas
        ClientSpriteRegistryCallback.event(TextureAtlas.LOCATION_BLOCKS).register((atlasTexture, registry) ->
        {
            registry.register(stillTextureFluidId);
            registry.register(flowTextureFluidId);
        });

        final ResourceLocation fluidId = Registry.FLUID.getKey(still);
        final ResourceLocation listenerId = new ResourceLocation(fluidId.getNamespace(), fluidId.getPath() + "_reload_listener");

        final TextureAtlasSprite[] fluidSprites = { null, null };

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener()
        {
            @Override
            public ResourceLocation getFabricId()
            {
                return listenerId;
            }

            /**
             * Get the sprites from the block atlas when resources are reloaded
             */
            @Override
            public void onResourceManagerReload(ResourceManager resourceManager)
            {
                final Function<ResourceLocation, TextureAtlasSprite> atlas = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS);
                fluidSprites[0] = atlas.apply(stillTextureFluidId);
                fluidSprites[1] = atlas.apply(flowTextureFluidId);
            }
        });

        // The FluidRenderer gets the sprites and color from a FluidRenderHandler during rendering
        final FluidRenderHandler renderHandler = new FluidRenderHandler()
        {
            @Override
            public TextureAtlasSprite[] getFluidSprites(BlockAndTintGetter view, BlockPos pos, FluidState state)
            {
                return fluidSprites;
            }

            @Override
            public int getFluidColor(BlockAndTintGetter view, BlockPos pos, FluidState state)
            {
                return waterColored && view != null && pos != null ? BiomeColors.getAverageWaterColor(view, pos) : -1;
            }
        };

        FluidRenderHandlerRegistry.INSTANCE.register(still, renderHandler);
        FluidRenderHandlerRegistry.INSTANCE.register(flowing, renderHandler);
    }
}
