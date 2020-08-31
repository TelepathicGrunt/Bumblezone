package com.telepathicgrunt.the_bumblezone.items;

import net.minecraft.block.BlockState;
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
import com.telepathicgrunt.the_bumblezone.blocks.BzBlocks;


public class EmptyBucketDispenseBehavior extends DefaultDispenseItemBehavior {
    public static IDispenseItemBehavior DEFAULT_EMPTY_BUCKET_DISPENSE_BEHAVIOR;
    public static DefaultDispenseItemBehavior DROP_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    @Override
    public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        World world = source.getWorld();
        IPosition iposition = DispenserBlock.getDispensePosition(source);
        BlockPos position = new BlockPos(iposition);
        BlockState blockstate = world.getBlockState(position);

        if (blockstate.getBlock() == BzBlocks.HONEY_CRYSTAL && blockstate.get(BlockStateProperties.WATERLOGGED)) {

            world.setBlockState(position, BzBlocks.HONEY_CRYSTAL.getDefaultState()
                    .with(BlockStateProperties.FACING, blockstate.get(BlockStateProperties.FACING))
                    .with(BlockStateProperties.WATERLOGGED, false));

            stack.shrink(1);

            if (!stack.isEmpty())
                addItemToDispenser(source, BzItems.SUGAR_WATER_BUCKET);
            else
                stack = new ItemStack(BzItems.SUGAR_WATER_BUCKET);
        } else {
            return DEFAULT_EMPTY_BUCKET_DISPENSE_BEHAVIOR.dispense(source, stack);
        }

        return stack;
    }


    /**
     * Play the dispense sound from the specified block.
     */
    @Override
    protected void playDispenseSound(IBlockSource source) {
        source.getWorld().playEvent(1002, source.getBlockPos(), 0);
    }


    /**
     * Adds honey bottle to dispenser or if no room, dispense it
     */
    private static void addItemToDispenser(IBlockSource source, Item newItem) {
        if (source.getBlockTileEntity() instanceof DispenserTileEntity) {
			DispenserTileEntity dispenser = source.getBlockTileEntity();
            ItemStack honeyBottle = new ItemStack(newItem);
            if (!HopperTileEntity.putStackInInventoryAllSlots(null, dispenser, honeyBottle, null).isEmpty()) {
                DROP_ITEM_BEHAVIOR.dispense(source, honeyBottle);
            }
        }
    }
}
