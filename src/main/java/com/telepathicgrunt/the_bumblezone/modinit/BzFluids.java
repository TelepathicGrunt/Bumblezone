package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.blocks.SugarWaterBlock;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluid;
import com.telepathicgrunt.the_bumblezone.fluids.SugarWaterFluid;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FlowingFluid;

public class BzFluids {
    public static final FlowingFluid SUGAR_WATER_FLUID = new SugarWaterFluid.Source();
    public static final FlowingFluid SUGAR_WATER_FLUID_FLOWING = new SugarWaterFluid.Flowing();
    public static final Block SUGAR_WATER_BLOCK = new SugarWaterBlock(SUGAR_WATER_FLUID);

    public static final FlowingFluid HONEY_FLUID = new HoneyFluid.Source();
    public static final FlowingFluid HONEY_FLUID_FLOWING = new HoneyFluid.Flowing();
    public static final Block HONEY_FLUID_BLOCK = new HoneyFluidBlock(HONEY_FLUID);

    public static void registerFluids() {
        Registry.register(Registry.FLUID, new ResourceLocation(Bumblezone.MODID, "sugar_water_still"), SUGAR_WATER_FLUID);
        Registry.register(Registry.FLUID, new ResourceLocation(Bumblezone.MODID, "sugar_water_flowing"), SUGAR_WATER_FLUID_FLOWING);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "sugar_water_block"), SUGAR_WATER_BLOCK);
        Registry.register(Registry.FLUID, new ResourceLocation(Bumblezone.MODID, "honey_fluid_still"), HONEY_FLUID);
        Registry.register(Registry.FLUID, new ResourceLocation(Bumblezone.MODID, "honey_fluid_flowing"), HONEY_FLUID_FLOWING);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "honey_fluid_block"), HONEY_FLUID_BLOCK);
    }
}
