package com.telepathicgrunt.the_bumblezone.fabric;

import com.telepathicgrunt.the_bumblezone.platform.BlockExtension;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.core.registries.BuiltInRegistries;

public class BumblezoneFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BuiltInRegistries.BLOCK.stream()
                .forEach(block -> {
                    if (block instanceof BlockExtension extension) {
                        OptionalBoolean result = extension.bz$shouldDisplayFluidOverlay();
                        if (result.isPresent()) {
                            FluidRenderHandlerRegistry.INSTANCE.setBlockTransparency(block, result.get());
                        }
                    }
                });
    }
}
