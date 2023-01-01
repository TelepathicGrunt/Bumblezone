package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluid;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.fluids.RoyalJellyFluid;
import com.telepathicgrunt.the_bumblezone.fluids.RoyalJellyFluidBlock;
import com.telepathicgrunt.the_bumblezone.fluids.SugarWaterBlock;
import com.telepathicgrunt.the_bumblezone.fluids.SugarWaterFluid;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
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

    public static final FlowingFluid ROYAL_JELLY_FLUID = new RoyalJellyFluid.Source();
    public static final FlowingFluid ROYAL_JELLY_FLUID_FLOWING = new RoyalJellyFluid.Flowing();
    public static final Block ROYAL_JELLY_FLUID_BLOCK = new RoyalJellyFluidBlock(ROYAL_JELLY_FLUID);

    public static void registerFluids() {
        Registry.register(BuiltInRegistries.FLUID, new ResourceLocation(Bumblezone.MODID, "sugar_water_still"), SUGAR_WATER_FLUID);
        Registry.register(BuiltInRegistries.FLUID, new ResourceLocation(Bumblezone.MODID, "sugar_water_flowing"), SUGAR_WATER_FLUID_FLOWING);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Bumblezone.MODID, "sugar_water_block"), SUGAR_WATER_BLOCK);
        Registry.register(BuiltInRegistries.FLUID, new ResourceLocation(Bumblezone.MODID, "honey_fluid_still"), HONEY_FLUID);
        Registry.register(BuiltInRegistries.FLUID, new ResourceLocation(Bumblezone.MODID, "honey_fluid_flowing"), HONEY_FLUID_FLOWING);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Bumblezone.MODID, "honey_fluid_block"), HONEY_FLUID_BLOCK);
        Registry.register(BuiltInRegistries.FLUID, new ResourceLocation(Bumblezone.MODID, "royal_jelly_fluid_still"), ROYAL_JELLY_FLUID);
        Registry.register(BuiltInRegistries.FLUID, new ResourceLocation(Bumblezone.MODID, "royal_jelly_fluid_flowing"), ROYAL_JELLY_FLUID_FLOWING);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Bumblezone.MODID, "royal_jelly_fluid_block"), ROYAL_JELLY_FLUID_BLOCK);
    }
}
