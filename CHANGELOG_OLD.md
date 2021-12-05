### **(V.3.4.1 Changes) (1.17.1 Minecraft)**

##### Misc:
Updated Fabric API code usage to not use deprecated code to prevent blowing up in future fabric API updates. Hopefully.

Switched to using FabricEntityTypeBuilder to not get "DataFixer" log spam for Bumblezone entities.


### **(V.3.4.0 Changes) (1.17.1 Minecraft)**

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

#### Mod Compat:
Added support for Notify - https://www.curseforge.com/minecraft/mc-mods/notify
 Notify will let you know if a new update for this mod is out on the Mod Menu screen.


### **(V.3.3.2 Changes) (1.17.1 Minecraft)**

##### Teleportation:
Fixed bug where setting Teleportation Mode to 2 or 3 and trying to exit Bumblezone on an entity that originated from Bumblezone would spawn player under Bedrock in Overworld.


### **(V.3.3.1 Changes) (1.17.1 Minecraft)**

##### Entities:
Beehemoth will now stay still when crouch right-clicked like how you make dogs and cats sit.
 If it is sitting, shift right click with empty hand to remove saddle. Shift right click while holding any item to make it unsit (or ride it to unsit)
 To feed Beehemoth, just right click while holding any honey feeding item. No crouching needed.

Right clicking Beehemoth without crouching will always make you ride the mob. No longer need an empty hand.

##### Lang:
Added english translations for advancements to the other language files so the translation keys don't show.


### **(V.3.3.0 Changes) (1.17.1 Minecraft)**

##### Advancements:
Added new advancements for this mod to add some progression and high xp rewards!
 Note: Progress or conditions met before this update will not contribute towards the new advancements.

##### Entities:
Fixed Honey Slime not giving Honey Bottle when harvested with Glass Bottle.

Honey Slimes have a bit more health, a bit more attack power, and will jump faster and higher when angry as well as stay angry longer.

Killing adult Honey Slimes with looting enchantment may drop Sugar and/or Slime Balls.
 Before, it was the child that had this feature instead of the adult by mistake.

Fixed issue where trying to remove Pollen Puff from bees causes some logspam.

Fixed using items on Bees/Honey Slime may cause the item's use to continue pass the entity. 
 Such as feeding bees a Honey Bucket will place honey behind the entity at well.

##### Items:
Fixed durability bar on Honey Crystal Shield to correctly show how much durability is really left.

##### Effects:
Protection of the Hive on the player will now be removed if you hurt a Bee outside The Bumblezone dimension.

##### Recipes:
Renamed the_bumblezone:beehive_beeswax_to_bee_nests recipe json to the_bumblezone:beehive_beeswax_to_bee_nest.


### **(V.3.2.6 Changes) (1.17.1 Minecraft)**

##### Misc:
Fixed crash caused by a bad mixin on my end. Sorry. Now requires Fabric API 0.41.0 or newer.


### **(V.3.2.5 Changes) (1.17.1 Minecraft)**

##### Teleportation:
Redid a bunch of teleportation code so now riding any mob or vehicle will still allow teleportation out of the Bumblezone.
And you can teleport into the Bumblezone while riding a vehicle if you use the piston pushing into beehive method.

##### Entities:
Shy's transbee texture and other LGBT+ textures are now applied to 2% of vanilla bees!
Her resourcepack can be downloaded separate here if you want transbees without Bumblezone on: https://www.curseforge.com/minecraft/texture-packs/shy-trans-bee

##### Config:
Added missing lang entry for the Beehemoth Speed config.

##### Lang:
Updated ru_ru.json translations. Thank you Bytegm!


### **(V.3.2.4 Changes) (1.17.1 Minecraft)**

##### Entities:
Beehemoth speed can not be changed by config in the world's serverconfig folder in the_bumblezone-general.toml
Base speed for Beehemoth was slightly buffed.

Holding down Space (jump button) while riding Beehemoth will make the bee fly upward.

Fixed bug where you could feed Beehemoth to surpass the 1000 point limit on friendship.
Capped at 1000 not and cannot be lowered below -100 too.


### **(V.3.2.3 Changes) (1.17.1 Minecraft)**

##### Lang:
Updated ru_ru.json translations. Thank you Tkhakiro!

##### Entities:
Beehemoth now follows players when they hold any item that is in c:items/buckets/honey tag.


### **(V.3.2.2 Changes) (1.17.1 Minecraft)**

##### Blocks:
Fixed collision on Sticky Honey Residue and Sticky Honey Redstone being backwards.
Was an error I missed from when I switched this project to Mojmap.


### **(V.3.2.1 Changes) (1.17.1 Minecraft)**

##### Lang:
Forgot to add lang translations for every new entries

##### Entities:
Increased spawnrate of Beehemoth

##### Structures:
Increased spawnrate of HoneyCave Room structure.


### **(V.3.2.0 Changes) (1.17.1 Minecraft)**

##### Misc:
Codebase is now remapped to use Mojmap instead of Yarn.

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


### **(V.3.1.2 Changes) (1.17.1 Minecraft)**

##### Lang:
Special thanks to Tkhakiro for helping to update the ru_ru.json translations!

Special thanks to mc-kaishixiaxue for helping to update the zh_cn.json translations!

##### Items:
Fixed several usages where I was not giving the right item to the player's inventory such as using a water bucket on
a Honey Crystal was not giving players an empty bucket afterwards. Now it will.

##### Blocks:
Fixed Honey Crystal block to properly implement Waterloggable so it interacts with other mods better for fluid adding/extracting.
Honey Crystal block can no longer be filled with fluid while in the Nether now and when placing the block in creative mode,
the block can only be waterlogged if placed in a water-tagged fluid source block. Otherwise, you need water-tagged buckets to
waterlog the block by hand or dispenser or by other mod's machines and stuff.


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


### **(V.2.4.8 Changes) (1.16.5 Minecraft)**

##### Items:
* Fixed Sugar Water Bucket being uncraftable.

* Crafting Sugar Water Bucket will no longer duplicate the bucket.


### **(V.2.4.7 Changes) (1.16.5 Minecraft)**

##### Blocks:
* Fixed Sticky Honey Residue and Sticky Honey Redstone having the wrong hitbox when placed on the sides.

##### Teleportation:
* Increased search radius for beehives when leaving Bumblezone.

* Fixed bug where client side visual makes it look like the player continues falling below y = 3 even though they remained at y = 3 on serverside. (Client was in no danger tho)


### **(V.2.4.6 Changes) (1.16.5 Minecraft)**

##### Blocks:
* Fixed crash at startup... oops.


### **(V.2.4.5 Changes) (1.16.5 Minecraft)**

##### Blocks:
* Beeswax Planks texture is adjusted to look better when next to other Beeswax Planks.

* Attached a POI system to Brood Blocks placed and generated from here on out. Will be a stepping stone to optimizing Wrath of the Hive in 1.17.

##### Entities:
* Cleaned up Bee Feeding code a ton and added the_bumblezone:bee_feed_items tag for players to add new items that can be fed to bees to heal them.

* Added the_bumblezone:wrath_activating_items_when_picked_up item tag so players can add/remove items that should anger bees when picked up.

* Added the_bumblezone:wrath_activating_blocks_when_mined block tag so players can specify what blocks, when mined, will make bees angry.
  Honey Blocks are now in this tag so they only anger bees when mined. Not when picked up now.

##### Teleportation:
* Teleporting out of The Bumblezone is now much better optimized and has a bigger search radius for existing bee hive blocks.


### **(V.2.4.4 Changes) (1.16.5 Minecraft)**

##### Mixins:
* Moved injection point for the GlassBottle to be slightly more mod compat.

* Cleaned up many mixins to prevent possible mixin conflicts with other mods.


### **(V.2.4.3 Changes) (1.16.5 Minecraft)**

##### Dependencies:
* Updated Cardinal Components that is jar-in-jar in my mod to v2.8.2 so it works with newer Fabric API.
  Do note, I will stop jar-in-jaring Cardinal Components in Bumblezone's next update so you will have to download that dependency mod separately.


### **(V.2.4.2 Changes) (1.16.5 Minecraft)**

##### Teleportation:
* Fixed issue where Enderpearl impacting beehives may not always work due to the impact coordinates being heavily offset from the actual impact.


### **(V.2.4.1 Changes) (1.16.5 Minecraft)**

##### Items:
* Changed how Music Discs are added to Wandering Trader's trades since Fabric API's hook is broken for this.


### **(V.2.4.0 Changes) (1.16.5 Minecraft)**

##### Dependencies:
* Cloth Config is no longer JIJ (jar-in-jar) into Bumblezone. You will need to download that mod separately.

##### Dimension:
* The loud Bee buzzing sound was replaced with a much softer beehive buzzing! Easier on the ears!

* Music will play when you are in a Bumblezone Biome! The song that plays is Honey Bee by Rat Faced Boy.

##### Effects:
* Wrath of the Hive now plays music when triggered! The song that plays is a midi version of Flight of the Bumblebee by Rimsky Korsakov.

* Wrath of the Hive's default config value is now a full minute and angry bee's strength config is lowered by two.

* When you have Wrath of the Hive, bees will now spawn in open space a bit away from you but will come chase you down! Grab your Bane of Arthropod sword!

* Bees now cannot get Wrath of the Hive effect to prevent them attacking each other.

* Drinking Honey Bottles no longer trigger Wrath of the Hive effect in Bumblezone's dimension.

* Mobs with Wrath of the Hive will now make Honeycomb Brood Blocks near them grow faster. Stay out of Bee Dungeons when you have the effect!

* Protection of the Hive's default config value for duration is now 1680.

##### Enchantments:
* Added Comb Cutter enchantment for Shears and Swords.
  This will make mining all blocks with "comb" in the name much faster!
  And it will slightly increase mining speed for Hive, Nests, and Wax based blocks too.
  It will also increase the number of combs you get when shearing a vanilla Bee Hive / Bee Nest!

##### Entities:
* Using Honey Blocks on vanilla Slime mobs that are size 1 or 2 will turn them into Honey Slime mobs!
  "the_bumblezone:turn_slime_to_honey_slime" item tag controls what item can do the conversion.

* Honey Slime now gets significant reduced fall damage when they are covered in honey!

##### Blocks:
* Fixed Honey Crystal block so that it rotates and mirrors properly when loaded from nbt files.

* Adjusted Empty Porous Honeycomb, empty Brood block, and non-empty Brood blocks's textures to try and reduce tiling issues a bit.

* Switched to using TagRegistry.block in backend for block tags.

##### Items:
* Honey Crystal Shield's valid repair items is now controlled by the item tag: "the_bumblezone:honey_crystal_shield_repair_items"

* Added new music discs to play the two new music added to this mod! You can obtain the discs from Wandering Traders as a rare trade!

##### Features:
* Optimized waterfall feature to use honeycombs_that_features_can_carve block tag.

* Bee Dungeon and Spider Infested Bee Dungeon code backend is significantly cleaned up and now uses processors to change blocks.

  Mod Compat:
* Added Charm support so now their candles spawn in Bee Dungeons and Spider Infested Bee Dungeons!

* Several Bumblezone Items are now used in Charm's BeeKeeper's trade offers!


### **(V.2.3.0 Changes) (1.16.5 Minecraft)**

Mod Compat:
• Added compatibility with Bee Better.

##### Blocks:
• Honeycomb Brood Blocks now spawn child Bees and child Honey Slime mobs.

##### Items:
• Honey Slime Spawn Eggs now use better code practices in backend.

##### Teleportation:
* Throwing Enderpearl at Beehives will teleport you to dimension more consistently.


### **(V.2.2.17 Changes) (1.16.4 Minecraft)**
   
   Mod Compat:
• Added compat with AnvilFix so that Honey Crystal Shield can still be upgraded again and cost XP for balance.


### **(V.2.2.16 Changes) (1.16.4 Minecraft) Quickfix**
   
##### Config:
• Changed broodBlocksBeeSpawnCapacity so that it is not multiplied by 10 anymore for bee limit. 
 If it is set to 50 now, then the Brood blocks will not spawn more bees if 50 bees already exist.


### **(V.2.2.15 Changes) (1.16.4 Minecraft)**
   
##### Dimension:
• Vanilla Bees in The Bumblezone now have a new AI that makes them wander better, lag less, and not cluster on the ceiling anymore.
  Bee rates have been lowered a bit as well.

##### Mixins:
• Prefixed all my accessor and invoker mixins due to this bug in mixins that could cause a crash with other mods for same named mixins.
 https://github.com/SpongePowered/Mixin/issues/430


### **(V.2.2.14 Changes) (1.16.4 Minecraft)**
   
##### Blocks:
• Added broodBlocksBeeSpawnCapacity config option to allow users to change the automatic bee spawning mechanics of Honeycomb Brood Blocks.

##### Teleportation:
• Fixed Teleportation mode 2 and 3 being broken and not saving previous pos and dims.

##### Dimension:
• Falling out of Bumblezone dimension to teleport out shouldn't deal fall damage now.

• Added onlyOverworldHivesTeleports config option to allow people to make it only possible to enter The Bumblezone from the Overworld.


### **(V.2.2.13 Changes) (1.16.4 Minecraft)**
   
##### Tags:
• Fixed tags so that they do not crash on servers.

   
### **(V.2.2.12 Changes) (1.16.4 Minecraft)**
   
##### Misc:
• Fixed various serverside crashes.
 
##### Dimension:
• Fixed Sugar Waterfalls only being placed in a single x/z column instead of spread out.

• Reduced Sugar Waterfall amount.

##### Teleportation:
• Added blacklisted_teleportable_hive_blocks.json tag file that datapacks can override.
  Add hive blocks to here if you don't want them to allow teleportation to the Bumblezone dimension.

##### Config:
• Removed the requiredBlockUnderHive config and instead, replaced it with the tag:
  the_bumblezone/tags/blocks/required_blocks_under_hive_to_teleport.json. Override
  this tag file with a datapack to change what blocks are needed under hives to allow
  teleportation.
  

### **(V.2.2.11 Changes) (1.16.4 Minecraft)**
   
##### Effects:
• Adjusted Wrath of the Hive to apply effects to bees with the duration equal to
  the remaining Wrath of the Hive time on the target. 
  
• Fixed bees being able to see through walls to find spiders and bears and not
  being able to see through walls for players making bees extra angry.


### **(V.2.2.10 Changes) (1.16.4 Minecraft)**
   
##### Items:
• Fixed particles and sounds being played twice for Empty Bucket, 
  Glass Bottle, and Honey Bottle when activated in a Dispenser.
  
##### Backend:
• Some cleaning up the code. Now requires newer Fabric API and Fabric Loader.

• No longer Jar-in-Jar ModMenu. Was a mistake on my part lol.
    
    
### **(V.2.2.9 Changes) (1.16.3 Minecraft)**
   
##### Dimension:
• Optimized the SurfaceBuilder and the caves a bit!
  The underwater block in the configured surfacebuilder json file
  was changed from the_bumblezone:porous_honeycomb_block to 
##### the_bumblezone:filled_porous_honeycomb_block.
  
• Fixed bug where Honey Crystals could be floating from worldgen.
  
• Attempted to optimize cave code slightly. Cave shape changed a bit as a result.
 
##### Teleportation:
• Fixed Enderpearls not being removed when thrown and Bee Nest and causing people to teleport
  back into The Bumblezone immediately when they leave it.

  
### **(V.2.2.8 Changes) (1.16.3 Minecraft)**
   
##### Teleportation:
• Fixed bug where exiting The Bumblezone always put you in Overworld
  instead of the actual dimension you came from when entering BZ's dimension.

##### Dimension:
• Cleaned up a tiny bit of code and special thanks to
  Pyrofab for updating Cardinal Component's usage in this mod!
  

### **(V.2.2.7 Changes) (1.16.3 Minecraft)**
   
##### Dimension:
• Cleaned up the json format for the dimension's json file.

##### Features:
• Adjusted Honey Crystals to make them spawn more often.


### **(V.2.2.6 Changes) (1.16.3 Minecraft)**
   
##### Teleportation:   
• Fixed coordinate scaling when entering/leaving The Bumblezone dimension.

• Fixed a possible theoretical crash that could occur when messing with 
  adding/removing dimensions and attempting to teleport to/from The Bumblezone.
  
##### Mobs:   
• Increased Bee and Honey Slime rates in the dimension as Lithium mod will 
  optimize the bee lag away.


### **(V.2.2.5 Changes) (1.16.3 Minecraft)**
   
##### Lang:   
• Added Simplified Chinese translation from Samekichi! Thank you!

• Added missing lang entries for mod compatibility config entries. 

##### Config:
• Significantly reduced the range of values for bee anger and 
  bee status effect intensity config options to allow the slider
  to actually let you pick good values now.
  
• The status effect configs now reflect their true level of intensity.
  1 is now the minimum value instead of 0 as 0 actually was level 1.
  Absorption is now at the correct level to give 4 extra health instead
  of 8 which made bees too hard to kill.
  
• Removed empty tooltips after updating autoconfigu library.


### **(V.2.2.4 Changes) (1.16.2 Minecraft)**
   
##### Lang:
• Added translation for Honey Slime mob name.

• Portuguese translations added by Mikeliro! Thank you!

    Mod Compat:
• Added Mod Compatibility with Potion of Bees! Their potions can be 
  used to revive Empty Honeycomb blocks by hand or by Dispensers.
##### https://www.curseforge.com/minecraft/mc-mods/potion-of-bees-fabric
  
• Moved a mixin that was conflicting with Carpet mod.
  As a result, Bees spawned in the Bumblezone will have a chance
  to be pollinated no matter how they are spawned in the dimension.
     
##### Biomes:
• Attempted a workaround fix to prevent generated Bumblezone biomes
  from being replaced with the wrong biome due to a rare Mojang bug 
  that happens when removing other datapack biomes.
  

### **(V.2.2.3 Changes) (1.16.2 Minecraft)**
   
##### Major:
• FIXED A SUPER WEIRD BUG THAT KILLED ALMOST ALL OTHER MODS
  WHEN A CERTAIN NUMBER OF MODS ARE PUT ON NEXT TO THE BUMBLEZONE.
  I'M SORRY!!! OTHER MODDERS, DO NOT CLASSLOAD DYNAMIC REGISTRY 
  IN YOUR MOD'S INITIALIZATION!!!

### **(V.2.2.2 Changes) (1.16.2 Minecraft)**
   
##### Major:
• Fixed crash on servers.

    Mod Compatibility:
• Fixed crash when paired with another mod that registers unfinished mobs that crashes when created.
 
##### Teleportation:
• Fixed message appear about wrong block under Bee Nest when throwing Enderpearls at any non-Bee Nest block.

### **(V.2.2.1 Changes) (1.16.2 Minecraft)**
   
	Config:
• Adjusted configs text to show description right away instead of in tooltips.
 
	Items:
• Fixed Honey Shield not having blocking animation.

### **(V.2.2.0 Changes) (1.16.2 Minecraft)**

	Major:
• Updated to 1.16.2 minecraft!

• Replaced Cotton Config with Autoconfigu + Cloth.

• Attempted to improve performance by removing a few mixins into tick methods.

##### Blocks:
• Added Beeswax Planks which can be used to craft Bee Nests blocks by
  crafting with 8 Hive Planks with a Honeycomb in the center.
  
• Honeycomb Blocks will not spawn a new bee when left alone
  and there is 3 or more bees within 50 blocks of it
  
	Dimension:
• Can now sleep in the dimension with a bed. No more exploding beds.

• Fixed player taking damage when throwing Enderpearls at Beenests/Beehives

• Beeswax Planks will be placed to mark the boundaries of the dimension's ceiling and floor.

• Teleporting into The Bumblezone will place Honeycomb Block at player's feet if there's air
  at the player's destination spot all the way down into the void. This will prevent players 
  from teleporting into The Bumblezone and immediately fall out of the dimension due to the 
  area being heavily mined before.

	Biomes:
• Reduced mob spawnrates a bit to lower bee density.

• Attempted experimental spawn_cost mechanic to make hostile mobs be less dense in the biomes.

	Mobs:
• Feeding bees Honey Bottle or Sugar Water Bottle will cause 
  player to swing their hand alongside drinking animation.

	Features:
• Adjusted spawnrates and heights of Sugar Waterfalls.

    Status Effects;
• Protection of the Hive now will be consumed if the player does anything that would 
  have trigger Wrath of the Hive. It acts like a second chance now to prevent angry bees.

### **(V.2.1.2 Changes) (1.16.1 Snapshot Minecraft)**

	Misc:
• Fixed reloadable config causing server to not shut down.

### **(V.2.1.1 Changes) (1.16.1 Snapshot Minecraft)**

	Dimension:
• Adjusted terrain to try and make it slightly more open.

• Fixed game not warning player that the block under the bee nest is incorrect if the config specified a different block is needed under the bee nest to enter the Bumblezone.

	Entities:
• Honey Slime now can speed up getting their honey back from being on Honeycomb Brood Blocks.

	Misc:
• Fixed crash on dedicated servers due to running client sided code in Cave Sugar Waterfall code.


### **(V.2.1.0 Changes) (1.16.1 Snapshot Minecraft)**

	Config:
• Clarified time units in the duration config for Wrath of the Hive effect.

	Entities:
• Added Honey Slime! (Special thanks to Bagel for donating Honey Slime to The Bumblezone) Spawns naturally in The Bumblezone and can spawn from Honey Brood Blocks with bees.

• Slightly reduced amount of bees that spawn at chunk creation.

	Effects:
• Added Protection of the Hive! When active, anyone that attacks you will get Wrath of the Hive effect! You can get Protection of the Hive by feeding Bees or Honeycomb Brood blocks Honey Bottles or Sugar Water Bottles!

	Misc:
• Attempted reducing file size.

### **(V.2.0.1 Changes) (1.16.1 Snapshot Minecraft)**

	Backend:
• Fixed crash on server.

• Fixed log spam due to trying to do clientside mixins on serverside.

• License changed to LGPLv3

	Dimension:
• Raised cloud height to 1000 to hide clouds.


### **(V.2.0.0 Changes) (1.16.1 Snapshot Minecraft)**

	Backend:
• Updated to 1.16.1 Snapshot MC

 	Dimension:
• Due to JSON formatted dimensions, some dimension stuff or configs may had broke. Please let me know if they have.

 	Teleportation:
• Any block that extends BeehiveBlock now can be used for teleportation to The Bumblezone!

 	Config:
• Nerfed the default Absorption level that bees get when affected by the Wrath of the Hive status from 2 to 1. This is because absorption 2 made it way too hard to kill bees but at 1, its easier but you may still need Bane of Arthropod enchantments.


### **(V.1.0.1 Changes) (1.15.2 Minecraft)**

	Backend:
• Fixed crash due to a mixin not being compatible with Carpet mod.
       
       
### **(V.1.0.0 Changes) (1.15.2 Minecraft)**
    
	Major:
• FIRST FABRIC RELEASE OF THIS MOD
  
 	Mobs:
• Bees have a 20% of being full of pollen when they spawn nautrally in the Bumblezone dimension.

##### Teleportation:
• Improved teleportation to make it place you on surface of water in Bumblezone if you were going to teleport underwater.
• This mod will treat all other non-Nether-like dimension as having a normal coordinate scaling (10:1 ratio of those dimensions's scale to Bumblezone's scale) and all Nether-like dimensions as having the nether scale (10:8 ratio of those Nether-like dimensions to Bumblezone's scale)

	Blocks:
• Fixed bug so using Glass Bottle or Honey Bottles on the Filled Porous Honeycomb blocks and Porous Honeycomb blocks while in creative mode won't use up the bottle in your hand.



And here's what has been done in the Forge version so you know what else this Fabric version has since everything is ported
------------------------------------------------
       | The Bumblezone Forge changelog |
       
       
### **(V.1.0.2 Changes) (1.15.2 Minecraft)**
  
  	Teleportation:
• Fixed teleportation math and player previous dimension storing to not allow a bug that lets you reach world border in seconds. Big oops. That's a BIG BUG I missed despite lots of testing!
  	
       
### **(V.1.0.1 Changes) (1.15.2 Minecraft)**
  
  	Teleportation:
• Fixed teleportation not working when trying to enter The Bumblezone from a non-Overworld dimension.

	Config:
• Added config option to make exiting The Bumblezone always place you into the Overworld.

       
### **(V.1.0.0 Changes) (1.15.2 Minecraft)**
    
	Major:
• FIRST RELEASE OF THIS MOD

	Teleportation:
• Throw an Enderpearl at a Bee Nest in any dimension to enter The Bumblezone dimension!
• To exit The Bumblezone dimension, go to Y = -1 or Y = 256 and it will place you back to the dimension you originally came from and if it can't resolve what dimension you came from, you'll be placed back in the Overworld.
• Your XZ coordinates will be scale going to and from the dimension. In fact, traveling 1 block in The Bumblezone is the same as traveling 10 blocks in the Overworld!
• If you exit the dimension by going down through the floor, it will look for the lowest Bee Nest that is in the general area of your new scaled coordinates and will place you next to it. If no Bee Nest is found, it will place you on highest place and generate a Bee Nest at your feet.
• If you exit the dimension by going down through the ceiling, it will look for the highest Bee Nest that is in the general area of your new scaled coordinates and will place you next to it. If no Bee Nest is found, it will place you on highest place and generate a Bee Nest at your feet.

	Blocks:
• Porous Honeycomb block is added. If you use a Honey Bottle on this block, it'll consume the honey and turn into a Filled Porous Honeycomb block.
• Filled Porous Honeycomb block is added. If you use a Glass Bottle on this block, you'll get a Honey Bottle and the block turn into a Porous Honeycomb block (this will REALLY anger bees around you in the Bumblezone dimension!).

	Biomes:
• Hive Wall biomes will be the giant flat vertical slabs filled with holes that the bees dug out. In each hole is some Honey Blocks!
• Between the Hive Wall biomes is the Sugar Water Floor biome which creates a water filled space between each honeycomb wall. Bring a boat!
• Also Hive Pillar biomes will spawn quite a bit to make massive pillars full of holes that connects the ceiling and floor!

	Mobs:
• If you drink a Honey Bottle, pick up a Honey Block in the dimension, take honey from a Filled Porous Honeycomb block, all bees within 64 range of you will become EXTREMELY aggressive towards you. Beware, the bees will be REALLY angry and will get a temporary Strength 3, Absorption 2, and Speed 1 effect!
• Bees generate at a high rate in the dimension and will become aggressive towards Polar Bears and Pandas if the player somehow gets them into the dimension. The bees will get a temporary Strength 1 and Speed 1 effect.
• Spiders, Endermen, and Phantoms will spawn at a low rate with Phantoms being extremely rare.
• Slime will spawn at any height when a new chunk is created for the first time. However, they will only respawn in an already generate chunk if that chunk is marked as a "slime chunk" and at Y = 40 or below just like the Overworld.

	Config:
• Added 11 configs with 8 affecting the Wrath of the Hive effect and 3 affecting the Bumblezone dimension itself. Here is what the config looks like: https://gist.github.com/TelepathicGrunt/0ad76feeb0bee1fc7eeba25d5f0821b4