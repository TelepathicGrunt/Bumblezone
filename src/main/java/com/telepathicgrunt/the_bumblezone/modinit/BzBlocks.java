package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricMaterialBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;


public class BzBlocks {
    public static Material RED_NOT_SOLID = new FabricMaterialBuilder(MaterialColor.TERRACOTTA_RED)
            .notSolid()
            .destroyOnPush()
            .noCollider()
            .replaceable()
            .nonSolid()
            .build();
    public static Material ORANGE_NOT_SOLID = new FabricMaterialBuilder(MaterialColor.TERRACOTTA_ORANGE)
            .notSolid()
            .destroyOnPush()
            .noCollider()
            .replaceable()
            .nonSolid()
            .build();
    public static Material YELLOW_NOT_SOLID = new FabricMaterialBuilder(MaterialColor.COLOR_YELLOW)
            .notSolid()
            .destroyOnPush()
            .noCollider()
            .replaceable()
            .nonSolid()
            .build();
    public static Material YELLOW_CRYSTAL_PLANT = new FabricMaterialBuilder(MaterialColor.TERRACOTTA_YELLOW)
            .notSolid()
            .blocksPistons()
            .noCollider()
            .nonSolid()
            .build();

    public static final Block POROUS_HONEYCOMB = new PorousHoneycomb();
    public static final Block FILLED_POROUS_HONEYCOMB = new FilledPorousHoneycomb();
    public static final Block EMPTY_HONEYCOMB_BROOD = new EmptyHoneycombBrood();
    public static final Block HONEYCOMB_BROOD = new HoneycombBrood();
    public static final Block SUGAR_INFUSED_STONE = new SugarInfusedStone();
    public static final Block SUGAR_INFUSED_COBBLESTONE = new SugarInfusedCobblestone();
    public static final Block HONEY_CRYSTAL = new HoneyCrystal();
    public static final Block STICKY_HONEY_RESIDUE = new StickyHoneyResidue();
    public static final Block STICKY_HONEY_REDSTONE = new StickyHoneyRedstone();
    public static final Block BEEHIVE_BEESWAX = new BeehiveBeeswax();
    public static final Block PILE_OF_POLLEN = new PileOfPollen();
    public static final Block HONEY_WEB = new HoneyWeb();
    public static final Block REDSTONE_HONEY_WEB = new RedstoneHoneyWeb();
    public static final Block HONEY_COCOON = new HoneyCocoon();
    public static final Block ROYAL_JELLY_BLOCK = new RoyalJellyBlock();
    public static final Block GLISTERING_HONEY_CRYSTAL = new GlisteringHoneyCrystal();
    public static final Block CARVABLE_WAX = new CarvableWax();
    public static final Block SUPER_CANDLE_BASE = new SuperCandleBase();
    public static final Block SUPER_CANDLE_WICK = new SuperCandleWick(false);
    public static final Block SUPER_CANDLE_WICK_SOUL = new SuperCandleWick(true);
    public static final Block SUPER_CANDLE_BASE_BLACK = new SuperCandleBase();
    public static final Block SUPER_CANDLE_BASE_BLUE = new SuperCandleBase();
    public static final Block SUPER_CANDLE_BASE_BROWN = new SuperCandleBase();
    public static final Block SUPER_CANDLE_BASE_CYAN = new SuperCandleBase();
    public static final Block SUPER_CANDLE_BASE_GRAY = new SuperCandleBase();
    public static final Block SUPER_CANDLE_BASE_GREEN = new SuperCandleBase();
    public static final Block SUPER_CANDLE_BASE_LIGHT_BLUE = new SuperCandleBase();
    public static final Block SUPER_CANDLE_BASE_LIGHT_GRAY = new SuperCandleBase();
    public static final Block SUPER_CANDLE_BASE_LIME = new SuperCandleBase();
    public static final Block SUPER_CANDLE_BASE_MAGENTA = new SuperCandleBase();
    public static final Block SUPER_CANDLE_BASE_ORANGE = new SuperCandleBase();
    public static final Block SUPER_CANDLE_BASE_PINK = new SuperCandleBase();
    public static final Block SUPER_CANDLE_BASE_PURPLE = new SuperCandleBase();
    public static final Block SUPER_CANDLE_BASE_RED = new SuperCandleBase();
    public static final Block SUPER_CANDLE_BASE_WHITE = new SuperCandleBase();
    public static final Block SUPER_CANDLE_BASE_YELLOW = new SuperCandleBase();
    public static final Block INCENSE_BASE_CANDLE = new IncenseCandleBase();
    public static final Block CRYSTALLINE_FLOWER = new CrystallineFlower();
    public static final Block STRING_CURTAIN_BLACK = new StringCurtain();
    public static final Block STRING_CURTAIN_BLUE = new StringCurtain();
    public static final Block STRING_CURTAIN_BROWN = new StringCurtain();
    public static final Block STRING_CURTAIN_CYAN = new StringCurtain();
    public static final Block STRING_CURTAIN_GRAY = new StringCurtain();
    public static final Block STRING_CURTAIN_GREEN = new StringCurtain();
    public static final Block STRING_CURTAIN_LIGHT_BLUE = new StringCurtain();
    public static final Block STRING_CURTAIN_LIGHT_GRAY = new StringCurtain();
    public static final Block STRING_CURTAIN_LIME = new StringCurtain();
    public static final Block STRING_CURTAIN_MAGENTA = new StringCurtain();
    public static final Block STRING_CURTAIN_ORANGE = new StringCurtain();
    public static final Block STRING_CURTAIN_PINK = new StringCurtain();
    public static final Block STRING_CURTAIN_PURPLE = new StringCurtain();
    public static final Block STRING_CURTAIN_RED = new StringCurtain();
    public static final Block STRING_CURTAIN_WHITE = new StringCurtain();
    public static final Block STRING_CURTAIN_YELLOW = new StringCurtain();


    /**
     * registers the Blocks so they now exist in the registry
     */
    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "porous_honeycomb_block"), POROUS_HONEYCOMB);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "filled_porous_honeycomb_block"), FILLED_POROUS_HONEYCOMB);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "empty_honeycomb_brood_block"), EMPTY_HONEYCOMB_BROOD);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "honeycomb_brood_block"), HONEYCOMB_BROOD);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "sugar_infused_stone"), SUGAR_INFUSED_STONE);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "sugar_infused_cobblestone"), SUGAR_INFUSED_COBBLESTONE);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "honey_crystal"), HONEY_CRYSTAL);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "sticky_honey_residue"), STICKY_HONEY_RESIDUE);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "sticky_honey_redstone"), STICKY_HONEY_REDSTONE);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "beehive_beeswax"), BEEHIVE_BEESWAX);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "pile_of_pollen"), PILE_OF_POLLEN);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "honey_web"), HONEY_WEB);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "redstone_honey_web"), REDSTONE_HONEY_WEB);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "honey_cocoon"), HONEY_COCOON);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "royal_jelly_block"), ROYAL_JELLY_BLOCK);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "glistering_honey_crystal"), GLISTERING_HONEY_CRYSTAL);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "carvable_wax"), CARVABLE_WAX);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "super_candle_base"), SUPER_CANDLE_BASE);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "super_candle_wick"), SUPER_CANDLE_WICK);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "super_candle_wick_soul"), SUPER_CANDLE_WICK_SOUL);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "super_candle_base_black"), SUPER_CANDLE_BASE_BLACK);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "super_candle_base_blue"), SUPER_CANDLE_BASE_BLUE);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "super_candle_base_brown"), SUPER_CANDLE_BASE_BROWN);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "super_candle_base_cyan"), SUPER_CANDLE_BASE_CYAN);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "super_candle_base_gray"), SUPER_CANDLE_BASE_GRAY);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "super_candle_base_green"), SUPER_CANDLE_BASE_GREEN);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "super_candle_base_light_blue"), SUPER_CANDLE_BASE_LIGHT_BLUE);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "super_candle_base_light_gray"), SUPER_CANDLE_BASE_LIGHT_GRAY);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "super_candle_base_lime"), SUPER_CANDLE_BASE_LIME);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "super_candle_base_magenta"), SUPER_CANDLE_BASE_MAGENTA);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "super_candle_base_orange"), SUPER_CANDLE_BASE_ORANGE);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "super_candle_base_pink"), SUPER_CANDLE_BASE_PINK);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "super_candle_base_purple"), SUPER_CANDLE_BASE_PURPLE);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "super_candle_base_red"), SUPER_CANDLE_BASE_RED);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "super_candle_base_white"), SUPER_CANDLE_BASE_WHITE);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "super_candle_base_yellow"), SUPER_CANDLE_BASE_YELLOW);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "incense_candle_base"), INCENSE_BASE_CANDLE);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "crystalline_flower"), CRYSTALLINE_FLOWER);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "string_curtain_black"), STRING_CURTAIN_BLACK);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "string_curtain_blue"), STRING_CURTAIN_BLUE);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "string_curtain_brown"), STRING_CURTAIN_BROWN);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "string_curtain_cyan"), STRING_CURTAIN_CYAN);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "string_curtain_gray"), STRING_CURTAIN_GRAY);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "string_curtain_green"), STRING_CURTAIN_GREEN);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "string_curtain_light_blue"), STRING_CURTAIN_LIGHT_BLUE);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "string_curtain_light_gray"), STRING_CURTAIN_LIGHT_GRAY);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "string_curtain_lime"), STRING_CURTAIN_LIME);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "string_curtain_magenta"), STRING_CURTAIN_MAGENTA);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "string_curtain_orange"), STRING_CURTAIN_ORANGE);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "string_curtain_pink"), STRING_CURTAIN_PINK);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "string_curtain_purple"), STRING_CURTAIN_PURPLE);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "string_curtain_red"), STRING_CURTAIN_RED);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "string_curtain_white"), STRING_CURTAIN_WHITE);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "string_curtain_yellow"), STRING_CURTAIN_YELLOW);

        StringCurtain.setupStringCurtainbehaviors();
    }
}