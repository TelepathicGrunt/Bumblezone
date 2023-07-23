package com.telepathicgrunt.the_bumblezone.client;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentityrenderer.EssenceBlockEntityRenderer;
import com.telepathicgrunt.the_bumblezone.client.armor.BeeArmorModelProvider;
import com.telepathicgrunt.the_bumblezone.client.items.HoneyCompassItemProperty;
import com.telepathicgrunt.the_bumblezone.client.items.IncenseCandleColoring;
import com.telepathicgrunt.the_bumblezone.client.items.InfinityBarrierColoring;
import com.telepathicgrunt.the_bumblezone.client.particles.DustParticle;
import com.telepathicgrunt.the_bumblezone.client.particles.HoneyParticle;
import com.telepathicgrunt.the_bumblezone.client.particles.PollenPuffParticle;
import com.telepathicgrunt.the_bumblezone.client.particles.RoyalJellyParticle;
import com.telepathicgrunt.the_bumblezone.client.particles.SparkleParticle;
import com.telepathicgrunt.the_bumblezone.client.particles.VoiceParticle;
import com.telepathicgrunt.the_bumblezone.client.particles.WindParticle;
import com.telepathicgrunt.the_bumblezone.client.rendering.HiddenEffectIconRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.VariantBeeRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.beearmor.BeeArmorModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.beehemoth.BeehemothModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.beehemoth.BeehemothRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.beequeen.BeeQueenModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.beequeen.BeeQueenRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.beestinger.BeeStingerModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.beestinger.BeeStingerRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.electricring.ElectricRingModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.electricring.ElectricRingRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.fluids.HoneyFluidClientProperties;
import com.telepathicgrunt.the_bumblezone.client.rendering.fluids.RoyalJellyClientProperties;
import com.telepathicgrunt.the_bumblezone.client.rendering.fluids.SugarWaterClientProperties;
import com.telepathicgrunt.the_bumblezone.client.rendering.honeycrystalshard.HoneyCrystalShardModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.honeycrystalshard.HoneyCrystalShardRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.honeyslime.HoneySlimeRendering;
import com.telepathicgrunt.the_bumblezone.client.rendering.pileofpollen.PileOfPollenRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.purplespike.PurpleSpikeModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.purplespike.PurpleSpikeRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.rootmin.RootminModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.rootmin.RootminRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.sentrywatcher.SentryWatcherModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.sentrywatcher.SentryWatcherRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.stingerspear.StingerSpearModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.stingerspear.StingerSpearRenderer;
import com.telepathicgrunt.the_bumblezone.events.client.BlockRenderedOnScreenEvent;
import com.telepathicgrunt.the_bumblezone.events.client.ClientSetupEnqueuedEvent;
import com.telepathicgrunt.the_bumblezone.events.client.ClientTickEvent;
import com.telepathicgrunt.the_bumblezone.events.client.KeyInputEvent;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterArmorProviderEvent;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterBlockColorEvent;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterBlockEntityRendererEvent;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterClientFluidPropertiesEvent;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterDimensionEffectsEvent;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterEffectRenderersEvent;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterEntityLayersEvent;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterEntityRenderersEvent;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterItemColorEvent;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterItemPropertiesEvent;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterKeyMappingEvent;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterMenuScreenEvent;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterParticleEvent;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterRenderTypeEvent;
import com.telepathicgrunt.the_bumblezone.items.BeeCannon;
import com.telepathicgrunt.the_bumblezone.items.CrystalCannon;
import com.telepathicgrunt.the_bumblezone.items.HoneyBeeLeggings;
import com.telepathicgrunt.the_bumblezone.items.StinglessBeeHelmet;
import com.telepathicgrunt.the_bumblezone.items.essence.AbilityEssenceItem;
import com.telepathicgrunt.the_bumblezone.mixin.client.ClientLevelAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.screens.BuzzingBriefcaseScreen;
import com.telepathicgrunt.the_bumblezone.screens.CrystallineFlowerScreen;
import com.telepathicgrunt.the_bumblezone.screens.StrictChestScreen;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.world.dimension.BzDimensionSpecialEffects;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.BrushableBlockRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import java.util.HashSet;
import java.util.Set;

public class BumblezoneClient {

    public static void init() {
        RegisterParticleEvent.EVENT.addListener(BumblezoneClient::onParticleSetup);
        RegisterEntityRenderersEvent.EVENT.addListener(BumblezoneClient::registerEntityRenderers);
        RegisterEntityLayersEvent.EVENT.addListener(BumblezoneClient::registerEntityLayers);
        RegisterKeyMappingEvent.EVENT.addListener(BumblezoneClient::registerKeyBinding);
        RegisterDimensionEffectsEvent.EVENT.addListener(BumblezoneClient::registerDimensionEffects);
        RegisterBlockColorEvent.EVENT.addListener(InfinityBarrierColoring::registerBlockColors);
        RegisterBlockColorEvent.EVENT.addListener(IncenseCandleColoring::registerBlockColors);
        RegisterItemColorEvent.EVENT.addListener(IncenseCandleColoring::registerItemColors);
        ClientTickEvent.EVENT.addListener(event -> {
            if (event.end()) {
                StinglessBeeHelmet.decrementHighlightingCounter(GeneralUtilsClient.getClientPlayer());
            }
        });

        ClientSetupEnqueuedEvent.EVENT.addListener(BumblezoneClient::clientSetup);
        BlockRenderedOnScreenEvent.EVENT.addListener(PileOfPollenRenderer::pileOfPollenOverlay);
        KeyInputEvent.EVENT.addListener(BeehemothControls::keyInput);
        RegisterMenuScreenEvent.EVENT.addListener(BumblezoneClient::registerScreens);
        RegisterClientFluidPropertiesEvent.EVENT.addListener(BumblezoneClient::onRegisterClientFluidProperties);
        RegisterItemPropertiesEvent.EVENT.addListener(BumblezoneClient::registerItemProperties);
        RegisterRenderTypeEvent.EVENT.addListener(BumblezoneClient::registerRenderTypes);
        RegisterArmorProviderEvent.EVENT.addListener(BumblezoneClient::registerArmorProviders);
        RegisterEffectRenderersEvent.EVENT.addListener(BumblezoneClient::registerEffectRenderers);
        RegisterBlockEntityRendererEvent.EVENT.addListener(BumblezoneClient::registerBlockEntityRenderers);
    }

    public static void clientSetup(ClientSetupEnqueuedEvent event) {
        Set<Item> particleMarkerBlocks = new HashSet<>(ClientLevelAccessor.getMARKER_PARTICLE_ITEMS());
        particleMarkerBlocks.add(BzItems.HEAVY_AIR.get());
        ClientLevelAccessor.setMARKER_PARTICLE_ITEMS(particleMarkerBlocks);
    }

    public static void registerBlockEntityRenderers(RegisterBlockEntityRendererEvent<?> event) {
        BlockEntityRenderers.register(BzBlockEntities.ESSENCE_BLOCK.get(), EssenceBlockEntityRenderer::new);
        BlockEntityRenderers.register(BzBlockEntities.STATE_FOCUSED_BRUSHABLE_BLOCK_ENTITY.get(), BrushableBlockRenderer::new);
    }

    public static void registerEffectRenderers(RegisterEffectRenderersEvent event) {
        event.register(BzEffects.HIDDEN.get(), new HiddenEffectIconRenderer());
    }

    public static void registerArmorProviders(RegisterArmorProviderEvent event) {
        event.register(BzItems.STINGLESS_BEE_HELMET_1.get(), BeeArmorModelProvider::new);
        event.register(BzItems.STINGLESS_BEE_HELMET_2.get(), BeeArmorModelProvider::new);
        event.register(BzItems.BUMBLE_BEE_CHESTPLATE_1.get(), BeeArmorModelProvider::new);
        event.register(BzItems.BUMBLE_BEE_CHESTPLATE_2.get(), BeeArmorModelProvider::new);
        event.register(BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_1.get(), BeeArmorModelProvider::new);
        event.register(BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_2.get(), BeeArmorModelProvider::new);
        event.register(BzItems.HONEY_BEE_LEGGINGS_1.get(), BeeArmorModelProvider::new);
        event.register(BzItems.HONEY_BEE_LEGGINGS_2.get(), BeeArmorModelProvider::new);
        event.register(BzItems.CARPENTER_BEE_BOOTS_1.get(), BeeArmorModelProvider::new);
        event.register(BzItems.CARPENTER_BEE_BOOTS_2.get(), BeeArmorModelProvider::new);
    }

    public static void onRegisterClientFluidProperties(RegisterClientFluidPropertiesEvent event) {
        event.register(BzFluids.HONEY_FLUID_TYPE.get(), HoneyFluidClientProperties::create);
        event.register(BzFluids.ROYAL_JELLY_FLUID_TYPE.get(), RoyalJellyClientProperties::create);
        event.register(BzFluids.SUGAR_WATER_FLUID_TYPE.get(), SugarWaterClientProperties::create);
    }

    public static void registerKeyBinding(RegisterKeyMappingEvent event) {
        event.register(BeehemothControls.KEY_BIND_BEEHEMOTH_UP);
        event.register(BeehemothControls.KEY_BIND_BEEHEMOTH_DOWN);
    }

    private static void registerScreens(RegisterMenuScreenEvent event) {
        event.register(BzMenuTypes.STRICT_9x1.get(), StrictChestScreen::new);
        event.register(BzMenuTypes.STRICT_9x2.get(), StrictChestScreen::new);
        event.register(BzMenuTypes.STRICT_9x3.get(), StrictChestScreen::new);
        event.register(BzMenuTypes.STRICT_9x4.get(), StrictChestScreen::new);
        event.register(BzMenuTypes.STRICT_9x5.get(), StrictChestScreen::new);
        event.register(BzMenuTypes.STRICT_9x6.get(), StrictChestScreen::new);
        event.register(BzMenuTypes.CRYSTALLINE_FLOWER.get(), CrystallineFlowerScreen::new);
        event.register(BzMenuTypes.BUZZING_BRIEFCASE.get(), BuzzingBriefcaseScreen::new);
    }

    private static void registerItemProperties(RegisterItemPropertiesEvent event) {
        // Allows shield to use the blocking json file for offset
        event.register(
                BzItems.HONEY_CRYSTAL_SHIELD.get(),
                new ResourceLocation("blocking"),
                (itemStack, world, livingEntity, integer) ->
                        livingEntity != null &&
                                livingEntity.isUsingItem() &&
                                livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F
        );

        // Correct model when about to throw
        event.register(
                BzItems.STINGER_SPEAR.get(),
                new ResourceLocation("throwing"),
                (itemStack, world, livingEntity, integer) ->
                        livingEntity != null &&
                                livingEntity.isUsingItem() &&
                                livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F
        );

        // Allows honey compass to render the correct texture
        event.register(
                BzItems.HONEY_COMPASS.get(),
                new ResourceLocation("angle"),
                HoneyCompassItemProperty.getClampedItemPropertyFunction());

        // Correct model when about to fire
        event.register(
                BzItems.BEE_CANNON.get(),
                new ResourceLocation("primed"),
                (itemStack, world, livingEntity, int1) ->
                        livingEntity != null &&
                                livingEntity.isUsingItem() &&
                                livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F
        );

        event.register(
                BzItems.CRYSTAL_CANNON.get(),
                new ResourceLocation("primed"),
                (itemStack, world, livingEntity, int1) ->
                        livingEntity != null &&
                                livingEntity.isUsingItem() &&
                                livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F
        );

        // Correct model based on bees
        event.register(
                BzItems.BEE_CANNON.get(),
                new ResourceLocation("bee_count"),
                (itemStack, world, livingEntity, int1) ->
                        BeeCannon.getNumberOfBees(itemStack) / 10f
        );

        // Correct model based on crystals
        event.register(
                BzItems.CRYSTAL_CANNON.get(),
                new ResourceLocation("crystal_count"),
                (itemStack, world, livingEntity, int1) ->
                        CrystalCannon.getNumberOfCrystals(itemStack) / 10f
        );


        // Correct model based on crystals
        event.register(
                BzItems.CRYSTAL_CANNON.get(),
                new ResourceLocation("crystal_count"),
                (itemStack, world, livingEntity, int1) ->
                        CrystalCannon.getNumberOfCrystals(itemStack) / 10f
        );


        // Show different stage for creative menu icon
        event.register(
                BzItems.HONEYCOMB_BROOD.get(),
                new ResourceLocation("is_creative_tab_icon"),
                (itemStack, world, livingEntity, integer) ->
                        itemStack.hasTag() && itemStack.getTag().getBoolean("isCreativeTabIcon") ? 1.0F : 0.0F
        );


        // Correct model based on pollen on leggings
        event.register(
                BzItems.HONEY_BEE_LEGGINGS_1.get(),
                new ResourceLocation("pollen"),
                (itemStack, world, livingEntity, int1) ->
                        HoneyBeeLeggings.isPollinated(itemStack) ? 1f : 0f
        );


        // Correct model based on pollen on leggings
        event.register(
                BzItems.HONEY_BEE_LEGGINGS_2.get(),
                new ResourceLocation("pollen"),
                (itemStack, world, livingEntity, int1) ->
                        HoneyBeeLeggings.isPollinated(itemStack) ? 1f : 0f
        );


        // Show state of essence
        registerEssenceItemProperty(event, BzItems.ESSENCE_RAGING.get());
        registerEssenceItemProperty(event, BzItems.ESSENCE_KNOWING.get());
        registerEssenceItemProperty(event, BzItems.ESSENCE_CALMING.get());
        registerEssenceItemProperty(event, BzItems.ESSENCE_LIFE.get());
        registerEssenceItemProperty(event, BzItems.ESSENCE_RADIANCE.get());
        registerEssenceItemProperty(event, BzItems.ESSENCE_CONTINUITY.get());
    }

    private static void registerEssenceItemProperty(RegisterItemPropertiesEvent event, Item item) {
        event.register(
            item,
            new ResourceLocation("state"),
            (itemStack, world, livingEntity, integer) -> {
                if (itemStack.getItem() instanceof AbilityEssenceItem abilityEssenceItem) {
                    if (!AbilityEssenceItem.getIsInInventory(itemStack)) {
                        return 0.0F;
                    }
                    else if (AbilityEssenceItem.getIsActive(itemStack)) {
                        return abilityEssenceItem.getAbilityUseRemaining(itemStack) == abilityEssenceItem.getMaxAbilityUseAmount() ?
                              0.2F : 0.25F;
                    }
                    else if (AbilityEssenceItem.getIsLocked(itemStack) || AbilityEssenceItem.getForcedCooldown(itemStack)) {
                        return 0.3F;
                    }
                    else {
                        return abilityEssenceItem.getAbilityUseRemaining(itemStack) == abilityEssenceItem.getMaxAbilityUseAmount() ?
                                0.1F : 0.15F;
                    }
                }
                return 0.0F;
            }
        );
    }

    private static void registerRenderTypes(RegisterRenderTypeEvent event) {
        event.register(RenderType.translucent(),
                BzFluids.SUGAR_WATER_FLUID.get(),
                BzFluids.SUGAR_WATER_FLUID_FLOWING.get(),
                BzFluids.HONEY_FLUID.get(),
                BzFluids.HONEY_FLUID_FLOWING.get(),
                BzFluids.ROYAL_JELLY_FLUID.get(),
                BzFluids.ROYAL_JELLY_FLUID_FLOWING.get()
        );

        event.register(RenderType.cutout(),
                BzBlocks.STICKY_HONEY_REDSTONE.get(),
                BzBlocks.STICKY_HONEY_RESIDUE.get(),
                BzBlocks.HONEY_WEB.get(),
                BzBlocks.REDSTONE_HONEY_WEB.get(),
                BzBlocks.SUPER_CANDLE_WICK.get(),
                BzBlocks.SUPER_CANDLE_WICK_SOUL.get(),
                BzBlocks.INCENSE_BASE_CANDLE.get(),
                BzBlocks.CRYSTALLINE_FLOWER.get(),
                BzBlocks.POROUS_HONEYCOMB.get(),
                BzBlocks.EMPTY_HONEYCOMB_BROOD.get(),
                BzBlocks.INFINITY_BARRIER.get()
        );

        BzBlocks.CURTAINS.stream().map(RegistryEntry::get).forEach(block -> event.register(RenderType.cutout(), block));

        event.register(RenderType.translucent(),
                BzBlocks.HONEY_CRYSTAL.get(),
                BzBlocks.GLISTERING_HONEY_CRYSTAL.get(),
                BzBlocks.ROYAL_JELLY_BLOCK.get(),
                BzBlocks.ESSENCE_BLOCK_RED.get(),
                BzBlocks.ESSENCE_BLOCK_PURPLE.get(),
                BzBlocks.ESSENCE_BLOCK_BLUE.get(),
                BzBlocks.ESSENCE_BLOCK_GREEN.get(),
                BzBlocks.ESSENCE_BLOCK_YELLOW.get(),
                BzBlocks.ESSENCE_BLOCK_WHITE.get()
        );
    }

    public static void registerEntityLayers(RegisterEntityLayersEvent event) {
        event.register(BeehemothModel.LAYER_LOCATION, BeehemothModel::createBodyLayer);
        event.register(BeeQueenModel.LAYER_LOCATION, BeeQueenModel::createBodyLayer);
        event.register(SentryWatcherModel.LAYER_LOCATION, SentryWatcherModel::createBodyLayer);
        event.register(RootminModel.LAYER_LOCATION, RootminModel::createBodyLayer);
        event.register(StingerSpearModel.LAYER_LOCATION, StingerSpearModel::createLayer);
        event.register(BeeStingerModel.LAYER_LOCATION, BeeStingerModel::createLayer);
        event.register(HoneyCrystalShardModel.LAYER_LOCATION, HoneyCrystalShardModel::createLayer);
        event.register(BeeArmorModel.VARIANT_1_LAYER_LOCATION, BeeArmorModel::createVariant1);
        event.register(BeeArmorModel.VARIANT_2_LAYER_LOCATION, BeeArmorModel::createVariant2);
        event.register(ElectricRingModel.LAYER_LOCATION, ElectricRingModel::createBodyLayer);
        event.register(PurpleSpikeModel.LAYER_LOCATION, PurpleSpikeModel::createBodyLayer);
    }

    public static void registerEntityRenderers(RegisterEntityRenderersEvent event) {
        event.register((EntityType) BzEntities.VARIANT_BEE.get(), VariantBeeRenderer::new);
        event.register(BzEntities.HONEY_SLIME.get(), HoneySlimeRendering::new);
        event.register(BzEntities.BEEHEMOTH.get(), BeehemothRenderer::new);
        event.register(BzEntities.BEE_QUEEN.get(), BeeQueenRenderer::new);
        event.register(BzEntities.ROOTMIN.get(), RootminRenderer::new);
        event.register(BzEntities.SENTRY_WATCHER.get(), SentryWatcherRenderer::new);
        event.register(BzEntities.POLLEN_PUFF_ENTITY.get(), ThrownItemRenderer::new);
        event.register(BzEntities.DIRT_PELLET_ENTITY.get(), ThrownItemRenderer::new);
        event.register(BzEntities.THROWN_STINGER_SPEAR_ENTITY.get(), StingerSpearRenderer::new);
        event.register(BzEntities.BEE_STINGER_ENTITY.get(), BeeStingerRenderer::new);
        event.register(BzEntities.HONEY_CRYSTAL_SHARD.get(), HoneyCrystalShardRenderer::new);
        event.register(BzEntities.ELECTRIC_RING_ENTITY.get(), ElectricRingRenderer::new);
        event.register(BzEntities.PURPLE_SPIKE_ENTITY.get(), PurpleSpikeRenderer::new);
    }

    public static void onParticleSetup(RegisterParticleEvent event) {
        event.register(BzParticles.POLLEN_PARTICLE.get(), PollenPuffParticle.Factory::new);
        event.register(BzParticles.SPARKLE_PARTICLE.get(), SparkleParticle.Factory::new);
        event.register(BzParticles.HONEY_PARTICLE.get(), HoneyParticle.Factory::new);
        event.register(BzParticles.ROYAL_JELLY_PARTICLE.get(), RoyalJellyParticle.Factory::new);
        event.register(BzParticles.DUST_PARTICLE.get(), DustParticle.Factory::new);
        event.register(BzParticles.WIND_PARTICLE.get(), WindParticle.Factory::new);
        event.register(BzParticles.ANGRY_PARTICLE.get(), VoiceParticle.Factory::new);
        event.register(BzParticles.CURIOUS_PARTICLE.get(), VoiceParticle.Factory::new);
        event.register(BzParticles.CURSING_PARTICLE.get(), VoiceParticle.Factory::new);
        event.register(BzParticles.EMBARRASSED_PARTICLE.get(), VoiceParticle.Factory::new);
        event.register(BzParticles.SHOCK_PARTICLE.get(), VoiceParticle.Factory::new);
    }

    public static void registerDimensionEffects(RegisterDimensionEffectsEvent event) {
        event.register(new ResourceLocation(Bumblezone.MODID, "dimension_special_effects"), new BzDimensionSpecialEffects());
    }
}