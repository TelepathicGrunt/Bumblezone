package com.telepathicgrunt.the_bumblezone.tags;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

public class BZEntityTags {
    // All tag wrappers need to be made at mod init.
    public static void tagInit(){}

    public static final ITag.INamedTag<EntityType<?>> BLACKLISTED_RESOURCEFUL_BEES_ENTITIES = EntityTypeTags.createOptional(new ResourceLocation(Bumblezone.MODID, "blacklisted_resourceful_bees_entities"));
}
