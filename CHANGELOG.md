### **(V.7.14.2 Changes) (1.21.1 Minecraft)**

#### Biomes:
Fuzzed the Pile of Pollen edges of Pollinated Fields biome so it looks a bit better.

#### Features:
Made the honeycomb holes feature be able to replace fewer blocks now to prevent it from eating away at other features.

#### Structures:
Adjusted dimension's terrain noise range so that terrain adaption (what adds and removes land around structures) looks a bit better.

#### Enchantments:
Comb Cutter now works if enchanted Shears is in the off-hand slot when extracting honeycombs from Beehives/Bee Nest blocks.


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


### **(V.7.13.6 Changes) (1.21.1 Minecraft)**

#### Features:
Slightly optimized the modded comb ore placing feature to skip a spot if it is surrounded by air.

#### Misc:
Optimized various part of the codebase to try and reduce memory object allocation spam a little bit. 


### **(V.7.13.5 Changes) (1.21.1 Minecraft)**

#### Misc:
Hey remember that mixin I deleted in previous update because it seemed like the Mojang bug was fixed?
 The mojang bug wasn't fixed... Added back mixin to prevent worldgen deadlock 
 BUT I adjusted the mixin to not cause a crash with certain combinations of mods on.
 (Edit: Turns out this wasn't a mojang bug but a ModernFix + Smooth Chunk Save interaction bug 
 that crashed my original mixin but my workaround does stop the crash)


### **(V.7.13.3 Changes) (1.21.1 Minecraft)**

#### Fluids:
Fixed Sugar Water making Sugar Cane growing replace solid blocks.

#### Misc:
Deleted a mixin that aimed to fix MC-246262, but it seems Mojang fixed that bug a long time ago.
 Slim chance that this mixin might had been involved with some server client-bound chunk data packet issue.

#### Lang:
zh_cn.json lang file updated by MechtaSnezhevna