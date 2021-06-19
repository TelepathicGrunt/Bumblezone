### **(V.2.4.9 Changes) (1.16.5 Minecraft)**

##### Mod compat:
* Fixed crash if a person tries to disable Charm's BeeKeeper villager profession.

* Added two new configs to tailor mod compat with Charm better


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