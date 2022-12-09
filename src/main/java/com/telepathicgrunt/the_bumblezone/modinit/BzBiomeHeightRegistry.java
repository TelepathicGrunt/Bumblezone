package com.telepathicgrunt.the_bumblezone.modinit;

import com.mojang.serialization.Lifecycle;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.Optional;
import java.util.function.Supplier;

public final class BzBiomeHeightRegistry {
    private BzBiomeHeightRegistry() {}

    public static final ResourceKey<Registry<BiomeTerrain>> BIOME_HEIGHT_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Bumblezone.MODID, "biome_height"));
    public static Supplier<IForgeRegistry<BiomeTerrain>> BIOME_HEIGHT_REGISTRY;

    public static void initBiomeHeightRegistry() {
        BIOME_HEIGHT_REGISTRY.get().register(new ResourceLocation(Bumblezone.MODID, "hive_pillar"), new BiomeTerrain(22f, 0.35f));
        BIOME_HEIGHT_REGISTRY.get().register(new ResourceLocation(Bumblezone.MODID, "hive_wall"), new BiomeTerrain(19f, 0.25f));
        BIOME_HEIGHT_REGISTRY.get().register(new ResourceLocation(Bumblezone.MODID, "pollinated_fields"), new BiomeTerrain(5.4f, 0.9f));
        BIOME_HEIGHT_REGISTRY.get().register(new ResourceLocation(Bumblezone.MODID, "pollinated_pillar"), new BiomeTerrain(22.5f, 0.05f));
        BIOME_HEIGHT_REGISTRY.get().register(new ResourceLocation(Bumblezone.MODID, "sugar_water_floor"), new BiomeTerrain(-3.7f, 0.75f));
        BIOME_HEIGHT_REGISTRY.get().register(new ResourceLocation(Bumblezone.MODID, "crystal_canyon"), new BiomeTerrain(0f, 0.75f));
    }

    public static void createNewRegistry(NewRegistryEvent event) {
        BIOME_HEIGHT_REGISTRY = event.create(new RegistryBuilder<BiomeTerrain>()
                .setName(BIOME_HEIGHT_KEY.location())
                .setIDRange(1, Integer.MAX_VALUE - 1)
                .disableSaving());
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
