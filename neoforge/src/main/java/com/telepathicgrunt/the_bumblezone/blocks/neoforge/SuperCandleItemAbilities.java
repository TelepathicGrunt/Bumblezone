package com.telepathicgrunt.the_bumblezone.blocks.neoforge;

import com.telepathicgrunt.the_bumblezone.blocks.SuperCandle;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

public class SuperCandleItemAbilities {
    public static @Nullable BlockState getNewCandleBlockState(SuperCandle superCandle, BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
        ItemStack itemStack = context.getItemInHand();

        if (!itemStack.canPerformAction(itemAbility)) {
            return null;
        }

        if (context.getPlayer() != null && !context.getPlayer().mayInteract(context.getLevel(), context.getClickedPos())) {
            return null;
        }

        if (SuperCandle.canBeLit(context.getLevel(), state, context.getClickedPos())) {
            if (ItemAbilities.FIRESTARTER_LIGHT == itemAbility) {
                if (!simulate) {
                    superCandle.lightCandle(context.getLevel(), context.getClickedPos(), context.getPlayer());
                }
                return state.setValue(BlockStateProperties.LIT, Boolean.TRUE);
            }
        }

        return null;
    }
}
