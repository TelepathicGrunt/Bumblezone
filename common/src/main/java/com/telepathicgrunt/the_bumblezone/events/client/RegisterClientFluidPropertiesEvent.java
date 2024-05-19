package com.telepathicgrunt.the_bumblezone.events.client;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import com.teamresourceful.resourcefullib.common.fluid.data.FluidProperties;
import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import com.telepathicgrunt.the_bumblezone.fluids.base.ClientFluidProperties;

import java.util.function.BiConsumer;
import java.util.function.Function;

public record RegisterClientFluidPropertiesEvent(BiConsumer<FluidData, ClientFluidProperties> registrar) {

    public static final EventHandler<RegisterClientFluidPropertiesEvent> EVENT = new EventHandler<>();

    public void register(FluidData info, ClientFluidProperties properties) {
        registrar.accept(info, properties);
    }

    public void register(FluidData info, Function<FluidProperties, ClientFluidProperties> properties) {
        registrar.accept(info, properties.apply(info.properties()));
    }
}
