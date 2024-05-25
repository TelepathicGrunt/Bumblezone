### **(V.7.3.9 Changes) (1.20.1 Minecraft)**

##### Structures:
Fixed some typoes in structure processor files that would've caused the processor to not fully function correctly.

##### Entities:
Fixed Honey Slimes not turning to face each other when breeding.

##### Items:
Stingless Bee Helmets will not eject bee passenger from head if player is hurt or attacked anymore.

##### Dimension:
Fixed Sugar Water waterfalls not spawning as they should.

##### Lang:
uk_ua.json updated by Unroman!

zh_cn.json updated by TskimiSeiran!


### **(V.7.3.8 Changes) (1.20.1 Minecraft)**

##### Fluids:
(Forge/NeoForge): Added diagonal textures for Honey Fluid and Royal Jelly Fluid.

##### Items:
Changed Honey Bucket texture and Royal Jelly Bucket textures. Special thanks to crispytwig!

Change texture of Royal Jelly Bottle and Royal Jelly Block.

##### Config:
Added alternativeFluidToReplaceHoneyFluid config option to allow swapping Bumblezone Honey Fluid in worldgen with another fluid.
Give it the resourcelocation of the target fluid to swap to. You would still need to disable the Honey Bucket recipe yourself by datapack.

##### Lang:
Updated zh_cn (Special thanks to TskimiSeiran)

##### Misc:
Compressed more textures.

##### Mod Compat:
Made Simple Hats's Bee On Head hat item is now added to `the_bumblezone:bee_armors/bz_armor_ability_enhancing_wearables` item tag.


### **(V.7.3.7 Changes) (1.20.1 Minecraft)**

##### Structures:
Throne-locating Honey Compasses will now be marked if they are in inventory of a player entering the compass's located Throne Pillar structure.
 When an unmarked Throne-locating Honey Compasses is in bounds of a Throne Pillar structure without Bee Queen,
 the Bee Queen will be respawned and a Royal Jelly Fluid block restored farther above.
 Controlled by beeQueenRespawning config.
