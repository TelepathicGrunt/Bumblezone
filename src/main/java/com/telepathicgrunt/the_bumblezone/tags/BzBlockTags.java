package com.telepathicgrunt.the_bumblezone.tags;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;

public class BzBlockTags {
    // All tag wrappers need to be made at mod init.
    public static void tagInit(){}

    public static final Tag.Named<Block> REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT = BlockTags.createOptional(new ResourceLocation(Bumblezone.MODID+":required_blocks_under_hive_to_teleport"));
    public static final Tag.Named<Block> BLACKLISTED_TELEPORTATION_HIVES = BlockTags.createOptional(new ResourceLocation(Bumblezone.MODID+":blacklisted_teleportable_hive_blocks"));
    public static final Tag.Named<Block> BLACKLISTED_RESOURCEFUL_COMBS = BlockTags.createOptional(new ResourceLocation(Bumblezone.MODID+":blacklisted_resourceful_bees_combs"));
    public static final Tag.Named<Block> BLACKLISTED_PRODUCTIVEBEES_COMBS = BlockTags.createOptional(new ResourceLocation(Bumblezone.MODID+":blacklisted_productive_bees_combs"));
    public static final Tag.Named<Block> HONEYCOMBS_THAT_FEATURES_CAN_CARVE = BlockTags.createOptional(new ResourceLocation(Bumblezone.MODID+":honeycombs_that_features_can_carve"));
    public static final Tag.Named<Block> WRATH_ACTIVATING_BLOCKS_WHEN_MINED = BlockTags.createOptional(new ResourceLocation(Bumblezone.MODID+":wrath_activating_blocks_when_mined"));
    public static final Tag.Named<Block> FLOWERS_ALLOWED_BY_POLLEN_PUFF = BlockTags.createOptional(new ResourceLocation(Bumblezone.MODID+":flowers_allowed_by_pollen_puff"));
    public static final Tag.Named<Block> FLOWERS_BLACKLISTED_FROM_POLLEN_PUFF = BlockTags.createOptional(new ResourceLocation(Bumblezone.MODID+":flowers_blacklisted_from_pollen_puff"));

    public static final Tag.Named<Block> FORGE_STORAGE_BLOCK_WAX = BlockTags.createOptional(new ResourceLocation("forge:storage_blocks/wax"));
}
