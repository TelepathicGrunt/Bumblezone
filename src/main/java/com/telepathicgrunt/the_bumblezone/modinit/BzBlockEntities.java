package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.HoneyCocoonBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;


public class BzBlockEntities {

    public static final BlockEntityType<?> HONEY_COCOON_BE = FabricBlockEntityTypeBuilder.create(HoneyCocoonBlockEntity::new, BzBlocks.HONEY_COCOON).build(null);

    public static void registerBlockEntities() {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "honey_cocoon"), HONEY_COCOON_BE);
    }
}