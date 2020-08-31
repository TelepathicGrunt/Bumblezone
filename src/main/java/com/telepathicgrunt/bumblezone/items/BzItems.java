package com.telepathicgrunt.bumblezone.items;

import com.telepathicgrunt.bumblezone.Bumblezone;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import com.telepathicgrunt.bumblezone.blocks.BzBlocks;


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
    public static final Item POROUS_HONEYCOMB = new BlockItem(BzBlocks.POROUS_HONEYCOMB, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB));
    public static final Item FILLED_POROUS_HONEYCOMB = new BlockItem(BzBlocks.FILLED_POROUS_HONEYCOMB, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB));
    public static final Item EMPTY_HONEYCOMB_LARVA = new BlockItem(BzBlocks.EMPTY_HONEYCOMB_BROOD, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB));
    public static final Item HONEYCOMB_LARVA = new BlockItem(BzBlocks.HONEYCOMB_BROOD, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB));
    public static final Item SUGAR_INFUSED_STONE = new BlockItem(BzBlocks.SUGAR_INFUSED_STONE, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB));
    public static final Item SUGAR_INFUSED_COBBLESTONE = new BlockItem(BzBlocks.SUGAR_INFUSED_COBBLESTONE, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB));
    public static final Item HONEY_CRYSTAL = new BlockItem(BzBlocks.HONEY_CRYSTAL, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB));
    public static final Item STICKY_HONEY_RESIDUE = new BlockItem(BzBlocks.STICKY_HONEY_RESIDUE, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB));
    public static final Item STICKY_HONEY_REDSTONE = new BlockItem(BzBlocks.STICKY_HONEY_REDSTONE, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB));
    public static final Item BEESWAX_PLANKS = new BlockItem(BzBlocks.BEESWAX_PLANKS, new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB));


    //items
    public static final Item HONEY_CRYSTAL_SHARDS = new Item(new Item.Properties().group(BUMBLEZONE_CREATIVE_TAB)
            .food((new Food.Builder()).hunger(2).saturation(0.15F).build()));

    public static final Item HONEY_CRYSTAL_SHIELD = new HoneyCrystalShield();

    public static final Item HONEY_SLIME_SPAWN_EGG = new HoneySlimeSpawnEgg(
            null, 16763904,16558080, (new Item.Properties()).group(BUMBLEZONE_CREATIVE_TAB));

    public static final Item SUGAR_WATER_BUCKET = new BucketItem(BzBlocks.SUGAR_WATER_FLUID, new Item.Properties()
            .containerItem(Items.BUCKET).maxStackSize(1).group(BUMBLEZONE_CREATIVE_TAB));

    public static final Item SUGAR_WATER_BOTTLE = new HoneyBottleItem((new Item.Properties())
            .containerItem(Items.GLASS_BOTTLE).food((new Food.Builder()).hunger(1).saturation(0.05F)
            .effect(() -> new EffectInstance(Effects.HASTE, 600, 0), 1.0F).build())
            .group(BUMBLEZONE_CREATIVE_TAB).maxStackSize(16));

    /**
     * registers the item version of the Blocks so they now exist in the registry
     */
    public static void registerItems() {
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "porous_honeycomb_block"), POROUS_HONEYCOMB);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "filled_porous_honeycomb_block"), FILLED_POROUS_HONEYCOMB);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "dead_honeycomb_larva_block"), EMPTY_HONEYCOMB_LARVA);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "honeycomb_larva_block"), HONEYCOMB_LARVA);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "sugar_infused_stone"), SUGAR_INFUSED_STONE);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "sugar_infused_cobblestone"), SUGAR_INFUSED_COBBLESTONE);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "honey_crystal"), HONEY_CRYSTAL);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "sticky_honey_residue"), STICKY_HONEY_RESIDUE);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "sticky_honey_redstone"), STICKY_HONEY_REDSTONE);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "beeswax_planks"), BEESWAX_PLANKS);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "honey_crystal_shards"), HONEY_CRYSTAL_SHARDS);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "honey_crystal_shield"), HONEY_CRYSTAL_SHIELD);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "sugar_water_bucket"), SUGAR_WATER_BUCKET);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "sugar_water_bottle"), SUGAR_WATER_BOTTLE);
        Registry.register(Registry.ITEM, new ResourceLocation(Bumblezone.MODID, "honey_slime_spawn_egg"), HONEY_SLIME_SPAWN_EGG);
    }
}