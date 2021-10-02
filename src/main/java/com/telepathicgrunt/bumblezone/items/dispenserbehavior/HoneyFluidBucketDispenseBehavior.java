package com.telepathicgrunt.bumblezone.items.dispenserbehavior;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class HoneyFluidBucketDispenseBehavior extends ItemDispenserBehavior {
    private final ItemDispenserBehavior dispenserBehavior = new ItemDispenserBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack dispenseSilently(BlockPointer source, ItemStack stack) {
        BucketItem bucketitem = (BucketItem) stack.getItem();
        BlockPos blockpos = source.getPos().offset(source.getBlockState().get(DispenserBlock.FACING));
        World world = source.getWorld();

        if (bucketitem.placeFluid(null, world, blockpos, null)) {

            bucketitem.onEmptied(null, world, stack, blockpos);
            return new ItemStack(Items.BUCKET);
        }
        else {
            return this.dispenserBehavior.dispense(source, stack);
        }
    }
}
