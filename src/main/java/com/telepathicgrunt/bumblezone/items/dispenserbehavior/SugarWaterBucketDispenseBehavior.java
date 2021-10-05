package com.telepathicgrunt.bumblezone.items.dispenserbehavior;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;


public class SugarWaterBucketDispenseBehavior extends ItemDispenserBehavior {
    private static final ItemDispenserBehavior DROP_ITEM_BEHAVIOR = new ItemDispenserBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack dispenseSilently(BlockPointer source, ItemStack stack) {
        BucketItem bucketitem = (BucketItem) stack.getItem();
        Position iposition = DispenserBlock.getOutputLocation(source);
        BlockPos position = new BlockPos(iposition);
        ServerWorld world = source.getWorld();
        BlockState blockstate = world.getBlockState(position);

        if (bucketitem.placeFluid(null, world, position, null)) {

            bucketitem.onEmptied(null, world, stack, position);
            return new ItemStack(Items.BUCKET);
        }
        else {
            return DROP_ITEM_BEHAVIOR.dispense(source, stack);
        }
    }
}
