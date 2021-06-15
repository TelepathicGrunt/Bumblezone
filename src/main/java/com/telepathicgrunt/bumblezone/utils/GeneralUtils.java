package com.telepathicgrunt.bumblezone.utils;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public class GeneralUtils {

    private static int ACTIVE_ENTITIES = 0;

    public static void updateEntityCount(ServerWorld world){

        // If iterable is a collection, just get size directly
        if (world.iterateEntities() instanceof Collection) {
            ACTIVE_ENTITIES = ((Collection<?>) world.iterateEntities()).size();
            return;
        }

        // If iterable isn't a collection, we have to manually count how many entities there are
        int counter = 0;
        for (Object ignored : world.iterateEntities()) {
            counter++;
        }
        ACTIVE_ENTITIES = counter;
    }

    public static int getEntityCountInBz(){
        return ACTIVE_ENTITIES;
    }


    /////////////////////////////

    // Weighted Random from: https://stackoverflow.com/a/6737362
    public static <T> T getRandomEntry(List<Pair<T, Integer>> rlList, Random random) {
        double totalWeight = 0.0;

        // Compute the total weight of all items together.
        for (Pair<T, Integer> pair : rlList) {
            totalWeight += pair.getSecond();
        }

        // Now choose a random item.
        int index = 0;
        for (double randomWeightPicked = random.nextFloat() * totalWeight; index < rlList.size() - 1; ++index) {
            randomWeightPicked -= rlList.get(index).getSecond();
            if (randomWeightPicked <= 0.0) break;
        }

        return rlList.get(index).getFirst();
    }

    ////////////////

    // Source: https://dzone.com/articles/be-lazy-with-java-8
    public static final class Lazy<T> {

        private volatile T value;

        public T getOrCompute(Supplier<T> supplier) {
            final T result = value; // Just one volatile read
            return result == null ? maybeCompute(supplier) : result;
        }

        private synchronized T maybeCompute(Supplier<T> supplier) {
            if (value == null) {
                value = requireNonNull(supplier.get());
            }
            return value;
        }
    }


    //////////////////////////////////////////

    /**
     * For doing basic trades.
     * Very short and barebone to what I want
     */
    public static class BasicItemTrade implements TradeOffers.Factory  {
        private final Item itemToTrade;
        private final Item itemToReceive;
        private final int amountToGive;
        private final int amountToReceive;
        protected final int maxUses;
        protected final int experience;
        protected final float multiplier;

        public BasicItemTrade(Item itemToTrade, int amountToGive, Item itemToReceive, int amountToReceive){
            this(itemToTrade, amountToGive, itemToReceive, amountToReceive, 20, 2, 0.05F);
        }

        public BasicItemTrade(Item itemToTrade, int amountToGive, Item itemToReceive, int amountToReceive, int maxUses, int experience, float multiplier){
            this.itemToTrade = itemToTrade;
            this.itemToReceive = itemToReceive;
            this.amountToGive = amountToGive;
            this.amountToReceive = amountToReceive;
            this.maxUses = maxUses;
            this.experience = experience;
            this.multiplier = multiplier;
        }

        @Override
        public TradeOffer create(Entity entity, Random random) {
            ItemStack in = new ItemStack(this.itemToTrade, this.amountToGive);
            ItemStack out = new ItemStack(this.itemToReceive, this.amountToReceive);
            return new TradeOffer(in, out, this.maxUses, this.experience, this.multiplier);
        }
    }

    ////////////////

    /**
     * Commonly used method for removing currently held item and giving Honey Bottle instead.
     */
    public static void giveHoneyBottle(PlayerEntity player, Hand hand, ItemStack itemstack, World world) {
        world.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        if (!player.isCreative()) {
            itemstack.decrement(1);
            if (itemstack.isEmpty()) {
                player.setStackInHand(hand, new ItemStack(Items.HONEY_BOTTLE));
            } else if (!player.getInventory().insertStack(new ItemStack(Items.HONEY_BOTTLE))) {
                player.dropItem(new ItemStack(Items.HONEY_BOTTLE), false);
            }
        }
    }

    ///////////////////////

    public static final List<BlockState> VANILLA_CANDLES = ImmutableList.of(
            Blocks.CANDLE.getDefaultState(),
            Blocks.CYAN_CANDLE.getDefaultState(),
            Blocks.BLACK_CANDLE.getDefaultState(),
            Blocks.BLUE_CANDLE.getDefaultState(),
            Blocks.BROWN_CANDLE.getDefaultState(),
            Blocks.GRAY_CANDLE.getDefaultState(),
            Blocks.GREEN_CANDLE.getDefaultState(),
            Blocks.LIGHT_BLUE_CANDLE.getDefaultState(),
            Blocks.LIGHT_GRAY_CANDLE.getDefaultState(),
            Blocks.LIME_CANDLE.getDefaultState(),
            Blocks.MAGENTA_CANDLE.getDefaultState(),
            Blocks.ORANGE_CANDLE.getDefaultState(),
            Blocks.PINK_CANDLE.getDefaultState(),
            Blocks.PURPLE_CANDLE.getDefaultState(),
            Blocks.RED_CANDLE.getDefaultState(),
            Blocks.WHITE_CANDLE.getDefaultState(),
            Blocks.YELLOW_CANDLE.getDefaultState()
    );
}
