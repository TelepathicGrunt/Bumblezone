### **(V.7.9.2 Changes) (1.21.1 Minecraft)**

#### Items:
(Fabric): Fixed crash with other mods when placing Bumblezone fluid buckets.


### **(V.7.9.1 Changes) (1.21.1 Minecraft)**

#### Structures:
Sempiternal Sanctum essence arenas now will add a tag to all spawned entities in the arena.
These tags are for easier selecting of the entities by datapacks or other mods for easier configuration by modpack makers.
The tags are the following:

- "the_bumblezone.red_essence_arena"
- "the_bumblezone.yellow_essence_arena"
- "the_bumblezone.green_essence_arena"
- "the_bumblezone.blue_essence_arena"
- "the_bumblezone.purple_essence_arena"
- "the_bumblezone.white_essence_arena"

#### Lang:
Argentine Spanish (es_ar.json) translation fixed by Texaliuz!

Turkish (tr_tr.json) translation added by RuyaSavascisi!


### **(V.7.9.0 Changes) (1.21.1 Minecraft)**

#### Entities:
Added `the_bumblezone:dirt_pellet` and `the_bumblezone:event_dirt_pellet` damage types to use for Dirt Pellets throw by player or shot by Rootmin.
Easier to target this damage type now by other mods.

Added `the_bumblezone:event_damage` damage type tag to allow easier disabling damage scaling from a different mod to these damage types.

Fixed Rootmin shooting not shooting from mouth when Rootmin turns.

Adjusted trajectory for Rootmin Dirt Pellet shooting.

Fixed Dirt Pellet hitbox not centered on the texture.

Doubled the damage that thrown Dirt Pellet does to flying mobs now. From damage of 3 to 6 now.

#### Misc:
Made the welcome message and near beehive advancement only show when within 3 blocks of a beehive instead of 8 blocks.

Adjusted welcome message to state advancements have info for entering/exiting dimension.

#### Mod Compat:
Added compat with EnvironmentZ so Bumblezone dimension is around 96 degrees Fahrenheit!
Special thanks to KrimZik for making the compat JSON file! See their other EnvironmentZ compat here: https://modrinth.com/datapack/environmentz-compats

Fixed bug where Ars Elemental projectiles can still teleport player to Bumblezone without Blink on.

#### Lang:
Argentine Spanish (es_ar.json) translation added by Texaliuz!
