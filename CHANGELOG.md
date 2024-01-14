### **(V.6.9.1 Changes) (1.19.2 Minecraft)**

##### Effects:
Getting Protection of the Hive now spawns a bit of particles at player head to signal the buff has been refreshed.

Fixed Protection of the Hive being granted at multiple levels when it is only supposed to be level 1.

##### Structures:
Spider Infested Bee Dungeon is now set to spawn more often by changing default config value for them.
 Edit your configs to set spiderInfestedBeeDungeonRarity to 5 if Bumblezone config file is already made.

1/5th of the Battle Cube structure's spawners will now be Cave Spider instead of Spider spawners.


### **(V.6.9.0 Changes) (1.19.2 Minecraft)**

##### Structures:
Added more rooms to Cell Maze! Special thanks to Tera for the new pieces!

Made caves not carve into Cell Maze structures.

##### Blocks:
Changed Crystalline Flower so that it sorts enchantments on clientside based on the actual translated names of enchantments.
 Will be much easier to find the enchantment you want.

Crystalline Flower will now hold onto items in its consume and enchant slots in the block itself instead of returning them when exiting UI.

Added two new entity type tags for marking entities as immune to the slowdown effects from Sticky Honey Residue and Honey Web blocks:
 `the_bumblezone:honey_web/cannot_slow`
 `the_bumblezone:sticky_honey_residue/cannot_slow`

Fixed Honey Fluid and Royal Jelly Fluid having a too bright overlay when inside it at night in Overworld.

Honey Fluid and Royal Jelly Fluid now will flow faster in warmer biomes! And slower in really cold biomes.

Fixed Honey Fluid and Royal Jelly Fluid bucket able to place the fluid in ultrawarm dimensions like the Nether. Now they turn into Glistering Honey Crystal block.

##### Items:
Fixed it so that Honey Compasses locked to a structure will ignore y value difference when showing distance to target when advanced tooltips is on.
