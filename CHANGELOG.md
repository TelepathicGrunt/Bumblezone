### **(V.4.0.1 Changes) (1.18.1 Minecraft)**

##### Blocks:
Undid a mixin for Honey Fluid rendering to use an old and fragile mixin instead. Reason is modifyArgs mixin is broken in 
 Forge and when I tried to use modifyArg with storing values I need in a field, it broke rendering Honey Fluid as it is not threadsafe.

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