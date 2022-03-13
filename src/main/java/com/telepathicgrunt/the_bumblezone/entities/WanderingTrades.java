package com.telepathicgrunt.the_bumblezone.entities;

import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;

public class WanderingTrades {

    public static void addWanderingTrades() {

        // Traders
        // Rare trades
        addRareTrade(2, new GeneralUtils.BasicItemTrade(
                Items.EMERALD,
                BzItems.MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV,
                20,
                1,
                1,
                10,
                0.5F
        ));

        addRareTrade(2, new GeneralUtils.BasicItemTrade(
                Items.EMERALD,
                BzItems.MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY,
                20,
                1,
                1,
                10,
                0.5F
        ));
    }

    public static void addRareTrade(int tier, VillagerTrades.ItemListing trade) {
        int newSize = VillagerTrades.WANDERING_TRADER_TRADES.get(tier).length + 1;
        VillagerTrades.ItemListing[] factory = new VillagerTrades.ItemListing[newSize];
        for(int index = 0; index < VillagerTrades.WANDERING_TRADER_TRADES.get(tier).length; index++) {
            factory[index] = VillagerTrades.WANDERING_TRADER_TRADES.get(tier)[index];
        }
        factory[newSize - 1] = trade;
        VillagerTrades.WANDERING_TRADER_TRADES.put(tier, factory);
    }
}
