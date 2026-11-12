### **(V.7.19.0 Changes) (1.21.1 Minecraft)**

#### Structures:
Tweaked what modded blocks shows in Hanging Gardens

#### Items:
Fixed shot Honey Crystal Shard crashing when it hits an entity while Fabric Twilight Forest is on.

Adjusted packet logic for syncing horse owner to clients from server so that it no longer broadcasts to all players.
 Reduces server load every time a horse is spawned into client's loaded chunks.

Optimized Buzzing Briefcase menu clicks to not deserialize all bees in briefcase before validating if the button's action can proceed.

#### Recipes:
Fixed possible crash if `the_bumblezone:container_shapeless_recipe_bz` gets an item with null crafting remainder
