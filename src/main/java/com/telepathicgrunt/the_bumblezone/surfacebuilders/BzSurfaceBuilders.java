package com.telepathicgrunt.the_bumblezone.surfacebuilders;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class BzSurfaceBuilders {
    public static final DeferredRegister<SurfaceBuilder<?>> SURFACE_BUILDERS = DeferredRegister.create(ForgeRegistries.SURFACE_BUILDERS, Bumblezone.MODID);

    public static final RegistryObject<SurfaceBuilder<SurfaceBuilderConfig>> HONEY_SURFACE_BUILDER = createSurfaceBuilder("honey_surface_builder", () -> new HoneySurfaceBuilder(SurfaceBuilderConfig.field_237203_a_));

    public static <S extends SurfaceBuilder<?>> RegistryObject<S> createSurfaceBuilder(String name, Supplier<? extends S> surfaceBuilder) {
        return SURFACE_BUILDERS.register(name, surfaceBuilder);
    }
}
