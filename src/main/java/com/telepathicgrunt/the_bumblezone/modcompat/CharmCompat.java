package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import svenhjol.charm.base.helper.VillagerHelper;
import svenhjol.charm.block.CandleBlock;
import svenhjol.charm.module.Beekeepers;
import svenhjol.charm.module.Candles;
import svenhjol.charm.village.BeekeeperTradeOffers;

import java.util.*;

public class CharmCompat {

    public static void setupCharm() {

        // fires when server starts up so long after FMLCommonSetupEvent.
        // Thus it is safe to register this event here.
        // Need lowest priority to make sure we add trades after the other mod has created their trades.
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(EventPriority.LOWEST, CharmCompat::setupCharmTrades);

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.charmPresent = true;
    }

    public static BlockState CGetCandle(boolean waterlogged, boolean lit) {
        return Candles.CANDLE.getDefaultState()
                .with(CandleBlock.LIT, lit)
                .with(CandleBlock.WATERLOGGED, waterlogged);
    }

    public static void setupCharmTrades(VillagerTradesEvent event) {
        if (event.getType() == Beekeepers.BEEKEEPER) {
            Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();

            List<VillagerTrades.ITrade> tradeList = new ArrayList<>(trades.get(2));
            tradeList.add(new GeneralUtils.BasicItemTrade(Items.EMERALD, 1, BzItems.STICKY_HONEY_RESIDUE.get(), 2));
            trades.put(2, tradeList);

            tradeList = new ArrayList<>(trades.get(3));
            tradeList.add(new GeneralUtils.BasicItemTrade(BzItems.HONEY_CRYSTAL_SHARDS.get(), 3, Items.EMERALD, 1));
            trades.put(3, tradeList);

            tradeList = new ArrayList<>(trades.get(4));
            tradeList.add(new GeneralUtils.BasicItemTrade(Items.EMERALD, 2, BzItems.HONEY_CRYSTAL.get(), 1));
            trades.put(4, tradeList);

            tradeList = new ArrayList<>(trades.get(5));
            tradeList.add(new GeneralUtils.BasicItemTrade(Items.EMERALD, 20, BzItems.HONEYCOMB_LARVA.get(), 1));
            trades.put(5, tradeList);
        }
    }
}
