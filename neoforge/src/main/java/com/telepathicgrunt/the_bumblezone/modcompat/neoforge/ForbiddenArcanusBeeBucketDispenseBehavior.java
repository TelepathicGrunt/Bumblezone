package com.telepathicgrunt.the_bumblezone.modcompat.neoforge;

import com.telepathicgrunt.the_bumblezone.blocks.EmptyHoneycombBrood;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.DefaultDispenseItemBehaviorInvoker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;


public class ForbiddenArcanusBeeBucketDispenseBehavior extends DefaultDispenseItemBehavior {
    public static DispenseItemBehavior DEFAULT_BEE_BUCKET_DISPENSE_BEHAVIOR;
    public static final DefaultDispenseItemBehavior DROP_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior();

    /**
     * Dispense the specified stack, play the dispenser sound and spawn particles.
     */
    public ItemStack execute(BlockSource source, ItemStack stack) {
        ServerLevel world = source.level();
        Position dispensePosition = DispenserBlock.getDispensePosition(source);
        BlockPos dispenseBlockPos = BlockPos.containing(dispensePosition);
        BlockState blockstate = world.getBlockState(dispenseBlockPos);

        if (blockstate.getBlock() == BzBlocks.EMPTY_HONEYCOMB_BROOD.get()) {
            world.setBlockAndUpdate(dispenseBlockPos, BzBlocks.HONEYCOMB_BROOD.get().defaultBlockState()
                    .setValue(HoneycombBrood.FACING, blockstate.getValue(EmptyHoneycombBrood.FACING))
                    .setValue(HoneycombBrood.STAGE, stack.hasTag() && stack.getOrCreateTag().getInt("Age") < 0 ? 2 : 3));

            stack.shrink(1);

            if(!BzGeneralConfigs.dispensersDropGlassBottles) {
                if (!stack.isEmpty()) {
                    addBucketToDispenser(source);
                }
                else {
                    stack = new ItemStack(BuiltInRegistries.ITEM.get(ForbiddenArcanusCompat.EMPTY_BUCKET_RL));
                }
            }
            else {
                DROP_ITEM_BEHAVIOR.dispense(source, new ItemStack(BuiltInRegistries.ITEM.get(ForbiddenArcanusCompat.EMPTY_BUCKET_RL)));
            }

            return stack;
        }
        else {
            // Use whatever the regular Dispenser behavior was
            return ((DefaultDispenseItemBehaviorInvoker) DEFAULT_BEE_BUCKET_DISPENSE_BEHAVIOR).invokeExecute(source, stack);
        }
    }



    /**
     * Play the dispenser sound from the specified block.
     */
    protected void playSound(BlockSource source) {
        source.level().levelEvent(1002, source.pos(), 0);
    }

    /**
     * Adds glass bottle to dispenser or if no room, dispense it
     */
    private static void addBucketToDispenser(BlockSource source) {
        if (source.blockEntity() instanceof DispenserBlockEntity) {
            DispenserBlockEntity dispenser = source.blockEntity();
            ItemStack honeyBottle = new ItemStack(BuiltInRegistries.ITEM.get(ForbiddenArcanusCompat.EMPTY_BUCKET_RL));
            if (!HopperBlockEntity.addItem(null, dispenser, honeyBottle, null).isEmpty()) {
                DROP_ITEM_BEHAVIOR.dispense(source, honeyBottle);
            }
        }
    }
}
