package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.util.Supplier;

import java.util.HashMap;
import java.util.Map;

public final class BzBiomeHeightRegistry {
    private BzBiomeHeightRegistry() {}

    public static final ResourceKey<Registry<BiomeTerrain>> BIOME_HEIGHT_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Bumblezone.MODID, "biome_height"));
//    public static final CustomRegistry<BiomeTerrain> BIOME_HEIGHT = ResourcefulRegistr.of(Bumblezone.MODID, BiomeTerrain.class, BIOME_HEIGHT_KEY, false, false, true);
//
//    public static final RegistryEntry<BiomeTerrain> HIVE_PILLAR = BIOME_HEIGHT.register("hive_pillar", () -> new BiomeTerrain(22f, 0.35f));
//    public static final RegistryEntry<BiomeTerrain> HIVE_WALL = BIOME_HEIGHT.register("hive_wall", () -> new BiomeTerrain(19f, 0.25f));
//    public static final RegistryEntry<BiomeTerrain> POLLINATED_FIELDS = BIOME_HEIGHT.register("pollinated_fields", () -> new BiomeTerrain(5.4f, 0.9f));
//    public static final RegistryEntry<BiomeTerrain> POLLINATED_PILLAR = BIOME_HEIGHT.register("pollinated_pillar", () -> new BiomeTerrain(22.5f, 0.05f));
//    public static final RegistryEntry<BiomeTerrain> SUGAR_WATER_FLOOR = BIOME_HEIGHT.register("sugar_water_floor", () -> new BiomeTerrain(-3.7f, 0.75f));
//    public static final RegistryEntry<BiomeTerrain> CRYSTAL_CANYON = BIOME_HEIGHT.register("crystal_canyon", () -> new BiomeTerrain(0f, 0.75f));
//    public static final RegistryEntry<BiomeTerrain> FLORAL_MEADOW = BIOME_HEIGHT.register("floral_meadow", () -> new BiomeTerrain(0.2f, 0.75f));
//    public static final RegistryEntry<BiomeTerrain> HOWLING_CONSTRUCTS = BIOME_HEIGHT.register("howling_constructs", () -> new BiomeTerrain(0.18f, 0.78f));
//
    public static final TemporaryFakeBiomeRegistry BIOME_HEIGHT = new TemporaryFakeBiomeRegistry();

    public static final Supplier<BiomeTerrain> HIVE_PILLAR = BIOME_HEIGHT.register("hive_pillar", () -> new BiomeTerrain(22f, 0.35f));
    public static final Supplier<BiomeTerrain> HIVE_WALL = BIOME_HEIGHT.register("hive_wall", () -> new BiomeTerrain(19f, 0.25f));
    public static final Supplier<BiomeTerrain> POLLINATED_FIELDS = BIOME_HEIGHT.register("pollinated_fields", () -> new BiomeTerrain(5.4f, 0.9f));
    public static final Supplier<BiomeTerrain> POLLINATED_PILLAR = BIOME_HEIGHT.register("pollinated_pillar", () -> new BiomeTerrain(22.5f, 0.05f));
    public static final Supplier<BiomeTerrain> SUGAR_WATER_FLOOR = BIOME_HEIGHT.register("sugar_water_floor", () -> new BiomeTerrain(-3.7f, 0.75f));
    public static final Supplier<BiomeTerrain> CRYSTAL_CANYON = BIOME_HEIGHT.register("crystal_canyon", () -> new BiomeTerrain(0f, 0.75f));
    public static final Supplier<BiomeTerrain> FLORAL_MEADOW = BIOME_HEIGHT.register("floral_meadow", () -> new BiomeTerrain(0.2f, 0.75f));
    public static final Supplier<BiomeTerrain> HOWLING_CONSTRUCTS = BIOME_HEIGHT.register("howling_constructs", () -> new BiomeTerrain(0.18f, 0.78f));

    public static class TemporaryFakeBiomeRegistry {
        private Map<ResourceLocation, BiomeTerrain> map = new HashMap<>();

        public Supplier<BiomeTerrain> register(String id, Supplier<BiomeTerrain> supplier) {
            map.put(new ResourceLocation(Bumblezone.MODID, id), supplier.get());
            return supplier;
        }

        public BiomeTerrain get(ResourceLocation location) {
            return map.get(location);
        }
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
