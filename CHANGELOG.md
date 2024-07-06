### **(V.7.5.3 Changes) (1.20.6 Minecraft)**

##### Misc:
This is a backport of fixes. Mainly to fix crashes. However, support for 1.20.6 will be dropped unless there's more really bad crashes.

Fixed crash when starting up server due to classloading issues in packet initialization.

##### Enchantments:
Fixed Neurotoxins enchantment to not crash when used against another player or non-mob.

##### Advancements:
Fixed killed tracker advancements for Bumblezone internally crashing on entity kills.

##### Blocks:
Fixed broken Honey Cocoon textures when Anti-Trypophobia resourcepack is enabled.

Fixed Bumblezone blocks treating any enchantment like it is Silk Touch when mined.

Fixed missing particle texture when falling onto carpet or trapdoor that is over Bumblezone fluids.

##### Fluids:
Fixed when using a very high resolution texture pack, that other textures bleed into diagonal Honey Fluid textures as tiny squares.

Improve the interaction between Honey Fluids and Lava.

(NeoForge): Fixed Bumblezone fluids not giving correct bucket when using an empty bucket on them.

##### Items:
Fixed crash when Sugar Water Bucket is attempted to be used in Nether.

Fixed missing translations for Bumblezone Music Discs.

Fixed Buzzing Briefcase deleting bees when clicking any bee changing buttons.

##### Structures:
Added more time to complete Yellow and Red Essence Arena events in Sempiternal Sanctums.

Arena victory rewards are now loot tables! You can add to or change rewards from completing Sempiternal Sanctums!
 `the_bumblezome:gameplay/rewards/red_arena_victory`
 `the_bumblezome:gameplay/rewards/yellow_arena_victory`
 `the_bumblezome:gameplay/rewards/green_arena_victory`
 `the_bumblezome:gameplay/rewards/blue_arena_victory`
 `the_bumblezome:gameplay/rewards/purple_arena_victory`
 `the_bumblezome:gameplay/rewards/white_arena_victory`

Made Purple Sempiternal Sanctum arena's Purple Spikes a bit more visible.

Mute the 24th and later Vexes in Yellow Sempiternal Sanctum's arena event to reduce chances of reaching sound limit.

Changed how I detect structures within features. May resolve issues where game gets deadlocked/stuck during worldgen.

##### Teleporting:
Send more packets when teleporting by Bumblezone. Might fix some sync issues?

##### Configs:
Removed keepEssenceOfTheBeesOnRespawning config option as it is a bit too difficult to reimplement and that I don't think anyone set it to false.
 Basically, Essence of the Bees kept on respawn is now permanent default.
