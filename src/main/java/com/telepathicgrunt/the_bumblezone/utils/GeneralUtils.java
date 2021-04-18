package com.telepathicgrunt.the_bumblezone.utils;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.DefaultDispenseItemBehaviorInvoker;
import com.telepathicgrunt.the_bumblezone.mixin.world.BiomeGenerationSettingsAccessor;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class GeneralUtils {

    private static int ACTIVE_ENTITIES = 0;

    public static void updateEntityCount(ServerWorld world){
        List<Entity> entitiesList = world.getEntities(null, (entity) -> true);
        ACTIVE_ENTITIES = entitiesList.size();
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

    /**
     * Helper method to make WB biomes mutable to add stuff to it later
     */
    public static void makeBiomeMutable(Biome biome){
        // Make the structure and features list mutable for modification late
        List<List<Supplier<ConfiguredFeature<?, ?>>>> tempFeature = ((BiomeGenerationSettingsAccessor)biome.getGenerationSettings()).bz_getFeatures();
        List<List<Supplier<ConfiguredFeature<?, ?>>>> mutableGenerationStages = new ArrayList<>();

        // Fill in generation stages so there are at least 10 or else Minecraft crashes.
        // (we need all stages for adding features/structures to the right stage too)
        for(int currentStageIndex = 0; currentStageIndex < Math.max(GenerationStage.Decoration.values().length, tempFeature.size()); currentStageIndex++){
            if(currentStageIndex >= tempFeature.size()){
                mutableGenerationStages.add(new ArrayList<>());
            }else{
                mutableGenerationStages.add(new ArrayList<>(tempFeature.get(currentStageIndex)));
            }
        }

        // Make the Structure and GenerationStages (features) list mutable for modification later
        ((BiomeGenerationSettingsAccessor)biome.getGenerationSettings()).bz_setFeatures(mutableGenerationStages);
    }

    // If it instanceof DefaultDispenseItemBehavior, call dispenseStack directly to avoid
    // playing particles and sound twice due to dispense method having that by default.
    public static ItemStack dispenseStackProperly(IBlockSource source, ItemStack stack, IDispenseItemBehavior defaultDispenseBehavior) {

        if (defaultDispenseBehavior instanceof DefaultDispenseItemBehavior) {
            return ((DefaultDispenseItemBehaviorInvoker) defaultDispenseBehavior).bz_invokeDispenseStack(source, stack);
        }
        else {
            // Fallback to dispense as someone chose to make a custom class without dispenseStack.
            return defaultDispenseBehavior.dispense(source, stack);
        }
    }

    //////////////////////////////////////////

    /**
     * For doing basic trades without forge's implementation.
     * Very short and barebone to what I want
     */
    public static class BasicItemTrade implements VillagerTrades.ITrade {
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
        public MerchantOffer getOffer(Entity entity, Random random) {
            ItemStack in = new ItemStack(this.itemToTrade, this.amountToGive);
            ItemStack out = new ItemStack(this.itemToReceive, this.amountToReceive);
            return new MerchantOffer(in, out, this.maxUses, this.experience, this.multiplier);
        }
    }

}
