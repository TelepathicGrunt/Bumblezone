package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.HoneyCocoonBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;


public class BzBlockEntities {

    public static final BlockEntityType<?> HONEY_COCOON_BE = BlockEntityType.Builder.of(HoneyCocoonBlockEntity::new, BzBlocks.HONEY_COCOON).build(null);

    public static void registerBlockEntities() {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "honey_cocoon"), HONEY_COCOON_BE);
    }
}