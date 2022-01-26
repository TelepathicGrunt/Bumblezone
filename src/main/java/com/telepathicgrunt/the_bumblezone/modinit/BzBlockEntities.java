package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.BeehiveBeeswax;
import com.telepathicgrunt.the_bumblezone.blocks.EmptyHoneycombBrood;
import com.telepathicgrunt.the_bumblezone.blocks.FilledPorousHoneycomb;
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
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.GlazedCocoonBlockEntity;
import com.telepathicgrunt.the_bumblezone.mixin.items.MaterialInvoker;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class BzBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Bumblezone.MODID);

    //Blocks
    public static final RegistryObject<BlockEntityType<?>> GLAZED_COCOON_BE = BLOCK_ENTITIES.register("glazed_cocoon", () -> BlockEntityType.Builder.of(GlazedCocoonBlockEntity::new, BzBlocks.GLAZED_COCOON.get()).build(null));
}