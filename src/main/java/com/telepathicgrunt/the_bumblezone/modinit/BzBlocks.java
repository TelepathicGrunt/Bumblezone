package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.BeehiveBeeswax;
import com.telepathicgrunt.the_bumblezone.blocks.CandleBase;
import com.telepathicgrunt.the_bumblezone.blocks.CandleWick;
import com.telepathicgrunt.the_bumblezone.blocks.EmptyHoneycombBrood;
import com.telepathicgrunt.the_bumblezone.blocks.FilledPorousHoneycomb;
import com.telepathicgrunt.the_bumblezone.blocks.GlisteringHoneyCrystal;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyCocoon;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyCrystal;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyWeb;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.blocks.PorousHoneycomb;
import com.telepathicgrunt.the_bumblezone.blocks.RedstoneHoneyWeb;
import com.telepathicgrunt.the_bumblezone.blocks.RoyalJellyBlock;
import com.telepathicgrunt.the_bumblezone.blocks.StickyHoneyRedstone;
import com.telepathicgrunt.the_bumblezone.blocks.StickyHoneyResidue;
import com.telepathicgrunt.the_bumblezone.blocks.SugarInfusedCobblestone;
import com.telepathicgrunt.the_bumblezone.blocks.SugarInfusedStone;
import com.telepathicgrunt.the_bumblezone.mixin.items.MaterialInvoker;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class BzBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Bumblezone.MODID);
    public static Material ORANGE_NOT_SOLID = ((MaterialInvoker) ((MaterialInvoker) new Material.Builder(MaterialColor.COLOR_ORANGE))
            .thebumblezone_getNotSolidBlocking()).thebumblezone_getDestroyOnPush().noCollider().replaceable().nonSolid().build();

    //Blocks
    public static final RegistryObject<Block> POROUS_HONEYCOMB = BLOCKS.register("porous_honeycomb_block", PorousHoneycomb::new);
    public static final RegistryObject<Block> FILLED_POROUS_HONEYCOMB = BLOCKS.register("filled_porous_honeycomb_block", FilledPorousHoneycomb::new);
    public static final RegistryObject<Block> EMPTY_HONEYCOMB_BROOD = BLOCKS.register("empty_honeycomb_brood_block", EmptyHoneycombBrood::new);
    public static final RegistryObject<Block> HONEYCOMB_BROOD = BLOCKS.register("honeycomb_brood_block", HoneycombBrood::new);
    public static final RegistryObject<Block> SUGAR_INFUSED_STONE = BLOCKS.register("sugar_infused_stone", SugarInfusedStone::new);
    public static final RegistryObject<Block> SUGAR_INFUSED_COBBLESTONE = BLOCKS.register("sugar_infused_cobblestone", SugarInfusedCobblestone::new);
    public static final RegistryObject<Block> HONEY_CRYSTAL = BLOCKS.register("honey_crystal", HoneyCrystal::new);
    public static final RegistryObject<Block> STICKY_HONEY_RESIDUE = BLOCKS.register("sticky_honey_residue", StickyHoneyResidue::new);
    public static final RegistryObject<Block> STICKY_HONEY_REDSTONE = BLOCKS.register("sticky_honey_redstone", StickyHoneyRedstone::new);
    public static final RegistryObject<Block> BEEHIVE_BEESWAX = BLOCKS.register("beehive_beeswax", BeehiveBeeswax::new);
    public static final RegistryObject<Block> PILE_OF_POLLEN = BLOCKS.register("pile_of_pollen", PileOfPollen::new);
    public static final RegistryObject<Block> HONEY_WEB = BLOCKS.register("honey_web", HoneyWeb::new);
    public static final RegistryObject<Block> REDSTONE_HONEY_WEB = BLOCKS.register("redstone_honey_web", RedstoneHoneyWeb::new);
    public static final RegistryObject<Block> HONEY_COCOON = BLOCKS.register("honey_cocoon", HoneyCocoon::new);
    public static final RegistryObject<Block> ROYAL_JELLY_BLOCK = BLOCKS.register("royal_jelly_block", RoyalJellyBlock::new);
    public static final RegistryObject<Block> GLISTERING_HONEY_CRYSTAL = BLOCKS.register("glistering_honey_crystal", GlisteringHoneyCrystal::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE = BLOCKS.register("super_candle_base", CandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_WICK = BLOCKS.register("super_candle_wick", CandleWick::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_BLACK = BLOCKS.register("super_candle_base_black", CandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_BLUE = BLOCKS.register("super_candle_base_blue", CandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_BROWN = BLOCKS.register("super_candle_base_brown", CandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_CYAN = BLOCKS.register("super_candle_base_cyan", CandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_GRAY = BLOCKS.register("super_candle_base_gray", CandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_GREEN = BLOCKS.register("super_candle_base_green", CandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_LIGHT_BLUE = BLOCKS.register("super_candle_base_light_blue", CandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_LIGHT_GRAY = BLOCKS.register("super_candle_base_light_gray", CandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_LIME = BLOCKS.register("super_candle_base_lime", CandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_MAGENTA = BLOCKS.register("super_candle_base_magenta", CandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_ORANGE = BLOCKS.register("super_candle_base_orange", CandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_PINK = BLOCKS.register("super_candle_base_pink", CandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_PURPLE = BLOCKS.register("super_candle_base_purple", CandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_RED = BLOCKS.register("super_candle_base_red", CandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_WHITE = BLOCKS.register("super_candle_base_white", CandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_YELLOW = BLOCKS.register("super_candle_base_yellow", CandleBase::new);
}