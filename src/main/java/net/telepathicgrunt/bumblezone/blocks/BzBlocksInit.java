package net.telepathicgrunt.bumblezone.blocks;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.telepathicgrunt.bumblezone.Bumblezone;


public class BzBlocksInit
{
    public static final Block POROUS_HONEYCOMB = new PorousHoneycombBlock();
    public static final Block FILLED_POROUS_HONEYCOMB = new FilledPorousHoneycombBlock();
    public static final ItemGroup BZ_ITEM_GROUP = FabricItemGroupBuilder.build(new Identifier(Bumblezone.MODID), () -> new ItemStack(FILLED_POROUS_HONEYCOMB.asItem()));
    
//    
//	/**
//	 * creative tab to hold our block items
//	 */
//	public static final ItemGroup BUMBLEZONE_CREATIVE_TAB = new ItemGroup(ItemGroup.GROUPS.length, Bumblezone.MODID)
//	{
//		public ItemStack createIcon()
//		{
//			return new ItemStack(FILLED_POROUS_HONEYCOMB);
//		}
//	};

	
	/**
	 * registers the Blocks so they now exist in the registry
	 * 
	 * @param event - registry to add blocks to
	 */
	public static void registerBlocks()
	{
		Registry.register(Registry.BLOCK, new Identifier(Bumblezone.MODID, "porous_honeycomb_block"), POROUS_HONEYCOMB);
		Registry.register(Registry.BLOCK, new Identifier(Bumblezone.MODID, "filled_porous_honeycomb_block"), FILLED_POROUS_HONEYCOMB);
	}


	/**
	 * registers the item version of the Blocks so they now exist in the registry
	 * 
	 * @param event - registry to add items to
	 */
	public static void registerItems()
	{
        Registry.register(Registry.ITEM, new Identifier(Bumblezone.MODID, "porous_honeycomb_block"), new BlockItem(POROUS_HONEYCOMB, new Item.Settings().group(BZ_ITEM_GROUP)));
        Registry.register(Registry.ITEM, new Identifier(Bumblezone.MODID, "filled_porous_honeycomb_block"), new BlockItem(FILLED_POROUS_HONEYCOMB, new Item.Settings().group(BZ_ITEM_GROUP)));
	}

}