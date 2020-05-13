package net.telepathicgrunt.bumblezone.blocks;

import java.lang.reflect.Method;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.Material.Builder;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.fluids.SugarWaterFluid;
import net.telepathicgrunt.bumblezone.items.BzItems;


public class BzBlocks
{
    public static Material RESIDUE;
    static {
	Material.Builder builder = (new Material.Builder(MaterialColor.ORANGE_TERRACOTTA)).doesNotBlockMovement().replaceable().notSolid();
	try {
	    Method method = ObfuscationReflectionHelper.findMethod(builder.getClass(), "notOpaque");
	    builder = ((Builder) method.invoke(builder));
	    method = ObfuscationReflectionHelper.findMethod(builder.getClass(), "pushDestroys");
	    RESIDUE = ((Builder) method.invoke(builder)).build();
	} catch (Exception e) {
	    e.printStackTrace();
	} 
    }
    
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Bumblezone.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = new DeferredRegister<>(ForgeRegistries.FLUIDS, Bumblezone.MODID);

    
    //normal blocks
    
    public static final RegistryObject<Block> POROUS_HONEYCOMB = BLOCKS.register("porous_honeycomb_block",
            () -> new PorousHoneycomb());
    
    public static final RegistryObject<Block> FILLED_POROUS_HONEYCOMB = BLOCKS.register("filled_porous_honeycomb_block",
            () -> new FilledPorousHoneycomb());

    public static final RegistryObject<Block> DEAD_HONEYCOMB_LARVA = BLOCKS.register("dead_honeycomb_larva_block",
            () -> new EmptyHoneycombBrood());

    public static final RegistryObject<Block> HONEYCOMB_LARVA = BLOCKS.register("honeycomb_larva_block",
            () -> new HoneycombBrood());

    public static final RegistryObject<Block> SUGAR_INFUSED_STONE = BLOCKS.register("sugar_infused_stone",
            () -> new SugarInfusedStone());

    public static final RegistryObject<Block> SUGAR_INFUSED_COBBLESTONE = BLOCKS.register("sugar_infused_cobblestone",
            () -> new SugarInfusedCobblestone());

    public static final RegistryObject<Block> HONEY_CRYSTAL = BLOCKS.register("honey_crystal",
            () -> new HoneyCrystal());
    
    public static final RegistryObject<Block> STICKY_HONEY_RESIDUE = BLOCKS.register("sticky_honey_residue",
            () -> new StickyHoneyResidue());
    
    public static final RegistryObject<Block> STICKY_HONEY_REDSTONE = BLOCKS.register("sticky_honey_redstone",
            () -> new StickyHoneyRedstone());
    
    //fluid mess
	
    public static final ResourceLocation FLUID_STILL = new ResourceLocation(Bumblezone.MODID+":block/sugar_water_still");
    public static final ResourceLocation FLUID_FLOWING = new ResourceLocation(Bumblezone.MODID+":block/sugar_water_flow");
    public static final ResourceLocation FLUID_OVERLAY = new ResourceLocation(Bumblezone.MODID+":block/sugar_water_overlay");
    
    public static final RegistryObject<FlowingFluid> SUGAR_WATER_FLUID = FLUIDS.register("sugar_water_fluid", () ->
            new SugarWaterFluid.Source(BzBlocks.SUGAR_WATER_FLUID_PROPERTIES)
    );
    public static final RegistryObject<FlowingFluid> SUGAR_WATER_FLUID_FLOWING = FLUIDS.register("sugar_water_flowing", () ->
            new SugarWaterFluid.Flowing(BzBlocks.SUGAR_WATER_FLUID_PROPERTIES)
    );
    public static final RegistryObject<FlowingFluidBlock> SUGAR_WATER_BLOCK = BLOCKS.register("sugar_water_block", () ->
	    new SugarWaterBlock(SUGAR_WATER_FLUID)
    );
    
    public static final ForgeFlowingFluid.Properties SUGAR_WATER_FLUID_PROPERTIES =
            new ForgeFlowingFluid.Properties(SUGAR_WATER_FLUID, SUGAR_WATER_FLUID_FLOWING, FluidAttributes.Water.builder(FLUID_STILL, FLUID_FLOWING).overlay(FLUID_OVERLAY).viscosity(1500))
                    .bucket(BzItems.SUGAR_WATER_BUCKET).canMultiply().block(SUGAR_WATER_BLOCK);
    
}