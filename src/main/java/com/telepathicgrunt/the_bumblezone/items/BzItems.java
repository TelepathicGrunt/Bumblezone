package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.BzBlocks;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;


public class BzItems {

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
            return new ItemStack(BzBlocks.FILLED_POROUS_HONEYCOMB);
        }
    };

    //blocks
    public static Item POROUS_HONEYCOMB = new BlockItem(BzBlocks.POROUS_HONEYCOMB, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB));
    public static Item FILLED_POROUS_HONEYCOMB = new BlockItem(BzBlocks.FILLED_POROUS_HONEYCOMB, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB));
    public static Item EMPTY_HONEYCOMB_LARVA = new BlockItem(BzBlocks.EMPTY_HONEYCOMB_BROOD, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB));
    public static Item HONEYCOMB_LARVA = new BlockItem(BzBlocks.HONEYCOMB_BROOD, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB));
    public static Item SUGAR_INFUSED_STONE = new BlockItem(BzBlocks.SUGAR_INFUSED_STONE, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB));
    public static Item SUGAR_INFUSED_COBBLESTONE = new BlockItem(BzBlocks.SUGAR_INFUSED_COBBLESTONE, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB));
    public static Item HONEY_CRYSTAL = new BlockItem(BzBlocks.HONEY_CRYSTAL, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB));
    public static Item STICKY_HONEY_RESIDUE = new BlockItem(BzBlocks.STICKY_HONEY_RESIDUE, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB));
    public static Item STICKY_HONEY_REDSTONE = new BlockItem(BzBlocks.STICKY_HONEY_REDSTONE, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB));
    public static Item BEESWAX_PLANKS = new BlockItem(BzBlocks.BEESWAX_PLANKS, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB));


    //items
    public static Item HONEY_CRYSTAL_SHARDS = new Item(new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB).food((new Food.Builder()).hunger(2).saturation(0.15F).build()));
    public static Item HONEY_CRYSTAL_SHIELD = new HoneyCrystalShield();
    public static Item SUGAR_WATER_BUCKET = new BucketItem(BzBlocks.SUGAR_WATER_FLUID, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(BUMBLEZONE_CREATIVE_TAB));
    public static Item SUGAR_WATER_BOTTLE = new HoneyBottleItem((new Item.Properties()).containerItem(Items.GLASS_BOTTLE).food((new Food.Builder()).hunger(1).saturation(0.05F).effect(() -> new EffectInstance(Effects.HASTE, 600, 0), 1.0F).build()).group(BUMBLEZONE_CREATIVE_TAB).maxStackSize(16));
    public static Item HONEY_SLIME_SPAWN_EGG = new HoneySlimeSpawnEgg(null, 0xFFCC00,0xFCA800, (new Item.Properties()).group(BUMBLEZONE_CREATIVE_TAB));

    /**
     * registers the item version of the Blocks so they now exist in the registry
     */
    public static void registerItems(final RegistryEvent.Register<Item> event) {

        event.getRegistry().register(POROUS_HONEYCOMB.setRegistryName(new ResourceLocation(Bumblezone.MODID, "porous_honeycomb_block")));
        event.getRegistry().register(FILLED_POROUS_HONEYCOMB.setRegistryName(new ResourceLocation(Bumblezone.MODID, "filled_porous_honeycomb_block")));
        event.getRegistry().register(EMPTY_HONEYCOMB_LARVA.setRegistryName(new ResourceLocation(Bumblezone.MODID, "dead_honeycomb_larva_block")));
        event.getRegistry().register(HONEYCOMB_LARVA.setRegistryName(new ResourceLocation(Bumblezone.MODID, "honeycomb_larva_block")));
        event.getRegistry().register(SUGAR_INFUSED_STONE.setRegistryName(new ResourceLocation(Bumblezone.MODID, "sugar_infused_stone")));
        event.getRegistry().register(SUGAR_INFUSED_COBBLESTONE.setRegistryName(new ResourceLocation(Bumblezone.MODID, "sugar_infused_cobblestone")));
        event.getRegistry().register(HONEY_CRYSTAL.setRegistryName(new ResourceLocation(Bumblezone.MODID, "honey_crystal")));
        event.getRegistry().register(STICKY_HONEY_RESIDUE.setRegistryName(new ResourceLocation(Bumblezone.MODID, "sticky_honey_residue")));
        event.getRegistry().register(STICKY_HONEY_REDSTONE.setRegistryName(new ResourceLocation(Bumblezone.MODID, "sticky_honey_redstone")));
        event.getRegistry().register(BEESWAX_PLANKS.setRegistryName(new ResourceLocation(Bumblezone.MODID, "beeswax_planks")));
        event.getRegistry().register(HONEY_CRYSTAL_SHARDS.setRegistryName(new ResourceLocation(Bumblezone.MODID, "honey_crystal_shards")));
        event.getRegistry().register(HONEY_CRYSTAL_SHIELD.setRegistryName(new ResourceLocation(Bumblezone.MODID, "honey_crystal_shield")));
        event.getRegistry().register(SUGAR_WATER_BUCKET.setRegistryName(new ResourceLocation(Bumblezone.MODID, "sugar_water_bucket")));
        event.getRegistry().register(SUGAR_WATER_BOTTLE.setRegistryName(new ResourceLocation(Bumblezone.MODID, "sugar_water_bottle")));
        event.getRegistry().register(HONEY_SLIME_SPAWN_EGG.setRegistryName(new ResourceLocation(Bumblezone.MODID, "honey_slime_spawn_egg")));

        SpawnEggItem.getEggs();
    }
}