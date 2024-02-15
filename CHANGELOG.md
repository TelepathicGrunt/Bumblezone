### **(V.7.2.10 Changes) (1.20.1 Minecraft)**

##### Fluids:
(Fabric/Quilt): Changed code for upcoming Fabric API fluid renderer breakage.

##### Mod Compat:
(Forge): Removed the code compat with Quark due to how fragile it is. Instead, made PR to Quark to add 
 Bumblezone's items to their enchantingStacks config. https://github.com/VazkiiMods/Quark/pull/4669


### **(V.7.2.9 Changes) (1.20.1 Minecraft)**

##### Items:
Fixed potential crash with using Essence of Continuity.

Made it harder to abuse swapping the same Essences around and bypassing cooldowns.

Fixed essence of continuity not removing harmful status effects when respawning.

Essence of Life will now heal other nearby players if PvP is turned off for the world.

(Forge only): Added a small Easter Egg where Buzzing Briefcase can be used as Furnace fuel even if the briefcase has bees inside!
 150 burn time pls an additional 1500 burn time for each bee inside. This kills the bees :(

##### Blocks:
Fixed it so Silk Touch mining Honeycomb Brood Blocks does not cause Wrath of the Hive effect.

Made Honeycomb Brood Blocks not spawn mobs if the doTileDrops gamerule is set to false.

Waterlogged Honey Cocoons will not drop items if doTileDrops gamerule is set to false.

##### Fluids:
Royal Jelly Fluid cannot be picked up with a Glass Bottle in world now to prevent people from wasting it due to how valuable it is.
 A message will appear saying to us a Bucket instead.

##### Mod Compat:
Removed mixin into Sodium that I was using to make my Honey Fluids render properly.
 Was fixing this: https://github.com/CaffeineMC/sodium-fabric/issues/2253
 The mixin removal is because 0.6.0 Sodium is moving classes around and likely would crash the mixin.
 Use Embeddium for now if you wish to see Bumblezone's Honey Fluid rendering properly.


### **(V.7.2.8 Changes) (1.20.1 Minecraft)**

##### Items:
Adjusted how Life Essence heals allied players. Hopefully should work better with other mods that creates teams.
 (Basically I check entity.isAlliedTo(serverPlayer) now so other mods need to make this return try for their team systems)

##### Blocks:
Rid the ancient_luminescent_contrast_increase internal resourcepack and made the textures for it now used by default.

##### Entities:
Added Rainbow Bee skin for Variant Bees! If you had already launched game with Bumblezone, add "rainbow_bee" to the variantBeeTypes config file for new bee skin to show up.

#### Structures:
Hid the Netherite Block in Sempiternal Sanctums a little bit better.

##### Mod Compat:
Fixed compat with Extra Golems (Throwing Pollen puff at certain golems spawns specific plants)

