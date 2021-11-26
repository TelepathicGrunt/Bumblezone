package com.telepathicgrunt.the_bumblezone.tags;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

public class BzEntityTags {
    // All tag wrappers need to be made at mod init.
    public static void tagInit(){}

    public static final ITag.INamedTag<EntityType<?>> POLLEN_PUFF_CAN_POLLINATE = EntityTypeTags.createOptional(new ResourceLocation(Bumblezone.MODID+":pollen_puff_can_pollinate"));
}
