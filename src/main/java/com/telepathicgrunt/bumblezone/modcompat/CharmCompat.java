package com.telepathicgrunt.bumblezone.modcompat;

import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class CharmCompat {

    public static void setupCharm() {
        ServerStartCallback.EVENT.register((server)-> setupCharmTrades());

       // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.charmPresent = true;
    }

    public static void setupCharmTrades(){
    //    VillagerHelper.addTrade(Beekeepers.BEEKEEPER, 2, new GeneralUtils.BasicItemTrade(Items.EMERALD, 1, BzItems.STICKY_HONEY_RESIDUE, 2));
    //    VillagerHelper.addTrade(Beekeepers.BEEKEEPER, 3, new GeneralUtils.BasicItemTrade(BzItems.HONEY_CRYSTAL_SHARDS, 3, Items.EMERALD, 1));
    //    VillagerHelper.addTrade(Beekeepers.BEEKEEPER, 4, new GeneralUtils.BasicItemTrade(Items.EMERALD, 2, BzItems.HONEY_CRYSTAL, 1));
    //    VillagerHelper.addTrade(Beekeepers.BEEKEEPER, 5, new GeneralUtils.BasicItemTrade(Items.EMERALD, 20, BzItems.HONEYCOMB_LARVA, 1));
    }

    public static BlockState CGetCandle(boolean waterlogged, boolean lit) {
    //    return Candles.CANDLE.getDefaultState()
    //            .with(CandleBlock.LIT, lit)
    //            .with(CandleBlock.WATERLOGGED, waterlogged);
        return Blocks.CAVE_AIR.getDefaultState();
    }
}
