### **(V.6.9.8 Changes) (1.19.2 Minecraft)**

##### Dimension:
Fixed bees despawning too close to player if dimension has more than 1 player in it and spread out.


### **(V.6.9.7 Changes) (1.19.2 Minecraft)**

##### Blocks:
Fixed it so Silk Touch mining Honeycomb Brood Blocks does not cause Wrath of the Hive effect.

Made Honeycomb Brood Blocks not spawn mobs if the doTileDrops gamerule is set to false.

Waterlogged Honey Cocoons will not drop items if doTileDrops gamerule is set to false.

##### Fluids:
Royal Jelly Fluid cannot be picked up with a Glass Bottle in world now to prevent people from wasting it due to how valuable it is.
 A message will appear saying to us a Bucket instead.


### **(V.6.9.6 Changes) (1.19.2 Minecraft)**

##### Dimension:
More optimization attempts for worldgen. See if this makes dimension load chunks faster...

##### Teleportation:
Added a check for portal cooldown on player and entities.
 If cooldown is active on entity (even if set by Nether Portal which is 15 second cooldown), you won't be able to teleport to Bumblezone.
 Exiting Bumblezone adds a 15 second portal cooldown on entity as well.
 This should help prevent teleportation loops issues.


### **(V.6.9.5 Changes) (1.19.2 Minecraft)**

##### Items:
Adjusted some code to make extra sure that killed bees with stingers only drop 1 Bee Stinger instead of multiple when other certain mods are present.

##### Enchantments:
Comb Cutter now treats negative Mining Fatigue as level 4 fatigue properly just like vanilla.

##### Dimension:
Tried some optimizations for cave generation. Chunks in the dimension may generate a bit faster now for people.