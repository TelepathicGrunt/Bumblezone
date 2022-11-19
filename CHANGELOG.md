### **(V.6.4.2 Changes) (1.19.2 Minecraft)**

##### Misc:
Fixed bug with vanilla Bees that make them not threadsafe when created as part of worldgen threaded chunk creation.
 Very niche rare crash to even get to happen but good for me to still patch just in case it does cause issues for people rarely.
 Could be cause of some crashes on AoF6.

##### Fluids:
Significantly fixed rendering issues with Honey Fluid and Royal Jelly Fluid.
 Also fixed particles from fluid spawning way out of the fluid at times.


### **(V.6.4.1) (1.19.2 Minecraft)**

##### Fluids:
Fixed Royal Jelly Fluid missing visual overlays and player physics when inside fluid.
 Had to update because this is a big issue. Can't have straight up broken fluids...

Fixed being able to "jump" while on ground inside Royal Jelly Fluid and Sugar Water Fluid.

##### Structures:
Increased number of threads spawned by Bumblezone so multiple Hive Temple maps and Honey Compasses can search at once from 1 to 3.

##### Misc:
Fixed LGBT+ and Ukraine Bee Skin configs fighting each other on applying. And changed the default config values for them.

Went through and cleaned up and modified several mixins to make some of them less hacky.


### **(V.6.4.0) (1.19.2 Minecraft)**

##### Major:
Ported Fabric Bumblezone stuff to Fabric version to make Fabric version be in parity with v6.4.2 Fabric Bumblezone.
 Updates will remain far and few in between for Fabric Bumblezone or delayed.