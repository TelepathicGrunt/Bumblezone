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


### **(V.7.6.21 Changes) (1.21 Minecraft)**

##### Entities:
Made sure Bees spawning in Bumblezone are spawned in generated/loaded chunks.

##### Advancements:
Fixed Crazy Trader advancement triggering at 126 Bee Queen trades instead of 128.

Fixed Crazy Trader advancement not triggering if you somehow skip over the target trade amount.

Fixed Throne Pillar advancement not triggering when holding Throne Honey Compass.


### **(V.7.6.20 Changes) (1.21 Minecraft)**

##### Mod Compat:
Improved general compat with More Babies mod

##### Configs:
(NeoForge): Marked restart-needed configs as needing restarts.

##### Misc:
(NeoForge): Now requires stable Neo v21.0.143 or newer.