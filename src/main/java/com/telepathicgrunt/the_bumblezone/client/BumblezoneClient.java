package com.telepathicgrunt.the_bumblezone.client;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.bakedmodel.EmptyHoneycombBroodBlockModel;
import com.telepathicgrunt.the_bumblezone.client.bakedmodel.PorousHoneycombBlockModel;
import com.telepathicgrunt.the_bumblezone.client.items.HoneyCompassItemProperty;
import com.telepathicgrunt.the_bumblezone.client.items.IncenseCandleColoring;
import com.telepathicgrunt.the_bumblezone.client.particles.HoneyParticle;
import com.telepathicgrunt.the_bumblezone.client.particles.PollenPuffParticle;
import com.telepathicgrunt.the_bumblezone.client.particles.RoyalJellyParticle;
import com.telepathicgrunt.the_bumblezone.client.particles.SparkleParticle;
import com.telepathicgrunt.the_bumblezone.client.rendering.beearmor.BeeArmorModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.beehemoth.BeehemothModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.beehemoth.BeehemothRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.beequeen.BeeQueenModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.beequeen.BeeQueenRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.beestinger.BeeStingerModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.beestinger.BeeStingerRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.honeycrystalshard.HoneyCrystalShardModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.honeycrystalshard.HoneyCrystalShardRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.honeyslime.HoneySlimeRendering;
import com.telepathicgrunt.the_bumblezone.client.rendering.pileofpollen.PileOfPollenRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.stingerspear.StingerSpearModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.stingerspear.StingerSpearRenderer;
import com.telepathicgrunt.the_bumblezone.items.BeeCannon;
import com.telepathicgrunt.the_bumblezone.items.CrystalCannon;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.screens.CrystallineFlowerScreen;
import com.telepathicgrunt.the_bumblezone.screens.StrictChestScreen;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzSkyProperty;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
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
        modEventBus.addListener(BumblezoneClient::registerKeyBinding);
        modEventBus.addListener(BumblezoneClient::registerDimensionSpecialEffects);
        modEventBus.addListener(IncenseCandleColoring::registerBlockColors);
        modEventBus.addListener(IncenseCandleColoring::registerItemColors);
        modEventBus.addListener(PorousHoneycombBlockModel::registerModelLoaders);
        modEventBus.addListener(PorousHoneycombBlockModel::onBakingCompleted);
        modEventBus.addListener(EmptyHoneycombBroodBlockModel::registerModelLoaders);
        modEventBus.addListener(EmptyHoneycombBroodBlockModel::onBakingCompleted);

        forgeBus.addListener(PileOfPollenRenderer::pileOfPollenOverlay);
        forgeBus.addListener(BeehemothControls::keyInput);

    }

    public static void registerKeyBinding(RegisterKeyMappingsEvent event) {
        event.register(BeehemothControls.KEY_BIND_BEEHEMOTH_UP);
        event.register(BeehemothControls.KEY_BIND_BEEHEMOTH_DOWN);
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            registerRenderLayers();
            registerItemProperties();
            registerScreens();
        });
    }

    private static void registerScreens() {
        MenuScreens.register(BzMenuTypes.STRICT_9x1.get(), StrictChestScreen::new);
        MenuScreens.register(BzMenuTypes.STRICT_9x2.get(), StrictChestScreen::new);
        MenuScreens.register(BzMenuTypes.STRICT_9x3.get(), StrictChestScreen::new);
        MenuScreens.register(BzMenuTypes.STRICT_9x4.get(), StrictChestScreen::new);
        MenuScreens.register(BzMenuTypes.STRICT_9x5.get(), StrictChestScreen::new);
        MenuScreens.register(BzMenuTypes.STRICT_9x6.get(), StrictChestScreen::new);
        MenuScreens.register(BzMenuTypes.CRYSTALLINE_FLOWER.get(), CrystallineFlowerScreen::new);
    }

    private static void registerItemProperties() {
        // Allows shield to use the blocking json file for offset
        ItemProperties.register(
                BzItems.HONEY_CRYSTAL_SHIELD.get(),
                new ResourceLocation("blocking"),
                (itemStack, world, livingEntity, integer) ->
                        livingEntity != null &&
                                livingEntity.isUsingItem() &&
                                livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F
        );

        // Correct model when about to throw
        ItemProperties.register(
                BzItems.STINGER_SPEAR.get(),
                new ResourceLocation("throwing"),
                (itemStack, world, livingEntity, integer) ->
                        livingEntity != null &&
                                livingEntity.isUsingItem() &&
                                livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F
        );

        // Allows honey compass to render the correct texture
        ItemProperties.register(
                BzItems.HONEY_COMPASS.get(),
                new ResourceLocation("angle"),
                HoneyCompassItemProperty.getClampedItemPropertyFunction());

        // Correct model when about to fire
        ItemProperties.register(
                BzItems.BEE_CANNON.get(),
                new ResourceLocation("primed"),
                (itemStack, world, livingEntity, int1) ->
                        livingEntity != null &&
                                livingEntity.isUsingItem() &&
                                livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F
        );

        ItemProperties.register(
                BzItems.CRYSTAL_CANNON.get(),
                new ResourceLocation("primed"),
                (itemStack, world, livingEntity, int1) ->
                        livingEntity != null &&
                                livingEntity.isUsingItem() &&
                                livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F
        );

        // Correct model based on bees
        ItemProperties.register(
                BzItems.BEE_CANNON.get(),
                new ResourceLocation("bee_count"),
                (itemStack, world, livingEntity, int1) ->
                        BeeCannon.getNumberOfBees(itemStack) / 10f
        );

        // Correct model based on crystals
        ItemProperties.register(
                BzItems.CRYSTAL_CANNON.get(),
                new ResourceLocation("crystal_count"),
                (itemStack, world, livingEntity, int1) ->
                        CrystalCannon.getNumberOfCrystals(itemStack) / 10f
        );


        // Correct model based on crystals
        ItemProperties.register(
                BzItems.CRYSTAL_CANNON.get(),
                new ResourceLocation("crystal_count"),
                (itemStack, world, livingEntity, int1) ->
                        CrystalCannon.getNumberOfCrystals(itemStack) / 10f
        );
    }

    private static void registerRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer(BzFluids.SUGAR_WATER_FLUID.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BzFluids.SUGAR_WATER_FLUID_FLOWING.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BzFluids.HONEY_FLUID.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BzFluids.HONEY_FLUID_FLOWING.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BzFluids.ROYAL_JELLY_FLUID.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BzFluids.ROYAL_JELLY_FLUID_FLOWING.get(), RenderType.translucent());
    }

    public static void registerEntityModels(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BeehemothModel.LAYER_LOCATION, BeehemothModel::createBodyLayer);
        event.registerLayerDefinition(BeeQueenModel.LAYER_LOCATION, BeeQueenModel::createBodyLayer);
        event.registerLayerDefinition(StingerSpearModel.LAYER_LOCATION, StingerSpearModel::createLayer);
        event.registerLayerDefinition(BeeStingerModel.LAYER_LOCATION, BeeStingerModel::createLayer);
        event.registerLayerDefinition(HoneyCrystalShardModel.LAYER_LOCATION, HoneyCrystalShardModel::createLayer);
        event.registerLayerDefinition(BeeArmorModel.VARIANT_1_LAYER_LOCATION, BeeArmorModel::createVariant1);
        event.registerLayerDefinition(BeeArmorModel.VARIANT_2_LAYER_LOCATION, BeeArmorModel::createVariant2);
    }

    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers.RegisterRenderers event) {
        EntityRenderers.register(BzEntities.HONEY_SLIME.get(), HoneySlimeRendering::new);
        EntityRenderers.register(BzEntities.BEEHEMOTH.get(), BeehemothRenderer::new);
        EntityRenderers.register(BzEntities.BEE_QUEEN.get(), BeeQueenRenderer::new);
        EntityRenderers.register(BzEntities.POLLEN_PUFF_ENTITY.get(), ThrownItemRenderer::new);
        EntityRenderers.register(BzEntities.THROWN_STINGER_SPEAR_ENTITY.get(), StingerSpearRenderer::new);
        EntityRenderers.register(BzEntities.BEE_STINGER_ENTITY.get(), BeeStingerRenderer::new);
        EntityRenderers.register(BzEntities.HONEY_CRYSTAL_SHARD.get(), HoneyCrystalShardRenderer::new);
    }

    public static void onParticleSetup(RegisterParticleProvidersEvent event) {
        event.register(BzParticles.POLLEN_PARTICLE.get(), PollenPuffParticle.Factory::new);
        event.register(BzParticles.SPARKLE_PARTICLE.get(), SparkleParticle.Factory::new);
        event.register(BzParticles.HONEY_PARTICLE.get(), HoneyParticle.Factory::new);
        event.register(BzParticles.ROYAL_JELLY_PARTICLE.get(), RoyalJellyParticle.Factory::new);
    }

    public static void registerDimensionSpecialEffects(RegisterDimensionSpecialEffectsEvent event) {
        event.register(new ResourceLocation(Bumblezone.MODID, "sky_property"), new BzSkyProperty());
    }
}