### **(V.7.0.18 Changes) (1.20.1 Minecraft)**

##### Mod Compat:
Improved PneumaticCraft boot jetpack disabling by using their API. Requires PneumaticCraft v6.0.9 or newer


### **(V.7.0.17 Changes) (1.20.1 Minecraft)**

##### Misc:
(Forge): Setup my networking protocol so that clients with Bumblezone must be using same Bumblezone version as server.
 This is because mismatched Bumblezone versions tends to cause many problems. Stay in sync with the server's version.


### **(V.7.0.16 Changes) (1.20.1 Minecraft)**

##### Items:
Attempted to improve Honey Compass's ability to remain linked to the async structure searching even when compass is 
 put into inventory while it is still searching.

Fixed eating a stack of Bee Soup consuming the entire stack instead of just 1.

##### Structures:
Made Honey Compass that points to Sempiternal Sanctums have different descriptions based on the sanctum it is locating.
 Sempiternal Sanctums Honey Compasses are a bit more common now after user consumed Essence of the Bees.
 Luck attribute/status effect makes Sempiternal Sanctum honey Compasses have higher chance of showing up.

Players who consumed Essence of the Bees should not get Wrath effect anymore in Cell Maze. Not sure how I missed that bug...

Updated structure nbts. Might save a little bit of memory in regard to DFU.

Added carpets as welcome mats in Honey Slime Ranch structure to prevent Villager from leaving the house.

Slightly reduced Rabbit Foot loot in Ancient Hoops structure.

#### Entities:
Fixed feeding a tamed Beehemoth Royal Jelly Bottle/Bucket or Bee Bread does not consume the food.

#### Biomes:
Fixed music and beehive ambience not playing in bumblezone biomes.


### **(V.7.0.15 Changes) (1.20.1 Minecraft)**

##### Entities:
Fixed Sentry Watcher not saving and keeping their owner UUID in NBT. 
 For those spawned by egg and now unkillable, use /kill command to eliminate them. Sorry about that.

Made Sentry Watcher Spawn Egg save Creative Mode players as owner now too.

Capped Sentry Watcher max speed to try and prevent stuff like -34616847.45 momentum. Don't ask how someone managed to accomplish that.