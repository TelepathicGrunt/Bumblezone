package com.telepathicgrunt.the_bumblezone.modinit;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import com.teamresourceful.resourcefullib.common.fluid.registry.ResourcefulFluidRegistry;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistryType;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluid;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidType;
import com.telepathicgrunt.the_bumblezone.fluids.RoyalJellyFluid;
import com.telepathicgrunt.the_bumblezone.fluids.RoyalJellyFluidBlock;
import com.telepathicgrunt.the_bumblezone.fluids.RoyalJellyFluidType;
import com.telepathicgrunt.the_bumblezone.fluids.SugarWaterBlock;
import com.telepathicgrunt.the_bumblezone.fluids.SugarWaterBubbleColumnBlock;
import com.telepathicgrunt.the_bumblezone.fluids.SugarWaterFluid;
import com.telepathicgrunt.the_bumblezone.fluids.SugarWaterFluidType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

public class BzFluids {
    public static final ResourcefulRegistry<Fluid> FLUIDS = ResourcefulRegistries.create(BuiltInRegistries.FLUID, Bumblezone.MODID);
    public static final ResourcefulFluidRegistry FLUID_TYPES = ResourcefulRegistries.create(ResourcefulRegistryType.FLUID, Bumblezone.MODID);

    //FluidTypes
    public static final RegistryEntry<FluidData> SUGAR_WATER_FLUID_TYPE = FLUID_TYPES.register("sugar_water", SugarWaterFluidType.create());
    public static final RegistryEntry<FluidData> HONEY_FLUID_TYPE = FLUID_TYPES.register("honey", HoneyFluidType.create());
    public static final RegistryEntry<FluidData> ROYAL_JELLY_FLUID_TYPE = FLUID_TYPES.register("royal_jelly", RoyalJellyFluidType.create());

    //Fluids
    public static final RegistryEntry<FlowingFluid> SUGAR_WATER_FLUID = FLUIDS.register("sugar_water_still", () -> new SugarWaterFluid.Source(BzFluids.SUGAR_WATER_FLUID_TYPE.get()));
    public static final RegistryEntry<FlowingFluid> SUGAR_WATER_FLUID_FLOWING = FLUIDS.register("sugar_water_flowing", () -> new SugarWaterFluid.Flowing(BzFluids.SUGAR_WATER_FLUID_TYPE.get()));
    public static final RegistryEntry<FlowingFluid> HONEY_FLUID = FLUIDS.register("honey_fluid_still", () -> new HoneyFluid.Source(BzFluids.HONEY_FLUID_TYPE.get()));
    public static final RegistryEntry<FlowingFluid> HONEY_FLUID_FLOWING = FLUIDS.register("honey_fluid_flowing", () -> new HoneyFluid.Flowing(BzFluids.HONEY_FLUID_TYPE.get()));
    public static final RegistryEntry<FlowingFluid> ROYAL_JELLY_FLUID = FLUIDS.register("royal_jelly_fluid_still", () -> new RoyalJellyFluid.Source(BzFluids.ROYAL_JELLY_FLUID_TYPE.get()));
    public static final RegistryEntry<FlowingFluid> ROYAL_JELLY_FLUID_FLOWING = FLUIDS.register("royal_jelly_fluid_flowing", () -> new RoyalJellyFluid.Flowing(BzFluids.ROYAL_JELLY_FLUID_TYPE.get()));

    //FluidBlocks
    public static final RegistryEntry<LiquidBlock> SUGAR_WATER_BLOCK = BzBlocks.BLOCKS.register("sugar_water_block", () -> new SugarWaterBlock(BzFluids.SUGAR_WATER_FLUID_TYPE.get()));
    public static final RegistryEntry<LiquidBlock> HONEY_FLUID_BLOCK = BzBlocks.BLOCKS.register("honey_fluid_block", () -> new HoneyFluidBlock(BzFluids.HONEY_FLUID_TYPE.get()));
    public static final RegistryEntry<LiquidBlock> ROYAL_JELLY_FLUID_BLOCK = BzBlocks.BLOCKS.register("royal_jelly_fluid_block", () -> new RoyalJellyFluidBlock(BzFluids.ROYAL_JELLY_FLUID_TYPE.get()));

    //Bubble Columns
    public static final RegistryEntry<SugarWaterBubbleColumnBlock> SUGAR_WATER_BUBBLE_COLUMN_BLOCK = BzBlocks.BLOCKS.register("sugar_water_bubble_column_block", SugarWaterBubbleColumnBlock::new);
}
