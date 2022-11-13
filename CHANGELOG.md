### **(V.6.4.1 Changes) (1.19.2 Minecraft)**

##### Blocks:
Nerfed Crystalline Flower so that it requires 2090xp to reach max tier (About the same as player going from xp level 0 to 35)
 Previously, the flower required xp roughly about the same as a player going from 0 to 23 xp levels.
 Was complained about being too low for the ability to pick enchantments. Now it requires about 2.7 time more xp.
 You can still increase or reduce this xp cost in the config file for Bumblezone.

##### Effects:
Wrath of the Hive and Protection of the Hive will no longer affect nearby Bees that have their AI turned off.


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
