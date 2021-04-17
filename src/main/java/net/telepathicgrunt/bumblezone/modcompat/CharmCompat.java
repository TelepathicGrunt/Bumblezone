package net.telepathicgrunt.bumblezone.modcompat;

import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffer;
import net.telepathicgrunt.bumblezone.modinit.BzItems;
import svenhjol.charm.Charm;
import svenhjol.charm.base.helper.VillagerHelper;
import svenhjol.charm.block.CandleBlock;
import svenhjol.charm.module.Beekeepers;
import svenhjol.charm.module.Candles;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class CharmCompat {

    public static void setupCharm() {
        ServerStartCallback.EVENT.register((server)-> setupCharmTrades());

       // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.charmPresent = true;
    }

    public static void setupCharmTrades(){
        VillagerHelper.addTrade(Beekeepers.BEEKEEPER, 2, new BasicItemTrade(Items.EMERALD, 1, BzItems.STICKY_HONEY_RESIDUE, 2));
        VillagerHelper.addTrade(Beekeepers.BEEKEEPER, 3, new BasicItemTrade(BzItems.HONEY_CRYSTAL_SHARDS, 3, Items.EMERALD, 1));
        VillagerHelper.addTrade(Beekeepers.BEEKEEPER, 4, new BasicItemTrade(Items.EMERALD, 2, BzItems.HONEY_CRYSTAL, 1));
        VillagerHelper.addTrade(Beekeepers.BEEKEEPER, 5, new BasicItemTrade(Items.EMERALD, 20, BzItems.HONEYCOMB_LARVA, 1));
    }

    public static class BasicItemTrade extends VillagerHelper.SingleItemTypeTrade {
        private final Item itemToTrade;
        private final Item itemToReceive;
        private final int amountToGive;
        private final int amountToReceive;

        BasicItemTrade(Item itemToTrade, int amountToGive, Item itemToReceive, int amountToReceive){
            this.itemToTrade = itemToTrade;
            this.itemToReceive = itemToReceive;
            this.amountToGive = amountToGive;
            this.amountToReceive = amountToReceive;
        }

        @Override
        public TradeOffer create(Entity entity, Random random) {
            setInput(itemToTrade, amountToGive);
            setOutput(itemToReceive, amountToReceive);
            return super.create(entity, random);
        }
    }

    public static BlockState CGetCandle(boolean waterlogged, boolean lit) {
        return Candles.CANDLE.getDefaultState()
                .with(CandleBlock.LIT, lit)
                .with(CandleBlock.WATERLOGGED, waterlogged);
    }
}
