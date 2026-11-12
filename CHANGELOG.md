### **(V.7.18.8 Changes) (26.1.2 Minecraft)**

#### Structures:
Tweaked what modded blocks shows in Hanging Gardens

#### Fluids:
Fixed crash when a block is changed next to Sugar Water Bubble Column

#### Entities:
Fixed Bee Queen crash if queen picks up a trade item that was not dropped by an entity.

Fixed super edge case crash if Bee Queen holiday trade timeframe ends and player looks at queen before hourly trade refresh happens.

#### Items:
Fixed crash when having two of the same endgame essence items in inventory and one goes into cooldown.

#### Recipes:
Fixed possible crash if `the_bumblezone:container_shapeless_recipe_bz` gets an item with null crafting remainder


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