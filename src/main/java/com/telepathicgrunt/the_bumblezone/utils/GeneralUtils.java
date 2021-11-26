package com.telepathicgrunt.the_bumblezone.utils;

import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.configs.BzModCompatibilityConfigs;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.DefaultDispenseItemBehaviorInvoker;
import com.telepathicgrunt.the_bumblezone.mixin.world.BiomeGenerationSettingsAccessor;
import com.telepathicgrunt.the_bumblezone.tags.BzFluidTags;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.Hand;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
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
        List<List<Supplier<ConfiguredFeature<?, ?>>>> tempFeature = ((BiomeGenerationSettingsAccessor)biome.getGenerationSettings()).thebumblezone_getFeatures();
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
        ((BiomeGenerationSettingsAccessor)biome.getGenerationSettings()).thebumblezone_setFeatures(mutableGenerationStages);
    }

    // If it instanceof DefaultDispenseItemBehavior, call dispenseStack directly to avoid
    // playing particles and sound twice due to dispense method having that by default.
    public static ItemStack dispenseStackProperly(IBlockSource source, ItemStack stack, IDispenseItemBehavior defaultDispenseBehavior) {

        if (defaultDispenseBehavior instanceof DefaultDispenseItemBehavior) {
            return ((DefaultDispenseItemBehaviorInvoker) defaultDispenseBehavior).thebumblezone_invokeExecute(source, stack);
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

    //////////////////////////////////////////////

    /**
     * For giving the player an item properly into their inventory
     */
    public static void givePlayerItem(PlayerEntity playerEntity, Hand hand, ItemStack itemstack, boolean giveContainerItem) {
        if(giveContainerItem && !itemstack.hasContainerItem()) return;

        ItemStack itemToGive = giveContainerItem ? itemstack.getContainerItem() : itemstack;
        if (itemstack.isEmpty()) {
            // places result item in hand
            playerEntity.setItemInHand(hand, itemToGive);
        }
        // places result item in inventory
        else if (!playerEntity.inventory.add(itemToGive)) {
            // drops result item if inventory is full
            playerEntity.drop(itemToGive, false);
        }
    }

    //////////////////////////////////////////////
    @CapabilityInject(IFluidHandlerItem.class)
    static Capability<IFluidHandlerItem> FLUID_HANDLER_ITEM_CAPABILITY = null;

    public static boolean hasHoneyFluid(ItemStack itemstack) {
        return BzModCompatibilityConfigs.allowHoneyFluidTanksFeedingCompat.get() &&
                FluidUtil.getFluidContained(itemstack).orElse(FluidStack.EMPTY).getFluid().is(BzFluidTags.HONEY_FLUID);
    }

    public static boolean hasLargeAmountOfHoneyFluid(ItemStack itemstack) {
        if(!BzModCompatibilityConfigs.allowHoneyFluidTanksFeedingCompat.get())
            return false;

        AtomicBoolean lotsOfHoney = new AtomicBoolean(false);
        itemstack.getCapability(FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(
            cap -> {
                for(int tankIndex = 0; tankIndex < cap.getTanks(); tankIndex++) {
                    FluidStack fluidStack = cap.getFluidInTank(tankIndex);
                    // Cannot do simulated drain because bucket items require an exact 1000 drain and other people could make items
                    // that require a drain of 1500 which would make a drain of 1000 fail. This gets true amount of fluid always.
                    if(fluidStack.getFluid().is(BzFluidTags.HONEY_FLUID) && fluidStack.getAmount() >= 1000) {
                        lotsOfHoney.set(true);
                        break;
                    }
                }
            }
        );
        return lotsOfHoney.get();
    }
}
