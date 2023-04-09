package com.telepathicgrunt.the_bumblezone.modinit;

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
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfo;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfoRegistry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

public class BzFluids {
    public static final ResourcefulRegistry<Fluid> FLUIDS = ResourcefulRegistries.create(BuiltInRegistries.FLUID, Bumblezone.MODID);
    public static final FluidInfoRegistry FLUID_TYPES = ResourcefulRegistries.createFluidRegistry(Bumblezone.MODID);

    //FluidTypes
    public static final RegistryEntry<FluidInfo> SUGAR_WATER_FLUID_TYPE = FLUID_TYPES.register(SugarWaterFluidType.create());
    public static final RegistryEntry<FluidInfo> HONEY_FLUID_TYPE = FLUID_TYPES.register(HoneyFluidType.create());
    public static final RegistryEntry<FluidInfo> ROYAL_JELLY_FLUID_TYPE = FLUID_TYPES.register(RoyalJellyFluidType.create());

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
