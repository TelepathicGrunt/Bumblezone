package com.telepathicgrunt.the_bumblezone.fabric;

import com.telepathicgrunt.the_bumblezone.client.fabric.FabricArmorRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.KnowingEssenceLootBlockOutlining;
import com.telepathicgrunt.the_bumblezone.client.screens.DimensionTeleportingScreen;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.events.client.BzClientSetupEnqueuedEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzHookupConditionalItemModelPropertiesEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzHookupItemTintSourcesEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzHookupRangeSelectItemModelPropertiesEvent;
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
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleResources;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Function;

public class FabricClientEventManager {

    public static void init() {
        FabricArmorRenderer.setupArmor();
        BzRegisterParticleEvent.EVENT.invoke(new BzRegisterParticleEvent(FabricClientEventManager::particleRegister));
        BzRegisterEntityRenderersEvent.EVENT.invoke(new BzRegisterEntityRenderersEvent(EntityRenderers::register));
        BzRegisterEntityLayersEvent.EVENT.invoke(new BzRegisterEntityLayersEvent(
                (modelLayerLocation, layerDefinitionSupplier) ->
                        ModelLayerRegistry.registerModelLayer(modelLayerLocation, layerDefinitionSupplier::get)));
        BzRegisterKeyMappingEvent.EVENT.invoke(new BzRegisterKeyMappingEvent(KeyMappingHelper::registerKeyMapping));
        BzRegisterBlockEntityRendererEvent.EVENT.invoke(new BzRegisterBlockEntityRendererEvent<>(BlockEntityRenderers::register));
        BzRegisterBlockColorEvent.EVENT.invoke(new BzRegisterBlockColorEvent(BlockColorRegistry::register));
        BzRegisterMenuScreenEvent.EVENT.invoke(new BzRegisterMenuScreenEvent(FabricClientEventManager::registerScreen));
        BzHookupConditionalItemModelPropertiesEvent.EVENT.invoke(new BzHookupConditionalItemModelPropertiesEvent(ConditionalItemModelProperties.ID_MAPPER::put));
        BzHookupRangeSelectItemModelPropertiesEvent.EVENT.invoke(new BzHookupRangeSelectItemModelPropertiesEvent(RangeSelectItemModelProperties.ID_MAPPER::put));
        BzHookupItemTintSourcesEvent.EVENT.invoke(new BzHookupItemTintSourcesEvent(ItemTintSources.ID_MAPPER::put));

        BzRegisterEffectRenderersEvent.EVENT.invoke(BzRegisterEffectRenderersEvent.INSTANCE);
        BzClientSetupEnqueuedEvent.EVENT.invoke(new BzClientSetupEnqueuedEvent(Runnable::run));

        LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register((worldRenderContext, result) -> {
            KnowingEssenceLootBlockOutlining.outlineLootBlocks(worldRenderContext.poseStack(), worldRenderContext.levelState().cameraRenderState.pos, worldRenderContext.levelRenderer());
            return true;
        });

        ClientTickEvents.END_CLIENT_TICK.register((mc) -> StinglessBeeHelmet.decrementHighlightingCounter(GeneralUtilsClient.getClientPlayer()));
        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof LevelLoadingScreen levelLoadingScreen &&
                    client.player != null &&
                    client.player.level().dimension() == BzDimension.BZ_WORLD_KEY)
            {
                ScreenEvents.afterExtract(levelLoadingScreen).register((screen1, graphics, mouseX, mouseY, tickProgress) ->
                        DimensionTeleportingScreen.renderScreenAndText((LevelLoadingScreen) screen1, graphics));
            }
        });
    }

    private static <T extends ParticleOptions> void particleRegister(ParticleType<T> particleType, Function<SpriteSet, ParticleProvider<T>> spriteParticleRegistration) {
        ParticleResources.MutableSpriteSet mutableSpriteSet = new ParticleResources.MutableSpriteSet();
        ParticleProviderRegistry.getInstance().register(particleType, spriteParticleRegistration.apply(mutableSpriteSet));
    }

    private static <T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> void registerScreen(MenuType<T> type, BzRegisterMenuScreenEvent.ScreenConstructor<T, U> provider) {
        MenuScreens.register(type, provider::create);
    }
}
