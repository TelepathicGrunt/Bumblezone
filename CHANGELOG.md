### **(V.3.1.1 Changes) (1.17.1 Minecraft)**

##### Fluids:
Restrict values on properties for Honey Fluid to try and prevent a crash if a mod or the game tries to get a level 9 fluid when Honey Fluid only goes up to 8.


### **(V.3.1.0 Changes) (1.17.1 Minecraft)**

##### Translations:
Special thanks to mcBegins2Snow for helping to clean up some zh_cn.json translations!

Special thanks to WeirdNerd for helping to complete the pt_br.json translations!

Note, I need people to help add translations for the new Blocks, Fluids, and Items added in this update for The Bumblezone. Contact me if you're interest!red

##### Blocks:
Added Pile of Pollen block!
Gives off pollen particles when any entity walks through it and slows down the entity based on how high the pile is.
Is a falling block that can be used to blind players or to hide (players cannot see through the block when inside).
Unpollinated bees will become pollinated when they touch the block (decreases the pile by 1 layer) (modded bees can be added to the the_bumblezone:pollen_puff_can_pollinate tag that controls what bee can be pollinated).
Pandas in the block will sneeze significantly more often.
Breaking the block gives a little bit of Pollen Puff item but Fortune increases the drops. Shovels is the best tool for this block.
Redstone Comparators can measure the amount of layers this block has for contraptions.

Honeycomb Brood blocks can be feed items from the the_bumblezone:bee_feeding_item tag.

Adjusted texture for Sticky Honey Residue and Sticky Honey Redstone.

Sticky Honey Redstone now gives off a light level of 1 when activated.

Fixed sideways Honey Crystal not rotating properly when spawned by rotated nbt builds.

Fixed Honey Crystals sometimes are placed floating during worldgen.

Dispensers now only allow Honey Bottle, Sugar Water Bottle, and Honey Bucket to feed Honeycomb Brood Blocks if those items are in the the_bumblezone:bee_feeding_item tag.

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
If used on a child bee, it fully heals the bee and has a chance of growing the child bee into an adult.
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
Right clicking a pollinated bee while holding a water bottle, wet sponge, or water bucket in your hand will unpollinate the bee and drop a Pollen Puff item!

Fixed feeding bees bucket items gives you a bowl back instead.

Bottles, buckets, or bowls added to the the_bumblezone:turn_slime_to_honey_slime tag will put their empty item into your inventory now.

the_bumblezone:turn_slime_to_honey_slime tag now has honey bucket.

Buzzier Bees Honey Wand now should be able to take honey from Honey Slimes.

##### Worldgen:
Converted the Honeycomb Hole feature in the walls of the dimension to be now an nbt feature.
You can change the shape and blocks of those holes with a datapack that replaces its nbt file or its processors!

Added Pollinated Fields and Pollinated Pillar biomes with lots of piles of pollen!

Added Pollinated Stream structure to Pollinated Fields and Pollinated Pillar biomes as a fun small tunnel to explore.

Added Honey Cave Room structure to Pollinated Pillar biomes as a big cool room to find naturally spawned Honey Fluid!

Upgraded the biome layout code so now any datapack biome that starts with "the_bumblezone" will automatically spawn in the Bumblezone dimension.