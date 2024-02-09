### **(V.7.3.9 Changes) (1.20.4 Minecraft)**

##### Items:
Adjusted how Life Essence heals allied players. Hopefully should work better with other mods that creates teams.
 (Basically I check entity.isAlliedTo(serverPlayer) now so other mods need to make this return try for their team systems)

##### Blocks:
Rid the ancient_luminescent_contrast_increase internal resourcepack and made the textures for it now used by default.

Made Heavy Air and Windy Air be seen as air for better mod compat and thus allow more ways of removing the air. Ideally...

##### Entities:
Added Rainbow Bee skin for Variant Bees! If you had already launched game with Bumblezone, add "rainbow_bee" to the variantBeeTypes config file for new bee skin to show up.

#### Misc
Fabric version requires v0.95.3 of Fabric API now. This is so I can make use of marking my air blocks as air.

##### Mod Compat:
Fixed compat with Extra Golems (Throwing Pollen puff at certain golems spawns specific plants)
