package net.telepathicgrunt.bumblezone.modcompat;

import net.minecraft.block.BlockState;
import svenhjol.charm.block.CandleBlock;
import svenhjol.charm.module.Candles;

public class CharmCompat {

    public static void setupCharm() {
        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.charmPresent = true;
    }

    public static BlockState CGetCandle(boolean waterlogged, boolean lit) {
        return Candles.CANDLE.getDefaultState()
                .with(CandleBlock.LIT, lit)
                .with(CandleBlock.WATERLOGGED, waterlogged);
    }
}
