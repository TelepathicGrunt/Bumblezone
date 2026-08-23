package com.telepathicgrunt.the_bumblezone.neoforge;

import com.hollingsworth.arsnouveau.common.camera.CameraEvents;
import com.hollingsworth.arsnouveau.common.camera.ClientCameraEvents;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.MusicHandler;
import com.telepathicgrunt.the_bumblezone.client.armor.ArmorModelProvider;
import com.telepathicgrunt.the_bumblezone.client.neoforge.DimensionFog;
import com.telepathicgrunt.the_bumblezone.client.neoforge.NeoforgeArmorProviders;
import com.telepathicgrunt.the_bumblezone.client.rendering.MobEffectRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.cosmiccrystal.CosmicCrystalRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.EssenceOverlay;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.KnowingEssenceLootBlockOutlining;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.KnowingEssenceStructureMessage;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.RadianceEssenceArmorMessage;
import com.telepathicgrunt.the_bumblezone.client.screens.DimensionTeleportingScreen;
import com.telepathicgrunt.the_bumblezone.client.screens.LevelLoadingScreenInterface;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.events.client.BzBlockRenderedOnScreenEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzClientSetupEnqueuedEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzHookupConditionalItemModelPropertiesEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzHookupItemTintSourcesEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzHookupRangeSelectItemModelPropertiesEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzKeyInputEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterBlockColorEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterBlockEntityRendererEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterEffectRenderersEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterEntityLayersEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterEntityRenderersEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterKeyMappingEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterMenuScreenEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterParticleEvent;
import com.telepathicgrunt.the_bumblezone.items.StinglessBeeHelmet;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.utils.LazySupplier;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.object.equipment.ElytraModel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class NeoForgeClientEventManager {

    public static void init(IEventBus modEventBus, IEventBus eventBus) {

        eventBus.addListener(NeoForgeClientEventManager::onBlockScreen);
        eventBus.addListener(NeoForgeClientEventManager::onKeyInput);
        eventBus.addListener(NeoForgeClientEventManager::onClientTickPost);
        eventBus.addListener(NeoForgeClientEventManager::onScreenRendering);
        eventBus.addListener(NeoForgeClientEventManager::onLevelExtractRendering);
        eventBus.addListener(NeoForgeClientEventManager::onBlockRenderStageRendering);
        eventBus.addListener(NeoForgeClientEventManager::onGuiRendering);
        eventBus.addListener(EventPriority.HIGHEST, true, DimensionFog::fogThicknessAdjustments);
        eventBus.addListener(NeoForgeClientEventManager::onCameraSetup);

        modEventBus.addListener(NeoForgeClientEventManager::onClientSetup);
        modEventBus.addListener(NeoForgeClientEventManager::onRegisterParticles);
        modEventBus.addListener(NeoForgeClientEventManager::onRegisterKeys);
        modEventBus.addListener(NeoForgeClientEventManager::onRegisterBlockColors);
        modEventBus.addListener(NeoForgeClientEventManager::onRegisterEntityRenderers);
        modEventBus.addListener(NeoForgeClientEventManager::onEntityLayers);
        modEventBus.addListener(NeoForgeClientEventManager::onRegisterScreens);
        modEventBus.addListener(NeoForgeClientEventManager::onRegisterClientExtensions);
        modEventBus.addListener(NeoForgeClientEventManager::onConditionalItemPropertiesSetup);
        modEventBus.addListener(NeoForgeClientEventManager::onRangeSelectItemPropertiesSetup);
        modEventBus.addListener(NeoForgeClientEventManager::onItemTintSourcesSetup);
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BzClientSetupEnqueuedEvent.EVENT.invoke(new BzClientSetupEnqueuedEvent(Runnable::run));
            BzRegisterEffectRenderersEvent.EVENT.invoke(BzRegisterEffectRenderersEvent.INSTANCE);
            BzRegisterBlockEntityRendererEvent.EVENT.invoke(new BzRegisterBlockEntityRendererEvent<>(BlockEntityRenderers::register));
        });
    }

    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(createArmorExtension(), BzItems.CARPENTER_BEE_BOOTS_1.get());
        event.registerItem(createArmorExtension(), BzItems.CARPENTER_BEE_BOOTS_2.get());
        event.registerItem(createArmorExtension(), BzItems.HONEY_BEE_LEGGINGS_1.get());
        event.registerItem(createArmorExtension(), BzItems.HONEY_BEE_LEGGINGS_2.get());
        event.registerItem(createArmorExtension(), BzItems.BUMBLE_BEE_CHESTPLATE_1.get());
        event.registerItem(createArmorExtension(), BzItems.BUMBLE_BEE_CHESTPLATE_2.get());
        event.registerItem(createArmorExtension(), BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_1.get());
        event.registerItem(createArmorExtension(), BzItems.TRANS_BUMBLE_BEE_CHESTPLATE_2.get());
        event.registerItem(createArmorExtension(), BzItems.STINGLESS_BEE_HELMET_1.get());
        event.registerItem(createArmorExtension(), BzItems.STINGLESS_BEE_HELMET_2.get());
        event.registerItem(createArmorExtension(), BzItems.FLOWER_HEADWEAR.get());

        final LazySupplier<MobEffectRenderer> renderer = LazySupplier.of(() -> MobEffectRenderer.RENDERERS.get(BzEffects.HIDDEN.holder()));
        event.registerMobEffect(
            new IClientMobEffectExtensions() {
                @Override
                public boolean renderGuiIcon(MobEffectInstance instance, Gui gui, GuiGraphicsExtractor guiGraphics, int x, int y, float z, float alpha) {
                    return renderer.getOptional().map(r -> r.renderGuiIcon(instance, gui, guiGraphics, x, y, z, alpha)).orElse(false);
                }
            },
            BzEffects.HIDDEN.get());
    }

    private static IClientItemExtensions createArmorExtension() {
        return new IClientItemExtensions() {
            private ArmorModelProvider provider;

            @Override
            public @NotNull Model getHumanoidArmorModel(ItemStack itemStack, EquipmentClientInfo.LayerType layerType, Model original) {
                if (provider == null) {
                    provider = NeoforgeArmorProviders.get(itemStack.getItem());
                }

                if (original instanceof ElytraModel) {
                    return original;
                }

                return Objects.requireNonNullElse(provider.getFinalModel(itemStack, null, (HumanoidModel<? super HumanoidRenderState>) original), original);
            }

            @Override
            public int getDefaultDyeColor(ItemStack stack) {
                if (stack.is(BzItems.FLOWER_HEADWEAR.get())) {
                    return DyedItemColor.getOrDefault(stack, stack.is(BzItems.FLOWER_HEADWEAR.get()) ? -1682375 : 0);
                }
                return DyedItemColor.getOrDefault(stack, 0);
            }

            @Override
            public Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer, Identifier _default) {
                if (provider == null) {
                    provider = NeoforgeArmorProviders.get(stack.getItem());
                }

                return provider.getArmorTexture(stack);
            }
        };
    }

    private static void onRegisterScreens(RegisterMenuScreensEvent event) {
        BzRegisterMenuScreenEvent.EVENT.invoke(new BzRegisterMenuScreenEvent(event::register));
    }

    private static void onRegisterKeys(RegisterKeyMappingsEvent event) {
        BzRegisterKeyMappingEvent.EVENT.invoke(new BzRegisterKeyMappingEvent(event::register));
    }

    private static void onRegisterBlockColors(RegisterColorHandlersEvent.BlockTintSources event) {
        BzRegisterBlockColorEvent.EVENT.invoke(new BzRegisterBlockColorEvent(event::register));
    }

    private static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        BzRegisterEntityRenderersEvent.EVENT.invoke(new BzRegisterEntityRenderersEvent(event::registerEntityRenderer));
    }

    private static void onEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        BzRegisterEntityLayersEvent.EVENT.invoke(new BzRegisterEntityLayersEvent(event::registerLayerDefinition));
    }

    private static void onKeyInput(InputEvent.Key event) {
        BzKeyInputEvent.EVENT.invoke(new BzKeyInputEvent(event.getKeyEvent(), event.getAction()));
    }

    private static void onClientTickPost(ClientTickEvent.Post event) {
        StinglessBeeHelmet.decrementHighlightingCounter(GeneralUtilsClient.getClientPlayer());
        MusicHandler.tickMusicFader();
    }

    private static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        var level = Minecraft.getInstance().level;
        if(level == null) {
            return;
        }

        var camera = event.getCamera();
        float partialTick = (float) event.getPartialTick(); // this is always a float but NF did an oops
        CosmicCrystalRenderer.laserScreenShake(level, camera, partialTick, (CosmicCrystalRenderer.CameraOrientation) event);
    }

    public static void onBlockScreen(RenderBlockScreenEffectEvent event) {
        BzBlockRenderedOnScreenEvent.Type type = switch (event.getOverlayType()) {
            case BLOCK -> BzBlockRenderedOnScreenEvent.Type.BLOCK;
            case FIRE -> BzBlockRenderedOnScreenEvent.Type.FIRE;
            case WATER -> BzBlockRenderedOnScreenEvent.Type.WATER;
        };
        event.setCanceled(BzBlockRenderedOnScreenEvent.EVENT.invoke(new BzBlockRenderedOnScreenEvent(
                event.getPlayer(), event.getPoseStack(), event.getBufferSource(), type, event.getBlockState(), event.getBlockPos())));
    }

    public static void onRegisterParticles(RegisterParticleProvidersEvent event) {
        BzRegisterParticleEvent.EVENT.invoke(new BzRegisterParticleEvent(NeoForgeClientEventManager.registerParticle(event)));
    }

    private static BzRegisterParticleEvent.Registrar registerParticle(RegisterParticleProvidersEvent event) {
        return new BzRegisterParticleEvent.Registrar() {
            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> registration) {
                event.registerSpriteSet(type, registration::apply);
            }
        };
    }

    public static void onScreenRendering(ScreenEvent.Render.Pre event) {
        if (event.getScreen() instanceof LevelLoadingScreen levelLoadingScreen &&
            GeneralUtilsClient.getClientPlayer() != null &&
            ((LevelLoadingScreenInterface)levelLoadingScreen).theBumblezone$getNewLevel() == BzDimension.BZ_WORLD_KEY)
        {
            DimensionTeleportingScreen.renderScreenAndText(levelLoadingScreen, event.getGuiGraphics());
            event.setCanceled(true);
        }
    }

    public static void onGuiRendering(RenderGuiLayerEvent.Pre event) {
        if (Minecraft.getInstance().player != null && event.getName().equals(VanillaGuiLayers.HOTBAR)) {
            EssenceOverlay.essenceItemOverlay(Minecraft.getInstance().player, event.getGuiGraphics());
            KnowingEssenceStructureMessage.inStructureMessage(Minecraft.getInstance().player, event.getGuiGraphics());
            RadianceEssenceArmorMessage.armorDurabilityMessage(Minecraft.getInstance().player, event.getGuiGraphics());
        }
    }

    public static final ContextKey<Long2ObjectOpenHashMap<List<KnowingEssenceLootBlockOutlining.CachedDrawData>>> KNOWING_ESSENCE_BLOCK_OUTLINES_KEY = new ContextKey<>(Identifier.fromNamespaceAndPath(Bumblezone.MODID, "knowing_essence_block_outlines"));
    public static void onLevelExtractRendering(ExtractLevelRenderStateEvent event) {
        var data = KnowingEssenceLootBlockOutlining.gatherLootBlocks(event.getLevel(), event.getCamera(), event.getFrustum());
        event.getRenderState().setRenderData(KNOWING_ESSENCE_BLOCK_OUTLINES_KEY, data);
    }

    public static void onBlockRenderStageRendering(RenderLevelStageEvent.AfterTranslucentBlocks event) {
        KnowingEssenceLootBlockOutlining.drawLootBlockOutlines(event.getLevelRenderState().getRenderData(KNOWING_ESSENCE_BLOCK_OUTLINES_KEY), event.getLevelRenderState().cameraRenderState.pos);
    }

    public static void onConditionalItemPropertiesSetup(RegisterConditionalItemModelPropertyEvent event) {
        BzHookupConditionalItemModelPropertiesEvent.EVENT.invoke(new BzHookupConditionalItemModelPropertiesEvent(event::register));
    }

    public static void onRangeSelectItemPropertiesSetup(RegisterRangeSelectItemModelPropertyEvent event) {
        BzHookupRangeSelectItemModelPropertiesEvent.EVENT.invoke(new BzHookupRangeSelectItemModelPropertiesEvent(event::register));
    }

    public static void onItemTintSourcesSetup(RegisterColorHandlersEvent.ItemTintSources event) {
        BzHookupItemTintSourcesEvent.EVENT.invoke(new BzHookupItemTintSourcesEvent(event::register));
    }
}
