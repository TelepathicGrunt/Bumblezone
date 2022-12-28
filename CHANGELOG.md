### **(V.6.6.0 Changes) (1.19.3 Minecraft)**

##### Structures:
Made Throne Pillar locating Honey Compasses more common in Cell Maze structure.

Added Bee House, Candle Parkour, Battle Cubes, Stinger Spear Shrine, Ice Monolith, Overground Flower,
Pirate Ship, Dance Floor, Cannon Range, Honitel, and Honey Fountain structures!

Honitel and Battle Cube structures has a chance of having a Honey Compass that can locate the Throne Pillar structure!

Fixed the giant honeycomb hole feature from eating away at many structures.

Made Honey Cave Room structure much more common so it is more easily found.
 Also improve the bottom of the structure to not be as flat.

##### Items:
Fixed Stinger Spear losing enchantments and damage if thrown, game closed, reopened, and the spear picked up again.

Fixed Crystal Cannon firing center crystal shard backwards.

##### Blocks:
Added String Curtains that bees cannot pathfind through and slightly pushes bees away. Great for making a walk-in bee room with bees escaping!
 Attaches to any solid surface and right clicking with string will extend the block downward.
 Recipe to create the curtain is this with (T) Stick, (S) String, (G) Glass, and (D) a Dye which is optional:
 T T T
 S G S
 S D S

Some tags that the String Curtains uses are:
 `the_bumblezone:string_curtains` (item tag)

 `the_bumblezone:string_curtains` (block tag)

 `the_bumblezone:string_curtains/curtain_extending` (item tag)

 `the_bumblezone:string_curtains/blocks_pathfinding_for_non_bee_mob` (entity tag)

 `the_bumblezone:string_curtains/force_allow_pathfinding` (entity tag)

Empty Porous Honeycomb Block will transform into a Filled Porous Honeycomb Block when touched by a pollinated bee!

Empty Honeycomb Brood Block will transform into a Honeycomb Brood Block with a larva inside when touched by a pollinated bee!

##### Fluids:
Fixed issue where mobs would not see Sugar Water, Honey Fluid, or Royal Jelly Fluid as water when pathfinding.
 Bees should now properly avoid flying into these liquids and suffocating. (Hopefully...)

Non-source Honey Fluid cannot be transformed into source blocks anymore by pollinated bees.
 Stops bees from messing up structures or builds as much as they currently are.

##### Entities:
Throwing/dropping items from player to Bee Queen will now trigger/progress the Bee Queen trade advancements for that player.
 This means dropping 64 stacks of an item is a superfast way to progress the 256 Bee Queen trade advancement.

Fixed some typos in translation keys for Bee Queen saying `beehemoth_queen` instead of `bee_queen`.

##### Effects:
Hopefully remove Wrath of the Hive from more bees properly when the target dies.

##### Biomes:
Fixed biome spawning that was spawning blobs of biomes that shouldn't be blobs.

Renamed nonstandard_biomes field in dimension json to blob_biomes where it takes a biome tag of biomes to spawn as a 
 blob of area. Crystal Canyon and Hive Pillar are added to this by default and can spawn over a medium sized area as a blob.

##### Misc:
Fixed bug where I was checking for crouching pose instead of if player was holding down shift key. (isCrouching vs isShiftKeyDown)
 Should make it so stuff like Carpenter Bees Boots do not mine automatically if you are in a 1.5 block high space.
 It will only mine if you specifically are holding down crouching key. Same with helmet and leggings behaviors.

Silence "Hanging entity at invalid position" logspam from vanilla by lowering the logging level of that event from error to debug level.
 Mojang bug report: https://bugs.mojang.com/browse/MC-252934
 The Item Frame spawned from nbt files still places as intended and functions as intended.
 This was just annoying logspam I decided to yeet.

##### Recipes:
Organized Bumblezone recipe json files much better into sub folders. Cleaner and easier to browse.

##### Tags:
Added the following new tags:

`the_bumblezone:dimension_teleportation/forced_allowed_teleportable_blocks` (block tag)

`the_bumblezone:honey_compass/forced_allowed_position_tracking` (block tag)

`the_bumblezone:stingless_bee_helmet/forced_allowed_passengers` (entity tag)


Organized Bumblezone tags much better into sub folders. Changes are:

`the_bumblezone:allowed_hanging_garden_flowers` -> `the_bumblezone:hanging_garden/allowed_flowers` (block tag)

`the_bumblezone:allowed_hanging_garden_tall_flowers` -> `the_bumblezone:hanging_garden/allowed_tall_flowers` (block tag)

`the_bumblezone:allowed_hanging_garden_leaves` -> `the_bumblezone:hanging_garden/allowed_leaves` (block tag)

`the_bumblezone:allowed_hanging_garden_logs` -> `the_bumblezone:hanging_garden/allowed_logs` (block tag)

`the_bumblezone:blacklisted_hanging_garden_flowers` -> `the_bumblezone:hanging_garden/forced_disallowed_flowers` (block tag)

`the_bumblezone:blacklisted_hanging_garden_tall_flowers` -> `the_bumblezone:hanging_garden/forced_disallowed_tall_flowers` (block tag)

`the_bumblezone:blacklisted_hanging_garden_leaves` -> `the_bumblezone:hanging_garden/forced_disallowed_leaves` (block tag)

`the_bumblezone:blacklisted_hanging_garden_logs` -> `the_bumblezone:hanging_garden/forced_disallowed_logs` (block tag)

`the_bumblezone:flowers_allowed_by_pollen_puff` -> `the_bumblezone:pollen_puff/multiplying_allowed_flowers` (block tag)

`the_bumblezone:flowers_blacklisted_from_pollen_puff` -> `the_bumblezone:pollen_puff/multiplying_forced_disallowed_flowers` (block tag)

`the_bumblezone:carpenter_bee_boots_climbables` -> `the_bumblezone:carpenter_bee_boots/climbables` (block tag)

`the_bumblezone:carpenter_bee_boots_mineables` -> `the_bumblezone:carpenter_bee_boots/mineables` (block tag)

`the_bumblezone:blacklisted_teleportable_hive_blocks` -> `the_bumblezone:dimension_teleportation/disallowed_teleportable_beehive_blocks` (block tag)

`the_bumblezone:required_blocks_under_hive_to_teleport` -> `the_bumblezone:dimension_teleportation/required_blocks_under_beehive_to_teleport` (block tag)

`the_bumblezone:cave_edge_blocks_for_modded_compats` -> `the_bumblezone:worldgen_checks/cave_edge_blocks_for_modded_compats` (block tag)

`the_bumblezone:honeycombs_that_features_can_carve` -> `the_bumblezone:worldgen_checks/honeycombs_that_features_can_carve` (block tag)

`the_bumblezone:wrath_activating_blocks_when_mined` -> `the_bumblezone:bee_aggression_in_dimension/wrath_activating_blocks_when_mined` (block tag)

`the_bumblezone:blacklisted_honey_compass_blocks` -> `the_bumblezone:honey_compass/beehives_disallowed_from_position_tracking` (block tag)

`the_bumblezone:blacklisted_bee_cannon_bees` -> `the_bumblezone:bee_cannon/disallowed_bee_pickup` (entity type tag)

`the_bumblezone:hanging_gardens_initial_spawn_entities` -> `the_bumblezone:hanging_garden/initial_spawn_entities` (entity type tag)

`the_bumblezone:pollen_puff_can_pollinate` -> `the_bumblezone:pollen_puff/can_pollinate` (entity type tag)

`the_bumblezone:blacklisted_stingless_bee_helmet_passengers` -> `the_bumblezone:stingless_bee_helmet/disallowed_passengers` (entity type tag)

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
