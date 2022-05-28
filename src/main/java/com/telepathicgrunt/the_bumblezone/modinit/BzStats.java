package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class BzStats {

    public static ResourceLocation CARPENTER_BEES_BOOTS_MINED_BLOCKS_RL;
    public static ResourceLocation CARPENTER_BEES_BOOTS_WALL_HANG_TIME_RL;

    private static ResourceLocation registerCustomStat(String key) {
        ResourceLocation resourcelocation = new ResourceLocation(Bumblezone.MODID, key);
        Registry.register(Registry.CUSTOM_STAT, key, resourcelocation);
        Stats.CUSTOM.get(resourcelocation, StatFormatter.DEFAULT);
        return resourcelocation;
    }

    public static void registerStats() {
        CARPENTER_BEES_BOOTS_MINED_BLOCKS_RL = registerCustomStat("carpenter_bees_boots_mined_blocks");
        CARPENTER_BEES_BOOTS_WALL_HANG_TIME_RL = registerCustomStat("carpenter_bees_boots_wall_hang_time");
    }
}
