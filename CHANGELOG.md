### **(V.5.1.0 Changes) (1.18.2 Minecraft)**

##### Items:
Play the missing glass pickup sound when using Glass Bottles on Honey Fluid source blocks.

Making Honey Bucket and turning Honey Buckets into Honey Bottles now requires 4 bottles instead of 3.
 Prevents duplication bugs with other mods that assumes bottles are 1:4 ratio to buckets.

##### Armor:
Fixed issue where Bee armor pants and chestplate may not show the right animation/models based on itemstack nbt state.
 (My model cache wasn't correctly done)

Added Carpenter Bee Boots! These boots will automine many kinds of wood, beehive, honeycomb blocks that you are standing on when you hold crouch down!
 The boots can be enchanted with the normal boot enchantments but can also be enchanted with Efficiency, Silk Touch, and Fortune.
 To go with Efficiency, the boots also mines faster when you have the full bee armor set on or have Beenergized status effect. These speeds do stack.
 The boots will also let you briefly hang on walls made of wood, beehive, or honeycomb blocks allowing you to wall jump or wall run!
 Both of these behaviors are controlled by these two block tags that determine what to mine or what to wall hang on:
 `the_bumblezone:carpenter_bee_boots_climbables` and `the_bumblezone:carpenter_bee_boots_mineables`

##### Entities:
Added null world check to checking if bees should be angry at spawned entities. 
 Prevents crash with mods that create an entity with a null world. Don't ask why...

##### Config:
Added enableExitTeleportation and enableEntranceTeleportation config options to let
 players disable Bumblezone's teleportation methods into and out of the Bumblezone dimension.


