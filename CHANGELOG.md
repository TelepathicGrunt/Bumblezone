### **(V.7.19.0 Changes) (1.21.1 Minecraft)**

#### Structures:
Tweaked what modded blocks shows in Hanging Gardens

#### Items:
Fixed shot Honey Crystal Shard crashing when it hits an entity while Fabric Twilight Forest is on.

Adjusted packet logic for syncing horse owner to clients from server so that it no longer broadcasts to all players.
 Reduces server load every time a horse is spawned into client's loaded chunks.

Optimized Buzzing Briefcase menu clicks to not deserialize all bees in briefcase before validating if the button's action can proceed.

Optimized Buzzing Briefcase menu clicks to not send button click to server if player clicks an action button without the required inventory item it needs.

Fixed crash if player tries to release a bee from Briefcase while it is in offhand slot

Fixed Bee Cannon and Crystal Cannon not working in offhand slot

Fixed issue where if you were bound to a now-depleted Respawn Anchor in another dimension and your death is prevented by
 Essence of Continuity, now you will spawn at the correct respawn-safe dimension and location instead of way off coordinates that could've result in death.

#### Blocks:
Ensured that `crystallineFlowerConsumeExperienceUI` and `crystallineFlowerConsumeItemUI` config options are respected on serverside for Crystalline Flower.

#### Structures:
Fixed issue where Purple Sempiternal Sanctum arena event could remove climbable and fluid blocks outside the arena bounds

Added safety net so that if Sempiternal Sanctum arena fails to save the pre-arena area, the arena event will not start.

#### Recipes:
Fixed possible crash if `the_bumblezone:container_shapeless_recipe_bz` gets an item with null crafting remainder

#### Features:
Fixed infinite loop if one sets the `size` field to 1 or 0 for `the_bumblezone:blockentity_combs` feature

#### Configs:
Fixed infinite loop if one sets `alternativeFluidToReplaceHoneyFluid` to Bumblezone's own Honey Fluid. Why would you do that? No idea. Don't do that.