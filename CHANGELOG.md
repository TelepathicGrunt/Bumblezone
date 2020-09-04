# Made for Minecraft v.1.15.2

## Created by TelepathicGrunt

Welcome to the Github! If you are looking for the most recent stable version, then checkout the master branch! Branches dedicated to the latest version of Minecraft may be unstable or broken as I test and experiment so stick with the master branch instead.

An awesome dimension full of bees that becomes REALLY angry if you take their honey! 
Enter the dimension by throwing an enderpearl at Bee Nest and exit it by going above Y = 256 to below Y = 0.

////
Mods I'm keeping an eye on for future compat
-https://www.curseforge.com/minecraft/mc-mods/carrier-bees
-https://www.curseforge.com/minecraft/mc-mods/the-bees-knees-mod
///

------------------------------------------------
# | Bumblezone changelog |
   
## (V.2.0.1 Changes) (1.16.2 Minecraft)
 
##### Major:

- Fixed crash on servers.

##### Mod Compatibility:

- Fixed crash when paired with another mod that registers unfinished mobs that crashes when created.
 
## (V.2.0.0 Changes) (1.16.2 Minecraft)
 
##### Major:

- UPDATED TO 1.16.2!! (Ported the 1.16.2 Fabric version to Forge)

- Removed compat with Buzzier Bees, Beesourceful, and Potion of Bees as they are not on 1.16.2 yet.

- Added Honey Slime mob natively to this mod which was donated by the Buzzier Bees dev Bagel! 

- Added Beeswax Planks block to be placed on the Bumblezone dimension's ceiling and floor boundaries.

- See Fabric Bumblezone changelog for more details of changes done in 1.16+.
 
## (V.1.3.9/1.3.10 Changes) (1.15.2 Minecraft)
 
##### Config:

-Added config to adjust Phantom and Endermen spawnrates in The Bumblezone. (Special thanks to wtchappell for the PR!)

-Added config to adjust Spider/Cave Spider in The Bumblezone.

-Fixed clearUnwantedBiomeFeatures and clearUnwantedBiomeMobs configs to actual work. (hopefully!)
 
## (V.1.3.8 Changes) (1.15.2 Minecraft)
 
##### Mod Compatibility:
 
-Changed Bumblezone's biome categories from Jungle to None so people can blacklist it from Quark's structures easier.

-Added an experimental option to reset Bumblezone's biomes after all mods are finished setup to try and let players remove other mod's features/structures/mobs out of Bumblezone.
   
## (V.1.3.7 Changes) (1.15.2 Minecraft)
 
##### Mod Compatibility:

-Added support for ProductiveBees's new honeycomb variants so they spawn in the dimension now!

-Bees can be fed and/or calmed with Buzzier Bees's Honey Soup or Sticky Honey Wand!

##### Recipes:
   
-Fixed bucket duplication when crafting Sugar Water from Water Bucket and Sugar in Crafting Table.
  
##### Bees:
   
-Feeding bees will swing your arm always now.
 
## (V.1.3.6 Changes) (1.15.2 Minecraft)
 
##### Recipes:

-Fixed and renamed several Bumblezone recipes to prevent conflict so that vanilla's Honey Block can be crafted again. (The smelting recipe overrode it before. Sorry!)
   
## (V.1.3.5 Changes) (1.15.2 Minecraft)
 
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
 
## (V.1.3.4 Changes) (1.15.2 Minecraft)
 
##### Teleportation:

-Setting warnPlayersOfWrongBlockUnderHive config to true and putting and invalid resource location into RequiredBlockUnderHive config will no longer crash the server when attempting to teleport to The Bumblezone.

-Added generateBeenest config to allow people to turn on or off the creation of Bee Nests when exiting The Bumblezone into an area with no nests or hives nearby.

-Added teleportationMode config to let players pick between spawning at converted coordinates always, spawning at original coordinates always, or a mix when exiting The Bumblezone.

-Constrained the converted coordinate when exiting The Bumblezone to be within -30 million and 30 million so you do not get stuck outside the world's edge.

## (V.1.3.3 Changes) (1.15.2 Minecraft)
 
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

## (V.1.3.2 Changes) (1.15.2 Minecraft)
 
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
  
## (V.1.3.1 Changes) (1.15.2 Minecraft)
 
##### Blocks: 

-Lower and smoothed out buzzing sound of Honeycomb Brood Blocks.
      
##### Items:

-Honey Crystal Shield description now says it can be enchanted with Curse of the Vanishing as Curse of the Binding is only for armor. 

##### Mod Compatibility:

-Added JEI integration. All of The Bumblezone's blocks and items now has a description page.

-Productive Bees's Honey Treat item can be used to grow Honeycomb Brood Blocks now. They have a 20% of growing the larva by 2 stages instead of 1.

## (V.1.3.0 Changes) (1.15.2 Minecraft)
    
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

## (V.1.2.0 Changes) (1.15.2 Minecraft)
      
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

## (V.1.1.0 Changes) (1.15.2 Minecraft)
      
##### Misc:�
-Fixed potential crash at startup when in a foreign language.
   
## (V.1.0.4 Changes) (1.15.2 Minecraft)
    
##### Teleportation:�
-Improved some edge cases with Teleportation and fixed some potential bugs with other edge cases.

##### Config:�
-lower Absorption default config value from 2 to 1 due to 2 making it way too hard to kill Wrath of the Hive angered bees. You'll still need Bane of Arthropods.

##### Mobs:�
-Bees that spawn in a chunk when the chunk is first created has a 20% chance of being pollinated bees.
-Patched a bug that could spawn mobs underground and hurt performance as they suffocate. 

##### Dimension:�
-Fixed potential visual bug that causes blue fog in some situations.
       
       
## (V.1.0.3 Changes) (1.15.2 Minecraft)
  
##### Blocks:�
-Fixed bug so using Glass Bottle or Honey Bottles on the Filled Porous Honeycomb blocks and Porous Honeycomb blocks while in creative mode won't use up the bottle in your hand.

       
## (V.1.0.2 Changes) (1.15.2 Minecraft)
  
##### Teleportation:�
-Fixed teleportation math and player previous dimension storing to not allow a bug that lets you reach world border in seconds. Big oops. That's a BIG BUG I missed despite lots of testing! 
  	
       
## (V.1.0.1 Changes) (1.15.2 Minecraft)
  
##### Teleportation:�
-Fixed teleportation not working when trying to enter The Bumblezone from a non-Overworld dimension.

##### Config:�
-Added config option to make exiting The Bumblezone always place you into the Overworld.

       
## (V.1.0.0 Changes) (1.15.2 Minecraft)
    
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
