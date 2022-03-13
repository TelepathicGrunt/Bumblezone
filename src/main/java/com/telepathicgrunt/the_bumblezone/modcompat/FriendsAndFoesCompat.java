package com.telepathicgrunt.the_bumblezone.modcompat;

import com.faboslav.friendsandfoes.entity.passive.ai.brain.task.BeekeeperWorkTask;
import com.faboslav.friendsandfoes.registry.VillagerProfessionRegistry;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;

public class FriendsAndFoesCompat {

    public static void setupCompat() {
        if(Bumblezone.BZ_CONFIG.BZModCompatibilityConfig.allowFriendsAndFoesBeekeeperTradesCompat) {
            ServerLifecycleEvents.SERVER_STARTED.register((server) -> setupFriendsAndFoesTrades());
        }

       // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.friendsAndFoesPresent = true;
    }

    public static void setupFriendsAndFoesTrades() {
        if(VillagerTrades.TRADES.containsKey(VillagerProfessionRegistry.BEEKEEPER)) {
            TradeOfferHelper.registerVillagerOffers(VillagerProfessionRegistry.BEEKEEPER, 2, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.STICKY_HONEY_RESIDUE, 1, 2, 10, 8, 0.05F)));

            TradeOfferHelper.registerVillagerOffers(VillagerProfessionRegistry.BEEKEEPER, 2, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(BzItems.POLLEN_PUFF, Items.EMERALD, 2, 1, 12, 7, 0.05F)));

            TradeOfferHelper.registerVillagerOffers(VillagerProfessionRegistry.BEEKEEPER, 2, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.POLLEN_PUFF, 1, 1, 14, 5, 0.05F)));

            TradeOfferHelper.registerVillagerOffers(VillagerProfessionRegistry.BEEKEEPER, 3, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(BzItems.HONEY_CRYSTAL_SHARDS, Items.EMERALD, 3, 1, 25, 12, 0.1F)));

            TradeOfferHelper.registerVillagerOffers(VillagerProfessionRegistry.BEEKEEPER, 3, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(BzItems.BEE_BREAD, Items.EMERALD, 3, 6, 12, 17, 0.1F)));

            TradeOfferHelper.registerVillagerOffers(VillagerProfessionRegistry.BEEKEEPER, 4, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.HONEY_CRYSTAL, 2, 1, 10, 22, 0.2F)));

            TradeOfferHelper.registerVillagerOffers(VillagerProfessionRegistry.BEEKEEPER, 4, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.BEE_BREAD, 10, 3, 10, 22, 0.2F)));

            TradeOfferHelper.registerVillagerOffers(VillagerProfessionRegistry.BEEKEEPER, 5, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(BzItems.HONEY_BUCKET, Items.EMERALD, 1, 6, 3, 25, 0.2F)));

            TradeOfferHelper.registerVillagerOffers(VillagerProfessionRegistry.BEEKEEPER, 5, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.HONEYCOMB_BROOD, 25, 1, 2, 30, 0.2F)));

            TradeOfferHelper.registerVillagerOffers(VillagerProfessionRegistry.BEEKEEPER, 5, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.HONEY_COCOON, 15, 1, 3, 30, 0.2F)));

            TradeOfferHelper.registerVillagerOffers(VillagerProfessionRegistry.BEEKEEPER, 5, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.HONEY_BUCKET, 20, 1, 2, 30, 0.2F)));
        }
    }
}
