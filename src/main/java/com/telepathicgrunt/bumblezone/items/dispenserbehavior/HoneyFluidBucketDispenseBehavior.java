package com.telepathicgrunt.bumblezone.items.dispenserbehavior;

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
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


public class HoneyFluidBucketDispenseBehavior extends ItemDispenserBehavior {
    private static final ItemDispenserBehavior DROP_ITEM_BEHAVIOR = new ItemDispenserBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack dispenseSilently(BlockPointer source, ItemStack stack) {
        BucketItem bucketitem = (BucketItem) stack.getItem();
        ServerWorld world = source.getWorld();
        Position iposition = DispenserBlock.getOutputLocation(source);
        BlockPos position = new BlockPos(iposition);
        BlockState blockstate = world.getBlockState(position);

        if (bucketitem.placeFluid(null, world, position, null)) {
            bucketitem.onEmptied(null, world, stack, position);
            return new ItemStack(Items.BUCKET);
        }
        else if (blockstate.isOf(BzBlocks.HONEYCOMB_BROOD) && BzItemTags.BEE_FEEDING_ITEMS.contains(stack.getItem())) {
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
            }
            else {
                for(double xOffset = 0; xOffset <= 1; xOffset++) {
                    for(double yOffset = 0; yOffset <= 1; yOffset++) {
                        for(double zOffset = 0; zOffset <= 1; zOffset++) {
                            world.spawnParticles(
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

                world.setBlockState(position, blockstate.with(HoneycombBrood.STAGE, 3));
            }

            stack.decrement(1);
            if (!stack.isEmpty())
                addBucketToDispenser(source);
            else
                stack = new ItemStack(Items.BUCKET);

            return stack;
        }
        else {
            return this.DROP_ITEM_BEHAVIOR.dispense(source, stack);
        }
    }

    /**
     * Adds bucket to dispenser or if no room, dispense it
     */
    private static void addBucketToDispenser(BlockPointer source) {
        if (source.getBlockEntity() instanceof DispenserBlockEntity) {
            DispenserBlockEntity dispenser = source.getBlockEntity();
            ItemStack honeyBottle = new ItemStack(Items.BUCKET);
            if (!HopperBlockEntity.transfer(null, dispenser, honeyBottle, null).isEmpty()) {
                DROP_ITEM_BEHAVIOR.dispense(source, honeyBottle);
            }
        }
    }
}
