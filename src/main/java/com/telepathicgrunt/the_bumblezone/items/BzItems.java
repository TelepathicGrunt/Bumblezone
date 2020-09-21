package com.telepathicgrunt.the_bumblezone.items;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.BzBlocks;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
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
    public static Item POROUS_HONEYCOMB;
    public static Item FILLED_POROUS_HONEYCOMB;
    public static Item EMPTY_HONEYCOMB_LARVA;
    public static Item HONEYCOMB_LARVA;
    public static Item SUGAR_INFUSED_STONE;
    public static Item SUGAR_INFUSED_COBBLESTONE;
    public static Item HONEY_CRYSTAL;
    public static Item STICKY_HONEY_RESIDUE;
    public static Item STICKY_HONEY_REDSTONE;
    public static Item BEESWAX_PLANKS;


    //items
    public static Item HONEY_CRYSTAL_SHARDS;
    public static Item HONEY_CRYSTAL_SHIELD;
    public static Item SUGAR_WATER_BUCKET;
    public static Item SUGAR_WATER_BOTTLE;
    public static Item HONEY_SLIME_SPAWN_EGG;

    /**
     * registers the item version of the Blocks so they now exist in the registry
     */
    public static void registerItems() {
        POROUS_HONEYCOMB = Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "porous_honeycomb_block"), new BlockItem(BzBlocks.POROUS_HONEYCOMB, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));
        FILLED_POROUS_HONEYCOMB = Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "filled_porous_honeycomb_block"), new BlockItem(BzBlocks.FILLED_POROUS_HONEYCOMB, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));
        EMPTY_HONEYCOMB_LARVA = Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "dead_honeycomb_larva_block"), new BlockItem(BzBlocks.EMPTY_HONEYCOMB_BROOD, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));
        HONEYCOMB_LARVA = Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "honeycomb_larva_block"), new BlockItem(BzBlocks.HONEYCOMB_BROOD, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));
        SUGAR_INFUSED_STONE = Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "sugar_infused_stone"), new BlockItem(BzBlocks.SUGAR_INFUSED_STONE, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));
        SUGAR_INFUSED_COBBLESTONE = Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "sugar_infused_cobblestone"), new BlockItem(BzBlocks.SUGAR_INFUSED_COBBLESTONE, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));
        HONEY_CRYSTAL = Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "honey_crystal"), new BlockItem(BzBlocks.HONEY_CRYSTAL, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));
        STICKY_HONEY_RESIDUE = Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "sticky_honey_residue"), new BlockItem(BzBlocks.STICKY_HONEY_RESIDUE, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));
        STICKY_HONEY_REDSTONE = Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "sticky_honey_redstone"), new BlockItem(BzBlocks.STICKY_HONEY_REDSTONE, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));
        BEESWAX_PLANKS = Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "beeswax_planks"), new BlockItem(BzBlocks.BEESWAX_PLANKS, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)));

        HONEY_CRYSTAL_SHARDS = Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "honey_crystal_shards"),
                new Item(new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)
                .food((new Food.Builder()).hunger(2).saturation(0.15F).build())));

        HONEY_CRYSTAL_SHIELD = Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "honey_crystal_shield"),
                new HoneyCrystalShield());

        SUGAR_WATER_BUCKET = Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "sugar_water_bucket"),
                new BucketItem(BzBlocks.SUGAR_WATER_FLUID, new Item.Properties()
                .containerItem(Items.BUCKET).maxStackSize(1).group(BUMBLEZONE_CREATIVE_TAB)));

        SUGAR_WATER_BOTTLE = Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "sugar_water_bottle"),
                new HoneyBottleItem((new Item.Properties())
                .containerItem(Items.GLASS_BOTTLE).food((new Food.Builder()).hunger(1).saturation(0.05F)
                        .effect(() -> new EffectInstance(Effects.HASTE, 600, 0), 1.0F).build())
                .group(BUMBLEZONE_CREATIVE_TAB).maxStackSize(16)));

        HONEY_SLIME_SPAWN_EGG = Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "honey_slime_spawn_egg"),
                new HoneySlimeSpawnEgg(null, 0xFFCC00,0xFCA800,
                (new Item.Properties()).group(BUMBLEZONE_CREATIVE_TAB)));

        SpawnEggItem.getEggs();
    }
}