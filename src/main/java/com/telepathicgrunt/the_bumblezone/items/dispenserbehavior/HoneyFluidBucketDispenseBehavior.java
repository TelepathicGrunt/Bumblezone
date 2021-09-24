package com.telepathicgrunt.the_bumblezone.items.dispenserbehavior;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class HoneyFluidBucketDispenseBehavior extends DefaultDispenseItemBehavior {
    private final DefaultDispenseItemBehavior dispenserBehavior = new DefaultDispenseItemBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack execute(IBlockSource source, ItemStack stack) {
        BucketItem bucketitem = (BucketItem) stack.getItem();
        BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
        World world = source.getLevel();

        if (bucketitem.emptyBucket(null, world, blockpos, null)) {

            bucketitem.checkExtraContent(world, stack, blockpos);
            return new ItemStack(Items.BUCKET);
        }
        else {
            return this.dispenserBehavior.dispense(source, stack);
        }
    }
}
