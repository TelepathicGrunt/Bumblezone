### **(V.6.6.0 Changes) (1.19.3 Minecraft)**

##### Structures:
Made Throne Pillar locating Honey Compasses more common in Cell Maze structure.

Added Bee House structure!

Added Candle Parkour structure!

Added Battle Cubes structure!

##### Misc:
Fixed bug where I was checking for crouching pose instead of if player was holding down shift key. (isCrouching vs isShiftKeyDown)
 Should make it so stuff like Carpenter Bees Boots do not mine automatically if you are in a 1.5 block high space.
 It will only mine if you specifically are holding down crouching key. Same with helmet and leggings behaviors.

##### Tags:
Organized Bumblezone tags much better into sub folders. Changes are:

`the_bumblezone:allowed_hanging_garden_flowers` -> `the_bumblezone:hanging_garden/allowed_flowers` (block tag)

`the_bumblezone:allowed_hanging_garden_tall_flowers` -> `the_bumblezone:hanging_garden/allowed_tall_flowers` (block tag)

`the_bumblezone:allowed_hanging_garden_leaves` -> `the_bumblezone:hanging_garden/allowed_leaves` (block tag)

`the_bumblezone:allowed_hanging_garden_logs` -> `the_bumblezone:hanging_garden/allowed_logs` (block tag)

`the_bumblezone:blacklisted_hanging_garden_flowers` -> `the_bumblezone:hanging_garden/blacklisted_flowers` (block tag)

`the_bumblezone:blacklisted_hanging_garden_tall_flowers` -> `the_bumblezone:hanging_garden/blacklisted_tall_flowers` (block tag)

`the_bumblezone:blacklisted_hanging_garden_leaves` -> `the_bumblezone:hanging_garden/blacklisted_leaves` (block tag)

`the_bumblezone:blacklisted_hanging_garden_logs` -> `the_bumblezone:hanging_garden/blacklisted_logs` (block tag)

`the_bumblezone:flowers_allowed_by_pollen_puff` -> `the_bumblezone:pollen_puff/multiplying_allowed_flowers` (block tag)

`the_bumblezone:flowers_blacklisted_from_pollen_puff` -> `the_bumblezone:pollen_puff/multiplying_disallowed_flowers` (block tag)

`the_bumblezone:carpenter_bee_boots_climbables` -> `the_bumblezone:carpenter_bee_boots/climbables` (block tag)

`the_bumblezone:carpenter_bee_boots_mineables` -> `the_bumblezone:carpenter_bee_boots/mineables` (block tag)

`the_bumblezone:blacklisted_teleportable_hive_blocks` -> `the_bumblezone:dimension_teleportation/blacklisted_teleportable_beehive_blocks` (block tag)

`the_bumblezone:required_blocks_under_hive_to_teleport` -> `the_bumblezone:dimension_teleportation/required_blocks_under_beehive_to_teleport` (block tag)

`the_bumblezone:cave_edge_blocks_for_modded_compats` -> `the_bumblezone:worldgen_checks/cave_edge_blocks_for_modded_compats` (block tag)

`the_bumblezone:honeycombs_that_features_can_carve` -> `the_bumblezone:worldgen_checks/honeycombs_that_features_can_carve` (block tag)

`the_bumblezone:wrath_activating_blocks_when_mined` -> `the_bumblezone:bee_aggression_in_dimension/wrath_activating_blocks_when_mined` (block tag)

`the_bumblezone:blacklisted_honey_compass_blocks` -> `the_bumblezone:honey_compass/beehives_blacklisted_from_position_tracking` (block tag)

`the_bumblezone:blacklisted_bee_cannon_bees` -> `the_bumblezone:bee_cannon/blacklisted_bees` (entity type tag)

`the_bumblezone:hanging_gardens_initial_spawn_entities` -> `the_bumblezone:hanging_garden/initial_spawn_entities` (entity type tag)

`the_bumblezone:pollen_puff_can_pollinate` -> `the_bumblezone:pollen_puff/can_pollinate` (entity type tag)

`the_bumblezone:blacklisted_stingless_bee_helmet_passengers` -> `the_bumblezone:stingless_bee_helmet/blacklisted_passengers` (entity type tag)

`the_bumblezone:bee_armor_repair_items` -> `the_bumblezone:repair_items/bee_armor` (item tag)

`the_bumblezone:bee_cannon_repair_items` -> `the_bumblezone:repair_items/bee_cannon` (item tag)

`the_bumblezone:crystal_cannon_repair_items` -> `the_bumblezone:repair_items/crystal_cannon` (item tag)

`the_bumblezone:honey_crystal_shield_repair_items` -> `the_bumblezone:repair_items/honey_crystal_shield` (item tag)

`the_bumblezone:stinger_spear_items` -> `the_bumblezone:repair_items/stinger_spear` (item tag)

`the_bumblezone:consumable_candle_lighting_items` -> `the_bumblezone:candle_lightables/consumable` (item tag)

`the_bumblezone:damagable_candle_lighting_items` -> `the_bumblezone:candle_lightables/damageable` (item tag)

`the_bumblezone:infinite_candle_lighting_items` -> `the_bumblezone:candle_lightables/infinite` (item tag)

`the_bumblezone:wrath_activating_items_when_picked_up` -> `the_bumblezone:bee_aggression_in_dimension/wrath_activating_items_when_picked_up` (item tag)

`the_bumblezone:bee_feeding_items` -> `the_bumblezone:bee_feedable_items` (item tag)

`the_bumblezone:honey_compass_default_locating` -> `the_bumblezone:honey_compass/default_locating` (structure tag)

`the_bumblezone:honey_compass_from_hive_temple` -> `the_bumblezone:honey_compass/misc_locating` (structure tag)

`the_bumblezone:honey_compass_throne_locating` -> `the_bumblezone:honey_compass/throne_locating` (structure tag)


### **(V.6.5.2 Changes) (1.19.3 Minecraft)**

##### Mod Compat:
Brought back Incense Candle recipe cleanup for REI, JEI, and EMI.

##### Advancement:
Fixed Carvable Wax Advancement not checking for Chain pattern Carvable Wax.


### **(V.6.5.1 Changes) (1.19.3 Minecraft)**

##### Items:
Added Bumblezone items to the Functional Blocks, Colored Blocks, Redstone Blocks, Combat, and Spawn Eggs creative tabs.
  Bumblezone still has its own tab btw.

Bumblezone's creative tab now uses the Honeycomb Brood Block

Slightly improved the coloring/look of Beehemoth Spawn Egg.


### **(V.6.5.0 Changes) (1.19.3 Minecraft)**

##### Misc:
Ported to 1.19.3