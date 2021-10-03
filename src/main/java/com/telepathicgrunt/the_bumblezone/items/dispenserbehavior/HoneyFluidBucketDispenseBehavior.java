package com.telepathicgrunt.the_bumblezone.items.dispenserbehavior;

import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.tags.BzItemTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.DispenserTileEntity;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;


public class HoneyFluidBucketDispenseBehavior extends DefaultDispenseItemBehavior {
    private static final DefaultDispenseItemBehavior DROP_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack execute(IBlockSource source, ItemStack stack) {
        BucketItem bucketitem = (BucketItem) stack.getItem();
        IPosition iposition = DispenserBlock.getDispensePosition(source);
        BlockPos position = new BlockPos(iposition);
        ServerWorld world = source.getLevel();
        BlockState blockstate = world.getBlockState(position);

        if (bucketitem.emptyBucket(null, world, position, null)) {
            bucketitem.checkExtraContent(world, stack, position);
            return new ItemStack(Items.BUCKET);
        }
        else if (blockstate.is(BzBlocks.HONEYCOMB_BROOD.get()) && BzItemTags.BEE_FEEDING_ITEMS.contains(stack.getItem())) {
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
                    world.setBlockAndUpdate(position, blockstate.setValue(HoneycombBrood.STAGE, 0));
                }
            }
            else {
                for(double xOffset = 0; xOffset <= 1; xOffset++) {
                    for(double yOffset = 0; yOffset <= 1; yOffset++) {
                        for(double zOffset = 0; zOffset <= 1; zOffset++) {
                            world.sendParticles(
                                    ParticleTypes.HEART,
                                    position.getX() + xOffset,
                                    position.getY() + yOffset - 0.4f,
                                    position.getZ() + zOffset,
                                    1,
                                    world.getRandom().nextFloat() * 0.3f - 0.15f,
                                    world.getRandom().nextFloat() * 0.2f - 0.1f,
                                    world.getRandom().nextFloat() * 0.3f - 0.15f,
                                    world.getRandom().nextFloat() * 0.2f + 0.2f);
                        }
                    }
                }

                world.setBlockAndUpdate(position, blockstate.setValue(HoneycombBrood.STAGE, 3));
            }

            stack.shrink(1);
            if (!stack.isEmpty())
                addBucketToDispenser(source);
            else
                stack = new ItemStack(Items.BUCKET);

            return stack;
        }
        else {
            return DROP_ITEM_BEHAVIOR.dispense(source, stack);
        }
    }

    /**
     * Adds bucket to dispenser or if no room, dispense it
     */
    private static void addBucketToDispenser(IBlockSource source) {
        if (source.getEntity() instanceof DispenserTileEntity) {
            DispenserTileEntity dispenser = source.getEntity();
            ItemStack honeyBottle = new ItemStack(Items.BUCKET);
            if (!HopperTileEntity.addItem(null, dispenser, honeyBottle, null).isEmpty()) {
                DROP_ITEM_BEHAVIOR.dispense(source, honeyBottle);
            }
        }
    }
}
