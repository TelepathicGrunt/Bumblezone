package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.CrystallineFlowerBlockEntity;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.HoneyCocoonBlockEntity;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.IncenseCandleBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;


public class BzBlockEntities {

    public static final BlockEntityType<?> HONEY_COCOON = FabricBlockEntityTypeBuilder.create(HoneyCocoonBlockEntity::new, BzBlocks.HONEY_COCOON).build(null);
    public static final BlockEntityType<?> INCENSE_CANDLE = FabricBlockEntityTypeBuilder.create(IncenseCandleBlockEntity::new, BzBlocks.INCENSE_BASE_CANDLE).build(null);
    public static final BlockEntityType<?> CRYSTALLINE_FLOWER = FabricBlockEntityTypeBuilder.create(CrystallineFlowerBlockEntity::new, BzBlocks.CRYSTALLINE_FLOWER).build(null);

    public static void registerBlockEntities() {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "honey_cocoon"), HONEY_COCOON);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "incense_candle"), INCENSE_CANDLE);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "crystalline_flower"), CRYSTALLINE_FLOWER);
    }
}