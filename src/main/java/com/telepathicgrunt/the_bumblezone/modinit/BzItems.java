package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.CarvableWax;
import com.telepathicgrunt.the_bumblezone.configs.BzConfig;
import com.telepathicgrunt.the_bumblezone.items.*;
import com.telepathicgrunt.the_bumblezone.items.materials.BeeArmorMaterial;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BzItems {
    /**
     * creative tab to hold our items
     */
    public static CreativeModeTab BUMBLEZONE_CREATIVE_TAB;

    //blocks
    public static final Item POROUS_HONEYCOMB = new BlockItem(BzBlocks.POROUS_HONEYCOMB, new Item.Properties());
    public static final Item FILLED_POROUS_HONEYCOMB = new BlockItem(BzBlocks.FILLED_POROUS_HONEYCOMB, new Item.Properties());
    public static final Item EMPTY_HONEYCOMB_BROOD = new BlockItem(BzBlocks.EMPTY_HONEYCOMB_BROOD, new Item.Properties());
    public static final Item HONEYCOMB_BROOD = new BlockItem(BzBlocks.HONEYCOMB_BROOD, new Item.Properties());
    public static final Item SUGAR_INFUSED_STONE = new BlockItem(BzBlocks.SUGAR_INFUSED_STONE, new Item.Properties());
    public static final Item SUGAR_INFUSED_COBBLESTONE = new BlockItem(BzBlocks.SUGAR_INFUSED_COBBLESTONE, new Item.Properties());
    public static final Item BEEHIVE_BEESWAX = new BlockItem(BzBlocks.BEEHIVE_BEESWAX, new Item.Properties());
    public static final Item STICKY_HONEY_RESIDUE = new BlockItem(BzBlocks.STICKY_HONEY_RESIDUE, new Item.Properties());
    public static final Item STICKY_HONEY_REDSTONE = new BlockItem(BzBlocks.STICKY_HONEY_REDSTONE, new Item.Properties());
    public static final Item HONEY_WEB = new BlockItem(BzBlocks.HONEY_WEB, new Item.Properties());
    public static final Item REDSTONE_HONEY_WEB = new BlockItem(BzBlocks.REDSTONE_HONEY_WEB, new Item.Properties());
    public static final Item PILE_OF_POLLEN = new BlockItem(BzBlocks.PILE_OF_POLLEN, new Item.Properties());
    public static final Item HONEY_CRYSTAL = new BzHoneyCrystalBlockItem(BzBlocks.HONEY_CRYSTAL, new Item.Properties());
    public static final Item HONEY_COCOON = new BzBlockItem(BzBlocks.HONEY_COCOON, new Item.Properties(), false, true);
    public static final Item ROYAL_JELLY_BLOCK = new BlockItem(BzBlocks.ROYAL_JELLY_BLOCK, new Item.Properties().rarity(Rarity.EPIC));
    public static final Item GLISTERING_HONEY_CRYSTAL = new BzHoneyCrystalBlockItem(BzBlocks.GLISTERING_HONEY_CRYSTAL, new Item.Properties());
    public static final Item CARVABLE_WAX = new BzBlockItem(BzBlocks.CARVABLE_WAX.defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.UNCARVED), new Item.Properties());
    public static final Item CARVABLE_WAX_WAVY = new BzBlockItem(BzBlocks.CARVABLE_WAX.defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.WAVY), new Item.Properties());
    public static final Item CARVABLE_WAX_FLOWER = new BzBlockItem(BzBlocks.CARVABLE_WAX.defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.FLOWER), new Item.Properties());
    public static final Item CARVABLE_WAX_CHISELED = new BzBlockItem(BzBlocks.CARVABLE_WAX.defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.CHISELED), new Item.Properties());
    public static final Item CARVABLE_WAX_DIAMOND = new BzBlockItem(BzBlocks.CARVABLE_WAX.defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.DIAMOND), new Item.Properties());
    public static final Item CARVABLE_WAX_BRICKS = new BzBlockItem(BzBlocks.CARVABLE_WAX.defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.BRICKS), new Item.Properties());
    public static final Item CARVABLE_WAX_CHAINS = new BzBlockItem(BzBlocks.CARVABLE_WAX.defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.CHAINS), new Item.Properties());
    public static final Item SUPER_CANDLE = new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE, new Item.Properties(), true, false);
    public static final Item SUPER_CANDLE_BLACK = new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_BLACK, new Item.Properties(), true, false);
    public static final Item SUPER_CANDLE_BLUE = new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_BLUE, new Item.Properties(), true, false);
    public static final Item SUPER_CANDLE_BROWN = new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_BROWN, new Item.Properties(), true, false);
    public static final Item SUPER_CANDLE_CYAN = new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_CYAN, new Item.Properties(), true, false);
    public static final Item SUPER_CANDLE_GRAY = new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_GRAY, new Item.Properties(), true, false);
    public static final Item SUPER_CANDLE_GREEN = new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_GREEN, new Item.Properties(), true, false);
    public static final Item SUPER_CANDLE_LIGHT_BLUE = new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_LIGHT_BLUE, new Item.Properties(), true, false);
    public static final Item SUPER_CANDLE_LIGHT_GRAY = new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_LIGHT_GRAY, new Item.Properties(), true, false);
    public static final Item SUPER_CANDLE_LIME = new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_LIME, new Item.Properties(), true, false);
    public static final Item SUPER_CANDLE_MAGENTA = new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_MAGENTA, new Item.Properties(), true, false);
    public static final Item SUPER_CANDLE_ORANGE = new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_ORANGE, new Item.Properties(), true, false);
    public static final Item SUPER_CANDLE_PINK = new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_PINK, new Item.Properties(), true, false);
    public static final Item SUPER_CANDLE_PURPLE = new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_PURPLE, new Item.Properties(), true, false);
    public static final Item SUPER_CANDLE_RED = new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_RED, new Item.Properties(), true, false);
    public static final Item SUPER_CANDLE_WHITE = new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_WHITE, new Item.Properties(), true, false);
    public static final Item SUPER_CANDLE_YELLOW = new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_YELLOW, new Item.Properties(), true, false);
    public static final Item INCENSE_CANDLE = new IncenseCandleBlockItem(BzBlocks.INCENSE_BASE_CANDLE, new Item.Properties());
    public static final Item CRYSTALLINE_FLOWER = new BzBlockItem(BzBlocks.CRYSTALLINE_FLOWER, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON), true, true);
    public static final Item STRING_CURTAIN_BLACK = new BlockItem(BzBlocks.STRING_CURTAIN_BLACK, new Item.Properties());
    public static final Item STRING_CURTAIN_BLUE = new BlockItem(BzBlocks.STRING_CURTAIN_BLUE, new Item.Properties());
    public static final Item STRING_CURTAIN_BROWN = new BlockItem(BzBlocks.STRING_CURTAIN_BROWN, new Item.Properties());
    public static final Item STRING_CURTAIN_CYAN = new BlockItem(BzBlocks.STRING_CURTAIN_CYAN, new Item.Properties());
    public static final Item STRING_CURTAIN_GRAY = new BlockItem(BzBlocks.STRING_CURTAIN_GRAY, new Item.Properties());
    public static final Item STRING_CURTAIN_GREEN = new BlockItem(BzBlocks.STRING_CURTAIN_GREEN, new Item.Properties());
    public static final Item STRING_CURTAIN_LIGHT_BLUE = new BlockItem(BzBlocks.STRING_CURTAIN_LIGHT_BLUE, new Item.Properties());
    public static final Item STRING_CURTAIN_LIGHT_GRAY = new BlockItem(BzBlocks.STRING_CURTAIN_LIGHT_GRAY, new Item.Properties());
    public static final Item STRING_CURTAIN_LIME = new BlockItem(BzBlocks.STRING_CURTAIN_LIME, new Item.Properties());
    public static final Item STRING_CURTAIN_MAGENTA = new BlockItem(BzBlocks.STRING_CURTAIN_MAGENTA, new Item.Properties());
    public static final Item STRING_CURTAIN_ORANGE = new BlockItem(BzBlocks.STRING_CURTAIN_ORANGE, new Item.Properties());
    public static final Item STRING_CURTAIN_PINK = new BlockItem(BzBlocks.STRING_CURTAIN_PINK, new Item.Properties());
    public static final Item STRING_CURTAIN_PURPLE = new BlockItem(BzBlocks.STRING_CURTAIN_PURPLE, new Item.Properties());
    public static final Item STRING_CURTAIN_RED = new BlockItem(BzBlocks.STRING_CURTAIN_RED, new Item.Properties());
    public static final Item STRING_CURTAIN_WHITE = new BlockItem(BzBlocks.STRING_CURTAIN_WHITE, new Item.Properties());
    public static final Item STRING_CURTAIN_YELLOW = new BlockItem(BzBlocks.STRING_CURTAIN_YELLOW, new Item.Properties());

    //items
    public static final HoneyCrystalShards HONEY_CRYSTAL_SHARDS = new HoneyCrystalShards(new Item.Properties().food((new FoodProperties.Builder()).nutrition(2).saturationMod(0.15F).build()));
    public static final Item BEE_STINGER = new BeeStinger(new Item.Properties());
    public static final Item SUGAR_WATER_BUCKET = new BzSmartBucket(BzFluids.SUGAR_WATER_FLUID, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));
    public static final Item SUGAR_WATER_BOTTLE = new SugarWaterBottle((new Item.Properties()).craftRemainder(Items.GLASS_BOTTLE).food((new FoodProperties.Builder()).nutrition(1).saturationMod(0.05F).effect(new MobEffectInstance(MobEffects.DIG_SPEED, 600, 0), 1.0F).build()).stacksTo(16));
    public static final Item HONEY_BUCKET = new BucketItem(BzFluids.HONEY_FLUID, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1));
    public static final Item ROYAL_JELLY_BUCKET = new BucketItem(BzFluids.ROYAL_JELLY_FLUID, new Item.Properties().rarity(Rarity.EPIC).craftRemainder(Items.BUCKET).stacksTo(1));
    public static final Item ROYAL_JELLY_BOTTLE = new RoyalJellyBottle((new Item.Properties().rarity(Rarity.EPIC)).craftRemainder(Items.GLASS_BOTTLE).food((new FoodProperties.Builder()).nutrition(10).saturationMod(0.2F).effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 0), 1.0F).effect(new MobEffectInstance(BzEffects.BEENERGIZED, 1200, 1), 1.0F).build()).stacksTo(16));
    public static final Item HONEY_SLIME_SPAWN_EGG = new SpawnEggItem(BzEntities.HONEY_SLIME, 0xFFCC00, 0xFCA800, (new Item.Properties()));
    public static final Item BEEHEMOTH_SPAWN_EGG = new SpawnEggItem(BzEntities.BEEHEMOTH, 0xFFCA47, 0x68372A, (new Item.Properties()));
    public static final Item BEE_QUEEN_SPAWN_EGG = new SpawnEggItem(BzEntities.BEE_QUEEN, 0xFFFFFF, 0xFFFFFF, (new Item.Properties().rarity(Rarity.EPIC)));
    public static final Item MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV = new BzMusicDiscs(14, BzSounds.MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV, (new Item.Properties()).rarity(Rarity.UNCOMMON).stacksTo(1).rarity(Rarity.RARE), BzConfig.musicDiscTimeLengthFlightOfTheBumblebee);
    public static final Item MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY = new BzMusicDiscs(15, BzSounds.MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY, (new Item.Properties()).rarity(Rarity.UNCOMMON).stacksTo(1).rarity(Rarity.RARE), BzConfig.musicDiscTimeLengthHoneyBee);
    public static final Item MUSIC_DISC_LA_BEE_DA_LOCA = new BzMusicDiscs(13, BzSounds.MUSIC_DISC_LA_BEE_DA_LOCA, (new Item.Properties()).rarity(Rarity.UNCOMMON).stacksTo(1).rarity(Rarity.RARE), BzConfig.musicDiscTimeLengthLaBeeDaLoca);
    public static final Item MUSIC_DISC_BEE_LAXING_WITH_THE_HOM_BEES = new BzMusicDiscs(12, BzSounds.MUSIC_DISC_BEE_LAXING_WITH_THE_HOM_BEES, (new Item.Properties()).rarity(Rarity.UNCOMMON).stacksTo(1).rarity(Rarity.RARE), BzConfig.musicDiscTimeLengthBeeLaxingWithTheHomBees);
    public static final Item POLLEN_PUFF = new PollenPuff(new Item.Properties().stacksTo(16));
    public static final Item BEE_BREAD = new BeeBread(new Item.Properties().food((new FoodProperties.Builder()).nutrition(8).saturationMod(0.12F).alwaysEat().effect(new MobEffectInstance(BzEffects.BEENERGIZED, 6000, 0), 1.0F).effect(new MobEffectInstance(MobEffects.CONFUSION, 120, 1), 1.0F).build()));
    public static final Item HONEY_CRYSTAL_SHIELD = new HoneyCrystalShield(new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final Item STINGER_SPEAR = new StingerSpearItem(new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final Item HONEY_COMPASS = new HoneyCompass(new Item.Properties().rarity(Rarity.UNCOMMON));
    public static final Item BEE_CANNON = new BeeCannon(new Item.Properties().stacksTo(1));
    public static final Item CRYSTAL_CANNON = new CrystalCannon(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1));
    public static final HoneyBeeLeggings HONEY_BEE_LEGGINGS_1 = new HoneyBeeLeggings(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.LEGS, new Item.Properties().rarity(Rarity.UNCOMMON), 1);
    public static final HoneyBeeLeggings HONEY_BEE_LEGGINGS_2 = new HoneyBeeLeggings(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.LEGS, new Item.Properties().rarity(Rarity.UNCOMMON), 2);
    public static final BumbleBeeChestplate BUMBLE_BEE_CHESTPLATE_1 = new BumbleBeeChestplate(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.CHEST, new Item.Properties().rarity(Rarity.UNCOMMON), false, 1);
    public static final BumbleBeeChestplate BUMBLE_BEE_CHESTPLATE_2 = new BumbleBeeChestplate(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.CHEST, new Item.Properties().rarity(Rarity.UNCOMMON), false, 2);
    public static final BumbleBeeChestplate TRANS_BUMBLE_BEE_CHESTPLATE_1 = new BumbleBeeChestplate(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.CHEST, new Item.Properties().rarity(Rarity.UNCOMMON), true, 1);
    public static final BumbleBeeChestplate TRANS_BUMBLE_BEE_CHESTPLATE_2 = new BumbleBeeChestplate(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.CHEST, new Item.Properties().rarity(Rarity.UNCOMMON), true, 2);
    public static final StinglessBeeHelmet STINGLESS_BEE_HELMET_1 = new StinglessBeeHelmet(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.HEAD, new Item.Properties().rarity(Rarity.UNCOMMON), 1);
    public static final StinglessBeeHelmet STINGLESS_BEE_HELMET_2 = new StinglessBeeHelmet(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.HEAD, new Item.Properties().rarity(Rarity.UNCOMMON), 2);
    public static final CarpenterBeeBoots CARPENTER_BEE_BOOTS_1 = new CarpenterBeeBoots(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.FEET, new Item.Properties().rarity(Rarity.UNCOMMON), 1);
    public static final CarpenterBeeBoots CARPENTER_BEE_BOOTS_2 = new CarpenterBeeBoots(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.FEET, new Item.Properties().rarity(Rarity.UNCOMMON), 2);
    public static final Item ESSENCE_OF_THE_BEES = new EssenceOfTheBees(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1));

    /**
     * registers the item version of the Blocks so they now exist in the registry
     */
    public static void registerItems() {

        registerItem(new ResourceLocation(Bumblezone.MODID, "pile_of_pollen"), PILE_OF_POLLEN);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "porous_honeycomb_block"), POROUS_HONEYCOMB);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "filled_porous_honeycomb_block"), FILLED_POROUS_HONEYCOMB);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "empty_honeycomb_brood_block"), EMPTY_HONEYCOMB_BROOD);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "honeycomb_brood_block"), HONEYCOMB_BROOD);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "sticky_honey_residue"), STICKY_HONEY_RESIDUE);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "sticky_honey_redstone"), STICKY_HONEY_REDSTONE);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "honey_web"), HONEY_WEB);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "redstone_honey_web"), REDSTONE_HONEY_WEB);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "beehive_beeswax"), BEEHIVE_BEESWAX);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "glistering_honey_crystal"), GLISTERING_HONEY_CRYSTAL);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "honey_crystal"), HONEY_CRYSTAL);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "carvable_wax"), CARVABLE_WAX);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "carvable_wax_wavy"), CARVABLE_WAX_WAVY);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "carvable_wax_flower"), CARVABLE_WAX_FLOWER);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "carvable_wax_chiseled"), CARVABLE_WAX_CHISELED);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "carvable_wax_diamond"), CARVABLE_WAX_DIAMOND);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "carvable_wax_bricks"), CARVABLE_WAX_BRICKS);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "carvable_wax_chains"), CARVABLE_WAX_CHAINS);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "honey_cocoon"), HONEY_COCOON);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "crystalline_flower"), CRYSTALLINE_FLOWER);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "sugar_infused_stone"), SUGAR_INFUSED_STONE);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "sugar_infused_cobblestone"), SUGAR_INFUSED_COBBLESTONE);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "incense_candle"), INCENSE_CANDLE);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "super_candle"), SUPER_CANDLE);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "super_candle_black"), SUPER_CANDLE_BLACK);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "super_candle_blue"), SUPER_CANDLE_BLUE);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "super_candle_brown"), SUPER_CANDLE_BROWN);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "super_candle_cyan"), SUPER_CANDLE_CYAN);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "super_candle_gray"), SUPER_CANDLE_GRAY);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "super_candle_green"), SUPER_CANDLE_GREEN);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "super_candle_light_blue"), SUPER_CANDLE_LIGHT_BLUE);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "super_candle_light_gray"), SUPER_CANDLE_LIGHT_GRAY);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "super_candle_lime"), SUPER_CANDLE_LIME);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "super_candle_magenta"), SUPER_CANDLE_MAGENTA);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "super_candle_orange"), SUPER_CANDLE_ORANGE);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "super_candle_pink"), SUPER_CANDLE_PINK);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "super_candle_purple"), SUPER_CANDLE_PURPLE);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "super_candle_red"), SUPER_CANDLE_RED);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "super_candle_white"), SUPER_CANDLE_WHITE);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "super_candle_yellow"), SUPER_CANDLE_YELLOW);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "string_curtain_black"), STRING_CURTAIN_BLACK);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "string_curtain_blue"), STRING_CURTAIN_BLUE);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "string_curtain_brown"), STRING_CURTAIN_BROWN);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "string_curtain_cyan"), STRING_CURTAIN_CYAN);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "string_curtain_gray"), STRING_CURTAIN_GRAY);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "string_curtain_green"), STRING_CURTAIN_GREEN);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "string_curtain_light_blue"), STRING_CURTAIN_LIGHT_BLUE);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "string_curtain_light_gray"), STRING_CURTAIN_LIGHT_GRAY);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "string_curtain_lime"), STRING_CURTAIN_LIME);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "string_curtain_magenta"), STRING_CURTAIN_MAGENTA);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "string_curtain_orange"), STRING_CURTAIN_ORANGE);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "string_curtain_pink"), STRING_CURTAIN_PINK);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "string_curtain_purple"), STRING_CURTAIN_PURPLE);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "string_curtain_red"), STRING_CURTAIN_RED);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "string_curtain_white"), STRING_CURTAIN_WHITE);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "string_curtain_yellow"), STRING_CURTAIN_YELLOW);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "sugar_water_bottle"), SUGAR_WATER_BOTTLE);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "sugar_water_bucket"), SUGAR_WATER_BUCKET);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "royal_jelly_bottle"), ROYAL_JELLY_BOTTLE);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "royal_jelly_bucket"), ROYAL_JELLY_BUCKET);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "royal_jelly_block"), ROYAL_JELLY_BLOCK);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "honey_bucket"), HONEY_BUCKET);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "honey_compass"), HONEY_COMPASS);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "bee_bread"), BEE_BREAD);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "pollen_puff"), POLLEN_PUFF);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "honey_crystal_shards"), HONEY_CRYSTAL_SHARDS);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "bee_stinger"), BEE_STINGER);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "stinger_spear"), STINGER_SPEAR);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "bee_cannon"), BEE_CANNON);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "crystal_cannon"), CRYSTAL_CANNON);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "honey_crystal_shield"), HONEY_CRYSTAL_SHIELD);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "stingless_bee_helmet_1"), STINGLESS_BEE_HELMET_1);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "stingless_bee_helmet_2"), STINGLESS_BEE_HELMET_2);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "bumble_bee_chestplate_1"), BUMBLE_BEE_CHESTPLATE_1);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "bumble_bee_chestplate_2"), BUMBLE_BEE_CHESTPLATE_2);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "bumble_bee_chestplate_trans_1"), TRANS_BUMBLE_BEE_CHESTPLATE_1);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "bumble_bee_chestplate_trans_2"), TRANS_BUMBLE_BEE_CHESTPLATE_2);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "honey_bee_leggings_1"), HONEY_BEE_LEGGINGS_1);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "honey_bee_leggings_2"), HONEY_BEE_LEGGINGS_2);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "carpenter_bee_boots_1"), CARPENTER_BEE_BOOTS_1);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "carpenter_bee_boots_2"), CARPENTER_BEE_BOOTS_2);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "music_disc_flight_of_the_bumblebee_rimsky_korsakov"), MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "music_disc_honey_bee_rat_faced_boy"), MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "music_disc_la_bee_da_loca"), MUSIC_DISC_LA_BEE_DA_LOCA);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "music_disc_bee_laxing_with_the_hom_bees"), MUSIC_DISC_BEE_LAXING_WITH_THE_HOM_BEES);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "honey_slime_spawn_egg"), HONEY_SLIME_SPAWN_EGG);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "beehemoth_spawn_egg"), BEEHEMOTH_SPAWN_EGG);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "bee_queen_spawn_egg"), BEE_QUEEN_SPAWN_EGG);
        registerItemAndShowInTab(new ResourceLocation(Bumblezone.MODID, "essence_of_the_bees"), ESSENCE_OF_THE_BEES);

        BUMBLEZONE_CREATIVE_TAB = FabricItemGroup
                .builder(new ResourceLocation(Bumblezone.MODID, "main_tab"))
                .displayItems((enabledFeatures, entries, operatorEnabled) ->
                        CREATIVE_MENU_ITEM_ORDERING.forEach(item -> entries.accept(item.getDefaultInstance())))
                .icon(BzItems.HONEYCOMB_BROOD::getDefaultInstance)
                .build();

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS)
            .register(new ResourceLocation(Bumblezone.MODID, "additions_redstone_blocks"),
                (itemGroupEntries) -> itemGroupEntries.acceptAll(Stream.of(
                    STICKY_HONEY_REDSTONE,
                    REDSTONE_HONEY_WEB,
                    ROYAL_JELLY_BLOCK
                ).map(Item::getDefaultInstance).toList())
            );

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS)
            .register(new ResourceLocation(Bumblezone.MODID, "additions_functional_blocks"),
                (itemGroupEntries) -> itemGroupEntries.acceptAll(Stream.of(
                    HONEYCOMB_BROOD,
                    GLISTERING_HONEY_CRYSTAL,
                    HONEY_COCOON,
                    CRYSTALLINE_FLOWER,
                    STICKY_HONEY_RESIDUE,
                    HONEY_WEB,
                    INCENSE_CANDLE,
                    SUPER_CANDLE,
                    SUPER_CANDLE_WHITE,
                    SUPER_CANDLE_LIGHT_GRAY,
                    SUPER_CANDLE_GRAY,
                    SUPER_CANDLE_BLACK,
                    SUPER_CANDLE_BROWN,
                    SUPER_CANDLE_RED,
                    SUPER_CANDLE_ORANGE,
                    SUPER_CANDLE_YELLOW,
                    SUPER_CANDLE_LIME,
                    SUPER_CANDLE_GREEN,
                    SUPER_CANDLE_CYAN,
                    SUPER_CANDLE_LIGHT_BLUE,
                    SUPER_CANDLE_BLUE,
                    SUPER_CANDLE_PURPLE,
                    SUPER_CANDLE_MAGENTA,
                    SUPER_CANDLE_PINK,
                    STRING_CURTAIN_WHITE,
                    STRING_CURTAIN_LIGHT_GRAY,
                    STRING_CURTAIN_GRAY,
                    STRING_CURTAIN_BLACK,
                    STRING_CURTAIN_BROWN,
                    STRING_CURTAIN_RED,
                    STRING_CURTAIN_ORANGE,
                    STRING_CURTAIN_YELLOW,
                    STRING_CURTAIN_LIME,
                    STRING_CURTAIN_GREEN,
                    STRING_CURTAIN_CYAN,
                    STRING_CURTAIN_LIGHT_BLUE,
                    STRING_CURTAIN_BLUE,
                    STRING_CURTAIN_PURPLE,
                    STRING_CURTAIN_MAGENTA,
                    STRING_CURTAIN_PINK
                ).map(Item::getDefaultInstance).toList())
            );

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COLORED_BLOCKS)
            .register(new ResourceLocation(Bumblezone.MODID, "additions_colored_blocks"),
                (itemGroupEntries) -> itemGroupEntries.acceptAll(Stream.of(
                    SUPER_CANDLE,
                    SUPER_CANDLE_WHITE,
                    SUPER_CANDLE_LIGHT_GRAY,
                    SUPER_CANDLE_GRAY,
                    SUPER_CANDLE_BLACK,
                    SUPER_CANDLE_BROWN,
                    SUPER_CANDLE_RED,
                    SUPER_CANDLE_ORANGE,
                    SUPER_CANDLE_YELLOW,
                    SUPER_CANDLE_LIME,
                    SUPER_CANDLE_GREEN,
                    SUPER_CANDLE_CYAN,
                    SUPER_CANDLE_LIGHT_BLUE,
                    SUPER_CANDLE_BLUE,
                    SUPER_CANDLE_PURPLE,
                    SUPER_CANDLE_MAGENTA,
                    SUPER_CANDLE_PINK,
                    STRING_CURTAIN_WHITE,
                    STRING_CURTAIN_LIGHT_GRAY,
                    STRING_CURTAIN_GRAY,
                    STRING_CURTAIN_BLACK,
                    STRING_CURTAIN_BROWN,
                    STRING_CURTAIN_RED,
                    STRING_CURTAIN_ORANGE,
                    STRING_CURTAIN_YELLOW,
                    STRING_CURTAIN_LIME,
                    STRING_CURTAIN_GREEN,
                    STRING_CURTAIN_CYAN,
                    STRING_CURTAIN_LIGHT_BLUE,
                    STRING_CURTAIN_BLUE,
                    STRING_CURTAIN_PURPLE,
                    STRING_CURTAIN_MAGENTA,
                    STRING_CURTAIN_PINK
                ).map(Item::getDefaultInstance).toList())
            );

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT)
            .register(new ResourceLocation(Bumblezone.MODID, "additions_combat_items"),
                (itemGroupEntries) -> itemGroupEntries.acceptAll(Stream.of(
                    BEE_STINGER,
                    STINGER_SPEAR,
                    BEE_CANNON,
                    CRYSTAL_CANNON,
                    HONEY_CRYSTAL_SHIELD,
                    STINGLESS_BEE_HELMET_1,
                    STINGLESS_BEE_HELMET_2,
                    BUMBLE_BEE_CHESTPLATE_1,
                    BUMBLE_BEE_CHESTPLATE_2,
                    TRANS_BUMBLE_BEE_CHESTPLATE_1,
                    TRANS_BUMBLE_BEE_CHESTPLATE_2,
                    HONEY_BEE_LEGGINGS_1,
                    HONEY_BEE_LEGGINGS_2,
                    CARPENTER_BEE_BOOTS_1,
                    CARPENTER_BEE_BOOTS_2
                ).map(Item::getDefaultInstance).toList())
            );

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS)
            .register(new ResourceLocation(Bumblezone.MODID, "additions_spawn_eggs"),
                (itemGroupEntries) -> itemGroupEntries.acceptAll(Stream.of(
                    HONEY_SLIME_SPAWN_EGG,
                    BEEHEMOTH_SPAWN_EGG,
                    BEE_QUEEN_SPAWN_EGG
                ).map(Item::getDefaultInstance).toList())
            );

    }

    private static final List<Item> CREATIVE_MENU_ITEM_ORDERING = new ArrayList<>();
    
    private static void registerItemAndShowInTab(ResourceLocation rl, Item item) {
        CREATIVE_MENU_ITEM_ORDERING.add(item);
        registerItem(rl, item);
    }

    private static void registerItem(ResourceLocation rl, Item item) {
        Registry.register(BuiltInRegistries.ITEM, rl, item);
    }
}