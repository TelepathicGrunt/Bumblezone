package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class BzBiomeHeightRegistry {
    private BzBiomeHeightRegistry() {}

    public static final ResourceKey<Registry<BiomeTerrain>> BIOME_HEIGHT_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Bumblezone.MODID, "biome_height"));
    public static final DeferredRegister<BiomeTerrain> BIOME_HEIGHT = DeferredRegister.create(BIOME_HEIGHT_KEY, Bumblezone.MODID);
    public static Supplier<IForgeRegistry<BiomeTerrain>> BIOME_HEIGHT_REGISTRY;

    public static final RegistryObject<BiomeTerrain> HIVE_PILLAR = BIOME_HEIGHT.register("hive_pillar", () -> new BiomeTerrain(22f, 0.35f));
    public static final RegistryObject<BiomeTerrain> HIVE_WALL = BIOME_HEIGHT.register("hive_wall", () -> new BiomeTerrain(19f, 0.25f));
    public static final RegistryObject<BiomeTerrain> POLLINATED_FIELDS = BIOME_HEIGHT.register("pollinated_fields", () -> new BiomeTerrain(5.4f, 0.9f));
    public static final RegistryObject<BiomeTerrain> POLLINATED_PILLAR = BIOME_HEIGHT.register("pollinated_pillar", () -> new BiomeTerrain(22.5f, 0.05f));
    public static final RegistryObject<BiomeTerrain> SUGAR_WATER_FLOOR = BIOME_HEIGHT.register("sugar_water_floor", () -> new BiomeTerrain(-3.7f, 0.75f));
    public static final RegistryObject<BiomeTerrain> CRYSTAL_CANYON = BIOME_HEIGHT.register("crystal_canyon", () -> new BiomeTerrain(0f, 0.75f));

    public static void createNewRegistry(NewRegistryEvent event) {
        BIOME_HEIGHT_REGISTRY = event.create(new RegistryBuilder<BiomeTerrain>()
                .setName(BIOME_HEIGHT_KEY.location())
                .setIDRange(1, Integer.MAX_VALUE - 1)
                .disableSaving()
                .disableSync()
                .allowModification());
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
