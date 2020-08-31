package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.BzBlocks;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;


public class HoneyBottleDispenseBehavior extends DefaultDispenseItemBehavior {
    public static IDispenseItemBehavior DEFAULT_HONEY_BOTTLE_DISPENSE_BEHAVIOR;
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

        if (blockstate.getBlock() == BzBlocks.HONEYCOMB_BROOD) {
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
                    beeEntity.onInitialSpawn(world, world.getDifficultyForLocation(new BlockPos(beeEntity.getBlockPos())), SpawnReason.TRIGGERED, null, (CompoundNBT) null);
                    world.addEntity(beeEntity);
                }

                world.setBlockState(position, blockstate.with(HoneycombBrood.STAGE, 0));
            } else {
                world.setBlockState(position, blockstate.with(HoneycombBrood.STAGE, stage + 1));
            }

            stack.shrink(1);
            if (!Bumblezone.BzBlockMechanicsConfig.dispensersDropGlassBottles.get()) {
                if (!stack.isEmpty())
                    addGlassBottleToDispenser(source);
                else
                    stack = new ItemStack(Items.GLASS_BOTTLE);
            } else {
                DROP_ITEM_BEHAVIOR.dispense(source, new ItemStack(Items.GLASS_BOTTLE));
            }
        } else if (blockstate.getBlock() == BzBlocks.POROUS_HONEYCOMB) {
            world.setBlockState(position, BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState());
            stack.shrink(1);
            if (!Bumblezone.BzBlockMechanicsConfig.dispensersDropGlassBottles.get()) {
                if (!stack.isEmpty())
                    addGlassBottleToDispenser(source);
                else
                    stack = new ItemStack(Items.GLASS_BOTTLE);
            }
        } else {
            return DEFAULT_HONEY_BOTTLE_DISPENSE_BEHAVIOR.dispense(source, stack);
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
     * Adds glass bottle to dispenser or if no room, dispense it
     */
    private static void addGlassBottleToDispenser(IBlockSource source) {
        if (source.getBlockTileEntity() instanceof DispenserTileEntity) {
			DispenserTileEntity dispenser = source.getBlockTileEntity();
            ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
            if (!HopperTileEntity.putStackInInventoryAllSlots(null, dispenser, bottle, null).isEmpty()) {
                DROP_ITEM_BEHAVIOR.dispense(source, bottle);
            }
        }
    }
}
