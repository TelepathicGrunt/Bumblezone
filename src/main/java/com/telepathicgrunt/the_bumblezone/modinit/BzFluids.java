package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.blocks.SugarWaterBlock;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluid;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluidType;
import com.telepathicgrunt.the_bumblezone.fluids.SugarWaterFluid;
import com.telepathicgrunt.the_bumblezone.fluids.SugarWaterFluidType;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BzFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Bumblezone.MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, Bumblezone.MODID);

    //Fluids
    public static final RegistryObject<FlowingFluid> SUGAR_WATER_FLUID = FLUIDS.register("sugar_water_still", () -> new SugarWaterFluid.Source(BzFluids.SUGAR_WATER_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> SUGAR_WATER_FLUID_FLOWING = FLUIDS.register("sugar_water_flowing", () -> new SugarWaterFluid.Flowing(BzFluids.SUGAR_WATER_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> HONEY_FLUID = FLUIDS.register("honey_fluid_still", () -> new HoneyFluid.Source(BzFluids.HONEY_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> HONEY_FLUID_FLOWING = FLUIDS.register("honey_fluid_flowing", () -> new HoneyFluid.Flowing(BzFluids.HONEY_FLUID_PROPERTIES));

    //FluidBlocks
    public static final RegistryObject<LiquidBlock> SUGAR_WATER_BLOCK = BzBlocks.BLOCKS.register("sugar_water_block", () -> new SugarWaterBlock(SUGAR_WATER_FLUID));
    public static final RegistryObject<LiquidBlock> HONEY_FLUID_BLOCK = BzBlocks.BLOCKS.register("honey_fluid_block", () -> new HoneyFluidBlock(HONEY_FLUID));

    //FluidTypes
    public static final RegistryObject<FluidType> HONEY_FLUID_TYPE = FLUID_TYPES.register("honey_fluid", HoneyFluidType::new);
    public static final RegistryObject<FluidType> SUGAR_WATER_FLUID_TYPE = FLUID_TYPES.register("sugar_water", SugarWaterFluidType::new);

    //Properties
    public static final ForgeFlowingFluid.Properties SUGAR_WATER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(SUGAR_WATER_FLUID_TYPE, SUGAR_WATER_FLUID, SUGAR_WATER_FLUID_FLOWING)
            .bucket(BzItems.SUGAR_WATER_BUCKET).block(SUGAR_WATER_BLOCK);

    public static final ForgeFlowingFluid.Properties HONEY_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(HONEY_FLUID_TYPE, HONEY_FLUID, HONEY_FLUID_FLOWING)
            .bucket(BzItems.HONEY_BUCKET).block(HONEY_FLUID_BLOCK).tickRate(30);
}
