package net.telepathicgrunt.bumblezone.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.telepathicgrunt.bumblezone.Bumblezone;


public class BlocksInit
{
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Bumblezone.MODID);

    public static final RegistryObject<Block> POROUS_HONEYCOMB = BLOCKS.register("porous_honeycomb_block",
            () -> new PorousHoneycombBlock()
    );
    
    
	/**
	 * creative tab to hold our block items
	 */
	public static final ItemGroup BUMBLEZONE_CREATIVE_TAB = new ItemGroup(ItemGroup.GROUPS.length, Bumblezone.MODID)
	{
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon()
		{
			return new ItemStack(POROUS_HONEYCOMB.get());
		}
	};

	
	/**
	 * registers the Blocks so they now exist in the registry
	 * 
	 * @param event - registry to add blocks to
	 */
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(
				new PorousHoneycombBlock());
	}


	/**
	 * registers the item version of the Blocks so they now exist in the registry
	 * 
	 * @param event - registry to add items to
	 */
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(
				new BlockItem(BlocksInit.POROUS_HONEYCOMB.get(), new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)).setRegistryName(BlocksInit.POROUS_HONEYCOMB.get().getRegistryName().getPath()));
	}

}