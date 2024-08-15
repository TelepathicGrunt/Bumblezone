### **(V.7.6.24 Changes) (1.21.1 Minecraft)**

##### Entities:
Fixed Cosmic Crystal Entity breaking non-arena blocks when arena is despawned.

##### Mod Compat:
Fixed Bumblezone breaking Productive Bees's Honey Treat behaviour by removing all Honey Treat compat from Bumblezone.
 You won't get Protection of the Bees now when feeding Honey Treat to bees. Better this way to prevent issues with Productive Bees.

Allow Mutant Monster's Endersoul Hands to allow teleporting to Bumblezone when crouch right clicking beehives!

Allow Bosses of Mass Destruction's Earthdiver Spear to allow teleporting to Bumblezone when right clicking beehives!

Allow Bosses of Mass Destruction's Charged Ender Pearl to allow teleporting to Bumblezone when thrown at beehives!


### **(V.7.6.23 Changes) (1.21.1 Minecraft)**

#### Misc:
Updated to 1.21.1 Minecraft.

#### Blocks:
Fixed Block Entity validation issues with Suspicious Pile of Pollen.


### **(V.7.6.22 Changes) (1.21 Minecraft)**

##### Dimensions:
Adjusted terrain noise to make chunk generation faster in Bumblezone!
 Also prevents Tectonic from making Bumblezone chunk generation slower/more memory intensive.

##### Blocks:
Fix case where carving Carvable Wax or Ancient Wax on servers could cause hand to swing twice.

Pile of Pollen and Suspicious Pile of Pollen now does not vertically slow entities if they have Slow Falling effect on.
 Makes traversing them less insufferable when having Slow Falling on.

##### Items:
Essence of Knowledge will now highlight Silverfish infested blocks, Vaults, Decorated Pots, Brewing Stands, Chiseled Bookshelves, and Trial Spawners.
 Added `the_bumblezone:essence/knowing/block_disable_highlighting` block tag to disallow highlighting non-Block Entity blocks!
 Added `the_bumblezone:essence/knowing/block_forced_highlighting` block tag to force allow highlighting non-Block Entity blocks!
 Added `the_bumblezone:essence/knowing/prevent_displaying_name` structure tag.

Fixed Stinger Spear unabled to be enchanted with Loyalty or Impaling.
 Added `the_bumblezone:enchantables/stinger_spear_forced_disallowed` enchantment tag to allow preventing unwanted enchantments on Stinger Spear.

Sugar Water Bottle, Royal Jelly Bottle, and Bee Soup now make used of `usingConvertsTo` so they should have better compat with other mods.

##### Mod Compat:
Essence of Knowledge will now highlight certain other mod's blocks now.

Pollen Puff thrown at Mobs of Mythology's Sporeling mob will spawn red or brown mushroom nearby if spot is valid.

Pollen Puff thrown at Arts and Craft's Lotus Flower may multiply the block.

Added Bee Queen color randomizing trades for a ton of mods now!

Improved JEED's descriptions of Bumblezone's effects.

Improved compat with Framed Blocks mod regarding Carvable Wax, Ancient Wax, and Luminescence Wax. Special Thanks to XFactHD for adding the compat work!

