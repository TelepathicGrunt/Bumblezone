package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.CarvableWax;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.items.*;
import com.telepathicgrunt.the_bumblezone.items.materials.BeeArmorMaterial;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Stream;


public class BzItems {
    /**
     * creative tab to hold our items
     */
    public static CreativeModeTab BUMBLEZONE_TAB;

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Bumblezone.MODID);

    //blocks
    public static final RegistryObject<Item> POROUS_HONEYCOMB = ITEMS.register("porous_honeycomb_block", () -> new BlockItem(BzBlocks.POROUS_HONEYCOMB.get(), new Item.Properties()));
    public static final RegistryObject<Item> FILLED_POROUS_HONEYCOMB = ITEMS.register("filled_porous_honeycomb_block", () -> new BlockItem(BzBlocks.FILLED_POROUS_HONEYCOMB.get(), new Item.Properties()));
    public static final RegistryObject<Item> EMPTY_HONEYCOMB_BROOD = ITEMS.register("empty_honeycomb_brood_block", () -> new BlockItem(BzBlocks.EMPTY_HONEYCOMB_BROOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> HONEYCOMB_BROOD = ITEMS.register("honeycomb_brood_block", () -> new BlockItem(BzBlocks.HONEYCOMB_BROOD.get(), new Item.Properties()));
    public static final RegistryObject<Item> SUGAR_INFUSED_STONE = ITEMS.register("sugar_infused_stone", () -> new BlockItem(BzBlocks.SUGAR_INFUSED_STONE.get(), new Item.Properties()));
    public static final RegistryObject<Item> SUGAR_INFUSED_COBBLESTONE = ITEMS.register("sugar_infused_cobblestone", () -> new BlockItem(BzBlocks.SUGAR_INFUSED_COBBLESTONE.get(), new Item.Properties()));
    public static final RegistryObject<Item> BEEHIVE_BEESWAX = ITEMS.register("beehive_beeswax", () -> new BlockItem(BzBlocks.BEEHIVE_BEESWAX.get(), new Item.Properties()));
    public static final RegistryObject<Item> STICKY_HONEY_RESIDUE = ITEMS.register("sticky_honey_residue", () -> new BlockItem(BzBlocks.STICKY_HONEY_RESIDUE.get(), new Item.Properties()));
    public static final RegistryObject<Item> STICKY_HONEY_REDSTONE = ITEMS.register("sticky_honey_redstone", () -> new BlockItem(BzBlocks.STICKY_HONEY_REDSTONE.get(), new Item.Properties()));
    public static final RegistryObject<Item> HONEY_WEB = ITEMS.register("honey_web", () -> new BlockItem(BzBlocks.HONEY_WEB.get(), new Item.Properties()));
    public static final RegistryObject<Item> REDSTONE_HONEY_WEB = ITEMS.register("redstone_honey_web", () -> new BlockItem(BzBlocks.REDSTONE_HONEY_WEB.get(), new Item.Properties()));
    public static final RegistryObject<Item> PILE_OF_POLLEN = ITEMS.register("pile_of_pollen", () -> new BlockItem(BzBlocks.PILE_OF_POLLEN.get(), new Item.Properties())); // Not obtainable by default. Purely for advancement icon.
    public static final RegistryObject<Item> HONEY_CRYSTAL = ITEMS.register("honey_crystal", () -> new BzHoneyCrystalBlockItem(BzBlocks.HONEY_CRYSTAL.get(), new Item.Properties()));
    public static final RegistryObject<Item> HONEY_COCOON = ITEMS.register("honey_cocoon", () -> new BzBlockItem(BzBlocks.HONEY_COCOON.get(), new Item.Properties(), false, true));
    public static final RegistryObject<Item> ROYAL_JELLY_BLOCK = ITEMS.register("royal_jelly_block", () -> new BlockItem(BzBlocks.ROYAL_JELLY_BLOCK.get(), new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> GLISTERING_HONEY_CRYSTAL = ITEMS.register("glistering_honey_crystal", () -> new BlockItem(BzBlocks.GLISTERING_HONEY_CRYSTAL.get(), new Item.Properties()));
    public static final RegistryObject<Item> CARVABLE_WAX = ITEMS.register("carvable_wax", () -> new BzBlockItem(BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.UNCARVED), new Item.Properties()));
    public static final RegistryObject<Item> CARVABLE_WAX_WAVY = ITEMS.register("carvable_wax_wavy", () -> new BzBlockItem(BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.WAVY), new Item.Properties()));
    public static final RegistryObject<Item> CARVABLE_WAX_FLOWER = ITEMS.register("carvable_wax_flower", () -> new BzBlockItem(BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.FLOWER), new Item.Properties()));
    public static final RegistryObject<Item> CARVABLE_WAX_CHISELED = ITEMS.register("carvable_wax_chiseled", () -> new BzBlockItem(BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.CHISELED), new Item.Properties()));
    public static final RegistryObject<Item> CARVABLE_WAX_DIAMOND = ITEMS.register("carvable_wax_diamond", () -> new BzBlockItem(BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.DIAMOND), new Item.Properties()));
    public static final RegistryObject<Item> CARVABLE_WAX_BRICKS = ITEMS.register("carvable_wax_bricks", () -> new BzBlockItem(BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.BRICKS), new Item.Properties()));
    public static final RegistryObject<Item> CARVABLE_WAX_CHAINS = ITEMS.register("carvable_wax_chains", () -> new BzBlockItem(BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.CHAINS), new Item.Properties()));
    public static final RegistryObject<Item> SUPER_CANDLE = ITEMS.register("super_candle", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE.get(), new Item.Properties(), true, false));
    public static final RegistryObject<Item> SUPER_CANDLE_BLACK = ITEMS.register("super_candle_black", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_BLACK.get(), new Item.Properties(), true, false));
    public static final RegistryObject<Item> SUPER_CANDLE_BLUE = ITEMS.register("super_candle_blue", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_BLUE.get(), new Item.Properties(), true, false));
    public static final RegistryObject<Item> SUPER_CANDLE_BROWN = ITEMS.register("super_candle_brown", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_BROWN.get(), new Item.Properties(), true, false));
    public static final RegistryObject<Item> SUPER_CANDLE_CYAN = ITEMS.register("super_candle_cyan", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_CYAN.get(), new Item.Properties(), true, false));
    public static final RegistryObject<Item> SUPER_CANDLE_GRAY = ITEMS.register("super_candle_gray", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_GRAY.get(), new Item.Properties(), true, false));
    public static final RegistryObject<Item> SUPER_CANDLE_GREEN = ITEMS.register("super_candle_green", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_GREEN.get(), new Item.Properties(), true, false));
    public static final RegistryObject<Item> SUPER_CANDLE_LIGHT_BLUE = ITEMS.register("super_candle_light_blue", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_LIGHT_BLUE.get(), new Item.Properties(), true, false));
    public static final RegistryObject<Item> SUPER_CANDLE_LIGHT_GRAY = ITEMS.register("super_candle_light_gray", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_LIGHT_GRAY.get(), new Item.Properties(), true, false));
    public static final RegistryObject<Item> SUPER_CANDLE_LIME = ITEMS.register("super_candle_lime", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_LIME.get(), new Item.Properties(), true, false));
    public static final RegistryObject<Item> SUPER_CANDLE_MAGENTA = ITEMS.register("super_candle_magenta", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_MAGENTA.get(), new Item.Properties(), true, false));
    public static final RegistryObject<Item> SUPER_CANDLE_ORANGE = ITEMS.register("super_candle_orange", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_ORANGE.get(), new Item.Properties(), true, false));
    public static final RegistryObject<Item> SUPER_CANDLE_PINK = ITEMS.register("super_candle_pink", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_PINK.get(), new Item.Properties(), true, false));
    public static final RegistryObject<Item> SUPER_CANDLE_PURPLE = ITEMS.register("super_candle_purple", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_PURPLE.get(), new Item.Properties(), true, false));
    public static final RegistryObject<Item> SUPER_CANDLE_RED = ITEMS.register("super_candle_red", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_RED.get(), new Item.Properties(), true, false));
    public static final RegistryObject<Item> SUPER_CANDLE_WHITE = ITEMS.register("super_candle_white", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_WHITE.get(), new Item.Properties(), true, false));
    public static final RegistryObject<Item> SUPER_CANDLE_YELLOW = ITEMS.register("super_candle_yellow", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_YELLOW.get(), new Item.Properties(), true, false));
    public static final RegistryObject<Item> INCENSE_CANDLE = ITEMS.register("incense_candle", () -> new IncenseCandleBlockItem(BzBlocks.INCENSE_BASE_CANDLE.get(), new Item.Properties()));
    public static final RegistryObject<Item> CRYSTALLINE_FLOWER = ITEMS.register("crystalline_flower", () -> new BzBlockItem(BzBlocks.CRYSTALLINE_FLOWER.get(), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON), true, true));

    //items
    public static final RegistryObject<ArrowItem> HONEY_CRYSTAL_SHARDS = ITEMS.register("honey_crystal_shards", () -> new HoneyCrystalShards(new Item.Properties().food((new FoodProperties.Builder()).nutrition(2).saturationMod(0.15F).build())));
    public static final RegistryObject<Item> BEE_STINGER = ITEMS.register("bee_stinger", () -> new BeeStinger(new Item.Properties()));
    public static final RegistryObject<Item> HONEY_BUCKET = ITEMS.register("honey_bucket", () -> new BucketItem(BzFluids.HONEY_FLUID, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryObject<Item> SUGAR_WATER_BUCKET = ITEMS.register("sugar_water_bucket", () -> new BzBucketItem(BzFluids.SUGAR_WATER_FLUID, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryObject<Item> SUGAR_WATER_BOTTLE = ITEMS.register("sugar_water_bottle", () -> new SugarWaterBottle((new Item.Properties()).craftRemainder(Items.GLASS_BOTTLE).food((new FoodProperties.Builder()).nutrition(1).saturationMod(0.05F).effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 600, 0), 1.0F).build()).stacksTo(16)));
    public static final RegistryObject<Item> ROYAL_JELLY_BUCKET = ITEMS.register("royal_jelly_bucket", () -> new BzBucketItem(BzFluids.ROYAL_JELLY_FLUID, new Item.Properties().rarity(Rarity.EPIC).craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryObject<Item> ROYAL_JELLY_BOTTLE = ITEMS.register("royal_jelly_bottle", () -> new RoyalJellyBottle((new Item.Properties().rarity(Rarity.EPIC)).craftRemainder(Items.GLASS_BOTTLE).food((new FoodProperties.Builder()).nutrition(12).saturationMod(1.0F).effect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, 1200, 0), 1.0F).effect(() -> new MobEffectInstance(MobEffects.JUMP, 1200, 3), 1.0F).effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 1), 1.0F).effect(() -> new MobEffectInstance(BzEffects.BEENERGIZED.get(), 1200, 1), 1.0F).build()).stacksTo(16)));
    public static final RegistryObject<Item> HONEY_SLIME_SPAWN_EGG = ITEMS.register("honey_slime_spawn_egg", () -> new DispenserAddedSpawnEgg(BzEntities.HONEY_SLIME, 0xFFCC00, 0xFCA800, (new Item.Properties())));
    public static final RegistryObject<Item> BEEHEMOTH_SPAWN_EGG = ITEMS.register("beehemoth_spawn_egg", () -> new DispenserAddedSpawnEgg(BzEntities.BEEHEMOTH, 0xFFCA47, 0x68372A, (new Item.Properties())));
    public static final RegistryObject<Item> BEE_QUEEN_SPAWN_EGG = ITEMS.register("bee_queen_spawn_egg", () -> new DispenserAddedSpawnEgg(BzEntities.BEE_QUEEN, 0xFFFFFF, 0xFFFFFF, (new Item.Properties().rarity(Rarity.EPIC))));
    public static final RegistryObject<Item> MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV = ITEMS.register("music_disc_flight_of_the_bumblebee_rimsky_korsakov", () -> new BzMusicDiscs(14, BzSounds.MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV, (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), BzGeneralConfigs.musicDiscTimeLengthFlightOfTheBumblebee));
    public static final RegistryObject<Item> MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY = ITEMS.register("music_disc_honey_bee_rat_faced_boy", () -> new BzMusicDiscs(15, BzSounds.MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY, (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), BzGeneralConfigs.musicDiscTimeLengthHoneyBee));
    public static final RegistryObject<Item> MUSIC_DISC_LA_BEE_DA_LOCA = ITEMS.register("music_disc_la_bee_da_loca", () -> new BzMusicDiscs(13, BzSounds.MUSIC_DISC_LA_BEE_DA_LOCA, (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), BzGeneralConfigs.musicDiscTimeLengthLaBeeDaLoca));
    public static final RegistryObject<Item> MUSIC_DISC_BEE_LAXING_WITH_THE_HOM_BEES = ITEMS.register("music_disc_bee_laxing_with_the_hom_bees", () -> new BzMusicDiscs(12, BzSounds.MUSIC_DISC_BEE_LAXING_WITH_THE_HOM_BEES, (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), BzGeneralConfigs.musicDiscTimeLengthBeeLaxingWithTheHomBees));
    public static final RegistryObject<Item> POLLEN_PUFF = ITEMS.register("pollen_puff", () -> new PollenPuff(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> BEE_BREAD = ITEMS.register("bee_bread", () -> new BeeBread(new Item.Properties().food((new FoodProperties.Builder()).nutrition(8).saturationMod(0.12F).alwaysEat().effect(() -> new MobEffectInstance(BzEffects.BEENERGIZED.get(), 6000, 0), 1.0F).effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 120, 1), 1.0F).build())));
    public static final RegistryObject<Item> HONEY_CRYSTAL_SHIELD = ITEMS.register("honey_crystal_shield", () -> new HoneyCrystalShield(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> STINGER_SPEAR = ITEMS.register("stinger_spear", () -> new StingerSpearItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> HONEY_COMPASS = ITEMS.register("honey_compass", () -> new HoneyCompass(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> BEE_CANNON = ITEMS.register("bee_cannon", () -> new BeeCannon(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> CRYSTAL_CANNON = ITEMS.register("crystal_cannon", () -> new CrystalCannon(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final RegistryObject<Item> HONEY_BEE_LEGGINGS_1 = ITEMS.register("honey_bee_leggings_1", () -> new HoneyBeeLeggings(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.LEGS, new Item.Properties().rarity(Rarity.UNCOMMON), 1));
    public static final RegistryObject<Item> HONEY_BEE_LEGGINGS_2 = ITEMS.register("honey_bee_leggings_2", () -> new HoneyBeeLeggings(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.LEGS, new Item.Properties().rarity(Rarity.UNCOMMON), 2));
    public static final RegistryObject<Item> BUMBLE_BEE_CHESTPLATE_1 = ITEMS.register("bumble_bee_chestplate_1", () -> new BumbleBeeChestplate(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.CHEST, new Item.Properties().rarity(Rarity.UNCOMMON), false, 1));
    public static final RegistryObject<Item> BUMBLE_BEE_CHESTPLATE_2 = ITEMS.register("bumble_bee_chestplate_2", () -> new BumbleBeeChestplate(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.CHEST, new Item.Properties().rarity(Rarity.UNCOMMON), false, 2));
    public static final RegistryObject<Item> TRANS_BUMBLE_BEE_CHESTPLATE_1 = ITEMS.register("bumble_bee_chestplate_trans_1", () -> new BumbleBeeChestplate(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.CHEST, new Item.Properties().rarity(Rarity.UNCOMMON), true, 1));
    public static final RegistryObject<Item> TRANS_BUMBLE_BEE_CHESTPLATE_2 = ITEMS.register("bumble_bee_chestplate_trans_2", () -> new BumbleBeeChestplate(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.CHEST, new Item.Properties().rarity(Rarity.UNCOMMON), true, 2));
    public static final RegistryObject<Item> STINGLESS_BEE_HELMET_1 = ITEMS.register("stingless_bee_helmet_1", () -> new StinglessBeeHelmet(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.HEAD, new Item.Properties().rarity(Rarity.UNCOMMON), 1));
    public static final RegistryObject<Item> STINGLESS_BEE_HELMET_2 = ITEMS.register("stingless_bee_helmet_2", () -> new StinglessBeeHelmet(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.HEAD, new Item.Properties().rarity(Rarity.UNCOMMON), 2));
    public static final RegistryObject<Item> CARPENTER_BEE_BOOTS_1 = ITEMS.register("carpenter_bee_boots_1", () -> new CarpenterBeeBoots(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.FEET, new Item.Properties().rarity(Rarity.UNCOMMON), 1));
    public static final RegistryObject<Item> CARPENTER_BEE_BOOTS_2 = ITEMS.register("carpenter_bee_boots_2", () -> new CarpenterBeeBoots(BeeArmorMaterial.BEE_MATERIAL, EquipmentSlot.FEET, new Item.Properties().rarity(Rarity.UNCOMMON), 2));
    public static final RegistryObject<Item> ESSENCE_OF_THE_BEES = ITEMS.register("essence_of_the_bees", () -> new EssenceOfTheBees(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));

    public static void registerCreativeModeTab(CreativeModeTabEvent.Register event) {

        BUMBLEZONE_TAB = event.registerCreativeModeTab(new ResourceLocation(Bumblezone.MODID, "main_tab"),
            builder -> builder.icon(() -> HONEYCOMB_BROOD.get().getDefaultInstance())
            .title(Component.translatable("itemGroup." + Bumblezone.MODID + ".main_tab"))
            .withLabelColor(0x664400)
            .displayItems((features, output, hasPermissions) ->
                output.acceptAll(Stream.of(
                        POROUS_HONEYCOMB,
                        FILLED_POROUS_HONEYCOMB,
                        EMPTY_HONEYCOMB_BROOD,
                        HONEYCOMB_BROOD,
                        STICKY_HONEY_RESIDUE,
                        STICKY_HONEY_REDSTONE,
                        HONEY_WEB,
                        REDSTONE_HONEY_WEB,
                        BEEHIVE_BEESWAX,
                        GLISTERING_HONEY_CRYSTAL,
                        HONEY_CRYSTAL,
                        CARVABLE_WAX,
                        CARVABLE_WAX_WAVY,
                        CARVABLE_WAX_FLOWER,
                        CARVABLE_WAX_CHISELED,
                        CARVABLE_WAX_DIAMOND,
                        CARVABLE_WAX_BRICKS,
                        CARVABLE_WAX_CHAINS,
                        HONEY_COCOON,
                        CRYSTALLINE_FLOWER,
                        SUGAR_INFUSED_STONE,
                        SUGAR_INFUSED_COBBLESTONE,
                        INCENSE_CANDLE,
                        SUPER_CANDLE,
                        SUPER_CANDLE_BLACK,
                        SUPER_CANDLE_BLUE,
                        SUPER_CANDLE_BROWN,
                        SUPER_CANDLE_CYAN,
                        SUPER_CANDLE_GRAY,
                        SUPER_CANDLE_GREEN,
                        SUPER_CANDLE_LIGHT_BLUE,
                        SUPER_CANDLE_LIGHT_GRAY,
                        SUPER_CANDLE_LIME,
                        SUPER_CANDLE_MAGENTA,
                        SUPER_CANDLE_ORANGE,
                        SUPER_CANDLE_PINK,
                        SUPER_CANDLE_PURPLE,
                        SUPER_CANDLE_RED,
                        SUPER_CANDLE_WHITE,
                        SUPER_CANDLE_YELLOW,
                        SUGAR_WATER_BOTTLE,
                        SUGAR_WATER_BUCKET,
                        ROYAL_JELLY_BOTTLE,
                        ROYAL_JELLY_BUCKET,
                        ROYAL_JELLY_BLOCK,
                        HONEY_BUCKET,
                        HONEY_CRYSTAL_SHARDS,
                        BEE_BREAD,
                        POLLEN_PUFF,
                        HONEY_COMPASS,
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
                        CARPENTER_BEE_BOOTS_2,
                        MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV,
                        MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY,
                        MUSIC_DISC_LA_BEE_DA_LOCA,
                        MUSIC_DISC_BEE_LAXING_WITH_THE_HOM_BEES,
                        HONEY_SLIME_SPAWN_EGG,
                        BEEHEMOTH_SPAWN_EGG,
                        BEE_QUEEN_SPAWN_EGG,
                        ESSENCE_OF_THE_BEES
                )
                .map(item -> item.get().getDefaultInstance())
                .toList())
            )
        );
    }

    public static void addToCreativeModeTabs(CreativeModeTabEvent.BuildContents event) {
        
        if (event.getTab() == CreativeModeTabs.REDSTONE_BLOCKS) {
            event.acceptAll(Stream.of(
                STICKY_HONEY_REDSTONE,
                REDSTONE_HONEY_WEB,
                ROYAL_JELLY_BLOCK
            ).map(item -> item.get().getDefaultInstance()).toList());
        }

        if (event.getTab() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.acceptAll(Stream.of(
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
                SUPER_CANDLE_PINK
            ).map(item -> item.get().getDefaultInstance()).toList());
        }

        if (event.getTab() == CreativeModeTabs.COLORED_BLOCKS) {
            event.acceptAll(Stream.of(
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
                SUPER_CANDLE_PINK
            ).map(item -> item.get().getDefaultInstance()).toList());
        }

        if (event.getTab() == CreativeModeTabs.COMBAT) {
            event.acceptAll(Stream.of(
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
            ).map(item -> item.get().getDefaultInstance()).toList());
        }

        if (event.getTab() == CreativeModeTabs.SPAWN_EGGS) {
            event.acceptAll(Stream.of(
                HONEY_SLIME_SPAWN_EGG,
                BEEHEMOTH_SPAWN_EGG,
                BEE_QUEEN_SPAWN_EGG
            ).map(item -> item.get().getDefaultInstance()).toList());
        }
    }
}