package com.telepathicgrunt.the_bumblezone.events.client;

import com.telepathicgrunt.the_bumblezone.events.base.EventHandler;
import com.telepathicgrunt.the_bumblezone.fluids.base.ClientFluidProperties;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfo;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidProperties;

import java.util.function.BiConsumer;
import java.util.function.Function;

public record RegisterClientFluidPropertiesEvent(BiConsumer<FluidInfo, ClientFluidProperties> registrar) {

    public static final EventHandler<RegisterClientFluidPropertiesEvent> EVENT = new EventHandler<>();

    public void register(FluidInfo info, ClientFluidProperties properties) {
        registrar.accept(info, properties);
    }

    public void register(FluidInfo info, Function<FluidProperties, ClientFluidProperties> properties) {
        registrar.accept(info, properties.apply(info.properties()));
    }
}
