package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyFluidBlock;
import com.telepathicgrunt.the_bumblezone.blocks.SugarWaterBlock;
import com.telepathicgrunt.the_bumblezone.fluids.HoneyFluid;
import com.telepathicgrunt.the_bumblezone.fluids.SugarWaterFluid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BzFluids {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Bumblezone.MODID);

    //Fluid Textures
    public static final ResourceLocation FLUID_STILL_TEXTURE = new ResourceLocation(Bumblezone.MODID + ":block/sugar_water_still");
    public static final ResourceLocation FLUID_FLOWING_TEXTURE = new ResourceLocation(Bumblezone.MODID + ":block/sugar_water_flow");
    public static final ResourceLocation FLUID_OVERLAY_TEXTURE = new ResourceLocation(Bumblezone.MODID + ":block/sugar_water_overlay");
    public static final ResourceLocation HONEY_FLUID_STILL_TEXTURE = new ResourceLocation(Bumblezone.MODID + ":block/honey_fluid_still");
    public static final ResourceLocation HONEY_FLUID_FLOWING_TEXTURE = new ResourceLocation(Bumblezone.MODID + ":block/honey_fluid_flow");
    public static final ResourceLocation HONEY_FLUID_OVERLAY_TEXTURE = new ResourceLocation(Bumblezone.MODID + ":block/honey_fluid_overlay");

    //Fluids
    public static final RegistryObject<FlowingFluid> SUGAR_WATER_FLUID = FLUIDS.register("sugar_water_still", () -> new SugarWaterFluid.Source(BzFluids.SUGAR_WATER_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> SUGAR_WATER_FLUID_FLOWING = FLUIDS.register("sugar_water_flowing", () -> new SugarWaterFluid.Flowing(BzFluids.SUGAR_WATER_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> HONEY_FLUID = FLUIDS.register("honey_fluid_still", () -> new HoneyFluid.Source(BzFluids.HONEY_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> HONEY_FLUID_FLOWING = FLUIDS.register("honey_fluid_flowing", () -> new HoneyFluid.Flowing(BzFluids.HONEY_FLUID_PROPERTIES));

    //FluidBlocks
    public static final RegistryObject<LiquidBlock> SUGAR_WATER_BLOCK = BzBlocks.BLOCKS.register("sugar_water_block", () -> new SugarWaterBlock(SUGAR_WATER_FLUID));
    public static final RegistryObject<LiquidBlock> HONEY_FLUID_BLOCK = BzBlocks.BLOCKS.register("honey_fluid_block", () -> new HoneyFluidBlock(HONEY_FLUID));

    //Properties
    public static final ForgeFlowingFluid.Properties SUGAR_WATER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(SUGAR_WATER_FLUID, SUGAR_WATER_FLUID_FLOWING,
            FluidAttributes.Water.builder(FLUID_STILL_TEXTURE, FLUID_FLOWING_TEXTURE).overlay(FLUID_OVERLAY_TEXTURE).viscosity(1500))
            .bucket(BzItems.SUGAR_WATER_BUCKET).canMultiply().block(SUGAR_WATER_BLOCK);

    public static final ForgeFlowingFluid.Properties HONEY_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(HONEY_FLUID, HONEY_FLUID_FLOWING,
            FluidAttributes.builder(HONEY_FLUID_STILL_TEXTURE, HONEY_FLUID_FLOWING_TEXTURE).overlay(HONEY_FLUID_OVERLAY_TEXTURE).viscosity(5000))
            .bucket(BzItems.HONEY_BUCKET).block(HONEY_FLUID_BLOCK).tickRate(30);
}
