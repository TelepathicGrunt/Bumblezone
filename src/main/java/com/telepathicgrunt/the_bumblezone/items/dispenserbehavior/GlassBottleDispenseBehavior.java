package com.telepathicgrunt.the_bumblezone.items.dispenserbehavior;

import com.telepathicgrunt.the_bumblezone.blocks.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
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
    public ItemStack execute(IBlockSource source, ItemStack stack) {
        ServerWorld world = source.getLevel();
        IPosition iposition = DispenserBlock.getDispensePosition(source);
        BlockPos position = new BlockPos(iposition);
        BlockState blockstate = world.getBlockState(position);

        if (blockstate.getBlock() == BzBlocks.HONEYCOMB_BROOD.get()) {
            // spawn bee if at final stage and front isn't blocked off
            int stage = blockstate.getValue(HoneycombBrood.STAGE);
            if (stage == 3) {
                // the front of the block
                BlockPos.Mutable blockpos = new BlockPos.Mutable().set(position);
                blockpos.move(blockstate.getValue(HoneycombBrood.FACING).getOpposite());

                // do nothing if front is blocked off
                if (!world.getBlockState(blockpos).getMaterial().isSolid()) {
                    MobEntity beeEntity = EntityType.BEE.create(world);
                    beeEntity.moveTo(blockpos.getX() + 0.5f, blockpos.getY(), blockpos.getZ() + 0.5f, world.getRandom().nextFloat() * 360.0F, 0.0F);
                    beeEntity.finalizeSpawn(world, world.getCurrentDifficultyAt(new BlockPos(beeEntity.blockPosition())), SpawnReason.TRIGGERED, null, null);
                    world.addFreshEntity(beeEntity);

                }
            }

            // kill the brood block
            world.setBlockAndUpdate(position, BzBlocks.EMPTY_HONEYCOMB_BROOD.get().defaultBlockState()
                    .setValue(BlockStateProperties.FACING, blockstate.getValue(BlockStateProperties.FACING)));
            stack.shrink(1);

            if (!stack.isEmpty())
                addHoneyBottleToDispenser(source, Items.HONEY_BOTTLE);
            else
                stack = new ItemStack(Items.HONEY_BOTTLE);
        }
        // remove honey
        else if (blockstate.getBlock() == BzBlocks.FILLED_POROUS_HONEYCOMB.get()) {
            world.setBlockAndUpdate(position, BzBlocks.POROUS_HONEYCOMB.get().defaultBlockState());
            stack.shrink(1);

            if (!stack.isEmpty())
                addHoneyBottleToDispenser(source, Items.HONEY_BOTTLE);
            else
                stack = new ItemStack(Items.HONEY_BOTTLE);
        }
        //pick up sugar water
        else if (blockstate.getBlock() == BzFluids.SUGAR_WATER_BLOCK.get() ||
                (blockstate.getBlock() == BzBlocks.HONEY_CRYSTAL.get() && blockstate.getValue(BlockStateProperties.WATERLOGGED))) {
            stack.shrink(1);
            if(!stack.isEmpty())
                addHoneyBottleToDispenser(source, BzItems.SUGAR_WATER_BOTTLE.get());
            else
                stack = new ItemStack(BzItems.SUGAR_WATER_BOTTLE.get());
        }
        //pick up honey fluid
        else if (blockstate.getBlock() == BzFluids.HONEY_FLUID_BLOCK.get() && blockstate.getFluidState().isSource()) {
            world.setBlockAndUpdate(position, BzFluids.HONEY_FLUID_FLOWING.get().defaultFluidState().createLegacyBlock().setValue(HoneyFluidBlock.LEVEL, 5));
            stack.shrink(1);
            if(!stack.isEmpty())
                addHoneyBottleToDispenser(source, Items.HONEY_BOTTLE);
            else
                stack = new ItemStack(Items.HONEY_BOTTLE);
        }
        else {
            return GeneralUtils.dispenseStackProperly(source, stack, DEFAULT_GLASS_BOTTLE_DISPENSE_BEHAVIOR);
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
    private static void addHoneyBottleToDispenser(IBlockSource source, Item item) {
        if (source.getEntity() instanceof DispenserTileEntity) {
            DispenserTileEntity dispenser = source.getEntity();
            ItemStack honeyBottle = new ItemStack(item);
            if (!HopperTileEntity.addItem(null, dispenser, honeyBottle, null).isEmpty()) {
                DROP_ITEM_BEHAVIOR.dispense(source, honeyBottle);
            }
        }
    }
}
