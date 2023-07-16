#TODO: create essence events

#TODO: Electric Ring Textures/animations

#TODO: Implement rootmin essence event.

#TODO: Re-evaluate advancements. Add more for new additions as well.

#TODO: Add music for essence events

#TODO: Add music for sanctums

#TODO: Add sounds for Rootmin 

#TODO: Add more sanctum rooms. Do sanctum variants

#TODO: on screen and player character spike effects for essence items active.

#TODO: Check up on mod compat (resourceful bees and productive bees especially. See Buzzing Briefcase for those too)


### **(V.7.0.0 Changes) (1.12.1 Minecraft)**

##### Major:
Updated to 1.20.1!

NOTE: Forge users must use 1.20.1 Forge up to v47.1.3 and nothing newer. 
 Forge starting merging in low-quality, breaking code changes that helps no one. 
 v47.1.3 Forge is most stable and is what I will build against until neoForged is publicly available.


#### *\*Additions*

##### Structures:
Added Subway structure that is a massive tunnel system with flowing air that bees uses to travel around quickly! 
 Lots of loots scattered throughout from all the travelers that has gone through this subway. 
 Can you fight against the airflow to explore everything?

Added Ancient Hoops structure which are many old pillars of wax that are decaying. Some pillars still have a hoop
 on top that mysteriously pulls air flow through. Grab a Bumblebee Chestplate and start flying from pillar to pillar!

Added Ancient Shrine structure that resembles a giant flower filled with pollen! Though the pollen looks suspicious...
 Maybe try some archaeology in this structure?

##### Blocks:
Added Ancient Wax blocks! Very high blast resistance, uncraftable, and will give Slowness, Weakness, and Mining Fatigue to 
 anyone who has not consumed Essence of the Bees before and are standing on said Ancient Wax blocks. You can use shears to 
 convert between 3 different patterns for these blocks. Only found in certain structures. Can be crafted into stairs or slabs.
 Stonecutter has more efficient recipe for making stairs/slabs.

Added Luminescent Wax Channel blocks, Luminescent Wax Corner blocks, and Luminescent Wax Node blocks! 
 Very high blast resistance, uncraftable, and will give Slowness, Weakness, and Mining Fatigue to anyone who has 
 not consumed Essence of the Bees before and are standing on said Ancient Wax blocks. If the block has a light in it, 
 people who consumed Essence of the Bees before and standing on said blocks will get Speed, Damage Resistance, 
 and Beenergized. The light forms will repel Piglins and Hoglins and also will count towards empowering Enchanting Table 
 like Bookshelves do. You can use shears to change the direction of these blocks. Only found in certain structures.
 The light forms can be crafted into lightless forms.

Windy Air added that will push any entity in whatever direction the air block is blowing! Only found in certain structures.
 Can be found in Creative Menu for builders! Can be controlled by this entity type tag: `the_bumblezone:windy_air/immune_to_push_effect`
 Bee Queen will give you Windy Air for Bumblezone armor or tools as an easy way to dispose of those unstackable items.

Heavy Air added that will pull entities down and remove Levitation, Slow Falling, Jump Boost, and non-creative flying! 
 Has no effect on Bees and Beehemoth. Only found in Sempiternal Sanctums. Can be found in Creative Menu for builders!
 Can be controlled by this entity type tag: `the_bumblezone:heavy_air/immune_to_gravity_effect`

Suspicious Pile of Pollen is added now! You may find these scattered throughout Bumblezone in certain structures. Brush them for loot!
 Also found in Creative Menu for builders, but you'll need to set the items or loot table for the block by command.
 They can also be found throughout the Pollinated Fields biomes with a decent amount of possible loot!


##### Items:
Essence of Life - The reward for beating the Green Sempiternal Sanctum structure! Holding this item in offhand will grow 
 crops and saplings nearby, heal mobs tamed by you, and heal other mobs or players on the same team as you! Also, will cure 
 Poison and Wither effect from the healed mobs. It has 1000 use before it is depleted and needs 10 minutes to recharge by 
 being in the inventory of a player that had consumed Essence of the Bees in the past. Add more effects to 
 `the_bumblezone:essence/life/cure_effects` mob effects tag for this item to be able to remove said tagged effects from allies/pets!
 Use the following two tags to control what plants can be grown or not with this item. Let me know if a modded plant is not able to be grown:
 `the_bumblezone:essence/life/grow_plants`
 `the_bumblezone:essence/life/force_disallowed_grow_plant`

Essence of Radiance - The reward for beating the Yellow Sempiternal Sanctum structure! Holding this item in offhand while being
 in a spot with sky brightness 13 or above (basically in view of the sky) will grant you Regeneration 1, Saturation 1, Speed 1,
 Haste 2, and Damage Resistance 2. It will also slowly heal your armor durability as well over time! It has 4800 use before it is 
 depleted and needs 10 minutes to recharge by being in the inventory of a player that had consumed Essence of the Bees in the past.
 Add or remove effects to give to player by editing `the_bumblezone:essence/radiance/sun_effects` mob effects tag.

Essence of Knowing - The reward for beating the Purple Sempiternal Sanctum structure! Holding this item in offhand will highlight mobs
 and outline certain block entities near you! Allowing you to see what monster is nearby or hidden chests or suspicious blocks! It has 
 1200 use before it is depleted and needs 15 minutes to recharge by being in the inventory of a player that had consumed Essence of the Bees in the past.
 This highlighting can be configured on client side by config. Server owners can use the block and entity tags to force things to be highlighted
 or not regardless of what the client config is set as. (The tags can be used to also correct highlighting issues on other mod's mobs with this item on)
 The entity type tags you can edit are these. Disable tag overrides all:
 `the_bumblezone:essence/knowing/prevent_highlighting`
 `the_bumblezone:essence/knowing/forced_green_highlight`
 `the_bumblezone:essence/knowing/forced_cyan_highlight`
 `the_bumblezone:essence/knowing/forced_purple_highlight`
 `the_bumblezone:essence/knowing/forced_red_highlight`
 `the_bumblezone:essence/knowing/forced_orange_highlight`
 `the_bumblezone:essence/knowing/forced_yellow_highlight`
 `the_bumblezone:essence/knowing/forced_white_highlight`
 The block tags you can edit are these. Disable tag overrides all:
 `the_bumblezone:essence/knowing/block_entity_forced_highlighting`
 `the_bumblezone:essence/knowing/block_entity_prevent_highlighting`

Essence of Calming - The reward for beating the Blue Sempiternal Sanctum structure! Holding this item in offhand will make mobs
 nearby no longer get angry at you! (Let me know if a modded mob still attacks you while this essence is active) The effect is lost
 early  gets hurt by any mob or attacks any mob. If the player sprints while it is active, the item loses power crazy quickly! No rushing! 
 It has 600 use (seconds) before it is depleted and needs 10 minutes to recharge by being in the inventory of a player that had consumed Essence of the Bees in the past.
 The entity type tag `the_bumblezone:essence/calming/allow_anger_through` can be used to mark entities that should keep staying angry 
 at players with this calming effect active. Note, due to implementation, this tag may not always work but let me know if it fails so
 I can investigate the specific use case.

Essence of Raging - The reward for beating the Red Sempiternal Sanctum structure! Holding this item in offhand will highlight
 4 nearby hostile mobs in red. Killing them will grant you Strength status effect and it will highlight more hostile mobs nearby 
 (never more than 4 at a time) Each highlighted kill makes Strength effect stronger and maxes out at strength 16 after killing 7 highlighted mobs!
 Making this a powerful boss destroyer weapon! The Strength only lasts 15 seconds between kills. If time runs out or you kill a non-highlighted mob,
 the item goes into 10 second cooldown. Making any kill after maxed Strength will also set the item onto 10 second cooldown. 
 It has 28 use (highlighted kills) before it is depleted and needs 30 minutes to recharge by being in the inventory of a player 
 that had consumed Essence of the Bees in the past. It will set cooldown on all other Essence of Raging that player has as well 
 due to how powerful this item is. Add or remove effects to give to player by editing `the_bumblezone:essence/raging/rage_effects` mob effects tag.

Essence of Continuity - The reward for beating the White Sempiternal Sanctum structure! Holding this item in offhand will make dying
 not kill you and instead teleport you back to your respawn point with all health restored, hunger bar filled, neutral and 
 negative status effects removed, and fire removed. It only has 1 use before it is depleted and needs 40 minutes to recharge 
 by being in the inventory of a player that had consumed Essence of the Bees in the past. It will set cooldown on all other
 Essence of Continuity that player has as well due to how powerful this item is. It even works in hardcore worlds!
 It will also give you a Written Book with details of where you were and how you almost died.

Pile of Pollen item form is now available in Creative Menu and can be crafted from 9 Pollen Puff in Crafting Table.
 Silk Touch mining a Pile of Pollen that is layer 8 will drop the Pile of Pollen item instead of the Pollen Puff items.

Added Buzzing Briefcase item! Rare drop from Bee House and Honitel structure's loot. Also, a rare drop from tier 4 
 Bee Queen trades (Honey Block/Honey Buckets mainly as want items). Right click (or hold right click) on a bee to stuff
 it into the briefcase. It can hold up to 14 bees of any kind. Shift right click to open the briefcase UI where you can
 heal bees, put stingers back on, pollinate them, or grow babies up to be an adult. You can release the bees from the briefcase
 by UI or by left clicking on a block. Left clicking on an entity releases the bees and have them attack the entity you left clicked.
 Do shift left click to release all the bees at once from briefcase.

Added Bee Soup that is craftable from Bee Bread, Bowl, Beetroot, Potato, 2 Honey Combs, and 1 Bee Stinger. Drinking this
 soup gives you a long level 2 Beenergized effect and has a chance of inflicting Levitation, Slow Falling, Poison, Paralysis, 
 or Luck status effect on you. You could get multiple of these effects at once if you're unlucky (or lucky if it is good effects)!
 Can be found in some structures's loot!

Added Dirt Pellet item craftable from Coarse Dirt + Rooted Dirt. It can be thrown like a Snowball for 1 point of damage and high knockback.
 However, it will deal 3 points of damage to most flying mobs (modded included)! Controlled by these entity tags to change what will receive bonus damage:
 `the_bumblezone:dirt_pellet/extra_damage_dealt_to`
 `the_bumblezone:dirt_pellet/forced_no_extra_damage_dealt_to`

##### Entities:
Added a new entity called Variant Bee! It is exactly like the vanilla bee in behavior but has a different skin on. This replaces
 the old UUID system Bumblezone had to replace vanilla bee skins. Now that system is gone and the bee variants are now a dedicated
 entity for Bumblezone! 4 new variants were added as well. The variant's types are exposed by a "variant" string tag in their nbt. 
 You can add or remove variants to spawn by editing the variantBeeTypes config entry (Server config takes priority over client's). 
 Be sure to add new bee textures to these files for the new bee variant you added to spawn with the textures it needs!
 `assets/the_bumblezone/textures/entity/bee_variants/<VariantType>/bee.png`
 `assets/the_bumblezone/textures/entity/bee_variants/<VariantType>/bee_angry.png`
 `assets/the_bumblezone/textures/entity/bee_variants/<VariantType>/bee_angry_nectar.png`
 `assets/the_bumblezone/textures/entity/bee_variants/<VariantType>/bee_nectar.png`

Added a new non-living entity called Sentry Watcher that lives in Sempiternal Sanctum and will blow up if taken out of the structure or somehow killed.
 This bee status will charge at any living non-bee mob in front of it and will break through many blocks up to a certain total explosion resistance of the wall of blocks.
 The damage it deals to all in its way will increase the faster the statue is moving. Wear Bee Armor or Bee Curios/Trinkets will reduce a bit of damage as well.
 Some tags to configure its behavior are:
 `the_bumblezone:sentry_watcher/always_destroy` (block tag)
 `the_bumblezone:sentry_watcher/forced_never_destroy` (block tag)
 `the_bumblezone:sentry_watcher/activates_when_seen` (entity tag)
 `the_bumblezone:sentry_watcher/forced_never_activates_when_seen` (entity tag)

Added Snow Block to Clay Block and its opposite to Bee Queen Trades. 

##### Mod Compat:
Integration with Lootr was added by Nooby! Special thanks to him! 
 Worldgen placed Honey Cocoons will have a different texture when Lootr is on and have per-player loot.

Added emissive textures for some entities and blocks. 
 These will take effect if Optifine or Continuity or other rendering mod is on that reads _e Optifine emissive textures.


#### *\*Changes*

##### Blocks:
Walking through Pile of Pollen should not trigger Sculk Sensor now. Pile of Pollen will also block Sculk Sensor's detection if between it and the sound source.

String Curtains should properly block Sculk Sensor's detection if between it and the sound source.

Fluids should not show drip animations under Glistering Honey Crystal now.

Snow should now be placeable on Royal Jelly Block. 

Swords are now the efficient tools for breaking Honey Web and Redstone Honey Web blocks.

Axes are now the efficient tools for breaking Beehive Beeswax blocks.

Several Bumblezone's full solid blocks has instruments set for Note Blocks to play when under Note Blocks.

Crystalline Flower can be broken by pistons now and drop itself.

Glistering Honey Crystal should not suffocate mobs inside itself, and it blocks Redstone power like how Glowstone blocks power.

Any Super Candle can be crafted to another color with 2 dyes now!

Any String Curtain can be crafted to another color with 1 dye now!

Crystalline Flower now will hold items in its consume slot and to-enchant slot even when exiting the UI. These items will drop when flower breaks.

Fixed bug where Crystalline Flower drop loses their xp progress when in item form.

Honeycomb Brood Block feeding now can grant you Protection of the Hive even when you have Wrath of the Hive.

Increased duration of Protection of the Hive obtained from Honeycomb Brood Block feeding.

##### Items:
Renamed `the_bumblezone:bee_armors/bz_armor_ability_enhancing_gear` to `the_bumblezone:bee_armors/bz_armor_ability_enhancing_wearables`

Renamed the Bumble Bee Chestplate optional nbt from `requiredGearCountForForcedFlyingTime` to `requiredWearablesCountForForcedFlyingTime`

Cleaned up how Bumblezone items are grouped in vanilla Recipe Book

Added Stinger Spear, Bee Cannon, and Crystal Cannon to the `minecraft:tools` tag.

Pollen Puff will not multiply Pitcher Plant when thrown at it as Mojang did not want it to be too easy to multiply (Pitcher Plant cannot be bonemealed)

Essence of the Bees takes less time to consume now.

All color Essence items added plus Essence of the Bees is now fire-resistant and won't be destroyed by lava/fire.

Removed Carvable Wax from forge:wax/c:wax item tag to prevent recipe conflict with other mod's candle recipes.

Added configs to turn off Brewing Recipes

Added two new nbt that modpack makers can set for Bumble Bee Chestplate to change the flight time. 
 Add "forcedMaxFlyingTickTime" int to the item's nbt to set the new maximum flight time for that item.
 Add "requiredWearablesCountForForcedFlyingTime" to make the above nbt only apply if player has the correct number of bee wearables equipped.
 "forcedMaxFlyingTickTime", if present, will always apply if "requiredWearablesCountForForcedFlyingTime" is not present

Fixed one Bumble Bee Chestplate's wings being off when viewed from above and is flying.

Fixed recipe for Trans Bumble Bee Chestplate nuking the NBT of the original chestplate

Adjusted texture on one Trans Bumble Bee armor variant to make it more distinct.

Adjusted texture for one Honey Bee Leggings to make it more distinct from the other variant.

Honey Bee Leggings item texture will now show pollen if the item actually has pollen set in nbt.

##### Fluids:
(Fabric/Quilt): Fixed Honey Fluid and Royal Jelly Fluid not rendering the fluid texture on the far side when looking through the fluid.

(Forge): Fixed unable to swim in Sugar Water Fluid.

(Forge): Fixed Night Vision not applying to Sugar Water Fluid when inside it.

Sugar Water Fluid and vanilla Water both now are tinted more light blue when in Bumblezone rather than the yellowish color before.

##### Enchantments:
Fixed Comb Cutter bypassing Mining Fatigue effect like it didn't exist.

##### Entities:
Honey Slime is now immune to fall damage like Magma Cubes. The honey protects the Slime!

Fixed dimensions for baby Honey Slimes.

Fixed z-fighting on bottom of Honey Slime and ground.

Fixed Bee Queen able to be leashed when it shouldn't.

Beehemoth now starts with 40 health points and will get more health as friendship increases! Maxes out at 60 health points.

Tamed Beehemoths will show friendship amount above when owner is looking at it.

Added beehemothFriendlyFire config option, so you can disable accidentally hurting your flying friend! Set to true by default to allow owners to kill their Beehemoth by attacking.

Fixed bug where Beehemoths always fly straight up if below sealevel.

Fixed Leash rendering for Beehemoth.

Beehemoth will not lose friendship if it suffers in-wall suffocation damage

##### Structures:
Hanging Gardens now can spawn Torch Flower, Pitcher Plant, Pink Petal, Cherry Leaves, Cherry Logs, and Cherry Saplings!.

Cell Maze now will always have 1 Throne locating Honey Compass always in its center start room.
 The structure can also now have a Mystery locating Honey Compass

Honey Compasses to Mystery Structures now will try to point to different structures even if you got multiple compasses from the same structure's loot.

Lesser Mystery Honey Compasses are added to several structure's loot where these compasses point to smaller Bumblezone structures.

##### Biomes:
Added Bumblezone biomes to `minecraft:snow_golem_melts` biome tag to make Snow Golems melt in Bumblezone since beehives are roughly 95 degrees!

Added Bumblezone biomes to `minecraft:without_wandering_trader_spawns` biome tag to make extra sure Wandering Traders do not spawn in Bumblezone.

##### Dimension:
Bumblezone dimension is slightly darker now to make lighting pop better from light giving blocks.

Fixed Water, Lava, and Powder Snow fog effect not working when in Bumblezone dimension

Set Portal Cooldown when entering or exiting Bumblezone to prevent portal chaining issues.

##### Effects:
(Forge): Removed Milk as a curative item for Wrath of the Hive. Now many other mod's methods of removing the effect should no longer work.

Protection of the Hive will now always remove Wrath of the Hive effect if you managed to get both on together.

##### Advancements:
Improved performance of is_near_beehive advancement.

Will now display a message to user saying to check out Bumblezone's advancements for progression when they obtain the is_near_beehive advancement.

Removed Intimidation advancement in the Queen's Desire advancement line. Too silly and pointless.

Renamed VIP Trader advancement to Crazy Trader to reduce confusion.

Added "is_target_tag" field to the_bumblezone:killed_counter trigger for advancements. Now you can make "target_entity" point to a tag.
 As a result, some of the Queen's Desire's advancements now checks entity tags for what counts towards them. These are:
 `the_bumblezone:queens_desire/too_many_legs_spiders`
 `the_bumblezone:queens_desire/fighting_the_swarm_silverfish`
 `the_bumblezone:queens_desire/otherworldly_mites_endermite`
 `the_bumblezone:queens_desire/terror_fangs_cave_spider`

##### Mod Compat:
Added tag translations for Bumblezone item tags so that EMI mod can display them.

##### Config:
(Forge): Updated comment on warnPlayersOfWrongBlockUnderHive config option to be more clear on tag to edit.

