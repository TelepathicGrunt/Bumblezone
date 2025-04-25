### **(V.7.8.13 Changes) (1.21.1 Minecraft)**

#### Mod Compat:
Added compat with EnvironmentZ so Bumblezone dimension is around 96 degrees Fahrenheit!
Special thanks to KrimZik for making the compat JSON file! See their other EnvironmentZ compat here: https://modrinth.com/datapack/environmentz-compats


### **(V.7.8.12 Changes) (1.21.1 Minecraft)**

#### Blocks:
Made Essence Blocks not push/damage players in getCollisionShape method but in entityInside instead.

#### Mod Compat:
Opt-out of Neruina's block suspending system to ensure a hard crash when Essence Blocks breaks.
 This will prevent issues with unbreakable arenas that are permanent/never despawning. 
 Issue with my block should be a hard crash and reported to me ASAP.

