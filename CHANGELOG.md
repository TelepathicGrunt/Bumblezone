### **(V.4.1.0 Changes) (1.18.1 Minecraft)**

##### Major:
Now hard depends on Feature NBT Deadlock Be Gone mod to prevent deadlocks with Bumblezone's worldgen caused by vanilla bug.

##### Entities:
Added specialBeeSpawning and nearbyBeesPerPlayerInBz config options which makes Bumblezone handle 
 spawning and despawning vanilla bees in its dimension entirely. This config makes this mod try to 
 always have a set number of vanilla bees near the player as often as possible to make the dimension feel full.
 Vanilla bees that are too far from player will be forcefully despawned unless the bee is name tagged, persistent, or has a hive associated with it.

Fixed Honey Slime not being honey filled for 1 frame after being spawned.

##### Fluids:
Fixed weird interactions with modded fluids bordering Bumblezone's fluids.
 Such as Honey Fluid turning any water-tagged modded fluid into Sugar Water regardless of that that other fluid is.
 Solution was more tags.
 the_bumblezone:fluids/convertible_to_sugar_water
 forge:fluids/visual/honey
 forge:fluids/visual/water

##### Structures:
Fixed Honey Cave Room and Pollinated Streams not spawning.

##### Features:
Bee Dungeon and Spider Infested Bee Dungeon spawns again now.


### **(V.4.0.1 Changes) (1.18.1 Minecraft)**

##### Blocks:
Undid a mixin for Honey Fluid rendering to use an old and fragile mixin instead. Reason is modifyArgs mixin is broken in 
 Forge and when I tried to use modifyArg with storing values I need in a field, it broke rendering Honey Fluid as it is not threadsafe.

Fixed Honey Fluid bottom not rendering when it is slowly falling onto an opaque block.

Fixed Honey Fluid falling faster if there is neighboring Honey Fluid that is also falling.

##### Items:
Fixed Pollen Puff not growing a Pile of Pollen if thrown through a tall stack.

##### Mod Compat:
Fixed crash with some mods asking for getBaseHeight from Bumblezone's chunk generator.


### **(V.4.0.0 Changes) (1.18.1 Minecraft)**

##### Major:
Ported to 1.18.1!

##### Biomes:
Now has ambient pollen particles.

##### Items:
Fixed some bugs with Pollen Puff and Pile of Pollen.