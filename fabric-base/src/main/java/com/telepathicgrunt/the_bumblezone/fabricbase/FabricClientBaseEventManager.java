package com.telepathicgrunt.the_bumblezone.fabricbase;

import com.telepathicgrunt.the_bumblezone.client.fabric.FabricArmorRenderer;
import com.telepathicgrunt.the_bumblezone.client.rendering.essence.KnowingEssenceLootBlockOutlining;
import com.telepathicgrunt.the_bumblezone.events.client.*;
import com.telepathicgrunt.the_bumblezone.fluids.fabric.BiomeColorFluidRenderHandler;
import com.telepathicgrunt.the_bumblezone.fluids.fabric.HoneyFluidRenderHandler;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.platform.BlockExtension;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class FabricClientBaseEventManager {

    public static void init() {
        FluidRenderHandlerRegistry fluidHandler = FluidRenderHandlerRegistry.INSTANCE;
        BuiltInRegistries.BLOCK.stream()
                .forEach(block -> {
                    if (block instanceof BlockExtension extension) {
                        OptionalBoolean result = extension.bz$shouldNotDisplayFluidOverlay();
                        if (result.isPresent()) {
                            fluidHandler.setBlockTransparency(block, result.get());
                        }
                    }
                });

        RegisterClientFluidPropertiesEvent.EVENT.invoke(new RegisterClientFluidPropertiesEvent(
                (info, properties) -> {
                    if (info.block() == BzFluids.HONEY_FLUID_BLOCK.get() || info.block() == BzFluids.ROYAL_JELLY_FLUID_BLOCK.get()) {
                        fluidHandler.register(info.source(), info.flowing(), new HoneyFluidRenderHandler(properties));
                    }
                    else {
                        fluidHandler.register(info.source(), info.flowing(), new BiomeColorFluidRenderHandler(properties));
                    }
                }));

        FabricArmorRenderer.setupArmor();
        RegisterEntityRenderersEvent.EVENT.invoke(new RegisterEntityRenderersEvent(EntityRendererRegistry::register));
        RegisterEntityLayersEvent.EVENT.invoke(new RegisterEntityLayersEvent((type, supplier) -> EntityModelLayerRegistry.registerModelLayer(type, supplier::get)));
        RegisterKeyMappingEvent.EVENT.invoke(new RegisterKeyMappingEvent(KeyBindingHelper::registerKeyBinding));
        RegisterDimensionEffectsEvent.EVENT.invoke(new RegisterDimensionEffectsEvent(DimensionRenderingRegistry::registerDimensionEffects));
        RegisterBlockEntityRendererEvent.EVENT.invoke(new RegisterBlockEntityRendererEvent<>(BlockEntityRenderers::register));
        RegisterBlockColorEvent.EVENT.invoke(new RegisterBlockColorEvent(ColorProviderRegistry.BLOCK::register));
        RegisterItemColorEvent.EVENT.invoke(new RegisterItemColorEvent(ColorProviderRegistry.ITEM::register,
                (state, level, pos, i) -> ColorProviderRegistry.BLOCK.get(state.getBlock()).getColor(state, level, pos, i)));
        RegisterMenuScreenEvent.EVENT.invoke(new RegisterMenuScreenEvent(FabricClientBaseEventManager::registerScreen));
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
    }

    private static <T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> void registerScreen(MenuType<T> type, RegisterMenuScreenEvent.ScreenConstructor<T, U> provider) {
        MenuScreens.register(type, provider::create);
    }
}
