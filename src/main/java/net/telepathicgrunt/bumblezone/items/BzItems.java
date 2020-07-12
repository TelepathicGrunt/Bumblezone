package net.telepathicgrunt.bumblezone.items;

import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Food;
import net.minecraft.item.HoneyBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;


public class BzItems
{
	public static final IRecipeSerializer<ContainerCraftingRecipe> CONTAINER_CRAFTING_RECIPE = new ContainerCraftingRecipe.Serializer();

    public static void registerCustomRecipes(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
        event.getRegistry().register(CONTAINER_CRAFTING_RECIPE.setRegistryName(new ResourceLocation(Bumblezone.MODID, "container_shapeless_recipe_bz")));
    }
	
	/**
	 * creative tab to hold our block items
	 */
	public static final ItemGroup BUMBLEZONE_CREATIVE_TAB = new ItemGroup(ItemGroup.GROUPS.length, Bumblezone.MODID)
	{
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon()
		{
			return new ItemStack(BzBlocks.FILLED_POROUS_HONEYCOMB.get());
		}
	};
    
    @SuppressWarnings("deprecation")
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Bumblezone.MODID);


    //blocks
    public static final RegistryObject<Item> POROUS_HONEYCOMB_ITEM = ITEMS.register("porous_honeycomb_block", () ->
    	new BlockItem(BzBlocks.POROUS_HONEYCOMB.get(), new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));
    
    public static final RegistryObject<Item> FILLED_POROUS_HONEYCOMB_ITEM = ITEMS.register("filled_porous_honeycomb_block", () ->
    	new BlockItem(BzBlocks.FILLED_POROUS_HONEYCOMB.get(), new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));

    public static final RegistryObject<Item> DEAD_HONEYCOMB_LARVA_ITEM = ITEMS.register("dead_honeycomb_larva_block", () ->
    	new BlockItem(BzBlocks.DEAD_HONEYCOMB_BROOD.get(), new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));
    
    public static final RegistryObject<Item> HONEYCOMB_LARVA_ITEM = ITEMS.register("honeycomb_larva_block", () ->
    	new BlockItem(BzBlocks.HONEYCOMB_BROOD.get(), new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));

    public static final RegistryObject<Item> SUGAR_INFUSED_STONE_ITEM = ITEMS.register("sugar_infused_stone", () ->
    	new BlockItem(BzBlocks.SUGAR_INFUSED_STONE.get(), new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));

    public static final RegistryObject<Item> SUGAR_INFUSED_COBBLESTONE_ITEM = ITEMS.register("sugar_infused_cobblestone", () ->
    	new BlockItem(BzBlocks.SUGAR_INFUSED_COBBLESTONE.get(), new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));

    public static final RegistryObject<Item> HONEY_CRYSTAL_BLOCK = ITEMS.register("honey_crystal", () ->
    	new BlockItem(BzBlocks.HONEY_CRYSTAL.get(), new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));
    
    public static final RegistryObject<Item> STICKY_HONEY_RESIDUE = ITEMS.register("sticky_honey_residue", () ->
    	new BlockItem(BzBlocks.STICKY_HONEY_RESIDUE.get(), new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));
    
    public static final RegistryObject<Item> STICKY_HONEY_REDSTONE = ITEMS.register("sticky_honey_redstone", () ->
	new BlockItem(BzBlocks.STICKY_HONEY_REDSTONE.get(), new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));

    
    //items
    public static final RegistryObject<Item> HONEY_CRYSTAL_SHARDS = ITEMS.register("honey_crystal_shards", () ->
	new Item(new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB).food((new Food.Builder()).hunger(2).saturation(0.15F).build())));

    public static final RegistryObject<Item> HONEY_CRYSTAL_SHIELD = ITEMS.register("honey_crystal_shield", () ->
	new HoneyCrystalShield());
    
    public static final RegistryObject<Item> SUGAR_WATER_BUCKET = ITEMS.register("sugar_water_bucket", () ->
    	new BucketItem(BzBlocks.SUGAR_WATER_FLUID, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(BUMBLEZONE_CREATIVE_TAB)));

    @SuppressWarnings("deprecation")
	public static final RegistryObject<Item> SUGAR_WATER_BOTTLE = ITEMS.register("sugar_water_bottle", () ->
    	new HoneyBottleItem((new Item.Properties()).containerItem(Items.GLASS_BOTTLE).food((new Food.Builder()).hunger(1).saturation(0.05F).effect(new EffectInstance(Effects.HASTE, 600, 0), 1.0F).build()).group(BUMBLEZONE_CREATIVE_TAB).maxStackSize(16)));

}