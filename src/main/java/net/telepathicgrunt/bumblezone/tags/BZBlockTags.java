package net.telepathicgrunt.bumblezone.tags;

import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.mixin.blocks.BlockTagsAccessor;

public class BZBlockTags {
    // Sole purpose is to initalize the tag wrappers at mod startup
    public static void initTags(){}

    public static final Tag.Identified<Block> REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT_TAG = BlockTagsAccessor.bz_callRegister(Bumblezone.MODID + ":required_blocks_under_hive_to_teleport");
    public static final Tag.Identified<Block> BLACKLISTED_TELEPORTATION_HIVES_TAG = BlockTagsAccessor.bz_callRegister(Bumblezone.MODID+":blacklisted_teleportable_hive_blocks");
    public static final Tag.Identified<Block> HONEYCOMBS_THAT_FEATURES_CAN_CARVE = BlockTagsAccessor.bz_callRegister(Bumblezone.MODID+":honeycombs_that_features_can_carve");
}
