package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modcompat.BuzzierBeesCompat;
import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import net.minecraft.block.AbstractBlock;
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
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class EmptyHoneycombBrood extends ProperFacingBlock {

    public EmptyHoneycombBrood() {
        super(AbstractBlock.Properties.of(Material.CLAY, MaterialColor.COLOR_ORANGE).strength(0.5F, 0.5F).sound(SoundType.CORAL_BLOCK));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }


    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
    }

    /**
     * MOD COMPAT
     *
     * Allow player to revive this block
     */
    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType use(BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand, BlockRayTraceResult raytraceResult) {

        ItemStack itemstack = playerEntity.getItemInHand(playerHand);

        //Buzzier Bees bottled bees compat
        if (ModChecker.buzzierBeesPresent && Bumblezone.BzModCompatibilityConfig.allowBottledBeesCompat.get()) {
            // Player is trying to revive the block
            ActionResultType action = BuzzierBeesCompat.bottledBeeInteract(itemstack, thisBlockState, world, position, playerEntity, playerHand);
            if (action == ActionResultType.SUCCESS) {
                world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BOTTLE_EMPTY, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                world.setBlockAndUpdate(position, BzBlocks.HONEYCOMB_BROOD.get().defaultBlockState()
                        .setValue(HoneycombBrood.STAGE, 0)
                        .setValue(DirectionalBlock.FACING, thisBlockState.getValue(DirectionalBlock.FACING)));

                return action;
            }
        }

        return super.use(thisBlockState, world, position, playerEntity, playerHand, raytraceResult);
    }
}
