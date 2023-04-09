package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.blocks.EmptyHoneycombBrood;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.DefaultDispenseItemBehaviorInvoker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;


public class ProductiveBeesDispenseBehavior extends DefaultDispenseItemBehavior {
    public static DispenseItemBehavior DEFAULT_BEE_CAGED_DISPENSE_BEHAVIOR;
    public static DispenseItemBehavior DEFAULT_STURDY_BEE_CAGED_DISPENSE_BEHAVIOR;
    public static DefaultDispenseItemBehavior DROP_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior();

    /**
     * Dispense the specified stack, play the dispenser sound and spawn particles.
     */
    public ItemStack execute(BlockSource source, ItemStack stack) {
        ServerLevel world = source.getLevel();
        Position iposition = DispenserBlock.getDispensePosition(source);
        BlockPos position = new BlockPos(iposition);
        BlockState blockstate = world.getBlockState(position);

        if (blockstate.getBlock() == BzBlocks.EMPTY_HONEYCOMB_BROOD.get() && ProductiveBeesCompat.isFilledBeeCageItem(stack)) {
            world.setBlockAndUpdate(position, BzBlocks.HONEYCOMB_BROOD.get().defaultBlockState()
                .setValue(HoneycombBrood.FACING, blockstate.getValue(EmptyHoneycombBrood.FACING))
                .setValue(HoneycombBrood.STAGE, 3));

            boolean isSturdy = stack.is(ProductiveBeesCompat.STURDY_BEE_CAGE);
            stack.shrink(1);

            if (!stack.isEmpty()) {
                addEmptyCageToDispenser(source, isSturdy);
            }
            else {
                if (isSturdy) {
                    stack = new ItemStack(ProductiveBeesCompat.STURDY_BEE_CAGE);
                }
                else {
                    stack = new ItemStack(ProductiveBeesCompat.BEE_CAGE);
                }
            }

            return stack;
        }
        else {
            if (stack.is(ProductiveBeesCompat.BEE_CAGE)) {
                return ((DefaultDispenseItemBehaviorInvoker) DEFAULT_BEE_CAGED_DISPENSE_BEHAVIOR).invokeExecute(source, stack);
            }
            else {
                return ((DefaultDispenseItemBehaviorInvoker) DEFAULT_STURDY_BEE_CAGED_DISPENSE_BEHAVIOR).invokeExecute(source, stack);
            }
        }
    }


    /**
     * Play the dispenser sound from the specified block.
     */
    protected void playSound(BlockSource source) {
        source.getLevel().levelEvent(1002, source.getPos(), 0);
    }

    /**
     * Adds glass bottle to dispenser or if no room, dispense it
     */
    private static void addEmptyCageToDispenser(BlockSource source, boolean isSturdy) {
        if (source.getEntity() instanceof DispenserBlockEntity) {
            DispenserBlockEntity dispenser = source.getEntity();
            ItemStack emptyCage;
            if (isSturdy) {
                emptyCage = new ItemStack(ProductiveBeesCompat.STURDY_BEE_CAGE);
            }
            else {
                emptyCage = new ItemStack(ProductiveBeesCompat.BEE_CAGE);
            }

            if (!HopperBlockEntity.addItem(null, dispenser, emptyCage, null).isEmpty()) {
                DROP_ITEM_BEHAVIOR.dispense(source, emptyCage);
            }
        }
    }
}