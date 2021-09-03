package com.telepathicgrunt.the_bumblezone.client;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.particles.PollenPuff;
import com.telepathicgrunt.the_bumblezone.client.rendering.FluidRender;
import com.telepathicgrunt.the_bumblezone.client.rendering.HoneySlimeRendering;
import com.telepathicgrunt.the_bumblezone.mixin.world.SkyPropertiesAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzSkyProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class BumblezoneClient
{
    public static void subscribeClientEvents()
    {
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(BumblezoneClient::onClientSetup);
        modEventBus.addListener(BumblezoneClient::onParticleSetup);
        forgeBus.addListener(FluidRender::sugarWaterOverlay);
    }

    //Deferred because I have been told RenderTypeLookup is not thread safe
    // CLIENT-SIDED
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        Minecraft minecraftClient = event.getMinecraftSupplier().get();

        SkyPropertiesAccessor.thebumblezone_getBY_ResourceLocation().put(new ResourceLocation(Bumblezone.MODID, "sky_property"), new BzSkyProperty());

        RenderingRegistry.registerEntityRenderingHandler(BzEntities.HONEY_SLIME.get(), HoneySlimeRendering::new);
        RenderingRegistry.registerEntityRenderingHandler(BzEntities.POLLEN_PUFF_ENTITY.get(), (entityRendererManager) -> new SpriteRenderer<>(entityRendererManager, minecraftClient.getItemRenderer()));

        //Replaced DeferredWorkQueue.runLater with the method added by the event - andrew
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(BzBlocks.STICKY_HONEY_REDSTONE.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(BzBlocks.STICKY_HONEY_RESIDUE.get(), RenderType.cutout());
            RenderTypeLookup.setRenderLayer(BzBlocks.HONEY_CRYSTAL.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(BzFluids.SUGAR_WATER_FLUID.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(BzFluids.SUGAR_WATER_FLUID_FLOWING.get(), RenderType.translucent());
            RenderTypeLookup.setRenderLayer(BzFluids.SUGAR_WATER_BLOCK.get(), RenderType.translucent());

            // Allows shield to use the blocking json file for offset
            ItemModelsProperties.register(
                    BzItems.HONEY_CRYSTAL_SHIELD.get(),
                    new ResourceLocation("blocking"),
                    (itemStack, world, livingEntity) ->
                            livingEntity != null &&
                                    livingEntity.isUsingItem() &&
                                    livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F
            );

        });
    }

    public static void onParticleSetup(ParticleFactoryRegisterEvent event)
    {
        Minecraft.getInstance().particleEngine.register(BzParticles.POLLEN.get(), PollenPuff.Factory::new);
    }
}