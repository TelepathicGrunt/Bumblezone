package com.telepathicgrunt.the_bumblezone.client;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.armor.BeeArmorModelProvider;
import com.telepathicgrunt.the_bumblezone.client.armor.FlowerHeadwearModelProvider;
import com.telepathicgrunt.the_bumblezone.client.blockentityrenderer.EssenceBlockEntityRenderer;
import com.telepathicgrunt.the_bumblezone.client.blocks.ConnectedBlockModel;
import com.telepathicgrunt.the_bumblezone.client.blocks.blocktintsources.InfinityBarrierBlockTintSource;
import com.telepathicgrunt.the_bumblezone.client.blocks.blocktintsources.PotionCandleBlockTintSource;
import com.telepathicgrunt.the_bumblezone.client.itemproperties.conditional.AbilityEssenceIsActive;
import com.telepathicgrunt.the_bumblezone.client.itemproperties.conditional.AbilityEssenceIsLockedOrCooldown;
import com.telepathicgrunt.the_bumblezone.client.itemproperties.conditional.AbilityEssenceMaxAbilityUseRemaining;
import com.telepathicgrunt.the_bumblezone.client.itemproperties.conditional.AbilityEssenceNotInInventory;
import com.telepathicgrunt.the_bumblezone.client.itemproperties.conditional.BeeCannonBeeCount;
import com.telepathicgrunt.the_bumblezone.client.itemproperties.conditional.CreativeTabMarker;
import com.telepathicgrunt.the_bumblezone.client.itemproperties.conditional.CrystalCannonCrystalCount;
import com.telepathicgrunt.the_bumblezone.client.itemproperties.conditional.PollinatedBeeLeggings;
import com.telepathicgrunt.the_bumblezone.client.itemproperties.rangeselect.HoneyCompassAngle;
import com.telepathicgrunt.the_bumblezone.client.items.FlowerHeadwearColoring;
import com.telepathicgrunt.the_bumblezone.client.items.HoneyCompassItemProperty;
import com.telepathicgrunt.the_bumblezone.client.items.InfinityBarrierColoring;
import com.telepathicgrunt.the_bumblezone.client.items.PotionCandleColoring;
import com.telepathicgrunt.the_bumblezone.client.itemtintsources.PotionCandleItemTintSource;
import com.telepathicgrunt.the_bumblezone.client.particles.DustParticle;
import com.telepathicgrunt.the_bumblezone.client.particles.HoneyParticle;
import com.telepathicgrunt.the_bumblezone.client.particles.PollenPuffParticle;
import com.telepathicgrunt.the_bumblezone.client.particles.RoyalJellyParticle;
import com.telepathicgrunt.the_bumblezone.client.particles.SparkleParticle;
import com.telepathicgrunt.the_bumblezone.client.particles.VoiceParticle;
import com.telepathicgrunt.the_bumblezone.client.particles.WindParticle;
import com.telepathicgrunt.the_bumblezone.client.rendering.HiddenEffectIconRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.armor.BeeArmorModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.armor.FlowerHeadwearModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.beehemoth.BeehemothModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.beehemoth.BeehemothRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.beequeen.BeeQueenModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.beequeen.BeeQueenRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.beestinger.BeeStingerModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.beestinger.BeeStingerRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.cosmiccrystal.CosmicCrystalModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.cosmiccrystal.CosmicCrystalRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.electricring.ElectricRingModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.electricring.ElectricRingRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.KnowingEssenceLootBlockOutlining;
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
import com.telepathicgrunt.the_bumblezone.client.rendering.variantbee.BackupVariantBeeModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.variantbee.BackupVariantBeeRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.variantbee.VariantBeeRenderer;
import com.telepathicgrunt.the_bumblezone.client.screens.BuzzingBriefcaseScreen;
import com.telepathicgrunt.the_bumblezone.client.screens.CrystallineFlowerScreen;
import com.telepathicgrunt.the_bumblezone.client.screens.StrictChestScreen;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.events.client.BzBlockRenderedOnScreenEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzClientSetupEnqueuedEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzHookupConditionalItemModelPropertiesEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzHookupItemTintSourcesEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzHookupRangeSelectItemModelPropertiesEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzKeyInputEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterArmorProviderEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterBlockColorEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterBlockEntityRendererEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterEffectRenderersEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterEntityLayersEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterEntityRenderersEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterKeyMappingEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterMenuScreenEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterParticleEvent;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.BzTagsUpdatedEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlockEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzClientFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzEntities;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import com.telepathicgrunt.the_bumblezone.modinit.BzParticles;
import earth.terrarium.athena.api.client.models.FactoryManager;
import net.minecraft.client.renderer.blockentity.BrushableBlockRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public class BumblezoneClient {

    public static void init() {
        FactoryManager.register(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "connected"), ConnectedBlockModel.FACTORY);

        BzRegisterParticleEvent.EVENT.addListener(BumblezoneClient::onParticleSetup);
        BzRegisterEntityRenderersEvent.EVENT.addListener(BumblezoneClient::registerEntityRenderers);
        BzRegisterEntityLayersEvent.EVENT.addListener(BumblezoneClient::registerEntityLayers);
        BzRegisterKeyMappingEvent.EVENT.addListener(BumblezoneClient::registerKeyBinding);
        BzRegisterShaderEvent.EVENT.addListener(BumblezoneClient::registerShaders);
        BzRegisterBlockColorEvent.EVENT.addListener((event) -> event.register(List.of(InfinityBarrierBlockTintSource.indexOneTintSource(), InfinityBarrierBlockTintSource.indexTwoTintSource()), BzBlocks.INFINITY_BARRIER.get()));
        BzRegisterBlockColorEvent.EVENT.addListener((event) -> event.register(List.of(PotionCandleBlockTintSource.indexOneTintSource(), PotionCandleBlockTintSource.indexTwoTintSource()), BzBlocks.POTION_BASE_CANDLE.get()));

        BzClientSetupEnqueuedEvent.EVENT.addListener(BumblezoneClient::clientSetup);
        BzBlockRenderedOnScreenEvent.EVENT.addListener(PileOfPollenRenderer::pileOfPollenOverlay);
        BzKeyInputEvent.EVENT.addListener(BeehemothControls::keyInput);
        BzRegisterMenuScreenEvent.EVENT.addListener(BumblezoneClient::registerScreens);
        BzHookupConditionalItemModelPropertiesEvent.EVENT.addListener(BumblezoneClient::registerConditionalItemProperties);
        BzHookupRangeSelectItemModelPropertiesEvent.EVENT.addListener(BumblezoneClient::registerRangeSelectItemProperties);
        BzHookupItemTintSourcesEvent.EVENT.addListener(BumblezoneClient::registerItemTintSources);
        BzRegisterArmorProviderEvent.EVENT.addListener(BumblezoneClient::registerArmorProviders);
        BzRegisterEffectRenderersEvent.EVENT.addListener(BumblezoneClient::registerEffectRenderers);
        BzRegisterBlockEntityRendererEvent.EVENT.addListener(BumblezoneClient::registerBlockEntityRenderers);
        BzTagsUpdatedEvent.EVENT.addListener((_) -> KnowingEssenceLootBlockOutlining.resetTargetBlockCache());

        BzClientFluids.CLIENT_FLUIDS.init();
    }

    public static void clientSetup(BzClientSetupEnqueuedEvent event) {}

    public static void registerBlockEntityRenderers(BzRegisterBlockEntityRendererEvent event) {
        event.register(BzBlockEntities.ESSENCE_BLOCK.get(), EssenceBlockEntityRenderer::new);
        event.register(BzBlockEntities.STATE_FOCUSED_BRUSHABLE_BLOCK_ENTITY.get(), BrushableBlockRenderer::new);
    }

    public static void registerEffectRenderers(BzRegisterEffectRenderersEvent event) {
        event.register(BzEffects.HIDDEN.holder(), new HiddenEffectIconRenderer());
    }

    public static void registerArmorProviders(BzRegisterArmorProviderEvent event) {
        event.register(BzItems.FLOWER_HEADWEAR.get(), FlowerHeadwearModelProvider::new);
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

    public static void registerKeyBinding(BzRegisterKeyMappingEvent event) {
        event.register(BeehemothControls.KEY_BIND_BEEHEMOTH_UP);
        event.register(BeehemothControls.KEY_BIND_BEEHEMOTH_DOWN);
    }

    private static void registerScreens(BzRegisterMenuScreenEvent event) {
        event.register(BzMenuTypes.STRICT_9x1.get(), StrictChestScreen::new);
        event.register(BzMenuTypes.STRICT_9x2.get(), StrictChestScreen::new);
        event.register(BzMenuTypes.STRICT_9x3.get(), StrictChestScreen::new);
        event.register(BzMenuTypes.STRICT_9x4.get(), StrictChestScreen::new);
        event.register(BzMenuTypes.STRICT_9x5.get(), StrictChestScreen::new);
        event.register(BzMenuTypes.STRICT_9x6.get(), StrictChestScreen::new);
        event.register(BzMenuTypes.CRYSTALLINE_FLOWER.get(), CrystallineFlowerScreen::new);
        event.register(BzMenuTypes.BUZZING_BRIEFCASE.get(), BuzzingBriefcaseScreen::new);
    }

    private static void registerConditionalItemProperties(BzHookupConditionalItemModelPropertiesEvent event) {
        event.registrator().accept(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "bee_cannon_bee_count"), BeeCannonBeeCount.MAP_CODEC);
        event.registrator().accept(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "crystal_cannon_crystal_count"), CrystalCannonCrystalCount.MAP_CODEC);
        event.registrator().accept(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "is_creative_tab_icon"), CreativeTabMarker.MAP_CODEC);
        event.registrator().accept(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "pollinated_bee_leggings"), PollinatedBeeLeggings.MAP_CODEC);
        event.registrator().accept(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "ability_essence_is_active"), AbilityEssenceIsActive.MAP_CODEC);
        event.registrator().accept(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "ability_essence_is_locked_or_cooldown"), AbilityEssenceIsLockedOrCooldown.MAP_CODEC);
        event.registrator().accept(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "ability_essence_max_ability_use_remaining"), AbilityEssenceMaxAbilityUseRemaining.MAP_CODEC);
        event.registrator().accept(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "ability_essence_not_in_inventory"), AbilityEssenceNotInInventory.MAP_CODEC);
    }

    private static void registerRangeSelectItemProperties(BzHookupRangeSelectItemModelPropertiesEvent event) {
        event.registrator().accept(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "honey_compass"), HoneyCompassAngle.MAP_CODEC);
    }

    private static void registerItemTintSources(BzHookupItemTintSourcesEvent event) {
        event.registrator().accept(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "potion_candle"), PotionCandleItemTintSource.MAP_CODEC);
    }

    public static void registerEntityLayers(BzRegisterEntityLayersEvent event) {
        if (BzClientConfigs.useBackupModelForVariantBee) {
            event.register(BackupVariantBeeModel.LAYER_LOCATION, BackupVariantBeeModel::createBodyLayer);
        }

        event.register(BeehemothModel.LAYER_LOCATION, BeehemothModel::createBodyLayer);
        event.register(BeeQueenModel.LAYER_LOCATION, BeeQueenModel::createBodyLayer);
        event.register(SentryWatcherModel.LAYER_LOCATION, SentryWatcherModel::createBodyLayer);
        event.register(RootminModel.LAYER_LOCATION, RootminModel::createBodyLayer);
        event.register(StingerSpearModel.LAYER_LOCATION, StingerSpearModel::createLayer);
        event.register(BeeStingerModel.LAYER_LOCATION, BeeStingerModel::createLayer);
        event.register(HoneyCrystalShardModel.LAYER_LOCATION, HoneyCrystalShardModel::createLayer);
        event.register(BeeArmorModel.VARIANT_1_LAYER_LOCATION, BeeArmorModel::createVariant1);
        event.register(BeeArmorModel.VARIANT_2_LAYER_LOCATION, BeeArmorModel::createVariant2);
        event.register(FlowerHeadwearModel.FLOWER_HEADWEAR_LAYER_LOCATION, FlowerHeadwearModel::createBodyLayer);
        event.register(ElectricRingModel.LAYER_LOCATION, ElectricRingModel::createBodyLayer);
        event.register(PurpleSpikeModel.LAYER_LOCATION, PurpleSpikeModel::createBodyLayer);
        event.register(CosmicCrystalModel.LAYER_LOCATION, CosmicCrystalModel::createBodyLayer);
    }

    public static void registerEntityRenderers(BzRegisterEntityRenderersEvent event) {
        if (BzClientConfigs.useBackupModelForVariantBee) {
            event.register(BzEntities.VARIANT_BEE.get(), BackupVariantBeeRenderer::new);
        }
        else {
            event.register((EntityType) BzEntities.VARIANT_BEE.get(), VariantBeeRenderer::new);
        }

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
        event.register(BzEntities.COSMIC_CRYSTAL_ENTITY.get(), CosmicCrystalRenderer::new);
    }

    public static void onParticleSetup(BzRegisterParticleEvent event) {
        event.register(BzParticles.POLLEN_PARTICLE.get(), PollenPuffParticle.Factory::new);
        event.register(BzParticles.SPARKLE_PARTICLE.get(), SparkleParticle.Factory::new);
        event.register(BzParticles.HONEY_PARTICLE.get(), HoneyParticle.Factory::new);
        event.register(BzParticles.ROYAL_JELLY_PARTICLE.get(), RoyalJellyParticle.Factory::new);
        event.register(BzParticles.DUST_PARTICLE.get(), DustParticle.Factory::new);
        event.register(BzParticles.WIND_PARTICLE.get(), (spriteSet) -> new WindParticle.Factory(spriteSet, false));
        event.register(BzParticles.MOVING_WIND_PARTICLE.get(), (spriteSet) -> new WindParticle.Factory(spriteSet, true));
        event.register(BzParticles.ANGRY_PARTICLE.get(), VoiceParticle.Factory::new);
        event.register(BzParticles.CURIOUS_PARTICLE.get(), VoiceParticle.Factory::new);
        event.register(BzParticles.CURSING_PARTICLE.get(), VoiceParticle.Factory::new);
        event.register(BzParticles.EMBARRASSED_PARTICLE.get(), VoiceParticle.Factory::new);
        event.register(BzParticles.SHOCK_PARTICLE.get(), VoiceParticle.Factory::new);
    }

    public static void registerShaders(BzRegisterShaderEvent event) {
//        event.register(
//            Identifier.fromNamespaceAndPath(Bumblezone.MODID, "rendertype_bumblezone_essence"),
//            EssenceBlockEntityRenderer.POSITION_COLOR_NORMAL,
//            (safeShader) -> EssenceBlockEntityRenderer.SAFE_SHADER_INSTANCE = safeShader
//        );
    }
}