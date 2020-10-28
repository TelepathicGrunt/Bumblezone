package com.telepathicgrunt.the_bumblezone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;


public class PorousHoneycomb extends Block {

    public PorousHoneycomb() {
        super(Block.Properties.create(Material.CLAY, MaterialColor.ADOBE).harvestTool(ToolType.AXE).hardnessAndResistance(0.5F, 0.5F).sound(SoundType.CORAL));
    }


    /**
     * Allow player to harvest honey and put honey into this block using bottles
     */
    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType onUse(BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand, BlockRayTraceResult raytraceResult) {
        ItemStack itemstack = playerEntity.getHeldItem(playerHand);
        /*
         * Player is adding honey to this block if it is not filled with honey
         */
        if (itemstack.getItem() == Items.HONEY_BOTTLE) {
            world.setBlockState(position, BzBlocks.FILLED_POROUS_HONEYCOMB.get().getDefaultState(), 3); // added honey to this block
            world.playSound(playerEntity, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            if (!playerEntity.isCreative()) {
                itemstack.shrink(1); // remove current honey bottle

                if (itemstack.isEmpty()) {
                    playerEntity.setHeldItem(playerHand, new ItemStack(Items.GLASS_BOTTLE)); // places empty bottle in hand
                } else if (!playerEntity.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE))) // places empty bottle in inventory
                {
                    playerEntity.dropItem(new ItemStack(Items.GLASS_BOTTLE), false); // drops empty bottle if inventory is full
                }
            }

            return ActionResultType.SUCCESS;
        }

        return super.onUse(thisBlockState, world, position, playerEntity, playerHand, raytraceResult);
    }
}
