package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class BzStats {
    public static final ResourcefulRegistry<ResourceLocation> CUSTOM_STAT = ResourcefulRegistries.create(BuiltInRegistries.CUSTOM_STAT, Bumblezone.MODID);

    public static final RegistryEntry<ResourceLocation> CARPENTER_BEE_BOOTS_MINED_BLOCKS_RL = CUSTOM_STAT.register("carpenter_bee_boots_mined_blocks", () -> makeRL("carpenter_bee_boots_mined_blocks"));
    public static final RegistryEntry<ResourceLocation> CARPENTER_BEE_BOOTS_WALL_HANG_TIME_RL = CUSTOM_STAT.register("carpenter_bee_boots_wall_hang_time", () -> makeRL("carpenter_bee_boots_wall_hang_time"));
    public static final RegistryEntry<ResourceLocation> STINGLESS_BEE_HELMET_BEE_RIDER_RL = CUSTOM_STAT.register("stingless_bee_helmet_bee_rider", () -> makeRL("stingless_bee_helmet_bee_rider"));
    public static final RegistryEntry<ResourceLocation> BUMBLE_BEE_CHESTPLATE_FLY_TIME_RL = CUSTOM_STAT.register("bumble_bee_chestplate_fly_time", () -> makeRL("bumble_bee_chestplate_fly_time"));
    public static final RegistryEntry<ResourceLocation> HONEY_BEE_LEGGINGS_FLOWER_POLLEN_RL = CUSTOM_STAT.register("honey_bee_leggings_flower_pollen", () -> makeRL("honey_bee_leggings_flower_pollen"));
    public static final RegistryEntry<ResourceLocation> INTERACT_WITH_CRYSTALLINE_FLOWER_RL = CUSTOM_STAT.register("interact_with_crystalline_flower", () -> makeRL("interact_with_crystalline_flower"));

    private static ResourceLocation makeRL(String key) {
        return new ResourceLocation(Bumblezone.MODID, key);
    }

    public static void initStatEntries() {
        Stats.CUSTOM.get(CARPENTER_BEE_BOOTS_MINED_BLOCKS_RL.get(), StatFormatter.DEFAULT);
        Stats.CUSTOM.get(CARPENTER_BEE_BOOTS_WALL_HANG_TIME_RL.get(), StatFormatter.DEFAULT);
        Stats.CUSTOM.get(STINGLESS_BEE_HELMET_BEE_RIDER_RL.get(), StatFormatter.DEFAULT);
        Stats.CUSTOM.get(BUMBLE_BEE_CHESTPLATE_FLY_TIME_RL.get(), StatFormatter.DEFAULT);
        Stats.CUSTOM.get(HONEY_BEE_LEGGINGS_FLOWER_POLLEN_RL.get(), StatFormatter.DEFAULT);
        Stats.CUSTOM.get(INTERACT_WITH_CRYSTALLINE_FLOWER_RL.get(), StatFormatter.DEFAULT);
    }
}
