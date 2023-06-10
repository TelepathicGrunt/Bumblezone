package com.telepathicgrunt.the_bumblezone.forge;

import com.telepathicgrunt.the_bumblezone.client.BumblezoneClient;
import com.telepathicgrunt.the_bumblezone.client.DimensionTeleportingScreen;
import com.telepathicgrunt.the_bumblezone.client.forge.ForgeConnectedBlockModel;
import com.telepathicgrunt.the_bumblezone.client.forge.ForgeConnectedModelLoader;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.KnowingEssenceLootBlockOutlining;
import com.telepathicgrunt.the_bumblezone.events.client.BlockRenderedOnScreenEvent;
import com.telepathicgrunt.the_bumblezone.events.client.ClientSetupEnqueuedEvent;
import com.telepathicgrunt.the_bumblezone.events.client.ClientTickEvent;
import com.telepathicgrunt.the_bumblezone.events.client.KeyInputEvent;
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
import com.telepathicgrunt.the_bumblezone.items.DispenserAddedSpawnEgg;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RenderBlockScreenEffectEvent;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Function;

public class BumblezoneForgeClient {


    public static void init() {
        BumblezoneClient.init();

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        forgeBus.addListener(BumblezoneForgeClient::onBlockScreen);
        forgeBus.addListener(BumblezoneForgeClient::onKeyInput);
        forgeBus.addListener(BumblezoneForgeClient::onClientTick);
        forgeBus.addListener(BumblezoneForgeClient::onScreenRendering);
        forgeBus.addListener(BumblezoneForgeClient::onBeforeBlockOutlineRendering);

        modBus.addListener(BumblezoneForgeClient::onClientSetup);
        modBus.addListener(BumblezoneForgeClient::onRegisterModelLoaders);
        modBus.addListener(BumblezoneForgeClient::onRegisterParticles);
        modBus.addListener(ForgeConnectedBlockModel::onBakingCompleted);
        modBus.addListener(BumblezoneForgeClient::onRegisterKeys);
        modBus.addListener(BumblezoneForgeClient::onRegisterItemColors);
        modBus.addListener(BumblezoneForgeClient::onRegisterBlockColors);
        modBus.addListener(BumblezoneForgeClient::onRegisterEntityRenderers);
        modBus.addListener(BumblezoneForgeClient::onEntityLayers);
        modBus.addListener(BumblezoneForgeClient::onRegisterDimensionEffects);

        RegisterClientFluidPropertiesEvent.EVENT.invoke(new RegisterClientFluidPropertiesEvent((info, properties) -> {}));
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ClientSetupEnqueuedEvent.EVENT.invoke(new ClientSetupEnqueuedEvent(Runnable::run));
            RegisterEffectRenderersEvent.EVENT.invoke(RegisterEffectRenderersEvent.INSTANCE);
            RegisterRenderTypeEvent.EVENT.invoke(new RegisterRenderTypeEvent(ItemBlockRenderTypes::setRenderLayer, ItemBlockRenderTypes::setRenderLayer));
            RegisterMenuScreenEvent.EVENT.invoke(new RegisterMenuScreenEvent(BumblezoneForgeClient::registerScreen));
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

    private static void onKeyInput(InputEvent.Key event) {
        KeyInputEvent.EVENT.invoke(new KeyInputEvent(event.getKey(), event.getScanCode(), event.getAction()));
    }

    private static void onClientTick(TickEvent.ClientTickEvent event) {
        ClientTickEvent.EVENT.invoke(new ClientTickEvent(event.phase == TickEvent.Phase.END));
    }

    public static void onBlockScreen(RenderBlockScreenEffectEvent event) {
        BlockRenderedOnScreenEvent.Type type = switch (event.getOverlayType()) {
            case BLOCK -> BlockRenderedOnScreenEvent.Type.BLOCK;
            case FIRE -> BlockRenderedOnScreenEvent.Type.FIRE;
            case WATER -> BlockRenderedOnScreenEvent.Type.WATER;
        };
        BlockRenderedOnScreenEvent.EVENT.invoke(new BlockRenderedOnScreenEvent(
                event.getPlayer(), event.getPoseStack(), type, event.getBlockState(), event.getBlockPos()));
    }

    public static void onRegisterModelLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register("connected_block", new ForgeConnectedModelLoader());
    }

    public static void onRegisterParticles(RegisterParticleProvidersEvent event) {
        RegisterParticleEvent.EVENT.invoke(new RegisterParticleEvent(BumblezoneForgeClient.registerParticle(event)));
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
            Minecraft.getInstance().player != null &&
            Minecraft.getInstance().player.level().dimension() == BzDimension.BZ_WORLD_KEY)
        {
            DimensionTeleportingScreen.renderScreenAndText(receivingLevelScreen, event.getGuiGraphics());
            event.setCanceled(true);
        }
    }

    public static void onBeforeBlockOutlineRendering(RenderHighlightEvent event) {
        KnowingEssenceLootBlockOutlining.outlineLootBlocks(event.getPoseStack(), event.getCamera(), event.getLevelRenderer());
    }
 }
