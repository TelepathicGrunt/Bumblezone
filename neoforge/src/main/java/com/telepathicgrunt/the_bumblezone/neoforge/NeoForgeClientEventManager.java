package com.telepathicgrunt.the_bumblezone.neoforge;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.client.DimensionTeleportingScreen;
import com.telepathicgrunt.the_bumblezone.client.neoforge.DimensionFog;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.EssenceOverlay;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.KnowingEssenceLootBlockOutlining;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.KnowingEssenceStructureMessage;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.RadianceEssenceArmorMessage;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.events.client.BlockRenderedOnScreenEvent;
import com.telepathicgrunt.the_bumblezone.events.client.ClientSetupEnqueuedEvent;
import com.telepathicgrunt.the_bumblezone.events.client.ClientTickEvent;
import com.telepathicgrunt.the_bumblezone.events.client.KeyInputEvent;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterBlockColorEvent;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterBlockEntityRendererEvent;
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
import com.telepathicgrunt.the_bumblezone.events.client.RegisterShaderEvent;
import com.telepathicgrunt.the_bumblezone.items.DispenserAddedSpawnEgg;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.event.RenderBlockScreenEffectEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

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
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ClientSetupEnqueuedEvent.EVENT.invoke(new ClientSetupEnqueuedEvent(Runnable::run));
            RegisterEffectRenderersEvent.EVENT.invoke(RegisterEffectRenderersEvent.INSTANCE);
            RegisterRenderTypeEvent.EVENT.invoke(new RegisterRenderTypeEvent(ItemBlockRenderTypes::setRenderLayer, ItemBlockRenderTypes::setRenderLayer));
            RegisterMenuScreenEvent.EVENT.invoke(new RegisterMenuScreenEvent(NeoForgeClientEventManager::registerScreen));
            RegisterItemPropertiesEvent.EVENT.invoke(new RegisterItemPropertiesEvent(ItemProperties::register));
            RegisterBlockEntityRendererEvent.EVENT.invoke(new RegisterBlockEntityRendererEvent<>(BlockEntityRenderers::register));
        });
    }

    private static <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void registerScreen(MenuType<? extends M> arg, RegisterMenuScreenEvent.ScreenConstructor<M, U> arg2) {
        MenuScreens.register(arg, arg2::create);
    }

    private static void onRegisterKeys(RegisterKeyMappingsEvent event) {
        RegisterKeyMappingEvent.EVENT.invoke(new RegisterKeyMappingEvent(event::register));
    }

    private static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        RegisterItemColorEvent.EVENT.invoke(new RegisterItemColorEvent(event::register, event.getBlockColors()::getColor));
        BzItems.ITEMS.stream()
                .map(RegistryEntry::get)
                .filter(item -> item instanceof DispenserAddedSpawnEgg)
                .map(item -> (DispenserAddedSpawnEgg) item)
                .forEach(item -> event.register((stack, index) -> item.getColor(index), item));
    }

    private static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        RegisterBlockColorEvent.EVENT.invoke(new RegisterBlockColorEvent(event::register));
    }

    private static void onRegisterEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        RegisterEntityRenderersEvent.EVENT.invoke(new RegisterEntityRenderersEvent(event::registerEntityRenderer));
    }

    private static void onEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        RegisterEntityLayersEvent.EVENT.invoke(new RegisterEntityLayersEvent(event::registerLayerDefinition));
    }

    private static void onRegisterDimensionEffects(RegisterDimensionSpecialEffectsEvent event) {
        RegisterDimensionEffectsEvent.EVENT.invoke(new RegisterDimensionEffectsEvent(event::register));
    }

    public static void onRegisterShaders(RegisterShadersEvent event) {
        RegisterShaderEvent.EVENT.invoke(new RegisterShaderEvent((name, vertexFormat, safeShaderCallback) -> {
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
        KeyInputEvent.EVENT.invoke(new KeyInputEvent(event.getKey(), event.getScanCode(), event.getAction()));
    }

    private static void onClientTickPre(net.neoforged.neoforge.client.event.ClientTickEvent.Pre event) {
        ClientTickEvent.EVENT.invoke(new ClientTickEvent(false));
    }

    private static void onClientTickPost(net.neoforged.neoforge.client.event.ClientTickEvent.Post event) {
        ClientTickEvent.EVENT.invoke(new ClientTickEvent(true));
    }

    public static void onBlockScreen(RenderBlockScreenEffectEvent event) {
        BlockRenderedOnScreenEvent.Type type = switch (event.getOverlayType()) {
            case BLOCK -> BlockRenderedOnScreenEvent.Type.BLOCK;
            case FIRE -> BlockRenderedOnScreenEvent.Type.FIRE;
            case WATER -> BlockRenderedOnScreenEvent.Type.WATER;
        };
        event.setCanceled(BlockRenderedOnScreenEvent.EVENT.invoke(new BlockRenderedOnScreenEvent(
                event.getPlayer(), event.getPoseStack(), type, event.getBlockState(), event.getBlockPos())));
    }

    public static void onRegisterParticles(RegisterParticleProvidersEvent event) {
        RegisterParticleEvent.EVENT.invoke(new RegisterParticleEvent(NeoForgeClientEventManager.registerParticle(event)));
    }

    private static RegisterParticleEvent.Registrar registerParticle(RegisterParticleProvidersEvent event) {
        return new RegisterParticleEvent.Registrar() {
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
