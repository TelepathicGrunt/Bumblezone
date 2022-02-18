package com.telepathicgrunt.the_bumblezone.client;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.particles.HoneyParticle;
import com.telepathicgrunt.the_bumblezone.client.particles.PollenPuff;
import com.telepathicgrunt.the_bumblezone.client.rendering.BeeArmorModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.BeeVariantRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.BeehemothModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.BeehemothRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.FluidClientOverlay;
import com.telepathicgrunt.the_bumblezone.client.rendering.HoneySlimeRendering;
import com.telepathicgrunt.the_bumblezone.client.rendering.PileOfPollenRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.StingerSpearModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.StingerSpearRenderer;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.mixin.client.DimensionSpecialEffectsAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.client.RenderingRegistryAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.screens.StrictChestScreen;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzSkyProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class BumblezoneClient {

    public static void subscribeClientEvents() {
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(BumblezoneClient::onClientSetup);
        modEventBus.addListener(BumblezoneClient::onParticleSetup);
        modEventBus.addListener(BumblezoneClient::registerEntityRenderers);
        modEventBus.addListener(BumblezoneClient::registerEntityModels);
        forgeBus.addListener(FluidClientOverlay::sugarWaterFluidOverlay);
        forgeBus.addListener(FluidClientOverlay::renderHoneyFog);
        forgeBus.addListener(PileOfPollenRenderer::pileOfPollenOverlay);
        forgeBus.addListener(BeehemothControls::keyInput);

        ClientRegistry.registerKeyBinding(BeehemothControls.KEY_BIND_BEEHEMOTH_UP);
        ClientRegistry.registerKeyBinding(BeehemothControls.KEY_BIND_BEEHEMOTH_DOWN);
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        //enqueueWork because I have been told RenderTypeLookup is not thread safe
        event.enqueueWork(() -> {
            DimensionSpecialEffectsAccessor.thebumblezone_getBY_ResourceLocation().put(new ResourceLocation(Bumblezone.MODID, "sky_property"), new BzSkyProperty());
            registerRenderLayers();

            if(BzClientConfigs.enableLgbtBeeRenderer.get()) {
                //noinspection unchecked cast
                BeeVariantRenderer.OLD_BEE_RENDER_FACTORY = (EntityRendererProvider<Bee>)RenderingRegistryAccessor.getEntityRenderers().get(EntityType.BEE);
                EntityRenderers.register(EntityType.BEE, BeeVariantRenderer::new);
            }

            // Allows shield to use the blocking json file for offset
            ItemProperties.register(
                    BzItems.HONEY_CRYSTAL_SHIELD.get(),
                    new ResourceLocation("blocking"),
                    (itemStack, world, livingEntity, integer) ->
                            livingEntity != null &&
                                    livingEntity.isUsingItem() &&
                                    livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F
            );

            ItemProperties.register(
                    BzItems.STINGER_SPEAR.get(),
                    new ResourceLocation("throwing"),
                    (itemStack, world, livingEntity, integer) ->
                            livingEntity != null &&
                                    livingEntity.isUsingItem() &&
                                    livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F
            );

            MenuScreens.register(BzMenuTypes.STRICT_9x1.get(), StrictChestScreen::new);
            MenuScreens.register(BzMenuTypes.STRICT_9x2.get(), StrictChestScreen::new);
            MenuScreens.register(BzMenuTypes.STRICT_9x3.get(), StrictChestScreen::new);
            MenuScreens.register(BzMenuTypes.STRICT_9x4.get(), StrictChestScreen::new);
            MenuScreens.register(BzMenuTypes.STRICT_9x5.get(), StrictChestScreen::new);
            MenuScreens.register(BzMenuTypes.STRICT_9x6.get(), StrictChestScreen::new);
        });
    }

    private static void registerRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer(BzBlocks.STICKY_HONEY_REDSTONE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BzBlocks.STICKY_HONEY_RESIDUE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BzBlocks.HONEY_WEB.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BzBlocks.REDSTONE_HONEY_WEB.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BzBlocks.HONEY_CRYSTAL.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BzFluids.SUGAR_WATER_FLUID.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BzFluids.SUGAR_WATER_FLUID_FLOWING.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BzFluids.SUGAR_WATER_BLOCK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BzFluids.HONEY_FLUID.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BzFluids.HONEY_FLUID_FLOWING.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BzFluids.HONEY_FLUID_BLOCK.get(), RenderType.translucent());
    }

    public static void registerEntityModels(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BeehemothModel.LAYER_LOCATION, BeehemothModel::createBodyLayer);
        event.registerLayerDefinition(StingerSpearModel.LAYER_LOCATION, StingerSpearModel::createLayer);
        event.registerLayerDefinition(BeeArmorModel.VARIANT_1_LAYER_LOCATION, BeeArmorModel::createVariant1);
        event.registerLayerDefinition(BeeArmorModel.VARIANT_2_LAYER_LOCATION, BeeArmorModel::createVariant2);
    }

    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers.RegisterRenderers event) {
        EntityRenderers.register(BzEntities.HONEY_SLIME.get(), HoneySlimeRendering::new);
        EntityRenderers.register(BzEntities.BEEHEMOTH.get(), BeehemothRenderer::new);
        EntityRenderers.register(BzEntities.POLLEN_PUFF_ENTITY.get(), ThrownItemRenderer::new);
        EntityRenderers.register(BzEntities.THROWN_STINGER_SPEAR_ENTITY.get(), StingerSpearRenderer::new);
    }

    public static void onParticleSetup(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(BzParticles.POLLEN.get(), PollenPuff.Factory::new);
        Minecraft.getInstance().particleEngine.register(BzParticles.HONEY_PARTICLE.get(), HoneyParticle.Factory::new);
    }
}