### **(V.6.6.19 Changes) (1.19.4 Minecraft)**

##### Misc:
Renamed Bee Queen's Super Trades to Bonus Trades everywhere. This includes code, configs, and even tags such as the now-named:
 `the_bumblezone:bee_queen/disallowed_random_bonus_trade_items`
 `the_bumblezone:bee_queen/forced_allow_random_bonus_trade_items`

##### Items:
Fixed possible crash with Bee Helmet's Bee Highlighting.

Fixed Stingless Bee Helmet attempting to pick up bees while shift right-clicking bees.
 Only normal right click should pick up bees because crouching is what removes the bee from your head.
 As a result, this fixes the incompat with Carry On mod as well when wearing the helmet and shift right-clicking bees.

Made Honeycomb Brood Block the Creative Menu icon for Bumblezone to match 1.19.4 Bumblezone

Decreased number of Bees that needs highlighting by Stingless Bee Helmet to complete its achievement from 100 to 60.

Bumblezone's Bee Armor abilities now scales with how many bee-themed armor/curios/trinkets you have equipped on. (Including a few other mod's bee armor/curios/trinkets)
 The armors/curios/trinkets that counts towards improving abilities such as flight time are controlled by this item tag:

 `the_bumblezone:bee_armors/bz_armor_ability_enhancing_gear`

 Compat with modded items includes:

- Traveler's Backpack's Bee Backpack (Requires backpack to be in Curios slot. Enable Curios compat in Traveler's Backpack's config)

- Dungeon Gear's Bee Nest and Beehive armor set.

- MC Dungeons Armors's Bee Nest and Beehive armor set.

- Dungeons Content's Bee Nest and Beehive armor set.

- Productive Bees's Bee Nest Diamond Helmet.

- Simple Hats's Bee Hat.

##### Blocks:
Fixed Crystalline Flower getting wrong enchantments if you are in flower's UI connected to server and someone else opens a flower as well.

Fixed Crystalline Flower xp requirements getting messed up on servers if use sets config to use more than 32,767 xp for a tier.

Fixed Crystalline Flower selecting wrong enchantment on servers if there is more than 127 enchantments in the UI.

Nerfed Crystalline flower so it takes 1634xp to reach max tier instead of 1451xp.

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
 spawning as loot for better balancing. Regular Botania flowers can still spawn. Just now overpowered flowers now.

Added new Entity Type tags that will be used for mod compat where a few of Bumblezone's Spider Spawners will be
 converted into spawners for certain other mod's mobs. You can use these tags to quickly add rare chances of new
 mob spawners in Bumblezone! The tags are:

- `the_bumblezone:structure_spawner_mobs/battle_cubes_rare_spawner_type`

- `the_bumblezone:structure_spawner_mobs/cell_maze_rare_spawner_type`

- `the_bumblezone:structure_spawner_mobs/pollinated_stream_rare_spawner_type`

- `the_bumblezone:structure_spawner_mobs/spider_infested_bee_dungeon_rare_spawner_type`

##### Mod Compat:
Improved and condensed the Bee Queen trades in JEI, REI, and EMI. Much less page spam when viewing all recipes on the Bee Queen Spawn Egg item.

Chance of spawning Enemy Expansion's Ladybug mob in Hanging Gardens structure.

Enemy Expansion's Wasp mob spawners may rarely spawn in Cell Maze, Battle Cubes, and Pollinated Streams structures.

Better Animal Plus's Tarantula mob spawners may rarely spawn in Spider Infested Bee Dungeons and Pollinated Streams structures.

Blue Skies's Nested Spider mob spawners may rarely spawn in Spider Infested Bee Dungeons.

Blue Skies's Venom Spider mob spawners may rarely spawn in Cell Maze and Battle Cubes structures.

Twilight Forest's Hedge Spider mob spawners may rarely spawn in Spider Infested Bee Dungeons, Cell Maze, and Pollinated Streams structures.

Twilight Forest's Swarm Spider mob spawners may rarely spawn in Spider Infested Bee Dungeons and Cell Maze structures.

Alchemists Garden's Spiderling mob spawners may rarely spawn in Spider Infested Bee Dungeons, Cell Maze, and Pollinated Streams structures.

Tofucraft's Tofu Spider mob spawners may rarely spawn in Cell Maze and Pollinated Streams structures.

Born In Chaos's Wither Spider mob spawners may rarely spawn in Battle Cubes structures.

Earth Mobs Mod's Bone Spider mob spawners may rarely spawn in Battle Cubes structures.

Earth Mobs Mod's Stray Bone Spider mob spawners may rarely spawn in Battle Cubes structures.

Earth To Java Mobs's Bone Spider mob spawners may rarely spawn in Battle Cubes structures.

Minecraft Earth Mod's Bone Spider mob spawners may rarely spawn in Battle Cubes structures.

Canes Wonderful Spiders's Jumping, Wolf, and Hard Shell Spider mob spawners may rarely spawn in Spider Infested Bee Dungeons structures.

Canes Wonderful Spiders's Black Widow and Orb Weaver mob spawners may rarely spawn in Battle Cubes structures.


### **(V.6.6.18 Changes) (1.19.2 Minecraft)**

##### Mod Compat:
JEI and REI now shows Bee Queen trades for tradeable items! Special thanks to GizmoTheMoonPig for adding this!
 (Left click items to see trades that gives the item. Right click items to see trades that uses the item. Right click Bee Queen Spawn Egg to see all possible trades.)

Hitting Alchemist Garden's Ent mob with Pollen Puff may spawn Oak Sapling nearby.

##### Items:
Fixed Bumblebee Chestplate and Honeybee Leggings rendering improperly if more than one of them is rendering in the world.

Added Stinger Spear to `forge:tools/tridents` and Honey Crystal Shield to `forge:tools/shields` item tags for a bit more mod compatibility.

Added all bee armors to the 4 `forge:armors/` item tags for a bit more mod compatibility.

##### Fluids:
Honey Fluid and Royal Jelly Fluid now turns into Glistering Honey Crystal block when it touches non-honey fluids!

##### Blocks:
Sugar Infused Stone and Sugar Infused Cobblestone will now turn neighboring Water tagged fluids into Sugar Water fluid.

String Curtains now says message to use String to extend curtain if one right-clicks the curtain with another curtain item.

More items now setup so they give Crystalline Flower more XP than normal when consumed.
 Beacon, Glowstone, Spectral Arrow, Glistering Melon Slice, Golden Carrot, Golden Apple, and End Crystal gives a little more XP to flower when consumed.
 Enchanted Books gives more XP when consumed by flower, so they can be partially recycled into new enchantments.
 Royal Jelly Bottle gives an insane more XP due to its rarity.
 Royal Jelly Bucket/Block now maxes out the Crystalline Flower regardless of its XP requirements.

Two new item tags were added for the above Crystalline Flower consuming changes:

- `the_bumblezone:crystalline_flower/xp_1000_when_consumed`

- `the_bumblezone:crystalline_flower/xp_maxed_when_consumed`

##### Entities:
Adult Honey Slime now will split into 2 to 4 baby Honey Slimes when killed.

Adult Honey Slime that you harvested honey off of now will split into 2 to 4 tiny vanilla Slimes when killed.

Baby Honey Slimes now drops loot when killed

When a Honey Slime is killed, all nearby Honey Slimes will aggro against you. This includes splitting adults into babies.

Removed many wooden trades from Bee Queen so there's less wood pollution in the trade system. Adjusted some trades as well for balance.

##### Advancements:
Changed the Music Disc advancement to now progress when obtaining the Music Discs from anywhere. Not just Wandering Trader's trades.
 As long as the Bumblezone Music Disc gets into your player's inventory, it'll count towards the advancement progress.

Added new item tag `the_bumblezone:queens_desire/honey_drunk_trigger_items` item tag so now Honey Drunk advancement
 can be completed with more tagged modded honey bottle items other than vanilla's Honey Bottle item.

##### Commands:
Fixed bumblezone_read_self_data command not showing data properly for some advancements

##### Lang:
Updated ru_ru.json file. Special thanks to MageInBlack!