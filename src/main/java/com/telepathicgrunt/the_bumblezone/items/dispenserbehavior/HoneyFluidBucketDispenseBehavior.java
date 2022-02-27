package com.telepathicgrunt.the_bumblezone.items.dispenserbehavior;

import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.tags.BzItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;


public class HoneyFluidBucketDispenseBehavior extends DefaultDispenseItemBehavior {
    private static final DefaultDispenseItemBehavior DROP_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack execute(BlockSource source, ItemStack stack) {
        BucketItem bucketitem = (BucketItem) stack.getItem();
        ServerLevel world = source.getLevel();
        Position iposition = DispenserBlock.getDispensePosition(source);
        BlockPos position = new BlockPos(iposition);
        BlockState blockstate = world.getBlockState(position);

        if (bucketitem.emptyContents(null, world, position, null)) {
            bucketitem.checkExtraContent(null, world, stack, position);
            return new ItemStack(Items.BUCKET);
        }
        else if (blockstate.is(BzBlocks.HONEYCOMB_BROOD) && stack.is(BzItemTags.BEE_FEEDING_ITEMS)) {
            // spawn bee if at final stage and front isn't blocked off
            int stage = blockstate.getValue(HoneycombBrood.STAGE);
            if (stage == 3) {
                // the front of the block
                BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos().set(position);
                blockpos.move(blockstate.getValue(HoneycombBrood.FACING).getOpposite());

                // do nothing if front is blocked off
                if (!world.getBlockState(blockpos).getMaterial().isSolid()) {
                    Mob beeEntity = EntityType.BEE.create(world);
                    beeEntity.moveTo(blockpos.getX() + 0.5f, blockpos.getY(), blockpos.getZ() + 0.5f, world.getRandom().nextFloat() * 360.0F, 0.0F);
                    beeEntity.finalizeSpawn(world, world.getCurrentDifficultyAt(new BlockPos(beeEntity.position())), MobSpawnType.TRIGGERED, null, null);
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
            return this.DROP_ITEM_BEHAVIOR.dispense(source, stack);
        }
    }

    /**
     * Adds bucket to dispenser or if no room, dispense it
     */
    private static void addBucketToDispenser(BlockSource source) {
        if (source.getEntity() instanceof DispenserBlockEntity) {
            DispenserBlockEntity dispenser = source.getEntity();
            ItemStack honeyBottle = new ItemStack(Items.BUCKET);
            if (!HopperBlockEntity.addItem(null, dispenser, honeyBottle, null).isEmpty()) {
                DROP_ITEM_BEHAVIOR.dispense(source, honeyBottle);
            }
        }
    }
}
