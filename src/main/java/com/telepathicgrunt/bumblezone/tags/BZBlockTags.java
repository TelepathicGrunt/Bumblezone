package com.telepathicgrunt.bumblezone.tags;

import com.telepathicgrunt.bumblezone.Bumblezone;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class BZBlockTags {
    // Sole purpose is to initalize the tag wrappers at mod startup
    public static void initTags(){}

    public static final Tag<Block> REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT_TAG = TagRegistry.block(new Identifier(Bumblezone.MODID, "required_blocks_under_hive_to_teleport"));
    public static final Tag<Block> BLACKLISTED_TELEPORTATION_HIVES_TAG = TagRegistry.block(new Identifier(Bumblezone.MODID, "blacklisted_teleportable_hive_blocks"));
    public static final Tag<Block> HONEYCOMBS_THAT_FEATURES_CAN_CARVE = TagRegistry.block(new Identifier(Bumblezone.MODID, "honeycombs_that_features_can_carve"));
    public static final Tag<Block> WRATH_ACTIVATING_BLOCKS_WHEN_MINED = TagRegistry.block(new Identifier(Bumblezone.MODID, "wrath_activating_blocks_when_mined"));
}
