package com.telepathicgrunt.the_bumblezone.items.dispenserbehavior;

import com.telepathicgrunt.the_bumblezone.blocks.SuperCandle;
import com.telepathicgrunt.the_bumblezone.blocks.SuperCandleWick;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.DefaultDispenseItemBehaviorInvoker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;


public class FlintAndSteelDispenseBehavior extends OptionalDispenseItemBehavior {
    public static DispenseItemBehavior DEFAULT_DISPENSE_BEHAVIOR;

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    @Override
    public ItemStack execute(BlockSource source, ItemStack stack) {
        ServerLevel level = source.getLevel();
        Position iposition = DispenserBlock.getDispensePosition(source);
        BlockPos position = new BlockPos(iposition);
        BlockState blockstate = level.getBlockState(position);
        if (blockstate.is(BzBlocks.SUPER_CANDLE_WICK.get())) {
            position = position.below();
        }

        if (SuperCandle.canBeLit(level, blockstate, position)) {
            boolean successfulLit = SuperCandleWick.setLit(level, level.getBlockState(position.above()), position.above(), true);
            this.setSuccess(successfulLit);
        }
        else {
            // If it instanceof DefaultDispenseItemBehavior, call dispenseStack directly to avoid
            // playing particles and sound twice due to dispense method having that by default.
            if(DEFAULT_DISPENSE_BEHAVIOR instanceof DefaultDispenseItemBehavior) {
                return ((DefaultDispenseItemBehaviorInvoker) DEFAULT_DISPENSE_BEHAVIOR).thebumblezone_invokeDispenseSilently(source, stack);
            }
            else {
                // Fallback to dispense as someone chose to make a custom class without dispenseStack.
                return DEFAULT_DISPENSE_BEHAVIOR.dispense(source, stack);
            }
        }

        return stack;
    }
}
