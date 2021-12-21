### **(V.4.0.2 Changes) (1.18.1 Minecraft)**

##### Fluids:
Fixed weird interactions with modded fluids bordering Bumblezone's fluids.
 Such as Honey Fluid turning any water-tagged modded fluid into Sugar Water regardless of that that other fluid is.
 Solution was more tags.
 the_bumblezone:fluids/convertible_to_sugar_water
 forge:fluids/visual/honey
 forge:fluids/visual/water


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