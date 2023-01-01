package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public final class BzBiomeHeightRegistry {
    private BzBiomeHeightRegistry() {}

    public static final ResourceKey<Registry<BiomeTerrain>> BIOME_HEIGHT_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Bumblezone.MODID, "biome_height"));
    public static final Registry<BiomeTerrain> BIOME_HEIGHT_REGISTRY = FabricRegistryBuilder.createSimple(BiomeTerrain.class, BIOME_HEIGHT_KEY.location()).buildAndRegister();

    public static void initBiomeHeightRegistry() {
        Optional<? extends Registry<?>> registryOptional = BuiltInRegistries.REGISTRY.getOptional(BIOME_HEIGHT_KEY.location());
        registryOptional.ifPresent(registry -> {
            Registry.register((Registry<BiomeTerrain>)registry,
                    new ResourceLocation(Bumblezone.MODID, "hive_pillar"), new BiomeTerrain(22f, 0.35f));
            Registry.register((Registry<BiomeTerrain>)registry,
                    new ResourceLocation(Bumblezone.MODID, "hive_wall"), new BiomeTerrain(19f, 0.25f));
            Registry.register((Registry<BiomeTerrain>)registry,
                    new ResourceLocation(Bumblezone.MODID, "pollinated_fields"), new BiomeTerrain(5.4f, 0.9f));
            Registry.register((Registry<BiomeTerrain>)registry,
                    new ResourceLocation(Bumblezone.MODID, "pollinated_pillar"), new BiomeTerrain(22.5f, 0.05f));
            Registry.register((Registry<BiomeTerrain>)registry,
                    new ResourceLocation(Bumblezone.MODID, "sugar_water_floor"), new BiomeTerrain(-3.7f, 0.75f));
            Registry.register((Registry<BiomeTerrain>)registry,
                    new ResourceLocation(Bumblezone.MODID, "crystal_canyon"), new BiomeTerrain(0f, 0.75f));
        });
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
