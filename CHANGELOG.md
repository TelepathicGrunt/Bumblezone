### **(V.7.11.6 Changes) (1.21.1 Minecraft)**

#### Structures:

Stopped Chorus Flower and a few other modded non-flowers from appearing in Structure's loot tables for the random flower entry.

#### Mod Compat:

Crystalline Flower can be placed on a few more mod's crystal-looking full blocks.

Throwing Pollen Puff at Variant and Ventures's Murk, Verdant, and Thicket will spawn Moss Carpet nearby sometimes.

Throwing Pollen Puff at Vanillabackport's Creaking will spawn Pale Moss Carpet and Open Eyeblossoms nearby sometimes.

Disallow some non-colorful modded flowers from showing in Hanging Gardens structure.


### **(V.7.11.5 Changes) (1.21.1 Minecraft)**

#### Structures:

Dying near the Essence Block in Sempiternal Sanctums will now move the player just outside the bounds of the sanctum's arena.
This will allow Gravestone/Tombstone mods to continue spawning their block and not risk item duplication issues with arena nor break arena's invincible blocks.
This does mean Bumblezone will no longer drop player items with unlimited despawn timer when Corail Tombstone is on and near Essence Block. Their tombstone block will spawn now.

#### Mod Compat:

Fixed Pollinated Pillar biome icon not showing properly in EMI Ores mod.

#### Lang:

Texaliuz updated es_ar.json file

PrincessStelllar updated pt_br.json file

mc-kaishixiaxue updated zh_cn.json file


### **(V.7.9.4 Changes) (1.20.1 Minecraft)**

#### Structures:
Added configs to change the Sempiternal Sanctum arena time frames. Config names are: 
 `blueArenaTimeFrameInTicks`, `greenArenaTimeFrameInTicks`, `purpleArenaTimeFrameInTicks`, 
 `redArenaTimeFrameInTicks`, `yellowArenaTimeFrameInTicks`, `whiteArenaTimeFrameInTicks`


### **(V.7.9.3 Changes) (1.20.1 Minecraft)**

#### Blocks:

Fixed Luminescent Wax Channel block not having reversed animation when horizontally sheared.


### **(V.7.9.2 Changes) (1.20.1 Minecraft)**

#### Entities:

Fixed tier 4 trades and holiday trades not working for Bee Queen.

Made Bee Queen trade files have more validation feedback on malformed files.

Added `variantBeeAfterWorldgenSpawnRate` config option so people can increase the rates of variant bees if they wish to do so.

#### Compat:

Fixed Buzzier Bees Glazed Porkchop not being feedable to bees. (PR from Toblerone0508)

#### Lang:

Romz24 updated and fixed ru_ru.json file


### **(V.7.9.1 Changes) (1.20.1 Minecraft)**

#### Misc:

Grass Blocks now can spread to nearby dirt in Bumblezone at any light value.

#### Lang:

AstardGrimoire updated and fixed ru_ru.json file

Texaliuz updated es_ar.json file


### **(V.7.9.0 Changes) (1.20.1 Minecraft)**

#### Enchantments:

Added a new treasure enchantment called Hive Lifeline that can only be put onto Bumblezone armor or some modded bee-themed armor.
 It will cause any entity attack damage to the player to be redirected to a nearby bee instead and put the armor piece on a brief cooldown!
 Can be obtained from Crystalline Flower, rarely from brushing any Suspicious Pile of Pollen block, or sometimes from libraries in Bumbling Beepartments.

#### Structures:

Bumbling Beepartments libraries now have properly randomized Chiseled Bookshelf contents and more possible enchantments they can hold.
 Frequency of Chiseled Bookshelves are increased as well.
 The following enchantment tag will control what kind of enchantments can show up in this structure: `the_bumblezone:structures/bumbling_beepartments_library`

#### Effects:

Wrath of the Hive, Protection of the Hive, and Hidden effects now should properly work on invisible bees.

#### Entities:

Adjusted position of the Trade Hint particle for Bee Queen.

#### Fluids:

(Fabric): Lava will transform into Sugar Infused Stone and Sugar Infused Cobblestone when touching Sugar Water. Matches Forge behavior.

#### Misc:

(Forge): Fixed Bumblezone welcome message not showing up when near Beehive for first time.

#### Lang:

zh_cn.json updated by BaoZouZac.