package com.telepathicgrunt.the_bumblezone.neoforge;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.client.DimensionTeleportingScreen;
import com.telepathicgrunt.the_bumblezone.client.armor.ArmorModelProvider;
import com.telepathicgrunt.the_bumblezone.client.neoforge.DimensionFog;
import com.telepathicgrunt.the_bumblezone.client.neoforge.NeoforgeArmorProviders;
import com.telepathicgrunt.the_bumblezone.client.rendering.MobEffectRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.EssenceOverlay;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.KnowingEssenceLootBlockOutlining;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.KnowingEssenceStructureMessage;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.RadianceEssenceArmorMessage;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.effects.BzEffect;
import com.telepathicgrunt.the_bumblezone.events.client.BzBlockRenderedOnScreenEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzClientSetupEnqueuedEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzClientTickEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzKeyInputEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterBlockColorEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterBlockEntityRendererEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterDimensionEffectsEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterEffectRenderersEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterEntityLayersEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterEntityRenderersEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterItemColorEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterItemPropertiesEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterKeyMappingEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterMenuScreenEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterParticleEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterRenderTypeEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterShaderEvent;
import com.telepathicgrunt.the_bumblezone.items.DispenserAddedSpawnEgg;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import com.telepathicgrunt.the_bumblezone.modinit.BzEffects;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.utils.LazySupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.event.RenderBlockScreenEffectEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.Function;

public class NeoForgeClientEventManager {

    public static void init(IEventBus modEventBus, IEventBus eventBus) {

        eventBus.addListener(NeoForgeClientEventManager::onBlockScreen);
        eventBus.addListener(NeoForgeClientEventManager::onKeyInput);
        eventBus.addListener(NeoForgeClientEventManager::onClientTickPre);
        eventBus.addListener(NeoForgeClientEventManager::onClientTickPost);
        eventBus.addListener(NeoForgeClientEventManager::onScreenRendering);
        eventBus.addListener(NeoForgeClientEventManager::onBeforeBlockOutlineRendering);
        eventBus.addListener(NeoForgeClientEventManager::onGuiRendering);
        eventBus.addListener(EventPriority.HIGHEST, true, DimensionFog::fogThicknessAdjustments);

        modEventBus.addListener(NeoForgeClientEventManager::onClientSetup);
        modEventBus.addListener(NeoForgeClientEventManager::onRegisterParticles);
        modEventBus.addListener(NeoForgeClientEventManager::onRegisterShaders);
        modEventBus.addListener(NeoForgeClientEventManager::onRegisterKeys);
        modEventBus.addListener(NeoForgeClientEventManager::onRegisterItemColors);
        modEventBus.addListener(NeoForgeClientEventManager::onRegisterBlockColors);
        modEventBus.addListener(NeoForgeClientEventManager::onRegisterEntityRenderers);
        modEventBus.addListener(NeoForgeClientEventManager::onEntityLayers);
        modEventBus.addListener(NeoForgeClientEventManager::onRegisterDimensionEffects);
        modEventBus.addListener(NeoForgeClientEventManager::onRegisterScreens);
        modEventBus.addListener(NeoForgeClientEventManager::onRegisterClientExtensions);
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BzClientSetupEnqueuedEvent.EVENT.invoke(new BzClientSetupEnqueuedEvent(Runnable::run));
            BzRegisterEffectRenderersEvent.EVENT.invoke(BzRegisterEffectRenderersEvent.INSTANCE);
            BzRegisterRenderTypeEvent.EVENT.invoke(new BzRegisterRenderTypeEvent(ItemBlockRenderTypes::setRenderLayer, ItemBlockRenderTypes::setRenderLayer));
            BzRegisterItemPropertiesEvent.EVENT.invoke(new BzRegisterItemPropertiesEvent(ItemProperties::register));
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
                public boolean renderGuiIcon(MobEffectInstance instance, Gui gui, GuiGraphics guiGraphics, int x, int y, float z, float alpha) {
                    return renderer.getOptional().map(r -> r.renderGuiIcon(instance, gui, guiGraphics, x, y, z, alpha)).orElse(false);
                }
            },
            BzEffects.HIDDEN.get());
    }

    private static IClientItemExtensions createArmorExtension() {
        return new IClientItemExtensions() {
            private ArmorModelProvider provider;

            @Override
            public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (provider == null) {
                    provider = NeoforgeArmorProviders.get(itemStack.getItem());
                }
                return provider.getFinalModel(livingEntity, itemStack, equipmentSlot, original);
            }
        };
    }

    private static void onRegisterScreens(RegisterMenuScreensEvent event) {
        BzRegisterMenuScreenEvent.EVENT.invoke(new BzRegisterMenuScreenEvent(event::register));
    }

    private static void onRegisterKeys(RegisterKeyMappingsEvent event) {
        BzRegisterKeyMappingEvent.EVENT.invoke(new BzRegisterKeyMappingEvent(event::register));
    }

    private static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        BzRegisterItemColorEvent.EVENT.invoke(new BzRegisterItemColorEvent(event::register, event.getBlockColors()::getColor));
        BzItems.ITEMS.stream()
                .map(RegistryEntry::get)
                .filter(item -> item instanceof DispenserAddedSpawnEgg)
                .map(item -> (DispenserAddedSpawnEgg) item)
                .forEach(item -> event.register((stack, index) -> item.getColor(index), item));
    }

    private static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        BzRegisterBlockColorEvent.EVENT.invoke(new BzRegisterBlockColorEvent(event::register));
    }

    private static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        BzRegisterEntityRenderersEvent.EVENT.invoke(new BzRegisterEntityRenderersEvent(event::registerEntityRenderer));
    }

    private static void onEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        BzRegisterEntityLayersEvent.EVENT.invoke(new BzRegisterEntityLayersEvent(event::registerLayerDefinition));
    }

    private static void onRegisterDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
        BzRegisterDimensionEffectsEvent.EVENT.invoke(new BzRegisterDimensionEffectsEvent(event::register));
    }

    public static void onRegisterShaders(RegisterShadersEvent event) {
        BzRegisterShaderEvent.EVENT.invoke(new BzRegisterShaderEvent((name, vertexFormat, safeShaderCallback) -> {
            ShaderInstance shaderInstance;
            try {
                shaderInstance = new ShaderInstance(Minecraft.getInstance().getResourceManager(), name, vertexFormat);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            event.registerShader(shaderInstance, safeShaderCallback);
        }));
    }

    private static void onKeyInput(InputEvent.Key event) {
        BzKeyInputEvent.EVENT.invoke(new BzKeyInputEvent(event.getKey(), event.getScanCode(), event.getAction()));
    }

    private static void onClientTickPre(ClientTickEvent.Pre event) {
        BzClientTickEvent.EVENT.invoke(new BzClientTickEvent(false));
    }

    private static void onClientTickPost(ClientTickEvent.Post event) {
        BzClientTickEvent.EVENT.invoke(new BzClientTickEvent(true));
    }

    public static void onBlockScreen(RenderBlockScreenEffectEvent event) {
        BzBlockRenderedOnScreenEvent.Type type = switch (event.getOverlayType()) {
            case BLOCK -> BzBlockRenderedOnScreenEvent.Type.BLOCK;
            case FIRE -> BzBlockRenderedOnScreenEvent.Type.FIRE;
            case WATER -> BzBlockRenderedOnScreenEvent.Type.WATER;
        };
        event.setCanceled(BzBlockRenderedOnScreenEvent.EVENT.invoke(new BzBlockRenderedOnScreenEvent(
                event.getPlayer(), event.getPoseStack(), type, event.getBlockState(), event.getBlockPos())));
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
        if (event.getScreen() instanceof ReceivingLevelScreen receivingLevelScreen &&
            GeneralUtilsClient.getClientPlayer() != null &&
            GeneralUtilsClient.getClientPlayer().level().dimension() == BzDimension.BZ_WORLD_KEY)
        {
            DimensionTeleportingScreen.renderScreenAndText(receivingLevelScreen, event.getGuiGraphics());
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

    public static void onBeforeBlockOutlineRendering(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            KnowingEssenceLootBlockOutlining.outlineLootBlocks(event.getPoseStack(), event.getCamera(), event.getLevelRenderer());
        }
    }
}
