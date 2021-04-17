    Made for Minecraft v.1.16.5
    Created by TelepathicGrunt

A Fabric pot of an awesome dimension full of bees that becomes REALLY angry if you take their honey! 
Enter the dimension by throwing an enderpearl at Bee Nest and exit it by going above Y = 256 to below Y = 0.

------------------------------------------------
       | The Bumblezone Fabric changelog |

   (V.2.3.1 Changes) (1.16.5 Minecraft)

   Dependencies:
- Cloth Config is no longer JIJ (jar-in-jar) into Bumblezone. You will need to download that mod separately.

  Dimension:
- The loud Bee buzzing sound was replaced with a much softer beehive buzzing! Easier on the ears!

- Music will play when you are in a Bumblezone Biome! The song that plays is Honey Bee by Rat Faced Boy.

  Effects:
- Wrath of the Hive now plays music when triggered! The song that plays is a midi version of Flight of the Bumblebee by Rimsky Korsakov.

- Wrath of the Hive's default config value is now a full minute and angry bee's strength config is lowered by two.

- When you have Wrath of the Hive, bees will now spawn in open space a bit away from you but will come chase you down! Grab your Bane of Arthropod sword!

- Bees now cannot get Wrath of the Hive effect to prevent them attacking each other.
  
- Mobs with Wrath of the Hive will now make Honeycomb Brood Blocks near them grow faster. Stay out of Bee Dungeons when you have the effect!

- Protection of the Hive's default config value for duration is now 1680.

  Enchantments:
- Added Comb Cutter enchantment for Shears and Swords. 
  This will make mining all blocks with "comb" in the name much faster!
  And it will slightly increase mining speed for Hive, Nests, and Wax based blocks too.
  It will also increase the number of combs you get when shearing a vanilla Bee Hive / Bee Nest!

   Entities:
- Using Honey Blocks on vanilla Slime mobs that are size 1 or 2 will turn them into Honey Slime mobs!
  "the_bumblezone:turn_slime_to_honey_slime" item tag controls what item can do the conversion.
  
- Honey Slime now gets significant reduced fall damage when they are covered in honey!

   Blocks:
- Fixed Honey Crystal block so that it rotates and mirrors properly when loaded from nbt files.
  
- Switched to using TagRegistry.block in backend for block tags.

   Items:
- Honey Crystal Shield's valid repair items is now controlled by the item tag: "the_bumblezone:honey_crystal_shield_repair_items"

   Features:
- Optimized waterfall feature to use honeycombs_that_features_can_carve block tag.
  
- Bee Dungeon and Spider Infested Bee Dungeon code backend is significantly cleaned up and now uses processors to change blocks.

   Mod Compat:
- Added Charm support so now their candles spawn in Bee Dungeons and Spider Infested Bee Dungeons!

- Several Bumblezone Items are now used in Charm's BeeKeeper's trade offers!


   (V.2.3.0 Changes) (1.16.5 Minecraft)
       
   Mod Compat:
• Added compatibility with Bee Better.

   Blocks:
• Honeycomb Brood Blocks now spawn child Bees and child Honey Slime mobs.
     
   Items:
• Honey Slime Spawn Eggs now use better code practices in backend. 

   Teleportation:
- Throwing Enderpearl at Beehives will teleport you to dimension more consistently.
   

   (V.2.2.17 Changes) (1.16.4 Minecraft)
   
   Mod Compat:
• Added compat with AnvilFix so that Honey Crystal Shield can still be upgraded again and cost XP for balance.


   (V.2.2.16 Changes) (1.16.4 Minecraft) Quickfix
   
   Config:
• Changed broodBlocksBeeSpawnCapacity so that it is not multiplied by 10 anymore for bee limit. 
 If it is set to 50 now, then the Brood blocks will not spawn more bees if 50 bees already exist.


   (V.2.2.15 Changes) (1.16.4 Minecraft)
   
   Dimension:
• Vanilla Bees in The Bumblezone now have a new AI that makes them wander better, lag less, and not cluster on the ceiling anymore.
  Bee rates have been lowered a bit as well.

   Mixins:
• Prefixed all my accessor and invoker mixins due to this bug in mixins that could cause a crash with other mods for same named mixins.
 https://github.com/SpongePowered/Mixin/issues/430


   (V.2.2.14 Changes) (1.16.4 Minecraft)
   
   Blocks:
• Added broodBlocksBeeSpawnCapacity config option to allow users to change the automatic bee spawning mechanics of Honeycomb Brood Blocks.

   Teleportation:
• Fixed Teleportation mode 2 and 3 being broken and not saving previous pos and dims.

   Dimension:
• Falling out of Bumblezone dimension to teleport out shouldn't deal fall damage now.

• Added onlyOverworldHivesTeleports config option to allow people to make it only possible to enter The Bumblezone from the Overworld.


   (V.2.2.13 Changes) (1.16.4 Minecraft)
   
   Tags:
• Fixed tags so that they do not crash on servers.

   
   (V.2.2.12 Changes) (1.16.4 Minecraft)
   
   Misc:
• Fixed various serverside crashes.
 
   Dimension:
• Fixed Sugar Waterfalls only being placed in a single x/z column instead of spread out.

• Reduced Sugar Waterfall amount.

   Teleportation:
• Added blacklisted_teleportable_hive_blocks.json tag file that datapacks can override.
  Add hive blocks to here if you don't want them to allow teleportation to the Bumblezone dimension.

   Config:
• Removed the requiredBlockUnderHive config and instead, replaced it with the tag:
  the_bumblezone/tags/blocks/required_blocks_under_hive_to_teleport.json. Override
  this tag file with a datapack to change what blocks are needed under hives to allow
  teleportation.
  

   (V.2.2.11 Changes) (1.16.4 Minecraft)
   
   Effects:
• Adjusted Wrath of the Hive to apply effects to bees with the duration equal to
  the remaining Wrath of the Hive time on the target. 
  
• Fixed bees being able to see through walls to find spiders and bears and not
  being able to see through walls for players making bees extra angry.


   (V.2.2.10 Changes) (1.16.4 Minecraft)
   
   Items:
• Fixed particles and sounds being played twice for Empty Bucket, 
  Glass Bottle, and Honey Bottle when activated in a Dispenser.
  
    Backend:
• Some cleaning up the code. Now requires newer Fabric API and Fabric Loader.

• No longer Jar-in-Jar ModMenu. Was a mistake on my part lol.
    
    
   (V.2.2.9 Changes) (1.16.3 Minecraft)
   
    Dimension:
• Optimized the SurfaceBuilder and the caves a bit!
  The underwater block in the configured surfacebuilder json file
  was changed from the_bumblezone:porous_honeycomb_block to 
  the_bumblezone:filled_porous_honeycomb_block.
  
• Fixed bug where Honey Crystals could be floating from worldgen.
  
• Attempted to optimize cave code slightly. Cave shape changed a bit as a result.
 
    Teleportation:
• Fixed Enderpearls not being removed when thrown and Bee Nest and causing people to teleport
  back into The Bumblezone immediately when they leave it.

  
   (V.2.2.8 Changes) (1.16.3 Minecraft)
   
    Teleportation:
• Fixed bug where exiting The Bumblezone always put you in Overworld
  instead of the actual dimension you came from when entering BZ's dimension.

    Dimension:
• Cleaned up a tiny bit of code and special thanks to
  Pyrofab for updating Cardinal Component's usage in this mod!
  

   (V.2.2.7 Changes) (1.16.3 Minecraft)
   
    Dimension:
• Cleaned up the json format for the dimension's json file.

    Features:
• Adjusted Honey Crystals to make them spawn more often.


   (V.2.2.6 Changes) (1.16.3 Minecraft)
   
    Teleportation:   
• Fixed coordinate scaling when entering/leaving The Bumblezone dimension.

• Fixed a possible theoretical crash that could occur when messing with 
  adding/removing dimensions and attempting to teleport to/from The Bumblezone.
  
    Mobs:   
• Increased Bee and Honey Slime rates in the dimension as Lithium mod will 
  optimize the bee lag away.


   (V.2.2.5 Changes) (1.16.3 Minecraft)
   
    Lang:   
• Added Simplified Chinese translation from Samekichi! Thank you!

• Added missing lang entries for mod compatibility config entries. 

    Config:
• Significantly reduced the range of values for bee anger and 
  bee status effect intensity config options to allow the slider
  to actually let you pick good values now.
  
• The status effect configs now reflect their true level of intensity.
  1 is now the minimum value instead of 0 as 0 actually was level 1.
  Absorption is now at the correct level to give 4 extra health instead
  of 8 which made bees too hard to kill.
  
• Removed empty tooltips after updating autoconfigu library.


   (V.2.2.4 Changes) (1.16.2 Minecraft)
   
    Lang:
• Added translation for Honey Slime mob name.

• Portuguese translations added by Mikeliro! Thank you!

    Mod Compat:
• Added Mod Compatibility with Potion of Bees! Their potions can be 
  used to revive Empty Honeycomb blocks by hand or by Dispensers.
  https://www.curseforge.com/minecraft/mc-mods/potion-of-bees-fabric
  
• Moved a mixin that was conflicting with Carpet mod.
  As a result, Bees spawned in the Bumblezone will have a chance
  to be pollinated no matter how they are spawned in the dimension.
     
    Biomes:
• Attempted a workaround fix to prevent generated Bumblezone biomes
  from being replaced with the wrong biome due to a rare Mojang bug 
  that happens when removing other datapack biomes.
  

   (V.2.2.3 Changes) (1.16.2 Minecraft)
   
    Major:
• FIXED A SUPER WEIRD BUG THAT KILLED ALMOST ALL OTHER MODS
  WHEN A CERTAIN NUMBER OF MODS ARE PUT ON NEXT TO THE BUMBLEZONE.
  I'M SORRY!!! OTHER MODDERS, DO NOT CLASSLOAD DYNAMIC REGISTRY 
  IN YOUR MOD'S INITIALIZATION!!!

   (V.2.2.2 Changes) (1.16.2 Minecraft)
   
    Major:
• Fixed crash on servers.

    Mod Compatibility:
• Fixed crash when paired with another mod that registers unfinished mobs that crashes when created.
 
    Teleportation:
• Fixed message appear about wrong block under Bee Nest when throwing Enderpearls at any non-Bee Nest block.

   (V.2.2.1 Changes) (1.16.2 Minecraft)
   
	Config:
• Adjusted configs text to show description right away instead of in tooltips.
 
	Items:
• Fixed Honey Shield not having blocking animation.

   (V.2.2.0 Changes) (1.16.2 Minecraft)

	Major:
• Updated to 1.16.2 minecraft!

• Replaced Cotton Config with Autoconfigu + Cloth.

• Attempted to improve performance by removing a few mixins into tick methods.

    Blocks:
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

   (V.2.1.2 Changes) (1.16.1 Snapshot Minecraft)

	Misc:
• Fixed reloadable config causing server to not shut down.

   (V.2.1.1 Changes) (1.16.1 Snapshot Minecraft)

	Dimension:
• Adjusted terrain to try and make it slightly more open.

• Fixed game not warning player that the block under the bee nest is incorrect if the config specified a different block is needed under the bee nest to enter the Bumblezone.

	Entities:
• Honey Slime now can speed up getting their honey back from being on Honeycomb Brood Blocks.

	Misc:
• Fixed crash on dedicated servers due to running client sided code in Cave Sugar Waterfall code.


   (V.2.1.0 Changes) (1.16.1 Snapshot Minecraft)

	Config:
• Clarified time units in the duration config for Wrath of the Hive effect.

	Entities:
• Added Honey Slime! (Special thanks to Bagel for donating Honey Slime to The Bumblezone) Spawns naturally in The Bumblezone and can spawn from Honey Brood Blocks with bees.

• Slightly reduced amount of bees that spawn at chunk creation.

	Effects:
• Added Protection of the Hive! When active, anyone that attacks you will get Wrath of the Hive effect! You can get Protection of the Hive by feeding Bees or Honeycomb Brood blocks Honey Bottles or Sugar Water Bottles!

	Misc:
• Attempted reducing file size.

   (V.2.0.1 Changes) (1.16.1 Snapshot Minecraft)

	Backend:
• Fixed crash on server.

• Fixed log spam due to trying to do clientside mixins on serverside.

• License changed to LGPLv3

	Dimension:
• Raised cloud height to 1000 to hide clouds.


   (V.2.0.0 Changes) (1.16.1 Snapshot Minecraft)

	Backend:
• Updated to 1.16.1 Snapshot MC

 	Dimension:
• Due to JSON formatted dimensions, some dimension stuff or configs may had broke. Please let me know if they have.

 	Teleportation:
• Any block that extends BeehiveBlock now can be used for teleportation to The Bumblezone!

 	Config:
• Nerfed the default Absorption level that bees get when affected by the Wrath of the Hive status from 2 to 1. This is because absorption 2 made it way too hard to kill bees but at 1, its easier but you may still need Bane of Arthropod enchantments.


   (V.1.0.1 Changes) (1.15.2 Minecraft)

	Backend:
• Fixed crash due to a mixin not being compatible with Carpet mod.
       
       
   (V.1.0.0 Changes) (1.15.2 Minecraft)
    
	Major:
• FIRST FABRIC RELEASE OF THIS MOD
  
 	Mobs:
• Bees have a 20% of being full of pollen when they spawn nautrally in the Bumblezone dimension.

  Teleportation:
• Improved teleportation to make it place you on surface of water in Bumblezone if you were going to teleport underwater.
• This mod will treat all other non-Nether-like dimension as having a normal coordinate scaling (10:1 ratio of those dimensions's scale to Bumblezone's scale) and all Nether-like dimensions as having the nether scale (10:8 ratio of those Nether-like dimensions to Bumblezone's scale)

	Blocks:
• Fixed bug so using Glass Bottle or Honey Bottles on the Filled Porous Honeycomb blocks and Porous Honeycomb blocks while in creative mode won't use up the bottle in your hand.



And here's what has been done in the Forge version so you know what else this Fabric version has since everything is ported
------------------------------------------------
       | The Bumblezone Forge changelog |
       
       
   (V.1.0.2 Changes) (1.15.2 Minecraft)
  
  	Teleportation:
• Fixed teleportation math and player previous dimension storing to not allow a bug that lets you reach world border in seconds. Big oops. That's a BIG BUG I missed despite lots of testing!
  	
       
   (V.1.0.1 Changes) (1.15.2 Minecraft)
  
  	Teleportation:
• Fixed teleportation not working when trying to enter The Bumblezone from a non-Overworld dimension.

	Config:
• Added config option to make exiting The Bumblezone always place you into the Overworld.

       
   (V.1.0.0 Changes) (1.15.2 Minecraft)
    
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