### **(V.6.4.7 Changes) (1.19.2 Minecraft)**

##### Blocks:
Pile of Pollen no longer slows Beehemoth or Bees down when inside it.

Pile of Pollen now more optimized with its Hidden effect. Less checking occurs while in block.
 Hidden effect is granted when Pile of Pollen covers up to just below the eye's height (Hidden 1)
 and when eyes are fully covered by Pile of Pollen, bees will give up chasing you unless you exit the Pile of Pollen (Hidden 2)
 Hidden effect of any level will act like 100% Invisibility effect where mobs won't see you until you touch/hit them. (Or they previously seen you)

Sticky Honey Residue/Sticky Honey Redstone is a bit more optimized now too.

##### Items:
Carpenter Bee Boots now only mine the below wood, leaves, honeycomb, or wax block when looking downward.
 More inuitive and prevents accidental block mining.

##### Entities:
Fixed Bee Queen reward item being spawned too far off to the side.

Honey Slime is now immune to the slowness/stickiness applied by Sticky Honey Residue, Sticky Honey Redstone, Honey Web and Redstone Honey Web.

##### Enchantments:
Condensed Bumblezone's enchantment descriptions that gets shown by Enchantment Description mod.

##### Mod Compat:
Added Bee Queen randomization trades for dyed items from Applied Energistics 2, Supplementaries, Quark, Farmer's Delight,
 Snowy Spirit, Chalk, Bontany Pots, Silent Gear, Comforts, Elevatorid

Pollen Puff can spawn nearby Red/Brown Mushrooms when it hits Alchemist's Garden's Alpha Shroom mob.

Make Pollen Puff cannot multiply Undergarden's Shimmerweed anymore.

Pollen Puff will multiply flowers and flower-like saplings from Biomes o Plenty, Fruit Trees, Cherry Blossom Grotto, Broglis Plants, Oaks Nature, Dungeons Content.

Pollen Puff hitting many of Dungeons Content's vine/flower based mobs will spawn certain flowers or vines nearby.

Some of Dungeons Content's flowers will now spawn in Hanging Gardens structure.

Enderpearls hitting Dungeons Content's Beenest mob will teleport player to Bumblezone dimension.

Carpenter Bee Boots can mine Dungeons Content's Wax Block.

Added compatibility with Resourceful Bees's alpha jar. Datapackable files for configuring are:
 `the_bumblezone:resourcefulbees/spawns_in_bee_dungeons` block tag
 `the_bumblezone:resourcefulbees/spawns_in_spider_infested_bee_dungeons` block tag
 `the_bumblezone:resourcefulbees/spawnable_from_brood_block` entity tag
 `the_bumblezone:resourcefulbees/spawnable_from_chunk_creation` entity tag
 `the_bumblezone:resourceful_bees_combs` worldgen placedfeature tag
There are some additional config options in Bumblezone's mod compatibility config file too.


### **(V.6.4.6 Changes) (1.19.2 Minecraft)**

##### Blocks:
Set the base of lit Super Candles/Incense Candles to return fire damage type for pathfinding.
 Should help stop other mod's mobs from pathfinding through the lit candles and burning.

Fixed weird lighting on Porous Honeycomb Block when it is in item form.

##### Items:
Increased the durability for all Bumblezone bee armor by 2.4x.

##### Features:
Fixed checks for valid spot for Bee Dungeons/Spider Infested Bee Dungeons.

##### Entities:
Added three tags to make it easier to define what items will lure Beehemoth and what item can lure/breed Honey Slime.
 Honey Slime will now follow players holding VanillaTweaks or Supplementaries's Sugar Block and can be bred with that along with vanilla's Sugar item.
 Candylands's Sugar brick, Crystalized Sugar, and Glazed Sugar can be used with Honey Slime too now.
 `the_bumblezone:mob_luring/beehemoth`
 `the_bumblezone:mob_luring/beehemoth_fast_luring`
 `the_bumblezone:mob_luring/honey_slime`

##### Advancements:
Mention that Honey Slime requires Sugar to breed in the Queen's Desire advancement for it.

##### Mod Compat:
Cyclic Honey Apple can be fed to bees and traded with Bee Queen now.

Cyclic's Lime Carnation Flower and Undergarden's Shimmerweeds cannot spawn in Hanging Gardens anymore.

Alchemist's garden's Cloudelion flower cannot be found in Hanging Gardens anymore.

Creatures and Beasts's Minipad and Lizard can spawn in Hanging Gardens along with Infernal Expansion's Glowsilk Moth


### **(V.6.4.5 Changes) (1.19.2 Minecraft)**

##### Structures:
Hanging Garden structure will now be able to randomly spawn flowering leaves or logs from other mods!
 This includes some leaves/logs from Oh The Biomes You'll Go, Biomes O Plenty, Quark, Ecologics, Terrestria, 
 Fruit Trees, Cherry Blossom Grotto, Colorful Azaleas, Blossom, and Aurora's Decorations!
 The new block tags that control this are:
 `the_bumblezone:allowed_hanging_garden_leaves`
 `the_bumblezone:allowed_hanging_garden_logs`
 `the_bumblezone:blacklisted_hanging_garden_leaves`
 `the_bumblezone:blacklisted_hanging_garden_logs`

##### Items:
Pollen Puff pollination json files will now safely skip invalid entries and parse the rest of the json file if a typo is done on a mob name.

##### Mod Compat:
Pollen Puff hitting Ars Elemental's Flashing Weald Walker will spawn Flashing Archwood Sapling nearby

Fixed broken Pollen Puff entry for Ars Nouveau's Flourishing Weald Walker causing all walkers to not spawn their saplings when hit with the puff.

Natural Decoration's snail and butterflies will spawn in Hanging Gardens now.

Changed my mind and now Biome Makeover's Black Thistle and Blue Skies's Lucentroot flowers no longer spawn in Hanging Garden structure now.
 Also blacklisted several flowers from Natural Decor Mod and Natural Expansion mod due to not looking good in structure. 

Broglis Plants's Nightshade, Oaks Nature's flowers, Biomes O Plenty's cherry/flowering oak sapling,
 Cherry Blossom Grotto's cherry sapling, Fruit Trees's cherry and redlove sapling will spawn in Hanging Garden.

Pokecube's Purple Wisteria Vines or Polluting Blossom and Extended Mushroom's Infected Flower will no longer spawn in Hanging Gardens 
 (Infected Flower will not be able to be reproduced with Pollen Puff either anymore)

Pokecube's bear themed Pokemon will now get Wrath of the Hive when in Bumblezone and attacked by bees!
 Careful on what Pokemon you choose in that dimension!

Alchemist's Garden's Shroom mobs will spawn their respective mushrooms when hit with Pollen Puff.

Fixed incompatibility with Forgery mod's Weaponized Enderpearl settings. You can now still teleport into Bumblezone with Enderpearl.
 Had to switch from an Enderpearl specific event to a general projectile impact event as Forgery will skip firing the Forge Enderpearl teleport event entirely.


### **(V.6.4.4 Changes) (1.19.2 Minecraft)**

##### Fluids:
Significantly fixed rendering issues with Honey Fluid and Royal Jelly Fluid.
 Also fixed particles from fluid spawning way out of the fluid at times.

Fixed issue where Sugar Water fluid cannot damage blazes nor allow fish to swim and other behaviors that Forge's Fluid API is currently having issues with.

##### Misc:
Fixed bug with vanilla Bees that make them not threadsafe when created as part of worldgen threaded chunk creation.
 Very niche rare crash to even get to happen but good for me to still patch just in case it does cause issues for people rarely.

Cleaned up some more mixins to make them more stackable with other people's mixins

##### Items:
Fixed Pollen Puff sometimes placing blocks at invalid locations like Biome Makeover's Moth Blossom flower in midair.

##### Mod Compat:
Bosses of Mass Destruction's Void Lily, Twilight Forests's Thorn Rose, 
 and Biome Makeover's Black Thistle and Foxglove can spawn in Hanging Garden structure now.

Farmer's Delight's Wild Tomatoes plant cannot spawn in Hanging Gardens structure now.

Biome Makeover's Black Thistle and Foxglove can be multiplied by Pollen Puff now.

Create Addition's Honey Cake can be fed to bees now and traded with Bee Queen


### **(V.6.4.3 Changes) (1.19.2 Minecraft)**

##### Structures:
Reduce chance of maps in Hive Temple loot a little bit.
 Increased number of threads spawned by Bumblezone so multiple Hive Temple maps and Honey Compasses can search at once from 1 to 3.

##### Fluids:
Fixed being able to "jump" while on ground inside Royal Jelly Fluid.

##### Misc:
Fixed LGBT+ and Ukraine Bee Skin configs fighting each other on applying. And changed the default config values for them.

Went through and cleaned up and modified several mixins to make some of them less hacky. Converted some to Forge events.


### **(V.6.4.2 Changes) (1.19.2 Minecraft)**

##### Teleportation:
Finally found a solution to make sure player teleports straight to destination spot in Bumblezone instead of briefly in 
 the ground while the game catches up to the teleport code. Needed to call serverPlayer.moveTo before serverPlayer.teleportTo
 since serverPlayer.teleportTo changes dimension and then has a lag for updating the position. Odd vanilla bug... 

##### Bee Aggression:
Added two new tags to control whether the specifically tagged entity will get Wrath of the Hive automatically or not in 
 Bumblezone dimension. The calming tag will take presence over the angry tag. But both tags will override the default
 built-in behavior for that mob on if it automatically gets wrath or not in the dimension. Tags are:
 `the_bumblezone:bee_aggression_in_dimension/always_angry_at`
 `the_bumblezone:bee_aggression_in_dimension/forced_calm_at`

##### Structures:
Hanging Garden will now spawn some extra bug mobs from other mods! Such as butterflies, snails, fireflies, or other small cute bug.
 This controlled by a new entity tag called: `the_bumblezone:hanging_gardens_initial_spawn_entities`

##### Block:
Fixed mobs pathfinding into lit Super Candles/Incense Candles and getting burned up.
 Now the wick cannot be pathfind through when lit.

Fixed mobs unable to jump over Honey Crystal Block. Now they should pathfind around the block properly.

##### Mod Compat:
Cleaned up how Enderpearl teleporting works when hitting Dreamland Biome's Bumble Beast or hits Productive Bees's Diamond Bee Nest Helmet.
 The following tags were added to handle the new way of doing compat and lets players add addition entities or items to hit to enter Bumblezone!

If entity hit is tagged `the_bumblezone:enderpearl_teleporting/target_entity_hit_anywhere`,
 will teleport thrower into Bumblezone.

If entity hit is tagged `the_bumblezone:enderpearl_teleporting/target_entity_hit_high`,
 top half of hitbox must be hit to teleport thrower into Bumblezone.

If entity hit is tagged `the_bumblezone:enderpearl_teleporting/target_entity_hit_low`,
 bottom half of hitbox must be hit to teleport thrower into Bumblezone.

If entity hit is wearing armor tagged `the_bumblezone:enderpearl_teleporting/target_armor`, will teleport thrower into Bumblezone.
 (Helmet requires top 60% of hitbox. Chestplate require top 40%. Leggings require below 60%. Boots require below 40%)
 
If entity hit is holding item tagged `the_bumblezone:enderpearl_teleporting/target_held_item`, will teleport thrower into Bumblezone.

Fixed overlay issues with REI's tooltip while on Crystalline Flower's screen

Ars Nouveau's Weald Walkers now spawn their respective sapling nearby when hit with Pollen Puff!


### **(V.6.4.2 Changes) (1.19.2 Minecraft)**

##### Mod Compat:
Cleaned up how Enderpearl teleporting works when hitting Productive Bees's Bee Nest Diamond Helmet or hits Dreamland Biome's Bumble Beast.
 The following tags were added to handle the new way of doing compat and lets players add addition entities or items to hit to enter Bumblezone!

If entity hit is tagged `the_bumblezone:enderpearl_teleporting/target_entity`, will teleport thrower into Bumblezone.
 If the entity is 1.8 blocks or bigger, pearl must hit top half of hitbox.

If entity hit is wearing armor tagged `the_bumblezone:enderpearl_teleporting/target_armor`, will teleport thrower into Bumblezone.
 (Helmet requires top 33% of hitbox. Chestplate require top 40%. Leggings require below 60%. Boots require below 33%)

If entity hit is holding item tagged `the_bumblezone:enderpearl_teleporting/target_held_item`, will teleport thrower into Bumblezone.


### **(V.6.4.1 Changes) (1.19.2 Minecraft)**

##### Blocks:
Nerfed Crystalline Flower so that it requires 1453xp to reach max tier (About the same as player going from xp level 0 to 30)
 Previously, the flower required xp roughly about the same as a player going from 0 to 23 xp levels.
 Was complained about being too low for the ability to pick enchantments. Now it requires about double more xp.
 You can still increase or reduce this xp cost in the config file for Bumblezone.

##### Items:
Fixed Honey Compass tooltip not wrapping properly.

##### Effects:
Wrath of the Hive and Protection of the Hive will no longer affect nearby Bees that have their AI turned off.

##### Mod Compat:
Added configs to make it easier to turn off spawning Buzzier Bees's blocks in Bumblezone.


### **(V.6.4.0 Changes) (1.19.2 Minecraft)**

##### Music:
Added two new songs by LudoCrypt!
 They will play randomly in Bumblezone but they also have Music Disc forms obtainable from Wandering traders!
 The songs can be obtained from Bandcamp here from LudoCrypt:
 https://ludocrypt.bandcamp.com/track/bee-laxing-with-the-hom-bees
 https://ludocrypt.bandcamp.com/track/la-bee-da-loca

##### Blocks:
Connected Textures added for Porous Honeycomb Block and Empty Honeycomb Brood Block! Special thanks to EERussianguy!

Added missing en_us translation for Crystalline Flower UI opening stat.

Adjusted textures for Sugar Infused Stone and Sugar Infused Cobblestone a bit.

Fixed Honeycomb Brood Block and Empty Honeycomb Brood Block facing the wrong way when in item form in inventory/creative menu.

Honeycomb Brood Block now has many more animation variations so the block is not always in sync with other blocks of itself in the world.

Fixed Honeycomb Brood Block spawning an adult bee instead of a baby one when triggered to spawn a bee by Dispenser using 
 Glass Bottle, Honey Bottle, Sugar Water Bottle, or Honey Bucket on the stage 4 Honeycomb Brood Block.

Fixed Honeycomb Brood Block not spawning bees properly outside Bumblezone and should more consistently spawn bees in Bumblezone when there's fewer mobs around.

Carvable Wax block now has a facing property. Some patterns will face different directions based on how you place it!

Glistering Honey Crystal now uses a facing property instead of axis property. Allows for more directions when placing for easier builds!

Added sound for lighting Super/Incense Candles.

Reduced duplicate entries in Bumblezone's Super Candle block and item tags.

Fixed Super Candles/Incense Candles not being waterlogged when placed in non-vanilla water tagged fluids. Fixed other watterlogging bug with them.
 Example, source Sugar Water was not waterlogging the Super Candles/Incense Candles when placed in it (Will be waterlogged with vanilla water when done so)

Creative pick block (middle click) on Honey Crystal block now gives the proper Honey Crystal item instead of Honey Crystal Shards.

##### Items:
Vanilla's Filled Maps now works in Bumblezone dimension properly!
 Many Bumblezone block's map colors were changed to look correct on maps.

Made an item form for all Carvable Wax blockstates to make building with it easier.
 Mining a Carvable Wax will drop the item form of its blockstate.
 
Adjusted the stone textures of Bee Cannon and Crystal Cannon to match the texture change for Sugar Infused Stone/Sugar Infused Cobblestone.

Adjusted the texture slightly for one variant of Stingless Bee Helmet and Bumble Bee Chestplate.

Carpenter Bees boots now can mine through Carvable Wax block when crouched on top in center of block.

Added tooltip descriptions to Honey Compass to make it more clear on how to use it.

Honey Compass now has a CustomName and CustomDescription nbt that takes a translation key to give it unique names or descriptions.
 Also now has a Locked nbt if you wish to spawn compasses that cannot be change to locate something else.
 These tags are used for the Honey Compass that locates a "mystery" structure.

Honey Compasses now only have the enchanted glint look if it is locating a Throne Pillar structure or is "locked" and cannot be made to locate anything else.
 Basically enchanted looking Honey Compass will not have the ability to right-click for saving Beehives/Bee Nests positions or locating Cell Maze.

Bee Essence effect will now show message to player if lost on respawning if keepEssenceOfTheBeesOnRespawning config is set to false.

Added command `\bumblezone is_bee_essenced` command to let any player check if they have Bee Essence still active. Does not need op status to use.

Pollen Puff thrown at What Did You Vote For's Sniffer will spawn Moss Carpet nearby.

##### Advancements:
Added `\bumblezone queen_desire_xxxxxxxx` commands. These commands let you see your progress towards many Queen Desire Advancements! 
 See the suggestion autocomplete for all options. Killed counter data requires an entity argument afterwards.

Fixed Bee Bread and Royal Jelly Bottle being fed to bees not counting towards the Queen's Desire advancement about feeding bees.

Moved Essence of the Bee consuming advancement to be at the end of the Queen's Desire advancement tree.
 Resetting the Queen's Desire advancement line by Bee Queen when fully completed will not reset the
 Essence of the Bee consuming advancement to show you had previously beaten this advancement line before.

##### Entities:
Fixed right clicking Bee Queen with empty hand sometimes not resetting the Queen's Desire advancement line when all of those advancements are done.

##### Enchantments:
Nerfed Neurotoxins enchantment so its missed counter does not increment if player hits entity while entity is already paralyzed.
 Helps prevent paralysis locking bosses.

Made Neurotoxin require more bookshelves before showing up in Enchanting Table

Made Potent Poison enchantment require 1 more bookshelf before showing up in Enchanting Table

##### Teleportation:
Fixed display message for when teleporting to Bumblezone fails due to not having a certain required block below the hive.
 This is controlled by the_bumblezone:required_blocks_under_hive_to_teleport block tag which is empty by default to not need a required block for teleporting.

##### Structures:
Added Hive Temple structure! A small Beeswax structure that has some persistent Honey Slimes and a small bit of loot.
 If you're lucky, you may find a Honey Compass in it that points to a Hanging Garden structure!

Made Pollinated Streams structure now spawn above ground with pillar support! Looks cooler and much, much easier to find!

Honey Cave Room structure is a bit more common now.

##### Effects:
Entities with Wrath of the Hive dying will remove the Wrath boosts from nearby bees that were targeting the Wrath afflicted mob.

##### Mod Compat:
Added config to allow turning off teleportation to Bumblezone if thrown Enderpearl hits top of the hitbox of a mob wearing Productive Bees's Bee Nest helmet.

Quark's enchantment tooltip on enchanted books can now show Bumblezone's armor. (Especially helpful as boots can get mining enchantments)

Enderpearls hitting top hitbox portion of Dreamland Biome's Bumble Beast will teleport players to Bumblezone dimension.

Buzzer Bees's Bee Bottle now when right-clicked on Empty Honeycomb Brood Block will turn it into a regular Honeycomb Brood Block with a larva.

Buzzier Bees's Crystallized Honey and Honeycomb Tiles blocks will spawn throughout Bumblezone.

Bee Queen will trade now for the following modded items now. `the_bumblezone:bz_bee_queen_trades/` tag folder:
 Dreamland Biome's Bumble Block
 Lots of Buzzier Bees items
 Delightful's Honey Glazed Walnut and Ender Nectar
 Farmersdelight's Honey Cookie and Honey Glazed Ham
 Adorn's Honeycomb Crate and beverages
 Skinned Lanterns's bee and honey lanterns (regular and soul)
 Create's Honeyed Apple
 Apotheosis's Beeshelf

Bees can be right-clicked fed with these following modded items now. `the_bumblezone:bee_feeding_items` item tag:
 Delightful's Honey Glazed Walnut
 Farmersdelight's Honey Cookie and Honey Glazed Ham
 Buzzier Bees's Honey Apple, Honey Bread, and Honey Porkchop
 Create's Honeyed Apple