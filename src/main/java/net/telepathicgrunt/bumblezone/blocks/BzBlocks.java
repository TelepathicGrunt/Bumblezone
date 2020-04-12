package net.telepathicgrunt.bumblezone.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.telepathicgrunt.bumblezone.Bumblezone;


public class BzBlocks
{
	/**
	 * creative tab to hold our block items
	 */
	public static final ItemGroup BUMBLEZONE_CREATIVE_TAB = new ItemGroup(ItemGroup.GROUPS.length, Bumblezone.MODID)
	{
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon()
		{
			return new ItemStack(FILLED_POROUS_HONEYCOMB.get());
		}
	};
    
    
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Bumblezone.MODID);
    public static final DeferredRegister<Fluid> FLUIDS = new DeferredRegister<>(ForgeRegistries.FLUIDS, Bumblezone.MODID);
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Bumblezone.MODID);


    //fluid mess
	
    public static final ResourceLocation FLUID_STILL = new ResourceLocation(Bumblezone.MODID+":block/sugar_water_still");
    public static final ResourceLocation FLUID_FLOWING = new ResourceLocation(Bumblezone.MODID+":block/sugar_water_flow");
    public static final ResourceLocation FLUID_OVERLAY = new ResourceLocation(Bumblezone.MODID+":block/sugar_water_overlay");
    
    public static final RegistryObject<FlowingFluid> SUGAR_WATER_FLUID = FLUIDS.register("sugar_water_fluid", () ->
            new ForgeFlowingFluid.Source(BzBlocks.SUGAR_WATER_FLUID_PROPERTIES)
    );
    public static final RegistryObject<FlowingFluid> SUGAR_WATER_FLUID_FLOWING = FLUIDS.register("sugar_water_flowing", () ->
            new ForgeFlowingFluid.Flowing(BzBlocks.SUGAR_WATER_FLUID_PROPERTIES)
    );
    public static final RegistryObject<FlowingFluidBlock> SUGAR_WATER_BLOCK = BLOCKS.register("sugar_water_block", () ->
	    new SugarWaterBlock(SUGAR_WATER_FLUID, Block.Properties.create(net.minecraft.block.material.Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops())
	);
    
    public static final RegistryObject<Item> SUGAR_WATER_BUCKET = ITEMS.register("sugar_water_bucket", () ->
	    new BucketItem(SUGAR_WATER_FLUID, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(BUMBLEZONE_CREATIVE_TAB))
	);

    public static final ForgeFlowingFluid.Properties SUGAR_WATER_FLUID_PROPERTIES =
            new ForgeFlowingFluid.Properties(SUGAR_WATER_FLUID, SUGAR_WATER_FLUID_FLOWING, FluidAttributes.Water.builder(FLUID_STILL, FLUID_FLOWING).overlay(FLUID_OVERLAY).viscosity(1500))
                    .bucket(SUGAR_WATER_BUCKET).block(SUGAR_WATER_BLOCK);
    
    
    //normal blocks
    
    public static final RegistryObject<Block> POROUS_HONEYCOMB = BLOCKS.register("porous_honeycomb_block",
            () -> new PorousHoneycombBlock());
    public static final RegistryObject<Item> POROUS_HONEYCOMB_ITEM = ITEMS.register("porous_honeycomb_block", () ->
    	new BlockItem(BzBlocks.POROUS_HONEYCOMB.get(), new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));
    
    public static final RegistryObject<Block> FILLED_POROUS_HONEYCOMB = BLOCKS.register("filled_porous_honeycomb_block",
            () -> new FilledPorousHoneycombBlock());
    public static final RegistryObject<Item> FILLED_POROUS_HONEYCOMB_ITEM = ITEMS.register("filled_porous_honeycomb_block", () ->
    	new BlockItem(BzBlocks.FILLED_POROUS_HONEYCOMB.get(), new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));
    
    public static final RegistryObject<Block> SUGAR_INFUSED_STONE = BLOCKS.register("sugar_infused_stone",
            () -> new SugarInfusedStoneBlock());
    public static final RegistryObject<Item> SUGAR_INFUSED_STONE_ITEM = ITEMS.register("sugar_infused_stone", () ->
    	new BlockItem(BzBlocks.SUGAR_INFUSED_STONE.get(), new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));

    public static final RegistryObject<Block> SUGAR_INFUSED_COBBLESTONE = BLOCKS.register("sugar_infused_cobblestone",
            () -> new SugarInfusedCobblestoneBlock());
    public static final RegistryObject<Item> SUGAR_INFUSED_COBBLESTONE_ITEM = ITEMS.register("sugar_infused_cobblestone", () ->
    	new BlockItem(BzBlocks.SUGAR_INFUSED_COBBLESTONE.get(), new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));
    
}