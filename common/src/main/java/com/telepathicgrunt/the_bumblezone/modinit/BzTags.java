package com.telepathicgrunt.the_bumblezone.modinit;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.NotImplementedException;

public class BzTags {
    // Sole purpose is to initalize the tag wrappers at mod startup
    public static void initTags() {}

    public static final TagKey<Block> REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/required_blocks_under_beehive_to_teleport"));
    public static final TagKey<Block> FORCED_ALLOWED_TELEPORTABLE_BLOCK = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/forced_allowed_teleportable_blocks"));
    public static final TagKey<Block> DISALLOWED_TELEPORTABLE_BEEHIVE = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/disallowed_teleportable_beehive_blocks"));
    public static final TagKey<Block> HONEYCOMBS_THAT_FEATURES_CAN_CARVE = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "worldgen_checks/honeycombs_that_features_can_carve"));
    public static final TagKey<Block> FORCE_CAVE_TO_NOT_CARVE = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "worldgen_checks/force_cave_to_not_carve"));
    public static final TagKey<Block> WRATH_ACTIVATING_BLOCKS_WHEN_MINED = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "bee_aggression_in_dimension/wrath_activating_blocks_when_mined"));
    public static final TagKey<Block> FLOWERS_ALLOWED_BY_POLLEN_PUFF = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "pollen_puff/multiplying_allowed_flowers"));
    public static final TagKey<Block> FLOWERS_FORCED_DISALLOWED_FROM_POLLEN_PUFF = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "pollen_puff/multiplying_forced_disallowed_flowers"));
    public static final TagKey<Block> CARPENTER_BEE_BOOTS_MINEABLES = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "carpenter_bee_boots/mineables"));
    public static final TagKey<Block> CARPENTER_BEE_BOOTS_CLIMBABLES = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "carpenter_bee_boots/climbables"));
    public static final TagKey<Block> DISALLOWED_POSITION_TRACKING_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "honey_compass/beehives_disallowed_from_position_tracking"));
    public static final TagKey<Block> FORCED_ALLOWED_POSITION_TRACKING_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "honey_compass/forced_allowed_position_tracking"));
    public static final TagKey<Block> CANDLES = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "candles"));
    public static final TagKey<Block> CANDLE_WICKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "candle_wicks"));
    public static final TagKey<Block> CANDLE_BASES = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "candle_bases"));
    public static final TagKey<Block> CAVE_EDGE_BLOCKS_FOR_MODDED_COMPATS = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "worldgen_checks/cave_edge_blocks_for_modded_compats"));
    public static final TagKey<Block> STRING_CURTAINS = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "string_curtains"));
    public static final TagKey<Block> CRYSTALLINE_FLOWER_CAN_SURVIVE_ON = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/can_be_placed_on"));
    public static final TagKey<Block> WATERLOGGABLE_BLOCKS_WHEN_PLACED_IN_FLUID = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "sugar_water/waterloggable_blocks_when_placed_in_fluid"));
    public static final TagKey<Block> FORCED_DISALLOW_WATERLOGGING_BLOCKS_WHEN_PLACED_IN_FLUID = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "sugar_water/forced_disallow_waterlogging_blocks_when_placed_in_fluid"));
    public static final TagKey<Block> DOWNWARD_BUBBLE_COLUMN_CAUSING = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "sugar_water/downward_bubble_column_causing"));
    public static final TagKey<Block> UPWARD_BUBBLE_COLUMN_CAUSING = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "sugar_water/upward_bubble_column_causing"));
    public static final TagKey<Block> ANCIENT_WAX_FULL_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "ancient_wax/full_blocks"));
    public static final TagKey<Block> ANCIENT_WAX_SLABS = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "ancient_wax/slabs"));
    public static final TagKey<Block> ANCIENT_WAX_STAIRS = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "ancient_wax/stairs"));
    public static final TagKey<Block> LUMINESCENT_WAX_LIGHT_CHANNELS = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "luminescent_wax/light_channels"));
    public static final TagKey<Block> LUMINESCENT_WAX_LIGHT_CORNERS = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "luminescent_wax/light_corners"));
    public static final TagKey<Block> LUMINESCENT_WAX_LIGHT_NODES = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "luminescent_wax/light_nodes"));
    public static final TagKey<Block> BLOCK_ENTITY_FORCED_HIGHLIGHTING = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "essence/knowing/block_entity_forced_highlighting"));
    public static final TagKey<Block> BLOCK_ENTITY_PREVENT_HIGHLIGHTING = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "essence/knowing/block_entity_prevent_highlighting"));
    public static final TagKey<Block> FORCE_DISALLOWED_GROW_PLANT = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "essence/life/force_disallowed_grow_plant"));
    public static final TagKey<Block> GROW_PLANTS = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "essence/life/grow_plants"));
    public static final TagKey<Block> POLLEN_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "pollen"));
    public static final TagKey<Block> SENTRY_WATCHER_ALWAYS_DESTROY = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "sentry_watcher/always_destroy"));
    public static final TagKey<Block> SENTRY_WATCHER_FORCED_NEVER_DESTROY = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "sentry_watcher/forced_never_destroy"));
    public static final TagKey<Block> ROOTMIN_DEFAULT_FLOWERS = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "rootmin/default_flowers"));
    public static final TagKey<Block> ROOTMIN_ALLOWED_FLOWER = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "rootmin/allowed_flower"));
    public static final TagKey<Block> ROOTMIN_FORCED_DISALLOWED_FLOWER = TagKey.create(Registries.BLOCK, new ResourceLocation(Bumblezone.MODID, "rootmin/forced_disallowed_flower"));

    public static final TagKey<Item> TURN_SLIME_TO_HONEY_SLIME = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "turn_slime_to_honey_slime"));
    public static final TagKey<Item> HONEY_CRYSTAL_SHIELD_REPAIR_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "repair_items/honey_crystal_shield"));
    public static final TagKey<Item> STINGER_SPEAR_REPAIR_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "repair_items/stinger_spear"));
    public static final TagKey<Item> BEE_ARMOR_REPAIR_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "repair_items/bee_armor"));
    public static final TagKey<Item> BEE_CANNON_REPAIR_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "repair_items/bee_cannon"));
    public static final TagKey<Item> CRYSTAL_CANNON_REPAIR_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "repair_items/crystal_cannon"));
    public static final TagKey<Item> BEE_FEEDING_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "bee_feedable_items"));
    public static final TagKey<Item> WRATH_ACTIVATING_ITEMS_WHEN_PICKED_UP = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "bee_aggression_in_dimension/wrath_activating_items_when_picked_up"));
    public static final TagKey<Item> HONEY_BUCKETS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "buckets/honey"));
    public static final TagKey<Item> ROYAL_JELLY_BUCKETS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "buckets/royal_jelly"));
    public static final TagKey<Item> SUPER_CANDLES_ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "super_candles"));
    public static final TagKey<Item> DAMAGEABLE_CANDLE_LIGHTING_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "candle_lightables/damageable"));
    public static final TagKey<Item> CONSUMABLE_CANDLE_LIGHTING_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "candle_lightables/consumable"));
    public static final TagKey<Item> INFINITE_CANDLE_LIGHTING_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "candle_lightables/infinite"));
    public static final TagKey<Item> XP_2_WHEN_CONSUMED_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/xp_2_when_consumed"));
    public static final TagKey<Item> XP_5_WHEN_CONSUMED_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/xp_5_when_consumed"));
    public static final TagKey<Item> XP_25_WHEN_CONSUMED_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/xp_25_when_consumed"));
    public static final TagKey<Item> XP_100_WHEN_CONSUMED_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/xp_100_when_consumed"));
    public static final TagKey<Item> XP_1000_WHEN_CONSUMED_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/xp_1000_when_consumed"));
    public static final TagKey<Item> XP_MAXED_WHEN_CONSUMED_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/xp_maxed_when_consumed"));
    public static final TagKey<Item> CANNOT_CONSUMED_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/cannot_consume"));
    public static final TagKey<Item> CAN_BE_ENCHANTED_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/can_be_enchanted"));
    public static final TagKey<Item> BEEHEMOTH_DESIRED_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "mob_luring/beehemoth"));
    public static final TagKey<Item> BEEHEMOTH_FAST_LURING_DESIRED_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "mob_luring/beehemoth_fast_luring"));
    public static final TagKey<Item> HONEY_SLIME_DESIRED_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "mob_luring/honey_slime"));
    public static final TagKey<Item> STRING_CURTAINS_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "string_curtains"));
    public static final TagKey<Item> STRING_CURTAINS_CURTAIN_EXTENDING_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "string_curtains/curtain_extending"));
    public static final TagKey<Item> FORCED_ALLOWED_RANDOM_BONUS_TRADE_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "bee_queen/forced_allowed_random_bonus_trade_items"));
    public static final TagKey<Item> DISALLOWED_RANDOM_BONUS_TRADE_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "bee_queen/disallowed_random_bonus_trade_items"));
    public static final TagKey<Item> TARGET_ARMOR_HIT_BY_TELEPORT_PROJECTILE = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/target_armor_hit_by_teleport_projectile"));
    public static final TagKey<Item> TARGET_WITH_HELD_ITEM_HIT_BY_TELEPORT_PROJECTILE = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/target_with_held_item_hit_by_teleport_projectile"));
    public static final TagKey<Item> TELEPORT_ITEM_RIGHT_CLICKED_BEEHIVE = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/item_right_clicked_beehive"));
    public static final TagKey<Item> TELEPORT_ITEM_RIGHT_CLICKED_BEEHIVE_CROUCHING = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/item_right_clicked_beehive_crouching"));
    public static final TagKey<Item> DO_ITEM_RIGHT_CLICK_CHECK_EARLIER = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/do_item_right_click_check_earlier"));
    public static final TagKey<Item> ITEM_SPECIAL_DEDICATED_COMPAT = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/item_special_dedicated_compat"));
    public static final TagKey<Item> HONEY_DRUNK_TRIGGER_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "queens_desire/honey_drunk_trigger_items"));
    public static final TagKey<Item> BZ_ARMOR_ABILITY_ENHANCING_WEARABLES = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "bee_armors/bz_armor_ability_enhancing_wearables"));
    public static final TagKey<Item> CALMING_DROWNED_BONUS_HELD_ITEM = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "essence/calming_drowned_bonus_held_item"));
    public static final TagKey<Item> ABILITY_ESSENCE_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(Bumblezone.MODID, "ability_essence_items"));

    public static final TagKey<Fluid> HONEY_FLUID = TagKey.create(Registries.FLUID, new ResourceLocation(getPlatformTagNamespace(), "honey"));
    public static final TagKey<Fluid> BZ_HONEY_FLUID = TagKey.create(Registries.FLUID, new ResourceLocation(Bumblezone.MODID, "honey"));
    public static final TagKey<Fluid> ROYAL_JELLY_FLUID = TagKey.create(Registries.FLUID, new ResourceLocation(Bumblezone.MODID, "royal_jelly"));
    public static final TagKey<Fluid> SPECIAL_HONEY_LIKE = TagKey.create(Registries.FLUID, new ResourceLocation(Bumblezone.MODID, "special_honey_like"));
    public static final TagKey<Fluid> VISUAL_HONEY_FLUID = TagKey.create(Registries.FLUID, new ResourceLocation(getPlatformTagNamespace(), "visual/honey"));
    public static final TagKey<Fluid> VISUAL_WATER_FLUID = TagKey.create(Registries.FLUID, new ResourceLocation(getPlatformTagNamespace(), "visual/water"));
    public static final TagKey<Fluid> SUGAR_WATER_FLUID = TagKey.create(Registries.FLUID, new ResourceLocation(Bumblezone.MODID, "sugar_water"));
    public static final TagKey<Fluid> CONVERTIBLE_TO_SUGAR_WATER = TagKey.create(Registries.FLUID, new ResourceLocation(Bumblezone.MODID, "convertible_to_sugar_water"));

    public static final TagKey<EntityType<?>> POLLEN_PUFF_CAN_POLLINATE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "pollen_puff/can_pollinate"));
    public static final TagKey<EntityType<?>> PILE_OF_POLLEN_CANNOT_SLOW = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "pile_of_pollen/cannot_slow"));
    public static final TagKey<EntityType<?>> BUZZING_BRIEFCASE_CAN_POLLINATE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "buzzing_briefcase/can_pollinate"));
    public static final TagKey<EntityType<?>> BUZZING_BRIEFCASE_DISALLOWED_BEE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "buzzing_briefcase/disallowed_bee_pickup"));
    public static final TagKey<EntityType<?>> CANNON_BEES_DISALLOWED_BEE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "bee_cannon/disallowed_bee_pickup"));
    public static final TagKey<EntityType<?>> STINGLESS_BEE_HELMET_FORCED_ALLOWED_PASSENGERS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "stingless_bee_helmet/forced_allowed_passengers"));
    public static final TagKey<EntityType<?>> STINGLESS_BEE_HELMET_DISALLOWED_PASSENGERS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "stingless_bee_helmet/disallowed_passengers"));
    public static final TagKey<EntityType<?>> FORCED_BEE_ANGRY_AT = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "bee_aggression_in_dimension/always_angry_at"));
    public static final TagKey<EntityType<?>> FORCED_BEE_CALM_AT = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "bee_aggression_in_dimension/forced_calm_at"));
    public static final TagKey<EntityType<?>> HANGING_GARDENS_INITIAL_SPAWN_ENTITIES = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "hanging_garden/initial_spawn_entities"));
    public static final TagKey<EntityType<?>> STRING_CURTAIN_BLOCKS_PATHFINDING_FOR_NON_BEE_MOB = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "string_curtains/blocks_pathfinding_for_non_bee_mob"));
    public static final TagKey<EntityType<?>> STRING_CURTAIN_FORCE_ALLOW_PATHFINDING = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "string_curtains/force_allow_pathfinding"));
    public static final TagKey<EntityType<?>> DIRT_PELLET_EXTRA_DAMAGE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "dirt_pellet/extra_damage_dealt_to"));
    public static final TagKey<EntityType<?>> DIRT_PELLET_FORCE_NO_EXTRA_DAMAGE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "dirt_pellet/forced_no_extra_damage_dealt_to"));
    public static final TagKey<EntityType<?>> ROOTMIN_PANIC_AVOID = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "rootmin/panic_avoid"));
    public static final TagKey<EntityType<?>> ROOTMIN_TARGETS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "rootmin/targets"));
    public static final TagKey<EntityType<?>> ROOTMIN_FORCED_DO_NOT_TARGET = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "rootmin/forced_do_not_target"));
    public static final TagKey<EntityType<?>> SENTRY_WATCHER_ACTIVATES_WHEN_SEEN = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "sentry_watcher/activates_when_seen"));
    public static final TagKey<EntityType<?>> SENTRY_WATCHER_FORCED_NEVER_ACTIVATES_WHEN_SEEN = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "sentry_watcher/forced_never_activates_when_seen"));
    public static final TagKey<EntityType<?>> BEE_LIKE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "bee_like"));
    public static final TagKey<EntityType<?>> HEAVY_AIR_IMMUNE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "heavy_air/immune_to_gravity_effect"));
    public static final TagKey<EntityType<?>> WINDY_AIR_IMMUNE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "windy_air/immune_to_push_effect"));
    public static final TagKey<EntityType<?>> TARGET_ENTITY_HIT_BY_TELEPORT_PROJECTILE_ANYWHERE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/target_entity_hit_by_teleport_projectile_anywhere"));
    public static final TagKey<EntityType<?>> TARGET_ENTITY_HIT_BY_TELEPORT_PROJECTILE_HIGH = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/target_entity_hit_by_teleport_projectile_high"));
    public static final TagKey<EntityType<?>> TARGET_ENTITY_HIT_BY_TELEPORT_PROJECTILE_LOW = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/target_entity_hit_by_teleport_projectile_low"));
    public static final TagKey<EntityType<?>> TELEPORT_PROJECTILES = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/teleport_projectiles"));
    public static final TagKey<EntityType<?>> ALLOW_ANGER_THROUGH = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "essence/calming/allow_anger_through"));
    public static final TagKey<EntityType<?>> ENTITY_PREVENT_HIGHLIGHTING = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "essence/knowing/prevent_highlighting"));
    public static final TagKey<EntityType<?>> ENTITY_FORCED_WHITE_HIGHLIGHT = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "essence/knowing/forced_white_highlight"));
    public static final TagKey<EntityType<?>> ENTITY_FORCED_PURPLE_HIGHLIGHT = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "essence/knowing/forced_purple_highlight"));
    public static final TagKey<EntityType<?>> ENTITY_FORCED_CYAN_HIGHLIGHT = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "essence/knowing/forced_cyan_highlight"));
    public static final TagKey<EntityType<?>> ENTITY_FORCED_GREEN_HIGHLIGHT = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "essence/knowing/forced_green_highlight"));
    public static final TagKey<EntityType<?>> ENTITY_FORCED_YELLOW_HIGHLIGHT = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "essence/knowing/forced_yellow_highlight"));
    public static final TagKey<EntityType<?>> ENTITY_FORCED_ORANGE_HIGHLIGHT = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "essence/knowing/forced_orange_highlight"));
    public static final TagKey<EntityType<?>> ENTITY_FORCED_RED_HIGHLIGHT = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "essence/knowing/forced_red_highlight"));
    public static final TagKey<EntityType<?>> BOSSES = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "bosses"));
    public static final TagKey<EntityType<?>> ESSENCE_RAGING_ARENA_NORMAL_ENEMY = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "essence/raging_arena/normal_enemy"));
    public static final TagKey<EntityType<?>> ESSENCE_RAGING_ARENA_RANGED_ENEMY = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "essence/raging_arena/ranged_enemy"));
    public static final TagKey<EntityType<?>> ESSENCE_RAGING_ARENA_STRONG_ENEMY = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "essence/raging_arena/strong_enemy"));
    public static final TagKey<EntityType<?>> ESSENCE_RAGING_ARENA_BOSS_ENEMY = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "essence/raging_arena/boss_enemy"));
    public static final TagKey<EntityType<?>> ESSENCE_CALMING_ARENA_NORMAL_ENEMY = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "essence/calming_arena/normal_enemy"));
    public static final TagKey<EntityType<?>> ESSENCE_CALMING_ARENA_LATE_NORMAL_ENEMY = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "essence/calming_arena/late_normal_enemy"));
    public static final TagKey<EntityType<?>> ESSENCE_CALMING_ARENA_STRONG_ENEMY = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "essence/calming_arena/strong_enemy"));
    public static final TagKey<EntityType<?>> ESSENCE_CALMING_ARENA_BOSS_ENEMY = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "essence/calming_arena/boss_enemy"));
    public static final TagKey<EntityType<?>> ESSENCE_LIFE_ARENA_NORMAL_ENEMY = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Bumblezone.MODID, "essence/life_arena/normal_enemy"));

    public static final TagKey<Structure> NO_DUNGEONS = TagKey.create(Registries.STRUCTURE, new ResourceLocation(Bumblezone.MODID, "no_dungeons"));
    public static final TagKey<Structure> NO_CAVES = TagKey.create(Registries.STRUCTURE, new ResourceLocation(Bumblezone.MODID, "no_caves"));
    public static final TagKey<Structure> NO_GIANT_HONEY_CRYSTALS = TagKey.create(Registries.STRUCTURE, new ResourceLocation(Bumblezone.MODID, "no_giant_honey_crystals"));
    public static final TagKey<Structure> NO_HONEYCOMB_HOLES = TagKey.create(Registries.STRUCTURE, new ResourceLocation(Bumblezone.MODID, "no_honeycomb_holes"));
    public static final TagKey<Structure> NO_HONEYCOMB_HOLES_PIECEWISE = TagKey.create(Registries.STRUCTURE, new ResourceLocation(Bumblezone.MODID, "no_honeycomb_holes_piecewise"));
    public static final TagKey<Structure> WRATH_CAUSING = TagKey.create(Registries.STRUCTURE, new ResourceLocation(Bumblezone.MODID, "wrath_causing"));
    public static final TagKey<Structure> HONEY_COMPASS_DEFAULT_LOCATING = TagKey.create(Registries.STRUCTURE, new ResourceLocation(Bumblezone.MODID, "honey_compass/default_locating"));
    public static final TagKey<Structure> HONEY_COMPASS_THRONE_LOCATING = TagKey.create(Registries.STRUCTURE, new ResourceLocation(Bumblezone.MODID, "honey_compass/throne_locating"));
    public static final TagKey<Structure> BEE_QUEEN_MINING_FATIGUE = TagKey.create(Registries.STRUCTURE, new ResourceLocation(Bumblezone.MODID, "bee_queen_mining_fatigue"));
    public static final TagKey<Structure> SEMPITERNAL_SANCTUMS = TagKey.create(Registries.STRUCTURE, new ResourceLocation(Bumblezone.MODID, "sempiternal_sanctums"));

    public static final TagKey<MobEffect> DISALLOWED_INCENSE_CANDLE_EFFECTS = TagKey.create(Registries.MOB_EFFECT, new ResourceLocation(Bumblezone.MODID, "incense_candle/disallowed_effects"));
    public static final TagKey<MobEffect> LIFE_CURE_EFFECTS = TagKey.create(Registries.MOB_EFFECT, new ResourceLocation(Bumblezone.MODID, "essence/life/cure_effects"));
    public static final TagKey<MobEffect> RADIANCE_SUN_EFFECTS = TagKey.create(Registries.MOB_EFFECT, new ResourceLocation(Bumblezone.MODID, "essence/radiance/sun_effects"));
    public static final TagKey<MobEffect> RAGING_RAGE_EFFECTS = TagKey.create(Registries.MOB_EFFECT, new ResourceLocation(Bumblezone.MODID, "essence/raging/rage_effects"));

    public static final TagKey<PoiType> IS_NEAR_BEEHIVE_ADVANCEMENT_TRIGGER_POI = TagKey.create(Registries.POINT_OF_INTEREST_TYPE, new ResourceLocation(Bumblezone.MODID, "is_near_beehive_advancement_trigger"));

    public static final TagKey<Enchantment> DISALLOWED_CRYSTALLINE_FLOWER_ENCHANTMENTS = TagKey.create(Registries.ENCHANTMENT, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/disallowed_enchantments"));
    public static final TagKey<Enchantment> FORCED_ALLOWED_CRYSTALLINE_FLOWER_ENCHANTMENTS = TagKey.create(Registries.ENCHANTMENT, new ResourceLocation(Bumblezone.MODID, "crystalline_flower/forced_allowed_enchantments"));
    public static final TagKey<Enchantment> ITEM_WITH_TELEPORT_ENCHANT = TagKey.create(Registries.ENCHANTMENT, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/any_item_right_clicked_beehive_with_enchant"));
    public static final TagKey<Enchantment> ITEM_WITH_TELEPORT_ENCHANT_CROUCHING = TagKey.create(Registries.ENCHANTMENT, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/any_item_right_clicked_beehive_with_enchant_crouching"));
    public static final TagKey<Enchantment> ENCHANT_SPECIAL_DEDICATED_COMPAT = TagKey.create(Registries.ENCHANTMENT, new ResourceLocation(Bumblezone.MODID, "dimension_teleportation/enchant_special_dedicated_compat"));

    public static final TagKey<Biome> THE_BUMBLEZONE = TagKey.create(Registries.BIOME, new ResourceLocation(Bumblezone.MODID, "the_bumblezone"));

    public static final TagKey<PlacedFeature> RESOURCEFUL_BEES_COMBS = TagKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Bumblezone.MODID, "resourceful_bees_combs"));
    public static final TagKey<PlacedFeature> PRODUCTIVE_BEES_COMBS = TagKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Bumblezone.MODID, "productive_bees_combs"));

    @ExpectPlatform
    public static String getPlatformTagNamespace() {
        throw new NotImplementedException("BzTags.getPlatformTagNamespace");
    }
}
