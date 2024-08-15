### **(V.7.4.16 Changes) (1.20.1 Minecraft)**

##### Entities:
Fixed Cosmic Crystal Entity breaking non-arena blocks when arena is despawned.

##### Structures:
Fixed Giant Spikes of Glistering Honey Crystal from spawning in Sempiternal Sanctum's Heavy Air or other structure's Windy Air areas.

Fixed Howling Construct biome's giant spikes from placing blocks in Sempiternal Sanctum's Heavy Air or other structure's Windy Air areas.

##### Mod Compat:
Fixed Bumblezone breaking Productive Bees's Honey Treat behaviour by removing all Honey Treat compat from Bumblezone.
 You won't get Protection of the Bees now when feeding Honey Treat to bees. Better this way to prevent issues with Productive Bees.

Allow Mutant Monster's Endersoul Hands to allow teleporting to Bumblezone when crouch right clicking beehives!

Allow Bosses of Mass Destruction's Earthdiver Spear to allow teleporting to Bumblezone when right clicking beehives!

Allow Bosses of Mass Destruction's Charged Ender Pearl to allow teleporting to Bumblezone when thrown at beehives!


### **(V.7.4.15 Changes) (1.20.1 Minecraft)**

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
 Added `the_bumblezone:essence/knowing/prevent_displaying_name` structure tag to make Alex's Cave's terraforming structures not show in name section.

##### Mod Compat:
Essence of Knowledge will now highlight Rare Ice's rare ice blocks and certain other mod's blocks now.

Added Bee Queen color randomizing trades for a ton of mods now!

Improved JEED's descriptions of Bumblezone's effects.


### **(V.7.4.14 Changes) (1.20.1 Minecraft)**

##### Entities:
Made sure Bees spawning in Bumblezone are spawned in generated/loaded chunks.


### **(V.7.4.13 Changes) (1.20.1 Minecraft)**

##### Mod Compat:
Recovered more amount of memory for EMI, REI compat. Around 10MB to 20MB in a sizable pack with EMI for example.
