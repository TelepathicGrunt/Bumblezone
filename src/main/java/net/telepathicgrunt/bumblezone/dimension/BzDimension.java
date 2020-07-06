package net.telepathicgrunt.bumblezone.dimension;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.telepathicgrunt.bumblezone.Bumblezone;

public class BzDimension {
    public static final RegistryKey<DimensionType> BZ_DIMENSION_KEY = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, Bumblezone.MOD_FULL_ID);
    public static final RegistryKey<World> BZ_WORLD_KEY = RegistryKey.of(Registry.DIMENSION, Bumblezone.MOD_FULL_ID);
}
