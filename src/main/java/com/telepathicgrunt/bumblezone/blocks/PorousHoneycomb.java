package com.telepathicgrunt.bumblezone.blocks;

import com.telepathicgrunt.bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.bumblezone.modinit.BzItems;
import com.telepathicgrunt.bumblezone.utils.GeneralUtils;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class PorousHoneycomb extends Block {

    public PorousHoneycomb() {
        super(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MapColor.ORANGE).strength(0.5F, 0.5F).sounds(BlockSoundGroup.CORAL));
    }

    /**
     * Allow player to harvest honey and put honey into this block using bottles
     */
    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand, BlockHitResult raytraceResult) {
        ItemStack itemstack = playerEntity.getStackInHand(playerHand);
        /*
         * Player is adding honey to this block if it is not filled with honey
         */
        if (itemstack.getItem() == Items.HONEY_BOTTLE) {
            world.setBlockState(position, BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState(), 3); // added honey to this block
            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            if (!playerEntity.isCreative()) {
                itemstack.decrement(1); // remove current honey bottle
                GeneralUtils.givePlayerItem(playerEntity, playerHand, itemstack, true);
            }

            return ActionResult.SUCCESS;
        }

        else if (itemstack.getItem() == BzItems.HONEY_BUCKET) {
            // added honey to this block and neighboring blocks
            world.setBlockState(position, BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState(), 3);

            // Clientside shuffle wont match server so let server fill neighbors and autosync to client.
            if(!world.isClient()) {
                int filledNeighbors = 0;
                List<Direction> shuffledDirections = Arrays.asList(Direction.values());
                Collections.shuffle(shuffledDirections);
                for(Direction direction : shuffledDirections) {
                    BlockState sideState = world.getBlockState(position.offset(direction));
                    if(sideState.isOf(BzBlocks.POROUS_HONEYCOMB)) {
                        world.setBlockState(position.offset(direction), BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState(), 3);
                        filledNeighbors++;
                    }
                    if(filledNeighbors == 2) break;
                }
            }

            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            if (!playerEntity.isCreative()) {
                itemstack.decrement(1); // remove current honey bucket
                GeneralUtils.givePlayerItem(playerEntity, playerHand, itemstack, true);
            }

            return ActionResult.SUCCESS;
        }

        return super.onUse(thisBlockState, world, position, playerEntity, playerHand, raytraceResult);
    }
}
