### **(V.7.3.5 Changes) (1.20.4 Minecraft)**

##### Blocks:
Allow Crystalline Flower to keep the consume xp buttons active when player has an ungodly absurd amount of xp.
 Person had so much xp that it overflowed some calculations and caused the consume xp levels buttons to get stuck disabled.

##### Enchantments:
Nerfed Neurotoxins to max level of 2.

Added `neurotoxinMaxLevel` config option to allow setting what the max level is for Neurotoxin enchantment. Set to 2 by default.
 Existing items with Neurotoxin will not be retroactively lowered in level.

##### Effects:
Iron Golem and Snow Golem are now immune to Paralyzed effect.

Added `the_bumblezone:paralyzed/immune` entity type tag to allow people to say what cannot be paralyzed in addition to undead mobs.
 Can add `minecraft:player` to the tag to make players immune to paralyzed.

Added `paralyzedMaxTickDuration` config option to allow setting maximum tick time an entity can stay paralyzed for at once. 
 Set to 600 by default to accommodate Long Paralyzed potion. (Actions that re-apply Paralyzed effect will reset timer tho)

