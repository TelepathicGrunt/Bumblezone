### **(V.7.8.13 Changes) (1.21.1 Minecraft)**

#### Misc:
Made the welcome message and near beehive advancement only show when within 3 blocks of a beehive instead of 8 blocks.

Adjusted welcome message to state advancements have info for entering/exiting dimension.

#### Mod Compat:
Added compat with EnvironmentZ so Bumblezone dimension is around 96 degrees Fahrenheit!
Special thanks to KrimZik for making the compat JSON file! See their other EnvironmentZ compat here: https://modrinth.com/datapack/environmentz-compats

Fixed bug where Ars Elemental projectiles can still teleport player to Bumblezone without Blink on.


### **(V.7.8.12 Changes) (1.21.1 Minecraft)**

#### Blocks:
Made Essence Blocks not push/damage players in getCollisionShape method but in entityInside instead.

#### Mod Compat:
Opt-out of Neruina's block suspending system to ensure a hard crash when Essence Blocks breaks.
 This will prevent issues with unbreakable arenas that are permanent/never despawning. 
 Issue with my block should be a hard crash and reported to me ASAP.

