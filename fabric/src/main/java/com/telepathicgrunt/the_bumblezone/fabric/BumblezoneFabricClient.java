package com.telepathicgrunt.the_bumblezone.fabric;

import com.telepathicgrunt.the_bumblezone.client.BumblezoneClient;
import com.telepathicgrunt.the_bumblezone.client.bakemodel.FabricModelProvider;
import com.telepathicgrunt.the_bumblezone.client.fabric.FabricArmorRenderer;
import com.telepathicgrunt.the_bumblezone.events.client.*;
import com.telepathicgrunt.the_bumblezone.fluids.fabric.FabricSimpleFluiderRenderHandler;
import com.telepathicgrunt.the_bumblezone.platform.BlockExtension;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Function;

public class BumblezoneFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BumblezoneClient.init();
        FluidRenderHandlerRegistry fluidHandler = FluidRenderHandlerRegistry.INSTANCE;
        BuiltInRegistries.BLOCK.stream()
                .forEach(block -> {
                    if (block instanceof BlockExtension extension) {
                        OptionalBoolean result = extension.bz$shouldDisplayFluidOverlay();
                        if (result.isPresent()) {
                            fluidHandler.setBlockTransparency(block, result.get());
                        }
                    }
                });

        RegisterClientFluidPropertiesEvent.EVENT.invoke(new RegisterClientFluidPropertiesEvent(
                (info, properties) -> fluidHandler.register(info.source(), info.flowing(), new FabricSimpleFluiderRenderHandler(properties))));


        FabricArmorRenderer.setupArmor();
        RegisterParticleEvent.EVENT.invoke(new RegisterParticleEvent(BumblezoneFabricClient::registerParticle));
        RegisterEntityRenderersEvent.EVENT.invoke(new RegisterEntityRenderersEvent(EntityRendererRegistry::register));
        RegisterEntityLayersEvent.EVENT.invoke(new RegisterEntityLayersEvent((type, supplier) -> EntityModelLayerRegistry.registerModelLayer(type, supplier::get)));
        RegisterKeyMappingEvent.EVENT.invoke(new RegisterKeyMappingEvent(KeyBindingHelper::registerKeyBinding));
        RegisterDimensionEffectsEvent.EVENT.invoke(new RegisterDimensionEffectsEvent(DimensionRenderingRegistry::registerDimensionEffects));
        RegisterBlockColorEvent.EVENT.invoke(new RegisterBlockColorEvent(ColorProviderRegistry.BLOCK::register));
        RegisterItemColorEvent.EVENT.invoke(new RegisterItemColorEvent(ColorProviderRegistry.ITEM::register, ColorProviderRegistry.BLOCK::get));
        ClientTickEvents.START_CLIENT_TICK.register((mc) -> ClientTickEvent.EVENT.invoke(ClientTickEvent.START));
        ClientTickEvents.END_CLIENT_TICK.register((mc) -> ClientTickEvent.EVENT.invoke(ClientTickEvent.END));
        RegisterMenuScreenEvent.EVENT.invoke(new RegisterMenuScreenEvent(BumblezoneFabricClient::registerScreen));
        RegisterItemPropertiesEvent.EVENT.invoke(new RegisterItemPropertiesEvent(ItemProperties::register));
        RegisterRenderTypeEvent.EVENT.invoke(new RegisterRenderTypeEvent(BlockRenderLayerMap.INSTANCE::putFluid, BlockRenderLayerMap.INSTANCE::putBlock));

        ModelLoadingRegistry.INSTANCE.registerVariantProvider(manager -> new FabricModelProvider());
        RegisterEffectRenderersEvent.EVENT.invoke(RegisterEffectRenderersEvent.INSTANCE);
    }

    private static <T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> void registerScreen(MenuType<T> type, RegisterMenuScreenEvent.ScreenConstructor<T, U> provider) {
        MenuScreens.register(type, provider::create);
    }

    private static <T extends ParticleOptions> void registerParticle(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> factory) {
        ParticleFactoryRegistry.getInstance().register(type, factory::apply);
    }
}
