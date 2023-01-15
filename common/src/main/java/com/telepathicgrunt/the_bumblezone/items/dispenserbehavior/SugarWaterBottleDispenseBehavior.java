package com.telepathicgrunt.the_bumblezone.items.dispenserbehavior;

import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;


public class SugarWaterBottleDispenseBehavior extends DefaultDispenseItemBehavior {
    private static final DefaultDispenseItemBehavior DROP_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    @Override
    public ItemStack execute(BlockSource source, ItemStack stack) {
        ServerLevel world = source.getLevel();
        Position iposition = DispenserBlock.getDispensePosition(source);
        BlockPos position = new BlockPos(iposition);
        BlockState blockstate = world.getBlockState(position);

        if (blockstate.is(BzBlocks.HONEYCOMB_BROOD.get()) && stack.is(BzTags.BEE_FEEDING_ITEMS)) {
            boolean deniedBeeSpawn = false;
            float chance = world.random.nextFloat();
            if (chance <= 0.3F) {
                // spawn bee if at final stage and front isn't blocked off
                int stage = blockstate.getValue(HoneycombBrood.STAGE);
                if (stage == 3) {
                    // the front of the block
                    BlockPos.MutableBlockPos blockpos = new BlockPos.MutableBlockPos().set(position);
                    blockpos.move(blockstate.getValue(HoneycombBrood.FACING).getOpposite());

                    // do nothing if front is blocked off
                    if (!world.getBlockState(blockpos).getMaterial().isSolid()) {
                        Mob beeEntity = EntityType.BEE.create(world);
                        beeEntity.moveTo(blockpos.getX() + 0.5f, blockpos.getY(), blockpos.getZ() + 0.5f, beeEntity.getRandom().nextFloat() * 360.0F, 0.0F);
                        beeEntity.finalizeSpawn(world, world.getCurrentDifficultyAt(new BlockPos(beeEntity.position())), MobSpawnType.TRIGGERED, null, null);
                        beeEntity.setBaby(true);
                        if (ForgeHooks.canEntitySpawn(beeEntity, world, beeEntity.position().x(), beeEntity.position().y(), beeEntity.position().z(), null, MobSpawnType.DISPENSER) != -1) {
                            world.addFreshEntity(beeEntity);
                            world.setBlockAndUpdate(position, blockstate.setValue(HoneycombBrood.STAGE, 0));
                        }
                        else {
                            deniedBeeSpawn = true;
                        }
                    }
                }
                else {
                    world.setBlockAndUpdate(position, blockstate.setValue(HoneycombBrood.STAGE, stage + 1));
                }
            }

            if (!deniedBeeSpawn) {
                stack.shrink(1);
            }

            if (!deniedBeeSpawn && !BzGeneralConfigs.dispensersDropGlassBottles.get()) {
                if (!stack.isEmpty())
                    addGlassBottleToDispenser(source);
                else
                    stack = new ItemStack(Items.GLASS_BOTTLE);
            }
            else {
                DROP_ITEM_BEHAVIOR.dispense(source, new ItemStack(Items.GLASS_BOTTLE));
            }
        }
        else {
            return super.execute(source, stack);
        }

        return stack;
    }


    /**
     * Play the dispense sound from the specified block.
     */
    @Override
    protected void playSound(BlockSource source) {
        source.getLevel().levelEvent(1002, source.getPos(), 0);
    }


    /**
     * Adds glass bottle to dispenser or if no room, dispense it
     */
    private static void addGlassBottleToDispenser(BlockSource source) {
        if (source.getEntity() instanceof DispenserBlockEntity) {
            DispenserBlockEntity dispenser = source.getEntity();
            ItemStack honeyBottle = new ItemStack(Items.GLASS_BOTTLE);
            if (!HopperBlockEntity.addItem(null, dispenser, honeyBottle, null).isEmpty()) {
                DROP_ITEM_BEHAVIOR.dispense(source, honeyBottle);
            }
        }
    }
}
