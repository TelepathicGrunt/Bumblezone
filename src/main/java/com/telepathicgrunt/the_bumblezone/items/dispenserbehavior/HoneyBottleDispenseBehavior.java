package com.telepathicgrunt.the_bumblezone.items.dispenserbehavior;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.tags.BzItemTags;
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
    public ItemStack execute(IBlockSource source, ItemStack stack) {
        ServerWorld world = source.getLevel();
        IPosition iposition = DispenserBlock.getDispensePosition(source);
        BlockPos position = new BlockPos(iposition);
        BlockState blockstate = world.getBlockState(position);

        if (blockstate.is(BzBlocks.HONEYCOMB_BROOD.get()) && BzItemTags.BEE_FEEDING_ITEMS.contains(stack.getItem())) {
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

                world.setBlockAndUpdate(position, blockstate.setValue(HoneycombBrood.STAGE, 0));
            }
            else {
                world.setBlockAndUpdate(position, blockstate.setValue(HoneycombBrood.STAGE, stage + 1));
            }

            stack.shrink(1);
            if (!Bumblezone.BzGeneralConfig.dispensersDropGlassBottles.get()) {
                if (!stack.isEmpty())
                    addGlassBottleToDispenser(source);
                else
                    stack = new ItemStack(Items.GLASS_BOTTLE);
            }
            else {
                DROP_ITEM_BEHAVIOR.dispense(source, new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        else if (blockstate.getBlock() == BzBlocks.POROUS_HONEYCOMB.get()) {
            world.setBlockAndUpdate(position, BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState());
            stack.shrink(1);
            if (!Bumblezone.BzGeneralConfig.dispensersDropGlassBottles.get()) {
                if (!stack.isEmpty())
                    addGlassBottleToDispenser(source);
                else
                    stack = new ItemStack(Items.GLASS_BOTTLE);
            }
        }
        else {
            return GeneralUtils.dispenseStackProperly(source, stack, DEFAULT_HONEY_BOTTLE_DISPENSE_BEHAVIOR);
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
     * Adds glass bottle to dispenser or if no room, dispense it
     */
    private static void addGlassBottleToDispenser(IBlockSource source) {
        if (source.getEntity() instanceof DispenserTileEntity) {
			DispenserTileEntity dispenser = source.getEntity();
            ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
            if (!HopperTileEntity.addItem(null, dispenser, bottle, null).isEmpty()) {
                DROP_ITEM_BEHAVIOR.dispense(source, bottle);
            }
        }
    }
}
