package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.fluids.SugarWaterFluid;
import com.telepathicgrunt.the_bumblezone.items.BzItems;
import com.telepathicgrunt.the_bumblezone.mixin.MaterialInvoker;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;


public class BzBlocks {
    public static Material RESIDUE;

    public static Block POROUS_HONEYCOMB;
    public static Block FILLED_POROUS_HONEYCOMB;
    public static Block EMPTY_HONEYCOMB_BROOD;
    public static Block HONEYCOMB_BROOD;
    public static Block SUGAR_INFUSED_STONE;
    public static Block SUGAR_INFUSED_COBBLESTONE;
    public static Block HONEY_CRYSTAL;
    public static Block STICKY_HONEY_RESIDUE;
    public static Block STICKY_HONEY_REDSTONE;
    public static Block BEESWAX_PLANKS;

    public static FlowingFluid SUGAR_WATER_FLUID;
    public static FlowingFluid SUGAR_WATER_FLUID_FLOWING;
    public static FlowingFluidBlock SUGAR_WATER_BLOCK;
    public static ForgeFlowingFluid.Properties SUGAR_WATER_FLUID_PROPERTIES;

    public static final ResourceLocation FLUID_STILL = new ResourceLocation(Bumblezone.MODID+":block/sugar_water_still");
    public static final ResourceLocation FLUID_FLOWING = new ResourceLocation(Bumblezone.MODID+":block/sugar_water_flow");
    public static final ResourceLocation FLUID_OVERLAY = new ResourceLocation(Bumblezone.MODID+":block/sugar_water_overlay");

    /**
     * registers the Blocks so they now exist in the registry
     */
    public static void registerBlocks() {
        RESIDUE = ((MaterialInvoker)((MaterialInvoker)new Material.Builder(MaterialColor.ADOBE))
                .getNotOpaque())
                .getPushDestroys()
                .doesNotBlockMovement()
                .replaceable()
                .notSolid()
                .build();

        SUGAR_WATER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(() -> SUGAR_WATER_FLUID, () -> SUGAR_WATER_FLUID_FLOWING,
                                                FluidAttributes.Water.builder(FLUID_STILL, FLUID_FLOWING).overlay(FLUID_OVERLAY).viscosity(1500))
                                                .bucket(() -> BzItems.SUGAR_WATER_BUCKET).canMultiply().block(() -> SUGAR_WATER_BLOCK);

        POROUS_HONEYCOMB = Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "porous_honeycomb_block"), new PorousHoneycomb());
        FILLED_POROUS_HONEYCOMB = Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "filled_porous_honeycomb_block"), new FilledPorousHoneycomb());
        EMPTY_HONEYCOMB_BROOD = Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "dead_honeycomb_larva_block"), new EmptyHoneycombBrood());
        HONEYCOMB_BROOD = Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "honeycomb_larva_block"), new HoneycombBrood());
        SUGAR_INFUSED_STONE = Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "sugar_infused_stone"), new SugarInfusedStone());
        SUGAR_INFUSED_COBBLESTONE = Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "sugar_infused_cobblestone"), new SugarInfusedCobblestone());
        HONEY_CRYSTAL = Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "honey_crystal"), new HoneyCrystal());
        STICKY_HONEY_RESIDUE = Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "sticky_honey_residue"), new StickyHoneyResidue());
        STICKY_HONEY_REDSTONE = Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "sticky_honey_redstone"), new StickyHoneyRedstone());
        BEESWAX_PLANKS = Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "beeswax_planks"), new BeeswaxPlanks());
        SUGAR_WATER_FLUID = Registry.register(Registry.FLUID, new ResourceLocation(Bumblezone.MODID, "sugar_water_still"), new SugarWaterFluid.Source(BzBlocks.SUGAR_WATER_FLUID_PROPERTIES));
        SUGAR_WATER_FLUID_FLOWING = Registry.register(Registry.FLUID, new ResourceLocation(Bumblezone.MODID, "sugar_water_flowing"), new SugarWaterFluid.Flowing(BzBlocks.SUGAR_WATER_FLUID_PROPERTIES));
        SUGAR_WATER_BLOCK = Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "sugar_water_block"), new SugarWaterBlock(SUGAR_WATER_FLUID));


    }
}