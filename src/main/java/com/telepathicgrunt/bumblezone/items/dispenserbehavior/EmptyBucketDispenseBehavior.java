package com.telepathicgrunt.bumblezone.items.dispenserbehavior;

import com.telepathicgrunt.bumblezone.mixin.blocks.ItemDispenserBehaviorInvoker;
import com.telepathicgrunt.bumblezone.modinit.BzFluids;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;


public class EmptyBucketDispenseBehavior extends ItemDispenserBehavior {
    public static DispenserBehavior DEFAULT_EMPTY_BUCKET_DISPENSE_BEHAVIOR;
    public static ItemDispenserBehavior DROP_ITEM_BEHAVIOR = new ItemDispenserBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    @Override
    public ItemStack dispenseSilently(BlockPointer source, ItemStack stack) {
        World world = source.getWorld();
        Position iposition = DispenserBlock.getOutputLocation(source);
        BlockPos position = new BlockPos(iposition);
        BlockState blockstate = world.getBlockState(position);

        if (blockstate.getBlock() == BzFluids.HONEY_FLUID_BLOCK && blockstate.getFluidState().isStill()) {
            world.setBlockState(position, Blocks.AIR.getDefaultState());
            stack.decrement(1);
            if (!stack.isEmpty())
                addItemToDispenser(source, BzItems.HONEY_BUCKET);
            else
                stack = new ItemStack(BzItems.HONEY_BUCKET);
        }
        else {
            // If it instanceof DefaultDispenseItemBehavior, call dispenseStack directly to avoid
            // playing particles and sound twice due to dispense method having that by default.
            if(DEFAULT_EMPTY_BUCKET_DISPENSE_BEHAVIOR instanceof ItemDispenserBehavior) {
                return ((ItemDispenserBehaviorInvoker)DEFAULT_EMPTY_BUCKET_DISPENSE_BEHAVIOR).thebumblezone_invokeDispenseSilently(source, stack);
            }
            else {
                // Fallback to dispense as someone chose to make a custom class without dispenseStack.
                return DEFAULT_EMPTY_BUCKET_DISPENSE_BEHAVIOR.dispense(source, stack);
            }
        }

        return stack;
    }


    /**
     * Play the dispense sound from the specified block.
     */
    @Override
    protected void playSound(BlockPointer source) {
        source.getWorld().syncWorldEvent(1002, source.getPos(), 0);
    }


    /**
     * Adds honey bottle to dispenser or if no room, dispense it
     */
    private static void addItemToDispenser(BlockPointer source, Item newItem) {
        if (source.getBlockEntity() instanceof DispenserBlockEntity) {
			DispenserBlockEntity dispenser = source.getBlockEntity();
            ItemStack honeyBottle = new ItemStack(newItem);
            if (!HopperBlockEntity.transfer(null, dispenser, honeyBottle, null).isEmpty()) {
                DROP_ITEM_BEHAVIOR.dispense(source, honeyBottle);
            }
        }
    }
}
