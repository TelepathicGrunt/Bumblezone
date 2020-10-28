package com.telepathicgrunt.the_bumblezone.fluids;

import java.util.function.Supplier;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.BzBlocks;
import com.telepathicgrunt.the_bumblezone.blocks.SugarWaterBlock;
import com.telepathicgrunt.the_bumblezone.items.BzItems;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BzFluids
{
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Bumblezone.MODID);
	
	//Fluid Textures
    public static final ResourceLocation FLUID_STILL = new ResourceLocation(Bumblezone.MODID+":block/sugar_water_still");
    public static final ResourceLocation FLUID_FLOWING = new ResourceLocation(Bumblezone.MODID+":block/sugar_water_flow");
    public static final ResourceLocation FLUID_OVERLAY = new ResourceLocation(Bumblezone.MODID+":block/sugar_water_overlay");

    //Fluids
    public static final RegistryObject<FlowingFluid> SUGAR_WATER_FLUID = createFluid("sugar_water_still", () -> new SugarWaterFluid.Source(BzFluids.SUGAR_WATER_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> SUGAR_WATER_FLUID_FLOWING = createFluid("sugar_water_flowing", () -> new SugarWaterFluid.Flowing(BzFluids.SUGAR_WATER_FLUID_PROPERTIES));

    //FluidBlocks
    public static final RegistryObject<FlowingFluidBlock> SUGAR_WATER_BLOCK = BzBlocks.createBlock("sugar_water_block", () -> new SugarWaterBlock(SUGAR_WATER_FLUID));

    //Properties
    public static final ForgeFlowingFluid.Properties SUGAR_WATER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(SUGAR_WATER_FLUID, SUGAR_WATER_FLUID_FLOWING,
            FluidAttributes.Water.builder(FLUID_STILL, FLUID_FLOWING).overlay(FLUID_OVERLAY).viscosity(1500))
            .bucket(BzItems.SUGAR_WATER_BUCKET).canMultiply().block(SUGAR_WATER_BLOCK);

    private static <F extends Fluid> RegistryObject<F> createFluid(String name, Supplier<F> fluid)
    {
        return FLUIDS.register(name, fluid);
    }
}
