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
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;


public class SugarWaterBucketDispenseBehavior extends ItemDispenserBehavior {
    private static final ItemDispenserBehavior DROP_ITEM_BEHAVIOR = new ItemDispenserBehavior();

    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    public ItemStack dispenseSilently(BlockPointer source, ItemStack stack) {
        BucketItem bucketitem = (BucketItem) stack.getItem();
        Position iposition = DispenserBlock.getOutputLocation(source);
        BlockPos position = new BlockPos(iposition);
        ServerWorld world = source.getWorld();
        BlockState blockstate = world.getBlockState(position);

        if (bucketitem.placeFluid(null, world, position, null)) {

            bucketitem.onEmptied(null, world, stack, position);
            return new ItemStack(Items.BUCKET);
        }
        else if(blockstate.getBlock() == BzBlocks.HONEY_CRYSTAL && !blockstate.get(Properties.WATERLOGGED)) {

            world.setBlockState(position, BzBlocks.HONEY_CRYSTAL.getDefaultState()
                    .with(Properties.FACING, blockstate.get(Properties.FACING))
                    .with(Properties.WATERLOGGED, true));
            return new ItemStack(Items.BUCKET);
        }
        else {
            return DROP_ITEM_BEHAVIOR.dispense(source, stack);
        }
    }
}
