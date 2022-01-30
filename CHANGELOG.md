### **(V.4.3.0 Changes) (1.18.1 Minecraft)**

- glazed cocoon (test breaking later)
- stinger spear (test durability later)

##### Blocks:
Fixed some honey filled blocks not consuming Glass Bottles when collecting Honey Bottles from the blocks.

Tall stacks of Pile of Pollen blocks now will hide entities inside them by giving them the new Hidden effect. 
 Monsters will now have to be closer to see the player buried in Pile of Pollen.
 However, once seen, the monsters will not be fooled if you dive back into the Pile of Pollen.
 Effective for hiding from swarms of angry bees.

Fixed Pile of Pollen sometimes not removing the FallingBlockEntity instance of itself from the client.

##### Items:
Honey Crystal Shields now have an internal "ShieldLevel" nbt to keep track of its strength.
 The shield level is increased by 1 each time the shield has more than 1/5th of its durability repaired.
 The maximum shield level now is 10 and shields now start with more durability initially.
 Legacy Honey Crystal Shields obtained before this update should automatically be converted to the new shield level system based on their RepairCost.

Honey Crystal Shield has a chance of being disabled by axes instead of always being disabled by axes. Matches vanilla shield behavior.

##### Entities:
Fixed Honey Slime bounding boxes.

Fixed Honey Slime not following creative mode players with Sugar in hand.

Fixed Beehemoth not being Arthropod mob type in backend. 
 Bane of Arthropod enchantment now should inflect more damage on Beehemoth.

##### Bee Aggression:
Fixed bees not getting automatically angry at bugs and bears in the Bumblezone dimension.

Fixed up the checks for what mobs are bears or non-bee arthropods so that they get Wrath of the Hive in Bumblezone dimension. 
 The new checks are much safer and shouldn't break other mods anymore.
 It may also be better and not have as many false positives/negatives.

##### Effects:
Protection of the Hive will no longer be removed when taking honey from blocks picking up/mining various honey blocks.
 HOWEVER, hitting a bee or mining/taking honey from Honeycomb Brood Blocks will still remove Protection of the Hive.

Hidden effect is added which is given to entities hiding in Pile of pollen. 
 It reduces range of sight fo hostiles to see the hidden mob.

##### Teleportation:
Fixed Piston teleportation no longer working when `REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT` block tag is non-empty.

##### Configs:
Added beehemothTriggersWrath config option. It is set to false by default.
 If turned on, any mobs that hurts a Beehemoth and is not the owner of the Beehemoth, that mob will get Wrath of the Hive effect.

Added playWrathOfHiveEffectMusic config option to allow players to turn off the music that plays when you have Wrath of the Hive effect.