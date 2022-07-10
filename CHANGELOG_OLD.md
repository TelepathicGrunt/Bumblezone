### **(V.6.0.1 Changes) (1.19 Minecraft)**

##### Entities:
Fixed Honey Slime and Beehemoth not spawning naturally for regular chunk generation.

##### Dimension:
Improved worldgen time a bit.


### **(V.6.0.0 Changes) (1.19 Minecraft)**

##### Major:
Ported to 1.19!

##### Structures:
Biome mobs can spawn in Bumblezone's structures naturally now.

Cell Maze structure is now buried better.

Made boundary of Cell Maze tighter so that you only get Wrath of the Hive when it is obvious you are in a Cell maze structure.

Cell Maze structure is now much more spread out in the world.

##### Fluids:
Fixed bottom of honey fluid rendering when it shouldn't in some cases.


### **(V.5.1.3 Changes) (1.18.2 Minecraft)**

##### Items:
Bee Cannon fires bees slightly lower so they do not block the entire player vision briefly.

##### Misc:
Make the special bee spawning mechanic in Bumblezone Dimension ignore FakePlayers created by other mods.
Just a precaution check that might help performance if a mod is making a ton of FakePlayers in the dimension.


### **(V.5.1.2 Changes) (1.18.2 Minecraft)**

##### Misc:
Fixed server crash due to me accidentally having clientside code in the wrong place for Honey Compass.


### **(V.5.1.1 Changes) (1.18.2 Minecraft)**

##### Armor:
Quick fix to make Stingless Bee Helmet only put bee on head if the main currently used hand is empty. Not the offhand.


### **(V.5.1.0 Changes) (1.18.2 Minecraft)**

##### Items:
Honey Compass is now added! When right clicked on beehives/bee nests, it will keep track of the block's position!
When you right click the air in Bumblezone dimension, it seems to point to some sort of structure...
Crafted from 1 Compass, 1 Pollen Puff, 1 Honey Bucket, and 1 Honey Crystal Shard.
Can be sometimes found in Honey Cocoons in Bee Dungeons, Spider Infested Bee Dungeons, or Honey Cave Room structures.

Bee Cannon is added now! This cannon lets you store bees you right click into the item up to 3 bees.
If you hold right button and then release, you fire the bees! Any non-bee mob you are looking at will be attacked by the bees!
This can be crafted from Sugar Infused Stone (s), Sugar Infused Cobblestone (c), and Gunpowder (g) in this shape:
s s
sgs
csc

Making Honey Bucket and turning Honey Buckets into Honey Bottles now requires 4 bottles instead of 3.
Prevents duplication bugs with other mods that assumes bottles are 1:4 ratio to buckets.

Play the missing glass pickup sound when using Glass Bottles on Honey Fluid source blocks.

##### Armor:
Added Carpenter Bee Boots! These boots will automine many kinds of wood, beehive, honeycomb blocks that you are standing on when you hold crouch down!
The boots can be enchanted with the normal boot enchantments but can also be enchanted with Efficiency, Silk Touch, and Fortune.
To go with Efficiency, the boots also mines faster when you have the full bee armor set on or have Beenergized status effect. These speeds do stack.
The boots will also let you briefly hang on walls made of wood, beehive, or honeycomb blocks allowing you to wall jump or wall run!
Both of these behaviors are controlled by these two block tags that determine what to mine or what to wall hang on:
`the_bumblezone:carpenter_bee_boots_climbables` and `the_bumblezone:carpenter_bee_boots_mineables`

Stingless Bee Helmet is now buffed to reduce Poison status effect time slowly even when you do not have the full bee armor set.
If full armor is on, then it halves the Poison status effect time.

Stingless Bee Helmet now lets you put any entity that extends BeeEntity onto your head by right clicking the mob with an empty hand!
The bee will leave your head if you take damage, crouch, go underwater, has Wrath of the Hive effect, or 30 seconds passes.
If full bee armor is on, the 30 second timer is disabled!

Honey Bee Leggings is now buffed to reduce Slowness status effect time slowly even when you do not have the full bee armor set.
If full armor is on, then it halves the Slowness status effect time.
Also fixed the Slowness effect timer not showing the correct sped up time when the leggings is active.

Added stat entries for all bee armor to the Statistics screen you can find when you pause the game.

Fixed issue where Bee armor pants and chestplate may not show the right animation/models based on itemstack nbt state.
(My model cache wasn't correctly done)

##### Dimension:
Fixed the terrain for Bumblezone dimension so it looks much closer to the terrain of 1.17 and older Bumblezone.

##### Entities:
Added null world check to checking if bees should be angry at spawned entities.
Prevents crash with mods that create an entity with a null world. Don't ask why...

##### Fluids:
Fixed Honey Fluid deleting regular waterlogged blocks next to it.

##### Config:
Added enableExitTeleportation and enableEntranceTeleportation config options to let
players disable Bumblezone's teleportation methods into and out of the Bumblezone dimension.


### **(V.5.0.11 Changes) (1.18.2 Minecraft)**

##### Items:
Fixed all items consuming behaviors for all Bumblezone stuff.
(Example: Glass bottle on Filled Porous Honeycomb block, Honey Fluid, and Sugar water. And when feeding items to Beehemoth/Bees)

##### Structures:
Significantly cleaned up the outer looks of Cell Maze structure.


### **(V.5.0.10 Changes) (1.18.2 Minecraft)**

##### Controls:
Fixed potential keybind issues with Beehemoth flying upward control being bound to spacebar by default.
(It will remain spacebar but now should not prevent jumping from working in rare cases)
Also slightly reduced amount of packets sent when controlling Beehemoth.


### **(V.5.0.9 Changes) (1.18.2 Minecraft)**

##### Structures:
Cell maze structure is now significantly larger and can have multiple floors. They only spawn at sealevel now.

##### Sounds:
Fixed Bumblezone Music Discs not playing music.

##### Mod Compat:
Added recipes to make Bumblezone's enchantments be craftable with Token Enchanter mod's tokens.

##### Lang:
Fixed throwing Pollen Puff now having translated captions.


### **(V.5.0.8 Changes) (1.18.2 Minecraft)**

##### Mod Compat:
Added compat with Beekeeper Villager from Friends and Foes mod. Bumblezone items will appear in Beekeeper's trades. This can be turned off in Bumblezone's mod compat config.


### **(V.5.0.7 Changes) (1.18.2 Minecraft)**

##### Misc:
Fixed honey buckets/honey bottles not giving their empty bucket/bottle if they are a stack of 1 and are used on Porous Honeycomb Block or fed to bees.


### **(V.5.0.6 Changes) (1.18.2 Minecraft)**

##### Bees:
Bees have a small chance of wearing a rare Ukraine flag pajamas!

##### Misc:
Now requires Forge 40.0.20 or newer. Removed a mixin that is unneeded now.


### **(V.5.0.5 Changes) (1.18.2 Minecraft)**

##### Teleportation:
Fixed teleportation mode 2 and 3 so that they actually spawn you at the original position you were at in the non-Bumblezone dimension when you entered Bumblezone.

Fixed teleportation mode 2 and 3 not replacing blocks with air if it was going to spawn you inside a block when exiting Bumblezone.

##### Mod Compat:
Made Productive Bees's Sugarbag Honeycomb item be able to be fed to bees to heal and get Protection of the Hive.


### **(V.5.0.4 Changes) (1.18.2 Minecraft)**

##### Teleportation:
Fixed teleportation mode 1 and 3 to spawn to closest beenest properly if there's multiple nearby when exiting Bumblezone

##### Mod Compat:
Added support for Just Enough Effect Descriptions mod so Bumblezone's effects now have a description.


### **(V.5.0.3 Changes) (1.18.2 Minecraft)**

##### Subtitles:
Added correct subtitles for Honey Slime, drinking Sugar Water Bottle, throwing pollen Puff, and washing away Honey Residue/Webs.


### **(V.5.0.2 Changes) (1.18.2 Minecraft)**

##### Dimension:
Adjusted terrain to be closer to 1.18.1 Bumblezone terrain. Not perfect tho.


### **(V.5.0.1 Changes) (1.18.2 Minecraft)**

##### Mod Compat:
Turned back on Productive Bees compat!

##### Features:
Fixed bug where Bee Dungeons can be slightly below sealevel.


### **(V.5.0.0 Changes) (1.18.2 Minecraft)**

##### Major:
Ported to 1.18.2


### **(V.4.3.6 Changes) (1.18.1 Minecraft)**

##### Dimension:
Fixed Bumblezone Dimension not using the random world seed.
May produce chunk borders when loading into the dimension if created with past versions of Bumblezone.

##### Teleportation:
Fixed teleportation getting screwed up when riding mobs and exiting Bumblezone. It should now use the controlling entity for destination instead of vehicle's data.
Hopefully there's no more teleportation bugs...

##### Misc:
Fixed Bumblezone crash when other mods create a null entity somehow for whatever cursed reason.


### **(V.4.3.5 Changes) (1.18.1 Minecraft)**

##### Entities:
When riding tamed Beehemoths, holding Caps Lock button will make the mob fly downward while Space will continue to move the mob up upward.
The keys for moving up or down can be changed in the Options -> Controls -> Key Binds menu.

Fixed issue where if multiple Beehemoths are around, the Beehemoths on the ground may have legs rendered in incorrect position.

##### Blocks:
Honey Cocoon now will lose items much faster when waterlogged with water above as well.

When Honey Cocoon consumes specific honey items to revive Empty Honeycomb Blocks inside, the ejected container of the consumed item will be dropped and should be the correct item this time.
(Example: glass bottle for consumed potions if you added potions to the bee_feeding_items item tag)

Honey Cocoon's inventory screen is now orange.

##### Recipes:
Improved the the_bumblezone:container_shapeless_recipe_bz recipe type so that it converts certain vanilla items into their containers properly.
Example: Powder Snow Bucket does not have a container set so I have to hardcode that it should be turned to an empty bucket if used with this recipe type.
This doesn't change anything in base Bumblezone. Just may help datapackers make recipes with Bumblezone work properly and have correct containers left behind after crafting.

##### Items:
Fixed captions when flying when Bumble Bee Chestplate.


### **(V.4.3.4 Changes) (1.18.1 Minecraft)**

##### Blocks:
Fixed Honey Cocoon not allowing any items to be placed inside correctly.

##### Entities:
Fixed tamed Beheemoths always following owner regardless of if owner is holding Honey Bucket or not.


### **(V.4.3.3 Changes) (1.18.1 Minecraft)**

##### Lang:
Added ja_jp.json lang file donated by a fan! Thank you all!

##### Blocks:
When washing away Honey Webs/Residues with water related items (buckets, bottles, wet sponge), the phantom swoop sound is now replaced with boat paddle water sound.

Honey Cocoons now keep their names when placed and picked up again.

Honey Cocoons now has a tooltip that shows what items they have inside when hovering over them in inventory.

Breaking or mining a Honey Cocoon that has a loot table before loot is generated will now generate the loot.

Honey Cocoons will now be dropped when broken in creative mode.

##### Items:
Added JEI descriptions for the two Bumblezone music discs: Honey Bee Rat Faced Boy disc and Flight of the Bumblebee Rimsky Korsakov disc.

##### Enchantments:
Fixed Potent Poison not applying to victim if they are hit by a thrown Trident with the enchantment while in survival.

##### Entities:
Tamed Beehemoth will now only follow their owners with Honey Buckets and no one else.
They will also fly fast to their owners holding Honey Buckets up to 200 blocks away even without a line of sight!


### **(V.4.3.2 Changes) (1.18.1 Minecraft)**

##### Blocks:
Honey Cocoon block now cannot be placed into Shulker Boxes.

Honey Cocoon block only allows other blocks to take or add items through the top of the Honey Cocoon block.

Honey Cocoon's item form now only stacks if it is empty. If it is holding items inside, it cannot stack like Shulker Boxes.

Shulker Boxes cannot be put into Honey Cocoon now and Honey Cocoon cannot be put into Shulker Boxes too.

##### Entities:
Improved animation so Beehemoth is still when on ground.

##### Lang:
Fixed lang entry for Thrown Stinger Spear entity.


### **(V.4.3.1 Changes) (1.18.1 Minecraft)**

##### Items:
Fixed Sugar Water Bottle being able to cure poison effect. (Was accidentally using Honey Bottle class for it... oops)

Fixed bucket/bottle duplication when crafting Sugar Water Bucket or converting Honey Bucket to Honey Bottles and vice versa.


### **(V.4.3.0 Changes) (1.18.1 Minecraft)**

##### Dimension:
The coordinate scale has been shrunk from 10 to 4.
Now exiting Bumblezone at 100, -3, 1000 will put you around 400, ?, 4000 in the destination dimension.

Teleportation mode 2 (selectable in config) has been fixed so that it properly places players in their destination without coordinate scaling done.

Tighten up and improved the Enderpearl collision checking code so it detects that it hit bee hive/bee nests better.
Before, due to speed of the pearl, it could think it hit the air before the actual hive block.
Now velocity is taken into account to get true hit position.

##### Blocks:
Added Honey Cocoon which is a new storage block! Found in Cell Maze structure, Honeycomb Cave structure, and Bee Dungeons.
They turn Empty Honeycomb Brood Blocks into filled Brood Blocks if they are inside this cocoon with a bee feeding item like Honey Bottles over time!
If waterlogged with water above, they will slowly drop items above over time.
When broken, they drop themselves and all items unless you use Silk Touch, then they keep their items inside like Shulker Boxes do.

Fixed some honey filled blocks not consuming Glass Bottles when collecting Honey Bottles from the blocks.

Tall stacks of Pile of Pollen blocks now will hide entities inside them by giving them the new Hidden effect.
Monsters will now have to be closer to see the player buried in Pile of Pollen.
However, once seen, the monsters will not be fooled if you dive back into the Pile of Pollen.
Effective for hiding from swarms of angry bees.

Fixed Pile of Pollen sometimes not removing the FallingBlockEntity instance of itself from the client.

Sticky Honey Residue/Redstone's collision shape is now cached so it has better performance.

Sticky Honey Residue/Redstone no longer extend VineBlock now.

Sticky Honey Residue/Redstone now has particle effects (dripping honey particle and the redstone version has redstone particles when powered)

Fixed Sugar Water Block not being able to waterlog some of Bumblezone's blocks like Honey Crystals.

Fixed Honey Sticky Webs and Honey Sticky Residue attaching an empty tag to held items when right clicked which was preventing that item from stacking.

##### Fluids:
Fixed crash if Bumblezone's Honey Fluid touches Productive Bee's Honey Fluid.

##### Items:
Stinger Spear item is added! They are rare throwable weapons found in Cell Maze structure.
Can be repaired by Flint and inflicts short weak poison on any non-undead mob it hits.
Has 1 dedicated enchantment for it called Neurotoxins. See Enchantment section for more info.

Stingless Bee Helmet is added!
When couching, all Bees and Beehemoths will be glowing for you.
Nausea effect duration will also decrease twice as fast when this armor is on.
Can be repaired by Honeycomb, Leather, Wool, or Rabbit Hide.
While having all 3 bee armor on, the bee outlining range will be increased and will remain glowing for a brief time after standing up.
Furthermore, with the 3 armor on, Nausea effect duration will decrease much faster and now Poison effect duration will decrease twice as fast.

Bumble Bee Chestplate is added!
This clothing lets you be able to fly for a short period of time by holding the jump button anytime after jumping!
Getting Beenergized effect will improve flight! Can be repaired by Honeycomb, Leather, Wool, or Rabbit Hide.
Flight time will be further boosted if all 3 bee armor is on!

Honey Bee Leggings is added!
It resists slowness from many Bumblezone blocks that cause slowness and can collect pollen from flowers or Pile of Pollens!
Crouch to spawn a Pollen Puff when full of pollen.
Can be repaired by Honeycomb, Leather, Wool, or Rabbit Hide.
If all three bee armor is on, chance of collecting pollen from flowers is improved and Slowness effect duration will decrease twice as fast now!

Honey Crystal Shields now have an internal "ShieldLevel" nbt to keep track of its strength.
The shield level is increased by 1 each time the shield has more than 1/5th of its durability repaired.
The maximum shield level now is 10 and shields now start with more durability initially.
Legacy Honey Crystal Shields obtained before this update should automatically be converted to the new shield level system based on their RepairCost.

Honey Crystal Shield has a chance of being disabled by axes instead of always being disabled by axes. Matches vanilla shield behavior.

Bee Bread can now always be eaten even when hunger bar is full.

##### Enchantments:
Neurotoxins enchantment added that only applies to Stinger Spear item. (Max enchantment level is 2)
Will cause the weapon to have a chance of causing Paralysis Effect on non-undead mobs for 5 seconds per enchantment level.
Has a lower chance of paralyzing if the mob has more health remaining with 10% chance at its lowest.
Every hit that does not cause paralysis will increase the chance of the next hit causing paralysis.

Potent Poison enchantment is available for all trident-like items including Stinger Spear. (Max enchantment level is 3)
When the weapon hits a non-undead mob, it inflicts poison effect on them with higher enchantment levels increasing
the duration and level 3 increases the poison level. If on Stinger Spear, the level of poison is increased by an extra level.

Fixed Comb Cutter showing up for invalid items in the Enchantment Table.

##### Entities:
Fixed Honey Slime bounding boxes.

Fixed Honey Slime not following creative mode players with Sugar in hand.

Fixed Beehemoth not being Arthropod mob type in backend.
Bane of Arthropod enchantment now should inflect more damage on Beehemoth.

##### Structures:
Added Cell Maze structure to all Bumblezone biomes! It will inflict Wrath of the Hive on you if you enter it without Protection of the Hive effect.
Enjoy the fun loot and exploration of this small maze-like structure!

Pollinated Stream structure is now more common in Pollinated Fields and Pollinated Pillar biomes.
Land will be made around this structure now to help prevent it from floating in midair.

##### Bee Aggression:
Fixed bees not getting automatically angry at bugs and bears in the Bumblezone dimension.

Fixed up the checks for what mobs are bears or non-bee arthropods so that they get Wrath of the Hive in Bumblezone dimension.
The new checks are much safer and shouldn't break other mods anymore.
It may also be better and not have as many false positives/negatives.

Bees fed and calmed while having Wrath of the Hive will now apply Protection of the Hive right away.

##### Effects:
Protection of the Hive will no longer be removed when taking honey from blocks picking up/mining various honey blocks.
HOWEVER, hitting a bee or mining/taking honey from Honeycomb Brood Blocks will still remove Protection of the Hive.

Hidden effect is added which is given to entities hiding in Pile of pollen.
It reduces range of sight fo hostiles to see the hidden mob.

Paralysis effect is added and is caused by Neurotoxins enchantment right now. This sets the mob's immobile method
to return true which causes many mobs to stand still. They will also shake when paralyzed and give off yellow particle effects.
Some mobs may be able to still do some actions such as Witches drinking potions.

##### Teleportation:
Fixed Piston teleportation no longer working when `REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT` block tag is non-empty.

##### Advancements:
Added more Advancements and moved some around to different branches.

##### Configs:
Added beehemothTriggersWrath config option. It is set to false by default.
If turned on, any mobs that hurts a Beehemoth and is not the owner of the Beehemoth, that mob will get Wrath of the Hive effect.

Added playWrathOfHiveEffectMusic config option to allow players to turn off the music that plays when you have Wrath of the Hive effect.

Default teleportation mode in config is changed from 1 to 3.


### **(V.4.2.1 Changes) (1.18.1 Minecraft)**

##### Teleportation:
Fixed beehives/beenest so that any Y value can still get players into The Bumblezone.


### **(V.4.2.0 Changes) (1.18.1 Minecraft)**

##### Blocks:
Added Honey Web and Sticky Honey Web blocks! They are crafted from 3 Sticky Honey Reside or 3 Sticky Honey Redstone in a row.
They act like the residue forms but instead, can be placed to create a wall of stickiness without being attached to a block surface.

Sticky Honey Residue and Sticky Honey Redstone now give a temporary slowness status effect.

Sticky Honey Residue and Sticky Honey Redstone collision checking is optimized a bit better.

Fixed issue where Sticky Honey Residue could spawn in midair during worldgen.

##### Items:
Fixed Pollen Puff able to replace non-replaceable non-solid blocks when thrown inside the space for that block.

Buffed the slowness that Honey Crystal Shield gives physical attackers.

##### Fluids:
Adjusted Honey Fluid texture slightly.

##### Entities:
Beehemoth now has a buzzing sound when flying!

##### Biomes:
Walls of Honey Web now spawns in the caves of Bumblezone!

##### Misc:
Fixed a client-sided mixin trying to load on servers.

##### Dimension:
Lowered volume of ambient buzzing sound in Bumblezone dimension.


### **(V.4.1.1 Changes) (1.18.1 Minecraft)**

##### Misc:
Tried fixing an issue where other mod's fake players or something is crashing Bumblezone. I think? It's cursed and hard to debug.


### **(V.4.1.0 Changes) (1.18.1 Minecraft)**

##### Major:
Now hard depends on Feature NBT Deadlock Be Gone mod to prevent deadlocks with Bumblezone's worldgen caused by vanilla bug.

##### Entities:
Added specialBeeSpawning and nearbyBeesPerPlayerInBz config options which makes Bumblezone handle
spawning and despawning vanilla bees in its dimension entirely. This config makes this mod try to
always have a set number of vanilla bees near the player as often as possible to make the dimension feel full.
Vanilla bees that are too far from player will be forcefully despawned unless the bee is name tagged, persistent, or has a hive associated with it.

Fixed Honey Slime not being honey filled for 1 frame after being spawned.

##### Fluids:
Fixed weird interactions with modded fluids bordering Bumblezone's fluids.
Such as Honey Fluid turning any water-tagged modded fluid into Sugar Water regardless of that that other fluid is.
Solution was more tags.
the_bumblezone:fluids/convertible_to_sugar_water
forge:fluids/visual/honey
forge:fluids/visual/water

##### Structures:
Fixed Honey Cave Room and Pollinated Streams not spawning.

##### Features:
Bee Dungeon and Spider Infested Bee Dungeon spawns again now.

Sticky Honey Residue now spawns in patches in The Bumblezone dimension.

##### Mod Compat:
Mod compat with Productive Bees is now back and better balanced now! More config options are available.

Mod compat with Pokecube is back again!


### **(V.4.0.1 Changes) (1.18.1 Minecraft)**

##### Blocks:
Undid a mixin for Honey Fluid rendering to use an old and fragile mixin instead. Reason is modifyArgs mixin is broken in
Forge and when I tried to use modifyArg with storing values I need in a field, it broke rendering Honey Fluid as it is not threadsafe.

Fixed Honey Fluid bottom not rendering when it is slowly falling onto an opaque block.

Fixed Honey Fluid falling faster if there is neighboring Honey Fluid that is also falling.

##### Items:
Fixed Pollen Puff not growing a Pile of Pollen if thrown through a tall stack.

##### Mod Compat:
Fixed crash with some mods asking for getBaseHeight from Bumblezone's chunk generator.


### **(V.4.0.0 Changes) (1.18.1 Minecraft)**

##### Major:
Ported to 1.18.1!

##### Biomes:
Now has ambient pollen particles.

##### Items:
Fixed some bugs with Pollen Puff and Pile of Pollen.


### **(V.3.2.1 Changes) (1.16.5 Minecraft)**

##### Mod Compat:
Added compat with Inspirations's honey buckets so it can be fed to bees and stuff.
 Inspirations mod: https://www.curseforge.com/minecraft/mc-mods/inspirations

Added allowHoneyFluidTanksFeedingCompat config option that works alongside the bee_feeding item tag.
 Any item with a Forge fluid capability attached and has a fluid tagged as forge:fluids/honey inside, will now be able
 to be fed to bees and have all the same behavior as bee_feeding item tag. This works for items that are not in that tag too.
 The config is in the mod compat config file and is set to true by default.


### **(V.3.2.0 Changes) (1.16.5 Minecraft)**

##### Items:
Added Bee Bread which can be crafted from pollen puff and honey bottles (or 3 pollen puff + honey bucket)
 When fed to bees or Beehemoth, it will grant Beenergized effect and make them go faster for 5 minutes! 
 Feed them multiple times to increase the flying speed boost up to level 3 where bees goes zoom!
 When player eats the item, it acts as a better food source than honey bottle but does not cure player of poison.
 Player will get Beenergized effect as well but only to level 1 and nausea for a few seconds.

##### Blocks:
Honeycomb Brood Blocks has a chance of dropping Bee Bread when broken.

##### Entities:
Beehemoth is now immune to block suffocation damage for a bit after teleporting to and from the bumblezone.
 (Uses portal cooldown to know when to be invincible to block suffocation)

Beehemoth now reads both Flying Speed attribute and flyingSpeed field when riding the mob or it is randomly flying around. 
 (changes to flyingSpeed field overrides Flying Speed attribute changes)

Beehemoth will no longer become untamed when friendship reached 0 or below. Instead, it'll retain its owner but still 
 prevent any riding until it is feed to bring its friendship above 0.

##### Effects:
Beenergized effect is now added! This will increase the flying speed of any mob it is affecting.
 If on player, it does not have any effect it seems.


### **(V.3.1.3 Changes) (1.16.5 Minecraft)**

##### Teleportation:
Sped up an edge case with teleporting.

Took an extra precaution against riding entities when exiting bumblezone could cause issues/


### **(V.3.1.2 Changes) (1.16.5 Minecraft)**

##### Teleportation:
Fixed bug where setting Teleportation Mode to 2 or 3 and trying to exit Bumblezone on an entity that originated from Bumblezone would spawn player under Bedrock in Overworld.


### **(V.3.1.1 Changes) (1.16.5 Minecraft)**

##### Entities:
Beehemoth will now stay still when crouch right-clicked like how you make dogs and cats sit. 
 If it is sitting, shift right click with empty hand to remove saddle. Shift right click while holding any item to make it unsit (or ride it to unsit)
 To feed Beehemoth, just right click while holding any honey feeding item. No crouching needed.

Right clicking Beehemoth without crouching will always make you ride the mob. No longer need an empty hand.

##### Lang:
Added english translations for advancements to the other language files so the translation keys don't show.


### **(V.3.1.0 Changes) (1.16.5 Minecraft)**

##### Advancements:
Added new advancements for this mod to add some progression and high xp rewards! 
 Note: Progress or conditions met before this update will not contribute towards the new advancements.

##### Configs:
Significantly cleaned up the config system backend code for my mod. Let me know if any config no longer works but it should be ok!

##### Entities:
Fixed Honey Buckets not able to convert Slime into Honey Slime. (Was an issue with the tag that controls what blocks can convert Slime)

Fixed Honey Slime scaling for adult to not be massive.

Fixed Honey Slime not giving Honey Bottle when harvested with Glass Bottle.

Honey Slimes have a bit more health, a bit more attack power, and will jump faster and higher when angry as well as stay angry longer.

Killing adult Honey Slimes with looting enchantment may drop Sugar and/or Slime Balls. 
 Before, it was the child that had this feature instead of the adult by mistake.

##### Effects:
Protection of the Hive on the player will now be removed if you hurt a Bee outside The Bumblezone dimension.

##### Recipes:
Renamed the_bumblezone:beeswax_planks_to_bee_nests recipe json to the_bumblezone:beehive_beeswax_to_bee_nest.

##### Mod Compat
Added code to skip compat with v1.16.5-7.1.1 Caves and Cliffs Backport mod because that version crashes Bumblezone. 
 Please use and older or newer version of that mod to enable compat with their candles again.
 
 
 ### **(V.3.0.0 Changes) (1.16.5 Minecraft)**

##### Configs:
NOTE: all configs have been renamed and moved from the world's config folder to a 'the_bumblezone' folder in the main config folder above the mods folder.
This is to help prevent confusion and make it easier for beginners to find the config files.
If you have edited the config files in the world save's config folder, you'll have to move your changes over to the new config file location.
Sorry about that but it is for the best for ease of finding and organization instead of having configs scattered all over.

##### Teleportation:
Redid a bunch of teleportation code so now riding any mob or vehicle will still allow teleportation out of the Bumblezone.
And you can teleport into the Bumblezone while riding a vehicle if you use the piston pushing into beehive method.

##### Entities:
Shy's transbee texture and other LGBT+ textures are now applied to 2% of vanilla bees!
Her resourcepack can be downloaded separate here if you want transbees without Bumblezone on: https://www.curseforge.com/minecraft/texture-packs/shy-trans-bee

##### Lang:
Updated ru_ru.json translations. Thank you Bytegm!


### **(V.2.6.5 Changes) (1.16.5 Minecraft)**

##### Mod Compat:
Productive Bee's Honey Treat now should work properly with their gene stuff while also giving The Bumblezone's Protection of the Hive.


### **(V.2.6.4 Changes) (1.16.5 Minecraft)**

##### Entities:
Beehemoth speed can not be changed by config in the world's serverconfig folder in the_bumblezone-general.toml
Base speed for Beehemoth was slightly buffed.

Holding down Space (jump button) while riding Beehemoth will make the bee fly upward.

Fixed bug where you could feed Beehemoth to surpass the 1000 point limit on friendship.
Capped at 1000 not and cannot be lowered below -100 too.


### **(V.2.6.3 Changes) (1.16.5 Minecraft)**

##### Lang:
Updated ru_ru.json translations. Thank you Tkhakiro!

##### Entities:
Beehemoth now follows players when they hold any item that is in forge:items/buckets/honey tag.

##### Mod Compat:
Added Cyclic's honey bucket to forge:items/buckets/honey tag within Bumblezone.

Also, be sure to update Productive Bees to their latest version so that their honey bucket is also placed into forge:items/buckets/honey tag.


### **(V.2.6.2 Changes) (1.16.5 Minecraft)**

##### Blocks:
Fixed Beehive Beeswax block not dropping anything. Sorry about that. I forgot to rename loot table when i renamed the block.


### **(V.2.6.1 Changes) (1.16.5 Minecraft)**

##### Entities:
Fixed Beehemoth not spawning naturally in Bumblezone biomes outside Honey Cave Room structures.

##### Structures:
Increased spawnrate of HoneyCave Room structure.


### **(V.2.6.0 Changes) (1.16.5 Minecraft)**

##### Entities:
Added Beehemoth, a giant ridable bee, from CarrierBees mod. Special thanks to the developers for allowing the new mob to have a home in The Bumblezone!
The devs who made Beehemoth originally are: Aranaira, Alexthe666, and Nooby!
Beehemoth can be tamed with bee feeding items such as Honey Buckets or Honey Bottles and other honey stuff.
However, they will only follow you if you hold Honey Buckets.
If you feed them, you can eventually tame them, put on a saddle, and ride them!
The more you feed and ride them, the more friendship they get which makes them move faster.
When friendship is finally maxed out, Beehemoth becomes a Queen Beehemoth and can fly at maximum speed!
However, friendship will decrease if Beehemoth takes damage from any source and if they lose all friendship, they will become untamed and unhappy. :(
They spawn in the Bumblezone rarely but Honey Cave Rooms have 2 always.

##### Items:
Texture for Honey Slime Spawn Egg is changed to be easier to identify. Especially for colorblind folks.

##### Worldgen:
Fixed some cases where candles in Bee Dungeons and Spider Infested Bee Dungeons can be floating.

##### Mod Compat:
Caves and Cliffs Backport's candles can now be found in Bee Dungeons and Spider Infested Bee Dungeons.


### **(V.2.5.2 Changes) (1.16.5 Minecraft)**

##### Lang:
Special thanks to Tkhakiro for helping to update the ru_ru.json translations!

Special thanks to mc-kaishixiaxue for helping to update the zh_cn.json translations!

##### Items:
Fixed several usages where I was not giving the right item to the player's inventory such as using a water bucket on
a Honey Crystal was not giving players an empty bucket afterwards. Now it will.

##### Blocks:
Fixed Honey Crystal block to properly implement IWaterLoggable so it interacts with other mods better for fluid adding/extracting.
Honey Crystal block can no longer be filled with fluid while in the Nether now and when placing the block in creative mode,
the block can only be waterlogged if placed in a water-tagged fluid source block. Otherwise, you need water-tagged buckets to
waterlog the block by hand or dispenser or by other mod's machines and stuff.


### **(V.2.5.1 Changes) (1.16.5 Minecraft)**

##### Fluids:
Restrict values on properties for Honey Fluid to try and prevent a crash if a mod or the game tries to get a level 9 fluid when Honey Fluid only goes up to 8.


### **(V.2.5.0 Changes) (1.16.5 Minecraft)**

##### Developers:
Mod jar name was changed so to download bumblezone from the nexus.resourcefulbees maven, you would use this new format instead:
implementation fg.deobf("com.telepathicgrunt:Bumblezone:<modversion>+1.16.5")

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


### **(V.2.4.11 Changes) (1.16.5 Minecraft)**

##### Sounds:
flight_of_the_bumblebee_rimsky_korsakov and honey_bee_rat_faced_boy music is now mono so that the Music Discs work properly in Jukeboxes.
  Special thanks to a friend who pointed out this issue and helped convert and compress the sound files!

##### Effects:
Turned on optimization to make Wrath of the Hive grow nearby Honeycomb Brood Blocks with less performance impact.

##### Mod Compat:
Added Better Beekeeping's honey bottles into Bumblezone's bee feeding items tag. 


### **(V.2.4.10 Changes) (1.16.5 Minecraft)**

##### Mod Compat:
* Fixed crash when trying to use the blacklist to stop spawning some Resourceful Bees in Bumblezone


### **(V.2.4.9 Changes) (1.16.5 Minecraft)**

##### Enchantments:
* Fixed Comb Cutter enchantment so it appears in enchantment table only for Swords and Books and only if the cost of the enchantment is 13 or less.
  This help solve the problem of some mods making curses able to show up in enchanting table which caused Comb Cutter to be applied to any tool.


### **(V.2.4.8 Changes) (1.16.5 Minecraft)**

##### Teleportation:
* Exiting and entering Bumblezone will show a message to just the teleporting player that they are being teleported
  
* Any living entity including players and mobs can enter the Bumblezone by being pushed into a Bee Hive or Bee Nest block by an activated Piston

* Any living entity can now exit the Bumblezone
  
* Fixed player not teleporting to closest bee hive block if the block is at sea level

##### Lang:
* Added Spanish translation donated by another person

##### Misc:
* Compressed one of the sound files by a large amount


### **(V.2.4.7 Changes) (1.16.5 Minecraft)**

##### Misc:
* Now I hard depend on v36.1.3 or newer Forge explicitly due to me using a newer Forge event.
  This should help prevent crashes due to people using a too old Forge version.


### **(V.2.4.6 Changes) (1.16.5 Minecraft)**

##### Mod Compat:
* Added config option to turn off trade compat with Charm, Buzzier Bees, and Reosurceful Bees villager trades.


### **(V.2.4.5 Changes) (1.16.5 Minecraft)**

##### Blocks:
* Fixed Sticky Honey Residue and Sticky Honey Redstone having the wrong hitbox when placed on the sides. 

##### Teleportation:
* Increased search radius for beehives when leaving Bumblezone.

* Fixed bug where client side visual makes it look like the player continues falling below y = 3 even though they remained at y = 3 on serverside. (Client was in no danger tho)


### **(V.2.4.4 Changes) (1.16.5 Minecraft)**

##### Blocks:
* Beeswax Planks texture is adjusted to look better when next to other Beeswax Planks.

* Attached a POI system to Brood Blocks placed and generated from here on out. Will be a stepping stone to optimizing Wrath of the Hive in 1.17.

##### Entities:
* Cleaned up Bee Feeding code a ton and added the_bumblezone:bee_feed_items item tag for players to add new items that can be fed to bees to heal them.

* Added the_bumblezone:wrath_activating_items_when_picked_up item tag so players can add/remove items that should anger bees when picked up.

* Added the_bumblezone:wrath_activating_blocks_when_mined block tag so players can specify what blocks, when mined, will make bees angry.
  Honey Blocks are now in this tag so they only anger bees when mined. Not when picked up now.

##### Mod Compat:
* Honey Apple and Honey bread from Buzzier Bees cna be fed to bees now.

* Fixed crash with my mod trying to spawn Carrier Bees's Bucket Entity from the Honeycomb Brood blocks.

##### Teleportation:
* Switched to the new Ender Pearl Teleport event from the old one as the old event is being removed by Forge in 1.17.

* Teleporting out of The Bumblezone is now much better optimized and has a bigger search radius for existing bee hive blocks.


### **(V.2.4.3 Changes) (1.16.5 Minecraft)**

##### Mixins:
* Cleaned up many mixins to prevent possible mixin conflicts with other mods.


### **(V.2.4.2 Changes) (1.16.5 Minecraft)**
  
##### Teleportation:
* Fixed issue where Enderpearl impacting beehives may not always work due to the impact coordinates being heavily offset from the actual impact.


### **(V.2.4.1 Changes) (1.16.5 Minecraft)**

##### Mod compat:
* Fixed Comb Cutter crashing when Resourceful Bees is not on.


### **(V.2.4.0 Changes) (1.16.5 Minecraft)**

##### Major:
* Now requires 36.0.42 Forge or newer as I am now using EntityAttributeCreationEvent to register entity attributes.

##### Dimension:
* The loud Bee buzzing sound was replaced with a much softer beehive buzzing! Easier on the ears!

* Music will play when you are in a Bumblezone Biome! The song that plays is Honey Bee by Rat Faced Boy.

##### Effects:
* Wrath of the Hive now plays music when triggered! The song that plays is a midi version of Flight of the Bumblebee by Rimsky Korsakov.

* Wrath of the Hive's default config value for duration is now 1680 and angry bee's strength config is lowered by one.

* When you have Wrath of the Hive, bees will now spawn in open space a bit away from you but will come chase you down! Grab your Bane of Arthropod sword!

* Bees now cannot get Wrath of the Hive effect to prevent them attacking each other.

* Drinking Honey Bottles no longer trigger Wrath of the Hive effect in Bumblezone's dimension.

* Mobs with Wrath of the Hive will now make Honeycomb Brood Blocks near them grow faster. Stay out of Bee Dungeons when you have the effect!
  
* Protection of the Hive's default config value for duration is now 1680.

Enchantments:
* Added Comb Cutter enchantment for Shears, Swords, and Resourceful Bees's Scrappers. 
  This enchantment will make mining all blocks with "comb" in the name much faster!
  And it will slightly increase mining speed for Hive, Nests, and Wax based blocks too.
  It will also increase the number of combs you get when shearing a BeeHive or BeeNest!

##### Entities:
* Using Honey Blocks on vanilla Slime mobs that are size 1 or 2 will turn them into Honey Slime mobs!
##### "the_bumblezone:turn_slime_to_honey_slime" item tag controls what item can do the conversion.

* Honey Slime now gets significant reduced fall damage when they are covered in honey!
  
##### Blocks:
* Fixed Honey Crystal block so that it rotates and mirrors properly when loaded from nbt files.

* Adjusted Empty Porous Honeycomb, empty Brood block, and non-empty Brood blocks's textures to try and reduce tiling issues a bit.

##### Items:
* Honey Crystal Shield's valid repair items is now controlled by the item tag: "the_bumblezone:honey_crystal_shield_repair_items"

* Added new music discs to play the two new music added to this mod! You can obtain the discs from Wandering Traders as a rare trade!
  
##### Translations:
* Removed English that was accidentally left in the Russian translations - by Alepod

##### Features:
* Optimized waterfall feature to use honeycombs_that_features_can_carve block tag.

* Bee Dungeon and Spider Infested Bee Dungeon code backend is significantly cleaned up and now uses processors to change blocks.

##### Mod Compat:
* Pokecube's Weedle, Combee, and Cutiefly evolution lines now spawn in Bumblezone! 

* If Pokecube is on, feeding Honey Brood Blocks will now have a chance of spawning a lvl 1 Weedle, Combee, or Cutiefly.

* Blacklisting any Bumblezone Biomes with Resourceful Bees's biome blacklist will prevent the bee from spawning at all in Bumblezone's Dimension.

* Resourceful Bees's Honey Blocks has a chance of showing up in Bee Dungeons or the holes in the walls of the dimension!
  Their Honey Blocks will now give Wrath of the Hive to players picking them up when inside The Bumblezone dimension.
  
* Reduced the spawnrates of many Resourceful Bees and Productive Bees honeycombs in The Bumblezone dimension and gave them new ranges that they can spawn at.

* Added Charm support so now their candles spawn in Bee Dungeons and Spider Infested Bee Dungeons!
  
* Several Bumblezone Items are now used in Charm's, ReosurcefulBees's, and Buzzier Bee's BeeKeeper/Apiarist trade offers!

* Carrier Bees's bees now cannot get Wrath of the Hive effect and gets properly angered too now when a mob gets the effect.


 (V.2.2.14 Changes) (1.16.5 Minecraft)
     
##### Mod Compat:
* Fixed Spider Dungeon crash if ResourcefulBees is on and not Productive Bees.

##### Teleportation:
* Throwing Enderpearl at Beehives will teleport you to dimension more consistently.
   
   
### **(V.2.2.13 Changes) (1.16.5 Minecraft)**
      
##### Blocks:
* Renamed the registry name of Brood Blocks from honeycomb_larva_block to honeycomb_brood_block.

* Renamed the registry name of Empty Brood Blocks from dead_honeycomb_larva_block to empty_honeycomb_brood_block. (fixes some having missing texture too if you used 2.2.12)
     
     
### **(V.2.2.12 Changes) (1.16.5 Minecraft)**
      
##### Translations:
* Special thanks to Alepod for the Russian translations!

##### Blocks:
* Honeycomb Brood Blocks now spawn child Bees and child Honey Slime mobs.

##### Items:
* Fixed Honey Slime Spawn Eggs not working with Dispensers and now use better code practices. 
    
##### Mod Compat:
* Special thanks to Vaerys-Dawn for improving Resourceful Bees compat in Bumblezone!
  Bees will now only spawn in Bumblezone if canSpawnInWorld is set to true in Resourceful Bees and 
  useSpawnInWorldConfigFromRB config in Bumblezone is set to true. Also added a new entity type tag 
  called blacklisted_resourceful_bees_entities.json that you can use to blacklist specific bees as well.
  
* Only Resourceful Bees Honeycombs of bees spawnable in Bumblezone will generate in the dimension now.

* Added PBBlacklistedBees config entry to allow users to blacklist what Productive Bees spawns in Bumblezone.

* Fixed spawnProductiveBeesHoneycombVariants config not preventing all Productive Bees combs from spawning in Bumblezone.

* Fixed Spider Dungeon grabbing Resourceful Bees comb instead of Productive Bees's in certain cases. 

* Fixed spawnResourcefulBeesHoneycombVariants config not preventing all Resourceful Bees combs from spawning in Bumblezone.
  
    
### **(V.2.2.11 Changes) (1.16.5 Minecraft)**
     
##### Dimension:
* Changed Enderpearl impact mixin to be Forge's EnderTeleportEvent instead to have better mod compat with mods replacing Enderpearls.
  However, due to a limitation of Forge's event, any modded Enderpearl (even those that doesn't replace vanilla's) will now teleport you to Bumblezone if it hits a beehive.

    
### **(V.2.2.10 Changes) (1.16.4 Minecraft)**
     
##### Dimension:
-Improved Bumblezone Bee AI to not stop in midair as often.
     
##### Config:
-Changed broodBlocksBeeSpawnCapacity so that it is not multiplied by 10 anymore for bee limit. 
 If it is set to 50 now, then the Brood blocks will not spawn more bees if 50 bees already exist.

  
### **(V.2.2.9 Changes) (1.16.4 Minecraft)**
     
##### Mod Compat:
-Productive Bees's Honey Treat item now can be fed to bees or Honeycomb Brood Blocks.

-Buzzier Bees compatibility is restored! Honey Wands can feed bees and Honeycomb Brood Blocks, take honey from blocks, 
 Crystallized Honey spawns in the dimension, Bottled Bees can restore Empty Honeycon Brood Blocks, and Candles spawn in Bee Dungeons!

##### Blocks:
-Added broodBlocksBeeSpawnCapacity config option to allow users to change the automatic bee spawning mechanics of Honeycomb Brood Blocks.

##### Dimension:
-Falling out of Bumblezone dimension to teleport out shouldn't deal fall damage now.

-Added onlyOverworldHivesTeleports config option to allow people to make it only possible to enter The Bumblezone from the Overworld.

-Vanilla Bees in The Bumblezone now have a new AI that makes them wander better, lag less, and not cluster on the ceiling anymore. (for Forge version only)

-Bees spawned from new chunks are less likely to be pollenated.

##### Teleportation:
-Fixed Teleportation mode 2 and 3 being broken and not saving previous pos and dims. 

##### Items:
-Fixed Honey Shield registering its Dispenser behavior twice.

##### Mixins:
-Prefixed all my accessor and invoker mixins due to this bug in mixins that could cause a crash with other mods for same named mixins.
 https://github.com/SpongePowered/Mixin/issues/430
 
##### Backend:
-Quite a bit of general cleaning up and switched from Yarn over MCP mappings to full MCP mappings.
  
  
### **(V.2.2.8 Changes) (1.16.4 Minecraft)**
   
##### Misc:
-Fixed various serverside crashes.
 
##### Dimension:
-Fixed Sugar Waterfalls only being placed in a single x/z column instead of spread out.

-Reduced Sugar Waterfall amount.

##### Teleportation:
-Added blacklisted_teleportable_hive_blocks.json tag file that datapacks can override.
  Add hive blocks to here if you don't want them to allow teleportation to the Bumblezone dimension.

##### Mod Compat:
-Added blacklisted_resourceful_bees_combs.json and blacklisted_productive_bees_combs.json
  tag files that datapacks can override. Add combs to these tags to blacklist them from being
  imported into the dimension.
  
-Now future combs that Productive Bees adds will be added to the dimension automatically.
   
##### Config:
-Moved the Mod Compat config file to be now per-startup instead of per-world.
  It can be found in the config folder next to the mods folder now.
  
-Removed the requiredBlockUnderHive config and instead, replaced it with the tag:
  the_bumblezone/tags/blocks/required_blocks_under_hive_to_teleport.json. Override
  this tag file with a datapack to change what blocks are needed under hives to allow
  teleportation.
  
### **(V.2.2.7 Changes) (1.16.4 Minecraft)**
   
##### Effects:
-Adjusted Wrath of the Hive to apply effects to bees with the duration equal to
  the remaining Wrath of the Hive time on the target. 
 
-Fixed bees being able to see through walls to find spiders and bears and not
  being able to see through walls for players making bees extra angry.
 
##### Dimension:
-Reduced mob cap a bit in The Bumblezone dimension to reduce amount of Bees due to lag.

 
### **(V.2.2.6 Changes) (1.16.4 Minecraft)**
   
##### Mod Compat:
-Fixed Resourceful Bees compat running way more than needed.

-Fixed unregistered ConfiguredFeatures from occurring from my code.

-Updated mod compat with Productive Bees v0.5.1.1 but dropped support for TileEntity based combs.

-Use Beehive tag for what block Enderpearls can hit to teleport to The Bumblezone dimension.


### **(V.2.2.5 Changes) (1.16.4 Minecraft)**
   
##### Items:
-Fixed particles and sounds being played twice for Empty Bucket, 
  Glass Bottle, and Honey Bottle when activated in a Dispenser.

 
### **(V.2.2.4 Changes) (1.16.4 Minecraft)**
   
##### Mod Compat:
-Fixed crash with Resourceful Bees if player disables their honeycombs.

  
### **(V.2.2.3 Changes) (1.16.4 Minecraft)**
   
##### Mod Compat:
-Now attempt to try and make sure Productive Bees honeycombs 
  does not try to be placed out of bounds. Also will now not 
  spawn any broken combs if that comb type is disabled in 
  Productive Bees.
  
-Fixed log spam about getting a Block Entity before it was made.

##### Misc:
-Removed logo blur from logo in mod list.
   
   
### **(V.2.2.2 Changes) (1.16.3 Minecraft)**
   
##### Mod compat:
-Fixed classloading issues.

-Fixed crash if Potion of Bees is on and Productive Bees is off.


### **(V.2.2.1 Changes) (1.16.3 Minecraft)**
   
##### Major:
-Register to Forge registry instead of vanilla due to a breaking 
  change done by Forge. Special thanks to andrew0030 for helping
  out with fixing this!

##### Dimension:
-Optimized the SurfaceBuilder and the caves a bit!
  The underwater block in the configured surfacebuilder json file
##### was changed from the_bumblezone:porous_honeycomb_block to 
##### the_bumblezone:filled_porous_honeycomb_block.
  
-Fixed bug where Honey Crystals could be floating from worldgen.
  
-Attempted to optimize cave code slightly. Cave shape changed a bit as a result.
 
##### Teleportation:
-Fixed teleportation mode 1 and 2 not having correct coordinates when exiting Bumblezone.

-Fixed Enderpearls not being removed when thrown and Bee Nest and causing people to teleport
  back into The Bumblezone immediately when they leave it.
 
##### Mod Compat:
-Fixed possible Dispenser issue if Potion of Bees's setup event is ran after mine.

-Fixed Resourceful Bees compat not running if Productive Bees is off.
  
  
### **(V.2.2.0 Changes) (1.16.3 Minecraft)**
   
##### Dimension:
-If you make a biome under the namespace of "the_bumblezone",
  that biome will now spawn in the Bumblezone dimension!!!

##### Mod Compat:
-Fixed crash with Resourceful Bees

-Added mod compat with Resourceful Bees! Use their Apairy or hive blocks to enter Bumblezone!
  Their bees spawn in the dimension and from the Honeycomb Brood Blocks! Their Honeycombs spawns
  in the dimension!
  
-Fixed possible crash with registering Dispenser Behaviors.
   
-Fixed bug where modded bees do not spawn when chunks are created in Bumblezone.

##### Bee Interactivity:
-Feeding bees now works again!

   
### **(V.2.1.1 Changes) (1.16.3 Minecraft)**
 
##### Dimension:
-Fixed SurfaceBuilder for BZ biomes being registered twice
  instead of once. Players will see no change other than 1
  less line in the latest.log file lol.
  
-Fixed an issue on my end with Biomes O Plenty on so now
  it will crash showing the real issue with BoP in the logs.
##### Still working on a real fix. Watch https://github.com/Glitchfiend/BiomesOPlenty/issues/1704
  for any progress.
 
-Trying to register biomes ahead of time to reserve their IDs. 
  Might help with a biome ID shifting issue with adding/removing mods or something.

 
### **(V.2.1.0 Changes) (1.16.3 Minecraft)**
 
##### Features:
-Adjusted Honey Crystals to make them spawn more often.

##### Teleportation:
-Fixed crash when exiting The Bumblezone at times.

-Fixed coordinate scaling not working properly when entering/exiting The Bumblezone.

##### Mod Compatibility:
-Updated Productive Bees compat to use their configurable bees and honeycombs.
  Support for their non-configurable bees and blocks was removed as they are 
  moving away from using them.
  
-Added mod support for Carrier Bees's bees!
  Their bees will now spawn in The Bumblezone when you have Wrath of the Hive!
  
-Potion of Bees is working as intended. 
  (They just ported so this was just me checking if the compat still works)
  
##### Dimension:
-Cleaned up the json format for the dimension's json file.

##### Misc:
-Cleaned up codebase a bit.


### **(V.2.0.5 Changes) (1.16.2 Minecraft)**
 
##### Lang:
-Added translation for Honey Slime mob name.

-Portuguese translations added by Mikeliro! Thank you!

-Added Simplified Chinese translation from Samekichi! Thank you!
   
##### Config:
-The status effect configs now reflect their true level of intensity.
  1 is now the minimum value instead of 0 as 0 actually was level 1.
  Absorption is now at the correct level to give 4 extra health instead
  of 8 which made bees too hard to kill.
   
   
### **(V.2.0.4 Changes) (1.16.2 Minecraft)**
 
##### Blocks:
-Fixed crash when mining Honeycomb Brood Block.

##### Mobs:
-Bees spawned in the Bumblezone will have a chance to be 
  pollinated no matter how they are spawned in the dimension.
  (Due to tiny performance improvement change done in backend)


### **(V.2.0.3 Changes) (1.16.2 Minecraft)**
 
##### Major:

* Fix crash at startup becuase I forgot to remove a refrence to a mixin I removed lmao.
  Never rush your fixes people!

### **(V.2.0.2 Changes) (1.16.2 Minecraft)**
 
##### Major:

* FIXED A SUPER WEIRD BUG THAT KILLED ALMOST ALL OTHER MODS
  WHEN A CERTAIN NUMBER OF MODS ARE PUT ON NEXT TO THE BUMBLEZONE.
  I'M SORRY!!! OTHER MODDERS, DO NOT CLASSLOAD DYNAMIC REGISTRY 
  IN YOUR MOD'S INITIALIZATION!!!

### **(V.2.0.1 Changes) (1.16.2 Minecraft)**
 
##### Major:

* Fixed crash on servers.

##### Mod Compatibility:

* Fixed crash when paired with another mod that registers unfinished mobs that crashes when created.
 
### **(V.2.0.0 Changes) (1.16.2 Minecraft)**
 
##### Major:

* UPDATED TO 1.16.2!! (Ported the 1.16.2 Fabric version to Forge)

* Removed compat with Buzzier Bees, Beesourceful, and Potion of Bees as they are not on 1.16.2 yet.

* Added Honey Slime mob natively to this mod which was donated by the Buzzier Bees dev Bagel! 

* Added Beeswax Planks block to be placed on the Bumblezone dimension's ceiling and floor boundaries.

* See Fabric Bumblezone changelog for more details of changes done in 1.16+.
 
### **(V.1.3.9/1.3.10 Changes) (1.15.2 Minecraft)**
 
##### Config:

-Added config to adjust Phantom and Endermen spawnrates in The Bumblezone. (Special thanks to wtchappell for the PR!)

-Added config to adjust Spider/Cave Spider in The Bumblezone.

-Fixed clearUnwantedBiomeFeatures and clearUnwantedBiomeMobs configs to actual work. (hopefully!)
 
### **(V.1.3.8 Changes) (1.15.2 Minecraft)**
 
##### Mod Compatibility:
 
-Changed Bumblezone's biome categories from Jungle to None so people can blacklist it from Quark's structures easier.

-Added an experimental option to reset Bumblezone's biomes after all mods are finished setup to try and let players remove other mod's features/structures/mobs out of Bumblezone.
   
### **(V.1.3.7 Changes) (1.15.2 Minecraft)**
 
##### Mod Compatibility:

-Added support for ProductiveBees's new honeycomb variants so they spawn in the dimension now!

-Bees can be fed and/or calmed with Buzzier Bees's Honey Soup or Sticky Honey Wand!

##### Recipes:
   
-Fixed bucket duplication when crafting Sugar Water from Water Bucket and Sugar in Crafting Table.
  
##### Bees:
   
-Feeding bees will swing your arm always now.
 
### **(V.1.3.6 Changes) (1.15.2 Minecraft)**
 
##### Recipes:

-Fixed and renamed several Bumblezone recipes to prevent conflict so that vanilla's Honey Block can be crafted again. (The smelting recipe overrode it before. Sorry!)
   
### **(V.1.3.5 Changes) (1.15.2 Minecraft)**
 
##### Mod Compatibility:

-Fixed crash when 1.2.0 Beesourceful mod is on.

-Fixed other mod compat getting skipped if one mod crashes or breaks during setup.

-Fixed type in the name of spawnBesourcefulHoneycombVariants config entry.

-Added (v0.1.8+) Productive Bees's honeycomb variants to The Bumblezone!

-Added (v0.1.8+) Productive Bees's Rottened Honeycomb to Spider Infested Bee Dungeons!

-If Beesourceful is off, (v0.1.8+) Productive Bees's ore based honeycombs will be used in worldgen and in Bee Dungeons!


##### Structures:

-Centered the Cobwebs around Spider Infested Bee Dungeons much better now.

##### Entities:
 
-Bees now will not see through walls to find insects, bears, or players taking honey from certain blocks. And Bees will now see through walls to go after players that anger bees by hitting them, drinking honey, or pick up Honey blocks. (Basically I had the requirement of needing a line of sight for their AI backwards due to bad mapping names from MCP)
   
##### Items:

-Using Glass Bottle to obtain Sugar Water in Creative mode will not use up the Glass Bottle now.

-Honey Crystal Shields now can be smelted into Sticky Honey Residue so you can still get a use out of nearly destroyed shields.

-Projectile fire damage will now deal an insane more amount of damage to Honey Crystal Shields.

-Made blocking explosions with shields now actually reduce damage taken by the player.

-Significantly improved the texture for Sticky Honey Residue and Sticky Honey Redstone.
 
### **(V.1.3.4 Changes) (1.15.2 Minecraft)**
 
##### Teleportation:

-Setting warnPlayersOfWrongBlockUnderHive config to true and putting and invalid resource location into RequiredBlockUnderHive config will no longer crash the server when attempting to teleport to The Bumblezone.

-Added generateBeenest config to allow people to turn on or off the creation of Bee Nests when exiting The Bumblezone into an area with no nests or hives nearby.

-Added teleportationMode config to let players pick between spawning at converted coordinates always, spawning at original coordinates always, or a mix when exiting The Bumblezone.

-Constrained the converted coordinate when exiting The Bumblezone to be within -30 million and 30 million so you do not get stuck outside the world's edge.

### **(V.1.3.3 Changes) (1.15.2 Minecraft)**
 
##### Teleportation:

-Fixed serverside crash with teleporting when trying to use the config to require a certain block under beehive. (requiredBlockUnderHive)

-RequiredBlockUnderHive config entry now actually works. 

-Added seaLevelOrHigherExitTeleporting config entry so that when you exit The bumblezone, you can have it where you are only placed near a hive that is above sealevel. This is to help prevent spawning underground due to Beesourceful's Beenests.

##### Blocks:

-Fixed when using a Glass Bottle on a waterlogged Honey Crystal Block gives you Water Bottle instead of Sugar Water Bottle like it should.

-Fixed bug where using Dispenser to make Honey Bottle put honey into Porous Honeycomb Block will not yield a Glass Bottle as waste.

##### Items:

-Fixed dispensersDropGlassBottles's config entry having the opposite effect than what it is currently set as.
   
-Glass Bottles in Dispensers facing Sugar Water or waterlogged Honey Crystal Block will now turn into Sugar Water Bottles when the Dispenser is activated.

-Sugar Water Bucket in Dispensers will now waterlog Honey Crystal Block if it is in-front of the Dispenser.

-Empty Bucket in Dispensers will now become Sugar Water Buckets if there is a waterlogged Honey Crystal Block in-front of the Dispenser.

### **(V.1.3.2 Changes) (1.15.2 Minecraft)**
 
##### Blocks:

-Sticky Honey Redstone now will power through the block it is attached to and will power anything attached to it if it is on the floor. Much more useful now!

##### Items:

-Honey Bottles in Dispensers can now turn Porous Honeycomb Block into Filled Porous Honeycomb Block.

-Honey Bottles in Dispensers will now add Glass Bottles to the Dispenser when used to revive Empty Honeycomb Brood Blocks. (Can be changed in config to always drop the Glass Bottle instead of putting it back into Dispenser)

-Glass Bottles in Dispensers now will take honey from Filled Porous Honeycomb Block and Honeycomb Brood Block. The blocks will turn into their honey-less counterparts and a Honey Bottle will appear in the Dispenser (or dropped if there's no room).
  
##### Mod Compat: 

-Fixed naming of allowSplashPotionOfBeesCompat config entry to be correct and actually takes effect now.

-Fixed bug where The Bumblezone's Depenser behavior overrode Potion of Bees's Splash Potion of Bees Dispenser behavior.

-Potion of Bees's regular Potion of Bees item will now revive Dead Honeycomb Brood Blocks correctly.

-Configs for Potion of Bees's potions compatibility with Empty Honeycomb Brood Blocks now applies to the Dispenser behaviors as well.
   
-Fixed The Bumblezone's custom Dispenser behavior for Honey Bottles to not completely override other mod's Dispenser behaviors for it.
  
-Buzzier Bees's Bottled o Bee and Potion of Bees's regular and splash Potion of Bees items will now add Glass Bottles to the Dispenser when used to revive Empty Honeycomb Brood Blocks.

-Updated Sticky Honey Redstone description in JEI to reflect the new changes to it.
  
### **(V.1.3.1 Changes) (1.15.2 Minecraft)**
 
##### Blocks: 

-Lower and smoothed out buzzing sound of Honeycomb Brood Blocks.
     
##### Items:

-Honey Crystal Shield description now says it can be enchanted with Curse of the Vanishing as Curse of the Binding is only for armor. 

##### Mod Compatibility:

-Added JEI integration. All of The Bumblezone's blocks and items now has a description page.

-Productive Bees's Honey Treat item can be used to grow Honeycomb Brood Blocks now. They have a 20% of growing the larva by 2 stages instead of 1.

### **(V.1.3.0 Changes) (1.15.2 Minecraft)**
   
##### Config:      
      
-Clarified that the Wrath of the Hive bee effect level values in the config are one less than their actual level applied in game.

-Config for Wrath of the Hive's effects it gives to bees now can be set to 0 which would mean the effect gets applied at effect level 1 in the game.

-Added config to control if Buzzier Bee's Bottled Bee item can revive Empty Honeycomb Brood blocks.
  
-Added config to control if Potion of Bees's Splash Potion of Bee item can revive Empty Honeycomb Brood blocks when splashed near it.

##### Mod Compatibility:

-Fixed bug where using Buzzier Bees's Bottled Bee item to revive Empty Honeycomb Brood block will make the block face the wrong way.

-Buzzier Bees's Hive Boat will now move faster than normal boats when on Sugar Water fluid in The Bumblezone dimension!

-Ender Pearls thrown at Productive Bees' nests and hive will now teleport you to The Bumblezone. The Expansion Box Blocks only teleports you to The Bumblezone if they are placed on their hive block and expanded their hive block.

-Productive Bees' bees will now spawn in The Bumblezone at a 1/15th chance and can spawn from the Honeycomb Brood Blocks too.

-Productive Bees' Honey Treat item has a 40% chance of removing Wrath of the Hive effect off of you when fed to bees unlike the Honey Bottle's 30% chance. Will also heal bees a lot more too!
  
##### Blocks: 

-Fixed Sugar Water Block from spamming in the logs that its blockstate model isn't set up even though it had no impact visually or on gameplay.
  
-Renamed Honeycomb Larva Block and Dead Honeycomb Larva Block to Honeycomb Brood Block and Empty Honeycomb Brood Block.
  
-Bees spawned by Honeycomb Brood Blocks will now spawn directly in front of block instead of getting stuck in the block and taking damage.
  
-Honeycomb Brood Blocks now will output power based on the larva's stage when a comparator is used on the block! The power output will be 1 to 4.

-Filled Porous Honeycomb Blocks now will output power when a comparator is used on the block! The power output will be 1.
  
-Added Honey Crystal blocks. Breaking these blocks without Silk Touch or breaking the block it's on will drop Honey Crystal Shards. These blocks are non-renewable so treasure them! They can also be waterlogged by any fluid with the water tag as well but it will turn the water inside the block into Sugar Water instead.
  
-Added Sticky Honey Residue that can be placed on multiple surfaces! Mobs touching this residue will be slowed down and the residue can be used to climb up walls. Using a comparator on this block will output a signal based on how many surfaces this block is attached to. You can obtain this block by smelting Honey Blocks, Honey Crystal Shards, or Honey Crystals. Also, this blocks takes time to mine but can be washed away by fluids or destroyed by right clicking while holding a Wet Sponge, any buckets holding fluids with water tag, or any bottle holding a fluid with water in its name!

-Added Sticky Honey Redstone that can be placed on multiple surfaces! It has all the same effects as Sticky Honey Residue as well as outputting a redstone signal of 1 when a mob is touching this block! This would help make neat contraptions easier. This block can be made by crafting with 1 Sticky Honey Residue and 1 Redstone in any shape.

##### Items: 

-Added Honey Crystal Shards which drops from Honey Crystal blocks. These shards can be eaten to recover some hunger in a pinch.

-Added Honey Crystal Shield which uses the same recipe as regular shields but uses Honey Crystal Shards for all the slots. The shield starts up super weak but will become stronger as you repair it with Honey Crystal Shards. At maximum strength, this shield has about twice the durability as regular shields and can be enchanted with Unbreaking or Curse of the Binding (No Mending as that would be OP!). However, despite that huge durability benefit, this shield is super weak to blocking explosions or fire damage so be careful of what you block with this shield! And lastly, and mob physically attacking your shield will get a slowness effect for 4 seconds due to the honey's stickiness!

-Added recipe to turn Water Bucket into Sugar Water Bucket. Just put 1 Water Bucket and 1 Sugar into a Crafting Table! 

##### Generation:�

-Added Bee Dungeons that will spawn in Honeycomb Holes or caves in the Bumblezone! They have lots of Honeycomb Brood Blocks but if you use Beesourceful, their ore-based Honeycombs will spawn too with Diamond, Ender, and Emeralds being the rarest honeycombs. And if you have Buzzier Bees on, their candles will also spawn in the dungeon with scented candles that gives potion effects spawning very rarely in the center. 

-Honeycomb Holes now has a chance of having an Empty Honeycomb Larva Block.

-Honey Crystal blocks will now generate in caves or underwater in The Bumblezone! They are rare in Sugar Water Biome, uncommon in Hive Pillar biome, and most common in Hive Wall biome.

##### Teleportation:�

-Exiting Bumblezone dimension by falling into the void will not kill the player with fall damage when they teleport into another dimension. I forgot to reset the player's fall time but now it's fixed.
 
##### Mobs:�

-Fixed bug where Bees cannot be angered normally outside of The Bumblezone with default config settings.

-Lowered Cave Spider and Spider spawn rates a bit and removed both spiders from Sugar Water Biome.

-Decreased how many bees will spawn in a bunch when spawning mobs. 

-Vanilla Slime mobs will now spawn in Sugar Water biome but only at a low height below sealevel and in certain chunks. That way you can still make a Slime farm in Bumblezone even with Buzzier Bees on.

##### Translation:

-German Translation added by Aurum! Thank you!

-Added more entries to en_us.json.�

##### Misc:

-Added a version check to make sure this mod will not crash with older versions of Buzzier Bees or Beesourceful.

### **(V.1.2.0 Changes) (1.15.2 Minecraft)**
     
##### Mod Compatibility:�

-If Buzzier Bees is on, Honey Slime mobs will spawn in The Bumblezone instead of regular Slime mobs and monsters will have a slightly higher rate of spawning.

-If Buzzier Bees is on, the roof and floor boundaries of the dimension will use Hive Planks instead, the top of land above sea level area will use Crystallized Honey Block, and the top layer of land at and below sea level area will use Wax Blocks.

-If Buzzier Bees is on, their Honey Wand now can add and remove honey from Porous Honeycomb Block and Honey Filled Porous Honeycomb Block.

-If Buzzier Bees is on, you can use their Bottle of Bee item on a Dead Honeycomb Larva Block will revive the block into a stage 1�Honeycomb Larva Block. This can be done by hand or by a Dispenser facing the block.

-If Beesourceful is on, their ore bees and ender bees can now spawn in The Bumblezone! They have a 15% chance of spawning when a regular bee is spawned. Then the chance of each type of Beesourceful bee being chosen is 1% Ender, 2% Diamond, 7% Emerald, 10% Lapis, 10% Quartz, 20% Gold, 20% Redstone, and 30% Iron.

-If Beesourceful is on, their honeycomb variants will now in The Bumblezone at all kinds of heights and height bands. Start exploring to find where they spawn!
      
-If Potion of Bees is on, you can use their Potion of Bees item on a Dead Honeycomb Larva Block will revive the block into a stage 1, 2, or 3�Honeycomb Larva Block. This can be done by hand or by a Dispenser facing the block.

-If Potion of Bees is on, you can throw their Splash Potion of Bees item and any Dead Honeycomb Larva Block nearby on impact will revive the blocks into a stage 1, 2, or 3�Honeycomb Larva Block. If you have this item in a Dispenser directly facing the Dead Honeycomb Larva Block, it'll revive the block into a stage 1, 2, or 3�Honeycomb Larva Block without bees going everywhere.

##### Config:      
      
-Added configs to control a lot of the interaction between this mod and Buzzier Bees.

-Added configs to control the interaction between this mod and Beesourceful.

-Added new config options to let players specify if a certain block needs to be under the Bee Nest/Beehive to teleport to The Bumblezone dimension.
 
-Added new config so players can allow or disallow teleporting to and from Bumblezone with modded Bee Nests or modded Beehive blocks.

##### Teleportation:�

-Throwing Enderpearls at all blocks that extends BeehiveBlock (includes modded and vanilla's BeeNests and BeeHive blocks) will teleport you into the dimension!

-Adjusted teleporting so teleporting to Bumblezone dimension from underground will spawn you trapped underground in the dimension less often.
 
##### Mobs:�

-Bees will now no longer switch to attacking nearby passive players if they are angered by another mob/player. They will only attack entities with Wrath of the Hive effect in the dimension so there is no more collateral damage anymore.

-Any mob or player hitting a bee will inflict Wrath of the Hive effect onto the attacker. Now fighting off angry bees will prolong the Wrath of the Hive effect!

-In The Bumblezone dimension, Bees will now become aggressive towards all mobs with "bear" in their name including modded bear mobs.

-Bees will now hunt down and attack all insect mobs that doesn't have "bee" in their name if they are in The Bumblezone dimension.
 
-You can feed bees a Sugar Water bottle to slightly heal them. If you feed them a Honey Bottle instead, they will recover a lot more health!
 
-If you have Wrath of the Hive effect on, feeding bees a Sugar Water Bottle will have a 7% chance of removing the effect from yourself and calming the bees. If you feed them a Honey Bottle instead, you have a 30% chance of removing Wrath of the Hive form yourself and calming the bees.

-Nerfed spawnrate for slime mobs when creating chunk for first time.

-Nerfed spawnrate of Phantoms some more.

-Increased Spider spawnrates.

-Cave Spider can spawn in Hive Wall biomes now. 

##### Blocks:�

-Added Sugar Water fluid to spawn in The Bumblezone instead of regular water! Swimming in this fluid is slightly slower than swimming in water due to the excess of sugar in it. If bees swim in the Sugar Water and aren't taking damage, the bees will sip the water and heal themselves very slowly. Also, any Sugar Cane placed next to a Sugar Water block will grow faster and taller (up to 5 blocks high!)

-Sugar Infused Stone and Sugar Infused Cobblestone will be made when Sugar Water touches Lava! When mined, they drop Sugar and Cobblestone but you can use Silk Touch to keep them as is. When put into a Furnace, you can quickly burn out the Sugar from the blocks for some quick cheap XP.
   
-Walking on Filled Porous Honeycomb Block will now slightly slow your movement due to the sticky honey.
        
-Honeycomb Larva Block is now added! They will grow a larva inside it through 4 stages and on the final stage, it will spawn a bee if there is less than 10 bees within 50 blocks of the block. The larva will grow faster in The Bumblezone dimension than any other dimension. You can use a Honey Bottle or Buzzier Bee's Sticky Honey Wand to speed up the larva growth or by using a Dispenser with Honey Bottles on the block. If you use a Glass Bottle or Buzzier Bee's Honey Wand, it will kill the larva but you get honey and a bunch of angry bees coming! If Buzzier Bees is on, it may also sometimes spawn a Honey Slime mob. And if Beesourceful is on, it can sometimes spawn a Beesourceful's resource bee mob.
  
-Added Dead Honeycomb Larva Block that is created when using a Glass Bottle on a Honeycomb Larva Block. This block is decorative and serves no other use. It's dead. :(  Buuuut if you have Buzzier Bee's on, you can use a Bottle of Bee to revive the block back to an alive Honeycomb Larva Block at stage 1. If you use Potion of Bee's bee potion items, that too can revive the block but it will be at stage 1, 2, or 3 as a bonus! This behavior can be done by hand or by using a Dispenser with these items in it right up against the Dead Honeycomb Larva Block. If you throw Potion of Bee's Bee Splash Potion item and it lands near some Dead Honeycomb Larva Blocks, it'll revive the blocks too!

##### Items:�

-Added Sugar Water Bottle! To get this, use an empty Glass Bottle on Sugar Water. If you drink a Sugar Water Bottle, you restore a tiny bit of hungry but get Haste 1 effect for a short period of time!

-Added Sugar Water Bucket to move Sugar Water around. Works with Dispensers too!

##### Generation:�

-The dimension uses Air instead of Cave Air to help with feature generation.

-Honeycomb Holes now only generate exposed to the air and not completely buried underground.

-Honeycomb Holes now will generate with Honeycomb Larva Block at various stages in its life cycle!

### **(V.1.1.0 Changes) (1.15.2 Minecraft)**
     
##### Misc:�
-Fixed potential crash at startup when in a foreign language.
   
### **(V.1.0.4 Changes) (1.15.2 Minecraft)**
   
##### Teleportation:�
-Improved some edge cases with Teleportation and fixed some potential bugs with other edge cases.

##### Config:�
-lower Absorption default config value from 2 to 1 due to 2 making it way too hard to kill Wrath of the Hive angered bees. You'll still need Bane of Arthropods.

##### Mobs:�
-Bees that spawn in a chunk when the chunk is first created has a 20% chance of being pollinated bees.
-Patched a bug that could spawn mobs underground and hurt performance as they suffocate. 

##### Dimension:�
-Fixed potential visual bug that causes blue fog in some situations.
      
      
### **(V.1.0.3 Changes) (1.15.2 Minecraft)**
  
##### Blocks:�
-Fixed bug so using Glass Bottle or Honey Bottles on the Filled Porous Honeycomb blocks and Porous Honeycomb blocks while in creative mode won't use up the bottle in your hand.

      
### **(V.1.0.2 Changes) (1.15.2 Minecraft)**
  
##### Teleportation:�
-Fixed teleportation math and player previous dimension storing to not allow a bug that lets you reach world border in seconds. Big oops. That's a BIG BUG I missed despite lots of testing! 
  	
      
### **(V.1.0.1 Changes) (1.15.2 Minecraft)**
  
##### Teleportation:�
-Fixed teleportation not working when trying to enter The Bumblezone from a non-Overworld dimension.

##### Config:�
-Added config option to make exiting The Bumblezone always place you into the Overworld.

      
### **(V.1.0.0 Changes) (1.15.2 Minecraft)**
   
##### Major:�
-FIRST RELEASE OF THIS MOD

##### Teleportation:�
-Throw an Enderpearl at a Bee Nest in any dimension to enter The Bumblezone dimension!
-To exit The Bumblezone dimension, go to Y = -1 or Y = 256 and it will place you back to the dimension you originally came from and if it can't resolve what dimension you came from, you'll be placed back in the Overworld. 
-Your XZ coordinates will be scale going to and from the dimension. In fact, traveling 1 block in The Bumblezone is the same as traveling 10 blocks in the Overworld!
-If you exit the dimension by going down through the floor, it will look for the lowest Bee Nest that is in the general area of your new scaled coordinates and will place you next to it. If no Bee Nest is found, it will place you on highest place and generate a Bee Nest at your feet.
-If you exit the dimension by going down through the ceiling, it will look for the highest Bee Nest that is in the general area of your new scaled coordinates and will place you next to it. If no Bee Nest is found, it will place you on highest place and generate a Bee Nest at your feet.

##### Blocks:�
-Porous Honeycomb block is added. If you use a Honey Bottle on this block, it'll consume the honey and turn into a Filled Porous Honeycomb block.
-Filled Porous Honeycomb block is added. If you use a Glass Bottle on this block, you'll get a Honey Bottle and the block turn into a Porous Honeycomb block (this will REALLY anger bees around you in the Bumblezone dimension!).

##### Biomes:�
-Hive Wall biomes will be the giant flat vertical slabs filled with holes that the bees dug out. In each hole is some Honey Blocks! 
-Between the Hive Wall biomes is the Sugar Water Floor biome which creates a water filled space between each honeycomb wall. Bring a boat!
-Also Hive Pillar biomes will spawn quite a bit to make massive pillars full of holes that connects the ceiling and floor!

##### Mobs:�
-If you drink a Honey Bottle, pick up a Honey Block in the dimension, take honey from a Filled Porous Honeycomb block, all bees within 64 range of you will become EXTREMELY aggressive towards you. Beware, the bees will be REALLY angry and will get a temporary Strength 3, Absorption 2, and Speed 1 effect!
-Bees generate at a high rate in the dimension and will become aggressive towards Polar Bears and Pandas if the player somehow gets them into the dimension. The bees will get a temporary Strength 1 and Speed 1 effect.
-Spiders, Endermen, and Phantoms will spawn at a low rate with Phantoms being extremely rare.
-Slime will spawn at any height when a new chunk is created for the first time. However, they will only respawn in an already generate chunk if that chunk is marked as a "slime chunk" and at Y = 40 or below just like the Overworld. 

##### Config:�
-Added 11 configs with 8 affecting the Wrath of the Hive effect and 3 affecting the Bumblezone dimension itself. Here is what the config looks like: https://gist.github.com/TelepathicGrunt/0ad76feeb0bee1fc7eeba25d5f0821b4
