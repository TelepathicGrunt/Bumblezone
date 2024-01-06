### **(V.7.2.0 Changes) (1.20.1 Minecraft)**

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

Fixed Honey Fluid and Royal Jelly Fluid having a too bright overlay when inside it at night in Overworld.

Fixed Honey Fluid and Royal Jelly Fluid making world too dark when going ina nd out of the fluid while hiding HUD.

Honey Fluid and Royal Jelly Fluid not will flow faster in warmer biomes! And slower in really cold biomes.

(Fabric/Quilt): Fixed Honey Fluid fog color being wrong in certain dimensions and applying thick fog too early before fully submerged.

##### Items:
Fixed it so that Honey Compasses locked to a structure will ignore y value difference when showing distance to target when advanced tooltips is on.

##### Entities:
Added quarterTurns option to Sentry Watcher nbt. Set this in the entity to true or 1b for the entity to do quarter turns instead of half turns.

Fixed Purple Spike inflicting poison effect that doesn't remove itself when its effect timer runs out.

##### Teleportation:
Fixed infinite loop if teleporting to and from Bumblezone is cancelled by another mod or script.

##### Lang:
Lowercased some stuff in ru_ru to match vanilla Russian casings (Thanks to SwayMinin for this PR)

##### Mod Compat:
(Fabric): Spectrum Jetpacks's jetpacks will now be disabled when in Heavy Air block

