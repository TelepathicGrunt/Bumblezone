package net.telepathicgrunt.bumblezone.items;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;


public class BzItems {
    /**
     * creative tab to hold our block items
     */
    public static final ItemGroup BUMBLEZONE_CREATIVE_TAB = FabricItemGroupBuilder.build(
            new Identifier(Bumblezone.MODID, "main_tab"),
            () -> new ItemStack(BzBlocks.FILLED_POROUS_HONEYCOMB)
    );

    //blocks
    public static final Item POROUS_HONEYCOMB = new BlockItem(BzBlocks.POROUS_HONEYCOMB, new Item.Settings().group(BUMBLEZONE_CREATIVE_TAB));
    public static final Item FILLED_POROUS_HONEYCOMB = new BlockItem(BzBlocks.FILLED_POROUS_HONEYCOMB, new Item.Settings().group(BUMBLEZONE_CREATIVE_TAB));
    public static final Item DEAD_HONEYCOMB_LARVA = new BlockItem(BzBlocks.EMPTY_HONEYCOMB_BROOD, new Item.Settings().group(BUMBLEZONE_CREATIVE_TAB));
    public static final Item HONEYCOMB_LARVA = new BlockItem(BzBlocks.HONEYCOMB_BROOD, new Item.Settings().group(BUMBLEZONE_CREATIVE_TAB));
    public static final Item SUGAR_INFUSED_STONE = new BlockItem(BzBlocks.SUGAR_INFUSED_STONE, new Item.Settings().group(BUMBLEZONE_CREATIVE_TAB));
    public static final Item SUGAR_INFUSED_COBBLESTONE = new BlockItem(BzBlocks.SUGAR_INFUSED_COBBLESTONE, new Item.Settings().group(BUMBLEZONE_CREATIVE_TAB));
    public static final Item HONEY_CRYSTAL = new BlockItem(BzBlocks.HONEY_CRYSTAL, new Item.Settings().group(BUMBLEZONE_CREATIVE_TAB));
    public static final Item STICKY_HONEY_RESIDUE = new BlockItem(BzBlocks.STICKY_HONEY_RESIDUE, new Item.Settings().group(BUMBLEZONE_CREATIVE_TAB));
    public static final Item STICKY_HONEY_REDSTONE = new BlockItem(BzBlocks.STICKY_HONEY_REDSTONE, new Item.Settings().group(BUMBLEZONE_CREATIVE_TAB));


    //items
    public static final Item HONEY_CRYSTAL_SHARDS = new Item(new Item.Settings().group(BUMBLEZONE_CREATIVE_TAB)
            .food((new FoodComponent.Builder()).hunger(2).saturationModifier(0.15F).build()));

    public static final Item HONEY_CRYSTAL_SHIELD = new HoneyCrystalShield();

    public static final Item SUGAR_WATER_BUCKET = new BucketItem(BzBlocks.SUGAR_WATER_FLUID, new Item.Settings()
            .recipeRemainder(Items.BUCKET).maxCount(1).group(BUMBLEZONE_CREATIVE_TAB));

    public static final Item SUGAR_WATER_BOTTLE = new HoneyBottleItem((new Item.Settings()).recipeRemainder(Items.GLASS_BOTTLE)
            .food((new FoodComponent.Builder()).hunger(1).saturationModifier(0.05F)
                    .statusEffect(new StatusEffectInstance(StatusEffects.HASTE, 600, 0), 1.0F).build())
            .group(BUMBLEZONE_CREATIVE_TAB).maxCount(16));

    /**
     * registers the item version of the Blocks so they now exist in the registry
     */
    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier(Bumblezone.MODID, "porous_honeycomb_block"), POROUS_HONEYCOMB);
        Registry.register(Registry.ITEM, new Identifier(Bumblezone.MODID, "filled_porous_honeycomb_block"), FILLED_POROUS_HONEYCOMB);
        Registry.register(Registry.ITEM, new Identifier(Bumblezone.MODID, "dead_honeycomb_larva_block"), DEAD_HONEYCOMB_LARVA);
        Registry.register(Registry.ITEM, new Identifier(Bumblezone.MODID, "honeycomb_larva_block"), HONEYCOMB_LARVA);
        Registry.register(Registry.ITEM, new Identifier(Bumblezone.MODID, "sugar_infused_stone"), SUGAR_INFUSED_STONE);
        Registry.register(Registry.ITEM, new Identifier(Bumblezone.MODID, "sugar_infused_cobblestone"), SUGAR_INFUSED_COBBLESTONE);
        Registry.register(Registry.ITEM, new Identifier(Bumblezone.MODID, "honey_crystal"), HONEY_CRYSTAL);
        Registry.register(Registry.ITEM, new Identifier(Bumblezone.MODID, "sticky_honey_residue"), STICKY_HONEY_RESIDUE);
        Registry.register(Registry.ITEM, new Identifier(Bumblezone.MODID, "sticky_honey_redstone"), STICKY_HONEY_REDSTONE);
        Registry.register(Registry.ITEM, new Identifier(Bumblezone.MODID, "honey_crystal_shards"), HONEY_CRYSTAL_SHARDS);
        Registry.register(Registry.ITEM, new Identifier(Bumblezone.MODID, "honey_crystal_shield"), HONEY_CRYSTAL_SHIELD);
        Registry.register(Registry.ITEM, new Identifier(Bumblezone.MODID, "sugar_water_bucket"), SUGAR_WATER_BUCKET);
        Registry.register(Registry.ITEM, new Identifier(Bumblezone.MODID, "sugar_water_bottle"), SUGAR_WATER_BOTTLE);
    }
}