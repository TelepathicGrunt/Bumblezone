### **(V.7.7.4 Changes) (1.21.1 Minecraft)**

#### Entities:
Fixed Bee Queen trade sync packets having too low a limit compared to 1.20.1 Bumblezone.


### **(V.7.7.3 Changes) (1.21.1 Minecraft)**

#### Entities:
The Bee Queen will now give you Bottle O' Enchanting for Music Discs, Trim Templates, Banners, and Sherds!
Good way to get rid of duplicates of these items.

When trading in Dead Bush, the Bee Queen will not give Spectrum's saplings, Productive Tree's saplings, Twilight Forest's magic saplings,
 Regions Unexplored's cactus and brimwood saplings, Bewitchment's dragon blood sapling, Dynamic Tree's seeds,
 Phantasm's pream sapling, Tinker's ender slime sapling, and several mod's dead saplings.
 Some smaller mod's saplings may not be available from Dead Bush trades not too since it is now explicitly listing out 
 the possible sapling rewards in the `the_bumblezone:bee_queen/dedicated_trade_tags/saplings_from_dead_bush` item tag.
 Add more saplings to this tag if you wish for them to be a possible reward for trading in Dead Bushes to the Bee Queen.

#### Dimension:
Fixed bug that caused biome borders to be blocky/jarring rather than a more wavy smooth border.

Fixed a possible deadlock issue with structure checking.


### **(V.7.7.2 Changes) (1.21.1 Minecraft)**

#### Misc:
Hard require v3.0.11 Resourceful Lib now as that version fixes a crash between the custom fluids in Bumblezone and rendering of the fluid in other mods.


### **(V.7.7.1 Changes) (1.21.1 Minecraft)**

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


### **(V.7.7.0 Changes) (1.21.1 Minecraft)**

#### Advancements:
Fixed issue where completing Honey Drunk advancement with 1 Honey Bottle in hand will replace the Royal Jelly Bottle reward with the Honey Bottle's Glass Bottle remainder.

Added a new advancement to show that you can sleep in beds within Bumblezone to set respawn point and beds won't explode!

Fixed Music Disc Collection advancement not requiring all Bumblezone Music Discs

#### Blocks:
Fixed Luminescence Wax Channel block's top and bottom texture not rotated when sword/shear right-clicked while horizontal.

Fixed Bee pattern Carvable Wax facing the wrong way when facing north, south, east, or west.

Fixed issue where untranslated enchantments are put at bottom of Crystalline Flower's list of enchantments.

Exclude Wind Burst enchantment from Crystalline Flower. It is supposed to only be obtainable from Ominous Vaults.

Added a sorting button to Crystalline Flower's screen.

Fixed bug where tier 1 Crystalline Flower only showed enchantments whose level 2 min cost is within tier 1 range.
 Kept the bug for vanilla enchantments so some enchantments only begin to show up in tier 2 flower for a little bit of balance.

Fixed issue where Crystalline Flower would show treasure Enchantments that are not random loot table or enchanting table drops.

Fixed Suspicious Pile of Pollen not possibly working with modded brushes tagged under `c:tools/brush`

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
 (Fabric still need a restart for config value to take effect)

Changed the vehicle move speed check on server to now allow even higher Beehemoth speeds without getting speed checked by the server.

Removed Leaves tag from tier 1 Bee Queen trades.

Made Cosmic Crystal Entity in White Sempiternal Sanctum now target tiny players better for horizontal laser attack.

Made Cosmic Crystal Entity in White Sempiternal Sanctum no longer reset their attack phase when a crystal is destroyed/enough damage is done.

If the White Sempiternal Sanctum event is being beaten far too quickly, the Cosmic Crystals will get a shield and be impervious to all damage for a limited time.
 Wait for the shield to disappear and resume your attacks!

Fixed Rootmin eye level being too high. Was causing Green Sempiternal Sanctum's Rootmin to shoot too low in third phase.

Rootmins will target smaller players better now when shooting Dirt Pellets.

Green Sempiternal Sanctum's event Rootmin will now have a shield showing when no player is on the other platform.
 Once a player is on platform, then the shield disappears and Rootmin can be damaged by any Dirt Pellet.

Green Sempiternal Sanctum's event Rootmin that knocks off armor with its Dirt Pellet will now make the dropped item never despawn.

Green Sempiternal Sanctum's event Rootmin's second phase will stagger the Dirt Pellets to make hitting back a bit better.

Made Yellow Sempiternal Sanctum's Electric Ring have a slightly bigger hitbox and spawns at a more fair heights in last 50% of the event.

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

Split Battle Cube structure into multiple pieces for faster generation during worldgen.

Fixed Luminescence Wax facing wrong way in several rooms in Sempiternal Sanctums.

Fixed Carvable Wax Pillars not reaching low ceilings in Sempiternal Sanctums.

Fixed int overflow with my `min_distance_from_world_origin` option in structure sets. 
 Was preventing structures from spawning at certain areas of the world beyond world center area.
 You should no longer see Sempiternal Sanctums like 9k away anymore. This means more Sanctums (and more Throne Pillars) are now spawning.

Spawn Honey Cave Room structure farther from sea level.

Fixed Giant Honey Crystal, Bee Dungeons, and a few other features to stop spawning inside certain structures.

#### Mod Compat:
Add more mod compat Bee Queen Trades and tagging! Special thanks to Cicopath for the work here!