### **(V.7.8.7 Changes) (1.21.1 Minecraft)**

#### Features/Structures:
Reduced most logspam about `Trying to set block entity`. Finally, found the feature and processor that was not properly removing block entity data when replacing the block during worldgen.

Attempted to mitigate a bias in vanilla's random number generator so that spawner's chosen mob is more random.
 Cave Spider spawners should show up properly in Spider Infested Bee Dungeons

#### Mod Compat:
Heavily reduced logspam about `Trying to set block entity` when Productive Bees is on.


### **(V.7.8.6 Changes) (1.21.1 Minecraft)**

#### Structures:
Fixed Bumbling Beepartment's not spawning Beehemoths. (Stops the logspam about it too)

Fixed Honey Cocoons in Bumbling Beepartments and a few other structures getting Ender Chest-like behavior if Lootr is on.
 This was due to blockEntityUuid field accidentally saved into the Honey Cocoon for the structures.
 The fix will apply to new ungenerated chunks. Will not apply to previously generated cocoons in this structure.


### **(V.7.8.5 Changes) (1.21.1 Minecraft)**

#### Entities:
Fixed a possible deadlock issue with spawning Rootmins in Floral Meadow biome.


### **(V.7.8.4 Changes) (1.21.1 Minecraft)**

#### Blocks:
Fixed an item duplication bug with Crystalline Flower that was not reported for a long while until recently.
 Seems it was around for a while in 1.21.1 Bumblezone...

#### Advancements:
Putting Bee Stingers back onto bees using Buzzing Briefcase's UI will now count towards the Back in Action advancement.

Fixed Unbeelievably Ineffective! advancement not triggering when reflecting Ghast's fireballs with a thrown Pollen Puff.


### **(V.7.8.3 Changes) (1.21.1 Minecraft)**

#### Structures:
Fixed Bumbling Beepartment's Recreational Center room not properly randomizing the flowers in it.

#### Entities:
Feeding Royal Jelly Bottle to Beehemoths will give them an infinite duration of level 4 Beenergized effect!
 Extra speed but now lasts forever unless you somehow wipe the effect off the mob.

Feeding Royal Jelly Bucket to Beehemoths will give them an infinite duration of level 5 Beenergized effect!
 Super speed but now lasts forever unless you somehow wipe the effect off the mob.

#### Advancements:
Made the two advancements that require paralyzing enemies with Stinger Spear to say that the spear needs Neurotoxin enchantment.

#### Mod Compat:
Honey Fluid and Royal Jelly Fluid, when they touch Lava, will now spawn Create's Limestone block! This automatic compat can be disabled in config by turning off `allowCreateLimestoneForHoneyLavaCompat`

Royal Jelly Bucket and Honey Buckets now work with Create's Deployers to spawn Glistering Honey Crystal when attempting to use the buckets in Nether. Sugar Water Bucket won't spawn Sugar with the Deployers right now due to an oversight in Create: https://github.com/Creators-of-Create/Create/issues/7956

#### Misc:
(Fabric): Switched to using UseBlockCallback.EVENT to determine if certain Bumblezone player actions are permitted at a spot (respecting chunk claims for example)


### **(V.7.8.2 Changes) (1.21.1 Minecraft)**

#### Structures:
Allow Sempiternal Sanctum arenas to have blocks that require support to safely added without it popping off when arena is removed.

Fixed bug that prevented many structures from spawning as frequently as they should've been.

Added 40 extra seconds to complete the Blue Sempiternal Sanctum's arena.

Added 20 extra seconds to complete the Red Sempiternal Sanctum's arena.

Added difficulty scaling to Blue and Red Sempiternal Sanctum arenas where if the player kills the enemies too fast, the enemies will get increasingly more health and attack damage!
 This should make it harder to cheese these arenas with overpowered modded armor and weapons. Not perfect but should slow down the player's fast progress.
 Player using vanilla-balanced gear should see mostly no change to the difficulty as it is already difficult enough as is for vanilla gear.

Red Sempiternal Sanctum arena's Magma Cubes now always spawn at large size. Eliminating the large size Magma Cubes is all that is needed to progress so no longer need to hunt down which tiny Magma Cube is required to count towards the arena progress.


### **(V.7.8.1 Changes) (1.21.1 Minecraft)**

#### Entities:
Allow Holiday Trades to be shown in the speech bubble for Bee Queen.
 Also fixed eggs being thrown when right click trading with Bee Queen on Easter.

#### Features:
Slightly reduce spawnrate of Hive Voyager and Ore Balloon features.

#### Items:
Added a change to Bee Stinger to avoid cases where Supplementaries's Quiver make shooting Bee Stinger and then picking it up again returns a stack of Bee Stingers greater than 1.

#### Blocks:
Added code to Potion Candles to now dynamically increase their linger times and delaying refreshing of the effect's duration for effects that apply on an interval.
 For example, Regeneration only apply the heal when the effect reaches certain duration numbers. Now the Potion Candle should automatically find those thresholds and allow effects to actually work when standing near the lit candle.
 This fix should apply retroactively to existing placed Potion Candles.


### **(V.7.8.0 Changes) (1.21.1 Minecraft)**

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