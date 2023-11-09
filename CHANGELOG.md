### **(V.7.0.29 Changes) (1.20.1 Minecraft)**

##### Entities:
Honey Slime is no longer marked as Enemy. Golems won't attack them now.

##### Structures:
Spawn Honey Slime Ranch Villager much closer to the Loom, so it becomes a Shepherd more consistently

Replaced carpet in Honey Slime Ranch with rails to keep villager from trying to pathfind out of the house.

##### Blocks:
Stop crash when brushing Suspicious Pile of Pollen when a mod sets the player held item to null which is wrong and dangerous. 
 Please find the mod that is setting hand item to null instead of using Itemstack.EMPTY like they should be doing. 
 You're going to have other issues too until you find the causer.