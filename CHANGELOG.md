### **(V.7.19.0 Changes) (1.21.1 Minecraft)**

#### Teleportation:
Fixed teleporting to and from Bumblezone using the wrong ticket. Causing the destination chunk to remain loaded forever until server restart.
 Now the right ticket is used so the chunk is only loaded for 300 ticks for teleporting purposes.

Fixed issue where if player enters Bumblezone from non-Overworld dimension, rides a mount that originated in Bumblezone,  and try to exit Bumblezone dimension,
 the bug would've spawned player and mount in Overworld instead of the player's original non-Overworld dimension.

Adjusted teleportation queue logic to run only during server tick instead of all dimension ticks.

#### Entities:
Bee Queen now gives rewards for entire stack of dropped items for holiday trades instead of a bugged single reward.

Fixed possible issue where in presence of multiple players, Cosmic Crystal Entity could track wrong player on client compared to server

Fixed visual issue where Cosmic Crystal Entity spazzes out when tracking spinning attack ends

Fixed Rootmins not attacking `the_bumblezone:rootmin/targets` tagged entities on their own

Made Rootmins stop panic running from entities not in `the_bumblezone:rootmin/panic_avoid` entity tag

Fixed Rootmins not getting curious about players wearing Bumblezone armor

Rootmins set to different scale attribute sizes now will have proper hitboxes

Fixed Honey Slime jump speed. 
 (Note: Honey Slimes already spawned before this update might now jump farther than intended due to previously saved MOVEMENT_SPEED attribute value)

Honey Slime with their honey taken is now optimized to not try and sync an int every tick

Baby Honey Slimes spawned during worldgen now get the proper baby health and other attributes instead of adult values

Reduced amount of particles that Sentry Watcher generates on client when it is charging at speed

Sentry Watcher fully respects the 80 block distance it can charge through instead of going excessively further due to bad coordinate calculation

Fixed incorrect Sentry Watcher health and armor damaging when it attacks a player or mob

Have Bumblezone chunk generation respect SPAWN_MOBS gamerule for mobs spawned on chunk creation

Adjusted mob spawning on chunk creation for Bumblezone dimension. Beehemoths now spawn more often as a result

Beehemoths no longer get full health healing when they are unloaded and then loaded back in

#### Items:
Fixed shot Honey Crystal Shard crashing when it hits an entity while Fabric Twilight Forest is on.

Adjusted packet logic for syncing horse owner to clients from server so that it no longer broadcasts to all players.
 Reduces server load every time a horse is spawned into client's loaded chunks.

Optimized Buzzing Briefcase menu clicks to not deserialize all bees in briefcase before validating if the button's action can proceed.

Optimized Buzzing Briefcase menu clicks to not send button click to server if player clicks an action button without the required inventory item it needs.

Fixed Buzzing Briefcase able to release bees through walls when attacking an enemy

Fixed Bee Cannon and Crystal Cannon not working in offhand slot

Fixed issue where if you were bound to a now-depleted Respawn Anchor in another dimension and your death is prevented by
 Essence of Continuity, now you will spawn at the correct respawn-safe dimension and location instead of way off coordinates that could've result in death.

Fixed Essence of Calming can mess with pre-existing player-mob alliances in a negative way

Fixed Carpenter Bee Boots not properly handling mined block entities.
 (Beehives's bees were previously deleted when mined with these boots. Not anymore)

Pollen Puff hitting tall flowers will now not have the top half of the newly duplicated tall flower replace existing block

Fixed Honey Crystal Shield max durability not increasing correctly when repaired

Fixed Floral Headwear on Armor Stands were not attracting 50% of bees. Now all 100% of bees will swoon over this item.

Stinger Spear enchanted with Potent Poison level 3 and above now inflicts higher poison levels as intended

Bee Stinger does not get consumed now if used on a bee that has a stinger already

Shot Bee Stingers and Crystal Shards now doesn't fire unused `broadcastEntityEvent`. Reduces server packet load.

#### Blocks:
Ensured that `crystallineFlowerConsumeExperienceUI` and `crystallineFlowerConsumeItemUI` config options are respected on serverside for Crystalline Flower.

Fixed crash if Crystalline Flower has 2 or more books in it, and you enchant a book while scrolled down in list of enchantments and resultant list of enchantments is between 1 and 3.

Fixed Crystalline Flower not marking itself as changed for chunk data saving when adding/removing items from flower's GUI.

Fixed Crystalline Flower crash if one sets up `bz_crystalline_flower_data` in a way to have consumed items give 0 xp to flower.

Made it so Experience Orbs looking for Crystalline Flower will not load nearby chunks that are ungenerated/unloaded for better performance.

Experience Orbs will stop tracking a Crystalline Flower that reached max tier or was removed

Crystalline Flower now absorbs the true full Experience Orb amount by factoring in the orb's size (count)

Crystalline Flower fixed to not consume more items from an itemstack than needed to max out flower tier

Fixed Honeycomb Brood Blocks not actually growing/spawning bees by random ticks

Silk Touch now prevents max stage Honeycomb Brood Blocks from spawning a bee when broken.
 Inversely, non-Silk touch mining will make max stage Honeycomb Brood Blocks spawn a bee.

Fixed weird bug where placing a Super Candle/Potion Candle with wick waterlogged will replace that top candle with below Super Candle/Potion Candle block instead.

Super Candle/Potion Candle wicks now can be replaced by flowing fluids and pistons

Replacing the lit wicks of Super Candle/Potion Candle will not leave the base block still lit.

Lighting Super Candle/Potion Candle by Dispenser with Flint and Steel will now decrease Flint and Steel durability

Optimized Sticky Honey Redstone to not keep calculating redstone signal every tick an entity is touching it
 if there was no change in redstone power since last tick.

Fixed weird speed boost that happens when going through different sized Pile of Pollen blocks

Fixed String Curtain on change, sending a block update to entire vertical column to world top

Fixed edge case where Royal Jelly Block couldn't be pulled by piston when it should or able to delete blocks.
 Now it should properly behave as a pull-only block

Carving Carvable Wax block now preserves its facing property

#### Fluids:
Fixed Royal Jelly Fluid and Honey Fluid overlay when in fluid from being messed up when player is below Y = 0

Fixed case where vanilla Bubble Column won't be supported on Sugar Water Bubble Column

#### Structures:
Fixed issue where Purple Sempiternal Sanctum arena event could remove climbable and fluid blocks outside the arena bounds

Added safety net so that if Sempiternal Sanctum arena fails to save the pre-arena area, the arena event will not start.

Fixed arena entity saving so that Purple Sempiternal Sanctum arena event will place Purple Spike Entities in correct 
 locations after closing world and re-entering during the arena event.

Made it so if `repeatableEssenceEvents` is true, beating the Sempiternal Sanctum arena event again grants 1/4th xp of the first time completion amount

Fixed bug where teleporting out of a Sempiternal Sanctum arena event into another dimension could retain the arena boss bar and music.

Fixed bug where players who had not consumed Essence of the Bees would have Sempiternal Sanctum music playing forever after leaving structure

Fixed Cell Maze having a room completely flooded with Sugar Water

Tweaked what modded blocks shows in Hanging Gardens

#### Music:
(Fabric): Fix Bumblezone music fading being sped up when there's multiple players together

#### Loot:
Fixed `the_bumblezone:honey_compass_locate_structure` loot function's `search_radius` and `skip_existing_chunks` fields not being respected

#### Recipes:
Fixed possible crash if `the_bumblezone:container_shapeless_recipe_bz` gets an item with null crafting remainder

#### Features:
Fixed infinite loop if one sets the `size` field to 1 or 0 for `the_bumblezone:blockentity_combs` feature

Fixed crash with `the_bumblezone:floral_fill_with_rootmin` feature if `rootminFlowerTag` is empty or if `disallowedRootminFlowerTag` causes the final list of blocks to be empty

Improved `the_bumblezone:remove_floating_blocks_processor` processor for features so that it will better target floating plants, Pile of Pollen, and Honey Cocoons better.

#### Enchantments:
Made Potent Poison's `the_bumblezone:poison` data component not accept 0 for enchant intervals fields

#### Effects:
Fixed case where Wrath of the Hive effect at level 3 or higher would remove itself if it fails to spawn a bee during 1 attempt

Made `the_bumblezone:bee_aggression_in_dimension/wrath_activating_items_when_picked_up` item tag functional again for Wrath of the Hive

Fixed it where multiple players together could cause one player's Wrath of the Hive client effects to be applied or not applied correctly

(NeoForge): `neurotoxinMaxLevel` and `paralyzedMaxTickDuration` should take effect right away after editing config file withouy needing to restart game

#### Sounds:
Fixed memory leak with buzzing sound instance for Bumble Bee Chestplate and Beehemoth still retaining client worlds after exiting a world

#### Commands:
Fix command suggestions getting messed up when switching between `bumblezone_modify_data` and `bumblezone_read_data`

#### Configs:
Fixed infinite loop if one sets `alternativeFluidToReplaceHoneyFluid` to Bumblezone's own Honey Fluid. Why would you do that? No idea. Don't do that.

Fixed it so `ragingEssenceStrengthLevels` config is actually read now

Made `dispensersDropGlassBottles` config option work for Honey Bottles filling Porous Honeycomb Block from Dispensers

#### Advancements:
Fixed many advancement icons not having right icon looks and fixed a few advancement trigger conditions

#### Mod Compat:
(NeoForge): Fixed it so if non-Bumblezone modded bee spawns in Bumblezone dimension, the original bee is canceled so that only the  non-Bumblezone modded bee spawns in its place 
