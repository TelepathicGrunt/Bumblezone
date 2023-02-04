package com.telepathicgrunt.the_bumblezone.fluids.base;

import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

public interface FluidInfoRegistry extends ResourcefulRegistry<FluidInfo> {

    RegistryEntry<FluidInfo> register(FluidProperties.Builder properties);

    @Override
    @Deprecated
    @ApiStatus.Internal
    default <I extends FluidInfo> RegistryEntry<I> register(String id, Supplier<I> supplier) {
        throw new UnsupportedOperationException();
    }

}
