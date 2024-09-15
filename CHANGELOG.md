### **(V.7.5.0 Changes) (1.20.1 Minecraft)**

#### Advancements:
Fixed issue where completing Honey Drunk advancement with 1 Honey Bottle in hand will replace the Royal Jelly Bottle reward with the Honey Bottle's Glass Bottle remainder.

Added a new advancement to show that you can sleep in beds within Bumblezone to set respawn point and beds won't explode!

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

#### Items:
Made Buzzing Briefcase no longer burnable in Furnace so that Modern Industrial no longer shows an EU energy tooltip on the item. Reduces confusion.

#### Structures:
Added Gazebuzz Cluster structure to replace half of Hanging Gardens structure! 
 This large structure made of many hanging gazebos will test your parkour skills and have a variety of stuff to grab! 
 Including Crystalline Flower and Bee Armor!

Added Goliath Honey Fountain that can spawn in any Bumblezone biome!
 This a massive fountain of honey with secret entrance to the insides! Explore!

Added Mite Fortress as a very difficult and deadly structure with good loot!
 It is the battleground where Bees are struggling to fend off the endless waves of Endermites invading the dimension!
 Fight your way through Silverfish and Endermites for resources!

Removed Cell Maze as a possible structure to locate from Pirate Ship's Honey Compass.

Removed End Rods and replace Purpur Block with Purpur Slabs in Cell Maze's End Bleed Room.
 Better balancing in packs by removing these two blocks that are used in some mod's endgame recipes.

Replaced the End Rods with Magenta Candles in Subway's Endermite End Piece.

Made Pyro The Burning Bee now have infinite Fire Resistance.

Split the tiny structures out of the main structure set into their own set of small structures.
 Will make exploration structures much more common in Bumblezone, so it is less barren.

Split Battle Cube structure into multiple pieces for faster generation during worldgen.

Made Bee House and Stinger Spear Shrine not spawn in Hive Wall, Hive Pillar, or Pollinated Pillar biomes.

Spawn Candle Parkour structure in Crystal Canyon biome now.

Split Stinger Spear Shrine, Bee House, Honey Fountain, and Candle Parkour into multiple pieces, so it terraform terrain around itself in a circular look instead of square look.
 Meshes better with terrain as a result.

Fixed Luminescence Wax facing wrong way in several rooms in Sempiternal Sanctums.

Fixed Carvable Wax Pillars not reaching low ceilings in Sempiternal Sanctums.

Fixed int overflow with my `min_distance_from_world_origin` option in structure sets.
 Was preventing structures from spawning at certain areas of the world beyond world center area.
 You should no longer see Sempiternal Sanctums like 9k away anymore. This means more Sanctums (and more Throne Pillars) are now spawning.

#### Mod Compat:
Add more mod compat Bee Queen Trades and tagging! Special thanks to Cicopath for the work here!