### **(V.7.19.0 Changes) (26.1.2 Minecraft)**

#### Structures:
Tweaked what modded blocks shows in Hanging Gardens

#### Fluids:
Fixed crash when a block is changed next to Sugar Water Bubble Column

#### Entities:
Fixed Bee Queen crash if queen picks up a trade item that was not dropped by an entity.

Fixed super edge case crash if Bee Queen holiday trade timeframe ends and player looks at queen before hourly trade refresh happens.

#### Items:
Fixed crash when having two of the same endgame essence items in inventory and one goes into cooldown.

Adjusted packet logic for syncing horse owner to clients from server so that it no longer broadcasts to all players.
 Reduces server load every time a horse is spawned into client's loaded chunks.
 Servers running v7.19.0 Bumblezone means clients must also update to v7.19.0 or else the adjusted packet will explode on server or client.

Optimized Buzzing Briefcase menu clicks to not deserialize all bees in briefcase before validating if the button's action can proceed.

Optimized Buzzing Briefcase menu clicks to not send button click to server if player clicks an action button without the required inventory item it needs.

Fixed crash if player tries to release a bee from Briefcase while it is in offhand slot

Fixed Bee Cannon and Crystal Cannon not working in offhand slot

Fixed issue where if you were bound to a now-depleted Respawn Anchor in another dimension and your death is prevented by
 Essence of Continuity, now you will spawn at the correct respawn-safe dimension and location instead of way off coordinates that could've result in death.

#### Blocks:
Ensured that `crystallineFlowerConsumeExperienceUI` and `crystallineFlowerConsumeItemUI` config options are respected on serverside for Crystalline Flower.

#### Recipes:
Fixed possible crash if `the_bumblezone:container_shapeless_recipe_bz` gets an item with null crafting remainder
