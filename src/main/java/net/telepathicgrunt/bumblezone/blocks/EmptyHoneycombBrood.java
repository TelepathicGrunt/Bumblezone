package net.telepathicgrunt.bumblezone.blocks;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.telepathicgrunt.bumblezone.modCompat.ModChecker;
import net.telepathicgrunt.bumblezone.modCompat.PotionOfBeesRedirection;


public class EmptyHoneycombBrood extends ProperFacingBlock {

    public EmptyHoneycombBrood() {
        super(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MaterialColor.ORANGE).strength(0.5F, 0.5F).sounds(BlockSoundGroup.CORAL).build());
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.SOUTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }


    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultState().with(FACING, context.getSide().getOpposite());
    }



    /**
     * Allow player to harvest honey and put honey into this block using bottles
     */
    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand, BlockHitResult HitResult) {
        ItemStack itemstack = playerEntity.getStackInHand(playerHand);

        /*
         * Player is harvesting the honey from this block if it is filled with honey
         */
        if (ModChecker.potionOfBeesPresent && PotionOfBeesRedirection.POBIsPotionOfBeesItem(itemstack.getItem())) {

            playerEntity.swingHand(playerHand);
            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            world.setBlockState(position, BzBlocks.HONEYCOMB_BROOD.getDefaultState()
                    .with(HoneycombBrood.STAGE, 0)
                    .with(FacingBlock.FACING, thisBlockState.get(FacingBlock.FACING)));

            if (!playerEntity.isCreative()) {
                itemstack.decrement(1); // remove current bee bottle

                if (itemstack.isEmpty()) {
                    playerEntity.setStackInHand(playerHand, new ItemStack(Items.GLASS_BOTTLE)); // places glass bottle in hand
                }
                else if (!playerEntity.inventory.insertStack(new ItemStack(Items.GLASS_BOTTLE))) // places glass bottle in inventory
                {
                    playerEntity.dropItem(new ItemStack(Items.GLASS_BOTTLE), false); // drops glass bottle if inventory is full
                }
            }

            return ActionResult.SUCCESS;
        }

        return super.onUse(thisBlockState, world, position, playerEntity, playerHand, HitResult);
    }
}
