### **(V.7.19.0 Changes) (26.1.2 Minecraft)**

#### Teleportation:
Fixed teleporting to and from Bumblezone using the wrong ticket. Causing the destination chunk to remain loaded forever until server restart.
 Now the right ticket is used so the chunk is only loaded for 300 ticks for teleporting purposes.

Fixed issue where if player enters Bumblezone from non-Overworld dimension, rides a mount that originated in Bumblezone,  and try to exit Bumblezone dimension,
 the bug would've spawned player and mount in Overworld instead of the player's original non-Overworld dimension.

Adjusted teleportation queue logic to run only during server tick instead of all dimension ticks.

Fixed incorrect coordinate scaling that can rarely happen when teleporting to Bumblezone.

#### Fluids:
Fixed crash when a block is changed next to Sugar Water Bubble Column

#### Entities:
Fixed Bee Queen crash if queen picks up a trade item that was not dropped by an entity.

Fixed super edge case crash if Bee Queen holiday trade timeframe ends and player looks at queen before hourly trade refresh happens.

Bee Queen now gives rewards for entire stack of dropped items for holiday trades instead of a bugged single reward.

Fixed Beehemoth walking far too slow on the ground when riding one

Removed logging I had forgotten about from when I was debugging Beehemoth riding controls

Fixed possible issue where in presence of multiple players, Cosmic Crystal Entity could track wrong player on client compared to server

Fixed visual issue where Cosmic Crystal Entity spazzes out when tracking spinning attack ends

Fixed Rootmins not attacking `the_bumblezone:rootmin/targets` tagged entities on their own

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

Fixed Carpenter Bee Boots not properly handling mined block entities.
 (Beehives's bees were previously deleted when mined with these boots. Not anymore)

Pollen Puff hitting tall flowers will now not have the top half of the newly duplicated tall flower replace existing block

#### Blocks:
Ensured that `crystallineFlowerConsumeExperienceUI` and `crystallineFlowerConsumeItemUI` config options are respected on serverside for Crystalline Flower.

Fixed crash if Crystalline Flower has 2 or more books in it, and you enchant a book while scrolled down in list of enchantments and resultant list of enchantments is between 1 and 3.

Fixed Crystalline Flower not marking itself as changed for chunk data saving when adding/removing items from flower's GUI.

Fixed Crystalline Flower crash if one sets up `bz_crystalline_flower_data` in a way to have consumed items give 0 xp to flower.

Made it so Experience Orbs looking for Crystalline Flower will not load nearby chunks that are ungenerated/unloaded for better performance.
 And reduce the range Experience Orbs searches for flower from 8 chunks to 1 chunk radius. I think I thought I was doing 8 block radius...

Crystalline Flower now absorbs the true full Experience Orb amount by factoring in the orb's size (count)

Fixed crash when hovering over Potion Candle with effects in a GUI

Fixed Potion Candle block losing its data when broken into item form

Fixed Honeycomb Brood Blocks not actually growing/spawning bees by random ticks

#### Structures:
Fixed issue where Purple Sempiternal Sanctum arena event could remove climbable and fluid blocks outside the arena bounds

Added safety net so that if Sempiternal Sanctum arena fails to save the pre-arena area, the arena event will not start.

Fixed arena entity saving so that Purple Sempiternal Sanctum arena event will place Purple Spike Entities in correct 
 locations after closing world and re-entering during the arena event.

Tweaked what modded blocks shows in Hanging Gardens

#### Recipes:
Fixed possible crash if `the_bumblezone:container_shapeless_recipe_bz` gets an item with null crafting remainder

#### Features:
Fixed infinite loop if one sets the `size` field to 1 or 0 for `the_bumblezone:blockentity_combs` feature

Fixed crash with `the_bumblezone:floral_fill_with_rootmin` feature if `rootminFlowerTag` is empty or if `disallowedRootminFlowerTag` causes the final list of blocks to be empty

Improved `the_bumblezone:remove_floating_blocks_processor` processor for features so that it will better target floating plants, Pile of Pollen, and Honey Cocoons better.

#### Enchantments:
Made Potent Poison's `the_bumblezone:poison` data component not accept 0 for enchant intervals fields

#### Sounds:
Fixed memory leak with buzzing sound instance for Bumble Bee Chestplate and Beehemoth still retaining client worlds after exiting a world

#### Configs:
Fixed infinite loop if one sets `alternativeFluidToReplaceHoneyFluid` to Bumblezone's own Honey Fluid. Why would you do that? No idea. Don't do that.

#### Mod Compat:
Fixed broken Reliable Recipe Viewer mod compat

Fixed JEI mod compat

Fixed REI mod compat

NOTE: Potion Candle recipes in recipe viewer mods are not quite correct for the potion input at the moment. This will be looked at and corrected if possible in future.