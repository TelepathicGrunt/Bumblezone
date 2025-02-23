### **(V.7.5.15 Changes) (1.20.1 Minecraft)

#### Mod Compat:
(Forge): Added compat with Corail Tombstone to make players drop no-despawn items when dying near Sempiternal Sanctum's Essence Block.
 No tombstone will spawn to prevent item duplication or exploits for both mods.

Fixed log error about Pollen Puff's compat with Ars Elemental (Credit to Satherov for PR fix)


### **(V.7.5.14 Changes) (1.20.1 Minecraft)

#### Blocks:
Adjusted how Heavy Air disables flight to reduce problematic conflict with other mod's creative flight.

Allow Super Candles and Potion Candles to stay lit if player waterlogs the bottom part of candle.

#### Fluids:
Fixed bug where Sugar Water flowing down as a waterfall next to Sugar Cane can grow Sugar Cane beyond a height of 5.

Added `the_bumblezone:sugar_water/grows_plant_faster` block tag that allows for adding other crops for Sugar Water to age faster.
 Will work on Wheat and such if you add them to this tag.

#### Configs:
(Fabric): Fixed missing translations for some configs
