### **(V.7.2.22 Changes) (1.20.1 Minecraft)**

##### Items:
Honey Compasses used in Creative Mode will now say they spawned a new compass in inventory rather than failed to locate.

(Fabric/Quilt): Made Honey Bucket, Royal Jelly Bucket, Royal Jelly Bottle, Sugar Water Bucket, and Sugar Water Bottle all use the Fabric API's FluidStorage API.
 So these items are now seen as containing the Bumblezone fluids. Vanilla Honey Bottle is untouched to prevent mod compat issues.

##### Blocks:
Fixed possible rare Honey Cocoon crash during worldgen if it is replaced with air before chunk is fully made.

##### Fluids:
(Fabric/Quilt): Backported diagonal textures for Honey Fluid and Royal Jelly Honey Fluid. Forge cannot support this due to lack of API.


### **(V.7.2.21 Changes) (1.20.1 Minecraft)**

##### Fluids:
New textures for Honey Fluid and Royal Jelly Honey Fluid! Special thanks to Kryppers for creating these new textures!

##### Items:
Fixed Flower Headwear not respect item cooldowns disabling item abilities.

##### Entity:
Fix Rootmin crash when certain combinations of mods are on.
