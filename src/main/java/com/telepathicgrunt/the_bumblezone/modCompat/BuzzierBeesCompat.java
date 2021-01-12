package com.telepathicgrunt.the_bumblezone.modCompat;

import com.minecraftabnormals.buzzier_bees.common.blocks.CandleBlock;
import com.minecraftabnormals.buzzier_bees.core.registry.BBBlocks;
import com.minecraftabnormals.buzzier_bees.core.registry.BBItems;
import com.mojang.datafixers.util.Pair;
import com.resourcefulbees.resourcefulbees.block.multiblocks.apiary.ApiaryBlock;
import com.resourcefulbees.resourcefulbees.block.multiblocks.apiary.ApiaryBreederBlock;
import com.resourcefulbees.resourcefulbees.block.multiblocks.apiary.ApiaryStorageBlock;
import com.resourcefulbees.resourcefulbees.registry.ModBlocks;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.features.BzConfiguredFeatures;
import com.telepathicgrunt.the_bumblezone.mixin.DispenserBlockInvoker;
import com.telepathicgrunt.the_bumblezone.tags.BZBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

import static com.telepathicgrunt.the_bumblezone.features.BzFeatures.HONEYCOMB_BUMBLEZONE;

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

        //tier 1 candles  - no effects basic colors candles

        TIER_1_CANDLES_VARIANTS.add(BBBlocks.CANDLE.get());
        TIER_1_CANDLES_VARIANTS.add(BBBlocks.WHITE_CANDLE.get());
        TIER_1_CANDLES_VARIANTS.add(BBBlocks.ORANGE_CANDLE.get());
        TIER_1_CANDLES_VARIANTS.add(BBBlocks.MAGENTA_CANDLE.get());
        TIER_1_CANDLES_VARIANTS.add(BBBlocks.LIGHT_BLUE_CANDLE.get());
        TIER_1_CANDLES_VARIANTS.add(BBBlocks.YELLOW_CANDLE.get());
        TIER_1_CANDLES_VARIANTS.add(BBBlocks.LIME_CANDLE.get());
        TIER_1_CANDLES_VARIANTS.add(BBBlocks.PINK_CANDLE.get());
        TIER_1_CANDLES_VARIANTS.add(BBBlocks.GRAY_CANDLE.get());
        TIER_1_CANDLES_VARIANTS.add(BBBlocks.LIGHT_GRAY_CANDLE.get());
        TIER_1_CANDLES_VARIANTS.add(BBBlocks.CYAN_CANDLE.get());
        TIER_1_CANDLES_VARIANTS.add(BBBlocks.PURPLE_CANDLE.get());
        TIER_1_CANDLES_VARIANTS.add(BBBlocks.BLUE_CANDLE.get());
        TIER_1_CANDLES_VARIANTS.add(BBBlocks.BROWN_CANDLE.get());
        TIER_1_CANDLES_VARIANTS.add(BBBlocks.GREEN_CANDLE.get());
        TIER_1_CANDLES_VARIANTS.add(BBBlocks.RED_CANDLE.get());
        TIER_1_CANDLES_VARIANTS.add(BBBlocks.BLACK_CANDLE.get());

        //tier 2 candles - no effects mixed colors candles plus generally negative effect candles

        TIER_2_CANDLES_VARIANTS.add(BBBlocks.WHITE_CANDLE.get());
        TIER_2_CANDLES_VARIANTS.add(BBBlocks.ORANGE_CANDLE.get());
        TIER_2_CANDLES_VARIANTS.add(BBBlocks.YELLOW_CANDLE.get());
        TIER_2_CANDLES_VARIANTS.add(BBBlocks.BLUE_CANDLE.get());
        TIER_2_CANDLES_VARIANTS.add(BBBlocks.GREEN_CANDLE.get());
        TIER_2_CANDLES_VARIANTS.add(BBBlocks.PURPLE_CANDLE.get());
        TIER_2_CANDLES_VARIANTS.add(BBBlocks.RED_CANDLE.get());
        TIER_2_CANDLES_VARIANTS.add(BBBlocks.BLACK_CANDLE.get());

        TIER_2_CANDLES_VARIANTS.add(BBBlocks.WHITE_CLOVER_SCENTED_CANDLE.get());
        TIER_2_CANDLES_VARIANTS.add(BBBlocks.PINK_CLOVER_SCENTED_CANDLE.get());
        TIER_2_CANDLES_VARIANTS.add(BBBlocks.WHITE_TULIP_SCENTED_CANDLE.get());
        TIER_2_CANDLES_VARIANTS.add(BBBlocks.ORANGE_TULIP_SCENTED_CANDLE.get());
        TIER_2_CANDLES_VARIANTS.add(BBBlocks.PINK_TULIP_SCENTED_CANDLE.get());
        TIER_2_CANDLES_VARIANTS.add(BBBlocks.RED_TULIP_SCENTED_CANDLE.get());
        TIER_2_CANDLES_VARIANTS.add(BBBlocks.AZURE_BLUET_SCENTED_CANDLE.get());
        TIER_2_CANDLES_VARIANTS.add(BBBlocks.LILY_OF_THE_VALLEY_SCENTED_CANDLE.get());
        TIER_2_CANDLES_VARIANTS.add(BBBlocks.SOUL_CANDLE.get());
        TIER_2_CANDLES_VARIANTS.add(BBBlocks.ENDER_CANDLE.get());

        //tier 3 candles - no effect mixed colors candles and generally positive effect candles + wither candle

        //16
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.WHITE_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.ORANGE_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.YELLOW_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.BLUE_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.GREEN_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.PURPLE_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.RED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.BLACK_CANDLE.get());

        TIER_3_CANDLES_VARIANTS.add(BBBlocks.WHITE_CLOVER_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.PINK_CLOVER_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.WHITE_TULIP_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.ORANGE_TULIP_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.PINK_TULIP_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.RED_TULIP_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.AZURE_BLUET_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.LILY_OF_THE_VALLEY_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.SOUL_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.ENDER_CANDLE.get());

        //6
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.YELLOW_HIBISCUS_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.ORANGE_HIBISCUS_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.RED_HIBISCUS_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.PINK_HIBISCUS_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.MAGENTA_HIBISCUS_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.PURPLE_HIBISCUS_SCENTED_CANDLE.get());

        //9
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.BLUE_ORCHID_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.DANDELION_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.CORNFLOWER_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.CARTWHEEL_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.POPPY_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.OXEYE_DAISY_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.DIANTHUS_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.BLUEBELL_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.VIOLET_SCENTED_CANDLE.get());

        //9
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.WARM_MONKEY_BRUSH_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.HOT_MONKEY_BRUSH_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.SCALDING_MONKEY_BRUSH_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.GILIA_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.YUCCA_FLOWER_SCENTED_CANDLE.get());

        TIER_3_CANDLES_VARIANTS.add(BBBlocks.PINK_SEAROCKET_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.WHITE_SEAROCKET_SCENTED_CANDLE.get());

        TIER_3_CANDLES_VARIANTS.add(BBBlocks.ALLIUM_SCENTED_CANDLE.get());
        TIER_3_CANDLES_VARIANTS.add(BBBlocks.WITHER_ROSE_SCENTED_CANDLE.get());

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
