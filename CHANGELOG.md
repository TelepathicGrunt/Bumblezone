### **(V.7.11.3 Changes) (1.20.1 Minecraft)**

#### Fluids:
Fixed Sugar Water making Suger Cane growing replace solid blocks.

#### Misc:
Deleted a mixin that aimed to fix MC-246262, but it seems Mojang fixed that bug a long time ago.
 Slim chance that this mixin might had been involved with some server client-bound chunk data packet issue.

#### Lang:
zh_cn.json lang file updated by MechtaSnezhevna


### **(V.7.11.2 Changes) (1.20.1 Minecraft)*

#### Lang:
pt_br.json lang file updated by PrincessStellar

ru_ru.json lang file updated by AstardGrimotre

#### Mod Compat:
Removed the Vanilla Backport bz_pollen_puff_entity_flowers compat file because it turns out Vanilla Backport registers content
 under the `minecraft` namespace and is not possible to make compatible with my compat file without a breaking change to my file format.
 I am electing to not do the breaking change and instead, officially remove my tiny insignificant compat with Vanilla Backport
 because mods registering new content under the `minecraft` namespace is not something that should ever be encouraged or supported.


### **(V.7.11.1 Changes) (1.20.1 Minecraft)*

#### Blocks:
Reduced some Z-fighting with large distances viewing Sticky Honey Residue and Sticky Honey Redstone.

#### Configs:
(Fabric): Added missing English translation for variantBeeAfterWorlgenSpawnRate config option.

Added restrictBeesOnGuiToBzDimension config option for those who want gui bees on all screens only when in Bumblezone dimension.


### **(V.7.11.0 Changes) (1.20.1 Minecraft)*

#### Entities:
Added a Bee Queen trade to exchange Banner Patterns for Experience Bottles.
 This uses the `c:loom_patterns` item tag so add other mod's Banner Patterns to there.

#### Gui:
Added an April Fool's joke for future years, where bees can appear on any in-world gui.
 This is controlled by three configs: showBeesOnGuiAllYearRound, showBeesOnGuiOnAprilFools, and maximumBeesOnGui
 You can activate these gui bees 24/7 with the year round config!
 The Bumblezone Teleporting screen will have these gui bees always as a small thing if stuck waiting for dimension to load.


### **(V.7.10.1 Changes) (1.20.1 Minecraft)*

#### Entities:
Optimized model part gathering code for Rootmins, Bee Queen, and Cosmic Crystal Entity. 
 Biggest benefit is Floral Meadows with tons of Rootmins will not smash the memory as much as before with super high memory allocation rates.


### **(V.7.10.0 Changes) (1.20.1 Minecraft)*

#### Mod Compat:
Created an internal resourcepack called "Bumblezone - Shader Emissive". 
  If manually enabled in-game, it will allow many shaders to show emissive (glowing) textures for Bumblezone stuff.
  Some mods will also then show colored lighting around certain Bumblezone blocks.
  Modpack makers can extract the resourcepack, make changes, and ship it in their packs as force enabled. No issue there.
  More info can be found in the wiki here: https://github.com/TelepathicGrunt/Bumblezone/wiki/%E2%9C%A8-Shaders-&-Lighting-%E2%9C%A8