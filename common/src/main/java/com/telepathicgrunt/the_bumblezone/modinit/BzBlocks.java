package com.telepathicgrunt.the_bumblezone.modinit;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.AncientWax;
import com.telepathicgrunt.the_bumblezone.blocks.AncientWaxSlab;
import com.telepathicgrunt.the_bumblezone.blocks.AncientWaxStairs;
import com.telepathicgrunt.the_bumblezone.blocks.BeehiveBeeswax;
import com.telepathicgrunt.the_bumblezone.blocks.CarvableWax;
import com.telepathicgrunt.the_bumblezone.blocks.CrystallineFlower;
import com.telepathicgrunt.the_bumblezone.blocks.DenseBubbleBlock;
import com.telepathicgrunt.the_bumblezone.blocks.EmptyHoneycombBrood;
import com.telepathicgrunt.the_bumblezone.blocks.EssenceBlockBlue;
import com.telepathicgrunt.the_bumblezone.blocks.EssenceBlockGreen;
import com.telepathicgrunt.the_bumblezone.blocks.EssenceBlockPurple;
import com.telepathicgrunt.the_bumblezone.blocks.EssenceBlockRed;
import com.telepathicgrunt.the_bumblezone.blocks.EssenceBlockWhite;
import com.telepathicgrunt.the_bumblezone.blocks.EssenceBlockYellow;
import com.telepathicgrunt.the_bumblezone.blocks.FilledPorousHoneycomb;
import com.telepathicgrunt.the_bumblezone.blocks.GlisteringHoneyCrystal;
import com.telepathicgrunt.the_bumblezone.blocks.HeavyAir;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyCocoon;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyCrystal;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyWeb;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.blocks.InfinityBarrier;
import com.telepathicgrunt.the_bumblezone.blocks.LuminescentWaxChannel;
import com.telepathicgrunt.the_bumblezone.blocks.LuminescentWaxCornerNode;
import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollenSuspicious;
import com.telepathicgrunt.the_bumblezone.blocks.PorousHoneycomb;
import com.telepathicgrunt.the_bumblezone.blocks.PotionCandleBase;
import com.telepathicgrunt.the_bumblezone.blocks.RedstoneHoneyWeb;
import com.telepathicgrunt.the_bumblezone.blocks.RoyalJellyBlock;
import com.telepathicgrunt.the_bumblezone.blocks.StickyHoneyRedstone;
import com.telepathicgrunt.the_bumblezone.blocks.StickyHoneyResidue;
import com.telepathicgrunt.the_bumblezone.blocks.StringCurtain;
import com.telepathicgrunt.the_bumblezone.blocks.SugarInfusedCobblestone;
import com.telepathicgrunt.the_bumblezone.blocks.SugarInfusedStone;
import com.telepathicgrunt.the_bumblezone.blocks.SuperCandleBase;
import com.telepathicgrunt.the_bumblezone.blocks.SuperCandleWick;
import com.telepathicgrunt.the_bumblezone.blocks.WindyAir;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;


public class BzBlocks {
    public static final ResourcefulRegistry<Block> BLOCKS = ResourcefulRegistries.create(BuiltInRegistries.BLOCK, Bumblezone.MODID);
    public static final ResourcefulRegistry<Block> CURTAINS = ResourcefulRegistries.create(BLOCKS);

    //Blocks
    public static final RegistryEntry<Block> POROUS_HONEYCOMB = register("porous_honeycomb_block", PorousHoneycomb::new);
    public static final RegistryEntry<Block> FILLED_POROUS_HONEYCOMB = register("filled_porous_honeycomb_block", FilledPorousHoneycomb::new);
    public static final RegistryEntry<Block> EMPTY_HONEYCOMB_BROOD = register("empty_honeycomb_brood_block", EmptyHoneycombBrood::new);
    public static final RegistryEntry<Block> HONEYCOMB_BROOD = register("honeycomb_brood_block", HoneycombBrood::new);
    public static final RegistryEntry<Block> SUGAR_INFUSED_STONE = register("sugar_infused_stone", SugarInfusedStone::new);
    public static final RegistryEntry<Block> SUGAR_INFUSED_COBBLESTONE = register("sugar_infused_cobblestone", SugarInfusedCobblestone::new);
    public static final RegistryEntry<Block> HONEY_CRYSTAL = register("honey_crystal", HoneyCrystal::new);
    public static final RegistryEntry<Block> STICKY_HONEY_RESIDUE = register("sticky_honey_residue", StickyHoneyResidue::new);
    public static final RegistryEntry<Block> STICKY_HONEY_REDSTONE = register("sticky_honey_redstone", StickyHoneyRedstone::new);
    public static final RegistryEntry<Block> BEEHIVE_BEESWAX = register("beehive_beeswax", BeehiveBeeswax::new);
    public static final RegistryEntry<Block> PILE_OF_POLLEN = register("pile_of_pollen", PileOfPollen::new);
    public static final RegistryEntry<Block> PILE_OF_POLLEN_SUSPICIOUS = register("pile_of_pollen_suspicious", (properties) -> new PileOfPollenSuspicious(null, SoundEvents.BRUSH_SAND, SoundEvents.BRUSH_SAND_COMPLETED, properties));
    public static final RegistryEntry<Block> HONEY_WEB = register("honey_web", HoneyWeb::new);
    public static final RegistryEntry<Block> REDSTONE_HONEY_WEB = register("redstone_honey_web", RedstoneHoneyWeb::new);
    public static final RegistryEntry<Block> HONEY_COCOON = register("honey_cocoon", HoneyCocoon::new);
    public static final RegistryEntry<Block> ROYAL_JELLY_BLOCK = register("royal_jelly_block", RoyalJellyBlock::new);
    public static final RegistryEntry<Block> GLISTERING_HONEY_CRYSTAL = register("glistering_honey_crystal", GlisteringHoneyCrystal::new);
    public static final RegistryEntry<Block> CARVABLE_WAX = register("carvable_wax", CarvableWax::new);
    public static final RegistryEntry<Block> ANCIENT_WAX_BRICKS = register("ancient_wax_bricks", AncientWax::new);
    public static final RegistryEntry<Block> ANCIENT_WAX_DIAMOND = register("ancient_wax_diamond", AncientWax::new);
    public static final RegistryEntry<Block> ANCIENT_WAX_COMPOUND_EYES = register("ancient_wax_compound_eyes", AncientWax::new);
    public static final RegistryEntry<Block> ANCIENT_WAX_BRICKS_STAIRS = register("ancient_wax_bricks_stairs", (properties) -> new AncientWaxStairs(BzBlocks.ANCIENT_WAX_BRICKS.get().defaultBlockState(), properties));
    public static final RegistryEntry<Block> ANCIENT_WAX_DIAMOND_STAIRS = register("ancient_wax_diamond_stairs", (properties) -> new AncientWaxStairs(BzBlocks.ANCIENT_WAX_DIAMOND.get().defaultBlockState(), properties));
    public static final RegistryEntry<Block> ANCIENT_WAX_COMPOUND_EYES_STAIRS = register("ancient_wax_compound_eyes_stairs", (properties) -> new AncientWaxStairs(BzBlocks.ANCIENT_WAX_COMPOUND_EYES.get().defaultBlockState(), properties));
    public static final RegistryEntry<Block> ANCIENT_WAX_BRICKS_SLAB = register("ancient_wax_bricks_slab", AncientWaxSlab::new);
    public static final RegistryEntry<Block> ANCIENT_WAX_DIAMOND_SLAB = register("ancient_wax_diamond_slab", AncientWaxSlab::new);
    public static final RegistryEntry<Block> ANCIENT_WAX_COMPOUND_EYES_SLAB = register("ancient_wax_compound_eyes_slab", AncientWaxSlab::new);
    public static final RegistryEntry<Block> LUMINESCENT_WAX_CHANNEL = register("luminescent_wax_channel", (properties) -> new LuminescentWaxChannel(MapColor.TERRACOTTA_BROWN, 0, properties));
    public static final RegistryEntry<Block> LUMINESCENT_WAX_CHANNEL_RED = register("luminescent_wax_channel_red", (properties) -> new LuminescentWaxChannel(MapColor.COLOR_RED, 15, properties));
    public static final RegistryEntry<Block> LUMINESCENT_WAX_CHANNEL_PURPLE = register("luminescent_wax_channel_purple", (properties) -> new LuminescentWaxChannel(MapColor.COLOR_PURPLE, 15, properties));
    public static final RegistryEntry<Block> LUMINESCENT_WAX_CHANNEL_BLUE = register("luminescent_wax_channel_blue", (properties) -> new LuminescentWaxChannel(MapColor.COLOR_BLUE, 15, properties));
    public static final RegistryEntry<Block> LUMINESCENT_WAX_CHANNEL_GREEN = register("luminescent_wax_channel_green", (properties) -> new LuminescentWaxChannel(MapColor.COLOR_GREEN, 15, properties));
    public static final RegistryEntry<Block> LUMINESCENT_WAX_CHANNEL_YELLOW = register("luminescent_wax_channel_yellow", (properties) -> new LuminescentWaxChannel(MapColor.COLOR_YELLOW, 15, properties));
    public static final RegistryEntry<Block> LUMINESCENT_WAX_CHANNEL_WHITE = register("luminescent_wax_channel_white", (properties) -> new LuminescentWaxChannel(MapColor.SNOW, 15, properties));
    public static final RegistryEntry<Block> LUMINESCENT_WAX_CORNER = register("luminescent_wax_corner", (properties) -> new LuminescentWaxCornerNode(MapColor.TERRACOTTA_BROWN, 0, properties));
    public static final RegistryEntry<Block> LUMINESCENT_WAX_CORNER_RED = register("luminescent_wax_corner_red", (properties) -> new LuminescentWaxCornerNode(MapColor.COLOR_RED, 15, properties));
    public static final RegistryEntry<Block> LUMINESCENT_WAX_CORNER_PURPLE = register("luminescent_wax_corner_purple", (properties) -> new LuminescentWaxCornerNode(MapColor.COLOR_PURPLE, 15, properties));
    public static final RegistryEntry<Block> LUMINESCENT_WAX_CORNER_BLUE = register("luminescent_wax_corner_blue", (properties) -> new LuminescentWaxCornerNode(MapColor.COLOR_BLUE, 15, properties));
    public static final RegistryEntry<Block> LUMINESCENT_WAX_CORNER_GREEN = register("luminescent_wax_corner_green", (properties) -> new LuminescentWaxCornerNode(MapColor.COLOR_GREEN, 15, properties));
    public static final RegistryEntry<Block> LUMINESCENT_WAX_CORNER_YELLOW = register("luminescent_wax_corner_yellow", (properties) -> new LuminescentWaxCornerNode(MapColor.COLOR_YELLOW, 15, properties));
    public static final RegistryEntry<Block> LUMINESCENT_WAX_CORNER_WHITE = register("luminescent_wax_corner_white", (properties) -> new LuminescentWaxCornerNode(MapColor.SNOW, 15, properties));
    public static final RegistryEntry<Block> LUMINESCENT_WAX_NODE = register("luminescent_wax_node", (properties) -> new LuminescentWaxCornerNode(MapColor.TERRACOTTA_BROWN, 0, properties));
    public static final RegistryEntry<Block> LUMINESCENT_WAX_NODE_RED = register("luminescent_wax_node_red", (properties) -> new LuminescentWaxCornerNode(MapColor.COLOR_RED, 15, properties));
    public static final RegistryEntry<Block> LUMINESCENT_WAX_NODE_PURPLE = register("luminescent_wax_node_purple", (properties) -> new LuminescentWaxCornerNode(MapColor.COLOR_PURPLE, 15, properties));
    public static final RegistryEntry<Block> LUMINESCENT_WAX_NODE_BLUE = register("luminescent_wax_node_blue", (properties) -> new LuminescentWaxCornerNode(MapColor.COLOR_BLUE, 15, properties));
    public static final RegistryEntry<Block> LUMINESCENT_WAX_NODE_GREEN = register("luminescent_wax_node_green", (properties) -> new LuminescentWaxCornerNode(MapColor.COLOR_GREEN, 15, properties));
    public static final RegistryEntry<Block> LUMINESCENT_WAX_NODE_YELLOW = register("luminescent_wax_node_yellow", (properties) -> new LuminescentWaxCornerNode(MapColor.COLOR_YELLOW, 15, properties));
    public static final RegistryEntry<Block> LUMINESCENT_WAX_NODE_WHITE = register("luminescent_wax_node_white", (properties) -> new LuminescentWaxCornerNode(MapColor.SNOW, 15, properties));
    public static final RegistryEntry<Block> SUPER_CANDLE_BASE = register("super_candle_base", SuperCandleBase::new);
    public static final RegistryEntry<Block> SUPER_CANDLE_WICK = register("super_candle_wick", (properties) -> new SuperCandleWick(false, properties));
    public static final RegistryEntry<Block> SUPER_CANDLE_WICK_SOUL = register("super_candle_wick_soul", (properties) -> new SuperCandleWick(true, properties));
    public static final RegistryEntry<Block> SUPER_CANDLE_BASE_BLACK = register("super_candle_base_black", SuperCandleBase::new);
    public static final RegistryEntry<Block> SUPER_CANDLE_BASE_BLUE = register("super_candle_base_blue", SuperCandleBase::new);
    public static final RegistryEntry<Block> SUPER_CANDLE_BASE_BROWN = register("super_candle_base_brown", SuperCandleBase::new);
    public static final RegistryEntry<Block> SUPER_CANDLE_BASE_CYAN = register("super_candle_base_cyan", SuperCandleBase::new);
    public static final RegistryEntry<Block> SUPER_CANDLE_BASE_GRAY = register("super_candle_base_gray", SuperCandleBase::new);
    public static final RegistryEntry<Block> SUPER_CANDLE_BASE_GREEN = register("super_candle_base_green", SuperCandleBase::new);
    public static final RegistryEntry<Block> SUPER_CANDLE_BASE_LIGHT_BLUE = register("super_candle_base_light_blue", SuperCandleBase::new);
    public static final RegistryEntry<Block> SUPER_CANDLE_BASE_LIGHT_GRAY = register("super_candle_base_light_gray", SuperCandleBase::new);
    public static final RegistryEntry<Block> SUPER_CANDLE_BASE_LIME = register("super_candle_base_lime", SuperCandleBase::new);
    public static final RegistryEntry<Block> SUPER_CANDLE_BASE_MAGENTA = register("super_candle_base_magenta", SuperCandleBase::new);
    public static final RegistryEntry<Block> SUPER_CANDLE_BASE_ORANGE = register("super_candle_base_orange", SuperCandleBase::new);
    public static final RegistryEntry<Block> SUPER_CANDLE_BASE_PINK = register("super_candle_base_pink", SuperCandleBase::new);
    public static final RegistryEntry<Block> SUPER_CANDLE_BASE_PURPLE = register("super_candle_base_purple", SuperCandleBase::new);
    public static final RegistryEntry<Block> SUPER_CANDLE_BASE_RED = register("super_candle_base_red", SuperCandleBase::new);
    public static final RegistryEntry<Block> SUPER_CANDLE_BASE_WHITE = register("super_candle_base_white", SuperCandleBase::new);
    public static final RegistryEntry<Block> SUPER_CANDLE_BASE_YELLOW = register("super_candle_base_yellow", SuperCandleBase::new);
    public static final RegistryEntry<Block> STRING_CURTAIN_BLACK = registerCurtains("string_curtain_black", StringCurtain::new);
    public static final RegistryEntry<Block> STRING_CURTAIN_BLUE = registerCurtains("string_curtain_blue", StringCurtain::new);
    public static final RegistryEntry<Block> STRING_CURTAIN_BROWN = registerCurtains("string_curtain_brown", StringCurtain::new);
    public static final RegistryEntry<Block> STRING_CURTAIN_CYAN = registerCurtains("string_curtain_cyan", StringCurtain::new);
    public static final RegistryEntry<Block> STRING_CURTAIN_GRAY = registerCurtains("string_curtain_gray", StringCurtain::new);
    public static final RegistryEntry<Block> STRING_CURTAIN_GREEN = registerCurtains("string_curtain_green", StringCurtain::new);
    public static final RegistryEntry<Block> STRING_CURTAIN_LIGHT_BLUE = registerCurtains("string_curtain_light_blue", StringCurtain::new);
    public static final RegistryEntry<Block> STRING_CURTAIN_LIGHT_GRAY = registerCurtains("string_curtain_light_gray", StringCurtain::new);
    public static final RegistryEntry<Block> STRING_CURTAIN_LIME = registerCurtains("string_curtain_lime", StringCurtain::new);
    public static final RegistryEntry<Block> STRING_CURTAIN_MAGENTA = registerCurtains("string_curtain_magenta", StringCurtain::new);
    public static final RegistryEntry<Block> STRING_CURTAIN_ORANGE = registerCurtains("string_curtain_orange", StringCurtain::new);
    public static final RegistryEntry<Block> STRING_CURTAIN_PINK = registerCurtains("string_curtain_pink", StringCurtain::new);
    public static final RegistryEntry<Block> STRING_CURTAIN_PURPLE = registerCurtains("string_curtain_purple", StringCurtain::new);
    public static final RegistryEntry<Block> STRING_CURTAIN_RED = registerCurtains("string_curtain_red", StringCurtain::new);
    public static final RegistryEntry<Block> STRING_CURTAIN_WHITE = registerCurtains("string_curtain_white", StringCurtain::new);
    public static final RegistryEntry<Block> STRING_CURTAIN_YELLOW = registerCurtains("string_curtain_yellow", StringCurtain::new);
    public static final RegistryEntry<Block> POTION_BASE_CANDLE = register("potion_candle_base", PotionCandleBase::new);
    public static final RegistryEntry<Block> CRYSTALLINE_FLOWER = register("crystalline_flower", CrystallineFlower::new);
    public static final RegistryEntry<Block> ESSENCE_BLOCK_RED = register("essence_block_red", EssenceBlockRed::new);
    public static final RegistryEntry<Block> ESSENCE_BLOCK_PURPLE = register("essence_block_purple", EssenceBlockPurple::new);
    public static final RegistryEntry<Block> ESSENCE_BLOCK_BLUE = register("essence_block_blue", EssenceBlockBlue::new);
    public static final RegistryEntry<Block> ESSENCE_BLOCK_GREEN = register("essence_block_green", EssenceBlockGreen::new);
    public static final RegistryEntry<Block> ESSENCE_BLOCK_YELLOW = register("essence_block_yellow", EssenceBlockYellow::new);
    public static final RegistryEntry<Block> ESSENCE_BLOCK_WHITE = register("essence_block_white", EssenceBlockWhite::new);
    public static final RegistryEntry<Block> HEAVY_AIR = register("heavy_air", HeavyAir::new);
    public static final RegistryEntry<Block> WINDY_AIR = register("windy_air", WindyAir::new);
    public static final RegistryEntry<Block> INFINITY_BARRIER = register("infinity_barrier", InfinityBarrier::new);
    public static final RegistryEntry<Block> DENSE_BUBBLE_BLOCK = register("dense_bubble_block", DenseBubbleBlock::new);

    protected static <T extends Block> RegistryEntry<T> register(String id, Function<BlockBehaviour.Properties, T> factory) {
        return BLOCKS.register(
                id,
                () -> factory.apply(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Bumblezone.MODID, id)))));
    }

    private static <T extends Block> RegistryEntry<T> registerCurtains(String id, Function<BlockBehaviour.Properties, T> factory) {
        return CURTAINS.register(
                id,
                () -> factory.apply(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Bumblezone.MODID, id)))));
    }
}