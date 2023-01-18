package com.telepathicgrunt.the_bumblezone.fluids.fabric;

import com.telepathicgrunt.the_bumblezone.fluids.base.ClientFluidProperties;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public class FabricSimpleFluiderRenderHandler extends SimpleFluidRenderHandler {

    private final ClientFluidProperties properties;

    public FabricSimpleFluiderRenderHandler(ClientFluidProperties properties) {
        super(properties.still(), properties.flowing(), properties.overlay());
        this.properties = properties;
    }

    @Override
    public int getFluidColor(@Nullable BlockAndTintGetter view, @Nullable BlockPos pos, FluidState state) {
        return this.properties.tintColor(state, view, pos);
    }
}
