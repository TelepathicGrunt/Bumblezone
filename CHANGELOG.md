### **(V.7.3.4 Changes) (1.20.4 Minecraft)**

##### Mod Compat:
Passed owner of thrown Pollen Puff to FakePlayers so claim owners can throw Pollen Puff to place blocks in their own claims.


### **(V.7.3.3 Changes) (1.20.4 Minecraft)**

##### Fluids:
Fixed side texture being squished for Honey Fluid and Royal Jelly Fluid when they are flowing downward and not yet a full block.

(NeoForge): Removed hacky mixins for Bumblezone's fluids and now uses NeoForge's new event for fluid rendering.
 Requires NeoForge 20.4.99-beta or newer now.

##### Bee Aggression:
Spider/Bear-like entities that are OwnableEntities that spawn into Bumblezone's dimension will not get attacked right away if the entity is owned by a player.


### **(V.7.3.2 Changes) (1.20.4 Minecraft)**

##### Entities:
Made Green Sempiternal Sanctum event Rootmin now not knock off equipped armor that has Curse of the Binding or is in `the_bumblezone:essence/life_arena/armor_cannot_be_knocked_off` item tag.

##### Effects:
Getting Protection of the Hive now spawns a bit of particles at player head to signal the buff has been refreshed.

Fixed Protection of the Hive being granted at multiple levels when it is only supposed to be level 1.

##### Structures:
Spider Infested Bee Dungeon is now set to spawn more often by changing default config value for them.
 Edit your configs to set spiderInfestedBeeDungeonRarity to 5 if Bumblezone config file is already made.

1/5th of the Battle Cube structure's spawners will now be Cave Spider instead of Spider spawners.

##### Mod Compat:
(Fabric): Fixed potential crash with Sodium


### **(V.7.3.1 Changes) (1.20.4 Minecraft)**

##### Structures:
Adjusted rates of some statues in Sempiternal Sanctum

Removed Mining Fatigue from White and Green Essence arenas because some *certain* mods messes with player attack speed when mining fatigue is on.

Dance Floor structure now has a bigger variety of music discs it can have.

##### Entities:
Cosmic Crystal Entity now overrides heal to make it do nothing. Should help stop *certain* other mods form healing this boss which makes fight impossible.

Cosmic Crystal Entity now rejects Regeneration, Heal, and Absorption status effect to stop *certain* other mods from healing the boss.

Dirt Pellet hitbox size doubled and increase range for hitting the projectile back to sender.

##### Items:
Updated JEI/EMI/REI description for Honey Bucket and Royal Jelly Bucket.

##### Mod Compat:
Fixed Honey Fluid/Royal Jelly Honey Fluid not rendering their flowing correctly when Sodium or Embeddium v0.2.16+ is on.


### **(V.7.3.0 Changes) (1.20.4 Minecraft)**

##### Major:
(NeoForge): Updated NeoForge version to work with the new networking code. NeoForge v20.4.80 or newer is now required.

##### Structures:
Added more rooms to Cell Maze and Sempiternal Sanctums! Special thanks to Tera for the new pieces!

Made caves not carve into Cell Maze structures.

Made Tree Dungeons only place pillars of dirt down about 10 blocks instead of forever downward.

Fixed some Luminescent Wax blocks facing wrong way in Sempiternal Sanctums.

##### Blocks:
Changed Crystalline Flower so that it sorts enchantments on clientside based on the actual translated names of enchantments.
 Will be much easier to find the enchantment you want.

Added two new entity type tags for marking entities as immune to the slowdown effects from Sticky Honey Residue and Honey Web blocks:
 `the_bumblezone:honey_web/cannot_slow`
 `the_bumblezone:sticky_honey_residue/cannot_slow`

Fixed Super Candle unlit wick being tilted.

Fixed Honey Fluid and Royal Jelly Fluid having a too bright overlay when inside it at night in Overworld.

Fixed Honey Fluid and Royal Jelly Fluid making world too dark when going in and out of the fluid while hiding HUD.

Honey Fluid and Royal Jelly Fluid now will flow faster in warmer biomes! And slower in really cold biomes.

Fixed Honey Fluid and Royal Jelly Fluid bucket able to place the fluid in ultrawarm dimensions like the Nether. Now they turn into Glistering Honey Crystal block.

(Fabric): Fixed Honey Fluid fog color being wrong in certain dimensions and applying thick fog too early before fully submerged.

##### Items:
Fixed it so that Honey Compasses locked to a structure will ignore y value difference when showing distance to target when advanced tooltips is on.

Essence of Life now grows Sugar Cane, Cactus, Bamboo Sapling, and Cocoa Beans! It'll revive Dead Bush into a random Sapling! (Minus Mangrove and Dark Oak saplings)
 New tags for controlling all this. All blocks must be in `the_bumblezone:essence/life/grow_plants` for any reviving or growing to activate:
 `the_bumblezone:essence/life/three_high_pillar_plant`
 `the_bumblezone:essence/life/is_dead_bush`
 `the_bumblezone:essence/life/dead_bush_revives_to`
 `the_bumblezone:essence/life/force_disallowed_dead_bush_revives_to`

##### Entities:
Fixed Sentry Watcher movement being stuttery. Mojang made Entity not lerp movement by default... Had to add back lerping for Sentry Watcher.

Added quarterTurns option to Sentry Watcher nbt. Set this in the entity to true or 1b for the entity to do quarter turns instead of half turns.

Fixed Purple Spike inflicting poison effect that doesn't remove itself when its effect timer runs out.

##### Teleportation:
Fixed infinite loop if teleporting to and from Bumblezone is cancelled by another mod or script.

##### Lang:
Lowercased some stuff in ru_ru to match vanilla Russian casings (Thanks to SwayMinin for this PR)

##### Mod Compat:
(Fabric): Spectrum Jetpacks's jetpacks will now be disabled when in Heavy Air block

