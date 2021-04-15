package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modcompat.BuzzierBeesRedirection;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;


public class EmptyHoneycombBrood extends ProperFacingBlock {

    public EmptyHoneycombBrood() {
        super(Block.Properties.create(Material.CLAY, MaterialColor.ADOBE).hardnessAndResistance(0.5F, 0.5F).sound(SoundType.CORAL));
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.SOUTH));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }


    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getFace().getOpposite());
    }

    /**
     * MOD COMPAT
     *
     * Allow player to revive this block
     */
    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onBlockActivated(BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand, BlockRayTraceResult raytraceResult) {

        ItemStack itemstack = playerEntity.getHeldItem(playerHand);

        //Buzzier Bees bottled bees compat
        if (ModChecker.buzzierBeesPresent && Bumblezone.BzModCompatibilityConfig.allowBottledBeesCompat.get()) {
            // Player is trying to revive the block
            ActionResultType action = BuzzierBeesRedirection.bottledBeeInteract(itemstack, thisBlockState, world, position, playerEntity, playerHand);
            if (action == ActionResultType.SUCCESS) {
                world.playSound(playerEntity, playerEntity.getPosX(), playerEntity.getPosY(), playerEntity.getPosZ(), SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                world.setBlockState(position, BzBlocks.HONEYCOMB_BROOD.get().getDefaultState()
                        .with(HoneycombBrood.STAGE, 0)
                        .with(DirectionalBlock.FACING, thisBlockState.get(DirectionalBlock.FACING)));

                return action;
            }
        }

        return super.onBlockActivated(thisBlockState, world, position, playerEntity, playerHand, raytraceResult);
    }
}
