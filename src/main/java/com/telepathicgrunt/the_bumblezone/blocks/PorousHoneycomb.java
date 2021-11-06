package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.modcompat.BuzzierBeesCompat;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.tags.BzItemTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PorousHoneycomb extends Block {

    public PorousHoneycomb() {
        super(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.COLOR_ORANGE).harvestTool(ToolType.AXE).strength(0.5F, 0.5F).sound(SoundType.CORAL_BLOCK));
    }


    /**
     * Allow player to harvest honey and put honey into this block using bottles
     */
    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType use(BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand, BlockRayTraceResult raytraceResult) {
        ItemStack itemstack = playerEntity.getItemInHand(playerHand);
        /*
         * Player is adding honey to this block if it is not filled with honey
         */
        if (itemstack.getItem() == Items.HONEY_BOTTLE) {
            world.setBlock(position, BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState(), 3); // added honey to this block
            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BOTTLE_EMPTY, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            if (!playerEntity.isCreative()) {
                Item item = itemstack.getItem();
                itemstack.shrink(1);
                GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(item), true);
            }

            return ActionResultType.SUCCESS;
        }
        else if (itemstack.getItem().is(BzItemTags.HONEY_BUCKETS)) {
            // added honey to this block and neighboring blocks
            world.setBlock(position, BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState(), 3);

            // Clientside shuffle wont match server so let server fill neighbors and autosync to client.
            if(!world.isClientSide()) {
                int filledNeighbors = 0;
                List<Direction> shuffledDirections = Arrays.asList(Direction.values());
                Collections.shuffle(shuffledDirections);
                for(Direction direction : shuffledDirections) {
                    BlockState sideState = world.getBlockState(position.relative(direction));
                    if(sideState.is(BzBlocks.POROUS_HONEYCOMB.get())) {
                        world.setBlock(position.relative(direction), BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState(), 3);
                        filledNeighbors++;
                    }
                    if(filledNeighbors == 2) break;
                }
            }

            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BUCKET_EMPTY, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            if (!playerEntity.isCreative()) {
                Item item = itemstack.getItem();
                itemstack.shrink(1);
                GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(item), true);
            }

            return ActionResultType.SUCCESS;
        }

        //allow compat with honey wand use
        else if (ModChecker.buzzierBeesPresent && BzModCompatibilityConfigs.allowHoneyWandCompat.get())
        {
            ActionResultType action = BuzzierBeesCompat.honeyWandGivingHoney(itemstack, playerEntity, playerHand);
            if (action == ActionResultType.SUCCESS)
            {
                world.setBlock(position, BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState(), 3); // added honey to this block
                world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.HONEY_BLOCK_BREAK, SoundCategory.NEUTRAL, 1.0F, 1.0F);

                return action;
            }
        }

        return super.use(thisBlockState, world, position, playerEntity, playerHand, raytraceResult);
    }
}
