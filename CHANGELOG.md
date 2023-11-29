### **(V.7.2.3 Changes) (1.20.2 Minecraft)**

##### Blocks:
Fixed rare concurrency crash when caching Sticky Honey Residue blockstates shapes.

Added two new tags for Potion Candle. Paralysis effect is now capped to 10 seconds if used for Potion Candle.
 `the_bumblezone:potion_candle/capped_to_1_minute_effects`
 `the_bumblezone:potion_candle/capped_to_10_seconds_effects`

##### Effects:
Fixed server-client sync issue with Paralysis effect.

Paralysis that is managed to be put on undead mobs will be removed from undead mobs.

##### Misc:
Tiny optimizations.

##### Mod Compat:
(Fabric): Worked around Porting Lib incompatibility that was breaking fishing in Bumblezone and preventing Bee Stinger drops from bees.
