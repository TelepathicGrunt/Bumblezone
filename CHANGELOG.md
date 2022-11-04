### **(V.6.3.8 Changes) (1.19.2 Minecraft)**

##### Mod Compat:
Future-proofed Pollen Puff compat with Pokecube for when they change the registry name of some pokemon.

Pollen Puff hitting Resourceful Bees's Crimson, Warped, Red, and Brown Bees will spawn those respective mushrooms nearby.

Bee Queen will now trade for a ton more of Resourceful Bees's items!

Added some methods in BumblezoneAPI class for other mods to have easier compat with Comb Cutter enchantment's beehive/bee nest drops.

Enderpearls hitting top hitbox portion of players wearing Productive Bees's Bee Nest Helmets will teleport players to Bumblezone dimension.

##### Dimension:
Fixed Bumblezone dimension fog/skybox being darker when it is raining/thundering in Overworld.

##### Textures:
Added an internal Anti-Trypophobia resourcepack to this mod.
 You can enable it by going into the resourcepack screen and move the resourcepack to the top of the right side to activate it.
 Mostly just tries to make vanilla's Honeycomb Block and some of Bumblezone's hole-based blocks have lower contrast and look more flatter to help.
 This is not the desired look for Bumblezone but as a good last resort if you're feeling uncomfortable with the default textures.


### **(V.6.3.7 Changes) (1.19.2 Minecraft)**

##### Blocks:
Crystalline Flower now blacklists the following enchantments from being an option in this flower's UI:
 minecraft:soul_speed, minecraft:swift_sneak, supplementaries:stasis, apotheosis:infusion, ars_elemental:mirror_shield, ars_nouveau:reactive
 Some of these are specific to finding a certain structure or shouldn't be pickable at all. For balance or technical reasons.

Crystalline Flower now has crystallineFlowerExtraTierCost config added.
 This will increase the tier cost of all enchantments available by whatever value you add.
 Default is 0. Negative numbers will decrease the tier cost for enchantments.

Crystalline Flower now has crystallineFlowerExtraXpNeededForTiers config added.
 This will increase the exp required for increasing the flower's tier. (This includes item consuming as items are converted to exp)
 Default is 0. Negative numbers will decrease the amount of exp needed to reach each tier.


### **(V.6.3.6 Changes) (1.19.2 Minecraft)**

##### Misc:
Fixed servers unable to start due to forgetting to change pollen puff particle name in biome json files.

##### Structures:
Fixed Cell Maze not spawning. Sorry about that.
 Was due to accidentally breaking some Cell Maze structure template pool json files by mistake...

##### Blocks:
Fixed Creative players taking honey from Filled Porous Honeycomb block adding Honey Bottle to inventory and decrementing Glass Bottle item.
 Creative players should have unlimited use when removing honey from the block to match behavior vanilla has with their blocks.

##### Textures:
Textures changed for several Bumblezone honeycomb/brood blocks! New textures mainly by CrispyTwig
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
Pollen Puff can spawn a Buttercup nearby if it hits an adult Buzzier Bees's Moobloom mob.

Fixed Pollen Puff able to multiply the following blocks that were tagged as minecraft:small_flowers when they don't look like a flower or for balance reasons:
 `byg:prairie_grass`
 `byg:pollen_block`
 `gaiadimension:missingno_plant`
 `resourcefulbees:gold_flower`
 `blue_skies:muckweed`
 `blue_skies:brittlebush`
 `blue_skies:brittlebush`
 `bushierflowers:grown_wither_rose`
 `farmersdelight:wild_beetroots`

Carpenter Bee Boots now only mines boot mineable blocks below you if you are more centered on the block.
 Crouching on block's edge won't mine the block anymore so you can crouch walk to not walk off edges or ledges.

##### Mod Compat:
Fixed visual incompatibility with Realistic Bees mod. Bees should be smaller now with that mod on.


### **(V.6.3.4 Changes) (1.19.2 Minecraft)**

##### Entities:
Fixed Bee Queen being always angry and spawning bees when set to noAi by command. (Also disables trading when noAi)

Fixed Beehemoth being able to be fed, tamed, and ridden when set to noAi by command.

##### Mod Compat:
Added compat with Quark where some of Bumblezone's items will show up in Quark's enchantment tooltips.
 Can be disabled with Bumblezone's config option called: injectBzItemsIntoQuarkEnchantmentTooltipsCompat

##### Items:
Pollen Puff has a chance of spawning Pumpkin Stem nearby on valid blocks if it hits a Snow Golem with Pumpkin still on.

Pollen Puff has a chance of spawning Melon Stem nearby on valid blocks if it hits a EarthMobsMod's Melon Golem with Melon still on.

Pollen Puff can spawn Red Mushroom nearby if it hits Alex's Mob's Bunfungus.

Pollen Puff has a chance of spawning Pumpkin Stem nearby on valid blocks if it hits a Morebabies's Snow Golem with Pumpkin still on.

Pollen Puff can spawn Nether Wart nearby if it hits Stalwart Dungeon's Nether Keeper or nether Wart Cocoon.


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

Crystalline Flower now blacklists the following Apotheosis enchantments from being an option in this flower's UI:
 apotheosis:scavenger, apotheosis:life_mending, apotheosis:berserkers_fury, apotheosis:knowledge, apotheosis:crescendo, apotheosis:earths_boon, apotheosis:chainsaw, apotheosis:endless_quiver
 Controlled by `the_bumblezone:blacklisted_crystalline_flower_enchantments` enchantment tag.


##### Items:
Pollen Puff has a low chance of spawning vine if it hits TakesAPillage's Clay Golem.

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

##### Structures:
Added Hanging Gardens structure that you can find Crystalline Flower in!
 Be sure to look up! Can be found in Sugar Water Floor or Pollinated Fields biomes. 

##### Biomes:
Fixed Pigs sometimes rarely spawning instead of Enderman.

Fixed Pollinated Fields and Pollinated Pillar sometimes placing the Pollen Piles on invalid blocks it cannot survive on.

##### Advancements:
Added more info for bee feeding and sugar infused stone advancements to clarify how to complete them.

Added advancement for Hanging Garden and Crystalline Flower block.
