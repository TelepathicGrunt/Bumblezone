package com.telepathicgrunt.bumblezone.tags;

import com.telepathicgrunt.bumblezone.Bumblezone;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class BzEntityTags {
    // All tag wrappers need to be made at mod init.
    public static void tagInit(){}

    public static final Tag<EntityType<?>> POLLEN_PUFF_CAN_POLLINATE = TagRegistry.entityType(new Identifier(Bumblezone.MODID+":pollen_puff_can_pollinate"));
}
