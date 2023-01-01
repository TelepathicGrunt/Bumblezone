package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.configs.BzConfig;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;
import org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents;

import java.util.Optional;

public class FriendsAndFoesCompat {

    public static void setupCompat() {
        if(BzConfig.allowFriendsAndFoesBeekeeperTradesCompat) {
            ServerLifecycleEvents.STARTING.register((server) -> setupFriendsAndFoesTrades());
        }

       // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.friendsAndFoesPresent = true;
    }

    public static void setupFriendsAndFoesTrades() {
        Optional<VillagerProfession> beekeeper = Registry.VILLAGER_PROFESSION.getOptional(new ResourceLocation("friendsandfoes", "beekeeper"));

        if(beekeeper.isPresent() && VillagerTrades.TRADES.containsKey(beekeeper.get())) {
            TradeOfferHelper.registerVillagerOffers(beekeeper.get(), 2, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.STICKY_HONEY_RESIDUE, 1, 2, 10, 8, 0.05F)));

            TradeOfferHelper.registerVillagerOffers(beekeeper.get(), 2, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(BzItems.POLLEN_PUFF, Items.EMERALD, 2, 1, 12, 7, 0.05F)));

            TradeOfferHelper.registerVillagerOffers(beekeeper.get(), 2, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.POLLEN_PUFF, 1, 1, 14, 5, 0.05F)));

            TradeOfferHelper.registerVillagerOffers(beekeeper.get(), 3, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(BzItems.HONEY_CRYSTAL_SHARDS, Items.EMERALD, 3, 1, 25, 12, 0.1F)));

            TradeOfferHelper.registerVillagerOffers(beekeeper.get(), 3, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(BzItems.BEE_BREAD, Items.EMERALD, 3, 6, 12, 17, 0.1F)));

            TradeOfferHelper.registerVillagerOffers(beekeeper.get(), 4, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.HONEY_CRYSTAL, 2, 1, 10, 22, 0.2F)));

            TradeOfferHelper.registerVillagerOffers(beekeeper.get(), 4, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.BEE_BREAD, 10, 3, 10, 22, 0.2F)));

            TradeOfferHelper.registerVillagerOffers(beekeeper.get(), 5, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(BzItems.HONEY_BUCKET, Items.EMERALD, 1, 6, 3, 25, 0.2F)));

            TradeOfferHelper.registerVillagerOffers(beekeeper.get(), 5, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.HONEYCOMB_BROOD, 25, 1, 2, 30, 0.2F)));

            TradeOfferHelper.registerVillagerOffers(beekeeper.get(), 5, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.HONEY_COCOON, 15, 1, 3, 30, 0.2F)));

            TradeOfferHelper.registerVillagerOffers(beekeeper.get(), 5, (itemListings) -> itemListings.add(
                    new GeneralUtils.BasicItemTrade(Items.EMERALD, BzItems.HONEY_BUCKET, 20, 1, 2, 30, 0.2F)));
        }
    }
}
