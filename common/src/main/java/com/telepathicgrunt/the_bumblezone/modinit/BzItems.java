package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.CarvableWax;
import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.configs.BzGeneralConfigs;
import com.telepathicgrunt.the_bumblezone.fluids.base.BzBucketItem;
import com.telepathicgrunt.the_bumblezone.items.*;
import com.telepathicgrunt.the_bumblezone.items.essence.CalmingEssence;
import com.telepathicgrunt.the_bumblezone.items.essence.ContinuityEssence;
import com.telepathicgrunt.the_bumblezone.items.essence.EssenceOfTheBees;
import com.telepathicgrunt.the_bumblezone.items.essence.KnowingEssence;
import com.telepathicgrunt.the_bumblezone.items.essence.LifeEssence;
import com.telepathicgrunt.the_bumblezone.items.essence.RadianceEssence;
import com.telepathicgrunt.the_bumblezone.items.essence.RagingEssence;
import com.telepathicgrunt.the_bumblezone.items.materials.BeeArmorMaterial;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;


public class BzItems {

    public static final ResourcefulRegistry<Item> ITEMS = ResourcefulRegistries.create(BuiltInRegistries.ITEM, Bumblezone.MODID);

    //blocks
    public static final RegistryEntry<Item> POROUS_HONEYCOMB = ITEMS.register("porous_honeycomb_block", () -> new BlockItem(BzBlocks.POROUS_HONEYCOMB.get(), new Item.Properties()));
    public static final RegistryEntry<Item> FILLED_POROUS_HONEYCOMB = ITEMS.register("filled_porous_honeycomb_block", () -> new BlockItem(BzBlocks.FILLED_POROUS_HONEYCOMB.get(), new Item.Properties()));
    public static final RegistryEntry<Item> EMPTY_HONEYCOMB_BROOD = ITEMS.register("empty_honeycomb_brood_block", () -> new BlockItem(BzBlocks.EMPTY_HONEYCOMB_BROOD.get(), new Item.Properties()));
    public static final RegistryEntry<Item> HONEYCOMB_BROOD = ITEMS.register("honeycomb_brood_block", () -> new BlockItem(BzBlocks.HONEYCOMB_BROOD.get(), new Item.Properties()));
    public static final RegistryEntry<Item> STICKY_HONEY_RESIDUE = ITEMS.register("sticky_honey_residue", () -> new BlockItem(BzBlocks.STICKY_HONEY_RESIDUE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> STICKY_HONEY_REDSTONE = ITEMS.register("sticky_honey_redstone", () -> new BlockItem(BzBlocks.STICKY_HONEY_REDSTONE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> HONEY_WEB = ITEMS.register("honey_web", () -> new BlockItem(BzBlocks.HONEY_WEB.get(), new Item.Properties()));
    public static final RegistryEntry<Item> REDSTONE_HONEY_WEB = ITEMS.register("redstone_honey_web", () -> new BlockItem(BzBlocks.REDSTONE_HONEY_WEB.get(), new Item.Properties()));
    public static final RegistryEntry<Item> BEEHIVE_BEESWAX = ITEMS.register("beehive_beeswax", () -> new BlockItem(BzBlocks.BEEHIVE_BEESWAX.get(), new Item.Properties()));
    public static final RegistryEntry<Item> GLISTERING_HONEY_CRYSTAL = ITEMS.register("glistering_honey_crystal", () -> new BlockItem(BzBlocks.GLISTERING_HONEY_CRYSTAL.get(), new Item.Properties()));
    public static final RegistryEntry<Item> HONEY_CRYSTAL = ITEMS.register("honey_crystal", () -> new BzHoneyCrystalBlockItem(BzBlocks.HONEY_CRYSTAL.get(), new Item.Properties()));
    public static final RegistryEntry<Item> CARVABLE_WAX = ITEMS.register("carvable_wax", () -> new BzBlockItem(BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.UNCARVED), new Item.Properties()));
    public static final RegistryEntry<Item> CARVABLE_WAX_WAVY = ITEMS.register("carvable_wax_wavy", () -> new BzBlockItem(BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.WAVY), new Item.Properties()));
    public static final RegistryEntry<Item> CARVABLE_WAX_FLOWER = ITEMS.register("carvable_wax_flower", () -> new BzBlockItem(BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.FLOWER), new Item.Properties()));
    public static final RegistryEntry<Item> CARVABLE_WAX_CHISELED = ITEMS.register("carvable_wax_chiseled", () -> new BzBlockItem(BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.CHISELED), new Item.Properties()));
    public static final RegistryEntry<Item> CARVABLE_WAX_DIAMOND = ITEMS.register("carvable_wax_diamond", () -> new BzBlockItem(BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.DIAMOND), new Item.Properties()));
    public static final RegistryEntry<Item> CARVABLE_WAX_BRICKS = ITEMS.register("carvable_wax_bricks", () -> new BzBlockItem(BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.BRICKS), new Item.Properties()));
    public static final RegistryEntry<Item> CARVABLE_WAX_CHAINS = ITEMS.register("carvable_wax_chains", () -> new BzBlockItem(BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.CHAINS), new Item.Properties()));
    public static final RegistryEntry<Item> CARVABLE_WAX_MUSIC = ITEMS.register("carvable_wax_music", () -> new BzBlockItem(BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.MUSIC), new Item.Properties()));
    public static final RegistryEntry<Item> CARVABLE_WAX_GRATE = ITEMS.register("carvable_wax_grate", () -> new BzBlockItem(BzBlocks.CARVABLE_WAX.get().defaultBlockState().setValue(CarvableWax.CARVING, CarvableWax.Carving.GRATE), new Item.Properties()));
    public static final RegistryEntry<Item> ANCIENT_WAX_BRICKS = ITEMS.register("ancient_wax_bricks", () -> new BlockItem(BzBlocks.ANCIENT_WAX_BRICKS.get(), new Item.Properties()));
    public static final RegistryEntry<Item> ANCIENT_WAX_DIAMOND = ITEMS.register("ancient_wax_diamond", () -> new BlockItem(BzBlocks.ANCIENT_WAX_DIAMOND.get(), new Item.Properties()));
    public static final RegistryEntry<Item> ANCIENT_WAX_COMPOUND_EYES = ITEMS.register("ancient_wax_compound_eyes", () -> new BlockItem(BzBlocks.ANCIENT_WAX_COMPOUND_EYES.get(), new Item.Properties()));
    public static final RegistryEntry<Item> ANCIENT_WAX_BRICKS_STAIRS = ITEMS.register("ancient_wax_bricks_stairs", () -> new BlockItem(BzBlocks.ANCIENT_WAX_BRICKS_STAIRS.get(), new Item.Properties()));
    public static final RegistryEntry<Item> ANCIENT_WAX_DIAMOND_STAIRS = ITEMS.register("ancient_wax_diamond_stairs", () -> new BlockItem(BzBlocks.ANCIENT_WAX_DIAMOND_STAIRS.get(), new Item.Properties()));
    public static final RegistryEntry<Item> ANCIENT_WAX_COMPOUND_EYES_STAIRS = ITEMS.register("ancient_wax_compound_eyes_stairs", () -> new BlockItem(BzBlocks.ANCIENT_WAX_COMPOUND_EYES_STAIRS.get(), new Item.Properties()));
    public static final RegistryEntry<Item> ANCIENT_WAX_BRICKS_SLAB = ITEMS.register("ancient_wax_bricks_slab", () -> new BlockItem(BzBlocks.ANCIENT_WAX_BRICKS_SLAB.get(), new Item.Properties()));
    public static final RegistryEntry<Item> ANCIENT_WAX_DIAMOND_SLAB = ITEMS.register("ancient_wax_diamond_slab", () -> new BlockItem(BzBlocks.ANCIENT_WAX_DIAMOND_SLAB.get(), new Item.Properties()));
    public static final RegistryEntry<Item> ANCIENT_WAX_COMPOUND_EYES_SLAB = ITEMS.register("ancient_wax_compound_eyes_slab", () -> new BlockItem(BzBlocks.ANCIENT_WAX_COMPOUND_EYES_SLAB.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_CHANNEL = ITEMS.register("luminescent_wax_channel", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_CHANNEL.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_CHANNEL_RED = ITEMS.register("luminescent_wax_channel_red", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_CHANNEL_RED.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_CHANNEL_PURPLE = ITEMS.register("luminescent_wax_channel_purple", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_CHANNEL_PURPLE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_CHANNEL_BLUE = ITEMS.register("luminescent_wax_channel_blue", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_CHANNEL_BLUE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_CHANNEL_GREEN = ITEMS.register("luminescent_wax_channel_green", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_CHANNEL_GREEN.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_CHANNEL_YELLOW = ITEMS.register("luminescent_wax_channel_yellow", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_CHANNEL_YELLOW.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_CHANNEL_WHITE = ITEMS.register("luminescent_wax_channel_white", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_CHANNEL_WHITE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_CORNER = ITEMS.register("luminescent_wax_corner", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_CORNER.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_CORNER_RED = ITEMS.register("luminescent_wax_corner_red", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_CORNER_RED.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_CORNER_PURPLE = ITEMS.register("luminescent_wax_corner_purple", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_CORNER_PURPLE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_CORNER_BLUE = ITEMS.register("luminescent_wax_corner_blue", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_CORNER_BLUE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_CORNER_GREEN = ITEMS.register("luminescent_wax_corner_green", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_CORNER_GREEN.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_CORNER_YELLOW = ITEMS.register("luminescent_wax_corner_yellow", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_CORNER_YELLOW.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_CORNER_WHITE = ITEMS.register("luminescent_wax_corner_white", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_CORNER_WHITE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_NODE = ITEMS.register("luminescent_wax_node", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_NODE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_NODE_RED = ITEMS.register("luminescent_wax_node_red", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_NODE_RED.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_NODE_PURPLE = ITEMS.register("luminescent_wax_node_purple", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_NODE_PURPLE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_NODE_BLUE = ITEMS.register("luminescent_wax_node_blue", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_NODE_BLUE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_NODE_GREEN = ITEMS.register("luminescent_wax_node_green", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_NODE_GREEN.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_NODE_YELLOW = ITEMS.register("luminescent_wax_node_yellow", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_NODE_YELLOW.get(), new Item.Properties()));
    public static final RegistryEntry<Item> LUMINESCENT_WAX_NODE_WHITE = ITEMS.register("luminescent_wax_node_white", () -> new BlockItem(BzBlocks.LUMINESCENT_WAX_NODE_WHITE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> HONEY_COCOON = ITEMS.register("honey_cocoon", () -> new BzBlockItem(BzBlocks.HONEY_COCOON.get(), new Item.Properties(), false, true));
    public static final RegistryEntry<Item> CRYSTALLINE_FLOWER = ITEMS.register("crystalline_flower", () -> new CrystallineFlowerBlockItem(BzBlocks.CRYSTALLINE_FLOWER.get(), new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON), true, true));
    public static final RegistryEntry<Item> PILE_OF_POLLEN = ITEMS.register("pile_of_pollen", () -> new BzBlockItem(BzBlocks.PILE_OF_POLLEN.get().defaultBlockState().setValue(PileOfPollen.LAYERS, 8), new Item.Properties()));
    public static final RegistryEntry<Item> PILE_OF_POLLEN_SUSPICIOUS = ITEMS.register("pile_of_pollen_suspicious", () -> new BlockItem(BzBlocks.PILE_OF_POLLEN_SUSPICIOUS.get(), new Item.Properties()));
    public static final RegistryEntry<Item> SUGAR_INFUSED_STONE = ITEMS.register("sugar_infused_stone", () -> new BlockItem(BzBlocks.SUGAR_INFUSED_STONE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> SUGAR_INFUSED_COBBLESTONE = ITEMS.register("sugar_infused_cobblestone", () -> new BlockItem(BzBlocks.SUGAR_INFUSED_COBBLESTONE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> POTION_CANDLE = ITEMS.register("potion_candle", () -> new PotionCandleBlockItem(BzBlocks.POTION_BASE_CANDLE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> SUPER_CANDLE = ITEMS.register("super_candle", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE.get(), new Item.Properties(), true, false));
    public static final RegistryEntry<Item> SUPER_CANDLE_WHITE = ITEMS.register("super_candle_white", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_WHITE.get(), new Item.Properties(), true, false));
    public static final RegistryEntry<Item> SUPER_CANDLE_LIGHT_GRAY = ITEMS.register("super_candle_light_gray", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_LIGHT_GRAY.get(), new Item.Properties(), true, false));
    public static final RegistryEntry<Item> SUPER_CANDLE_GRAY = ITEMS.register("super_candle_gray", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_GRAY.get(), new Item.Properties(), true, false));
    public static final RegistryEntry<Item> SUPER_CANDLE_BLACK = ITEMS.register("super_candle_black", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_BLACK.get(), new Item.Properties(), true, false));
    public static final RegistryEntry<Item> SUPER_CANDLE_BROWN = ITEMS.register("super_candle_brown", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_BROWN.get(), new Item.Properties(), true, false));
    public static final RegistryEntry<Item> SUPER_CANDLE_RED = ITEMS.register("super_candle_red", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_RED.get(), new Item.Properties(), true, false));
    public static final RegistryEntry<Item> SUPER_CANDLE_ORANGE = ITEMS.register("super_candle_orange", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_ORANGE.get(), new Item.Properties(), true, false));
    public static final RegistryEntry<Item> SUPER_CANDLE_YELLOW = ITEMS.register("super_candle_yellow", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_YELLOW.get(), new Item.Properties(), true, false));
    public static final RegistryEntry<Item> SUPER_CANDLE_LIME = ITEMS.register("super_candle_lime", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_LIME.get(), new Item.Properties(), true, false));
    public static final RegistryEntry<Item> SUPER_CANDLE_GREEN = ITEMS.register("super_candle_green", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_GREEN.get(), new Item.Properties(), true, false));
    public static final RegistryEntry<Item> SUPER_CANDLE_CYAN = ITEMS.register("super_candle_cyan", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_CYAN.get(), new Item.Properties(), true, false));
    public static final RegistryEntry<Item> SUPER_CANDLE_LIGHT_BLUE = ITEMS.register("super_candle_light_blue", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_LIGHT_BLUE.get(), new Item.Properties(), true, false));
    public static final RegistryEntry<Item> SUPER_CANDLE_BLUE = ITEMS.register("super_candle_blue", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_BLUE.get(), new Item.Properties(), true, false));
    public static final RegistryEntry<Item> SUPER_CANDLE_PURPLE = ITEMS.register("super_candle_purple", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_PURPLE.get(), new Item.Properties(), true, false));
    public static final RegistryEntry<Item> SUPER_CANDLE_MAGENTA = ITEMS.register("super_candle_magenta", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_MAGENTA.get(), new Item.Properties(), true, false));
    public static final RegistryEntry<Item> SUPER_CANDLE_PINK = ITEMS.register("super_candle_pink", () -> new BzBlockItem(BzBlocks.SUPER_CANDLE_BASE_PINK.get(), new Item.Properties(), true, false));
    public static final RegistryEntry<Item> STRING_CURTAIN_WHITE = ITEMS.register("string_curtain_white", () -> new BlockItem(BzBlocks.STRING_CURTAIN_WHITE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> STRING_CURTAIN_LIGHT_GRAY = ITEMS.register("string_curtain_light_gray", () -> new BlockItem(BzBlocks.STRING_CURTAIN_LIGHT_GRAY.get(), new Item.Properties()));
    public static final RegistryEntry<Item> STRING_CURTAIN_GRAY = ITEMS.register("string_curtain_gray", () -> new BlockItem(BzBlocks.STRING_CURTAIN_GRAY.get(), new Item.Properties()));
    public static final RegistryEntry<Item> STRING_CURTAIN_BLACK = ITEMS.register("string_curtain_black", () -> new BlockItem(BzBlocks.STRING_CURTAIN_BLACK.get(), new Item.Properties()));
    public static final RegistryEntry<Item> STRING_CURTAIN_BROWN = ITEMS.register("string_curtain_brown", () -> new BlockItem(BzBlocks.STRING_CURTAIN_BROWN.get(), new Item.Properties()));
    public static final RegistryEntry<Item> STRING_CURTAIN_RED = ITEMS.register("string_curtain_red", () -> new BlockItem(BzBlocks.STRING_CURTAIN_RED.get(), new Item.Properties()));
    public static final RegistryEntry<Item> STRING_CURTAIN_ORANGE = ITEMS.register("string_curtain_orange", () -> new BlockItem(BzBlocks.STRING_CURTAIN_ORANGE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> STRING_CURTAIN_YELLOW = ITEMS.register("string_curtain_yellow", () -> new BlockItem(BzBlocks.STRING_CURTAIN_YELLOW.get(), new Item.Properties()));
    public static final RegistryEntry<Item> STRING_CURTAIN_LIME = ITEMS.register("string_curtain_lime", () -> new BlockItem(BzBlocks.STRING_CURTAIN_LIME.get(), new Item.Properties()));
    public static final RegistryEntry<Item> STRING_CURTAIN_GREEN = ITEMS.register("string_curtain_green", () -> new BlockItem(BzBlocks.STRING_CURTAIN_GREEN.get(), new Item.Properties()));
    public static final RegistryEntry<Item> STRING_CURTAIN_CYAN = ITEMS.register("string_curtain_cyan", () -> new BlockItem(BzBlocks.STRING_CURTAIN_CYAN.get(), new Item.Properties()));
    public static final RegistryEntry<Item> STRING_CURTAIN_LIGHT_BLUE = ITEMS.register("string_curtain_light_blue", () -> new BlockItem(BzBlocks.STRING_CURTAIN_LIGHT_BLUE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> STRING_CURTAIN_BLUE = ITEMS.register("string_curtain_blue", () -> new BlockItem(BzBlocks.STRING_CURTAIN_BLUE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> STRING_CURTAIN_PURPLE = ITEMS.register("string_curtain_purple", () -> new BlockItem(BzBlocks.STRING_CURTAIN_PURPLE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> STRING_CURTAIN_MAGENTA = ITEMS.register("string_curtain_magenta", () -> new BlockItem(BzBlocks.STRING_CURTAIN_MAGENTA.get(), new Item.Properties()));
    public static final RegistryEntry<Item> STRING_CURTAIN_PINK = ITEMS.register("string_curtain_pink", () -> new BlockItem(BzBlocks.STRING_CURTAIN_PINK.get(), new Item.Properties()));
    public static final RegistryEntry<Item> ROYAL_JELLY_BLOCK = ITEMS.register("royal_jelly_block", () -> new BlockItem(BzBlocks.ROYAL_JELLY_BLOCK.get(), new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryEntry<Item> HEAVY_AIR = ITEMS.register("heavy_air", () -> new BlockItem(BzBlocks.HEAVY_AIR.get(), new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryEntry<Item> WINDY_AIR = ITEMS.register("windy_air", () -> new BlockItem(BzBlocks.WINDY_AIR.get(), new Item.Properties().rarity(Rarity.EPIC)));

    //items
    public static final RegistryEntry<Item> ROYAL_JELLY_BUCKET = ITEMS.register("royal_jelly_bucket", () -> new BzCustomBucketItem(BzFluids.ROYAL_JELLY_FLUID_TYPE.get(), new Item.Properties().rarity(Rarity.EPIC).craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryEntry<Item> ROYAL_JELLY_BOTTLE = ITEMS.register("royal_jelly_bottle", () -> new RoyalJellyBottle((new Item.Properties().rarity(Rarity.EPIC)).craftRemainder(Items.GLASS_BOTTLE).food(BzFoodProperties.builder(12, 1.0F).effect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, 1200, 0), 1.0F).effect(() -> new MobEffectInstance(MobEffects.JUMP, 1200, 3), 1.0F).effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200, 1), 1.0F).effect(() -> new MobEffectInstance(BzEffects.BEENERGIZED.get(), 1200, 1), 1.0F).build()).stacksTo(16)));
    public static final RegistryEntry<Item> HONEY_BUCKET = ITEMS.register("honey_bucket", () -> new BzBucketItem(BzFluids.HONEY_FLUID_TYPE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryEntry<Item> SUGAR_WATER_BUCKET = ITEMS.register("sugar_water_bucket", () -> new BzCustomBucketItem(BzFluids.SUGAR_WATER_FLUID_TYPE.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    public static final RegistryEntry<Item> SUGAR_WATER_BOTTLE = ITEMS.register("sugar_water_bottle", () -> new SugarWaterBottle((new Item.Properties()).craftRemainder(Items.GLASS_BOTTLE).food(BzFoodProperties.builder(1, 0.05F).effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 600, 0), 1.0F).build()).stacksTo(16)));
    public static final RegistryEntry<Item> HONEY_COMPASS = ITEMS.register("honey_compass", () -> new HoneyCompass(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryEntry<Item> BEE_BREAD = ITEMS.register("bee_bread", () -> new BeeBread(new Item.Properties().food(BzFoodProperties.builder(8, 0.12F).alwaysEat().effect(() -> new MobEffectInstance(BzEffects.BEENERGIZED.get(), 6000, 0), 1.0F).effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 120, 1), 1.0F).build())));
    public static final RegistryEntry<Item> BEE_SOUP = ITEMS.register("bee_soup", () -> new FoodBowlItem(new Item.Properties().craftRemainder(Items.BOWL).food(BzFoodProperties.builder(15, 24F).effect(() -> new MobEffectInstance(BzEffects.BEENERGIZED.get(), 12000, 1), 1.0F).effect(() -> new MobEffectInstance(MobEffects.LEVITATION, 800, 0), 0.2F).effect(() -> new MobEffectInstance(MobEffects.POISON, 800, 0), 0.2F).effect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, 6000, 0), 0.2F).effect(() -> new MobEffectInstance(MobEffects.LUCK, 18000, 0), 0.2F).effect(() -> new MobEffectInstance(BzEffects.PARALYZED.get(), 200, 0), 0.2F).build())));
    public static final RegistryEntry<Item> BUZZING_BRIEFCASE = ITEMS.register("buzzing_briefcase", () -> new BuzzingBriefcase(new Item.Properties().stacksTo(1)));
    public static final RegistryEntry<Item> POLLEN_PUFF = ITEMS.register("pollen_puff", () -> new PollenPuff(new Item.Properties().stacksTo(16)));
    public static final RegistryEntry<Item> DIRT_PELLET = ITEMS.register("dirt_pellet", () -> new DirtPellet(new Item.Properties().stacksTo(16)));
    public static final RegistryEntry<HoneyCrystalShards> HONEY_CRYSTAL_SHARDS = ITEMS.register("honey_crystal_shards", () -> new HoneyCrystalShards(new Item.Properties().food((new FoodProperties.Builder()).nutrition(2).saturationMod(0.15F).build())));
    public static final RegistryEntry<Item> BEE_STINGER = ITEMS.register("bee_stinger", () -> new BeeStinger(new Item.Properties()));
    public static final RegistryEntry<Item> STINGER_SPEAR = ITEMS.register("stinger_spear", () -> new StingerSpearItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryEntry<Item> BEE_CANNON = ITEMS.register("bee_cannon", () -> new BeeCannon(new Item.Properties().stacksTo(1)));
    public static final RegistryEntry<Item> CRYSTAL_CANNON = ITEMS.register("crystal_cannon", () -> new CrystalCannon(new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final RegistryEntry<Item> HONEY_CRYSTAL_SHIELD = ITEMS.register("honey_crystal_shield", () -> new HoneyCrystalShield(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryEntry<Item> FLOWER_HEADWEAR = ITEMS.register("flower_headwear", () -> new FlowerHeadwearHelmet(ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryEntry<Item> STINGLESS_BEE_HELMET_1 = ITEMS.register("stingless_bee_helmet_1", () -> new StinglessBeeHelmet(BeeArmorMaterial.BEE_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties().rarity(Rarity.UNCOMMON), 1));
    public static final RegistryEntry<Item> STINGLESS_BEE_HELMET_2 = ITEMS.register("stingless_bee_helmet_2", () -> new StinglessBeeHelmet(BeeArmorMaterial.BEE_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties().rarity(Rarity.UNCOMMON), 2));
    public static final RegistryEntry<Item> BUMBLE_BEE_CHESTPLATE_1 = ITEMS.register("bumble_bee_chestplate_1", () -> new BumbleBeeChestplate(BeeArmorMaterial.BEE_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(Rarity.UNCOMMON), false, 1));
    public static final RegistryEntry<Item> BUMBLE_BEE_CHESTPLATE_2 = ITEMS.register("bumble_bee_chestplate_2", () -> new BumbleBeeChestplate(BeeArmorMaterial.BEE_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(Rarity.UNCOMMON), false, 2));
    public static final RegistryEntry<Item> TRANS_BUMBLE_BEE_CHESTPLATE_1 = ITEMS.register("bumble_bee_chestplate_trans_1", () -> new BumbleBeeChestplate(BeeArmorMaterial.BEE_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(Rarity.UNCOMMON), true, 1));
    public static final RegistryEntry<Item> TRANS_BUMBLE_BEE_CHESTPLATE_2 = ITEMS.register("bumble_bee_chestplate_trans_2", () -> new BumbleBeeChestplate(BeeArmorMaterial.BEE_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(Rarity.UNCOMMON), true, 2));
    public static final RegistryEntry<Item> HONEY_BEE_LEGGINGS_1 = ITEMS.register("honey_bee_leggings_1", () -> new HoneyBeeLeggings(BeeArmorMaterial.BEE_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties().rarity(Rarity.UNCOMMON), 1));
    public static final RegistryEntry<Item> HONEY_BEE_LEGGINGS_2 = ITEMS.register("honey_bee_leggings_2", () -> new HoneyBeeLeggings(BeeArmorMaterial.BEE_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties().rarity(Rarity.UNCOMMON), 2));
    public static final RegistryEntry<Item> CARPENTER_BEE_BOOTS_1 = ITEMS.register("carpenter_bee_boots_1", () -> new CarpenterBeeBoots(BeeArmorMaterial.BEE_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties().rarity(Rarity.UNCOMMON), 1));
    public static final RegistryEntry<Item> CARPENTER_BEE_BOOTS_2 = ITEMS.register("carpenter_bee_boots_2", () -> new CarpenterBeeBoots(BeeArmorMaterial.BEE_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties().rarity(Rarity.UNCOMMON), 2));
    public static final RegistryEntry<Item> BANNER_PATTERN_BEE = ITEMS.register("banner_pattern_bee", () -> new BannerPatternItem(BzTags.PATTERN_ITEM_BEE, new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final RegistryEntry<Item> BANNER_PATTERN_HONEYCOMBS = ITEMS.register("banner_pattern_honeycombs", () -> new BannerPatternItem(BzTags.PATTERN_ITEM_HONEYCOMBS, new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final RegistryEntry<Item> BANNER_PATTERN_SWORDS = ITEMS.register("banner_pattern_swords", () -> new BannerPatternItem(BzTags.PATTERN_ITEM_SWORDS, new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final RegistryEntry<Item> BANNER_PATTERN_SUN = ITEMS.register("banner_pattern_sun", () -> new BannerPatternItem(BzTags.PATTERN_ITEM_SUN, new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final RegistryEntry<Item> BANNER_PATTERN_PLUSES = ITEMS.register("banner_pattern_pluses", () -> new BannerPatternItem(BzTags.PATTERN_ITEM_PLUSES, new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final RegistryEntry<Item> BANNER_PATTERN_EYES = ITEMS.register("banner_pattern_eyes", () -> new BannerPatternItem(BzTags.PATTERN_ITEM_EYES, new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final RegistryEntry<Item> BANNER_PATTERN_PEACE = ITEMS.register("banner_pattern_peace", () -> new BannerPatternItem(BzTags.PATTERN_ITEM_PEACE, new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final RegistryEntry<Item> BANNER_PATTERN_ARROWS = ITEMS.register("banner_pattern_arrows", () -> new BannerPatternItem(BzTags.PATTERN_ITEM_ARROWS, new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
    public static final RegistryEntry<Item> MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV = ITEMS.register("music_disc_flight_of_the_bumblebee_rimsky_korsakov", () -> new BzMusicDiscs(14, BzSounds.MUSIC_DISC_FLIGHT_OF_THE_BUMBLEBEE_RIMSKY_KORSAKOV, (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), () -> BzGeneralConfigs.musicDiscTimeLengthFlightOfTheBumblebee, false));
    public static final RegistryEntry<Item> MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY = ITEMS.register("music_disc_honey_bee_rat_faced_boy", () -> new BzMusicDiscs(15, BzSounds.MUSIC_DISC_HONEY_BEE_RAT_FACED_BOY, (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), () -> BzGeneralConfigs.musicDiscTimeLengthHoneyBee, true));
    public static final RegistryEntry<Item> MUSIC_DISC_LA_BEE_DA_LOCA = ITEMS.register("music_disc_la_bee_da_loca", () -> new BzMusicDiscs(13, BzSounds.MUSIC_DISC_LA_BEE_DA_LOCA, (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), () -> BzGeneralConfigs.musicDiscTimeLengthLaBeeDaLoca, true));
    public static final RegistryEntry<Item> MUSIC_DISC_BEE_LAXING_WITH_THE_HOM_BEES = ITEMS.register("music_disc_bee_laxing_with_the_hom_bees", () -> new BzMusicDiscs(12, BzSounds.MUSIC_DISC_BEE_LAXING_WITH_THE_HOM_BEES, (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), () -> BzGeneralConfigs.musicDiscTimeLengthBeeLaxingWithTheHomBees, true));
    public static final RegistryEntry<Item> MUSIC_DISC_BEE_WARE_OF_THE_TEMPLE = ITEMS.register("music_disc_bee_ware_of_the_temple", () -> new BzMusicDiscs(12, BzSounds.MUSIC_DISC_BEE_WARE_OF_THE_TEMPLE, (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), () -> BzGeneralConfigs.musicDiscTimeLengthBeeWareOfTheTemple, true));
    public static final RegistryEntry<Item> HONEY_SLIME_SPAWN_EGG = ITEMS.register("honey_slime_spawn_egg", () -> new DispenserAddedSpawnEgg(BzEntities.HONEY_SLIME, 0xFFCC00, 0xFCA800, (new Item.Properties())));
    public static final RegistryEntry<Item> VARIANT_BEE_SPAWN_EGG = ITEMS.register("variant_bee_spawn_egg", () -> new DispenserAddedSpawnEgg(BzEntities.VARIANT_BEE, /*Prevent item tinting*/ 0xFFFFFF, 0xFFFFFF,(new Item.Properties())));
    public static final RegistryEntry<Item> BEEHEMOTH_SPAWN_EGG = ITEMS.register("beehemoth_spawn_egg", () -> new DispenserAddedSpawnEgg(BzEntities.BEEHEMOTH, 0xFFCA47, 0x68372A, (new Item.Properties())));
    public static final RegistryEntry<Item> BEE_QUEEN_SPAWN_EGG = ITEMS.register("bee_queen_spawn_egg", () -> new DispenserAddedSpawnEgg(BzEntities.BEE_QUEEN, /*Prevent item tinting*/ 0xFFFFFF, 0xFFFFFF,(new Item.Properties().rarity(Rarity.EPIC))));
    public static final RegistryEntry<Item> ROOTMIN_SPAWN_EGG = ITEMS.register("rootmin_spawn_egg", () -> new DispenserAddedSpawnEgg(BzEntities.ROOTMIN, /*Prevent item tinting*/ 0xFFFFFF, 0xFFFFFF, (new Item.Properties())));
    public static final RegistryEntry<Item> SENTRY_WATCHER_SPAWN_EGG = ITEMS.register("sentry_watcher_spawn_egg", () -> new SentryWatcherSpawnEgg(BzEntities.SENTRY_WATCHER, (new Item.Properties().rarity(Rarity.RARE))));
    public static final RegistryEntry<Item> ESSENCE_OF_THE_BEES = ITEMS.register("essence_of_the_bees", () -> new EssenceOfTheBees(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).fireResistant()));
    public static final RegistryEntry<Item> ESSENCE_RAGING = ITEMS.register("essence_raging", () -> new RagingEssence(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryEntry<Item> ESSENCE_KNOWING = ITEMS.register("essence_knowing", () -> new KnowingEssence(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryEntry<Item> ESSENCE_CALMING = ITEMS.register("essence_calming", () -> new CalmingEssence(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryEntry<Item> ESSENCE_LIFE = ITEMS.register("essence_life", () -> new LifeEssence(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryEntry<Item> ESSENCE_RADIANCE = ITEMS.register("essence_radiance", () -> new RadianceEssence(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));
    public static final RegistryEntry<Item> ESSENCE_CONTINUITY = ITEMS.register("essence_continuity", () -> new ContinuityEssence(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant()));

    public static void SetupCauldronCompat() {
        CauldronInteraction.WATER.put(BzItems.FLOWER_HEADWEAR.get(), CauldronInteraction.DYED_ITEM);
    }
}