### **(V.3.1.3 Changes) (1.16.5 Minecraft)**

##### Teleportation:
Sped up an edge case with teleporting.

Took an extra precaution against riding entities when exiting bumblezone could cause issues/


### **(V.3.1.2 Changes) (1.16.5 Minecraft)**

##### Teleportation:
Fixed bug where setting Teleportation Mode to 2 or 3 and trying to exit Bumblezone on an entity that originated from Bumblezone would spawn player under Bedrock in Overworld.


### **(V.3.1.1 Changes) (1.16.5 Minecraft)**

##### Entities:
Beehemoth will now stay still when crouch right-clicked like how you make dogs and cats sit. 
 If it is sitting, shift right click with empty hand to remove saddle. Shift right click while holding any item to make it unsit (or ride it to unsit)
 To feed Beehemoth, just right click while holding any honey feeding item. No crouching needed.

Right clicking Beehemoth without crouching will always make you ride the mob. No longer need an empty hand.

##### Lang:
Added english translations for advancements to the other language files so the translation keys don't show.


### **(V.3.1.0 Changes) (1.16.5 Minecraft)**

##### Advancements:
Added new advancements for this mod to add some progression and high xp rewards! 
 Note: Progress or conditions met before this update will not contribute towards the new advancements.

##### Configs:
Significantly cleaned up the config system backend code for my mod. Let me know if any config no longer works but it should be ok!

##### Entities:
Fixed Honey Buckets not able to convert Slime into Honey Slime. (Was an issue with the tag that controls what blocks can convert Slime)

Fixed Honey Slime scaling for adult to not be massive.

Fixed Honey Slime not giving Honey Bottle when harvested with Glass Bottle.

Honey Slimes have a bit more health, a bit more attack power, and will jump faster and higher when angry as well as stay angry longer.

Killing adult Honey Slimes with looting enchantment may drop Sugar and/or Slime Balls. 
 Before, it was the child that had this feature instead of the adult by mistake.

##### Effects:
Protection of the Hive on the player will now be removed if you hurt a Bee outside The Bumblezone dimension.

##### Recipes:
Renamed the_bumblezone:beeswax_planks_to_bee_nests recipe json to the_bumblezone:beehive_beeswax_to_bee_nest.

##### Mod Compat
Added code to skip compat with v1.16.5-7.1.1 Caves and Cliffs Backport mod because that version crashes Bumblezone. 
 Please use and older or newer version of that mod to enable compat with their candles again.