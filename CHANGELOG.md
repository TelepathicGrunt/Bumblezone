### **(V.6.4.9 Changes) (1.19.2 Minecraft)**

##### Items:
Fixed recipe to make Bee Bread from Honey Buckets.

Cleaned up amount of recipes shown for Incense Candles in JEI, REI, and EMI.


### **(V.6.4.8 Changes) (1.19.2 Minecraft)**

##### Advancements:
Changed it so that Bumblezone's advancement screen is always visible even if you have no completed a prior advancement before.

Reorder advancements so that the teleportation advancements are shown first.

Renamed and adjusted descriptions for a few advancements.

Teleporting out of Bumblezone advancement now does not have a speed requirement and can be granted if exiting below or above dimension bounds.

Added a new advancement for crafting an Incense Candle with two or more potions! (The candle's stats is a mix between the potion's stats)

##### Dimension:
Very slight color change to dimension fog color.
 Remember folks, there's config options to change the dimension's fog color, thickness, or turn it off.

##### Block:
Fixed Incense Candle placing crashing servers. My bad.

Fixed it so that Incense Candles and Super Candles's flaming wick now can set projectiles on fire again like arrows.

Flaming projectiles going through the Incense Candle or Super Candle wick's space will now light the candle.
 Hitting the body of candle with flaming projectile will also still light the candle. 

Incense Candles now can be made from Super Candles too. Still needs Royal Jelly Bottle and 1 or more potion.

Lightning hitting the wick space of Super Candles or Incense Candles will light the candles.
 If the lightning hits the body of the candle, it'll destroy the candle!

##### Bee Queen:
Forgot to add two of Bumblezone's Music Discs into the tier 4 Bee Queen trades.

##### Entities:
Nerfed Beehemoth flying speeds a bit to better balance it. Base speed can still be changed in config.

##### Mod Compat:
SlimyBoyos mod now lets Honey Slime pick up items.

NOTE: If you are using Sodium, please download Unofficial Sodium Biome Blending Fix mod so that Sodium does not break
 the rendering of Bumblezone's Incense Candle. The bug fix mod can be downloaded here: 
 https://www.curseforge.com/minecraft/mc-mods/unofficial-sodium-biome-blending-fix


### **(V.6.4.7 Changes) (1.19.2 Minecraft)**

##### Blocks:
Pile of Pollen no longer slows Beehemoth or Bees down when inside it.

Pile of Pollen now more optimized with its Hidden effect. Less checking occurs while in block.
 Hidden effect is granted when Pile of Pollen covers up to just below the eye's height (Hidden 1) 
 and when eyes are fully covered by Pile of Pollen, bees will give up chasing you unless you exit the Pile of Pollen (Hidden 2)
 Hidden effect of any level will act like 100% Invisibility effect where mobs won't see you until you touch/hit them. (Or they previously seen you)

Sticky Honey Residue/Sticky Honey Redstone is a bit more optimized now too.

Honey Web and Redstone Honey Web cannot apply slowness/stickiness to creative players now.

##### Items:
Carpenter Bee Boots now only mine the below wood, leaves, honeycomb, or wax block when looking downward.
 More intuitive and prevents accidental block mining.

##### Entities:
Fixed Bee Queen reward item being spawned too far off to the side.

Honey Slime is now immune to the slowness/stickiness applied by Sticky Honey Residue, Sticky Honey Redstone, Honey Web and Redstone Honey Web.

Hopefully fixed Honey Slimes spawning on slopes and suffocating in blocks when chunk is first created.

##### Enchantments:
Condensed Bumblezone's enchantment descriptions that gets shown by Enchantment Description mod.

##### Mod Compat:
Added Bee Queen randomization trades for dyed items from Applied Energistics 2, Another Furniture, Bontany Pots, Companion,
 Cat Walks Inc, Farmer's Delight, Kibe, Modern Industrialization, Snowy Spirit, and Supplementaries.


### **(V.6.4.6 Changes) (1.19.2 Minecraft)**

##### Blocks:
Set the base of lit Super Candles/Incense Candles to return fire damage type for pathfinding.
 Should help stop other mod's mobs from pathfinding through the lit candles and burning.

##### Items:
Increased the durability for all Bumblezone bee armor by 2.4x.

##### Features:
Fixed checks for valid spot for Bee Dungeons/Spider Infested Bee Dungeons.

##### Entities:
Added three tags to make it easier to define what items will lure Beehemoth and what item can lure/breed Honey Slime.
 Honey Slime will now follow players holding Charm, VanillaTweaks, or Supplementaries's Sugar Block and can be bred with that along with vanilla's Sugar item.
 `the_bumblezone:mob_luring/beehemoth`
 `the_bumblezone:mob_luring/beehemoth_fast_luring`
 `the_bumblezone:mob_luring/honey_slime`

##### Advancements:
Mention that Honey Slime requires Sugar to breed in the Queen's Desire advancement for it.


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
Changed my mind and now Biome Makeover's Black Thistle and Blue Skies's Lucentroot flowers no longer spawn in Hanging Garden structure now.
 Also blacklisted several flowers from Natural Decor Mod and Natural Expansion mod due to not looking good in structure. 


### **(V.6.4.4 Changes) (1.19.2 Minecraft)**

##### Fluids:
Significantly fixed rendering issues with Honey Fluid and Royal Jelly Fluid.
 Also fixed particles from fluid spawning way out of the fluid at times.

Fixed Honey Fluid not falling when in midair.

##### Misc:
Fixed bug with vanilla Bees that make them not threadsafe when created as part of worldgen threaded chunk creation.
 Very niche rare crash to even get to happen but good for me to still patch just in case it does cause issues for people rarely.

Cleaned up some more mixins to make them more stackable with other people's mixins

##### Items:
Fixed Pollen Puff sometimes placing blocks at invalid locations like Biome Makeover's Moth Blossom flower in midair.

##### Mod Compat:
Bosses of Mass Destruction's Void Lily, Twilight Forests's Thorn Rose, Sria's Flowers's small flowers,
 and Biome Makeover's Black Thistle and Foxglove can spawn in Hanging Garden structure now.

Farmer's Delight's Wild Tomatoes plant cannot spawn in Hanging Gardens structure now.

Biome Makeover's Black Thistle and Foxglove can be multiplied by Pollen Puff now.


### **(V.6.4.3 Changes) (1.19.2 Minecraft)**

##### Fluids:
Fixed Royal Jelly Fluid missing visual overlays and player physics when inside fluid.

Fixed being able to "jump" while on ground inside Royal Jelly Fluid and Sugar Water Fluid.

##### Structures:
Reduce chance of maps in Hive Temple loot a little bit.
 Increased number of threads spawned by Bumblezone so multiple Hive Temple maps and Honey Compasses can search at once from 1 to 3.

##### Misc:
Fixed LGBT+ and Ukraine Bee Skin configs fighting each other on applying. And changed the default config values for them.

Went through and cleaned up and modified several mixins to make some of them less hacky.


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
Cleaned up how Enderpearl teleporting works when hitting Llamarama Bumble Llama or hits MC Dungeons Armors's Bee Nest/Beehive armors.
 The following tags were added to handle the new way of doing compat and lets players add addition entities or items to hit to enter Bumblezone!

If entity hit is tagged `the_bumblezone:enderpearl_teleporting/target_entity_hit_anywhere`,
 will teleport thrower into Bumblezone.

If entity hit is tagged `the_bumblezone:enderpearl_teleporting/target_entity_hit_high`,
 top half of hitbox must be hit to teleport thrower into Bumblezone.

If entity hit is tagged `the_bumblezone:enderpearl_teleporting/target_entity_hit_low`,
 bottom half of hitbox must be hit to teleport thrower into Bumblezone.

If entity hit is wearing armor tagged `the_bumblezone:enderpearl_teleporting/target_armor`, will teleport thrower into Bumblezone.
 (Helmet requires top 40% of hitbox. Chestplate require top 60%. Leggings require below 60%. Boots require below 40%)
 
If entity hit is holding item tagged `the_bumblezone:enderpearl_teleporting/target_held_item`, will teleport thrower into Bumblezone.

Fixed overlay issues with REI's tooltip while on Crystalline Flower's screen


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
Blacklisted several MC Dungeons Weapons enchantments from being selectable in Crystalline Flower for balancing the game. 
 Controlled by the enchantment tag: `the_bumblezone:blacklisted_crystalline_flower_enchantments`
 The newly blacklisted enchantments from MCDW are Void Strike, Void Shot, Radiance, Radiance Shot, Guarding Strike,
 Burst Bowstring, Dynamo, Shadow Barb, Shadow Shot, and Shared Pain.

MC Dungeons Weapons's Bee Stinger item can be used to craft Stinger Spear and repair it now too!

Throwing Enderpearl at a mob or armor stand wearing MC Dungeons Armors's Bee Nest/Beehive helmet or chestplate will
 teleport the thrower into Bumblezone dimension!
 

### **(V.6.4.0 Changes) (1.19.2 Minecraft)**

##### Music:
Added two new songs by LudoCrypt!
 They will play randomly in Bumblezone but they also have Music Disc forms obtainable from Wandering traders!
 The songs can be obtained from Bandcamp here from LudoCrypt:
 https://ludocrypt.bandcamp.com/track/bee-laxing-with-the-hom-bees
 https://ludocrypt.bandcamp.com/track/la-bee-da-loca

##### Textures:
Add Continuity on to have Connected Textures for Porous Honeycomb Block and Empty Honeycomb Brood Block when either are 
 touching Filled Porous Honeycomb Block or Honeycomb Brood Block! Now is a recommended optional dependency due to how good this looks lol.
 Special thanks to CrispyTwig for setting up the CTM resource files!

##### Blocks:
Fixed opening Crystalline Flower's UI not incrementing the stat for interacting with it.
 (Was logging an exception to the log file)

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

Pollen Puff thrown at Mobscarecrow's Default Scarecrow mob will spawn pumpkin stems nearby on valid blocks/farmland

Pollen Puff thrown at Mobvote2022's Sniffer will spawn Moss Carpet nearby.

Pollen Puff thrown at Probably Chests's Lush Mimic Chests may rarely spawn Azalea or Flowering Azalea nearby.

Pollen Puff thrown at Woof's Lush variant Wolf will spawn Moss Carpet nearby.

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
Added config to allow turning off teleportation to Bumblezone if thrown Enderpearl hits top of the hitbox of Llamarama's Bumble Llama.

Removed unused config entry for Productive Bees as that mod is not on Fabric/Quilt.

Bee Queen will trade now for the following modded items now. `the_bumblezone:bz_bee_queen_trades/` tag folder:
 Farmersdelight's Honey Cookie and Honey Glazed Ham
 Adorn's Honeycomb Crate and beverages
 Skinned Lanterns's bee and honey lanterns (regular and soul)
 Expanded Delight's Peanut Butter Honey Sandwich and Peanut Honey Soup
 Create's Honeyed Apple

Bees can be right-clicked fed with these following modded items now. `the_bumblezone:bee_feeding_items` item tag:
 Farmersdelight's Honey Cookie and Honey Glazed Ham
 Expanded Delight's Peanut Butter Honey Sandwich and Peanut Honey Soup
 Create's Honeyed Apple
