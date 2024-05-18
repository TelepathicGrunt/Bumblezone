### **(V.7.3.8 Changes) (1.20.1 Minecraft)**

##### Config:
Added alternativeFluidToReplaceHoneyFluid config option to allow swapping Bumblezone Honey Fluid in worldgen with another fluid.
Give it the resourcelocation of the target fluid to swap to.

##### Lang:
Updated zh_cn (Special thanks to TskimiSeiran)


### **(V.7.3.7 Changes) (1.20.1 Minecraft)**

##### Structures:
Throne-locating Honey Compasses will now be marked if they are in inventory of a player entering the compass's located Throne Pillar structure.
 When an unmarked Throne-locating Honey Compasses is in bounds of a Throne Pillar structure without Bee Queen,
 the Bee Queen will be respawned and a Royal Jelly Fluid block restored farther above.
 Controlled by beeQueenRespawning config.


### **(V.7.3.6 Changes) (1.20.1 Minecraft)**

##### Fluids:
Fixed fluid fog/overlay rendering when player stands under falling honey fluids.

Fixed Bumblezone fluid filled buckets able to place fluids in claimed chunks that should block placing.

##### Entities:
Fixed Cosmic Crystal Entity not despawning if killed outside the Sempiternal Sanctum event.


### **(V.7.3.5 Changes) (1.20.1 Minecraft)**

##### Items:
Allow Infinity enchantment to work properly with Bee Stinger being used as bow ammo.

##### Enchantment:
Made it so items with pre-existing Neurotoxin enchantment level greater than neurotoxinMaxLevel config
 will now cap it's actual effect power to the config value's level.

##### Structures:
Fixed plants not spawning in Green Sempiternal Sanctum.

##### Mod Compat:
Fixed config for allowing Productive Bees bees spawning in bumblezone being called blacklistedBees when it was really supposed to be allowedBees.
 Removed Diamond Bee from being allowed.

Removed Spectrum's Nephrite flower and leaves from Hanging Gardens. Kept Nephrite leaves in Sempiternal Sanctums


### **(V.7.3.4 Changes) (1.20.1 Minecraft)**

##### Misc:
Fixed worldgen deadlock if a horse with an owner UUID is spawned during world generation.
