### **(V.7.3.10 Changes) (1.20.4 Minecraft)**

##### Items:
Fixed potential crash with using Essence of Continuity.

Made it harder to abuse swapping the same Essences around and bypassing cooldowns.

Fixed essence of continuity not removing harmful status effects when respawning.

Essence of Life will now heal other nearby players if PvP is turned off for the world.

##### Blocks:
Made Honeycomb Brood Block not spawn mobs if the DoBlockDrops gamerule is set to false.

Fixed it so Silk Touch mining Honeycomb Brood Blocks does not cause Wrath of the Hive effect.

##### Misc:
Made Essence cooldown timer and Essence arena timer show only minutes and seconds. No milliseconds.

##### Mod Compat:
Removed mixin into Sodium that I was using to make my Honey Fluids render properly.
 Was fixing this: https://github.com/CaffeineMC/sodium-fabric/issues/2253
 The mixin removal is because 0.6.0 Sodium is moving classes around and likely would crash the mixin.
 Use Embeddium for now if you wish to see Bumblezone's Honey Fluid rendering properly.


### **(V.7.3.9 Changes) (1.20.4 Minecraft)**

##### Items:
Adjusted how Life Essence heals allied players. Hopefully should work better with other mods that creates teams.
 (Basically I check entity.isAlliedTo(serverPlayer) now so other mods need to make this return try for their team systems)

##### Blocks:
Rid the ancient_luminescent_contrast_increase internal resourcepack and made the textures for it now used by default.

Made Heavy Air and Windy Air be seen as air for better mod compat and thus allow more ways of removing the air. Ideally...

##### Entities:
Added Rainbow Bee skin for Variant Bees! If you had already launched game with Bumblezone, add "rainbow_bee" to the variantBeeTypes config file for new bee skin to show up.

#### Structures:
Hid the Netherite Block in Sempiternal Sanctums a little bit better.

#### Misc:
Fabric version requires v0.95.3 of Fabric API now. This is so I can make use of marking my air blocks as air.

##### Mod Compat:
Fixed compat with Extra Golems (Throwing Pollen puff at certain golems spawns specific plants)
