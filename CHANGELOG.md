### **(V.4.0.2 Changes) (1.18.1 Minecraft)**

##### Entities:
Added specialBeeSpawning and nearbyBeesPerPlayerInBz config options which makes Bumblezone handle
 spawning and despawning vanilla bees in its dimension entirely. This config makes this mod try to
 always have a set number of vanilla bees near the player as often as possible to make the dimension feel full.
 Vanilla bees that are too far from player will be forcefully despawned unless the bee is name tagged, persistent, or has a hive associated with it.

##### Fluids:
Fixed weird interactions with modded fluids bordering Bumblezone's fluids.
 Such as Honey Fluid turning any water-tagged modded fluid into Sugar Water regardless of that that other fluid is. 
 Solution was more tags. 
 the_bumblezone:fluids/convertible_to_sugar_water
 c:fluids/visual/honey
 c:fluids/visual/water


### **(V.4.0.1 Changes) (1.18.1 Minecraft)**

##### Fluids:
Fixed Honey Fluid bottom not rendering when it is slowly falling onto an opaque block.

Fixed Honey Fluid falling faster if there is neighboring Honey Fluid that is also falling.

##### Mod Compat:
Fixed crash with some mods asking for getBaseHeight from Bumblezone's chunk generator.


### **(V.4.0.0 Changes) (1.18.1 Minecraft)**

##### Major:
Ported to 1.18.1!

##### Biomes:
Now has ambient pollen particles.

##### Items:
Fixed some bugs with Pollen Puff and Pile of Pollen.