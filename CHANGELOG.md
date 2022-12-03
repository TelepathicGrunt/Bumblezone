### **(V.6.4.6 Changes) (1.19.2 Minecraft)**

##### Advancements:
Changed it so that Bumblezone's advancement screen is always visible even if you have no completed a prior advancement before.

Reorder advancements so that the teleportation advancements are shown first.

Renamed and adjusted descriptions for a few advancements.

Teleporting out of Bumblezone advancement now does not have a speed requirement and can be granted if exiting below or above dimension bounds.

##### Dimension:
Very slight color change to dimension fog color.
 Remember folks, there's config options to change the dimension's fog color, thickness, or turn it off.

##### Bee Queen:
Forgot to add two of Bumblezone's Music Discs into the tier 4 Bee Queen trades.

##### Entities:
Nerfed Beehemoth flying speeds a bit to better balance it. Base speed can still be changed in config.

##### Mod Compat:
SlimyBoyos mod now lets Honey Slime pick up items.


### **(V.6.4.5 Changes) (1.19.2 Minecraft)**

##### Blocks:
Pile of Pollen no longer slows Beehemoth or Bees down when inside it.

Pile of Pollen now more optimized with its Hidden effect. Less checking occurs while in block.
 Hidden effect is granted when Pile of Pollen covers up to just below the eye's height (Hidden 1) 
 and when eyes are fully covered by Pile of Pollen, bees will give up chasing you unless you exit the Pile of Pollen (Hidden 2)
 Hidden effect of any level will act like 100% Invisibility effect where mobs won't see you until you touch/hit them. (Or they previously seen you)

Sticky Honey Residue/Sticky Honey Redstone is a bit more optimized now too.

Honey Web and Redstone Honey Web cannot apply slowness/stickiness to creative players now.

##### Items:
Carpenter Bee Boots now only mine the below wood, leaves, honeycomb, or wax block when looking downward.
 More intuitive and prevents accidental block mining.

##### Entities:
Fixed Bee Queen reward item being spawned too far off to the side.

Honey Slime is now immune to the slowness/stickiness applied by Sticky Honey Residue, Sticky Honey Redstone, Honey Web and Redstone Honey Web.

Hopefully fixed Honey Slimes spawning on slopes and suffocating in blocks when chunk is first created.

##### Enchantments:
Condensed Bumblezone's enchantment descriptions that gets shown by Enchantment Description mod.

##### Mod Compat:
Added Bee Queen randomization trades for dyed items from Applied Energistics 2, Another Furniture, Bontany Pots, Companion,
 Cat Walks Inc, Farmer's Delight, Kibe, Modern Industrialization, Snowy Spirit, and Supplementaries.


### **(V.6.4.4 Changes) (1.19.2 Minecraft)**

##### Blocks:
Set the base of lit Super Candles/Incense Candles to return fire damage type for pathfinding.
 Should help stop other mod's mobs from pathfinding through the lit candles and burning.

##### Items:
Increased the durability for all Bumblezone bee armor by 2.4x.

##### Features:
Fixed checks for valid spot for Bee Dungeons/Spider Infested Bee Dungeons.

##### Entities:
Added three tags to make it easier to define what items will lure Beehemoth and what item can lure/breed Honey Slime.
 Honey Slime will now follow players holding Charm, VanillaTweaks, or Supplementaries's Sugar Block and can be bred with that along with vanilla's Sugar item.
 `the_bumblezone:mob_luring/beehemoth`
 `the_bumblezone:mob_luring/beehemoth_fast_luring`
 `the_bumblezone:mob_luring/honey_slime`

##### Advancements:
Mention that Honey Slime requires Sugar to breed in the Queen's Desire advancement for it.


### **(V.6.4.3 Changes) (1.19.2 Minecraft)**

##### Structures:
Hanging Garden structure will now be able to randomly spawn flowering leaves or logs from other mods!
 This includes some leaves/logs from Oh The Biomes You'll Go, Biomes O Plenty, Quark, Ecologics, Terrestria, 
 Fruit Trees, Cherry Blossom Grotto, Colorful Azaleas, Blossom, and Aurora's Decorations!
 The new block tags that control this are:
 `the_bumblezone:allowed_hanging_garden_leaves`
 `the_bumblezone:allowed_hanging_garden_logs`
 `the_bumblezone:blacklisted_hanging_garden_leaves`
 `the_bumblezone:blacklisted_hanging_garden_logs`

##### Items:
Pollen Puff pollination json files will now safely skip invalid entries and parse the rest of the json file if a typo is done on a mob name.

##### Mod Compat:
Changed my mind and now Biome Makeover's Black Thistle and Blue Skies's Lucentroot flowers no longer spawn in Hanging Garden structure now.
 Also blacklisted several flowers from Natural Decor Mod and Natural Expansion mod due to not looking good in structure. 


### **(V.6.4.2 Changes) (1.19.2 Minecraft)**

##### Misc:
Fixed bug with vanilla Bees that make them not threadsafe when created as part of worldgen threaded chunk creation.
 Very niche rare crash to even get to happen but good for me to still patch just in case it does cause issues for people rarely.
 Could be cause of some crashes on AoF6 which is why this update is released to rule out a vanilla bee issue and harden Bumblezone against the crash.

Cleaned up some more mixins to make them more stackable with other people's mixins

##### Fluids:
Significantly fixed rendering issues with Honey Fluid and Royal Jelly Fluid.
 Also fixed particles from fluid spawning way out of the fluid at times.

Fixed Honey Fluid not falling when in midair.

##### Items:
Fixed Pollen Puff sometimes placing blocks at invalid locations like Biome Makeover's Moth Blossom flower in midair.

##### Mod Compat:
Bosses of Mass Destruction's Void Lily, Twilight Forests's Thorn Rose, Sria's Flowers's small flowers,
and Biome Makeover's Black Thistle and Foxglove can spawn in Hanging Garden structure now.

Farmer's Delight's Wild Tomatoes plant cannot spawn in Hanging Gardens structure now.

Biome Makeover's Black Thistle and Foxglove can be multiplied by Pollen Puff now.


### **(V.6.4.1) (1.19.2 Minecraft)**

##### Fluids:
Fixed Royal Jelly Fluid missing visual overlays and player physics when inside fluid.
 Had to update because this is a big issue. Can't have straight up broken fluids...

Fixed being able to "jump" while on ground inside Royal Jelly Fluid and Sugar Water Fluid.

##### Structures:
Increased number of threads spawned by Bumblezone so multiple Hive Temple maps and Honey Compasses can search at once from 1 to 3.

##### Misc:
Fixed LGBT+ and Ukraine Bee Skin configs fighting each other on applying. And changed the default config values for them.

Went through and cleaned up and modified several mixins to make some of them less hacky.


### **(V.6.4.0) (1.19.2 Minecraft)**

##### Major:
Ported Fabric Bumblezone stuff to Fabric version to make Fabric version be in parity with v6.4.2 Fabric Bumblezone.
 Updates will remain far and few in between for Fabric Bumblezone or delayed.