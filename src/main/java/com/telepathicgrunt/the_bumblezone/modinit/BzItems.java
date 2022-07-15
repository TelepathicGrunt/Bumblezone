package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.items.*;
import com.telepathicgrunt.the_bumblezone.items.materials.BeeArmorMaterial;
import com.telepathicgrunt.the_bumblezone.items.recipes.ContainerCraftingRecipe;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class BzItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Bumblezone.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Bumblezone.MODID);

    /**
     * creative tab to hold our block items
     */
    public static final CreativeModeTab BUMBLEZONE_CREATIVE_TAB = new CreativeModeTab(CreativeModeTab.TABS.length, Bumblezone.MODID) {
        @Override
        // CLIENT-SIDED
        public ItemStack makeIcon() {
            return new ItemStack(BzBlocks.FILLED_POROUS_HONEYCOMB.get());
        }
    };

    //Recipe
    public static final RegistryObject<RecipeSerializer<ContainerCraftingRecipe>> CONTAINER_CRAFTING_RECIPE = RECIPES.register("container_shapeless_recipe_bz", ContainerCraftingRecipe.Serializer::new);

    //blocks
    public static final RegistryObject<Item> POROUS_HONEYCOMB = ITEMS.register("porous_honeycomb_block", () -> new BlockItem(BzBlocks.POROUS_HONEYCOMB.get(), new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> FILLED_POROUS_HONEYCOMB = ITEMS.register("filled_porous_honeycomb_block", () -> new BlockItem(BzBlocks.FILLED_POROUS_HONEYCOMB.get(), new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> EMPTY_HONEYCOMB_BROOD = ITEMS.register("empty_honeycomb_brood_block", () -> new BlockItem(BzBlocks.EMPTY_HONEYCOMB_BROOD.get(), new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> HONEYCOMB_BROOD = ITEMS.register("honeycomb_brood_block", () -> new BlockItem(BzBlocks.HONEYCOMB_BROOD.get(), new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> SUGAR_INFUSED_STONE = ITEMS.register("sugar_infused_stone", () -> new BlockItem(BzBlocks.SUGAR_INFUSED_STONE.get(), new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> SUGAR_INFUSED_COBBLESTONE = ITEMS.register("sugar_infused_cobblestone", () -> new BlockItem(BzBlocks.SUGAR_INFUSED_COBBLESTONE.get(), new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> BEEHIVE_BEESWAX = ITEMS.register("beehive_beeswax", () -> new BlockItem(BzBlocks.BEEHIVE_BEESWAX.get(), new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> STICKY_HONEY_RESIDUE = ITEMS.register("sticky_honey_residue", () -> new BlockItem(BzBlocks.STICKY_HONEY_RESIDUE.get(), new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> STICKY_HONEY_REDSTONE = ITEMS.register("sticky_honey_redstone", () -> new BlockItem(BzBlocks.STICKY_HONEY_REDSTONE.get(), new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> HONEY_WEB = ITEMS.register("honey_web", () -> new BlockItem(BzBlocks.HONEY_WEB.get(), new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> REDSTONE_HONEY_WEB = ITEMS.register("redstone_honey_web", () -> new BlockItem(BzBlocks.REDSTONE_HONEY_WEB.get(), new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> PILE_OF_POLLEN = ITEMS.register("pile_of_pollen", () -> new BlockItem(BzBlocks.PILE_OF_POLLEN.get(), new Item.Properties())); // Not obtainable by default. Purely for advancement icon.
    public static final RegistryObject<Item> HONEY_CRYSTAL = ITEMS.register("honey_crystal", () -> new BzHoneyCrystalBlockItem(BzBlocks.HONEY_CRYSTAL.get(), new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> HONEY_COCOON = ITEMS.register("honey_cocoon", () -> new BzBlockItem(BzBlocks.HONEY_COCOON.get(), new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB), false));
    public static final RegistryObject<Item> ROYAL_JELLY_BLOCK = ITEMS.register("royal_jelly_block", () -> new BlockItem(BzBlocks.ROYAL_JELLY_BLOCK.get(), new Item.Properties().rarity(Rarity.EPIC).tab(BUMBLEZONE_CREATIVE_TAB)));


    //items
    public static final RegistryObject<ArrowItem> HONEY_CRYSTAL_SHARDS = ITEMS.register("honey_crystal_shards", () -> new HoneyCrystalShards(new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB).food((new FoodProperties.Builder()).nutrition(2).saturationMod(0.15F).build())));
    public static final RegistryObject<Item> BEE_STINGER = ITEMS.register("bee_stinger", () -> new BeeStinger(new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> HONEY_BUCKET = ITEMS.register("honey_bucket", () -> new BucketItem(BzFluids.HONEY_FLUID, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> SUGAR_WATER_BUCKET = ITEMS.register("sugar_water_bucket", () -> new BzBucketItem(BzFluids.SUGAR_WATER_FLUID, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> SUGAR_WATER_BOTTLE = ITEMS.register("sugar_water_bottle", () -> new SugarWaterBottle((new Item.Properties()).craftRemainder(Items.GLASS_BOTTLE).food((new FoodProperties.Builder()).nutrition(1).saturationMod(0.05F).effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 600, 0), 1.0F).build()).tab(BUMBLEZONE_CREATIVE_TAB).stacksTo(16)));
    public static final RegistryObject<Item> ROYAL_JELLY_BUCKET = ITEMS.register("royal_jelly_bucket", () -> new BzBucketItem(BzFluids.ROYAL_JELLY_FLUID, new Item.Properties().rarity(Rarity.EPIC).craftRemainder(Items.BUCKET).stacksTo(1).tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> ROYAL_JELLY_BOTTLE = ITEMS.register("royal_jelly_bottle", () -> new RoyalJellyBottle((new Item.Properties().rarity(Rarity.EPIC)).craftRemainder(Items.GLASS_BOTTLE).food((new FoodProperties.Builder()).nutrition(10).saturationMod(0.2F).effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 0), 1.0F).effect(() -> new MobEffectInstance(BzEffects.BEENERGIZED.get(), 1200, 1), 1.0F).build()).tab(BUMBLEZONE_CREATIVE_TAB).stacksTo(16)));
    public static final RegistryObject<Item> HONEY_SLIME_SPAWN_EGG = ITEMS.register("honey_slime_spawn_egg", () -> new DispenserAddedSpawnEgg(BzEntities.HONEY_SLIME, 0xFFCC00, 0xFCA800, (new Item.Properties()).tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> BEEHEMOTH_SPAWN_EGG = ITEMS.register("beehemoth_spawn_egg", () -> new DispenserAddedSpawnEgg(BzEntities.BEEHEMOTH, 0xEDC343, 0x43241B, (new Item.Properties()).tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> BEE_QUEEN_SPAWN_EGG = ITEMS.register("bee_queen_spawn_egg", () -> new DispenserAddedSpawnEgg(BzEntities.BEE_QUEEN, 0xFFFFFF, 0xFFFFFF, (new Item.Properties().rarity(Rarity.EPIC)).tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV = ITEMS.register("music_disc_flight_of_the_bumblebee_rimsky_korsakov", () -> new RecordItem(14, BzSounds.MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV, (new Item.Properties()).stacksTo(1).tab(BUMBLEZONE_CREATIVE_TAB).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY = ITEMS.register("music_disc_honey_bee_rat_faced_boy", () -> new RecordItem(15, BzSounds.MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY, (new Item.Properties()).stacksTo(1).tab(BUMBLEZONE_CREATIVE_TAB).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> POLLEN_PUFF = ITEMS.register("pollen_puff", () -> new PollenPuff(new Item.Properties().tab(BUMBLEZONE_CREATIVE_TAB).stacksTo(16)));
    public static final RegistryObject<Item> BEE_BREAD = ITEMS.register("bee_bread", () -> new BeeBread(new Item.Properties().food((new FoodProperties.Builder()).nutrition(8).saturationMod(0.12F).alwaysEat().effect(() -> new MobEffectInstance(BzEffects.BEENERGIZED.get(), 6000, 0), 1.0F).effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 120, 1), 1.0F).build()).tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> HONEY_CRYSTAL_SHIELD = ITEMS.register("honey_crystal_shield", () -> new HoneyCrystalShield(new Item.Properties().rarity(Rarity.UNCOMMON).tab(BzItems.BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> STINGER_SPEAR = ITEMS.register("stinger_spear", () -> new StingerSpearItem(new Item.Properties().rarity(Rarity.UNCOMMON).tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> HONEY_COMPASS = ITEMS.register("honey_compass", () -> new HoneyCompass(new Item.Properties().rarity(Rarity.UNCOMMON).tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> BEE_CANNON = ITEMS.register("bee_cannon", () -> new BeeCannon(new Item.Properties().stacksTo(1).tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> CRYSTAL_CANNON = ITEMS.register("crystal_cannon", () -> new CrystalCannon(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1).tab(BUMBLEZONE_CREATIVE_TAB)));
    public static final RegistryObject<Item> HONEY_BEE_LEGGINGS_1 = ITEMS.register("honey_bee_leggings_1", () -> new HoneyBeeLeggings(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.LEGS, new Item.Properties().rarity(Rarity.UNCOMMON).tab(BzItems.BUMBLEZONE_CREATIVE_TAB), 1));
    public static final RegistryObject<Item> HONEY_BEE_LEGGINGS_2 = ITEMS.register("honey_bee_leggings_2", () -> new HoneyBeeLeggings(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.LEGS, new Item.Properties().rarity(Rarity.UNCOMMON).tab(BzItems.BUMBLEZONE_CREATIVE_TAB), 2));
    public static final RegistryObject<Item> BUMBLE_BEE_CHESTPLATE_1 = ITEMS.register("bumble_bee_chestplate_1", () -> new BumbleBeeChestplate(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.CHEST, new Item.Properties().rarity(Rarity.UNCOMMON).tab(BzItems.BUMBLEZONE_CREATIVE_TAB), false, 1));
    public static final RegistryObject<Item> BUMBLE_BEE_CHESTPLATE_2 = ITEMS.register("bumble_bee_chestplate_2", () -> new BumbleBeeChestplate(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.CHEST, new Item.Properties().rarity(Rarity.UNCOMMON).tab(BzItems.BUMBLEZONE_CREATIVE_TAB), false, 2));
    public static final RegistryObject<Item> TRANS_BUMBLE_BEE_CHESTPLATE_1 = ITEMS.register("bumble_bee_chestplate_trans_1", () -> new BumbleBeeChestplate(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.CHEST, new Item.Properties().rarity(Rarity.UNCOMMON).tab(BzItems.BUMBLEZONE_CREATIVE_TAB), true, 1));
    public static final RegistryObject<Item> TRANS_BUMBLE_BEE_CHESTPLATE_2 = ITEMS.register("bumble_bee_chestplate_trans_2", () -> new BumbleBeeChestplate(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.CHEST, new Item.Properties().rarity(Rarity.UNCOMMON).tab(BzItems.BUMBLEZONE_CREATIVE_TAB), true, 2));
    public static final RegistryObject<Item> STINGLESS_BEE_HELMET_1 = ITEMS.register("stingless_bee_helmet_1", () -> new StinglessBeeHelmet(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.HEAD, new Item.Properties().rarity(Rarity.UNCOMMON).tab(BzItems.BUMBLEZONE_CREATIVE_TAB), 1));
    public static final RegistryObject<Item> STINGLESS_BEE_HELMET_2 = ITEMS.register("stingless_bee_helmet_2", () -> new StinglessBeeHelmet(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.HEAD, new Item.Properties().rarity(Rarity.UNCOMMON).tab(BzItems.BUMBLEZONE_CREATIVE_TAB), 2));
    public static final RegistryObject<Item> CARPENTER_BEE_BOOTS_1 = ITEMS.register("carpenter_bee_boots_1", () -> new CarpenterBeeBoots(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.FEET, new Item.Properties().rarity(Rarity.UNCOMMON).tab(BzItems.BUMBLEZONE_CREATIVE_TAB), 1));
    public static final RegistryObject<Item> CARPENTER_BEE_BOOTS_2 = ITEMS.register("carpenter_bee_boots_2", () -> new CarpenterBeeBoots(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.FEET, new Item.Properties().rarity(Rarity.UNCOMMON).tab(BzItems.BUMBLEZONE_CREATIVE_TAB), 2));
    public static final RegistryObject<Item> ESSENCE_OF_THE_BEES = ITEMS.register("essence_of_the_bees", () -> new EssenceOfTheBees(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).tab(BUMBLEZONE_CREATIVE_TAB)));
}