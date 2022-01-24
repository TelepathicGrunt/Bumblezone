package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzCriterias;
import com.telepathicgrunt.the_bumblezone.tags.BzItemTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class PorousHoneycomb extends Block {

    public PorousHoneycomb() {
        super(BlockBehaviour.Properties.of(Material.CLAY, MaterialColor.COLOR_ORANGE).strength(0.5F, 0.5F).sound(SoundType.CORAL_BLOCK));
    }

    /**
     * Allow player to harvest honey and put honey into this block using bottles
     */
    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState thisBlockState, Level world, BlockPos position, Player playerEntity, InteractionHand playerHand, BlockHitResult raytraceResult) {
        ItemStack itemstack = playerEntity.getItemInHand(playerHand);
        /*
         * Player is adding honey to this block if it is not filled with honey
         */
        if (itemstack.getItem() == Items.HONEY_BOTTLE) {
            world.setBlock(position, BzBlocks.FILLED_POROUS_HONEYCOMB.get().defaultBlockState(), 3); // added honey to this block
            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BOTTLE_EMPTY, SoundSource.NEUTRAL, 1.0F, 1.0F);

            if (!playerEntity.isCreative()) {
                GeneralUtils.givePlayerItem(playerEntity, playerHand, itemstack, true, true);
            }

            return InteractionResult.SUCCESS;
        }

        else if (itemstack.is(BzItemTags.HONEY_BUCKETS)) {
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

                    if(filledNeighbors == 1 && playerEntity instanceof ServerPlayer) {
                        BzCriterias.HONEY_BUCKET_POROUS_HONEYCOMB_TRIGGER.trigger((ServerPlayer) playerEntity);
                    }

                    if(filledNeighbors == 2) break;
                }
            }

            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BUCKET_EMPTY, SoundSource.NEUTRAL, 1.0F, 1.0F);

            if (!playerEntity.isCreative()) {
                GeneralUtils.givePlayerItem(playerEntity, playerHand, itemstack, true, true);
            }

            return InteractionResult.SUCCESS;
        }

        return super.use(thisBlockState, world, position, playerEntity, playerHand, raytraceResult);
    }
}
