package com.telepathicgrunt.the_bumblezone.items.dispenserbehavior;

import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.DefaultDispenseItemBehaviorInvoker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzFluids;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;


public class GlassBottleDispenseBehavior extends DefaultDispenseItemBehavior {
    public static DispenseItemBehavior DEFAULT_GLASS_BOTTLE_DISPENSE_BEHAVIOR;
    public static DefaultDispenseItemBehavior DROP_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    @Override
    public ItemStack execute(BlockSource source, ItemStack stack) {
        ServerLevel world = source.getLevel();
        Position iposition = DispenserBlock.getDispensePosition(source);
        BlockPos position = new BlockPos(iposition);
        BlockState blockstate = world.getBlockState(position);

        if (blockstate.getBlock() == BzBlocks.HONEYCOMB_BROOD) {
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
                    world.addFreshEntity(beeEntity);
                }
            }

            // kill the brood block
            world.setBlockAndUpdate(position, BzBlocks.EMPTY_HONEYCOMB_BROOD.defaultBlockState()
                    .setValue(BlockStateProperties.FACING, blockstate.getValue(BlockStateProperties.FACING)));
            stack.shrink(1);

            if (!stack.isEmpty())
                addHoneyBottleToDispenser(source, Items.HONEY_BOTTLE);
            else
                stack = new ItemStack(Items.HONEY_BOTTLE);
        }
        // remove honey
        else if (blockstate.getBlock() == BzBlocks.FILLED_POROUS_HONEYCOMB) {
            world.setBlockAndUpdate(position, BzBlocks.POROUS_HONEYCOMB.defaultBlockState());
            stack.shrink(1);

            if (!stack.isEmpty())
                addHoneyBottleToDispenser(source, Items.HONEY_BOTTLE);
            else
                stack = new ItemStack(Items.HONEY_BOTTLE);
        }
        //pick up sugar water
        else if (blockstate.getBlock() == BzFluids.SUGAR_WATER_BLOCK ||
                (blockstate.getBlock() == BzBlocks.HONEY_CRYSTAL && blockstate.getValue(BlockStateProperties.WATERLOGGED))) {
            stack.shrink(1);
            if(!stack.isEmpty())
                addHoneyBottleToDispenser(source, BzItems.SUGAR_WATER_BOTTLE);
            else
                stack = new ItemStack(BzItems.SUGAR_WATER_BOTTLE);
        }
        //pick up honey fluid
        else if (blockstate.getBlock() == BzFluids.HONEY_FLUID_BLOCK && blockstate.getFluidState().isSource()) {
            world.setBlockAndUpdate(position, BzFluids.HONEY_FLUID_FLOWING.defaultFluidState().createLegacyBlock().setValue(HoneyFluidBlock.LEVEL, 5));
            stack.shrink(1);
            if(!stack.isEmpty())
                addHoneyBottleToDispenser(source, Items.HONEY_BOTTLE);
            else
                stack = new ItemStack(Items.HONEY_BOTTLE);
        }
        else {
            // If it instanceof DefaultDispenseItemBehavior, call dispenseStack directly to avoid
            // playing particles and sound twice due to dispense method having that by default.
            if(DEFAULT_GLASS_BOTTLE_DISPENSE_BEHAVIOR instanceof DefaultDispenseItemBehavior) {
                return ((DefaultDispenseItemBehaviorInvoker)DEFAULT_GLASS_BOTTLE_DISPENSE_BEHAVIOR).thebumblezone_invokeDispenseSilently(source, stack);
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
    protected void playSound(BlockSource source) {
        source.getLevel().levelEvent(1002, source.getPos(), 0);
    }


    /**
     * Adds honey bottle to dispenser or if no room, dispense it
     */
    private static void addHoneyBottleToDispenser(BlockSource source, Item item) {
        if (source.getEntity() instanceof DispenserBlockEntity) {
            DispenserBlockEntity dispenser = source.getEntity();
            ItemStack honeyBottle = new ItemStack(item);
            if (!HopperBlockEntity.addItem(null, dispenser, honeyBottle, null).isEmpty()) {
                DROP_ITEM_BEHAVIOR.dispense(source, honeyBottle);
            }
        }
    }
}
