### **(V.7.4.7 Changes) (1.20.4 Minecraft)**

##### Structures:
Throne-locating Honey Compasses will now be marked if they are in inventory of a player entering Throne Pillar structure.
 When an unmarked Throne-locating Honey Compasses is in bounds of a Throne Pillar structure without Bee Queen,
 the Bee Queen will be respawned and a Royal Jelly Fluid block restored farther above.


### **(V.7.4.6 Changes) (1.20.4 Minecraft)**

##### Fluids:
Fixed fluid fog/overlay rendering when player stands under falling honey fluids.

Fixed Bumblezone fluid filled buckets able to place fluids in claimed chunks that should block placing.

##### Entities:
Fixed Cosmic Crystal Entity not despawning if killed outside the Sempiternal Sanctum event.


### **(V.7.4.5 Changes) (1.20.4 Minecraft)**

##### Items:
Allow Infinity enchantment to work properly with Bee Stinger being used as bow ammo.

##### Enchantment:
Made it so items with pre-existing Neurotoxin enchantment level greater than neurotoxinMaxLevel config 
 will now cap it's actual effect power to the config value's level.

##### Structures:
Fixed Green Sempiternal Sanctum spawning flower Rootmins and grass now spawning in flower room

##### Mod Compat:
Fixed config for allowing Productive Bees bees spawning in bumblezone being called blacklistedBees when it was really supposed to be allowedBees.
 Removed Diamond Bee from being allowed.

Removed Spectrum's Nephrite flower and leaves from Hanging Gardens. Kept Nephrite leaves in Sempiternal Sanctums

Reactivated Mekanism teleporting multi-tool compat


### **(V.7.4.4 Changes) (1.20.4 Minecraft)**

##### Misc:
Fixed worldgen deadlock if a horse with an owner UUID is spawned during world generation.
