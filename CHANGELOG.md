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

##### Lang:
Updated ru_ru.json file. Special thanks to MageInBlack!