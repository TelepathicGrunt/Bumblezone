### **(V.7.19.0 Changes) (1.20.1 Minecraft)**

#### Structures:
Tweaked what modded blocks shows in Hanging Gardens

Adjusted teleportation queue logic to run only during server tick instead of all dimension ticks.

#### Entities:
Fixed possible issue where in presence of multiple players, Cosmic Crystal Entity could track wrong player on client compared to server

Fixed visual issue where Cosmic Crystal Entity spazzes out when tracking spinning attack ends

Fixed issue where if player enters Bumblezone from non-Overworld dimension, rides a mount that originated in Bumblezone, and try to exit Bumblezone dimension,
 the bug would've spawned player and mount in Overworld instead of the player's original non-Overworld dimension.

#### Items:
Adjusted packet logic for syncing horse owner to clients from server so that it no longer broadcasts to all players.
 Reduces server load every time a horse is spawned into client's loaded chunks.

Optimized Buzzing Briefcase menu clicks to not deserialize all bees in briefcase before validating if the button's action can proceed.

Optimized Buzzing Briefcase menu clicks to not send button click to server if player clicks an action button without the required inventory item it needs.

Fixed Carpenter Bee Boots not properly handling mined block entities.
 (Beehives's bees were previously deleted when mined with these boots. Not anymore)

Pollen Puff hitting tall flowers will now not have the top half of the newly duplicated tall flower replace existing block

#### Blocks:
Ensured that `crystallineFlowerConsumeExperienceUI` and `crystallineFlowerConsumeItemUI` config options are respected on serverside for Crystalline Flower.

Fixed crash if Crystalline Flower has 2 or more books in it, and you enchant a book while scrolled down in list of enchantments and resultant list of enchantments is between 1 and 3.

Fixed Crystalline Flower not marking itself as changed for chunk data saving when adding/removing items from flower's GUI.

Fixed Honeycomb Brood Blocks not actually growing/spawning bees by random ticks

#### Structures:
Fixed issue where Purple Sempiternal Sanctum arena event could remove climbable and fluid blocks outside the arena bounds

Added safety net so that if Sempiternal Sanctum arena fails to save the pre-arena area, the arena event will not start.

#### Features:
Fixed infinite loop if one sets the `size` field to 1 or 0 for `the_bumblezone:blockentity_combs` feature

Fixed crash with `the_bumblezone:floral_fill_with_rootmin` feature if `rootminFlowerTag` is empty or if `disallowedRootminFlowerTag` causes the final list of blocks to be empty

#### Configs:
Fixed infinite loop if one sets `alternativeFluidToReplaceHoneyFluid` to Bumblezone's own Honey Fluid. Why would you do that? No idea. Don't do that.
