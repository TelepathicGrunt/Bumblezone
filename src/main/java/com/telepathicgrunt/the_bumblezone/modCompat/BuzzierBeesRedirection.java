package com.telepathicgrunt.the_bumblezone.modCompat;

import com.minecraftabnormals.buzzier_bees.common.blocks.CandleBlock;
import com.minecraftabnormals.buzzier_bees.core.registry.BBBlocks;
import com.minecraftabnormals.buzzier_bees.core.registry.BBItems;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.mixin.DispenserBlockInvoker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BuzzierBeesRedirection {

    public static BlockState getCrystallizedHoneyBlock() {
        return BuzzierBeesCompat.getCrystallizedHoneyBlock();
    }

    public static ActionResultType honeyWandTakingHoney(ItemStack itemstack, BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand) {
        return BuzzierBeesCompat.honeyWandTakingHoney(itemstack, thisBlockState, world, position, playerEntity, playerHand);
    }

    public static ActionResultType honeyWandGivingHoney(ItemStack itemstack, BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand) {
        return BuzzierBeesCompat.honeyWandGivingHoney(itemstack, thisBlockState, world, position, playerEntity, playerHand);
    }

    public static ActionResultType bottledBeeInteract(ItemStack itemstack, BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand) {
        return BuzzierBeesCompat.bottledBeeInteract(itemstack, thisBlockState, world, position, playerEntity, playerHand);
    }

    public static BlockState BBGetRandomTier1Candle(Random random, int numOfCandles, boolean waterlogged, boolean lit)
    {
        return BuzzierBeesCompat.BBGetRandomTier1Candle(random, numOfCandles, waterlogged, lit);
    }

    public static BlockState BBGetRandomTier2Candle(Random random, int lowerEndBias, int numOfCandles, boolean waterlogged, boolean lit)
    {
        return BuzzierBeesCompat.BBGetRandomTier2Candle(random, lowerEndBias, numOfCandles, waterlogged, lit);
    }

    public static BlockState BBGetRandomTier3Candle(Random random, int lowerEndBias, int numOfCandles, boolean waterlogged, boolean lit)
    {
        return BuzzierBeesCompat.BBGetRandomTier3Candle(random, lowerEndBias, numOfCandles, waterlogged, lit);
    }
}
