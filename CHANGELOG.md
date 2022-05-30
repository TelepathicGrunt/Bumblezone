### **(V.5.1.3 Changes) (1.18.2 Minecraft)**

##### Items:
Bee Cannon fires bees slightly lower so they do not block the entire player vision briefly.


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

Fixed Stinger Spear having backwards item model when viewed in 3rd person and about to throw.

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

NOTE: This will be the last version that uses Cloth Config. Future versions of Bumblezone will use a new config api instead.
 This is done because Cloth Config use is heavily discouraged by its creator 
 but I'll try and see if I can switch to new config api without losing your old configs settings.