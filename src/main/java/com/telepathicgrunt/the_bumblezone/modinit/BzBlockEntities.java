package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.GlazedCocoonBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class BzBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Bumblezone.MODID);

    //Blocks
    public static final RegistryObject<BlockEntityType<?>> GLAZED_COCOON_BE = BLOCK_ENTITIES.register("glazed_cocoon", () -> BlockEntityType.Builder.of(GlazedCocoonBlockEntity::new, BzBlocks.GLAZED_COCOON.get()).build(null));
}