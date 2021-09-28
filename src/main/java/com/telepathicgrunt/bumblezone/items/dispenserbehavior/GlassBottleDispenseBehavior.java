package com.telepathicgrunt.bumblezone.items.dispenserbehavior;

import com.telepathicgrunt.bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.bumblezone.mixin.blocks.ItemDispenserBehaviorInvoker;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;


public class GlassBottleDispenseBehavior extends ItemDispenserBehavior {
    public static DispenserBehavior DEFAULT_GLASS_BOTTLE_DISPENSE_BEHAVIOR;
    public static ItemDispenserBehavior DROP_ITEM_BEHAVIOR = new ItemDispenserBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    @Override
    public ItemStack dispenseSilently(BlockPointer source, ItemStack stack) {
        ServerWorld world = source.getWorld();
        Position iposition = DispenserBlock.getOutputLocation(source);
        BlockPos position = new BlockPos(iposition);
        BlockState blockstate = world.getBlockState(position);

        if (blockstate.getBlock() == BzBlocks.HONEYCOMB_BROOD) {
            // spawn bee if at final stage and front isn't blocked off
            int stage = blockstate.get(HoneycombBrood.STAGE);
            if (stage == 3) {
                // the front of the block
                BlockPos.Mutable blockpos = new BlockPos.Mutable().set(position);
                blockpos.move(blockstate.get(HoneycombBrood.FACING).getOpposite());

                // do nothing if front is blocked off
                if (!world.getBlockState(blockpos).getMaterial().isSolid()) {
                    MobEntity beeEntity = EntityType.BEE.create(world);
                    beeEntity.refreshPositionAndAngles(blockpos.getX() + 0.5f, blockpos.getY(), blockpos.getZ() + 0.5f, world.getRandom().nextFloat() * 360.0F, 0.0F);
                    beeEntity.initialize(world, world.getLocalDifficulty(new BlockPos(beeEntity.getPos())), SpawnReason.TRIGGERED, null, null);
                    world.spawnEntity(beeEntity);

                }
            }

            // kill the brood block
            world.setBlockState(position, BzBlocks.EMPTY_HONEYCOMB_BROOD.getDefaultState()
                    .with(Properties.FACING, blockstate.get(Properties.FACING)));
            stack.decrement(1);

            if (!stack.isEmpty())
                addHoneyBottleToDispenser(source, Items.HONEY_BOTTLE);
            else
                stack = new ItemStack(Items.HONEY_BOTTLE);
        }
        // remove honey
        else if (blockstate.getBlock() == BzBlocks.FILLED_POROUS_HONEYCOMB) {
            world.setBlockState(position, BzBlocks.POROUS_HONEYCOMB.getDefaultState());
            stack.decrement(1);

            if (!stack.isEmpty())
                addHoneyBottleToDispenser(source, Items.HONEY_BOTTLE);
            else
                stack = new ItemStack(Items.HONEY_BOTTLE);
        }
        //pick up sugar water
        else if (blockstate.getBlock() == BzBlocks.SUGAR_WATER_BLOCK ||
                (blockstate.getBlock() == BzBlocks.HONEY_CRYSTAL && blockstate.get(Properties.WATERLOGGED))) {
            stack.decrement(1);
            if(!stack.isEmpty())
                addHoneyBottleToDispenser(source, BzItems.SUGAR_WATER_BOTTLE);
            else
                stack = new ItemStack(BzItems.SUGAR_WATER_BOTTLE);
        }
        else {
            // If it instanceof DefaultDispenseItemBehavior, call dispenseStack directly to avoid
            // playing particles and sound twice due to dispense method having that by default.
            if(DEFAULT_GLASS_BOTTLE_DISPENSE_BEHAVIOR instanceof ItemDispenserBehavior) {
                return ((ItemDispenserBehaviorInvoker)DEFAULT_GLASS_BOTTLE_DISPENSE_BEHAVIOR).thebumblezone_invokeDispenseSilently(source, stack);
            }
            else {
                // Fallback to dispense as someone chose to make a custom class without dispenseStack.
                return DEFAULT_GLASS_BOTTLE_DISPENSE_BEHAVIOR.dispense(source, stack);
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
    private static void addHoneyBottleToDispenser(BlockPointer source, Item item) {
        if (source.getBlockEntity() instanceof DispenserBlockEntity) {
            DispenserBlockEntity dispenser = source.getBlockEntity();
            ItemStack honeyBottle = new ItemStack(item);
            if (!HopperBlockEntity.transfer(null, dispenser, honeyBottle, null).isEmpty()) {
                DROP_ITEM_BEHAVIOR.dispense(source, honeyBottle);
            }
        }
    }
}
