package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.*;
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
    public static Material ORANGE_CRYSTAL_PLANT = ((MaterialInvoker) ((MaterialInvoker) new Material.Builder(MaterialColor.COLOR_ORANGE))
            .thebumblezone_getNotSolidBlocking()).thebumblezone_getNotPushable().noCollider().nonSolid().build();

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
    public static final RegistryObject<Block> CARVABLE_WAX = BLOCKS.register("carvable_wax", CarvableWax::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE = BLOCKS.register("super_candle_base", SuperCandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_WICK = BLOCKS.register("super_candle_wick", () -> new SuperCandleWick(false));
    public static final RegistryObject<Block> SUPER_CANDLE_WICK_SOUL = BLOCKS.register("super_candle_wick_soul", () -> new SuperCandleWick(true));
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_BLACK = BLOCKS.register("super_candle_base_black", SuperCandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_BLUE = BLOCKS.register("super_candle_base_blue", SuperCandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_BROWN = BLOCKS.register("super_candle_base_brown", SuperCandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_CYAN = BLOCKS.register("super_candle_base_cyan", SuperCandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_GRAY = BLOCKS.register("super_candle_base_gray", SuperCandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_GREEN = BLOCKS.register("super_candle_base_green", SuperCandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_LIGHT_BLUE = BLOCKS.register("super_candle_base_light_blue", SuperCandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_LIGHT_GRAY = BLOCKS.register("super_candle_base_light_gray", SuperCandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_LIME = BLOCKS.register("super_candle_base_lime", SuperCandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_MAGENTA = BLOCKS.register("super_candle_base_magenta", SuperCandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_ORANGE = BLOCKS.register("super_candle_base_orange", SuperCandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_PINK = BLOCKS.register("super_candle_base_pink", SuperCandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_PURPLE = BLOCKS.register("super_candle_base_purple", SuperCandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_RED = BLOCKS.register("super_candle_base_red", SuperCandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_WHITE = BLOCKS.register("super_candle_base_white", SuperCandleBase::new);
    public static final RegistryObject<Block> SUPER_CANDLE_BASE_YELLOW = BLOCKS.register("super_candle_base_yellow", SuperCandleBase::new);
    public static final RegistryObject<Block> INCENSE_BASE_CANDLE = BLOCKS.register("incense_candle_base", IncenseCandleBase::new);
    public static final RegistryObject<Block> CRYSTALLINE_FLOWER = BLOCKS.register("crystalline_flower", CrystallineFlower::new);
}