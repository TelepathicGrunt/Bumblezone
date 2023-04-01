### **(V.6.7.4 Changes) (1.19.2 Minecraft)**

##### Structures:
Changed how Dance Floor structure spawns Music Discs. Now it will pick a random disc to spawn from this item tag:
 `the_bumblezone:structures/dance_floor_music_discs`
 This will allow me and modpack makers to add other mod's Music Discs that feel fits to spawn in this structure.
 Windswept's Bumblebee and Biome Makeover's Red Rose Music Disc may appear in this structure if those mods are on!

##### Teleportation:
Send some packets now when teleporting to and from Bumblezone. Might help against some issues or oddities with teleporting.
 Will fix Modern Industrialization's Quantum Armor not having flight after going to/from Bumblezone. Now it will keep the ability.

##### Fluids:
Got Sugar Water Fluid to now spread to neighboring waterloggable blocks if they have 2 source blocks next to them. They will get waterlogged by vanilla Water.

##### Mod Compat:
Shows short description of Bumblezone's enchantments for Enchantment Lore mod.


### **(V.6.7.3 Changes) (1.19.2 Minecraft)**

##### Structures:
Added back Mystical Botania flowers to Honey Cocoon loot in structures. Forgot they were the "normal" flowers. 
 The "special" Botania flowers still will not be permitted in structure loot for balance reasons.

##### Entities:
Adjusted Bee Queen trading so it logs error instead of crashing due to empty/broken item tags used for trading.

Fixed Bee Queen able to do Bonus Trades for items that should not be allowed for Bonus Trading.

##### Fluids:
Added `the_bumblezone:sugar_water/forced_disallow_waterlogging_blocks_when_placed_in_fluid` block tag to allow fine tuning
 what blocks can be waterlogged when placed into Sugar Water Fluid to go along with other item tag
 `the_bumblezone:sugar_water/waterloggable_blocks_when_placed_in_fluid`


### **(V.6.7.1 Changes) (1.19.2 Minecraft)**

##### Structures:
Fixed Honey Cave Room's Honey Cocoons still able to spawn Botania's Special/Mythical flowers. 
 Was not properly reading from the `the_bumblezone:structures/disallowed_flowers_in_cocoon_loot` item tag.

##### Items:
Spectrum's Bee Head item when worn will count towards boosting the abilities of Bumblezone's bee armors.


### **(V.6.7.0 Changes) (1.19.2 Minecraft)**

##### Misc:
Renamed Bee Queen's Super Trades to Bonus Trades everywhere. This includes code, configs, and even tags such as the now-named:
 `the_bumblezone:bee_queen/disallowed_random_bonus_trade_items`
 `the_bumblezone:bee_queen/forced_allow_random_bonus_trade_items`

##### Items:
Made Honeycomb Brood Block the Creative Menu icon for Bumblezone to match 1.19.4 Bumblezone

Fixed possible crash with Bee Helmet's Bee Highlighting.

Fixed Stingless Bee Helmet attempting to pick up bees while shift right-clicking bees.
 Only normal right click should pick up bees because crouching is what removes the bee from your head.
 As a result, this fixes the incompat with Carry On mod as well when wearing the helmet and shift right-clicking bees.

Decreased number of Bees that needs highlighting by Stingless Bee Helmet to complete its achievement from 100 to 60.

Buffed Honeybee Leggings to become pollinated much more often when running through flowers.

Sugar Water Bucket will now be able to do regular vanilla Water waterlogging for more waterloggable blocks by faking to be vanilla water to those blocks.

Sugar Water Bucket spawns Sugar item when attempted to be placed in nether or other dimension that extinguishes water.

Bumblezone's Bee Armor abilities now scales with how many bee-themed armor/curios/trinkets you have equipped on. (Including a few other mod's bee armor/curios/trinkets)
 The armors/curios/trinkets that counts towards improving abilities such as flight time are controlled by this item tag:

 `the_bumblezone:bee_armors/bz_armor_ability_enhancing_gear`

 Compat with modded items includes:

- Traveler's Backpack's Bee Backpack (Requires backpack to be in Trinkets slot. Enable Trinkets compat in Traveler's Backpack's config)

- MC Dungeons Armors's Bee Nest and Beehive armor set.

- Simple Hats's Bee Hat.

##### Blocks:
Fixed Crystalline Flower getting wrong enchantments if you are in flower's UI connected to server and someone else opens a flower as well.

Fixed Crystalline Flower xp requirements getting messed up on servers if use sets config to use more than 32,767 xp for a tier.

Fixed Crystalline Flower selecting wrong enchantment on servers if there is more than 127 enchantments in the UI.

Nerfed Crystalline flower so it takes 1634xp to reach max tier instead of 1451xp.

Fixed mobs trying to pathfind through Honey Cocoon instead of around or on top.

##### Fluids:
Sugar Water fluid now can allow Lilypads and Frogspawn to be placed on it. Vanilla had a hardcoded check for vanilla water in the Lilypad code originally.

Many more waterloggable blocks can be placed into Sugar Water and be waterlogged with vanilla Water.
 If a modded block doesn't get waterlogged, try adding it to this new blockstate to see if it'll waterlog when placed into Sugar Water fluid:
 `the_bumblezone:sugar_water/waterloggable_blocks_when_placed_in_fluid`

##### Entities:
If using another mod to put passengers on the Bee Queen, now those passengers are positioned properly on the queen's back.

Royal Jelly Block/Bucket trades now have 0.11% of Dragon Egg chance down from previously 0.43%.
 Already incredibly difficult to get. Now even more of a once-in-a-lifetime chance of getting. Are you a lottery winner???

Fixed Bee Queen trades not being synced from server to client. Now clients with recipe viewers can see trades when connected to server.

##### Structures:
Slightly buffed the loot in Honitel and Pirate Ship structures. Pirate Ship loot can have Spyglass now.

Fixed one of Cell Maze's piece not having properly randomize loot in its Honey Cocoon.

Throne Pillar's queen room should not be crowded with Glistering Honey Crystals anymore.
 Also prevent the giant crystal features in a few other structures as well.

Increased chances of Pollinated Streams of having a spider spawner piece.

Added new item tag called `the_bumblezone:structures/disallowed_flowers_in_cocoon_loot` that can disallow flowers from showing
 up in Honey Cocoons in Bumblezone's structures. This is mainly used to prevent Botania's "special" flowers from
 spawning as loot for better balancing. Regular Botania flowers can still spawn. Just not overpowered flowers now.

Added new Entity Type tags that will be used for mod compat where a few of Bumblezone's Spider Spawners will be
 converted into spawners for certain other mod's mobs. You can use these tags to quickly add rare chances of new
 mob spawners in Bumblezone! The tags are:

- `the_bumblezone:structure_spawner_mobs/battle_cubes_rare_spawner_type`

- `the_bumblezone:structure_spawner_mobs/cell_maze_rare_spawner_type`

- `the_bumblezone:structure_spawner_mobs/pollinated_stream_rare_spawner_type`

- `the_bumblezone:structure_spawner_mobs/spider_infested_bee_dungeon_rare_spawner_type`

##### Mod Compat:
Improved and condensed the Bee Queen trades in JEI, REI, and EMI. Much less page spam when viewing all recipes on the Bee Queen Spawn Egg item.

Better Animal Plus's Tarantula mob spawners may rarely spawn in Spider Infested Bee Dungeons and Pollinated Streams structures.

Twilight Forest's Hedge Spider mob spawners may rarely spawn in Spider Infested Bee Dungeons, Cell Maze, and Pollinated Streams structures.

Twilight Forest's Swarm Spider mob spawners may rarely spawn in Spider Infested Bee Dungeons and Cell Maze structures.

Earth To Java Mobs's Bone Spider mob spawners may rarely spawn in Battle Cubes structures.
