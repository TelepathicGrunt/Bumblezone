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
    public static final RegistryEntry<ResourceLocation> INTERACT_WITH_BUZZING_BRIEFCASE_RL = CUSTOM_STAT.register("interact_with_buzzing_briefcase", () -> makeRL("interact_with_buzzing_briefcase"));
    public static final RegistryEntry<ResourceLocation> BUZZING_BRIEFCASE_BEE_CAPTURE_RL = CUSTOM_STAT.register("buzzing_briefcase_bee_capture", () -> makeRL("buzzing_briefcase_bee_capture"));
    public static final RegistryEntry<ResourceLocation> WINDY_AIR_TIME_RL = CUSTOM_STAT.register("windy_air_time", () -> makeRL("windy_air_time"));
    public static final RegistryEntry<ResourceLocation> RAGING_EVENT_DEFEATED_RL = CUSTOM_STAT.register("raging_event_defeated", () -> makeRL("raging_event_defeated"));
    public static final RegistryEntry<ResourceLocation> RADIANCE_EVENT_DEFEATED_RL = CUSTOM_STAT.register("radiance_event_defeated", () -> makeRL("radiance_event_defeated"));
    public static final RegistryEntry<ResourceLocation> LIFE_EVENT_DEFEATED_RL = CUSTOM_STAT.register("life_event_defeated", () -> makeRL("life_event_defeated"));
    public static final RegistryEntry<ResourceLocation> CALMING_EVENT_DEFEATED_RL = CUSTOM_STAT.register("calming_event_defeated", () -> makeRL("calming_event_defeated"));
    public static final RegistryEntry<ResourceLocation> KNOWING_EVENT_DEFEATED_RL = CUSTOM_STAT.register("knowing_event_defeated", () -> makeRL("knowing_event_defeated"));
    public static final RegistryEntry<ResourceLocation> CONTINUITY_EVENT_DEFEATED_RL = CUSTOM_STAT.register("continuity_event_defeated", () -> makeRL("continuity_event_defeated"));

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
        Stats.CUSTOM.get(INTERACT_WITH_BUZZING_BRIEFCASE_RL.get(), StatFormatter.DEFAULT);
        Stats.CUSTOM.get(BUZZING_BRIEFCASE_BEE_CAPTURE_RL.get(), StatFormatter.DEFAULT);
        Stats.CUSTOM.get(WINDY_AIR_TIME_RL.get(), StatFormatter.TIME);
        Stats.CUSTOM.get(RAGING_EVENT_DEFEATED_RL.get(), StatFormatter.DEFAULT);
        Stats.CUSTOM.get(RADIANCE_EVENT_DEFEATED_RL.get(), StatFormatter.DEFAULT);
        Stats.CUSTOM.get(LIFE_EVENT_DEFEATED_RL.get(), StatFormatter.DEFAULT);
        Stats.CUSTOM.get(CALMING_EVENT_DEFEATED_RL.get(), StatFormatter.DEFAULT);
        Stats.CUSTOM.get(KNOWING_EVENT_DEFEATED_RL.get(), StatFormatter.DEFAULT);
        Stats.CUSTOM.get(CONTINUITY_EVENT_DEFEATED_RL.get(), StatFormatter.DEFAULT);
    }
}
