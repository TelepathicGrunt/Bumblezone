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

Honey Fluid and Royal Jelly Fluid not will flow faster in warmer biomes! And slower in really cold biomes.

##### Items:
Fixed it so that Honey Compasses locked to a structure will ignore y value difference when showing distance to target when advanced tooltips is on.
