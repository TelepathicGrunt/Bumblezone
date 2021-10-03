package com.telepathicgrunt.bumblezone.items.dispenserbehavior;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.tags.BzItemTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;


public class SugarWaterBottleDispenseBehavior extends ItemDispenserBehavior {
    private static final ItemDispenserBehavior DROP_ITEM_BEHAVIOR = new ItemDispenserBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    @Override
    public ItemStack dispenseSilently(BlockPointer source, ItemStack stack) {
        ServerWorld world = source.getWorld();
        Position iposition = DispenserBlock.getOutputLocation(source);
        BlockPos position = new BlockPos(iposition);
        BlockState blockstate = world.getBlockState(position);

        if (blockstate.isOf(BzBlocks.HONEYCOMB_BROOD) && BzItemTags.BEE_FEEDING_ITEMS.contains(stack.getItem())) {
            float chance = world.random.nextFloat();
            if (chance <= 0.3F) {
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
                        world.setBlockState(position, blockstate.with(HoneycombBrood.STAGE, 0));
                    }
                } else {
                    world.setBlockState(position, blockstate.with(HoneycombBrood.STAGE, stage + 1));
                }
            }

            stack.decrement(1);

            if (!Bumblezone.BZ_CONFIG.BZBlockMechanicsConfig.dispensersDropGlassBottles) {
                if (!stack.isEmpty())
                    addGlassBottleToDispenser(source);
                else
                    stack = new ItemStack(Items.GLASS_BOTTLE);
            } else {
                DROP_ITEM_BEHAVIOR.dispense(source, new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        else {
            return super.dispenseSilently(source, stack);
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
     * Adds glass bottle to dispenser or if no room, dispense it
     */
    private static void addGlassBottleToDispenser(BlockPointer source) {
        if (source.getBlockEntity() instanceof DispenserBlockEntity) {
            DispenserBlockEntity dispenser = source.getBlockEntity();
            ItemStack honeyBottle = new ItemStack(Items.GLASS_BOTTLE);
            if (!HopperBlockEntity.transfer(null, dispenser, honeyBottle, null).isEmpty()) {
                DROP_ITEM_BEHAVIOR.dispense(source, honeyBottle);
            }
        }
    }
}
