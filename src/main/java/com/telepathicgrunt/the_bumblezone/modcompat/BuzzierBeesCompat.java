package com.telepathicgrunt.the_bumblezone.modcompat;

import com.minecraftabnormals.buzzier_bees.common.blocks.CandleBlock;
import com.minecraftabnormals.buzzier_bees.core.registry.BBBlocks;
import com.minecraftabnormals.buzzier_bees.core.registry.BBItems;
import com.minecraftabnormals.buzzier_bees.core.registry.BBVillagers;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.DispenserBlockInvoker;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
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
        CRYSTALLIZED_HONEY_BLOCK = BBBlocks.CRYSTALLIZED_HONEY_BLOCK.get().defaultBlockState();
        Item BOTTLED_BEES = ForgeRegistries.ITEMS.getValue(new ResourceLocation(BUZZIER_BEES_NAMESPACE, "bee_bottle"));

        if (BOTTLED_BEES != null && Bumblezone.BzModCompatibilityConfig.allowPotionOfBeesCompat.get()) {
            BuzzierBeesBottledBeesDispenseBehavior.DEFAULT_BOTTLED_BEE_DISPENSE_BEHAVIOR = ((DispenserBlockInvoker) Blocks.DISPENSER).thebumblezone_invokeGetDispenseMethod(new ItemStack(BOTTLED_BEES));
            DispenserBlock.registerBehavior(BBItems.BOTTLE_OF_BEE.get(), BEHAVIOUR_BOTTLED_BEE_DISPENSE_ITEM); //adds compatibility with bottled bees in dispensers
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

        // fires when server starts up so long after FMLCommonSetupEvent.
        // Thus it is safe to register this event here.
        // Need lowest priority to make sure we add trades after the other mod has created their trades.
        if(Bumblezone.BzModCompatibilityConfig.allowBuzzierBeesTradeCompat.get()){
            IEventBus forgeBus = MinecraftForge.EVENT_BUS;
            forgeBus.addListener(EventPriority.LOWEST, BuzzierBeesCompat::setupBuzzierBeesTrades);
        }

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.buzzierBeesPresent = true;
    }

    public static void setupBuzzierBeesTrades(VillagerTradesEvent event) {
        if (event.getType() == BBVillagers.APIARIST.get()) {
            Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();

            List<VillagerTrades.ITrade> tradeList = new ArrayList<>(trades.get(2));
            tradeList.add(new GeneralUtils.BasicItemTrade(Items.EMERALD, 1, BzItems.STICKY_HONEY_RESIDUE.get(), 2));
            trades.put(2, tradeList);

            tradeList = new ArrayList<>(trades.get(3));
            tradeList.add(new GeneralUtils.BasicItemTrade(BzItems.HONEY_CRYSTAL_SHARDS.get(), 3, Items.EMERALD, 1));
            trades.put(3, tradeList);

            tradeList = new ArrayList<>(trades.get(4));
            tradeList.add(new GeneralUtils.BasicItemTrade(Items.EMERALD, 2, BzItems.HONEY_CRYSTAL.get(), 1));
            trades.put(4, tradeList);

            tradeList = new ArrayList<>(trades.get(5));
            tradeList.add(new GeneralUtils.BasicItemTrade(Items.EMERALD, 20, BzItems.HONEYCOMB_LARVA.get(), 1));
            trades.put(5, tradeList);
        }
    }

    public static BlockState getCrystallizedHoneyBlock() {
        return CRYSTALLIZED_HONEY_BLOCK;
    }


    public static ActionResultType honeyWandTakingHoney(ItemStack itemstack, PlayerEntity playerEntity, Hand playerHand) {
        if (itemstack.getItem() == BBItems.HONEY_WAND.get()) {
            if (!playerEntity.isCrouching()) {
                if (!playerEntity.isCreative()) {
                    //replaced empty Honey Wand with Sticky Honey Wand in hand
                    itemstack.shrink(1);
                    GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(BBItems.STICKY_HONEY_WAND.get()), false);
                }

                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.FAIL;
    }

    public static ActionResultType honeyWandGivingHoney(ItemStack itemstack, PlayerEntity playerEntity, Hand playerHand) {
        if (itemstack.getItem() == BBItems.STICKY_HONEY_WAND.get()) {
            if (!playerEntity.isCrouching()) {
                if (!playerEntity.isCreative()) {
                    //replaced Honey Wand with empty Sticky Honey Wand in hand
                    itemstack.shrink(1);
                    GeneralUtils.givePlayerItem(playerEntity, playerHand, new ItemStack(BBItems.HONEY_WAND.get()), false);
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
                    playerEntity.setItemInHand(playerHand, new ItemStack(Items.GLASS_BOTTLE)); //replaced bottled bee with glass bottle
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
        return TIER_1_CANDLES_VARIANTS.get(index).defaultBlockState()
                .setValue(CandleBlock.CANDLES, numOfCandles)
                .setValue(CandleBlock.WATERLOGGED, waterlogged)
                .setValue(CandleBlock.LIT, lit);
    }

    /**
     * Picks a random BuzzierBees scented candle with lower indices list being more common
     */
    public static BlockState BBGetRandomTier2Candle(Random random, int lowerEndBias, int numOfCandles, boolean waterlogged, boolean lit) {
        int index = TIER_2_CANDLES_VARIANTS.size() - 1;

        for (int i = 0; i < lowerEndBias && index != 0; i++) {
            index = random.nextInt(index + 1);
        }

        return TIER_2_CANDLES_VARIANTS.get(index).defaultBlockState()
                .setValue(CandleBlock.CANDLES, numOfCandles)
                .setValue(CandleBlock.WATERLOGGED, waterlogged)
                .setValue(CandleBlock.LIT, lit);
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


        return TIER_3_CANDLES_VARIANTS.get(index).defaultBlockState()
                .setValue(CandleBlock.CANDLES, numOfCandles)
                .setValue(CandleBlock.WATERLOGGED, waterlogged)
                .setValue(CandleBlock.LIT, lit);
    }
}
