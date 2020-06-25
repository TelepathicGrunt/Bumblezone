package net.telepathicgrunt.bumblezone.blocks;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
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
import net.minecraft.world.World;


public class PorousHoneycomb extends Block {

    public PorousHoneycomb() {
        super(FabricBlockSettings.of(Material.ORGANIC_PRODUCT, MaterialColor.ORANGE).strength(0.5F, 0.5F).sounds(BlockSoundGroup.CORAL).build());
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

                if (itemstack.isEmpty()) {
                    playerEntity.setStackInHand(playerHand, new ItemStack(Items.GLASS_BOTTLE)); // places empty bottle in hand
                } else if (!playerEntity.inventory.insertStack(new ItemStack(Items.GLASS_BOTTLE))) // places empty bottle in inventory
                {
                    playerEntity.dropItem(new ItemStack(Items.GLASS_BOTTLE), false); // drops empty bottle if inventory is full
                }
            }

            return ActionResult.SUCCESS;
        }

        return super.onUse(thisBlockState, world, position, playerEntity, playerHand, raytraceResult);
    }
}
