package com.telepathicgrunt.the_bumblezone.tags;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;

public class BzBlockTags {
    // All tag wrappers need to be made at mod init.
    public static void tagInit(){}

    public static final ITag.INamedTag<Block> REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT = BlockTags.bind(Bumblezone.MODID+":required_blocks_under_hive_to_teleport");
    public static final ITag.INamedTag<Block> BLACKLISTED_TELEPORTATION_HIVES = BlockTags.bind(Bumblezone.MODID+":blacklisted_teleportable_hive_blocks");
    public static final ITag.INamedTag<Block> BLACKLISTED_RESOURCEFUL_COMBS = BlockTags.bind(Bumblezone.MODID+":blacklisted_resourceful_bees_combs");
    public static final ITag.INamedTag<Block> BLACKLISTED_PRODUCTIVEBEES_COMBS = BlockTags.bind(Bumblezone.MODID+":blacklisted_productive_bees_combs");
    public static final ITag.INamedTag<Block> HONEYCOMBS_THAT_FEATURES_CAN_CARVE = BlockTags.bind(Bumblezone.MODID+":honeycombs_that_features_can_carve");
    public static final ITag.INamedTag<Block> WRATH_ACTIVATING_BLOCKS_WHEN_MINED = BlockTags.bind(Bumblezone.MODID+":wrath_activating_blocks_when_mined");
    public static final ITag.INamedTag<Block> FLOWERS_ALLOWED_BY_POLLEN_PUFF = BlockTags.bind(Bumblezone.MODID+":flowers_allowed_by_pollen_puff");
    public static final ITag.INamedTag<Block> FLOWERS_BLACKLISTED_FROM_POLLEN_PUFF = BlockTags.bind(Bumblezone.MODID+":flowers_blacklisted_from_pollen_puff");
}
