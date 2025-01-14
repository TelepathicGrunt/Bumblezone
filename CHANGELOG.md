### **(V.7.7.15 Changes) (1.21.1 Minecraft)**

#### Blocks:
Fixed some Crystalline Flower items not giving correct XP.

Fixes Crystalline Flower consume button showing when clicking despite no consume item.

Fixed int overflow when trying to consume a stack of max-tier granting items in Crystalline Flower.

Made the inside planes for Glistering Honey Crystal be cutout render type.


### **(V.7.7.14 Changes) (1.21.1 Minecraft)**

#### Features:
Fixed logspam due to not setting POI correctly on Honeycomb Brood Blocks in large Honeycomb Hole features.

Improved generation speed of the Pile of Pollen surface feature. Pollinated Fields and Pollinated Pillar biomes should spawn faster.

Pollinated Fields and Pollinated Pillar biome's caves are now slightly taller to allow easier exploring.

#### Structures:
Honey Cave Room structures can spawn exposed in walls now. Slightly reduced spawnrate to compensate.

Reduced number of Jigsaw Blocks and slightly reduce amount of pieces in Gazebuzz Cluster structure so its layout can generate twice as fast (about 3 seconds previously)

Fixed some outdated Crystalline Flower logspam related to spawning certain structures nbt.

#### Mod Compat:
Changed a bit of code to work better with C2ME (Concurrent Chunk Management Engine).


### **(V.7.7.13 Changes) (1.21.1 Minecraft)**

#### Biomes:
Fixed bug where biome borders were all weird squared-like between certain biomes. Should be fixed now for good.


### **(V.7.7.12 Changes) (1.21.1 Minecraft)**

#### Structures:
Fixed an issue where processors in Structures were running their logic too often in side chunks they didn't need to run in.

#### Features:
Improve generation speed of the large honeycomb holes in walls.

#### Processors:
Optimized some processors that run for structures/features. Makes worldgen a slight bit faster.

#### Blocks:
Improved speed for Sticky Honey Residue's check if it can survive at a spot.
 This will slightly improve worldgen speed for Crystal Canyon biome.

#### Mod Compat:
Allowed Enderman Overhaul's Bubble Pearl to teleport players to Bumblezone.


### **(V.7.7.11 Changes) (1.21.1 Minecraft)**

#### Dimension:
Replaced 2 Surface Rules with a feature and adjusted rest of Surface Rules to make worldgen faster.


### **(V.7.7.10 Changes) (1.21.1 Minecraft)**

#### Blocks:
Improved canSurvive check with StickyHoneyResidue. Makes worldgen placing of this block a bit faster.

#### Features:
Unhardcoded the possible candles that can spawn in Bee Dungeon and Spider Infested Bee Dungeon. They are now controlled by these two block tags:
 `the_bumblezone:bee_dungeon/possible_candles`
 `the_bumblezone:spider_infested_bee_dungeon/possible_candles`

Turned off neighbor shape update when placing giant honeycomb holes in the walls during worldgen as it is not needed to be ran.
 Improves worldgen speed a bit.

Improved worldgen time a bit for caves in Bumblezone

#### Structures:
Stopped checking "selection_priority" in Jigsaw Blocks for Bumblezone structures since Bumblezone doesn't use it. Speeds up structure layout generation a little bit.

#### Dimension:
Tried to cleanup and adjust Surface Rules to make worldgen a bit faster.

#### Mod Compat:
Added icons for Bumblezone biomes when viewed with Emi Ores.

Fixed code to properly swap bees in structures rarely with modded bees.


### **(V.7.7.9 Changes) (1.21.1 Minecraft)**

#### Entities:
Fixed Bee Queen trades not giving correct amount of rewards.


### **(V.7.7.8 Changes) (1.21.1 Minecraft)**

#### Items:
Fixed Bee-laxing with the Hom-bees Music Disc missing Bandcamp link in tooltip.

#### Misc:
Fixed a possible bug where the no-feature-spawning-in-structure code could crash on edge of super massive structures.


### **(V.7.7.7 Changes) (1.21.1 Minecraft)**

#### Items:
Fixed Carpenter Bee Boots not gripping comb/wood/leaves walls properly.

Fixed Beena Box Music Disc not having translated description.


### **(V.7.7.6 Changes) (1.21.1 Minecraft)**

#### Blocks:
Fixed floating in Windy Air causing client disconnect from servers where server has flying disabled.

#### Items:
Fixed flying with Bumblebee Chestplate causing client disconnect from servers where server has flying disabled.


### **(V.7.7.5 Changes) (1.21.1 Minecraft)**

#### Blocks:
Pile of Pollen will now affect the fallDistance field on the entity falling into it.
 Meaning now it should properly reduce fall damage when falling into it where before, it did not change the fall damage based on your fall height.

#### Items:
Made Life Essence not turn Dead Bush into certain modded Saplings now by no longer using #saplings tag in
 the `the_bumblezone:essence/life/dead_bush_revives_to` block tag.

#### Structures:
Fixed features able to spawn in structures that they are not supposed to spawn in. 
 Example, Giant Glistering Honey Crystals spawning in Throne Pillar's room.

#### Mod Compat:
EMI, REI, and JEI will have info pages for flowers that show up in Hanging Garden and for blocks that can support Crystalline Flower.
