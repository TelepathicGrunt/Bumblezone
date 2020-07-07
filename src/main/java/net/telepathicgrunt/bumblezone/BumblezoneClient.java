package net.telepathicgrunt.bumblezone;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockRenderView;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;

import java.util.function.Function;


@SuppressWarnings("deprecation")
public class BumblezoneClient implements ClientModInitializer {

    //fluid mess
    public static final Identifier FLUID_STILL = new Identifier(Bumblezone.MODID + ":block/sugar_water_still");
    public static final Identifier FLUID_FLOWING = new Identifier(Bumblezone.MODID + ":block/sugar_water_flow");
    public static final Identifier FLUID_OVERLAY = new Identifier(Bumblezone.MODID + ":block/sugar_water_overlay");

    @Override
    public void onInitializeClient() {
        setupFluidRendering(BzBlocks.SUGAR_WATER_FLUID, BzBlocks.SUGAR_WATER_FLUID_FLOWING, FLUID_STILL, FLUID_FLOWING, 0x0059FF);
        BzBlocks.registerRenderLayers();
    }



    public static void setupFluidRendering(final Fluid still, final Fluid flowing, final Identifier stillTextureFluidId,  final Identifier flowTextureFluidId,final int color)
    {
        // If they're not already present, add the sprites to the block atlas
        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEX).register((atlasTexture, registry) ->
        {
            registry.register(stillTextureFluidId);
            registry.register(flowTextureFluidId);
        });

        final Identifier fluidId = Registry.FLUID.getId(still);
        final Identifier listenerId = new Identifier(fluidId.getNamespace(), fluidId.getPath() + "_reload_listener");

        final Sprite[] fluidSprites = { null, null };

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener()
        {
            @Override
            public Identifier getFabricId()
            {
                return listenerId;
            }

            /**
             * Get the sprites from the block atlas when resources are reloaded
             */
            @Override
            public void apply(ResourceManager resourceManager)
            {
                final Function<Identifier, Sprite> atlas = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
                fluidSprites[0] = atlas.apply(stillTextureFluidId);
                fluidSprites[1] = atlas.apply(flowTextureFluidId);
            }
        });

        // The FluidRenderer gets the sprites and color from a FluidRenderHandler during rendering
        final FluidRenderHandler renderHandler = new FluidRenderHandler()
        {
            @Override
            public Sprite[] getFluidSprites(BlockRenderView view, BlockPos pos, FluidState state)
            {
                return fluidSprites;
            }

            @Override
            public int getFluidColor(BlockRenderView view, BlockPos pos, FluidState state)
            {
                return view != null && pos != null ? BiomeColors.getWaterColor(view, pos) : -1;
            }
        };

        FluidRenderHandlerRegistry.INSTANCE.register(still, renderHandler);
        FluidRenderHandlerRegistry.INSTANCE.register(flowing, renderHandler);
    }
}