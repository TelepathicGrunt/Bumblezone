### **(V.7.2.8 Changes) (1.20.1 Minecraft)**

##### Items:
Adjusted how Life Essence heals allied players. Hopefully should work better with other mods that creates teams.
 (Basically I check entity.isAlliedTo(serverPlayer) now so other mods need to make this return try for their team systems)

##### Blocks:
Rid the ancient_luminescent_contrast_increase internal resourcepack and made the textures for it now used by default.

##### Entities:
Added Rainbow Bee skin for Variant Bees! If you had already launched game with Bumblezone, add "rainbow_bee" to the variantBeeTypes config file for new bee skin to show up.

##### Mod Compat:
Fixed compat with Extra Golems (Throwing Pollen puff at certain golems spawns specific plants)

