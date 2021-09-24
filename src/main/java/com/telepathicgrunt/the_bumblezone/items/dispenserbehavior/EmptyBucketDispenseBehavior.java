package com.telepathicgrunt.the_bumblezone.items.dispenserbehavior;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class EmptyBucketDispenseBehavior extends DefaultDispenseItemBehavior {
    public static IDispenseItemBehavior DEFAULT_EMPTY_BUCKET_DISPENSE_BEHAVIOR;
    public static DefaultDispenseItemBehavior DROP_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    @Override
    public ItemStack execute(IBlockSource source, ItemStack stack) {
        World world = source.getLevel();
        IPosition iposition = DispenserBlock.getDispensePosition(source);
        BlockPos position = new BlockPos(iposition);
        BlockState blockstate = world.getBlockState(position);

        if (blockstate.getBlock() == BzBlocks.HONEY_CRYSTAL.get() && blockstate.getValue(BlockStateProperties.WATERLOGGED)) {
            world.setBlockAndUpdate(position, BzBlocks.HONEY_CRYSTAL.get().defaultBlockState()
                    .setValue(BlockStateProperties.FACING, blockstate.getValue(BlockStateProperties.FACING))
                    .setValue(BlockStateProperties.WATERLOGGED, false));

            stack.shrink(1);

            if (!stack.isEmpty())
                addItemToDispenser(source, BzItems.SUGAR_WATER_BUCKET.get());
            else
                stack = new ItemStack(BzItems.SUGAR_WATER_BUCKET.get());
        }
        else if (blockstate.getBlock() == BzFluids.HONEY_FLUID_BLOCK.get() && blockstate.getFluidState().isSource()) {
            world.setBlockAndUpdate(position, Blocks.AIR.defaultBlockState());
            stack.shrink(1);
            if (!stack.isEmpty())
                addItemToDispenser(source, BzItems.HONEY_BUCKET.get());
            else
                stack = new ItemStack(BzItems.HONEY_BUCKET.get());
        }
        else {
            return GeneralUtils.dispenseStackProperly(source, stack, DEFAULT_EMPTY_BUCKET_DISPENSE_BEHAVIOR);
        }

        return stack;
    }


    /**
     * Play the dispense sound from the specified block.
     */
    @Override
    protected void playSound(IBlockSource source) {
        source.getLevel().levelEvent(1002, source.getPos(), 0);
    }


    /**
     * Adds honey bottle to dispenser or if no room, dispense it
     */
    private static void addItemToDispenser(IBlockSource source, Item newItem) {
        if (source.getEntity() instanceof DispenserTileEntity) {
			DispenserTileEntity dispenser = source.getEntity();
            ItemStack honeyBottle = new ItemStack(newItem);
            if (!HopperTileEntity.addItem(null, dispenser, honeyBottle, null).isEmpty()) {
                DROP_ITEM_BEHAVIOR.dispense(source, honeyBottle);
            }
        }
    }
}
