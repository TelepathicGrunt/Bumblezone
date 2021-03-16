package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.mixin.items.MaterialInvoker;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;


public class BzBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Bumblezone.MODID);
    public static Material RESIDUE = ((MaterialInvoker) ((MaterialInvoker) new Material.Builder(MaterialColor.ADOBE))
            .bz_getNotOpaque()).bz_getPushDestroys().doesNotBlockMovement().replaceable().notSolid().build();

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
    public static final RegistryObject<Block> BEESWAX_PLANKS = BLOCKS.register("beeswax_planks", BeeswaxPlanks::new);
}