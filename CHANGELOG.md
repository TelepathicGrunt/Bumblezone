### **(V.6.9.6 Changes) (1.19.2 Minecraft)**

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