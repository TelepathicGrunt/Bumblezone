package com.telepathicgrunt.the_bumblezone.blocks;

import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;


public class EmptyHoneycombBrood extends ProperFacingBlock {

    public EmptyHoneycombBrood() {
        super(QuiltBlockSettings.of(Material.CLAY, MaterialColor.COLOR_ORANGE).strength(0.5F, 0.5F).sound(SoundType.CORAL_BLOCK));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }


    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace().getOpposite());
    }



    /**
     * Allow player to harvest honey and put honey into this block using bottles
     */
    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState thisBlockState, Level world, BlockPos position, Player playerEntity, InteractionHand playerHand, BlockHitResult HitResult) {
        ItemStack itemstack = playerEntity.getItemInHand(playerHand);

        /*
         * Player is harvesting the honey from this block if it is filled with honey
         */
        /*
        if (ModChecker.potionOfBeesPresent && PotionOfBeesRedirection.POBIsPotionOfBeesItem(itemstack.getItem())) {

            playerEntity.swingHand(playerHand);
            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            world.setBlockState(position, BzBlocks.HONEYCOMB_BROOD.defaultBlockState()
                    .with(HoneycombBrood.STAGE, 0)
                    .with(FacingBlock.FACING, thisBlockState.get(FacingBlock.FACING)));

            if (!playerEntity.isCreative()) {
                itemstack.decrement(1); // remove current bee bottle

                if (itemstack.isEmpty()) {
                    playerEntity.setStackInHand(playerHand, new ItemStack(Items.GLASS_BOTTLE)); // places glass bottle in hand
                }
                else if (!playerEntity.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE))) // places glass bottle in inventory
                {
                    playerEntity.dropItem(new ItemStack(Items.GLASS_BOTTLE), false); // drops glass bottle if inventory is full
                }
            }

            return ActionResult.SUCCESS;
        }
        */

        return super.use(thisBlockState, world, position, playerEntity, playerHand, HitResult);
    }
}
