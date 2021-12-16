package com.telepathicgrunt.the_bumblezone.modinit;

import com.mojang.serialization.Lifecycle;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public final class BzBiomeHeightRegistry {
    private BzBiomeHeightRegistry() {}

    public static final ResourceKey<Registry<BiomeTerrain>> BIOME_HEIGHT_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Bumblezone.MODID, "biome_height"));
    public static final Registry<BiomeTerrain> BIOME_HEIGHT_REGISTRY = createRegistry(BIOME_HEIGHT_KEY);

    public static void initBiomeHeightRegistry() {
        Optional<? extends Registry<?>> registryOptional = Registry.REGISTRY.getOptional(BIOME_HEIGHT_KEY.location());
        registryOptional.ifPresent(registry -> {
            Registry.register((Registry<BiomeTerrain>)registry,
                    new ResourceLocation(Bumblezone.MODID, "hive_pillar"), new BiomeTerrain(15, 1));
            Registry.register((Registry<BiomeTerrain>)registry,
                    new ResourceLocation(Bumblezone.MODID, "hive_wall"), new BiomeTerrain(8, 1.2f));
            Registry.register((Registry<BiomeTerrain>)registry,
                    new ResourceLocation(Bumblezone.MODID, "pollinated_fields"), new BiomeTerrain(3, 3));
            Registry.register((Registry<BiomeTerrain>)registry,
                    new ResourceLocation(Bumblezone.MODID, "pollinated_pillar"), new BiomeTerrain(15, 1));
            Registry.register((Registry<BiomeTerrain>)registry,
                    new ResourceLocation(Bumblezone.MODID, "sugar_water_floor"), new BiomeTerrain(-3.5f, 1));
        });
    }

    private static <T, R extends Registry<T>> R createRegistry(ResourceKey<R> resourceKey) {
        return ((WritableRegistry<R>)Registry.REGISTRY).register(resourceKey, (R)new MappedRegistry<>(resourceKey, Lifecycle.stable()), Lifecycle.stable());
    }

    public static class BiomeTerrain{
        public final float depth;
        public final float weightModifier;

        public BiomeTerrain(float depth, float weightModifier) {
            this.depth = depth;
            this.weightModifier = weightModifier;
        }
    }
}
