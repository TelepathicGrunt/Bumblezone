package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.HoneyCocoonBlockEntity;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.SuperIncenseCandleBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class BzBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Bumblezone.MODID);

    //Blocks
    public static final RegistryObject<BlockEntityType<?>> HONEY_COCOON = BLOCK_ENTITIES.register("honey_cocoon", () -> BlockEntityType.Builder.of(HoneyCocoonBlockEntity::new, BzBlocks.HONEY_COCOON.get()).build(null));
    public static final RegistryObject<BlockEntityType<?>> SUPER_INCENSE_CANDLE = BLOCK_ENTITIES.register("super_incense_candle", () -> BlockEntityType.Builder.of(SuperIncenseCandleBlockEntity::new, BzBlocks.SUPER_INCENSE_BASE_CANDLE.get()).build(null));
}