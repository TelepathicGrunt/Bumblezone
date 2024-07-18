package com.telepathicgrunt.the_bumblezone.mixin.neoforge.block;

import com.telepathicgrunt.the_bumblezone.blocks.PotionCandleBase;
import com.telepathicgrunt.the_bumblezone.blocks.SuperCandle;
import com.telepathicgrunt.the_bumblezone.blocks.SuperCandleBase;
import com.telepathicgrunt.the_bumblezone.blocks.neoforge.SuperCandleItemAbilities;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbility;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({PotionCandleBase.class, SuperCandleBase.class})
public class SuperCandleMixin implements SuperCandle  {

    // No unique or prefix so this applies the neo method
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
        return SuperCandleItemAbilities.getNewCandleBlockState(this, state, context, itemAbility, simulate);
    }
}
