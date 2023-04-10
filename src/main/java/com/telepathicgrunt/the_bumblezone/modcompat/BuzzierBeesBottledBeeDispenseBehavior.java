package com.telepathicgrunt.the_bumblezone.modcompat;

import com.telepathicgrunt.the_bumblezone.blocks.EmptyHoneycombBrood;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.DefaultDispenseItemBehaviorInvoker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;


public class BuzzierBeesBottledBeeDispenseBehavior extends DefaultDispenseItemBehavior {
    public static DispenseItemBehavior DEFAULT_BOTTLED_BEE_DISPENSE_BEHAVIOR;
    public static DefaultDispenseItemBehavior DROP_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior();

    /**
     * Dispense the specified stack, play the dispenser sound and spawn particles.
     */
    public ItemStack execute(BlockSource source, ItemStack stack) {
        ServerLevel world = source.getLevel();
        Position iposition = DispenserBlock.getDispensePosition(source);
        BlockPos position = new BlockPos(iposition);
        BlockState blockstate = world.getBlockState(position);

        if (blockstate.getBlock() == BzBlocks.EMPTY_HONEYCOMB_BROOD.get()) {
            world.setBlockAndUpdate(position, BzBlocks.HONEYCOMB_BROOD.get().defaultBlockState()
                    .setValue(HoneycombBrood.FACING, blockstate.getValue(EmptyHoneycombBrood.FACING))
                    .setValue(HoneycombBrood.STAGE, stack.hasTag() && stack.getOrCreateTag().getInt("Age") < 0 ? 2 : 3));

            stack.shrink(1);

            if(!BzGeneralConfigs.dispensersDropGlassBottles.get()) {
                if (!stack.isEmpty()) {
                    addGlassBottleToDispenser(source);
                }
                else {
                    stack = new ItemStack(Items.GLASS_BOTTLE);
                }
            }
            else {
                DROP_ITEM_BEHAVIOR.dispense(source, new ItemStack(Items.GLASS_BOTTLE));
            }

            return stack;
        }
        else {
            // Use whatever the regular Dispenser behavior was
            return ((DefaultDispenseItemBehaviorInvoker)DEFAULT_BOTTLED_BEE_DISPENSE_BEHAVIOR).invokeExecute(source, stack);
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
    private static void addGlassBottleToDispenser(BlockSource source) {
        if (source.getEntity() instanceof DispenserBlockEntity) {
            DispenserBlockEntity dispenser = source.getEntity();
            ItemStack honeyBottle = new ItemStack(Items.GLASS_BOTTLE);
            if (!HopperBlockEntity.addItem(null, dispenser, honeyBottle, null).isEmpty()) {
                DROP_ITEM_BEHAVIOR.dispense(source, honeyBottle);
            }
        }
    }
}
