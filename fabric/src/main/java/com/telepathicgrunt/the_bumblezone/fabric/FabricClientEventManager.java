package com.telepathicgrunt.the_bumblezone.fabric;

import com.telepathicgrunt.the_bumblezone.client.fabric.FabricArmorRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.KnowingEssenceLootBlockOutlining;
import com.telepathicgrunt.the_bumblezone.events.client.ClientSetupEnqueuedEvent;
import com.telepathicgrunt.the_bumblezone.events.client.ClientTickEvent;
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
import com.telepathicgrunt.the_bumblezone.events.client.RegisterRenderTypeEvent;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterShaderEvent;
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
        RegisterEntityRenderersEvent.EVENT.invoke(new RegisterEntityRenderersEvent(EntityRendererRegistry::register));
        RegisterEntityLayersEvent.EVENT.invoke(new RegisterEntityLayersEvent((type, supplier) -> EntityModelLayerRegistry.registerModelLayer(type, supplier::get)));
        RegisterKeyMappingEvent.EVENT.invoke(new RegisterKeyMappingEvent(KeyBindingHelper::registerKeyBinding));
        RegisterDimensionEffectsEvent.EVENT.invoke(new RegisterDimensionEffectsEvent(DimensionRenderingRegistry::registerDimensionEffects));
        RegisterBlockEntityRendererEvent.EVENT.invoke(new RegisterBlockEntityRendererEvent<>(BlockEntityRenderers::register));
        RegisterBlockColorEvent.EVENT.invoke(new RegisterBlockColorEvent(ColorProviderRegistry.BLOCK::register));
        RegisterItemColorEvent.EVENT.invoke(new RegisterItemColorEvent(ColorProviderRegistry.ITEM::register,
                (state, level, pos, i) -> ColorProviderRegistry.BLOCK.get(state.getBlock()).getColor(state, level, pos, i)));
        RegisterMenuScreenEvent.EVENT.invoke(new RegisterMenuScreenEvent(FabricClientEventManager::registerScreen));
        RegisterItemPropertiesEvent.EVENT.invoke(new RegisterItemPropertiesEvent(ItemProperties::register));
        RegisterRenderTypeEvent.EVENT.invoke(new RegisterRenderTypeEvent(BlockRenderLayerMap.INSTANCE::putFluid, BlockRenderLayerMap.INSTANCE::putBlock));
        RegisterShaderEvent.EVENT.invoke(new RegisterShaderEvent(
                (name, vertexFormat, safeShaderConsumer) -> CoreShaderRegistrationCallback.EVENT.register(
                        context -> context.register(name, vertexFormat, safeShaderConsumer))
                )
        );

        RegisterEffectRenderersEvent.EVENT.invoke(RegisterEffectRenderersEvent.INSTANCE);
        ClientSetupEnqueuedEvent.EVENT.invoke(new ClientSetupEnqueuedEvent(Runnable::run));

        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((worldRenderContext, result) -> {
            KnowingEssenceLootBlockOutlining.outlineLootBlocks(worldRenderContext.matrixStack(), worldRenderContext.camera(), worldRenderContext.worldRenderer());
            return true;
        });

        ClientTickEvents.START_CLIENT_TICK.register((mc) -> ClientTickEvent.EVENT.invoke(ClientTickEvent.START));
        ClientTickEvents.END_CLIENT_TICK.register((mc) -> ClientTickEvent.EVENT.invoke(ClientTickEvent.END));
    }

    private static <T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> void registerScreen(MenuType<T> type, RegisterMenuScreenEvent.ScreenConstructor<T, U> provider) {
        MenuScreens.register(type, provider::create);
    }
}
