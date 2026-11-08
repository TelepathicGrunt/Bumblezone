### **(V.7.18.7 Changes) (26.1.2 Minecraft)**

#### Entities:
Fixed crash with Sentry Watchers seeing mobs/players

Dirt Pellet now deals extra damage to Happy Ghast and to modded mobs that return true for Entity#isFlyingVehicle().


### **(V.7.18.6 Changes) (26.1.2 Minecraft)**

#### Advancements:
Changed the Queen Desire Advancement "Flooding the housing market" to now be "Smackdowns" where you kill flying mobs
 with Dirt Pellet instead of crafting beehives. This is due to detecting when player crafted beehives is fragile and
 breaks from time to time with other mods. This new advancement is more stable.

#### Mod Compat:
(NeoForge): Fixed Productive Bees compat. 
  You may need to delete the_bumblezone\mod_compatibility.toml file to regenerate it with proper Productive Bees default values now.