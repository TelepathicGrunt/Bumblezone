### **(V.7.0.30 Changes) (1.20.1 Minecraft)**

##### Mod Compat:
Pollen Puff hitting Sickened Mushroom Cown from Wither Storm Mod will spawn Tainted Mushrooms nearby.

Wither Storm Mod's Sickened Spider will count towards Bumblezone's Too Many Legs advancement.

Wither Storm Mod's Sickened Spider spawners can spawn in Battle Cube and Spider Infested Bee Dungeons now.


### **(V.7.0.29 Changes) (1.20.1 Minecraft)**

##### Entities:
Honey Slime is no longer marked as Enemy. Golems won't attack them now.

##### Structures:
Spawn Honey Slime Ranch Villager much closer to the Loom, so it becomes a Shepherd more consistently

Replaced carpet in Honey Slime Ranch with rails to keep villager from trying to pathfind out of the house.

(Fabric/Quilt): Fixed Ancient Origins advancement not obtainable when in Sempiternal Sanctum.

##### Blocks:
Stop crash when brushing Suspicious Pile of Pollen when a mod sets the player held item to null which is wrong and dangerous. 
 Please find the mod that is setting hand item to null instead of using Itemstack.EMPTY like they should be doing. 
 You're going to have other issues too until you find the causer.

Made Bees and Rootmins immune to Ancient Wax/Luminescent Wax negative effects.

(Fabric/Quilt): Fixed Brood Blocks not growing faster when Wrath of the Hive player is nearby.

##### Misc: 
Fixed possible concurrent modification crash when iterating over effects in a few places.

(Forge): Undid Projectile Impact event result change to allow 47.1.3 Forge to run with Bumblezone again.

Some backend code cleanup regarding organization of goals for my entities.