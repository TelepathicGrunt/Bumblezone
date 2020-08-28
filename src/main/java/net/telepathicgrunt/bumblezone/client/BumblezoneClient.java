package net.telepathicgrunt.bumblezone.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.ClientEvents;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.client.rendering.FluidRender;
import net.telepathicgrunt.bumblezone.client.rendering.HoneySlimeRendering;
import net.telepathicgrunt.bumblezone.dimension.BzSkyProperty;
import net.telepathicgrunt.bumblezone.entities.BzEntities;
import net.telepathicgrunt.bumblezone.mixin.SkyPropertiesAccessor;

@SuppressWarnings("deprecation")
public class BumblezoneClient
{
    public static final ResourceLocation FLUID_STILL = new ResourceLocation(Bumblezone.MODID + ":block/sugar_water_still");
    public static final ResourceLocation FLUID_FLOWING = new ResourceLocation(Bumblezone.MODID + ":block/sugar_water_flow");

    public static void subscribeClientEvents(IEventBus modBus, IEventBus forgeBus)
    {
        modBus.addListener(BumblezoneClient::onClientSetup);
        forgeBus.addListener(FluidRender::sugarWaterOverlay);
    }


    //Deferred because I have been told RenderTypeLookup is not thread safe
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        RenderingRegistry.registerEntityRenderingHandler(BzEntities.HONEY_SLIME, HoneySlimeRendering::new);
        SkyPropertiesAccessor.getBY_ResourceLocation().put(new ResourceLocation(Bumblezone.MODID, "sky_property"), new BzSkyProperty());

        FluidRender.setupFluidRendering(BzBlocks.SUGAR_WATER_FLUID, BzBlocks.SUGAR_WATER_FLUID_FLOWING, FLUID_STILL, FLUID_FLOWING);
        DeferredWorkQueue.runLater(() -> {
            RenderTypeLookup.setRenderLayer(BzBlocks.STICKY_HONEY_REDSTONE, RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(BzBlocks.STICKY_HONEY_RESIDUE, RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(BzBlocks.HONEY_CRYSTAL, RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(BzBlocks.SUGAR_WATER_BLOCK, RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(BzBlocks.SUGAR_WATER_FLUID, RenderType.getTranslucent());
            RenderTypeLookup.setRenderLayer(BzBlocks.SUGAR_WATER_FLUID_FLOWING, RenderType.getTranslucent());
        });
    }
}