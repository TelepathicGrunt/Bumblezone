package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.blocks.BzBlocks;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.fluids.BzFluids;
import com.telepathicgrunt.the_bumblezone.mixin.DefaultDispenseItemBehaviorInvoker;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;


public class GlassBottleDispenseBehavior extends DefaultDispenseItemBehavior {
    public static IDispenseItemBehavior DEFAULT_GLASS_BOTTLE_DISPENSE_BEHAVIOR;
    public static DefaultDispenseItemBehavior DROP_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    @Override
    public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
        ServerWorld world = source.getWorld();
        IPosition iposition = DispenserBlock.getDispensePosition(source);
        BlockPos position = new BlockPos(iposition);
        BlockState blockstate = world.getBlockState(position);

        if (blockstate.getBlock() == BzBlocks.HONEYCOMB_BROOD.get()) {
            // spawn bee if at final stage and front isn't blocked off
            int stage = blockstate.get(HoneycombBrood.STAGE);
            if (stage == 3) {
                // the front of the block
                BlockPos.Mutable blockpos = new BlockPos.Mutable().setPos(position);
                blockpos.move(blockstate.get(HoneycombBrood.FACING).getOpposite());

                // do nothing if front is blocked off
                if (!world.getBlockState(blockpos).getMaterial().isSolid()) {
                    MobEntity beeEntity = EntityType.BEE.create(world);
                    beeEntity.setLocationAndAngles(blockpos.getX() + 0.5f, blockpos.getY(), blockpos.getZ() + 0.5f, world.getRandom().nextFloat() * 360.0F, 0.0F);
                    beeEntity.onInitialSpawn(world, world.getDifficultyForLocation(new BlockPos(beeEntity.getBlockPos())), SpawnReason.TRIGGERED, null, null);
                    world.addEntity(beeEntity);

                }
            }

            // kill the brood block
            world.setBlockState(position, BzBlocks.EMPTY_HONEYCOMB_BROOD.get().getDefaultState()
                    .with(BlockStateProperties.FACING, blockstate.get(BlockStateProperties.FACING)));
            stack.shrink(1);

            if (!stack.isEmpty())
                addHoneyBottleToDispenser(source, Items.HONEY_BOTTLE);
            else
                stack = new ItemStack(Items.HONEY_BOTTLE);
        }
        // remove honey
        else if (blockstate.getBlock() == BzBlocks.FILLED_POROUS_HONEYCOMB.get()) {
            world.setBlockState(position, BzBlocks.POROUS_HONEYCOMB.get().getDefaultState());
            stack.shrink(1);

            if (!stack.isEmpty())
                addHoneyBottleToDispenser(source, Items.HONEY_BOTTLE);
            else
                stack = new ItemStack(Items.HONEY_BOTTLE);
        }
        //pick up sugar water
        else if (blockstate.getBlock() == BzFluids.SUGAR_WATER_BLOCK.get() ||
                (blockstate.getBlock() == BzBlocks.HONEY_CRYSTAL.get() && blockstate.get(BlockStateProperties.WATERLOGGED))) {
            stack.shrink(1);
            if(!stack.isEmpty())
                addHoneyBottleToDispenser(source, BzItems.SUGAR_WATER_BOTTLE.get());
            else
                stack = new ItemStack(BzItems.SUGAR_WATER_BOTTLE.get());
        }
        else {
            // If it instanceof DefaultDispenseItemBehavior, call dispenseStack directly to avoid
            // playing particles and sound twice due to dispense method having that by default.
            if(DEFAULT_GLASS_BOTTLE_DISPENSE_BEHAVIOR instanceof DefaultDispenseItemBehavior) {
                return ((DefaultDispenseItemBehaviorInvoker)DEFAULT_GLASS_BOTTLE_DISPENSE_BEHAVIOR).bz_invokeDispenseStack(source, stack);
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
    protected void playDispenseSound(IBlockSource source) {
        source.getWorld().playEvent(1002, source.getBlockPos(), 0);
    }


    /**
     * Adds honey bottle to dispenser or if no room, dispense it
     */
    private static void addHoneyBottleToDispenser(IBlockSource source, Item item) {
        if (source.getBlockTileEntity() instanceof DispenserTileEntity) {
            DispenserTileEntity dispenser = source.getBlockTileEntity();
            ItemStack honeyBottle = new ItemStack(item);
            if (!HopperTileEntity.putStackInInventoryAllSlots(null, dispenser, honeyBottle, null).isEmpty()) {
                DROP_ITEM_BEHAVIOR.dispense(source, honeyBottle);
            }
        }
    }
}
