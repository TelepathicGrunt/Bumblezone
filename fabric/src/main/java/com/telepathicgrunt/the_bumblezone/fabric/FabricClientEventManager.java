package com.telepathicgrunt.the_bumblezone.fabric;

import com.telepathicgrunt.the_bumblezone.client.fabric.FabricArmorRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.KnowingEssenceLootBlockOutlining;
import com.telepathicgrunt.the_bumblezone.events.client.BzClientSetupEnqueuedEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzClientTickEvent;
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
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterRenderTypeEvent;
import com.telepathicgrunt.the_bumblezone.events.client.BzRegisterShaderEvent;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.platform.BlockExtension;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

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

        FabricArmorRenderer.setupArmor();
        BzRegisterEntityRenderersEvent.EVENT.invoke(new BzRegisterEntityRenderersEvent(EntityRendererRegistry::register));
        BzRegisterEntityLayersEvent.EVENT.invoke(new BzRegisterEntityLayersEvent((type, supplier) -> EntityModelLayerRegistry.registerModelLayer(type, supplier::get)));
        BzRegisterKeyMappingEvent.EVENT.invoke(new BzRegisterKeyMappingEvent(KeyBindingHelper::registerKeyBinding));
        BzRegisterDimensionEffectsEvent.EVENT.invoke(new BzRegisterDimensionEffectsEvent(DimensionRenderingRegistry::registerDimensionEffects));
        BzRegisterBlockEntityRendererEvent.EVENT.invoke(new BzRegisterBlockEntityRendererEvent<>(BlockEntityRenderers::register));
        BzRegisterBlockColorEvent.EVENT.invoke(new BzRegisterBlockColorEvent(ColorProviderRegistry.BLOCK::register));
        BzRegisterItemColorEvent.EVENT.invoke(new BzRegisterItemColorEvent(ColorProviderRegistry.ITEM::register,
                (state, level, pos, i) -> ColorProviderRegistry.BLOCK.get(state.getBlock()).getColor(state, level, pos, i)));
        BzRegisterMenuScreenEvent.EVENT.invoke(new BzRegisterMenuScreenEvent(FabricClientEventManager::registerScreen));
        BzRegisterItemPropertiesEvent.EVENT.invoke(new BzRegisterItemPropertiesEvent(ItemProperties::register));
        BzRegisterRenderTypeEvent.EVENT.invoke(new BzRegisterRenderTypeEvent(BlockRenderLayerMap.INSTANCE::putFluid, BlockRenderLayerMap.INSTANCE::putBlock));
        BzRegisterShaderEvent.EVENT.invoke(new BzRegisterShaderEvent(
                (name, vertexFormat, safeShaderConsumer) -> CoreShaderRegistrationCallback.EVENT.register(
                        context -> context.register(name, vertexFormat, safeShaderConsumer))
                )
        );

        BzRegisterEffectRenderersEvent.EVENT.invoke(BzRegisterEffectRenderersEvent.INSTANCE);
        BzClientSetupEnqueuedEvent.EVENT.invoke(new BzClientSetupEnqueuedEvent(Runnable::run));

        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((worldRenderContext, result) -> {
            KnowingEssenceLootBlockOutlining.outlineLootBlocks(worldRenderContext.matrixStack(), worldRenderContext.camera(), worldRenderContext.worldRenderer());
            return true;
        });

        ClientTickEvents.START_CLIENT_TICK.register((mc) -> BzClientTickEvent.EVENT.invoke(BzClientTickEvent.START));
        ClientTickEvents.END_CLIENT_TICK.register((mc) -> BzClientTickEvent.EVENT.invoke(BzClientTickEvent.END));
    }

    private static <T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> void registerScreen(MenuType<T> type, BzRegisterMenuScreenEvent.ScreenConstructor<T, U> provider) {
        MenuScreens.register(type, provider::create);
    }
}
