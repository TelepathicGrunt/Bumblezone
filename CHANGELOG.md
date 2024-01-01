### **(V.7.1.15 Changes) (1.20.4 Minecraft)**

##### Entities:
Fixed Purple Spike inflicting poison effect that doesn't remove itself when its effect timer runs out.

##### Teleportation:
Fixed infinite loop if teleporting to and from Bumblezone is cancelled by another mod or script.

##### Mod Compat:
(Fabric): Spectrum Jetpacks's jetpacks will now be disabled when in Heavy Air block


### **(V.7.1.14 Changes) (1.20.4 Minecraft)**

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