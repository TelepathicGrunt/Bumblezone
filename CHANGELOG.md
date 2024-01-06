### **(V.7.1.15 Changes) (1.20.1 Minecraft)**

##### Structures:
Added more rooms to Cell Maze and Sempiternal Sanctums! Special thanks to Tera for the new pieces!

Made caves not carve into Cell Maze structures.

Made Tree Dungeons only place pillars of dirt down about 10 blocks instead of forever downward.

##### Blocks:
Changed Crystalline Flower so that it sorts enchantments on clientside based on the actual translated names of enchantments.
 Will be much easier to find the enchantment you want.

Added two new entity type tags for marking entities as immune to the slowdown effects from Sticky Honey Residue and Honey Web blocks:
 `the_bumblezone:honey_web/cannot_slow`
 `the_bumblezone:sticky_honey_residue/cannot_slow`

Fixed Honey Fluid having a too bright overlay when inside it at night in Overworld.

(Fabric/Quilt): Fixed Honey Fluid fog color being wrong in certain dimensions and applying thick fog too early before fully submerged.

##### Items:
Fixed it so that Honey Compasses locked to a structure will ignore y value difference when showing distance to target when advanced tooltips is on.

##### Entities:
Added quarterTurns option to Sentry Watcher nbt. Set this in the entity to true or 1b for the entity to do quarter turns instead of half turns.

Fixed Purple Spike inflicting poison effect that doesn't remove itself when its effect timer runs out.

##### Teleportation:
Fixed infinite loop if teleporting to and from Bumblezone is cancelled by another mod or script.

##### Mod Compat:
(Fabric): Spectrum Jetpacks's jetpacks will now be disabled when in Heavy Air block


### **(V.7.1.14 Changes) (1.20.1 Minecraft)**

##### Blocks:
Made Sticky Honey Residue and Sticky Honey Redstone be visually closer to the block they are attached to.

Fixed Sticky Honey Residue and Sticky Honey Redstone textures being flipped when facing north, west, or east.

Slightly adjusted textures of Sticky Honey Residue, Sticky Honey Redstone, Honey Web, and Redstone Honey Web.

##### Lang:
Fixed Rootmin Shooting lang entry

Special thanks to Unroman for updating uk_ua lang file!

##### Teleportation:
Properly break and drop blocks that would've suffocated the player when teleporting to and from Bumblezone.

##### Mod Compat:
Removed Better Archeology's Growth Totems from Bumblezone's Hanging Garden and from Honey Cocoon loot.

Show tenth place for Bee Queen trades in REI/EMI/JEI if the trade chance is below 1%


### **(V.7.1.13 Changes) (1.20.1 Minecraft)**

##### Items:
Rebalances Food and Saturation of Bee Bread and Bee Soup

##### Mod Compat:
(NeoForge): With help from a ton of people, we narrowed down seemingly random client disconnects from server due to how me and others 
 were registering our custom entity data serializers. This Bumblezone jar should fix it on my end by using the Forge registry.