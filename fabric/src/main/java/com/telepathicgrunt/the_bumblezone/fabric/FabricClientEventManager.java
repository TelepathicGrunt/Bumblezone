package com.telepathicgrunt.the_bumblezone.fabric;

import com.telepathicgrunt.the_bumblezone.client.fabric.FabricArmorRenderer;
import com.telepathicgrunt.the_bumblezone.client.fabric.GlisteringHoneyCrystalModels;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.KnowingEssenceLootBlockOutlining;
import com.telepathicgrunt.the_bumblezone.client.screens.DimensionTeleportingScreen;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.events.client.BzClientSetupEnqueuedEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterBlockColorEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterBlockEntityRendererEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterEffectRenderersEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterEntityLayersEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterEntityRenderersEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterItemColorEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterItemPropertiesEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterKeyMappingEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterMenuScreenEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterParticleEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterRenderTypeEvent;
import com.telepathicgrunt.the_bumblezone.items.StinglessBeeHelmet;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzDimension;
import com.telepathicgrunt.the_bumblezone.platform.BlockExtension;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Function;

public class FabricClientEventManager {

    public static void init() {
        FluidRenderHandlerRegistry fluidHandler = FluidRenderHandlerRegistry.INSTANCE;
        BzBlocks.BLOCKS.getEntries()
                .forEach(blockRegistryEntry -> {
                    if (blockRegistryEntry.get() instanceof BlockExtension extension) {
                        OptionalBoolean result = extension.bz$shouldNotDisplayFluidOverlay();
                        if (result.isPresent()) {
                            fluidHandler.setBlockTransparency(blockRegistryEntry.get(), result.get());
                        }
                    }
                });

        GlisteringHoneyCrystalModels.setupModels();
        FabricArmorRenderer.setupArmor();
        BzRegisterParticleEvent.EVENT.invoke(new BzRegisterParticleEvent(FabricClientEventManager::particleRegister));
        BzRegisterEntityRenderersEvent.EVENT.invoke(new BzRegisterEntityRenderersEvent(EntityRenderers::register));
        BzRegisterEntityLayersEvent.EVENT.invoke(new BzRegisterEntityLayersEvent((type, supplier) -> EntityModelLayerRegistry.registerModelLayer(type, supplier::get)));
        BzRegisterKeyMappingEvent.EVENT.invoke(new BzRegisterKeyMappingEvent(KeyMappingHelper::registerKeyMapping));
        BzRegisterBlockEntityRendererEvent.EVENT.invoke(new BzRegisterBlockEntityRendererEvent<>(BlockEntityRenderers::register));
        BzRegisterBlockColorEvent.EVENT.invoke(new BzRegisterBlockColorEvent(ColorProviderRegistry.BLOCK::register));
        BzRegisterItemColorEvent.EVENT.invoke(new BzRegisterItemColorEvent(ColorProviderRegistry.ITEM::register,
                (state, level, pos, i) -> ColorProviderRegistry.BLOCK.get(state.getBlock()).getColor(state, level, pos, i)));
        BzRegisterMenuScreenEvent.EVENT.invoke(new BzRegisterMenuScreenEvent(FabricClientEventManager::registerScreen));
        BzRegisterItemPropertiesEvent.EVENT.invoke(new BzRegisterItemPropertiesEvent(ItemProperties::register));
        BzRegisterRenderTypeEvent.EVENT.invoke(new BzRegisterRenderTypeEvent(BlockRenderLayerMap.INSTANCE::putFluid, BlockRenderLayerMap.INSTANCE::putBlock));

        BzRegisterEffectRenderersEvent.EVENT.invoke(BzRegisterEffectRenderersEvent.INSTANCE);
        BzClientSetupEnqueuedEvent.EVENT.invoke(new BzClientSetupEnqueuedEvent(Runnable::run));

        LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register((worldRenderContext, result) -> {
            KnowingEssenceLootBlockOutlining.outlineLootBlocks(worldRenderContext.poseStack(), worldRenderContext.levelState().cameraRenderState.pos, worldRenderContext.levelRenderer());
            return true;
        });

        ClientTickEvents.END_CLIENT_TICK.register((mc) -> StinglessBeeHelmet.decrementHighlightingCounter(GeneralUtilsClient.getClientPlayer()));
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof LevelLoadingScreen levelLoadingScreen &&
                    GeneralUtilsClient.getClientPlayer() != null &&
                    GeneralUtilsClient.getClientPlayer().level().dimension() == BzDimension.BZ_WORLD_KEY)
            {
                GuiGraphicsExtractor graphics = new GuiGraphicsExtractor(client, client., scaledWidth, scaledHeight);
                DimensionTeleportingScreen.renderScreenAndText(levelLoadingScreen, graphics);
            }
        });
    }

    private static <T extends ParticleOptions> void particleRegister(ParticleType<T> particleType, Function<SpriteSet, ParticleProvider<T>> spriteParticleRegistration) {
        ParticleProviderRegistry.getInstance().register(particleType, spriteParticleRegistration.apply(mutableSpriteSet));
    }

    private static <T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> void registerScreen(MenuType<T> type, BzRegisterMenuScreenEvent.ScreenConstructor<T, U> provider) {
        MenuScreens.register(type, provider::create);
    }
}
