### **(V.7.0.0 Changes) (1.12.0 Minecraft)**

##### Major:
Updated to 1.20!


#### *\*Additions*

##### Blocks:
Added Ancient Wax blocks! Very high blast resistance, uncraftable, and will give Slowness, Weakness, and Mining Fatigue to 
 anyone who has not consumed Essence of the Bees before and are standing on said Ancient Wax blocks. You can use shears to 
 convert between 3 different patterns for these blocks. Only found in Sempiternal Sanctum. Can be crafted into stairs or slabs.
 Stonecutter has more efficient recipe for making stairs/slabs.

Added Luminescent Wax Channel blocks and Luminescent Wax Node blocks! Very high blast resistance, uncraftable, and will 
 give Slowness, Weakness, and Mining Fatigue to anyone who has not consumed Essence of the Bees before and are standing 
 on said Ancient Wax blocks. If the block has a light in it, people who consumed Essence of the Bees before and standing 
 on said blocks will get Speed, Damage Resistance, and Beenergized. The light forms will repel Piglins and Hoglins and 
 also will count towards empowering Enchanting Table like Bookshelves do. You can use shears to change the direction of these blocks.
 Only found in Sempiternal Sanctum.

Windy Air added that will push any entity in whatever direction the air block is blowing! Only found in certain structures.
 Can be found in Creative Menu for builders!

Heavy Air added that will pull entities down and remove Levitation, Slow Falling, Jump Boost, and non-creative flying! 
 Has no effect on Bees and Beehemoth. Only found in Sempiternal Sanctum. Can be found in Creative Menu for builders!

##### Items:
Essence of Life - The reward for beating the Yellow Sempiternal Sanctum structure! Holding this item in offhand will grow 
 crops and saplings nearby, heal mobs tamed by you, and heal other mobs or players on the same team as you! It has 1000 use 
 before it is depleted and needs 10 minutes to recharge by being in the inventory of a player that had consumed Essence of the Bees in the past.

Essence of Radiance - The reward for beating the Green Sempiternal Sanctum structure! Holding this item in offhand while being
 in a spot with sky brightnes 13 or above (basically in view of the sky) will grant you Regeneration 1, Saturation 1, Speed 1,
 Haste 2, and Damage Resistance 2. It will also slowly heal your armor durability as well over time! It has 4800 use before it is 
 depleted and needs 10 minutes to recharge by being in the inventory of a player that had consumed Essence of the Bees in the past.

Essence of Knowing - The reward for beating the Purple Sempiternal Sanctum structure! Holding this item in offhand will highlight mobs
 and outline certain block entities near you! Allowing you to see what monster is nearby or hidden chests or suspicious blocks! It has 
 1200 use before it is depleted and needs 15 minutes to recharge by being in the inventory of a player that had consumed Essence of the Bees in the past.
 This highlighting can be configured on client side by config. Server owners can use the block and entity tags to force things to be highlighted
 or not regardless of what the client config is set as. (The tags can be used to also correct highlighting issues on other mod's mobs with this item on)

Essence of Continuity - The reward for beating the White Sempiternal Sanctum structure! Holding this item in offhand will make dying
 not kill you and instead teleport you back to your respawn point with all health restored, hunger bar filled, neutral and 
 negative status effects removed, and fire removed. It only has 1 use before it is depleted and needs 4 hours to recharge 
 by being in the inventory of a player that had consumed Essence of the Bees in the past. It will set cooldown on all other
 Essence of Continuity that player has as well due to how powerful this item is. It even works in hardcore worlds!


#### *\*Changes*

##### Blocks:
Walking through Pile of Pollen should not trigger Sculk Sensor now. Pile of Pollen will also block Sculk Sensor's detection if between it and the sound source.

String Curtains should properly block Sculk Sensor's detection if between it and the sound source.

Fluids should not show drip animations under Glistering Honey Crystal now.

Snow should now be placeable on Royal Jelly Block. 

Swords are now the efficient tools for breaking Honey Web and Redstone Honey Web blocks.

Axes are now the efficient tools for breaking Beehive Beeswax blocks.

Several Bumblezone's full solid blocks has instruments set for Note Blocks to play when under Note Blocks.

Crystalline Flower can be broken by pistons now and drop itself.

Glistering Honey Crystal should not suffocate mobs inside itself, and it blocks Redstone power like how Glowstone blocks power.

Any Super Candle can be crafted to another color with 2 dyes now!

Any String Curtain can be crafted to another color with 1 dye now!

##### Items:
Added Stinger Spear, Bee Cannon, and Crystal Cannon to the `minecraft:tools` tag.

Pollen Puff will not multiply Pitcher Plant when thrown at it as Mojang did not want it to be too easy to multiply (Pitcher Plant cannot be bonemealed)

##### Fluids:
(Fabric/Quilt): Fixed Honey Fluid and Royal Jelly Fluid not rendering the fluid texture on the far side when looking through the fluid.

##### Enchantments:
Fixed Comb Cutter bypassing Mining Fatigue effect like it didn't exist.

##### Entities:
Honey Slime is now immune to fall damage like Magma Cubes. The honey protects the Slime!

Fixed dimensions for baby Honey Slimes.

Fixed Bee Queen able to be leashed when it shouldn't.

Beehemoth now starts with 40 health points and will get more health as friendship increases! Maxes out at 60 health points.

Tamed Beehemoths will show friendship amount above when owner is looking at it.

Added beehemothFriendlyFire config option, so you can disable accidentally hurting your flying friend! Set to true by default to allow owners to kill their Beehemoth by attacking.

Fixed bug where Beehemoths always fly straight up if below sealevel.

Fixed Leash rendering for Beehemoth.

##### Structures:
Hanging Gardens now can spawn Torch Flower, Pitcher Plant, Pink Petal, Cherry Leaves, Cherry Logs, and Cherry Saplings!.

Cell Maze now will always have 1 Throne locating Honey Compass always in its center start room.
 The structure can also now have a Mystery locating Honey Compass

Honey Compasses to Mystery Structures now will try to point to different structures even if you got multiple compasses from the same structure's loot.

Lesser Mystery Honey Compasses are added to several structure's loot where these compasses point to smaller Bumblezone structures.

##### Biomes:
Added Bumblezone biomes to `minecraft:snow_golem_melts` biome tag to make Snow Golems melt in Bumblezone since beehives are roughly 95 degrees!

Added Bumblezone biomes to `minecraft:without_wandering_trader_spawns` biome tag to make extra sure Wandering Traders do not spawn in Bumblezone.

##### Dimension:
Bumblezone dimension is slightly darker now to make lighting pop better from light giving blocks.

##### Mod Compat:
Added tag translations for Bumblezone item tags so that EMI mod can display them.

#TODO: add recipe view descriptions of new blocks and items.