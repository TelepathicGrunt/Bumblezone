package com.telepathicgrunt.the_bumblezone.worldgen.dimension;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;

public class BiomeRegistryHolder {
    public static Registry<Biome> BIOME_REGISTRY;
    private static final Identifier EMPTY_RL = Identifier.fromNamespaceAndPath("b", "empty");

    public static void setupBiomeRegistry(MinecraftServer server) {
        BIOME_REGISTRY = server.registryAccess().getOrThrow(Registries.BIOME).value();
    }

    public static Identifier convertToRL(int id) {
        if (id == -1) {
            return EMPTY_RL;
        }
        return BIOME_REGISTRY.get(id).get().key().location();
    }

    public static int convertToID(Identifier biome) {
        return BIOME_REGISTRY.getId(BIOME_REGISTRY.getValue(biome));
    }
}
