### **(V.4.3.0 Changes) (1.18.1 Minecraft)**

##### Dimension:
The coordinate scale has been shrunk from 10 to 4. 
 Now exiting Bumblezone at 100, -3, 1000 will put you around 400, ?, 4000 in the destination dimension.

Teleportation mode 2 (selectable in config) has been fixed so that it properly places players in their destination without coordinate scaling done.

##### Blocks:
Added Honey Cocoon which is a new storage block! Found in Cell Maze structure, Honeycomb Cave structure, and Bee Dungeons.
 They turn Empty Honeycomb Brood Blocks into filled Brood Blocks if they are inside this cocoon with a bee feeding item like Honey Bottles over time!
 If waterlogged with water above, they will slowly drop items above over time.
 When broken, they drop themselves and all items unless you use Silk Touch, then they keep their items inside like Shulker Boxes do.

Fixed some honey filled blocks not consuming Glass Bottles when collecting Honey Bottles from the blocks.

Tall stacks of Pile of Pollen blocks now will hide entities inside them by giving them the new Hidden effect. 
 Monsters will now have to be closer to see the player buried in Pile of Pollen.
 However, once seen, the monsters will not be fooled if you dive back into the Pile of Pollen.
 Effective for hiding from swarms of angry bees.

Fixed Pile of Pollen sometimes not removing the FallingBlockEntity instance of itself from the client.

Sticky Honey Residue/Redstone's collision shape is now cached so it has better performance.

Sticky Honey Residue/Redstone no longer extend VineBlock now.

Sticky Honey Residue/Redstone now has particle effects (dripping honey particle and the redstone version has redstone particles when powered)

Fixed Sugar Water Block not being able to waterlog some of Bumblezone's blocks like Honey Crystals.

Fixed Honey Sticky Webs and Honey Sticky Residue attaching an empty tag to held items when right clicked which was preventing that item from stacking.

##### Items:
Stinger Spear item is added! They are rare throwable weapons found in Cell Maze structure. 
 Can be repaired by Flint and inflicts short weak poison on any non-undead mob it hits.
 Has 1 dedicated enchantment for it called Neurotoxins. See Enchantment section for more info.

Honey Crystal Shields now have an internal "ShieldLevel" nbt to keep track of its strength.
 The shield level is increased by 1 each time the shield has more than 1/5th of its durability repaired.
 The maximum shield level now is 10 and shields now start with more durability initially.
 Legacy Honey Crystal Shields obtained before this update should automatically be converted to the new shield level system based on their RepairCost.

Honey Crystal Shield has a chance of being disabled by axes instead of always being disabled by axes. Matches vanilla shield behavior.

##### Enchantments:
Neurotoxins enchantment added that only applies to Stinger Spear item. (Max enchantment level is 2)
 Will cause the weapon to have a chance of causing Paralysis Effect on non-undead mobs for 5 seconds per enchantment level. 
 Has a lower chance of paralyzing if the mob has more health remaining with 10% chance at its lowest.
 Every hit that does not cause paralysis will increase the chance of the next hit causing paralysis.

Potent Poison enchantment is available for all trident-like items including Stinger Spear. (Max enchantment level is 3)
 When the weapon hits a non-undead mob, it inflicts poison effect on them with higher enchantment levels increasing
 the duration and level 3 increases the poison level. If on Stinger Spear, the level of poison is increased by an extra level.

Fixed Comb Cutter showing up for invalid items in the Enchantment Table.

##### Entities:
Fixed Honey Slime bounding boxes.

Fixed Honey Slime not following creative mode players with Sugar in hand.

Fixed Beehemoth not being Arthropod mob type in backend. 
 Bane of Arthropod enchantment now should inflect more damage on Beehemoth.

##### Structures:
Added Cell Maze structure to all Bumblezone biomes! It will inflict Wrath of the Hive on you if you enter it without Protection of the Hive effect.
 Enjoy the fun loot and exploration of this small maze-like structure!

Pollinated Stream structure is now more common in Pollinated Fields and Pollinated Pillar biomes.
 Land will be made around this structure now to help prevent it from floating in midair.

##### Bee Aggression:
Fixed bees not getting automatically angry at bugs and bears in the Bumblezone dimension.

Fixed up the checks for what mobs are bears or non-bee arthropods so that they get Wrath of the Hive in Bumblezone dimension. 
 The new checks are much safer and shouldn't break other mods anymore.
 It may also be better and not have as many false positives/negatives.

Bees fed and calmed while having Wrath of the Hive will now apply Protection of the Hive right away.

##### Effects:
Protection of the Hive will no longer be removed when taking honey from blocks picking up/mining various honey blocks.
 HOWEVER, hitting a bee or mining/taking honey from Honeycomb Brood Blocks will still remove Protection of the Hive.

Hidden effect is added which is given to entities hiding in Pile of pollen. 
 It reduces range of sight fo hostiles to see the hidden mob.

Paralysis effect is added and is caused by Neurotoxins enchantment right now. This sets the mob's immobile method
 to return true which causes many mobs to stand still. They will also shake when paralyzed and give off yellow particle effects.
 Some mobs may be able to still do some actions such as Witches drinking potions.

##### Teleportation:
Fixed Piston teleportation no longer working when `REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT` block tag is non-empty.

##### Advancements:
Added more Advancements and moved some around to different branches.

##### Configs:
Added beehemothTriggersWrath config option. It is set to false by default.
 If turned on, any mobs that hurts a Beehemoth and is not the owner of the Beehemoth, that mob will get Wrath of the Hive effect.

Added playWrathOfHiveEffectMusic config option to allow players to turn off the music that plays when you have Wrath of the Hive effect.

Default teleportation mode in config is changed from 1 to 3.