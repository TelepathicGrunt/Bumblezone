package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class BzStats {

    public static ResourceLocation CARPENTER_BEE_BOOTS_MINED_BLOCKS_RL;
    public static ResourceLocation CARPENTER_BEE_BOOTS_WALL_HANG_TIME_RL;
    public static ResourceLocation STINGLESS_BEE_HELMET_BEE_RIDER_RL;
    public static ResourceLocation BUMBLE_BEE_CHESTPLATE_FLY_TIME_RL;
    public static ResourceLocation HONEY_BEE_LEGGINGS_FLOWER_POLLEN_RL;
    public static ResourceLocation INTERACT_WITH_CRYSTALLINE_FLOWER_RL;

    private static ResourceLocation registerCustomStat(String key) {
        ResourceLocation resourcelocation = new ResourceLocation(Bumblezone.MODID, key);
        Registry.register(Registry.CUSTOM_STAT, key, resourcelocation);
        Stats.CUSTOM.get(resourcelocation, StatFormatter.DEFAULT);
        return resourcelocation;
    }

    public static void registerStats() {
        CARPENTER_BEE_BOOTS_MINED_BLOCKS_RL = registerCustomStat("carpenter_bee_boots_mined_blocks");
        CARPENTER_BEE_BOOTS_WALL_HANG_TIME_RL = registerCustomStat("carpenter_bee_boots_wall_hang_time");
        STINGLESS_BEE_HELMET_BEE_RIDER_RL = registerCustomStat("stingless_bee_helmet_bee_rider");
        BUMBLE_BEE_CHESTPLATE_FLY_TIME_RL = registerCustomStat("bumble_bee_chestplate_fly_time");
        HONEY_BEE_LEGGINGS_FLOWER_POLLEN_RL = registerCustomStat("honey_bee_leggings_flower_pollen");
        HONEY_BEE_LEGGINGS_FLOWER_POLLEN_RL = registerCustomStat("interact_with_crystalline_flower");
    }
}
