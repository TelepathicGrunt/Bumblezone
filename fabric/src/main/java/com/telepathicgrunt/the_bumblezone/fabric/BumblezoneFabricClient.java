package com.telepathicgrunt.the_bumblezone.fabric;

import com.telepathicgrunt.the_bumblezone.client.armor.ArmorModelProvider;
import com.telepathicgrunt.the_bumblezone.client.fabric.FabricArmorRenderer;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterArmorProviderEvent;
import com.telepathicgrunt.the_bumblezone.events.client.RegisterClientFluidPropertiesEvent;
import com.telepathicgrunt.the_bumblezone.fluids.fabric.FabricSimpleFluiderRenderHandler;
import com.telepathicgrunt.the_bumblezone.platform.BlockExtension;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.core.registries.BuiltInRegistries;

public class BumblezoneFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        //TODO invoke main client class.
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
    }
}
