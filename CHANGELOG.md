### **(V.7.6.3 Changes) (1.20.1 Minecraft)**

#### Mod Compat:
Honey Fluid and Royal Jelly Fluid, when they touch Lava, will now spawn Create's Limestone block! This automatic compat can be disabled in config by turning off `allowCreateLimestoneForHoneyLavaCompat`

#### Misc:
(Fabric): Switched to using UseBlockCallback.EVENT to determine if certain Bumblezone player actions are permitted at a spot (respecting chunk claims for example)


### **(V.7.6.2 Changes) (1.20.1 Minecraft)**

#### Structures:
Allow Sempiternal Sanctum arenas to have blocks that require support to safely added without it popping off when arena is removed.

Fixed bug that prevented many structures from spawning as frequently as they should've been.

Added 40 extra seconds to complete the Blue Sempiternal Sanctum's arena.

Added 20 extra seconds to complete the Red Sempiternal Sanctum's arena.

Added difficulty scaling to Blue and Red Sempiternal Sanctum arenas where if the player kills the enemies too fast, the enemies will get increasingly more health and attack damage!
 This should make it harder to cheese these arenas with overpowered modded armor and weapons. Not perfect but should slow down the player's fast progress.
 Player using vanilla-balanced gear should see mostly no change to the difficulty as it is already difficult enough as is for vanilla gear.

Red Sempiternal Sanctum arena's Magma Cubes now always spawn at large size. Eliminating the large size Magma Cubes is all that is needed to progress so no longer need to hunt down which tiny Magma Cube is required to count towards the arena progress.


### **(V.7.6.1 Changes) (1.20.1 Minecraft)**

#### Entities:
Allow Holiday Trades to be shown in the speech bubble for Bee Queen.
 Also fixed eggs being thrown when right click trading with Bee Queen on Easter.

#### Features:
Slightly reduce spawnrate of Hive Voyager and Ore Balloon features.

#### Blocks:
Added code to Potion Candles to now dynamically increase their linger times and delaying refreshing of the effect's duration for effects that apply on an interval.
 For example, Regeneration only apply the heal when the effect reaches certain duration numbers. Now the Potion Candle should automatically find those thresholds and allow effects to actually work when standing near the lit candle.
 This fix should apply retroactively to existing placed Potion Candles.


### **(V.7.6.0 Changes) (1.20.1 Minecraft)**

#### Structures:
Added a rare Bumbling Beepartments biome filled with Bumbling Beepartments structure!
 Mystery Honey Compasses might point to this biome rarely...

#### Features:
Added a Hive Voyager, a tiny helicopter piloted by an adventurous bee you can find throughout the dimension!
 The Honey Cocoon in it contains several Honey Compasses.
 If you had consumed Essence of the Bees before, this cocoon is guaranteed to have at least 1 Honey Compass pointing to a Sempiternal Sanctum.

Added an Ore Balloon feature. A tiny balloon holding 1 ore and a long burning Smoker!
 It can hold Coal Ore, Iron Ore, Lapis Ore, Redstone Ore, Gold Ore, Emerald Ore, or Diamond Ore. Some ores are more rare than others.

#### Entities:
Removed the Bonus Trade system from Bee Queen. (Special day trades on holidays are still available)
 Instead, when looking at the Bee Queen, she will show a speech bubble for a few seconds of a random trade you can do with her.
 Think of it like a miniature randomized recipe viewer in game. You can disable this speech bubble on client side by config.

#### Lang:
Mexican Spanish Translation added by TheLegendofSaram!