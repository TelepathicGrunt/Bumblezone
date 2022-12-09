package com.telepathicgrunt.the_bumblezone.world.dimension;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;

public class BiomeRegistryHolder {
    public static Registry<Biome> BIOME_REGISTRY;
    private static final ResourceLocation EMPTY_RL = new ResourceLocation("b", "empty");

    public static void setupBiomeRegistry(MinecraftServer server) {
        BIOME_REGISTRY = server.registryAccess().registryOrThrow(Registries.BIOME);
    }

    public static ResourceLocation convertToRL(int id) {
        if (id == -1) {
            return EMPTY_RL;
        }
        return BIOME_REGISTRY.getHolder(id).get().key().location();
    }

    public static int convertToID(ResourceLocation biome) {
        return BIOME_REGISTRY.getId(BIOME_REGISTRY.get(biome));
    }
}
