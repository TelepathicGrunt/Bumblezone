### **(V.3.1.0 Changes) (1.17.1 Minecraft)**

##### Translations:
Special thanks to mcBegins2Snow for helping to clean up some zh_cn.json translations!

Note, I need people to help add translations for the new Blocks, Fluids, and Items added in this update for The Bumblezone. Contact me if you're interest!red

##### Blocks:
Added Pile of Pollen block!
 Gives off pollen particles when any entity walks through it and slows down the entity based on how high the pile is.
 Is a falling block that can be used to blind players or to hide (players cannot see through the block when inside).
 Unpollinated bees will become polinated when they touch the block (decreases the pile by 1 layer) (modded bees can be added to the the_bumblezone:pollen_puff_can_pollinate tag that controls what bee can be pollinated).
 Pandas in the block will sneeze significantly more often.
 Breaking the block gives a little bit of Pollen Puff item but Fortune increases the drops. Shovels is the best tool for this block.
 Redstone Comparators can measure the amount of layers this block has for contraptions.

Honeycomb Brood blocks can be feed items from the the_bumblezone:bee_feeding_item tag.

Adjusted texture for Sticky Honey Residue and Sticky Honey Redstone.

Sticky Honey Redstone now gives off a light level of 1 when activated.

##### Items:
Added Pollen Puff item!
 Can be thrown like a snowball but will not deal any damage or pushback.
 When it hits a block, it will try to spawn a Pile of Pollen block (if in midair, it'll turn into a falling block)
 Will make pandas sneeze if thrown at them.
 If it hits an unpollinated bee, it will pollinate them (modded bees can be added to the the_bumblezone:pollen_puff_can_pollinate tag that controls what bee can be pollinated)
 If it hits a flower, the flower may reproduce! (see the block tags for thw two tags that control what flowers can be reproduced by Pollen Puff)

Added Honey Fluid Bucket!
 Can be crafted from 1 Bucket + 3 Honey Bottles and when doing 1 Honey Bucket + 3 Glass Bottles, it can be crafted back into 3 Honey Bottles.
 Can place Honey Fluid anywhere and can be obtained by using a Bucket on a Honey Fluid source block.
 If used on an adult bee, it will fully heal the bee and set it and all bees within 10 blocks into love mode.
 If used on a child beed, it fully heals the bee and has a chance of growing the child bee into an adult.
 Using Honey Bucket on Porous Honeycomb block will turn it and 2 neighboring Porous Honeycomb blocks into Filled Porous Honeycomb blocks.
 Using Honey Bucket on Honeycomb Brood block will fully grow the larva straight to its final stage.
 Works with Dispensers too!

##### Fluids:
Fixed Sugar Water Fluid overlay being applied based on player's feet instead of if their eyes are actually in the fluid.

Added Honey Fluid!
 Flows slowly in a unique way downward but does not renew itself like Water does.
 Heals bees that touch the fluid while slowing all mobs in it.
 Non-player mobs, fishing bobbers, and boats sink in the honey fluid making it a nasty tasty trap!
 Reacts with lava to create Sugar Infused Stone/Sugar Infused Cobblestone.
 Source blocks can be picked up by bucket or turned into a non-source block by using a glass bottle on it.

##### Entities:
Fixed feeding bees bucket items gives you a bowl back instead.

Fixed using Glass Bottles on Honey Slime consumes Glass Bottle when in creative mode.

##### Worldgen:
Converted the Honeycomb Hole feature in the walls of the dimension to be now an nbt feature.
 You can change the shape and blocks of those holes with a datapack that replaces its nbt file or its processors!

Added Pollinated Fields and Pollinated Pillar biomes with lots of piles of pollen!

Added Pollinated Stream structure to Pollinated Fields and Pollinated Pillar biomes as a fun small tunnel to explore.

Added Honey Cave Room structure to Pollinated Pillar biomes as a big cool room to find naturally spawned Honey Fluid!


### **(V.3.0.9 Changes) (1.17.1 Minecraft)**

##### Sounds:
flight_of_the_bumblebee_rimsky_korsakov and honey_bee_rat_faced_boy music is now mono so that the Music Discs work properly in Jukeboxes.
  Special thanks to a friend who pointed out this issue and helped convert and compress the sound files!


### **(V.3.0.8 Changes) (1.17.1 Minecraft)**

##### Bee Interactivity:
Bees now can be fed what is in BEE_FEEDING_ITEMS tag and not be fed everything that isn't in that tag.


### **(V.3.0.7 Changes) (1.17.1 Minecraft)**

##### Lang:
Fixed up some lang files and config entries not being translated properly.


### **(V.3.0.6 Changes) (1.17.1 Minecraft)**

##### Mod Compat:
Made teleportOutOfBz method public in an API class so Requiem mod can access it without reflection.
  runEnderpearlImpact and runPistonPushed are also public in BumblezoneAPI class as well if any mod needs them.


### **(V.3.0.5 Changes) (1.17.1 Minecraft)**

##### Enchantments:
Fixed Comb Cutter enchantment so it appears in enchantment table only for Swords and Books and only if the cost of the enchantment is 13 or less.
  This help solve the problem of some mods making curses able to show up in enchanting table which caused Comb Cutter to be applied to any tool.


### **(V.3.0.4 Changes) (1.17.0 Minecraft)**

##### Teleportation:
Exiting and entering Bumblezone will show a message to just the teleporting player that they are being teleported

Any living entity including players and mobs can enter the Bumblezone by being pushed into a Bee Hive or Bee Nest block by an activated Piston

Any living entity can now exit the Bumblezone

Fixed player not teleporting to closest bee hive block if the block is at sea level

##### Lang:
Added Spanish translation donated by another person

##### Misc:
Compressed one of the sound files by a large amount


### **(V.3.0.3 Changes) (1.17.0 Minecraft)**

##### Mod Compat:
Added config option turn off compat with Charm's villager trades.
  
Fixed crash with Charm villager trade compat if charm's villagers is turned off.


### **(V.3.0.2 Changes) (1.17.0 Minecraft)**

##### Blocks:
Fixed and added back screen overlay for Sugar Water Block


### **(V.3.0.1 Changes) (1.17.0 Minecraft)**

##### Blocks:
Removed broken screen overlay for Sugar Water Block

##### Mod Compat:
Added back mod compat with Charm


### **(V.3.0.0 Changes) (1.17.0 Minecraft)**

##### Major:
Updated to 1.17.0 MC

##### Features:
Bee Dungeon and Spider Infested Bee Dungeons now spawns vanilla's candle blocks!