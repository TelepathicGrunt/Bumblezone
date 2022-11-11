### **(V.6.4.0 Changes) (1.19.2 Minecraft)**

##### Music:
Added two new songs by LudoCrypt!
 They will play randomly in Bumblezone but they also have Music Disc forms obtainable from Wandering traders!
 The songs can be obtained from Bandcamp here from LudoCrypt:
 https://ludocrypt.bandcamp.com/track/bee-laxing-with-the-hom-bees
 https://ludocrypt.bandcamp.com/track/la-bee-da-loca

##### Blocks:
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

##### Items:
Made an item form for all Carvable Wax blockstates to make building with it easier.
 Mining a Carvable Wax will drop the item form of its blockstate.
 
Adjusted the stone textures of Bee Cannon and Crystal Cannon to match the texture change for Sugar Infused Stone/Sugar Infused Cobblestone.

Adjusted the texture slightly for one variant of Stingless Bee Helmet and Bumble Bee Chestplate.

Carpenter Bees boots now can mine through Carvable Wax block when crouched on top in center of block.

Added tooltip descriptions to Honey Compass to make it more clear on how to use it.

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

##### Enchantments:
Nerfed Neurotoxins enchantment so its missed counter does not increment if player hits entity while entity is already paralyzed.
 Helps prevent paralysis locking bosses.

Made Neurotoxin require more bookshelves before showing up in Enchanting Table

Made Potent Poison enchantment require 1 more bookshelf before showing up in Enchanting Table

##### Teleportation:
Fixed display message for when teleporting to Bumblezone fails due to not having a certain required block below the hive.
 This is controlled by the_bumblezone:required_blocks_under_hive_to_teleport block tag which is empty by default to not need a required block for teleporting.

##### Structures:
Made Pollinated Streams structure now spawn above ground with pillar support! Looks cooler and much, much easier to find!

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