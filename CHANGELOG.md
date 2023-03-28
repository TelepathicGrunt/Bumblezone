### **(V.6.7.1 Changes) (1.19.2 Minecraft)**

##### Structures:
Fixed Honey Cave Room's Honey Cocoons still able to spawn Botania's Special/Mythical flowers.
 Was not properly reading from the `the_bumblezone:structures/disallowed_flowers_in_cocoon_loot` item tag.


### **(V.6.7.0 Changes) (1.19.4 Minecraft)**

##### Misc:
(Forge): Fixed crash with v45.0.23 Forge due to Forge breaking change.

Renamed Bee Queen's Super Trades to Bonus Trades everywhere. This includes code, configs, and even tags such as the now-named:
 `the_bumblezone:bee_queen/disallowed_random_bonus_trade_items`
 `the_bumblezone:bee_queen/forced_allow_random_bonus_trade_items`

##### Items:
Fixed possible crash with Bee Helmet's Bee Highlighting.

Fixed Stingless Bee Helmet attempting to pick up bees while shift right-clicking bees.
 Only normal right click should pick up bees because crouching is what removes the bee from your head.
 As a result, this fixes the incompat with Carry On mod as well when wearing the helmet and shift right-clicking bees.

Decreased number of Bees that needs highlighting by Stingless Bee Helmet to complete its achievement from 100 to 60.

Buffed Honeybee Leggings to become pollinated much more often when running through flowers.

(Fabric/Quilt): Sugar Water Bucket will now be able to do regular vanilla Water waterlogging for more waterloggable blocks by faking to be vanilla water to those blocks.

Sugar Water Bucket spawns Sugar item when attempted to be placed in nether or other dimension that extinguishes water.

Bumblezone's Bee Armor abilities now scales with how many bee-themed armor/curios/trinkets you have equipped on. (Including a few other mod's bee armor/curios/trinkets)
 The armors/curios/trinkets that counts towards improving abilities such as flight time are controlled by this item tag:

  `the_bumblezone:bee_armors/bz_armor_ability_enhancing_gear`

  Compat with modded items includes these mods once they update to 1.19.4 MC:

- Traveler's Backpack's Bee Backpack (Requires backpack to be in Curios/Trinkets slot. Enable Curios/Trinkets compat in Traveler's Backpack's config)

- Dungeon Gear's Bee Nest and Beehive armor set along with their Buzzy Nest artifact.

- MC Dungeons Armors's Bee Nest and Beehive armor set.

- Dungeons Content's Bee Nest and Beehive armor set.

- Productive Bees's Bee Nest Diamond Helmet.

- Simple Hats's Bee Hat.

##### Blocks:
Fixed Crystalline Flower getting wrong enchantments if you are in flower's UI connected to server and someone else opens a flower as well.

Fixed Crystalline Flower xp requirements getting messed up on servers if use sets config to use more than 32,767 xp for a tier.

Fixed Crystalline Flower selecting wrong enchantment on servers if there is more than 127 enchantments in the UI.

Nerfed Crystalline flower so it takes 1634xp to reach max tier instead of 1451xp.

Fixed mobs trying to pathfind through Honey Cocoon instead of around or on top.

##### Fluids:
Sugar Water fluid now can allow Lilypads to be placed on it. Vanilla had a hardcoded check for vanilla water in the Lilypad code originally.

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

(Forge): Chance of spawning Enemy Expansion's Ladybug mob in Hanging Gardens structure.

(Forge): Enemy Expansion's Wasp mob spawners may rarely spawn in Cell Maze, Battle Cubes, and Pollinated Streams structures.

(Forge/Fabric/Quilt): Better Animal Plus's Tarantula mob spawners may rarely spawn in Spider Infested Bee Dungeons and Pollinated Streams structures.

(Forge): Blue Skies's Nested Spider mob spawners may rarely spawn in Spider Infested Bee Dungeons.

(Forge): Blue Skies's Venom Spider mob spawners may rarely spawn in Cell Maze and Battle Cubes structures.

(Forge/Fabric/Quilt): Twilight Forest's Hedge Spider mob spawners may rarely spawn in Spider Infested Bee Dungeons, Cell Maze, and Pollinated Streams structures.

(Forge/Fabric/Quilt): Twilight Forest's Swarm Spider mob spawners may rarely spawn in Spider Infested Bee Dungeons and Cell Maze structures.

(Forge): Alchemists Garden's Spiderling mob spawners may rarely spawn in Spider Infested Bee Dungeons, Cell Maze, and Pollinated Streams structures.

(Forge): Tofucraft's Tofu Spider mob spawners may rarely spawn in Cell Maze and Pollinated Streams structures.

(Forge): Born In Chaos's Wither Spider mob spawners may rarely spawn in Battle Cubes structures.

(Forge): Earth Mobs Mod's Bone Spider mob spawners may rarely spawn in Battle Cubes structures.

(Forge): Earth Mobs Mod's Stray Bone Spider mob spawners may rarely spawn in Battle Cubes structures.

(Forge/Fabric/Quilt): Earth To Java Mobs's Bone Spider mob spawners may rarely spawn in Battle Cubes structures.

(Forge): Minecraft Earth Mod's Bone Spider mob spawners may rarely spawn in Battle Cubes structures.

(Forge): Canes Wonderful Spiders's Jumping, Wolf, and Hard Shell Spider mob spawners may rarely spawn in Spider Infested Bee Dungeons structures.

(Forge): Canes Wonderful Spiders's Black Widow and Orb Weaver mob spawners may rarely spawn in Battle Cubes structures.
