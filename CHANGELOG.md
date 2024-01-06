### **(V.6.8.7 Changes) (1.19.2 Minecraft)**

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

Fixed Honey Fluid having a too bright overlay when inside it at night in Overworld.

##### Items:
Fixed it so that Honey Compasses locked to a structure will ignore y value difference when showing distance to target when advanced tooltips is on.


### **(V.6.8.6 Changes) (1.19.2 Minecraft)**

##### Blocks:
Made Sticky Honey Residue and Sticky Honey Redstone be visually closer to the block they are attached to.

Fixed Sticky Honey Residue and Sticky Honey Redstone textures being flipped when facing north, west, or east.

Slightly adjusted textures of Sticky Honey Residue, Sticky Honey Redstone, Honey Web, and Redstone Honey Web.

##### Teleportation:
Properly break and drop blocks that would've suffocated the player when teleporting to and from Bumblezone.

##### Mod Compat:
Removed Better Archeology's Growth Totems from Bumblezone's Hanging Garden and from Honey Cocoon loot.

Show tenth place for Bee Queen trades in REI/EMI/JEI if the trade chance is below 1%


### **(V.6.8.5 Changes) (1.19.2 Minecraft)**

##### Items:
Rebalances Food and Saturation of Bee Bread

##### Mod Compat:
(Forge): With help from a ton of people, we narrowed down seemingly random client disconnects from server due to how me and others
 were registering our custom entity data serializers. This Bumblezone jar should fix it on my end by using the Forge registry.