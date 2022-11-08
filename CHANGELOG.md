### **(V.6.3.9 Changes) (1.19.2 Minecraft)**

##### Textures:
Add Continuity on to have Connected Textures for Porous Honeycomb Block and Empty Honeycomb Brood Block when either are 
 touching Filled Porous Honeycomb Block or Honeycomb Brood Block! Now is a required dependency due to how good this looks lol.

##### Blocks:
Fixed opening Crystalline Flower's UI not incrementing the stat for interacting with it.
 (Was logging an exception to the log file)

Adjusted textures for Sugar Infused Stone and Sugar Infused Cobblestone a bit.

Fixed Honeycomb Brood Block and Empty Honeycomb Brood Block facing the wrong way when in item form in inventory/creative menu.

Honeycomb Brood Block now has many more animation variations so the block is not always in sync with other blocks of itself in the world.

Carvable Wax block now has a facing property. Some of the patterns will face different directions based on how you place it!

Glistering Honey Crystal now uses a facing property instead of axis property. Allows for more directions when placing for easier builds!

Added sound for lighting Super/Incense Candles.

##### Items:
Pollen Puff thrown at Mobscarecrow's Default Scarecrow mob will spawn pumpkin stems nearby on valid blocks/farmland

Pollen Puff thrown at Mobvote2022's Sniffer will spawn Moss Carpet nearby.

Pollen Puff thrown at Probably Chests's Lush Mimic Chests may rarely spawn Azalea or Flowering Azalea nearby.

Pollen Puff thrown at Woof's Lush variant Wolf will spawn Moss Carpet nearby.

Made an item form for all Carvable Wax blockstates to make building with it easier.
 Mining a Carvable Wax will drop the item form of its blockstate.
 
Adjusted the stone textures of Bee Cannon and Crystal Cannon to match the texture change for Sugar Infused Stone/Sugar Infused Cobblestone.

Adjusted the texture slightly for one variant of Stingless Bee Helmet and Bumble Bee Chestplate.

Carpenter Bees boots now can mine through Carvable Wax block when crouched on top in center of block.

##### Enchantments:
Nerfed Neurotoxins enchantment so its missed counter does not increment if player hits entity while entity is already paralyzed.
 Helps prevent paralysis locking bosses.

Made Neurotoxin require more bookshelves before showing up in Enchanting Table

Made Potent Poison enchantment require 1 more bookshelf before showing up in Enchanting Table

##### Teleportation:
Fixed display message for when teleporting to Bumblezone fails due to not having a certain required block below the hive.
 This is controlled by the_bumblezone:required_blocks_under_hive_to_teleport block tag which is empty by default to not need a required block for teleporting.

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


### **(V.6.3.8 Changes) (1.19.2 Minecraft)**

##### Mod Compat:
Added some methods in BumblezoneAPI class for other mods to have easier compat with Comb Cutter enchantment's beehive/bee nest drops.

##### Dimension:
Fixed Bumblezone dimension fog/skybox being darker when it is raining/thundering in Overworld.

##### Textures:
Added an internal Anti-Trypophobia resourcepack to this mod.
 You can enable it by going into the resourcepack screen and move the resourcepack to the top of the right side to activate it.
 Mostly just tries to make vanilla's Honeycomb Block and some of Bumblezone's hole-based blocks have lower contrast and look more flatter to help.
 This is not the desired look for Bumblezone but as a good last resort if you're feeling uncomfortable with the default textures.


### **(V.6.3.7 Changes) (1.19.2 Minecraft)**

##### Blocks:
Fixed Incense Candle and Crystalline Flower getting mixed up in registration.

Crystalline Flower now blacklists the following enchantments from being an option in this flower's UI:
 minecraft:soul_speed, minecraft:swift_sneak, supplementaries:stasis, apotheosis:infusion, ars_elemental:mirror_shield, ars_nouveau:reactive
 Some of these are specific to finding a certain structure or shouldn't be pickable at all. For balance or technical reasons.

Crystalline Flower now has crystallineFlowerExtraTierCost config added.
This will increase the tier cost of all enchantments available by whatever value you add.
 Default is 0. Negative numbers will decrease the tier cost for enchantments.

Crystalline Flower now has crystallineFlowerExtraXpNeededForTiers config added.
 This will increase the exp required for increasing the flower's tier. (This includes item consuming as items are converted to exp)
 Default is 0. Negative numbers will decrease the amount of exp needed to reach each tier.

##### Lang:
Fixed missing lang entry for a config entry.


### **(V.6.3.6 Changes) (1.19.2 Minecraft)**

##### Misc:
Fixed servers unable to start due to forgetting to change pollen puff particle name in biome json files.

##### Structures:
Fixed Cell Maze not spawning. Sorry about that.
 Was due to accidentally breaking some Cell Maze structure template pool json files by mistake...

##### Blocks:
Fixed Creative players taking honey from Filled Porous Honeycomb block adding Honey Bottle to inventory and decrementing Glass Bottle item.
Creative players should have unlimited use when removing honey from the block to match behavior vanilla has with their blocks.

##### Mod Compat:
Pollen Puff hitting Llamarama's Bumble Llama may spawn Poppy, Dandelion, Cornflower, or Pink Tulip nearby!

Enderpearls hitting top portion of Llamarama's Bumble Llama's hitbox will teleport players to Bumblezone dimension.

##### Textures:
Textures changed for several Bumblezone honeycomb/brood blocks. New textures mainly by CrispyTwig
 Connected Textures will be coming later to make textures connect better.


### **(V.6.3.5 Changes) (1.19.2 Minecraft)**

##### Structures:
Hanging Gardens will now spawn most flowers from the `minecraft:small_flowers` and `minecraft:tall_flowers` block tag including other mod's flowers.
 Use `the_bumblezone:blacklisted_hanging_garden_flowers` block tag to prevent certain small flower tagged blocks from spawning in Hanging Gardens.
 Use `the_bumblezone:allowed_hanging_garden_flowers` to add more flowers to spawn that are not in `minecraft:small_flowers` block tag.
 The tall flower versions of these tags are `the_bumblezone:blacklisted_hanging_garden_tall_flowers` and `the_bumblezone:allowed_hanging_garden_tall_flowers`

 Stopped Bumblezone's caves from eating away at part of Hanging Garden or Throne Pillar.

##### Effects:
Improved the effect icon textures for Beenergized, Hidden, Protection of the Hive, and Wrath of the Hive.

Wrath of the Hive's effect of growing nearby Brood blocks faster only applies if it is the player that has Wrath of the Hive.
 Wrath of the Hive level 1 is now throttled to only anger nearby bees every 20 ticks instead of every tick (spiders/bears/arthropods will have this level of wrath)
 Just a small change to squeeze out better performance on servers.

##### Config:
Changed the default config value for keepBeeEssenceOnRespawning from false to true and renamed it to
 keepEssenceOfTheBeesOnRespawning so that everyone who had ran Bumblezone before now gets this true value.
 Less confusion this way.

Added defaultDimension config option for modpacks whose default dimension is not Overworld.
 Very very few people will ever need to touch this option. Let modpack makers be the ones to touch this only.

##### Blocks:
Fixed Comparator not correctly outputting power based on Crystalline Flower's height/tier.

##### Items:
Fixed Pollen Puff able to multiply the following blocks that were tagged as minecraft:small_flowers when they don't look like a flower or for balance reasons:
 `byg:prairie_grass`
 `byg:pollen_block`
 `gaiadimension:missingno_plant`
 `bushierflowers:grown_wither_rose`
 `farmersdelight:wild_beetroots`

Carpenter Bee Boots now only mines boot mineable blocks below you if you are more centered on the block.
 Crouching on block's edge won't mine the block anymore so you can crouch walk to not walk off edges or ledges.

##### Mod Compat:
Made Bumblezone's Bee Renderer more mod compat friendly with other mods that add Bee Renderers.


### **(V.6.3.4 Changes) (1.19.2 Minecraft)**

##### Entities:
Fixed Bee Queen being always angry and spawning bees when set to noAi by command. (Also disables trading when noAi)

Fixed Beehemoth being able to be fed, tamed, and ridden when set to noAi by command.

##### Items:
Pollen Puff has a chance of spawning Pumpkin Stem nearby on valid blocks if it hits a Snow Golem with Pumpkin still on.

Pollen Puff has a chance of spawning Pumpkin Stem nearby on valid blocks if it hits a Morebabies's Snow Golem with Pumpkin still on.


### **(V.6.3.3 Changes) (1.19.2 Minecraft)**

##### Blocks:
Added two new configs for Crystalline Flower block: crystallineFlowerConsumeExperienceUI and crystallineFlowerConsumeItemUI
 Turning those off along with these two configs will disable flower's consuming entirely:
 crystallineFlowerConsumeItemEntities and crystallineFlowerConsumeExperienceOrbEntities

Added a third config called crystallineFlowerEnchantingPowerAllowedPerTier which sets the "enchanting power" of the flower per tier.
 Think of this like how Enchanting Tables only shows stronger or rarer enchantments when you have more bookshelves.
 Except here, the flower's tier times this config value is used as the threshold to know what enchantment and level to show.

Crystalline Flower now blacklists Veinmining's Vein Mining enchantment from being an option in this flower's UI.
 Controlled by `the_bumblezone:blacklisted_crystalline_flower_enchantments` enchantment tag.

##### Items:
Pollen Puff has a chance of spawning Red or Brown Mushroom if it hits Adventurez's red or Brown Fungus mob respectively of their type.

Pollen Puff can spawn Chorus Grass if it hits Better End's End Slime mob.

Pollen Puff can spawn Jungle Plant if it hits Better Nether's Jungle Skeleton.

Pollen Puff can spawn Spore Blossom if it hits Bosses of Mass Destruction's Void Blossom. Why you would do this? No clue lol.

Pollen Puff has a chance of spawning vine if it hits Rotten Creatures's Swampy mob.


### **(V.6.3.2 Changes) (1.19.2 Minecraft)**

##### Blocks:
Fixed Crystalline Flower's enchantment text symbols not showing correct symbol.


### **(V.6.3.1 Changes) (1.19.2 Minecraft)**

##### Blocks:
Fixed client side blockstate visual sometimes being incorrect when mass feeding Crystalline Flower by dropping items into it

##### Structures:
Made Hanging Gardens more common

##### Biomes:
Tried to lower chance of Spider/Cave Spider spawning in relation to Enderman and Phantoms


### **(V.6.3.0 Changes) (1.19.2 Minecraft)**

##### Blocks:
Fixed Glistering Honey Crystal not dropping itself when mined with Silk Touch.

Improved Glistering Honey Crystal's sparkling so it can appear anywhere on the block instead of just the corners.

Improved Glistering Honey Crystal's sparkling to use a new animated particle texture now.

Fixed unsuccessful lighting of Super Candles/Incense Candles still damaging/consuming the lighting item used by the player.

Added Crystalline Flower block!
 Spawns in the new Hanging Garden structure!
 Right click it to open up a GUI where you can grow the plant by giving it you XP or stacks of items to consume. Grows up to 7 blocks (tiers) high.
 Put a book or enchanted book into the enchantment side and you can select what enchantment you want to add to it! This will cost some tiers to work.
 More and stronger enchantments are available the higher the block's tier! But you cannot add more enchantments to books that have 3 or more enchantments already.
 You can grow the block's tiers even outside the GUI by dropping items into the block or letting experience orbs touch the block!
 Some items grant more progress towards upgrading the tiers. And when using tiers to enchant, some Honey Crystal Shards will drop!
 You can blacklist items from consumption, blacklist what enchantments are available, or change what items can be enchanted by tags:
 `the_bumblezone:crystalline_flower/cannot_consume` (item tag)
 `the_bumblezone:crystalline_flower/can_be_enchanted` (item tag)
 `the_bumblezone:crystalline_flower/xp_2_when_consumed` (item tag)
 `the_bumblezone:crystalline_flower/xp_5_when_consumed` (item tag)
 `the_bumblezone:crystalline_flower/xp_25_when_consumed` (item tag)
 `the_bumblezone:crystalline_flower/xp_100_when_consumed` (item tag)
 `the_bumblezone:blacklisted_crystalline_flower_enchantments` (enchantment tag)

Fixed Pile of Pollen, Sticky Honey Redstone, and Sticky Honey Residue blocking pistons when they should be destroyed on push.

##### Structures:
Added Hanging Gardens structure that you can find Crystalline Flower in!
 Be sure to look up! Can be found in Sugar Water Floor or Pollinated Fields biomes.

##### Biomes:
Fixed Pigs sometimes rarely spawning instead of Enderman.

Fixed Pollinated Fields and Pollinated Pillar sometimes placing the Pollen Piles on invalid blocks it cannot survive on.

##### Advancements:
Added more info for bee feeding and sugar infused stone advancements to clarify how to complete them.

Added advancement for Hanging Garden and Crystalline Flower block.
