### **(V.7.4.7 Changes) (1.20.1 Minecraft)**

##### Structures:
Made Purple Sempiternal Sanctum arena's Purple Spikes a bit more visible.

Mute the 24th and later Vexes in Yellow Sempiternal Sanctum's arena event to reduce chances of reaching sound limit.

##### Fluids:
Fixed when using a very high resolution texture pack, that other textures bleed into diagonal Honey Fluid textures as tiny squares.

##### Items:
Fixed Stinger Spear, fired Bee Stinger, and fired Honey Crystal Shards unable to break Decorated Pots properly.

##### Teleporting:
Send more packets when teleporting by Bumblezone. Might fix some sync issues?


### **(V.7.4.6 Changes) (1.20.1 Minecraft)**

##### Blocks:
Fixed broken Honey Cocoon textures when Anti-Trypophobia resourcepack is enabled.

##### Fluids:
Fixed other textures bleeding into diagonal Honey Fluid textures as tiny squares.

##### Structures:
Added 30 extra seconds to complete Yellow and Red Essence Arena events in Sempiternal Sanctums.

Arena victory rewards are now loot tables! You can add to or change rewards from completing Sempiternal Sanctums!
  `the_bumblezome:gameplay/rewards/red_arena_victory`
  `the_bumblezome:gameplay/rewards/yellow_arena_victory`
  `the_bumblezome:gameplay/rewards/green_arena_victory`
  `the_bumblezome:gameplay/rewards/blue_arena_victory`
  `the_bumblezome:gameplay/rewards/purple_arena_victory`
  `the_bumblezome:gameplay/rewards/white_arena_victory`


### **(V.7.4.5 Changes) (1.20.1 Minecraft)**

##### Entities:
Fixed Rootmins in Tree Dungeons not having a flower.

Changes Pyro bee spawned in structures to use the HasVisualFire boolean nbt field instead of actually burning.


### **(V.7.4.4 Changes) (1.20.1 Minecraft)**

##### Entities:
Fixed Rootmins spawned in Floral Meadow not using flowers from `the_bumblezone:biomes/floral_meadow_rootmin_flowers`.


### **(V.7.4.3 Changes) (1.20.1 Minecraft)**

##### Blocks:
Fixed crash when touching impossible no-side blockstate for Sticky Honey Redstone (debug world)

Fixed Honey Web causing log spam when touched in Debug World.

##### Entities:
Added `the_bumblezone:biomes/floral_meadow_rootmin_flowers` and `the_bumblezone:biomes/disallowed_floral_meadow_rootmin_flowers`
 block tags to allow changing what flowers Rootmins can spawn with in Floral Meadow without changing the flowers that are placed in that biome.


### **(V.7.4.2 Changes) (1.20.1 Minecraft)**

##### Entities:
Fixed breeding Honey Slime not counting towards the Bee Queen advancement for breeding them.

##### Dimension:
Added `the_bumblezone:fog_adjusting_effects` mob effect tag to allow specifying what mob effect changes the fog in Bumblezone.
 By default, this has Blindness and Darkness. This new addition now fixes those two to properly set the fog distance in Bumblezone dimension.


### **(V.7.4.1 Changes) (1.20.1 Minecraft)**

##### Items:
Fixed Life Essence not growing crops if laid out a certain way in relation to player.

##### Fluids:
Fixed fog and screen overlay for Honey Fluid/Royal Jelly Fluid when inside them below y = 0.


### **(V.7.4.0 Changes) (1.20.1 Minecraft)**

##### Music:
Added a new song by punpudle called Drowning in Despair!
 A music disc was made for it and the song will play for the Blue Sempiternal Sanctum's essence event!

##### Structures:
Fixed some typoes in structure processor files that would've caused the processor to not fully function correctly.

##### Entities:
Fixed Honey Slimes not turning to face each other when breeding.

Fixed issue where Beehemoth model was straighter than it should've been when in sitting pose.

##### Items:
Stingless Bee Helmets will not eject bee passenger from head if player is hurt or attacked anymore.

Honey Crystal Shield now does not get heavy damage from blocking fire sources if the defender has Fire Resistance effect on.

Fixed blocking damage with Honey Crystal Shield not incrementing the use stat for the shield.

##### Enchantments:
Fixed Neurotoxins not angering neutral mobs on hit with a weapon with this enchantment.

##### Dimension:
Fixed Sugar Water waterfalls not spawning as they should.

##### Lang:
uk_ua.json updated by Unroman!

zh_cn.json updated by TskimiSeiran!

##### Mod Compat:
Added special textures for Bumblezone Music Discs to be used when put into Amendments's Jukeboxes.
