### **(V.7.0.13 Changes) (1.20.1 Minecraft)**

##### Blocks:
Heavy Air now uses a `the_bumblezone:heavy_air/vehicles_to_apply_gravity_to` entity type tag for player ridden vehicles 
 to be pulled down as well when in Heavy Air. Prevent flying mounts from working in Heavy Air.


### **(V.7.0.12 Changes) (1.20.1 Minecraft)**

##### Advancements:
Fixed Queen Desire's Journey's End advancement not being completable due to still referencing an old removed advancement.

##### Compat:
(Forge): Disabled Create Jetpack mod's Jetpack when in Heavy Air.


### **(V.7.0.11 Changes) (1.20.1 Minecraft)**

##### Compat:
Added user messages for when Jetpacks and Jet Boots are disabled in Heavy Air.

(Forge): Block Blood Magic's Air Sigil and Reliquary's Rending Gale use when in Heavy Air block.

(Forge): Fixed bug where PneumaticCraft compat would put cooldown on any boots equipped.

(Forge): Disabled Mekanism's Gravitation and Jetpack modules when in Heavy Air.
 (The Jetpack items will be disabled properly by future Mekanism update to respect item cooldown)

(Fabric/Quilt): Added NoIndium mod into jar so Sodium users gets warned to add Indium for better compat.


### **(V.7.0.10 Changes) (1.20.1 Minecraft)**

##### Items:
Fixed crash in Buzzing Briefcase screen when clicking on an item button when you don't have said item in inventory.

##### Blocks:
Set Infinity Air breaking speed to -1.0 to better state it is unbreakable to other mods.

##### Entities:
Removed emissive texture files for Bee Queen and Beehemoth as some emissive texture mods aren't applying them correctly and the emissive was not needed anyway.

##### Compat:
(Forge): Added back missing Ars Nouveau compat. (Teleport spells)


### **(V.7.0.9 Changes) (1.20.1 Minecraft)**

##### Entities:
(Fabric/Quilt): Fixed check that prevented Bumblezone from spawning enough bees over time.

##### Blocks:
Heavy Air now uses this mob effect tag to know what effect to remove from entities inside the block: 
 `the_bumblezone:heavy_air/remove_effects`

The pull down effect gets stronger faster in Heavy Air now.

##### Items:
Made a bunch of Bumblezone items now respect cooldown if they are given a cooldown. 
 Will suppress their abilities or behavior.

##### Client:
(Forge): Switched fog mixin into Forge event setup in a way to increase compat and fixed some issues with other mods.

(Fabric/Quilt): Adjusted fog code internally that could've been causing issues under very rare circumstances.

##### Compat:
Iron Jetpacks are now disabled when in Heavy Air block.

EvilCraft Brooms will sink in Heavy Air when ridden.
