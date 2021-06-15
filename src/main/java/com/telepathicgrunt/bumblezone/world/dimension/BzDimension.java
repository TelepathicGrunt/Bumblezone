package com.telepathicgrunt.bumblezone.world.dimension;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.modinit.BzSurfaceBuilders;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class BzDimension {
    public static final RegistryKey<World> BZ_WORLD_KEY = RegistryKey.of(Registry.WORLD_KEY, Bumblezone.MOD_DIMENSION_ID);

    public static void setupDimension(){
        BzChunkGenerator.registerChunkgenerator();
        BzBiomeProvider.registerBiomeProvider();

        BzSurfaceBuilders.registerSurfaceBuilders();
    }
}
