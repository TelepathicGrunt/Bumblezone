package com.telepathicgrunt.bumblezone.modinit;

import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.blocks.BeeswaxPlanks;
import com.telepathicgrunt.bumblezone.blocks.EmptyHoneycombBrood;
import com.telepathicgrunt.bumblezone.blocks.FilledPorousHoneycomb;
import com.telepathicgrunt.bumblezone.blocks.HoneyCrystal;
import com.telepathicgrunt.bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.bumblezone.blocks.PorousHoneycomb;
import com.telepathicgrunt.bumblezone.blocks.StickyHoneyRedstone;
import com.telepathicgrunt.bumblezone.blocks.StickyHoneyResidue;
import com.telepathicgrunt.bumblezone.blocks.SugarInfusedCobblestone;
import com.telepathicgrunt.bumblezone.blocks.SugarInfusedStone;
import net.fabricmc.fabric.api.block.FabricMaterialBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;


public class BzBlocks {
    public static Material ORANGE_NOT_SOLID = new FabricMaterialBuilder(MaterialColor.TERRACOTTA_ORANGE)
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
    public static final Block BEESWAX_PLANKS = new BeeswaxPlanks();
    public static final Block PILE_OF_POLLEN = new PileOfPollen();

    /**
     * registers the Blocks so they now exist in the registry
     */
    public static void registerBlocks() {
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "porous_honeycomb_block"), POROUS_HONEYCOMB);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "filled_porous_honeycomb_block"), FILLED_POROUS_HONEYCOMB);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "dead_honeycomb_larva_block"), EMPTY_HONEYCOMB_BROOD);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "honeycomb_larva_block"), HONEYCOMB_BROOD);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "sugar_infused_stone"), SUGAR_INFUSED_STONE);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "sugar_infused_cobblestone"), SUGAR_INFUSED_COBBLESTONE);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "honey_crystal"), HONEY_CRYSTAL);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "sticky_honey_residue"), STICKY_HONEY_RESIDUE);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "sticky_honey_redstone"), STICKY_HONEY_REDSTONE);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "beeswax_planks"), BEESWAX_PLANKS);
        Registry.register(Registry.BLOCK, new ResourceLocation(Bumblezone.MODID, "pile_of_pollen"), PILE_OF_POLLEN);
    }
}