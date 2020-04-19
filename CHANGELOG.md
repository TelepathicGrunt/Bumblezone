# Made for Minecraft v.1.15.2

## Created by TelepathicGrunt

Welcome to the Github! If you are looking for the most recent stable version, then checkout the master branch! Branches dedicated to the latest version of Minecraft may be unstable or broken as I test and experiment so stick with the master branch instead.

An awesome dimension full of bees that becomes REALLY angry if you take their honey! 
Enter the dimension by throwing an enderpearl at Bee Nest and exit it by going above Y = 256 to below Y = 0.

------------------------------------------------
# | Bumblezone changelog |


   
## (V.1.2.0 Changes) (1.15.2 Minecraft)
      
##### Mod Compatibility: 

-If Buzzier Bees is on, Honey Slime mobs will spawn in The Bumblezone instead of regular Slime mobs and monsters will have a slightly higher rate of spawning.

-If Buzzier Bees is on, the roof and floor boundaries of the dimension will use Hive Planks instead, the top of land above sea level area will use Crystallized Honey Block, and the top layer of land at and below sea level area will use Wax Blocks.

-If Buzzier Bees is on, their Honey Wand now can add and remove honey from Porous Honeycomb Block and Honey Filled Porous Honeycomb Block.

-If Beesourceful is on, their ore bees and ender bees can now spawn in The Bumblezone! They have a 15% chance of spawning when a regular bee is spawned. Then the chance of each type of Beesourceful bee being chosen is 1% Ender, 2% Diamond, 7% Emerald, 10% Lapis, 10% Quartz, 20% Gold, 20% Redstone, and 30% Iron.

-If Beesourceful is on, their honeycomb variants will now in The Bumblezone at all kinds of heights and height bands. Start exploring to find where they spawn!
        
##### Config:        
        
-Added configs to control a lot of the interaction between this mod and Buzzier Bees.

-Added configs to control the interaction between this mod and Beesourceful.

-Added new config options to let players specify if a certain block needs to be under the Bee Nest/Beehive to teleport to The Bumblezone dimension.
 
-Added new config so players can allow or disallow teleporting to and from Bumblezone with modded Bee Nests or modded Beehive blocks.

##### Teleportation: 

-Throwing Enderpearls at all blocks that extends BeehiveBlock (includes modded and vanilla's BeeNests and BeeHive blocks) will teleport you into the dimension!

-Adjusted teleporting so teleporting to Bumblezone dimension from underground will spawn you trapped underground in the dimension less often.
 
##### Mobs: 

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

##### Blocks: 

-Added Sugar Water fluid to spawn in The Bumblezone instead of regular water! Swimming in this fluid is slightly slower than swimming in water due to the excess of sugar in it. If bees swim in the Sugar Water and aren't taking damage, the bees will sip the water and heal themselves very slowly. Also, any Sugar Cane placed next to a Sugar Water block will grow faster and taller (up to 5 blocks high!)

-Sugar Infused Stone and Sugar Infused Cobblestone will be made when Sugar Water touches Lava! When mined, they drop Sugar and Cobblestone but you can use Silk Touch to keep them as is. When put into a Furnace, you can quickly burn out the Sugar from the blocks for some quick cheap XP.
   
-Walking on Filled Porous Honeycomb Block will now slightly slow your movement due to the sticky honey.
          
-Honeycomb Larva Block is now added! They will grow a larva inside it through 4 stages and on the final stage, it will spawn a bee if there is less than 10 bees within 50 blocks of the block. The larva will grow faster in The Bumblezone dimension than any other dimension. You can use a Honey Bottle or Buzzier Bee's Sticky Honey Wand to speed up the larva growth or by using a Dispenser with Honey Bottles on the block. If you use a Glass Bottle or Buzzier Bee's Honey Wand, it will kill the larva but you get honey and a bunch of angry bees coming! If Buzzier Bees is on, it may also sometimes spawn a Honey Slime mob. And if Beesourceful is on, it can sometimes spawn a Beesourceful's resource bee mob.
  
-Added Dead Honeycomb Larva Block that is created when using a Glass Bottle on a Honeycomb Larva Block. This block is decorative and serves no other use. It's dead. :(  Buuuut if you have Buzzier Bee's on, you can use a Bottle of Bee to revive the block back to an alive Honeycomb Larva Block at stage 1. If you use Potion of Bee's bee potion items, that too can revive the block but it will be at stage 1, 2, or 3 as a bonus! This behavior can be done by hand or by using a Dispenser with these items in it right up against the Dead Honeycomb Larva Block.

##### Items: 

-Added Sugar Water Bottle! To get this, use an empty Glass Bottle on Sugar Water. If you drink a Sugar Water Bottle, you restore a tiny bit of hungry but get Haste 1 effect for a short period of time!

##### Generation: 

-The dimension uses Air instead of Cave Air to help with feature generation.

-Honeycomb Holes now only generate exposed to the air and not completely buried underground.

-Honeycomb Holes now will generate with Honeycomb Larva Block at various stages in its life cycle!

## (V.1.1.0 Changes) (1.15.2 Minecraft)
      
##### Misc: 
-Fixed potential crash at startup when in a foreign language.
   
## (V.1.0.4 Changes) (1.15.2 Minecraft)
    
##### Teleportation: 
-Improved some edge cases with Teleportation and fixed some potential bugs with other edge cases.

##### Config: 
-lower Absorption default config value from 2 to 1 due to 2 making it way too hard to kill Wrath of the Hive angered bees. You'll still need Bane of Arthropods.

##### Mobs: 
-Bees that spawn in a chunk when the chunk is first created has a 20% chance of being pollinated bees.
-Patched a bug that could spawn mobs underground and hurt performance as they suffocate. 

##### Dimension: 
-Fixed potential visual bug that causes blue fog in some situations.
       
       
## (V.1.0.3 Changes) (1.15.2 Minecraft)
  
##### Blocks: 
-Fixed bug so using Glass Bottle or Honey Bottles on the Filled Porous Honeycomb blocks and Porous Honeycomb blocks while in creative mode won't use up the bottle in your hand.

       
## (V.1.0.2 Changes) (1.15.2 Minecraft)
  
##### Teleportation: 
-Fixed teleportation math and player previous dimension storing to not allow a bug that lets you reach world border in seconds. Big oops. That's a BIG BUG I missed despite lots of testing! 
  	
       
## (V.1.0.1 Changes) (1.15.2 Minecraft)
  
##### Teleportation: 
-Fixed teleportation not working when trying to enter The Bumblezone from a non-Overworld dimension.

##### Config: 
-Added config option to make exiting The Bumblezone always place you into the Overworld.

       
## (V.1.0.0 Changes) (1.15.2 Minecraft)
    
##### Major: 
-FIRST RELEASE OF THIS MOD

##### Teleportation: 
-Throw an Enderpearl at a Bee Nest in any dimension to enter The Bumblezone dimension!
-To exit The Bumblezone dimension, go to Y = -1 or Y = 256 and it will place you back to the dimension you originally came from and if it can't resolve what dimension you came from, you'll be placed back in the Overworld. 
-Your XZ coordinates will be scale going to and from the dimension. In fact, traveling 1 block in The Bumblezone is the same as traveling 10 blocks in the Overworld!
-If you exit the dimension by going down through the floor, it will look for the lowest Bee Nest that is in the general area of your new scaled coordinates and will place you next to it. If no Bee Nest is found, it will place you on highest place and generate a Bee Nest at your feet.
-If you exit the dimension by going down through the ceiling, it will look for the highest Bee Nest that is in the general area of your new scaled coordinates and will place you next to it. If no Bee Nest is found, it will place you on highest place and generate a Bee Nest at your feet.

##### Blocks: 
-Porous Honeycomb block is added. If you use a Honey Bottle on this block, it'll consume the honey and turn into a Filled Porous Honeycomb block.
-Filled Porous Honeycomb block is added. If you use a Glass Bottle on this block, you'll get a Honey Bottle and the block turn into a Porous Honeycomb block (this will REALLY anger bees around you in the Bumblezone dimension!).

##### Biomes: 
-Hive Wall biomes will be the giant flat vertical slabs filled with holes that the bees dug out. In each hole is some Honey Blocks! 
-Between the Hive Wall biomes is the Sugar Water Floor biome which creates a water filled space between each honeycomb wall. Bring a boat!
-Also Hive Pillar biomes will spawn quite a bit to make massive pillars full of holes that connects the ceiling and floor!

##### Mobs: 
-If you drink a Honey Bottle, pick up a Honey Block in the dimension, take honey from a Filled Porous Honeycomb block, all bees within 64 range of you will become EXTREMELY aggressive towards you. Beware, the bees will be REALLY angry and will get a temporary Strength 3, Absorption 2, and Speed 1 effect!
-Bees generate at a high rate in the dimension and will become aggressive towards Polar Bears and Pandas if the player somehow gets them into the dimension. The bees will get a temporary Strength 1 and Speed 1 effect.
-Spiders, Endermen, and Phantoms will spawn at a low rate with Phantoms being extremely rare.
-Slime will spawn at any height when a new chunk is created for the first time. However, they will only respawn in an already generate chunk if that chunk is marked as a "slime chunk" and at Y = 40 or below just like the Overworld. 

##### Config: 
-Added 11 configs with 8 affecting the Wrath of the Hive effect and 3 affecting the Bumblezone dimension itself. Here is what the config looks like: https://gist.github.com/TelepathicGrunt/0ad76feeb0bee1fc7eeba25d5f0821b4