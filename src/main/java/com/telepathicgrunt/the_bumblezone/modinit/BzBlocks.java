package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.BeehiveBeeswax;
import com.telepathicgrunt.the_bumblezone.blocks.EmptyHoneycombBrood;
import com.telepathicgrunt.the_bumblezone.blocks.FilledPorousHoneycomb;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyCocoon;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyCrystal;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyWeb;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.blocks.PorousHoneycomb;
import com.telepathicgrunt.the_bumblezone.blocks.RedstoneHoneyWeb;
import com.telepathicgrunt.the_bumblezone.blocks.StickyHoneyRedstone;
import com.telepathicgrunt.the_bumblezone.blocks.StickyHoneyResidue;
import com.telepathicgrunt.the_bumblezone.blocks.SugarInfusedCobblestone;
import com.telepathicgrunt.the_bumblezone.blocks.SugarInfusedStone;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.quiltmc.qsl.block.extensions.api.QuiltMaterialBuilder;


public class BzBlocks {
    public static Material ORANGE_NOT_SOLID = new QuiltMaterialBuilder(MaterialColor.TERRACOTTA_ORANGE)
            .notSolid()
            .blocksPistons()
            .noCollider()
            .replaceable()
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
    }
}