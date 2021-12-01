package com.telepathicgrunt.bumblezone.tags;

import com.telepathicgrunt.bumblezone.Bumblezone;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;

public class BzBlockTags {
    // Sole purpose is to initalize the tag wrappers at mod startup
    public static void initTags() {}

    public static final Tag<Block> REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT = TagFactory.BLOCK.create(new ResourceLocation(Bumblezone.MODID, "required_blocks_under_hive_to_teleport"));
    public static final Tag<Block> BLACKLISTED_TELEPORTATION_HIVES = TagFactory.BLOCK.create(new ResourceLocation(Bumblezone.MODID, "blacklisted_teleportable_hive_blocks"));
    public static final Tag<Block> HONEYCOMBS_THAT_FEATURES_CAN_CARVE = TagFactory.BLOCK.create(new ResourceLocation(Bumblezone.MODID, "honeycombs_that_features_can_carve"));
    public static final Tag<Block> WRATH_ACTIVATING_BLOCKS_WHEN_MINED = TagFactory.BLOCK.create(new ResourceLocation(Bumblezone.MODID, "wrath_activating_blocks_when_mined"));
    public static final Tag<Block> FLOWERS_ALLOWED_BY_POLLEN_PUFF = TagFactory.BLOCK.create(new ResourceLocation(Bumblezone.MODID+":flowers_allowed_by_pollen_puff"));
    public static final Tag<Block> FLOWERS_BLACKLISTED_FROM_POLLEN_PUFF = TagFactory.BLOCK.create(new ResourceLocation(Bumblezone.MODID+":flowers_blacklisted_from_pollen_puff"));
}
