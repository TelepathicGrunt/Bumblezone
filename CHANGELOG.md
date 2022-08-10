### **(V.6.1.8-beta Changes) (1.19.2 Minecraft)**

##### Misc:
Ported to 1.19.2. Music Disc constructors changed a bit in 1.19.1 which requires me to release this fix.

##### Lang:
zh_cn.json lang file updated by Litttlefish. Thank you!


### **(V.6.1.7 Changes) (1.19 Minecraft)**

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

zh_cn.json lang file updated by Litttlefish. Thank you!


### **(V.6.1.6 Changes) (1.19 Minecraft)**

##### Misc:
Added checks to shield against missing capabilities on serverPlayers that Forge was supposed to keep alive but for some
 unknown reason, just lets the caps get nuked which kills my mod. Now null cap crashes should be gone for good.
 I wrapped all my caps grabs with checks to make sure the cap exists as my nuclear option for Forge's instability with caps.
 Yes this is a ~~gamer~~ modder rant.


### **(V.6.1.5 Changes) (1.19 Minecraft)**

##### Items:
Redid the `bz_pollen_puff_entity_flowers` json folder so now the entries can take a weighted list of plants to pick from
 and can match the entity's nbt. This means now that Brown Mooshrooms will spawn Brown Mushrooms instead of Red Mushrooms!
 Hitting Earth Mobs Mod's muddy pigs with Pollen Puff will now spawn a random tulip nearby!

Pollen Puff's `bz_pollen_puff_entity_flowers` json entries can now take air block entries safely.
 Air entries can be used to make spawning plants more rare when the mob is hit by Pollen Puff.
 Many mossy mobs from other mods now have their rates of spawning Moss Carpet or Azalea now lowered as a result to balance.

##### Mod Compat:
Friends and Foe's Mauler will now sometimes spawn Moss Carpet when hit with Pollen Puff.


### **(V.6.1.4 Changes) (1.19 Minecraft)**

##### Dimension:
Fixed not storing the correct dimension the player came from when entering Bumblezone.
 Exiting Bumblezone should put you back to the original dimension properly now.

##### Mod Compat:
Fixed crash when Alex's Mob's bear tries to break and eat a beehive. 
 They passed null into beehivetileentity.emptyAllLivingFromHive which crashed my code.
 I added a null check to my code to fix this.


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

Made special bee spawning mechanics in Bumblezone ignore Fake players for performance.

##### Structures:
Optimized Bumblezone structure layout creation much faster than what vanilla allows.

##### Blocks:
Fixed Honey Cocoon loading loot tables by itself randomly.
 This should prevent some lag when they first load in from worldgen.

##### Items:
Honey Compass now locates structures in an async way so server should not lag when using a Honey Compass to find Cell Maze/Throne Pillar structures.
 Special thanks for Bright Spark's Async Locator code!


### **(V.6.1.1 Changes) (1.19 Minecraft)**

##### Entities:
Fixed Beehemoth not being saddleable and being controllable without being saddled.

##### Items:
Honey Compass now says the name of the block it is tracking when in block tracking mode.


### **(V.6.1.0 Changes) (1.19 Minecraft)**

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
