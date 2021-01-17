package com.telepathicgrunt.the_bumblezone.modCompat;

import com.minecraftabnormals.buzzier_bees.common.blocks.CandleBlock;
import com.minecraftabnormals.buzzier_bees.core.registry.BBBlocks;
import com.minecraftabnormals.buzzier_bees.core.registry.BBItems;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.DispenserBlockInvoker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
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
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BuzzierBeesCompat {

    private static final DefaultDispenseItemBehavior BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM = new BuzzierBeesBottledBeesDispenseBehavior();
    private static BlockState CRYSTALLIZED_HONEY_BLOCK;
    private static List<Block> TIER_1_CANDLES_VARIANTS;
    private static List<Block> TIER_2_CANDLES_VARIANTS;
    private static List<Block> TIER_3_CANDLES_VARIANTS;
    private static final String BUZZIER_BEES_NAMESPACE = "buzzier_bees";

    public static void setupBuzzierBees() {
        CRYSTALLIZED_HONEY_BLOCK = BBBlocks.CRYSTALLIZED_HONEY_BLOCK.get().getDefaultState();
        Item BOTTLED_BEES = ForgeRegistries.ITEMS.getValue(new ResourceLocation(BUZZIER_BEES_NAMESPACE, "bee_bottle"));

        if (BOTTLED_BEES != null && Bumblezone.BzModCompatibilityConfig.allowPotionOfBeesCompat.get()) {
            BuzzierBeesBottledBeesDispenseBehavior.DEFAULT_BOTTLED_BEE_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker) Blocks.DISPENSER).bz_invokeGetBehavior(new ItemStack(BOTTLED_BEES));
            DispenserBlock.registerDispenseBehavior(BBItems.BOTTLE_OF_BEE.get(), BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM); //adds compatibility with bottled bees in dispensers
        }

        TIER_1_CANDLES_VARIANTS = new ArrayList<>();
        TIER_2_CANDLES_VARIANTS = new ArrayList<>();
        TIER_3_CANDLES_VARIANTS = new ArrayList<>();

        //Tier 1 candles - basic no effects basic colors candles
        TIER_1_CANDLES_VARIANTS.addAll(Arrays.asList(
                BBBlocks.CANDLE.get(),
                BBBlocks.WHITE_CANDLE.get(),
                BBBlocks.ORANGE_CANDLE.get(),
                BBBlocks.MAGENTA_CANDLE.get(),
                BBBlocks.LIGHT_BLUE_CANDLE.get(),
                BBBlocks.YELLOW_CANDLE.get(),
                BBBlocks.LIME_CANDLE.get(),
                BBBlocks.PINK_CANDLE.get(),
                BBBlocks.GRAY_CANDLE.get(),
                BBBlocks.LIGHT_GRAY_CANDLE.get(),
                BBBlocks.CYAN_CANDLE.get(),
                BBBlocks.PURPLE_CANDLE.get(),
                BBBlocks.BLUE_CANDLE.get(),
                BBBlocks.BROWN_CANDLE.get(),
                BBBlocks.GREEN_CANDLE.get(),
                BBBlocks.RED_CANDLE.get(),
                BBBlocks.BLACK_CANDLE.get()
        ));

        //tier 2 candles - mixed colors candles plus generally negative effect candles
        TIER_2_CANDLES_VARIANTS.addAll(Arrays.asList(
                BBBlocks.WHITE_CANDLE.get(),
                BBBlocks.ORANGE_CANDLE.get(),
                BBBlocks.YELLOW_CANDLE.get(),
                BBBlocks.BLUE_CANDLE.get(),
                BBBlocks.GREEN_CANDLE.get(),
                BBBlocks.PURPLE_CANDLE.get(),
                BBBlocks.RED_CANDLE.get(),
                BBBlocks.BLACK_CANDLE.get(),

                BBBlocks.SOUL_CANDLE.get(),
                BBBlocks.ENDER_CANDLE.get(),

                BBBlocks.WHITE_CLOVER_SCENTED_CANDLE.get(),
                BBBlocks.PINK_CLOVER_SCENTED_CANDLE.get(),
                BBBlocks.AUTUMN_CROCUS_SCENTED_CANDLE.get(),
                BBBlocks.WATER_HYACINTH_SCENTED_CANDLE.get(),

                BBBlocks.WHITE_TULIP_SCENTED_CANDLE.get(),
                BBBlocks.ORANGE_TULIP_SCENTED_CANDLE.get(),
                BBBlocks.PINK_TULIP_SCENTED_CANDLE.get(),
                BBBlocks.RED_TULIP_SCENTED_CANDLE.get(),

                BBBlocks.LILY_OF_THE_VALLEY_SCENTED_CANDLE.get(),
                BBBlocks.AZURE_BLUET_SCENTED_CANDLE.get(),

                BBBlocks.BUTTERCUP_SCENTED_CANDLE.get(),
                BBBlocks.BUTTERCUP_SCENTED_CANDLE.get()
        ));

        //tier 3 candles - generally positive effect candles + wither candle
        TIER_3_CANDLES_VARIANTS.addAll(Arrays.asList(
                BBBlocks.SOUL_CANDLE.get(),
                BBBlocks.ENDER_CANDLE.get(),
                BBBlocks.BUTTERCUP_SCENTED_CANDLE.get(),

                BBBlocks.YELLOW_HIBISCUS_SCENTED_CANDLE.get(),
                BBBlocks.ORANGE_HIBISCUS_SCENTED_CANDLE.get(),
                BBBlocks.RED_HIBISCUS_SCENTED_CANDLE.get(),
                BBBlocks.PINK_HIBISCUS_SCENTED_CANDLE.get(),
                BBBlocks.MAGENTA_HIBISCUS_SCENTED_CANDLE.get(),
                BBBlocks.PURPLE_HIBISCUS_SCENTED_CANDLE.get(),

                BBBlocks.CORNFLOWER_SCENTED_CANDLE.get(),
                BBBlocks.RED_LOTUS_FLOWER_SCENTED_CANDLE.get(),
                BBBlocks.WHITE_LOTUS_FLOWER_SCENTED_CANDLE.get(),
                BBBlocks.BLUEBELL_SCENTED_CANDLE.get(),

                BBBlocks.BLUE_ORCHID_SCENTED_CANDLE.get(),
                BBBlocks.DANDELION_SCENTED_CANDLE.get(),
                BBBlocks.CARTWHEEL_SCENTED_CANDLE.get(),
                BBBlocks.POPPY_SCENTED_CANDLE.get(),
                BBBlocks.OXEYE_DAISY_SCENTED_CANDLE.get(),
                BBBlocks.DIANTHUS_SCENTED_CANDLE.get(),
                BBBlocks.VIOLET_SCENTED_CANDLE.get(),

                BBBlocks.WARM_MONKEY_BRUSH_SCENTED_CANDLE.get(),
                BBBlocks.HOT_MONKEY_BRUSH_SCENTED_CANDLE.get(),
                BBBlocks.SCALDING_MONKEY_BRUSH_SCENTED_CANDLE.get(),
                BBBlocks.GILIA_SCENTED_CANDLE.get(),
                BBBlocks.YUCCA_FLOWER_SCENTED_CANDLE.get(),

                BBBlocks.PINK_SEAROCKET_SCENTED_CANDLE.get(),
                BBBlocks.WHITE_SEAROCKET_SCENTED_CANDLE.get(),

                BBBlocks.ALLIUM_SCENTED_CANDLE.get(),
                BBBlocks.WITHER_ROSE_SCENTED_CANDLE.get()
        ));

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.buzzierBeesPresent = true;
    }

    public static BlockState getCrystallizedHoneyBlock() {
        return CRYSTALLIZED_HONEY_BLOCK;
    }


    public static ActionResultType honeyWandTakingHoney(ItemStack itemstack, BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand) {
        if (itemstack.getItem() == BBItems.HONEY_WAND.get()) {
            if (!playerEntity.isCrouching()) {
                if (!playerEntity.isCreative()) {
                    playerEntity.setHeldItem(playerHand, new ItemStack(BBItems.STICKY_HONEY_WAND.get())); //replaced empty Honey Wand with Sticky Honey Wand in hand
                }

                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.FAIL;
    }

    public static ActionResultType honeyWandGivingHoney(ItemStack itemstack, BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand) {
        if (itemstack.getItem() == BBItems.STICKY_HONEY_WAND.get()) {
            if (!playerEntity.isCrouching()) {
                if (!playerEntity.isCreative()) {
                    playerEntity.setHeldItem(playerHand, new ItemStack(BBItems.HONEY_WAND.get())); //replaced Sticky Honey Wand with empty Honey Wand in hand
                }

                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.FAIL;
    }

    public static ActionResultType bottledBeeInteract(ItemStack itemstack, BlockState thisBlockState, World world, BlockPos position, PlayerEntity playerEntity, Hand playerHand) {
        if (itemstack.getItem() == BBItems.BOTTLE_OF_BEE.get()) {
            if (!playerEntity.isCrouching()) {
                if (!playerEntity.isCreative()) {
                    playerEntity.setHeldItem(playerHand, new ItemStack(Items.GLASS_BOTTLE)); //replaced bottled bee with glass bottle
                }

                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.FAIL;
    }


    /**
     * Picks a random BuzzierBees candle with lower indices list being more common
     */
    public static BlockState BBGetRandomTier1Candle(Random random, int numOfCandles, boolean waterlogged, boolean lit) {
        int index = random.nextInt(TIER_1_CANDLES_VARIANTS.size());
        return TIER_1_CANDLES_VARIANTS.get(index).getDefaultState()
                .with(CandleBlock.CANDLES, numOfCandles)
                .with(CandleBlock.WATERLOGGED, waterlogged)
                .with(CandleBlock.LIT, lit);
    }

    /**
     * Picks a random BuzzierBees scented candle with lower indices list being more common
     */
    public static BlockState BBGetRandomTier2Candle(Random random, int lowerEndBias, int numOfCandles, boolean waterlogged, boolean lit) {
        int index = TIER_2_CANDLES_VARIANTS.size() - 1;

        for (int i = 0; i < lowerEndBias && index != 0; i++) {
            index = random.nextInt(index + 1);
        }

        return TIER_2_CANDLES_VARIANTS.get(index).getDefaultState()
                .with(CandleBlock.CANDLES, numOfCandles)
                .with(CandleBlock.WATERLOGGED, waterlogged)
                .with(CandleBlock.LIT, lit);
    }

    /**
     * Picks a random BuzzierBees scented candle with lower indices list being more common
     * lowerEndBias cannot be 0 or negative. usually 2
     */
    public static BlockState BBGetRandomTier3Candle(Random random, int lowerEndBias, int numOfCandles, boolean waterlogged, boolean lit) {
        int index = random.nextInt(TIER_3_CANDLES_VARIANTS.size());

        if (index >= 31 && random.nextInt(lowerEndBias + 2) != 0) {
            index = random.nextInt(index); //reroll to a lower subtier
        }

        if (index >= 22 && random.nextInt(lowerEndBias + 1) != 0) {
            index = random.nextInt(index); //reroll to a lower subtier
        }

        if (index >= 16 && random.nextInt(lowerEndBias) != 0) {
            index = random.nextInt(index); //reroll to a lower subtier
        }


        return TIER_3_CANDLES_VARIANTS.get(index).getDefaultState()
                .with(CandleBlock.CANDLES, numOfCandles)
                .with(CandleBlock.WATERLOGGED, waterlogged)
                .with(CandleBlock.LIT, lit);
    }
}
