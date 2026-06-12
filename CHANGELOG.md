### **(V.7.15.0 Changes) (1.21.1 Minecraft)**

#### Blocks:
Honey Web and Redstone Honey Web now can be broken by flowing fluids.

#### Enchantments:
Comb Cutter now works if enchanted Shears is in the off-hand slot when extracting honeycombs from Beehives/Bee Nest blocks.

#### Biomes:
Fuzzed the Pile of Pollen edges of Pollinated Fields biome so it looks a bit better.

Further optimized the Carvable Wax road generation in Floral Meadow to skip doing unnecessary work at spots it will for sure will not place roads at.

#### Features:
Made the Honeycomb Holes feature be able to replace fewer blocks now to prevent it from eating away at other features.

Further optimized Honeycomb Caves feature to skip doing unnecessary work at spots it will for sure will not carve.

Add a new small rare feature called Fungus Spore that is a source of Mushroom Block, Shroomlight, Hanging Roots, and Brown Mushrooms.
 Spawns in all Bumblezone biomes.

Added a new feature called Web Bridge that is a wall of Honey Web blocks that connects walls of narrow valleys in Sugar Water Floor, Hive Pillar, Hive Wall, and Crystal Canyon biomes.

#### Structures:
Adjusted dimension's terrain noise range so that terrain adaption (what adds and removes land around structures) looks a bit better.

Improved the shape of terrain adaption around Candle Parkour structure.

Improved the shape of terrain adaption around Mite Fortress structure.

#### Dimension:
Optimized the density function for the dimension terrain a tiny bit to squeeze out a tiny bit more performance.


### **(V.7.14.1 Changes) (1.21.1 Minecraft)**

#### Misc:
Fixed an incredibly rare concurrency modification exception crash when Honey Compass tries to locate a 
 structure while something else is also searching for structures at same time.

#### Structures
Fixed typo in Blue Sempiternal Sanctum translated name.


### **(V.7.14.0 Changes) (1.21.1 Minecraft)**

#### Misc:
Updated the Athena mod dependency that is shipped within Bumblezone jar.
 Fixes incorrect Ambient Occlusion on Filled/Empty Porous Honeycomb Block and Honeycomb Brood Blocks.

#### Biomes:
Replaced the Carvable Wax roads Surface Rules for Floral Meadow biome to be done as a feature instead. 

Replaced the Carvable Wax and Sugar Infused Stone Surface Rules for Howling Construct biome to be done as a feature instead.

Replaced the Glistering Honey Crystal Surface Rules for Buzzing Beepartments biome to be done as a feature instead.

(These surface rule replacements will help reduces memory allocation spam for dimension worldgen)

Made Pollinated Field biome's pollen surface feature be better optimized with biome checks.

Made Pollinated Field and Pollinated Pillar biome's pollen filled caves be wider and easier to traverse.

#### Features:
Optimized Honeycomb Caves a tiny bit.

The balloon and helicopter features will not be covered in Pile of Pollen blocks when in Pollinated Field biome.

Added small spikes to ceiling of most Bumblezone biomes. Help break up the flat ceiling look.
