package com.telepathicgrunt.the_bumblezone.items.dispenserbehavior;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class SugarWaterBucketDispenseBehavior extends DefaultDispenseItemBehavior {
    private final DefaultDispenseItemBehavior dispenserBehavior = new DefaultDispenseItemBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack execute(IBlockSource source, ItemStack stack) {
        BucketItem bucketitem = (BucketItem) stack.getItem();
        BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
        World world = source.getLevel();
        BlockState blockstate = world.getBlockState(blockpos);

        if (bucketitem.emptyBucket(null, world, blockpos, null)) {

            bucketitem.checkExtraContent(world, stack, blockpos);
            return new ItemStack(Items.BUCKET);
        }
        else if(blockstate.getBlock() == BzBlocks.HONEY_CRYSTAL.get() && !blockstate.getValue(BlockStateProperties.WATERLOGGED)) {

            world.setBlockAndUpdate(blockpos, BzBlocks.HONEY_CRYSTAL.get().defaultBlockState()
                    .setValue(BlockStateProperties.FACING, blockstate.getValue(BlockStateProperties.FACING))
                    .setValue(BlockStateProperties.WATERLOGGED, true));
            return new ItemStack(Items.BUCKET);
        }
        else {
            return this.dispenserBehavior.dispense(source, stack);
        }
    }
}
