package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluid;

public class BzTags {
    // Sole purpose is to initalize the tag wrappers at mod startup
    public static void initTags() {}

    public static final TagKey<Block> REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/required_blocks_under_beehive_to_teleport"));
    public static final TagKey<Block> FORCED_ALLOWED_TELEPORTABLE_BLOCK = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/forced_allowed_teleportable_blocks"));
    public static final TagKey<Block> DISALLOWED_TELEPORTABLE_BEEHIVE = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/disallowed_teleportable_beehive_blocks"));
    public static final TagKey<Block> HONEYCOMBS_THAT_FEATURES_CAN_CARVE = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "worldgen_checks/honeycombs_that_features_can_carve"));
    public static final TagKey<Block> WRATH_ACTIVATING_BLOCKS_WHEN_MINED = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "bee_aggression_in_dimension/wrath_activating_blocks_when_mined"));
    public static final TagKey<Block> FLOWERS_ALLOWED_BY_POLLEN_PUFF = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "pollen_puff/multiplying_allowed_flowers"));
    public static final TagKey<Block> FLOWERS_FORCED_DISALLOWED_FROM_POLLEN_PUFF = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "pollen_puff/multiplying_forced_disallowed_flowers"));
    public static final TagKey<Block> CARPENTER_BEE_BOOTS_MINEABLES = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "carpenter_bee_boots/mineables"));
    public static final TagKey<Block> CARPENTER_BEE_BOOTS_CLIMBABLES = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "carpenter_bee_boots/climbables"));
    public static final TagKey<Block> DISALLOWED_POSITION_TRACKING_BLOCKS = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "honey_compass/beehives_disallowed_from_position_tracking"));
    public static final TagKey<Block> FORCED_ALLOWED_POSITION_TRACKING_BLOCKS = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "honey_compass/forced_allowed_position_tracking"));
    public static final TagKey<Block> CANDLES = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "candles"));
    public static final TagKey<Block> CANDLE_WICKS = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "candle_wicks"));
    public static final TagKey<Block> CANDLE_BASES = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "candle_bases"));
    public static final TagKey<Block> CAVE_EDGE_BLOCKS_FOR_MODDED_COMPATS = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "worldgen_checks/cave_edge_blocks_for_modded_compats"));
    public static final TagKey<Block> STRING_CURTAINS = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "string_curtains"));
    public static final TagKey<Block> CRYSTALLINE_FLOWER_CAN_SURVIVE_ON = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/can_be_placed_on"));
    public static final TagKey<Block> WATERLOGGABLE_BLOCKS_WHEN_PLACED_IN_FLUID = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "sugar_water/waterloggable_blocks_when_placed_in_fluid"));
    public static final TagKey<Block> FORCED_DISALLOW_WATERLOGGING_BLOCKS_WHEN_PLACED_IN_FLUID = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "sugar_water/forced_disallow_waterlogging_blocks_when_placed_in_fluid"));
    public static final TagKey<Block> DOWNWARD_BUBBLE_COLUMN_CAUSING = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "sugar_water/downward_bubble_column_causing"));
    public static final TagKey<Block> UPWARD_BUBBLE_COLUMN_CAUSING = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Bumblezone.MODID, "sugar_water/upward_bubble_column_causing"));

    public static final TagKey<Item> TURN_SLIME_TO_HONEY_SLIME = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "turn_slime_to_honey_slime"));
    public static final TagKey<Item> HONEY_CRYSTAL_SHIELD_REPAIR_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "repair_items/honey_crystal_shield"));
    public static final TagKey<Item> STINGER_SPEAR_REPAIR_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "repair_items/stinger_spear"));
    public static final TagKey<Item> BEE_ARMOR_REPAIR_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "repair_items/bee_armor"));
    public static final TagKey<Item> BEE_CANNON_REPAIR_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "repair_items/bee_cannon"));
    public static final TagKey<Item> CRYSTAL_CANNON_REPAIR_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "repair_items/crystal_cannon"));
    public static final TagKey<Item> BEE_FEEDING_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "bee_feedable_items"));
    public static final TagKey<Item> WRATH_ACTIVATING_ITEMS_WHEN_PICKED_UP = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "bee_aggression_in_dimension/wrath_activating_items_when_picked_up"));
    public static final TagKey<Item> HONEY_BUCKETS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "buckets/honey"));
    public static final TagKey<Item> ROYAL_JELLY_BUCKETS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "buckets/royal_jelly"));
    public static final TagKey<Item> SHULKER_BOXES = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("c", "shulker_boxes"));
    public static final TagKey<Item> SUPER_CANDLES_ITEM = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "super_candles"));
    public static final TagKey<Item> DAMAGEABLE_CANDLE_LIGHTING_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "candle_lightables/damageable"));
    public static final TagKey<Item> CONSUMABLE_CANDLE_LIGHTING_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "candle_lightables/consumable"));
    public static final TagKey<Item> INFINITE_CANDLE_LIGHTING_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "candle_lightables/infinite"));
    public static final TagKey<Item> XP_2_WHEN_CONSUMED_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/xp_2_when_consumed"));
    public static final TagKey<Item> XP_5_WHEN_CONSUMED_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/xp_5_when_consumed"));
    public static final TagKey<Item> XP_25_WHEN_CONSUMED_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/xp_25_when_consumed"));
    public static final TagKey<Item> XP_100_WHEN_CONSUMED_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/xp_100_when_consumed"));
    public static final TagKey<Item> XP_1000_WHEN_CONSUMED_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/xp_1000_when_consumed"));
    public static final TagKey<Item> XP_MAXED_WHEN_CONSUMED_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/xp_maxed_when_consumed"));
    public static final TagKey<Item> CANNOT_CONSUMED_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/cannot_consume"));
    public static final TagKey<Item> CAN_BE_ENCHANTED_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/can_be_enchanted"));
    public static final TagKey<Item> BEEHEMOTH_DESIRED_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "mob_luring/beehemoth"));
    public static final TagKey<Item> BEEHEMOTH_FAST_LURING_DESIRED_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "mob_luring/beehemoth_fast_luring"));
    public static final TagKey<Item> HONEY_SLIME_DESIRED_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "mob_luring/honey_slime"));
    public static final TagKey<Item> STRING_CURTAINS_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "string_curtains"));
    public static final TagKey<Item> STRING_CURTAINS_CURTAIN_EXTENDING_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "string_curtains/curtain_extending"));
    public static final TagKey<Item> FORCED_ALLOWED_RANDOM_BONUS_TRADE_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "bee_queen/forced_allowed_random_bonus_trade_items"));
    public static final TagKey<Item> DISALLOWED_RANDOM_BONUS_TRADE_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "bee_queen/disallowed_random_bonus_trade_items"));
    public static final TagKey<Item> TARGET_ARMOR_HIT_BY_TELEPORT_PROJECTILE = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/target_armor_hit_by_teleport_projectile"));
    public static final TagKey<Item> TARGET_WITH_HELD_ITEM_HIT_BY_TELEPORT_PROJECTILE = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/target_with_held_item_hit_by_teleport_projectile"));
    public static final TagKey<Item> TELEPORT_ITEM_RIGHT_CLICKED_BEEHIVE = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/item_right_clicked_beehive"));
    public static final TagKey<Item> TELEPORT_ITEM_RIGHT_CLICKED_BEEHIVE_CROUCHING = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/item_right_clicked_beehive_crouching"));
    public static final TagKey<Item> DO_ITEM_RIGHT_CLICK_CHECK_EARLIER = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/do_item_right_click_check_earlier"));
    public static final TagKey<Item> ITEM_SPECIAL_DEDICATED_COMPAT = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/item_special_dedicated_compat"));
    public static final TagKey<Item> HONEY_DRUNK_TRIGGER_ITEMS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "queens_desire/honey_drunk_trigger_items"));
    public static final TagKey<Item> BZ_ARMOR_ABILITY_ENHANCING_GEAR = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Bumblezone.MODID, "bee_armors/bz_armor_ability_enhancing_gear"));

    public static final TagKey<Fluid> HONEY_FLUID = TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation("c", "honey"));
    public static final TagKey<Fluid> BZ_HONEY_FLUID = TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation(Bumblezone.MODID, "honey"));
    public static final TagKey<Fluid> VISUAL_HONEY_FLUID = TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation("c", "visual/honey"));
    public static final TagKey<Fluid> VISUAL_WATER_FLUID = TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation("c", "visual/water"));
    public static final TagKey<Fluid> CONVERTIBLE_TO_SUGAR_WATER = TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation(Bumblezone.MODID, "convertible_to_sugar_water"));
    public static final TagKey<Fluid> ROYAL_JELLY_FLUID = TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation(Bumblezone.MODID, "royal_jelly"));
    public static final TagKey<Fluid> SUGAR_WATER_FLUID = TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation(Bumblezone.MODID, "sugar_water"));
    public static final TagKey<Fluid> SPECIAL_HONEY_LIKE = TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation(Bumblezone.MODID, "special_honey_like"));

    public static final TagKey<EntityType<?>> POLLEN_PUFF_CAN_POLLINATE = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "pollen_puff/can_pollinate"));
    public static final TagKey<EntityType<?>> DISALLOWED_BEE_CANNON_BEES = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "bee_cannon/disallowed_bee_pickup"));
    public static final TagKey<EntityType<?>> FORCED_ALLOWED_STINGLESS_BEE_HELMET_PASSENGERS = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "stingless_bee_helmet/forced_allowed_passengers"));
    public static final TagKey<EntityType<?>> DISALLOWED_STINGLESS_BEE_HELMET_PASSENGERS = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "stingless_bee_helmet/disallowed_passengers"));
    public static final TagKey<EntityType<?>> FORCED_BEE_ANGRY_AT = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "bee_aggression_in_dimension/always_angry_at"));
    public static final TagKey<EntityType<?>> FORCED_BEE_CALM_AT = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "bee_aggression_in_dimension/forced_calm_at"));
    public static final TagKey<EntityType<?>> HANGING_GARDENS_INITIAL_SPAWN_ENTITIES = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "hanging_garden/initial_spawn_entities"));
    public static final TagKey<EntityType<?>> STRING_CURTAIN_BLOCKS_PATHFINDING_FOR_NON_BEE_MOB = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "string_curtains/blocks_pathfinding_for_non_bee_mob"));
    public static final TagKey<EntityType<?>> STRING_CURTAIN_FORCE_ALLOW_PATHFINDING = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "string_curtains/force_allow_pathfinding"));
    public static final TagKey<EntityType<?>> BEE_LIKE = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "bee_like"));
    public static final TagKey<EntityType<?>> TARGET_ENTITY_HIT_BY_TELEPORT_PROJECTILE_ANYWHERE = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/target_entity_hit_by_teleport_projectile_anywhere"));
    public static final TagKey<EntityType<?>> TARGET_ENTITY_HIT_BY_TELEPORT_PROJECTILE_HIGH = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/target_entity_hit_by_teleport_projectile_high"));
    public static final TagKey<EntityType<?>> TARGET_ENTITY_HIT_BY_TELEPORT_PROJECTILE_LOW = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/target_entity_hit_by_teleport_projectile_low"));
    public static final TagKey<EntityType<?>> TELEPORT_PROJECTILES = TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/teleport_projectiles"));

    public static final TagKey<Structure> NO_DUNGEONS = TagKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "no_dungeons"));
    public static final TagKey<Structure> NO_CAVES = TagKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "no_caves"));
    public static final TagKey<Structure> NO_GIANT_HONEY_CRYSTALS = TagKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "no_giant_honey_crystals"));
    public static final TagKey<Structure> NO_HONEYCOMB_HOLES = TagKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "no_honeycomb_holes"));
    public static final TagKey<Structure> NO_HONEYCOMB_HOLES_PIECEWISE = TagKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "no_honeycomb_holes_piecewise"));
    public static final TagKey<Structure> WRATH_CAUSING = TagKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "wrath_causing"));
    public static final TagKey<Structure> HONEY_COMPASS_DEFAULT_LOCATING = TagKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "honey_compass/default_locating"));
    public static final TagKey<Structure> HONEY_COMPASS_THRONE_LOCATING = TagKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "honey_compass/throne_locating"));
    public static final TagKey<Structure> BEE_QUEEN_MINING_FATIGUE = TagKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "bee_queen_mining_fatigue"));

    public static final TagKey<MobEffect> DISALLOWED_INCENSE_CANDLE_EFFECTS = TagKey.create(Registry.MOB_EFFECT_REGISTRY, new ResourceLocation(Bumblezone.MODID, "incense_candle/disallowed_effects"));

    public static final TagKey<PoiType> IS_NEAR_BEEHIVE_ADVANCEMENT_TRIGGER_POI = TagKey.create(Registry.POINT_OF_INTEREST_TYPE_REGISTRY, new ResourceLocation(Bumblezone.MODID, "is_near_beehive_advancement_trigger"));

    public static final TagKey<Enchantment> DISALLOWED_CRYSTALLINE_FLOWER_ENCHANTMENTS = TagKey.create(Registry.ENCHANTMENT_REGISTRY, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/disallowed_enchantments"));
    public static final TagKey<Enchantment> FORCED_ALLOWED_CRYSTALLINE_FLOWER_ENCHANTMENTS = TagKey.create(Registry.ENCHANTMENT_REGISTRY, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/forced_allowed_enchantments"));
    public static final TagKey<Enchantment> ITEM_WITH_TELEPORT_ENCHANT = TagKey.create(Registry.ENCHANTMENT_REGISTRY, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/any_item_right_clicked_beehive_with_enchant"));
    public static final TagKey<Enchantment> ITEM_WITH_TELEPORT_ENCHANT_CROUCHING = TagKey.create(Registry.ENCHANTMENT_REGISTRY, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/any_item_right_clicked_beehive_with_enchant_crouching"));
}
