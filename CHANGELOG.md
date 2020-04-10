# Made for Minecraft v.1.15.2

## Created by TelepathicGrunt

Welcome to the Github! If you are looking for the most recent stable version, then checkout the master branch! Branches dedicated to the latest version of Minecraft may be unstable or broken as I test and experiment so stick with the master branch instead.

An awesome dimension full of bees that becomes REALLY angry if you take their honey! 
Enter the dimension by throwing an enderpearl at Bee Nest and exit it by going above Y = 256 to below Y = 0.

------------------------------------------------
# | Bumblezone changelog |


   
## (V.1.2.0 Changes) (1.15.2 Minecraft)
      
##### Teleportation: 

-Throwing Enderpearls at all blocks that extends BeehiveBlock (includes modded and vanilla's BeeNests and BeeHive blocks) will teleport you into the dimension!
   
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