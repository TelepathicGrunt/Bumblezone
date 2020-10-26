package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.fluids.SugarWaterFluid;
import com.telepathicgrunt.the_bumblezone.generation.BzBiomeProvider;
import com.telepathicgrunt.the_bumblezone.items.BzItems;
import com.telepathicgrunt.the_bumblezone.mixin.MaterialInvoker;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.BiomeMaker;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;


public class BzBlocks {
    public static Material RESIDUE;

    public static Block POROUS_HONEYCOMB = new PorousHoneycomb();
    public static Block FILLED_POROUS_HONEYCOMB = new FilledPorousHoneycomb();
    public static Block EMPTY_HONEYCOMB_BROOD = new EmptyHoneycombBrood();
    public static Block HONEYCOMB_BROOD = new HoneycombBrood();
    public static Block SUGAR_INFUSED_STONE = new SugarInfusedStone();
    public static Block SUGAR_INFUSED_COBBLESTONE = new SugarInfusedCobblestone();
    public static Block HONEY_CRYSTAL = new HoneyCrystal();
    public static Block STICKY_HONEY_RESIDUE = new StickyHoneyResidue();
    public static Block STICKY_HONEY_REDSTONE = new StickyHoneyRedstone();
    public static Block BEESWAX_PLANKS = new BeeswaxPlanks();

    public static FlowingFluid SUGAR_WATER_FLUID = new SugarWaterFluid.Source(BzBlocks.SUGAR_WATER_FLUID_PROPERTIES);
    public static FlowingFluid SUGAR_WATER_FLUID_FLOWING = new SugarWaterFluid.Flowing(BzBlocks.SUGAR_WATER_FLUID_PROPERTIES);
    public static FlowingFluidBlock SUGAR_WATER_BLOCK = new SugarWaterBlock(SUGAR_WATER_FLUID);

    public static final ResourceLocation FLUID_STILL = new ResourceLocation(Bumblezone.MODID+":block/sugar_water_still");
    public static final ResourceLocation FLUID_FLOWING = new ResourceLocation(Bumblezone.MODID+":block/sugar_water_flow");
    public static final ResourceLocation FLUID_OVERLAY = new ResourceLocation(Bumblezone.MODID+":block/sugar_water_overlay");

    public static ForgeFlowingFluid.Properties SUGAR_WATER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(() -> SUGAR_WATER_FLUID, () -> SUGAR_WATER_FLUID_FLOWING,
            FluidAttributes.Water.builder(FLUID_STILL, FLUID_FLOWING).overlay(FLUID_OVERLAY).viscosity(1500))
            .bucket(() -> BzItems.SUGAR_WATER_BUCKET).canMultiply().block(() -> SUGAR_WATER_BLOCK);

    /**
     * registers the Blocks so they now exist in the registry
     */
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        RESIDUE = ((MaterialInvoker)((MaterialInvoker)new Material.Builder(MaterialColor.ADOBE))
                .getNotOpaque())
                .getPushDestroys()
                .doesNotBlockMovement()
                .replaceable()
                .notSolid()
                .build();

        event.getRegistry().register(POROUS_HONEYCOMB.setRegistryName(new ResourceLocation(Bumblezone.MODID, "porous_honeycomb_block")));
        event.getRegistry().register(FILLED_POROUS_HONEYCOMB.setRegistryName(new ResourceLocation(Bumblezone.MODID, "filled_porous_honeycomb_block")));
        event.getRegistry().register(EMPTY_HONEYCOMB_BROOD.setRegistryName(new ResourceLocation(Bumblezone.MODID, "dead_honeycomb_larva_block")));
        event.getRegistry().register(HONEYCOMB_BROOD.setRegistryName(new ResourceLocation(Bumblezone.MODID, "honeycomb_larva_block")));
        event.getRegistry().register(SUGAR_INFUSED_STONE.setRegistryName(new ResourceLocation(Bumblezone.MODID, "sugar_infused_stone")));
        event.getRegistry().register(SUGAR_INFUSED_COBBLESTONE.setRegistryName(new ResourceLocation(Bumblezone.MODID, "sugar_infused_cobblestone")));
        event.getRegistry().register(HONEY_CRYSTAL.setRegistryName(new ResourceLocation(Bumblezone.MODID, "honey_crystal")));
        event.getRegistry().register(STICKY_HONEY_RESIDUE.setRegistryName(new ResourceLocation(Bumblezone.MODID, "sticky_honey_residue")));
        event.getRegistry().register(BEESWAX_PLANKS.setRegistryName(new ResourceLocation(Bumblezone.MODID, "beeswax_planks")));

        event.getRegistry().register(SUGAR_WATER_BLOCK.setRegistryName(new ResourceLocation(Bumblezone.MODID, "sugar_water_block")));

    }

    public static void registerFluids(final RegistryEvent.Register<Fluid> event) {
        event.getRegistry().register(SUGAR_WATER_FLUID.setRegistryName(new ResourceLocation(Bumblezone.MODID, "sugar_water_still")));
        event.getRegistry().register(SUGAR_WATER_FLUID_FLOWING.setRegistryName(new ResourceLocation(Bumblezone.MODID, "sugar_water_flowing")));
    }
}