package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.util.Supplier;

import java.util.HashMap;
import java.util.Map;

public final class BzBiomeHeightRegistry {
    private BzBiomeHeightRegistry() {}

    public static final TemporaryFakeBiomeRegistry BIOME_HEIGHT = new TemporaryFakeBiomeRegistry();

    public static final Supplier<BiomeTerrain> HIVE_PILLAR = BIOME_HEIGHT.register("hive_pillar", () -> new BiomeTerrain(22f, 0.35f));
    public static final Supplier<BiomeTerrain> HIVE_WALL = BIOME_HEIGHT.register("hive_wall", () -> new BiomeTerrain(19f, 0.25f));
    public static final Supplier<BiomeTerrain> POLLINATED_FIELDS = BIOME_HEIGHT.register("pollinated_fields", () -> new BiomeTerrain(5.4f, 0.9f));
    public static final Supplier<BiomeTerrain> POLLINATED_PILLAR = BIOME_HEIGHT.register("pollinated_pillar", () -> new BiomeTerrain(22.5f, 0.05f));
    public static final Supplier<BiomeTerrain> SUGAR_WATER_FLOOR = BIOME_HEIGHT.register("sugar_water_floor", () -> new BiomeTerrain(-3.7f, 0.75f));
    public static final Supplier<BiomeTerrain> CRYSTAL_CANYON = BIOME_HEIGHT.register("crystal_canyon", () -> new BiomeTerrain(0f, 0.75f));
    public static final Supplier<BiomeTerrain> FLORAL_MEADOW = BIOME_HEIGHT.register("floral_meadow", () -> new BiomeTerrain(0.2f, 0.75f));
    public static final Supplier<BiomeTerrain> HOWLING_CONSTRUCTS = BIOME_HEIGHT.register("howling_constructs", () -> new BiomeTerrain(0.18f, 0.78f));
    public static final Supplier<BiomeTerrain> BUMBLING_BEEPARTMENTS = BIOME_HEIGHT.register("bumbling_beepartments", () -> new BiomeTerrain(18.5f, 0.225f));

    public static class TemporaryFakeBiomeRegistry {
        private final Map<Identifier, BiomeTerrain> map = new HashMap<>();

        public Supplier<BiomeTerrain> register(String id, Supplier<BiomeTerrain> supplier) {
            map.put(Identifier.fromNamespaceAndPath(Bumblezone.MODID, id), supplier.get());
            return supplier;
        }

        public BiomeTerrain get(Identifier location) {
            return map.get(location);
        }
    }

    public record BiomeTerrain(float depth, float weightModifier) { }
}
