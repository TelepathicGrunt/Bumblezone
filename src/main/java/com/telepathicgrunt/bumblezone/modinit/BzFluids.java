package com.telepathicgrunt.bumblezone.modinit;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.blocks.HoneyFluidBlock;
import com.telepathicgrunt.bumblezone.blocks.SugarWaterBlock;
import com.telepathicgrunt.bumblezone.fluids.HoneyFluid;
import com.telepathicgrunt.bumblezone.fluids.SugarWaterFluid;
import net.minecraft.block.Block;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BzFluids {
    public static final FlowableFluid SUGAR_WATER_FLUID = new SugarWaterFluid.Source();
    public static final FlowableFluid SUGAR_WATER_FLUID_FLOWING = new SugarWaterFluid.Flowing();
    public static final Block SUGAR_WATER_BLOCK = new SugarWaterBlock(SUGAR_WATER_FLUID);

    public static final FlowableFluid HONEY_FLUID = new HoneyFluid.Source();
    public static final FlowableFluid HONEY_FLUID_FLOWING = new HoneyFluid.Flowing();
    public static final Block HONEY_FLUID_BLOCK = new HoneyFluidBlock(HONEY_FLUID);

    public static void registerFluids() {
        Registry.register(Registry.FLUID, new Identifier(Bumblezone.MODID, "sugar_water_still"), SUGAR_WATER_FLUID);
        Registry.register(Registry.FLUID, new Identifier(Bumblezone.MODID, "sugar_water_flowing"), SUGAR_WATER_FLUID_FLOWING);
        Registry.register(Registry.BLOCK, new Identifier(Bumblezone.MODID, "sugar_water_block"), SUGAR_WATER_BLOCK);
        Registry.register(Registry.FLUID, new Identifier(Bumblezone.MODID, "honey_fluid_still"), HONEY_FLUID);
        Registry.register(Registry.FLUID, new Identifier(Bumblezone.MODID, "honey_fluid_flowing"), HONEY_FLUID_FLOWING);
        Registry.register(Registry.BLOCK, new Identifier(Bumblezone.MODID, "honey_fluid_block"), HONEY_FLUID_BLOCK);
    }
}
