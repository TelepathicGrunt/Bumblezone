### **(V.6.6.6 Changes) (1.19.3 Minecraft)**

##### Blocks:
Made Crystalline Flower not reset fall distance calculations for entities falling into it.

Made Sticky Honey Residue and Sticky Honey Redstone bottom hit box won't reset fall distance calculations for entities falling into it.


### **(V.6.6.5 Changes) (1.19.3 Minecraft)**

##### Entities:
Fixed Bee Queen still asking to do trades when angry.

##### Mod Compat:
(Fabric/Quilt): Blocked several Zenith enchants from Crystalline Flower for balance out of the box.
 Can be changed by `the_bumblezone:crystalline_flower/disallowed_enchantments` enchantment tag.

(Forge): Fixed crash when put on with another mod that jar-in-jars MixinExtras

(Fabric/Quilt): Added Bumblezone Honey and Royal Jelly fluids to `create:diving_fluids` fluid tag so that Copper Backtank works properly in those fluids for next Create update.


### **(V.6.6.4 Changes) (1.19.3 Minecraft)**

##### Structures:
Fixed structure heightmap checks not being in center of structure properly

##### Effects:
Made Bee Queen, Beehemoth, and other mod's mobs that have "bee" or "bumble_beast" in the name immune to Wrath of the Hive effect

##### Entities:
Beehemoth and Bee Queen are now immune to Sweet Berry Bush thorn damaging and slow effect.

##### Mod Compat:
(Forge): Fixed Pollen Puff compat with Honey Bucket mod's Moobloom. Should be spawning their Golden Bloom flower instead of Dandelion
 Also fixed pollen Puff not spawning Echoshrooms when hitting their Sculk Snail


### **(V.6.6.3 Changes) (1.19.3 Minecraft)**

##### Major:
Switched to using Arch in backend. Special thanks to ThatGravyBoat for doing the work!
 Please let me know if any bugs or issues arises as this porting took a large overhaul of the codebase and could have introduces some bugs.

(Fabric/Quilt): Continuity is no longer needed for connected textures in Bumblezone!

##### Dimension:
Renamed fogReducer config option to fogThickness and fixed it so it nows shows more of the dimension and scales the fog properly.
 Also fixes dimension rendering breaking when render distance is 30 or greater and a rendering optimizing mod is on.

##### Blocks:
Made XP Orbs no longer spawn pollen particles when moving through Pile of Pollen block.

Crystalline Flower can now be placed on Amethyst Blocks along with the Glistering Honey Crystal.
 This is controlled now by this block tag: `the_bumblezone:crystalline_flower/can_be_placed_on`
 Some modded crystal blocks are support. See tag for which. You can datapack the tag to add more blocks or replace the tag to remove entries.

Crystalline Flower won't attract xp orbs if at max tier. Xp orb attraction is slightly strengthened for when not at max tier.

Fixed crash that occurred when taking non-enchanted book out of Crystalline Flower UI after clicking an enchant.

##### Fluids:
Fixed it so Bees that spend too long in Honey Fluid and Royal Jelly Fluid will drown properly.

(Quilt/Fabric): Fixed Honey Fluid and Royal Jelly Fluid so mobs can float on top of it now.

##### Items:
Fixed thrown Pollen Puff able to multiply Flowering Azalea Leaves and other modded flowering leaves.

##### Entities:
Bee Queen will not get angry at attacking Players in Peaceful mode now.

When Bee Queen is angry, the translucent super trade item rendering will disappear until queen is calm again.

Bee Queen won't have many plank or plank-derivative blocks as part of the super trade system to reduce wood bloat in it.

Fixed default keybind for flying down on Beehemoth being Space instead of Caps Lock. Space is fly up. Caps Locks should be fly down.
 Go into keybind menu to fix this manually if this update doesn't automatically fix it for your game.

##### Effects:
Wrath of the Hive will not apply to players in Peaceful mode now.

##### Advancements:
Fixed Journey's End advancement to not include The Beginning advancement requirement. Was throwing off MC's advancement progress tracker.

##### Lang:
Added a few extra details on some blocks in the JEI/REI/EMI item descriptions.

##### Config:
(Forge): Moved the fog config options out of the dimension.toml file into the client.toml config file because fog is clientsided.

##### Mod Compat:
Made it so that Pollen Puff hitting baby Mooblooms from Bovines and Buttercups will spawn flowers too.

(Forge): Added Pollen Puff compat with Moobloom's (FlowerCow) mod on Forge.

(Forge): Added Pollen Puff compat with Honey Bucket mod's Moobloom and other mobs with mushrooms on them. 
 Their Golden Bloom flower will spawn in Hanging Gardens now

(Forge): Fixed crash with Productive Bees and also completely redid how natural worldgen comb spawning works.
 To add or remove specific combs from spawning, datapack replace this placed feature tag file:
 `data/the_bumblezone/tags/worldgen/placed_feature/productive_bees_combs.json`
 Otherwise, use the spawnProductiveBeesHoneycombVariants config option to turn them all off if desired.