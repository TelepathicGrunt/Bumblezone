package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.CrystallineFlowerBlockEntity;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.HoneyCocoonBlockEntity;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.IncenseCandleBlockEntity;
import com.telepathicgrunt.the_bumblezone.modinit.registry.RegistryEntry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistries;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;


public class BzBlockEntities {
    public static final ResourcefulRegistry<BlockEntityType<?>> BLOCK_ENTITIES = ResourcefulRegistries.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Bumblezone.MODID);

    //Blocks
    public static final RegistryEntry<BlockEntityType<?>> HONEY_COCOON = BLOCK_ENTITIES.register("honey_cocoon", () -> BlockEntityType.Builder.of(HoneyCocoonBlockEntity::new, BzBlocks.HONEY_COCOON.get()).build(null));
    public static final RegistryEntry<BlockEntityType<?>> INCENSE_CANDLE = BLOCK_ENTITIES.register("incense_candle", () -> BlockEntityType.Builder.of(IncenseCandleBlockEntity::new, BzBlocks.INCENSE_BASE_CANDLE.get()).build(null));
    public static final RegistryEntry<BlockEntityType<?>> CRYSTALLINE_FLOWER = BLOCK_ENTITIES.register("crystalline_flower", () -> BlockEntityType.Builder.of(CrystallineFlowerBlockEntity::new, BzBlocks.CRYSTALLINE_FLOWER.get()).build(null));
    public static final RegistryEntry<BlockEntityType<EssenceBlockEntity>> WHITE_ESSENCE_BLOCK = BLOCK_ENTITIES.register("white_essence_block", () -> BlockEntityType.Builder.of(EssenceBlockEntity::new, BzBlocks.WHITE_ESSENCE_BLOCK.get()).build(null));
}