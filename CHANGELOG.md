### **(V.7.5.10 Changes) (1.20.1 Minecraft)**

#### Blocks:
Fixes Crystalline Flower consume button showing when clicking despite no consume item.

Fixed int overflow when trying to consume a stack of max-tier granting items in Crystalline Flower.


### **(V.7.5.9 Changes) (1.20.1 Minecraft)**

#### Features:
Fixed logspam due to not setting POI correctly on Honeycomb Brood Blocks in large Honeycomb Hole features.

Improved generation speed of the Pile of Pollen surface feature. Pollinated Fields and Pollinated Pillar biomes should spawn faster.

Pollinated Fields and Pollinated Pillar biome's caves are now slightly taller to allow easier exploring.

#### Structures:
Honey Cave Room structures can spawn exposed in walls now. Slightly reduced spawnrate to compensate.

Reduced number of Jigsaw Blocks and slightly reduce amount of pieces in Gazebuzz Cluster structure so its layout can generate twice as fast (about 3 seconds previously)

#### Mod Compat:
Changed a bit of code to work better with C2ME (Concurrent Chunk Management Engine).


### **(V.7.5.8 Changes) (1.20.1 Minecraft)**

#### Structures:
Fixed an issue where processors in Structures were running their logic too often in side chunks they didn't need to run in.

#### Features:
Improve generation speed of the large honeycomb holes in walls.

#### Processors:
Optimized some processors that run for structures/features. Makes worldgen a slight bit faster.

#### Blocks:
Improved speed for Sticky Honey Residue's check if it can survive at a spot.
 This will slightly improve worldgen speed for Crystal Canyon biome.

#### Mod Compat:
Allowed Enderman Overhaul's Bubble Pearl to teleport players to Bumblezone.


### **(V.7.5.7 Changes) (1.20.1 Minecraft)**

#### Dimension:
Replaced 2 Surface Rules with a feature and adjusted rest of Surface Rules to make worldgen faster.


### **(V.7.5.6 Changes) (1.20.1 Minecraft)**

#### Blocks:
Improved canSurvive check with StickyHoneyResidue. Makes worldgen placing of this block a bit faster.

#### Features:
Turned off neighbor shape update when placing giant honeycomb holes in the walls during worldgen as it is not needed to be ran.
 Improves worldgen speed a bit.

Improved worldgen time a bit for caves in Bumblezone

#### Structures:
Stopped checking "selection_priority" in Jigsaw Blocks for Bumblezone structures since Bumblezone doesn't use it. Speeds up structure layout generation a little bit.

#### Dimension:
Tried to cleanup and adjust Surface Rules to make worldgen a bit faster.

#### Mod Compat:
Added icons for Bumblezone biomes when viewed with Emi Ores.

Fixed code to properly swap bees in structures rarely with modded bees.


### **(V.7.5.5 Changes) (1.20.1 Minecraft)**

#### Blocks:
Fixed floating in Windy Air causing client disconnect from servers where server has flying disabled.

#### Items:
Fixed flying with Bumblebee Chestplate causing client disconnect from servers where server has flying disabled.


### **(V.7.5.4 Changes) (1.20.1 Minecraft)**

#### Blocks:
Pile of Pollen will now affect the fallDistance field on the entity falling into it.
 Meaning now it should properly reduce fall damage when falling into it where before, it did not change the fall damage based on your fall height.

#### Items:
Made Life Essence not turn Dead Bush into certain modded Saplings now by no longer using #saplings tag in 
 the `the_bumblezone:essence/life/dead_bush_revives_to` block tag.

#### Mod Compat:
EMI, REI, and JEI will have info pages for flowers that show up in Hanging Garden and for blocks that can support Crystalline Flower.


### **(V.7.5.3 Changes) (1.20.1 Minecraft)**

#### Blocks:
Made Crystalline Flower respect maximum level for enchantments when set by Apotheosis or Zenith.


### **(V.7.5.2 Changes) (1.20.1 Minecraft)**

#### Advancements:
Halved the requirements of many Queen Desire advancements to be less grindy.

#### Entities:
The Bee Queen will now give you Bottle O' Enchanting for Music Discs, Trim Templates, Banners, and Sherds!
 Good way to get rid of duplicates of these items.

When trading in Dead Bush, the Bee Queen will not give Spectrum's saplings, Productive Tree's saplings, Twilight Forest's magic saplings,
 Regions Unexplored's cactus and brimwood saplings, Bewitchment's Dragon Blood sapling, Dynamic Tree's seeds,
 Phantasm's pream sapling, Tinker's ender slime sapling, and several mod's dead saplings.
 Some smaller mod's saplings may not be available from Dead Bush trades not too since it is now explicitly listing out
 the possible sapling rewards in the `the_bumblezone:bee_queen/dedicated_trade_tags/saplings_from_dead_bush` item tag.
 Add more saplings to this tag if you wish for them to be a possible reward for trading in Dead Bushes to the Bee Queen.

#### Dimension:
Fixed bug that caused biome borders to be blocky/jarring rather than a more wavy smooth border.


### **(V.7.5.1 Changes) (1.20.1 Minecraft)**

#### Structures:
Fixed it so Gazebuzz Cluster structure pulls random music discs to spawn from `the_bumblezone:structures/gazebuzz_cluster_music_discs` item tag 
 instead of the `the_bumblezone:structures/dance_floor_music_discs` item tag

Removed the floating honey inside Goliath Honey Fountain. No idea how the heck that snuck in while saving pieces of the fountain.

#### Entities:
Rootmins can be "tamed" by using a Name Tag on them. 
 Once they are owned by a player, the Rootmin will not shoot that player but will shoot at other players without bee armor.
 Owned Rootmins will also now shoot at any monster not owned by the Rootmin's owner and the monster is not tagged `the_bumblezone:rootmin/forced_do_not_target`

#### Fluids:
Fixed a case where Honey Fluids didn't render a side while falling. Also fixed still textures being too large when viewed through glass.

Falling Honey Fluid does not get squished textures now.


### **(V.7.5.0 Changes) (1.20.1 Minecraft)**

#### Advancements:
Fixed issue where completing Honey Drunk advancement with 1 Honey Bottle in hand will replace the Royal Jelly Bottle reward with the Honey Bottle's Glass Bottle remainder.

Added a new advancement to show that you can sleep in beds within Bumblezone to set respawn point and beds won't explode!

Fixed Music Disc Collection advancement not requiring all Bumblezone Music Discs

#### Blocks:
Fixed Luminescence Wax Channel block's top and bottom texture not rotated when sword/shear right-clicked while horizontal.

Fixed Bee pattern Carvable Wax facing the wrong way when facing north, south, east, or west.

Fixed issue where untranslated enchantments are put at bottom of Crystalline Flower's list of enchantments.

Added a sorting button to Crystalline Flower's screen.

Fixed bug where tier 1 Crystalline Flower only showed enchantments whose level 2 min cost is within tier 1 range.
 Kept the bug for vanilla enchantments so some enchantments only begin to show up in tier 2 flower for a little bit of balance.

Fixed issue where Crystalline Flower would show treasure Enchantments that are not random loot table or enchanting table drops.

Heavy Air block now disables the ability to climb blocks.
 Mostly to stop Origins and other mods allowing people to climb walls easily to cheese Essence Events in Sempiternal Sanctums.

Fixed Honey Fluid rendering bottom of top Honey Fluid incorrectly when inside the bottom fluid.

#### Enchantments:
Allow Comb Cutter to now go up to Level 2 for faster mining. By default, Level 2 won't show in vanilla Enchanting Table.
 But you can get it from max tier Crystalline Flower or by combining two level 1 Comb Cutters together.

#### Effects:
Increased the default config value for the duration of Protection of the Hive. 
 Now protection lasts 5 minutes when obtained by feeding Bees or feeding Honeycomb Brood blocks.
 Anyone with pre-existing config files would need to edit `howLongProtectionOfTheHiveLasts` to 6000 to match the new default value.

#### Entities:
Made beehemothSpeed config be synced from server to client, so it takes full effect without needing client to change value to match server.
 (Still need a restart for config value to take effect)

Changed the vehicle move speed check on server to now allow even higher Beehemoth speeds without getting speed checked by the server.

Removed Leaves tag from tier 1 Bee Queen trades.

Made Cosmic Crystal Entity in White Sempiternal Sanctum now target tiny players better for horizontal laser attack.

Made Cosmic Crystal Entity in White Sempiternal Sanctum no longer reset their attack phase when a crystal is destroyed/enough damage is done.

If the White Sempiternal Sanctum event is being beaten far too quickly, the Cosmic Crystals will get a shield and be impervious to all damage for a limited time.
 Wait for the shield to disappear and resume your attacks!

Rootmins will target smaller players better now when shooting Dirt Pellets.

Green Sempiternal Sanctum's event Rootmin will now have a shield showing when no player is on the other platform.
 Once a player is on platform, then the shield disappears and Rootmin can be damaged by any Dirt Pellet.

Green Sempiternal Sanctum's event Rootmin that knocks off armor with its Dirt Pellet will now make the dropped item never despawn.

Green Sempiternal Sanctum's event Rootmin's second phase will stagger the Dirt Pellets to make hitting back a bit better.

Made Yellow Sempiternal Sanctum's Electric Ring have a slightly bigger hitbox and spawns at a more fair heights in last 50% of the event.

Fixed Rootmins not hiding when in Heavy Air.

Fixed wild/wandering Beehemoths not wandering in Heavy Air.

Variant Bees can breed with normal Bees now but 1/4th chance of having a Variant Bee baby.

Fixed Honey Slime spawning stuck in blocks and suffocating.

#### Items:
Made Buzzing Briefcase no longer burnable in Furnace so that Modern Industrial no longer shows an EU energy tooltip on the item. Reduces confusion.

All endgame essence items now won't decrement their ability use counter if user is in Creative or Spectator mode.

#### Structures:
Added Gazebuzz Cluster structure to replace half of Hanging Gardens structure! 
 This large structure made of many hanging gazebos will test your parkour skills and have a variety of stuff to grab! 
 Including Crystalline Flower and Bee Armor!

Added Goliath Honey Fountain that can spawn in any Bumblezone biome!
 This a massive fountain of honey with secret entrance to the insides! Explore!

Added Mite Fortress as a very difficult and deadly structure with good loot!
 It is the battleground where Bees are struggling to fend off the endless waves of Endermites invading the dimension!
 Fight your way through Silverfish and Endermites for resources!

Added Phantasm Aviary as a hanging structure full of Phantoms along with Crystalline Flower and some loot! Has some Rootmins as well usually.
 This structure was originally made by Biban_Auriu with vanilla blocks so special thanks to them for donating this structure to this mod and letting us re-theme it!

Red Sempiternal Sanctum's arena event now has it own music! Beenna Box by Punpudle! Also a Music Disc was made for it!

Removed Cell Maze as a possible structure to locate from Pirate Ship's Honey Compass.

Removed End Rods and replace Purpur Block with Purpur Slabs in Cell Maze's End Bleed Room.
 Better balancing in packs by removing these two blocks that are used in some mod's endgame recipes.

Replaced the End Rods with Magenta Candles in Subway's Endermite End Piece.

Made Pyro The Burning Bee now have infinite Fire Resistance.

Split the tiny structures out of the main structure set into their own set of small structures.
 Will make exploration structures much more common in Bumblezone, so it is less barren.

Made Bee House and Stinger Spear Shrine not spawn in Hive Wall, Hive Pillar, or Pollinated Pillar biomes.

Spawn Candle Parkour structure in Crystal Canyon biome now.

Split Stinger Spear Shrine, Bee House, Honey Fountain, and Candle Parkour into multiple pieces, so it terraform terrain around itself in a circular look instead of square look.
 Meshes better with terrain as a result.

Split Battle Cube and Sempiternal Sanctum structures into multiple pieces for much faster generation during worldgen.

Fixed Luminescence Wax facing wrong way in several rooms in Sempiternal Sanctums.

Fixed Carvable Wax Pillars not reaching low ceilings in Sempiternal Sanctums.

Fixed int overflow with my `min_distance_from_world_origin` option in structure sets.
 Was preventing structures from spawning at certain areas of the world beyond world center area.
 You should no longer see Sempiternal Sanctums like 9k away anymore. This means more Sanctums (and more Throne Pillars) are now spawning.

Spawn Honey Cave Room structure farther from sea level.

#### Mod Compat:
Add more mod compat Bee Queen Trades and tagging! Special thanks to Cicopath for the work here!