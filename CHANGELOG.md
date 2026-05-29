### **(V.7.12.0 Changes) (1.20.1 Minecraft)**

#### Biomes:
Replaced the Carvable Wax roads Surface Rule for Floral Meadow biome to be done as a feature instead. 
 Reduces memory allocation spam in that biome generation. Required changes to some Bumblezone worldgen JSON files.

Made Pollinated Field biome's pollen surface feature be better optimized with biome checks.

Made Pollinated Field and Pollinated Pillar biome's pollen filled caves be wider and easier to traverse.

#### Features:
Optimized Honeycomb Caves a tiny bit.


### **(V.7.11.5 Changes) (1.20.1 Minecraft)**

#### Features:
Slightly optimized the modded comb ore placing feature to skip a spot if it is surrounded by air.

#### Misc:
Optimized various part of the codebase to try and reduce memory object allocation spam a little bit. 


### **(V.7.11.4 Changes) (1.20.1 Minecraft)**

#### Misc:
Hey remember that mixin I deleted in previous update because it seemed like the Mojang bug was fixed?
 The mojang bug wasn't fixed... Added back mixin to prevent worldgen deadlock
 BUT I adjusted the mixin to not cause a crash with certain combinations of mods on.
 (Edit: Turns out this wasn't a mojang bug but a ModernFix + Smooth Chunk Save interaction bug 
 that crashed my original mixin but my workaround does stop the crash)


### **(V.7.11.3 Changes) (1.20.1 Minecraft)**

#### Fluids:
Fixed Sugar Water making Sugar Cane growing replace solid blocks.

#### Misc:
Deleted a mixin that aimed to fix MC-246262, but it seems Mojang fixed that bug a long time ago.
Slim chance that this mixin might had been involved with some server client-bound chunk data packet issue.

#### Lang:
zh_cn.json lang file updated by MechtaSnezhevna
