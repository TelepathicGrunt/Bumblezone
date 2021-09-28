package com.telepathicgrunt.bumblezone.items.dispenserbehavior;

import com.telepathicgrunt.bumblezone.mixin.blocks.ItemDispenserBehaviorInvoker;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;


public class SugarWaterBucketDispenseBehavior extends ItemDispenserBehavior {
    private final ItemDispenserBehavior dispenserBehavior = new ItemDispenserBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack dispenseSilently(BlockPointer source, ItemStack stack) {
        BucketItem bucketitem = (BucketItem) stack.getItem();
        BlockPos blockpos = source.getPos().offset(source.getBlockState().get(DispenserBlock.FACING));
        World world = source.getWorld();
        BlockState blockstate = world.getBlockState(blockpos);

        if (bucketitem.placeFluid(null, world, blockpos, null)) {

            bucketitem.onEmptied(null, world, stack, blockpos);
            return new ItemStack(Items.BUCKET);
        }
        else if(blockstate.getBlock() == BzBlocks.HONEY_CRYSTAL && !blockstate.get(Properties.WATERLOGGED)) {

            world.setBlockState(blockpos, BzBlocks.HONEY_CRYSTAL.getDefaultState()
                    .with(Properties.FACING, blockstate.get(Properties.FACING))
                    .with(Properties.WATERLOGGED, true));
            return new ItemStack(Items.BUCKET);
        }
        else {
            return this.dispenserBehavior.dispense(source, stack);
        }
    }
}
