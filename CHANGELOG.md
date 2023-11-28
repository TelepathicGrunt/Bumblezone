### **(V.7.1.5 Changes) (1.20.1 Minecraft)**

##### Mod Comapt:
(Fabric/Quilt): Worked around Porting Lib incompatibility that was breaking fishing in Bumblezone and preventing Bee Stinger drops from bees.


### **(V.7.1.4 Changes) (1.20.1 Minecraft)**

##### Blocks:
Fixed it so Suspicious Pile of Pollen can be brushed even if there is no solid block behind it.

Fixed Potion Candles granting effects 1 level higher than what tooltip says.

Luminescent Wax only drops its lit form if mined by a player that had consumed Essence of the Bees before.
 Otherwise, non-lit version is dropped.

Fixed nasty bug where shutting down the game would get stuck if Essence Block was loaded at any point.

##### Items:
(Fabric/Quilt): Fixed placing Sugar Water Bucket next to Sugar Cane not granting advancement.

##### Enchantments:
Comb Cutter now mines Ancient Wax and Luminescent Wax much faster.


### **(V.7.1.3 Changes) (1.20.1 Minecraft)**

##### Items:
(Fabric/Quilt): Fixed Essence of Continuity not preventing death for players.


### **(V.7.1.2 Changes) (1.20.1 Minecraft)**

##### Entities:
Fixed Variant Bee not showing angry texture until it stung something.

Improved Bee Queen holiday trades.

##### Blocks:
Fixed mining Honeycomb Brood Block without Silk Touch not removing Protection of the Hive effect.

Fixed Glistering Honey Crystal not able to be smelted to Sticky Honey Residue.

##### Items:
Fixed Potion Candles recipe filed under String Curtains in Recipe Book.


### **(V.7.1.1 Changes) (1.20.1 Minecraft)**

##### Entities:
Fixed Bee Queen not showing Thanksgivings only trade properly.

Make Bee Queen not have Luminescent Wax for bonus trades.


### **(V.7.1.0 Changes) (1.20.1 Minecraft)**

##### Blocks:
Fixed the rotation of Ancient Wax Stairs when they are in item form.

Added disableEssenceBlockShaders config option to let people disable the default shaders for Essence Blocks and use normal block textures instead.

End event properly if the Essence Block is forcibly removed while event is running.

##### Items:
Honey Bee Leggings now will eject pollen if standing on Wet Sponge or when in Water! New advancement added for this.
 Also, now works with Armor Stands so you can automate Pollen Puff generation! See if you can figure out a contraption to do this!

##### Entities:
Bee Queen will trade for Rabbit Stew and Suspicious Stew now.

Made sure Bee Queen will not become angry at other Bees or Rootmins or a few other mobs.

Fixed crash if spawning Cosmic Crystal, Electric Ring, or Purple Spike outside of Essence events by summon command.

Improved Rootmin AI, so hopefully it does nto get stuck in the ground as much anymore.

##### Structures:
Added small Tree Dungeons to Hive Wall, Hive Pillar, and Pollinated Pillar biomes to help decorate caves a bit more.

Added small Gear Columns to Floral Meadow and Crystal Canyon.
 The small feature holds Flower Headwear, Crystal Cannon, and Bee Cannon can be obtained here.

##### Effects:
Fixed Wrath of the Hive growing Brood Blocks that are too far above or below mob.

Adjusted Wrath of the Hive effect to not run every tick. Might see a tiny bit of performance boost.

##### Advancements:
Darken background for Bumblezone advancement tab.

Lower requirement for Stingless Bee Helmet advancement from 60 bees to 50 bees.

##### Lang:
uk_ua.json updated by unroman! Special thanks to them for the huge amount of translations done!

##### Mod Compat:
Marked Sentry Watcher as noCulling so Entity Culling mod won't cause desync with the entity's movements.

Workaround Lithium, Canary, Rubidium that caused my Suspicious Pile of Pollen block to not be brushable.

Fixed Iris causing Essence of Knowing to not highlight blocks behind walls.

Pollen Puff hitting Sickened Mushroom Cow from Wither Storm Mod will spawn Tainted Mushrooms nearby.

Wither Storm Mod's Sickened Spider will count towards Bumblezone's Too Many Legs advancement.

Wither Storm Mod's Sickened Spider spawners can spawn in Battle Cube and Spider Infested Bee Dungeons now.