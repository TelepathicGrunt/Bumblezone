package net.telepathicgrunt.bumblezone.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.blocks.HoneycombBrood;


public class SugarWaterBottleDispenseBehavior extends ItemDispenserBehavior {
    public static ItemDispenserBehavior DROP_ITEM_BEHAVIOR = new ItemDispenserBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    @Override
    public ItemStack dispenseSilently(BlockPointer source, ItemStack stack) {
        World world = source.getWorld();
        Position iposition = DispenserBlock.getOutputLocation(source);
        BlockPos position = new BlockPos(iposition);
        BlockState blockstate = world.getBlockState(position);

        if (blockstate.getBlock() == BzBlocks.HONEYCOMB_BROOD) {
            float chance = world.random.nextFloat();
            if (chance <= 0.3F) {
                // spawn bee if at final stage and front isn't blocked off
                int stage = blockstate.get(HoneycombBrood.STAGE);
                if (stage == 3) {
                    // the front of the block
                    BlockPos.Mutable blockpos = new BlockPos.Mutable().set(position);
                    blockpos.setOffset(blockstate.get(HoneycombBrood.FACING).getOpposite());

                    // do nothing if front is blocked off
                    if (!world.getBlockState(blockpos).getMaterial().isSolid()) {
                        MobEntity beeEntity = EntityType.BEE.create(world);
                        beeEntity.refreshPositionAndAngles(blockpos.getX() + 0.5f, blockpos.getY(), blockpos.getZ() + 0.5f, world.getRandom().nextFloat() * 360.0F, 0.0F);
                        beeEntity.initialize(world, world.getLocalDifficulty(new BlockPos(beeEntity.getPos())), SpawnType.TRIGGERED, null, (CompoundTag) null);
                        world.spawnEntity(beeEntity);
                        world.setBlockState(position, blockstate.with(HoneycombBrood.STAGE, Integer.valueOf(0)));
                    }
                } else {
                    world.setBlockState(position, blockstate.with(HoneycombBrood.STAGE, Integer.valueOf(stage + 1)));
                }
            }

            stack.decrement(1);

            if (!Bumblezone.BZ_CONFIG.dispensersDropGlassBottles) {
                if (!stack.isEmpty())
                    addGlassBottleToDispenser(source);
                else
                    stack = new ItemStack(Items.GLASS_BOTTLE);
            } else {
                DROP_ITEM_BEHAVIOR.dispense(source, new ItemStack(Items.GLASS_BOTTLE));
            }
        } else {
            return super.dispenseSilently(source, stack);
        }

        return stack;
    }


    /**
     * Play the dispense sound from the specified block.
     */
    @Override
    protected void playSound(BlockPointer source) {
        source.getWorld().playGlobalEvent(1002, source.getBlockPos(), 0);
    }


    /**
     * Adds glass bottle to dispenser or if no room, dispense it
     */
    private static void addGlassBottleToDispenser(BlockPointer source) {
        if (source.getBlockEntity() instanceof DispenserBlockEntity) {
            DispenserBlockEntity dispenser = (DispenserBlockEntity) source.getBlockEntity();
            ItemStack honeyBottle = new ItemStack(Items.GLASS_BOTTLE);
            if (!HopperBlockEntity.transfer(null, dispenser, honeyBottle, null).isEmpty()) {
                DROP_ITEM_BEHAVIOR.dispense(source, honeyBottle);
            }
        }
    }
}
