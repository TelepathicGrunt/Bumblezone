### **(V.6.2.0 Changes) (1.19.2 Minecraft)**

##### Blocks:
Added Glistering Honey Crystal block!
 A glowing light source of crystallized honey. Found only in Crystal Canyon biome in Bumblezone.
 Gives a high amount of Honey Crystal Shards when broken or can be smelted into a ton of Sticky Honey Residue.
 Great for decor or lighting! Also transform neighboring water source blocks into sugar water.
 If combined with an Awkward Potion in a Brewing Stand, it will create a Potion of Luck!
 The most common benefit of Luck status is it may improve the loot drops of certain datapack/modded loot tables.

Added Carvable Wax block!
 Right click with Shears or Swords to change the texture for it for decoration purposes!
 Can be crafted from 9 Beeswax. Or created from Smelting Beehive Beeswax.

Super Candles added! A massive decorative bulky candle that gives off a ton of light! Can be dyed various colors through crafting.
 Can be lit and waterlogged the same way as regular Minecraft Candles. Also can be lit by flaming projectile hits as well.
 When lit, any mob in the flame will be set on fire while taking low damage.
 Super Candles can be stacked and if Soul Sand/Soul Soil is placed under, it gets a soul flame that scares away Piglins!
 If a Comparator is facing the Super Candle Base, it gets a power of 5 when lit.
 If Comparator is facing the Super Candle Wick, it will get a power of 3 when it is a soul flame and 5 when it is a normal flame.
 Dispensers can light the Candle too.

Incense Candles added! A powerful magical candle that radiates its effects to nearby entities!
 On the surface, this candle has all the same behaviors as the regular Super Candles. Except these powerful Incense Candles
 can be imbued with different status effects to grant nearby entities based on what potions are used to craft the Incense Candle.
 When the Incense Candle's power runs out, the candle will become unlit.
 Relighting the candle will restart its status effects it gives.
 See what kind of Incense Candles you can make based on different potion combinations!

##### Biomes:
Added Crystal Canyon biome full of Carvable Wax and Glistering Honey Crystals!

##### Items:
Buffed Royal Jelly Bottle so it gives move saturation, nutrition, and speed when consumed. It now also gives Jump Boost and Slow Falling as well.

##### Structures:
Throne Pillar structure uses Glistering Honey Crystal instead of Jack o Lanterns now.

Fixed Throne Pillar Honey Compass in Cell Maze structure not locating Throne Pillars.

##### Entities:
Bee Queen trade json files now will stack possible rewards properly if multiple files have the same want item.

##### Dimensions:
Fixed issue where ceiling of dimension could rarely generate to have holes to the outside of the dimension boundary.

##### Config:
Added a new config to disable the thick fog in Bumblezone. Set to enable fog by default.
 The config entry can be found with Mod Menu on and edited in-game through Mod Menu's mod button.

Added a new config to reduce the thickness of the fog in Bumblezone. Set to 3 by default to make fog 3 time less thick.
 The config entry can be found with Mod Menu on and edited in-game through Mod Menu's mod button.

##### Lang:
zh_cn.json lang file updated by Litttlefish. Thank you!


### **(V.6.1.7 Changes) (1.19.1 Minecraft)**

##### Entities:
Fixed Bee Queen healing superfast and thus, unkillable.

Added more alternative Bee skins added by Shy!

##### Teleportation:
Fixed issue where mobs that spawned in Bumblezone originally would teleport to incorrect coordinates when exiting Bumblezone.

Added a new config option called forceBumblezoneOriginMobToOverworldCenter which is set to true by default now.
 If set to true, mobs that spawned in Bumblezone originally will teleport to 0,0 world center of Overworld always at top of terrain.
 Hopefully this is more intuitive and prevent lost Bee Queens when pushing them out of Bumblezone.

##### Lang:
Added missing en_us.json lang translation for Bee Queen entity type name.

Added translations for in-game Bumblezone config menu provided by Midnightlib.


### **(V.6.1.6 Changes) (1.19.1 Minecraft)**

##### Items:
Pollen Puff's `bz_pollen_puff_entity_flowers` json entries can now take air block entries safely.
 Air entries can be used to make spawning plants more rare when the mob is hit by Pollen Puff.
 Many mossy mobs from other mods now have their rates of spawning Moss Carpet or Azalea now lowered as a result to balance.

##### Mod Compat:
Earth2Java's Muddy Pig will only spawn Poppy flowers when it is hit by Pollen Puff while in mud.
 Non-muddy form of this Muddy Pig mob will not spawn flowers anymore when hit by Pollen Puff.

Friends and Foe's Mauler will now sometimes spawn Moss Carpet when hit with Pollen Puff.

Earth2Java's Moolip now spawns Pink Daisy when hit with Pollen Puff instead of Pink Tulip.

##### Lang:
zh_cn.json lang file updated by Litttlefish. Thank you!


### **(V.6.1.5 Changes) (1.19.1 Minecraft)**

##### Items:
Redid the `bz_pollen_puff_entity_flowers` json folder so now the entries can take a weighted list of plants to pick from
 and can match the entity's nbt. This means now that Brown Mooshrooms will spawn Brown Mushrooms instead of Red Mushrooms!

 Fixed crash when Pollen Puff hits a mob. (Had outdated fake Player API)

##### Dimension:
Fixed not storing the correct dimension the player came from when entering Bumblezone.
 Exiting Bumblezone should put you back to the original dimension properly now.

##### Mod Compat:
Fixed crash if a mod passes null into beehivetileentity.emptyAllLivingFromHive.
 I added a null check to my code to prevent a crash.


### **(V.6.1.4 Changes) (1.19.1 Minecraft)**

##### Misc:
Ported to 1.19.1. Music Disc constructors changed a bit in 1.19.1 which requires me to release this fix.


### **(V.6.1.3 Changes) (1.19 Minecraft)**

##### Dimension:
Teleporting to and from Bumblezone is now mostly async to reduce server lag/deadlock.
This is new and a bit of an experiment so please report any issues that arises from teleporting to and from Bumblezone.

Reduced the jittering screen a bit when exiting Bumblezone by going below Y = 0.

Fixed teleporting in/out of Bumblezone message not showing up while teleporting instead of afterwards.

##### Structures:
Cell maze now has a slightly higher chance of having a Throne Pillar locating Honey Compass in the Honey Cocoon blocks.
 The special compass will also have a higher chance of showing up if you have luck status effect/attribute.

##### Lang:
Converted some warnings and teleportation text to be translatable now.


### **(V.6.1.2 Changes) (1.19 Minecraft)** 

##### Entities:
Fixed entities spawning below y = 0 in Bumblezone somehow which was causing the teleportation code to trigger which then
 would force the Overworld to generate its chunk which then lags the game. With this fix in place, Bumblezone worldgen is
 literally as fast as I can possibly make it. There's no further improvement I can do as it is blazingly fast now.

##### Structures:
Optimized Bumblezone structure layout creation much faster than what vanilla allows.

##### Blocks:
Fixed Honey Cocoon loading loot tables by itself randomly.
 This should prevent some lag when they first load in from worldgen.

##### Items:
Honey Compass now locates structures in an async way so server should not lag when using a Honey Compass to find Cell Maze/Throne Pillar structures.
 Special thanks for Bright Spark's Async Locator code!


### **(V.6.1.1 Changes) (1.19 Minecraft)**

##### Blocks:
Fixed Shulker Boxes able to get Honey Cocoon inside when Honey Cocoon is given to a Hopper attached to a Shulker Box.

##### Lang:
Fixed Creative Tab name not having translation.


### **(V.6.1.0 Changes) (1.19 Minecraft)**

##### Major:
Ported to Quilt!

##### Entities:
Bee Queen!!! A massive passive Bee that wants to trade! Right click while holding items or drop items in front of the
 Queen Bee to trade. Try trading various items to see what kind of items are possible to receive based what you give!
 Upon first successful trade you will be given The Queen's Desires advancement path which gives a reward for each
 advancement completed. After completing the entire advancement path, visit the Bee Queen and right click with
 empty hand to reset that advancement path for rewards!
 Can be found in Throne Pillar structure only.

Fixed Beehemoth controls and made it easier to move around as well.

Sparkles will spawn when the Beehemoth transforms into a Queen Beehemoth now.

##### Items:
Bee Stingers added which is a common gift from the Queen Bee! Can be used as low-damage ammo for Bows and Crossbows
 to have chances of inflicting poison, weakness, slowness, or paralysis effects on non-undead mobs.
 Can be right clicked on stinger-less bees to put a stinger back onto them.
 Can craft Stinger Spear when combined with two sticks in a line. Also can repair Stinger Spears.

Crystal Cannon! By combining a Bee Cannon with a Honey Crystal and 2 Honey Crystal Shards in the right layout,
 you can craft this lethal powerful cannon of questionable sturdiness that fires Honey Crystal Shards!
 Can store up to 3 shards. Hold down the use button and release to fire the shards that come with high damage and knockback!
 Can be repaired with Honey Crystal Shards, Sugar Infused Stone, or Sugar Infused Cobblestone.
 Takes Quick Charge, Punch, Power, and Piercing enchantments along with Unbreaking, Curse of the Vanishing, and Mending.

Bee Cannon now can get Quick Charge Enchantment along with their existing Unbreaking, Curse of the Vanishing, and Mending.

Royal Jelly Bottle added! A super amazing form of honey from the Bee Queen from a royalty recipe passed down for generations!
 Drinking this item will restore high amount of Hunger, Speed Boost, and Beenergized level 2!
 It will also remove Poison, Weakness, and Slowness status effects when drank as well.
 You can trade this item back to the Bee Queen for a chance at some valuable rewards!
 Can be fed to bees to give extremely high and long lived Beenergized effect (and also grants very high friendship points with Beehemoths).

Royal Jelly Bucket added! A bucket sloppily holding extremely rare and valuable form of honey created by the Bee Queen!
 Can be fed to bees to give extremely high and insanely long lived Beenergized effect (and also turns Beehemoths
 straight into Queen Behemoths). Trading this back to the Bee Queen can give you a chance at extremely valuable rewards!
 Can be obtained by using an empty Bucket on Royal Jelly Fluid or created from 4 Royal Jelly Bottles plus a Bucket.
 Can be used in Dispensers too!

Essence of the Bees item added! An end game item to get for Bumblezone! When consumed, you get the following bonus permanently:

> Can enter Cell Maze and not get Wrath of the Hive effect anymore.

> Can enter Throne Pillar and not get Mining Fatigue when near Bee Queen.

> Entering Honey Fluid will grant Regenerative effect.

> Entering Royal Jelly Fluid will grant a short lived Beenergized effect and stronger Regenerative effect.

> Beehive/Bee Nests can be mined, honey taken by bottle, or sheared for honeycombs without angering the bees. No campfire needed!

> Can take honey from Filled Porous Honeycomb Blocks safely without getting Wrath of the Hive Effect.

> Honey Slime will not get angry at you when you take its honey.

Pollen Puff can be thrown at Mooshrooms to spawn Red Mushrooms nearby!
 Other mobs can be made to spawn flowers as well by adding new files to bz_pollen_puff_entity_flowers by datapack.
 Friends and Foes's Mooblooms and Glare is supported out of the box. Let me know what other flower mobs should be added by default!

Honey Compass now says the name of the block it is tracking when in block tracking mode.

##### Blocks:
Royal Jelly Block added! A solidified storage form of Royal jelly!
 It behaves similar to vanilla's Honey Block except for the fact that it can only be "pulled" by pistons and not pushed!
 Give it some thought and see what kind of awesome moving redstone builds you can do with this!

Honey Web, Sticky Honey Residue, and their redstone counterpart will not give slowness to creative mod players now.

##### Fluids:
Royal Jelly Fluid added! A specially brewed honey of bee royalty! It flows very slowly and will slow mobs in it significantly.
 Non-player mobs and boats tends to sink into the Royal Jelly Fluid as if it is a sticky trap!
 Can be picked up with a Bucket or Glass Bottle, and it will heal any hurt bee that flies into the fluid and give them a Beenergized and Regenerative effect.
 When this fluid comes in contact with any other fluid that has a lava tag, this fluid will turn into either Sugar Infused Cobblestone or Sugar Infused Stone blocks.

##### Structures:
Throne Pillar structure added! This is where the Bee Queen spawns!
 If inside this structure and near Bee Queen, you get mining Fatigue.

Cell Maze structure is now more common and its armor room's Honey Cocoon blocks have a 1/20th chance of having a special
 Honey Compass that is permanently locked onto the closest Throne Pillar structure.

##### Advancements:
The Queen's Desire advancement line is added that acts like a quest-line! It is started by first trading with the Bee Queen.
 Every advancment completed gives a reward! Complete them all for an end-game item!
 You can then reset the advancement line by right clicking Bee Queen with empty hand.

##### Sounds:
Fixed drinking Sugar Water not having subtitles.
