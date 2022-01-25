### **(V.4.3.0 Changes) (1.18.1 Minecraft)**

##### Blocks:
Fixed some honey filled blocks not consuming Glass Bottles when collecting Honey Bottles from the blocks.

Tall stacks of Pile of Pollen blocks now will hide entities inside them by giving them the new Hidden effect.
Monsters will now have to be closer to see the player buried in Pile of Pollen.
 However, once seen, the monsters will not be fooled if you dive back into the Pile of Pollen.
 Effective for hiding from swarms of angry bees.

Honey Crystal Shield has a chance of being disabled by axes instead of always being disabled by axes. Matches vanilla shield behavior.

##### Items:
Honey Crystal Shields now have an internal "ShieldLevel" nbt to keep track of its strength.
 The shield level is increased by 1 each time the shield has more than 1/5th of its durability repaired.
 The maximum shield level now is 10 and shields now start with more durability initially.
 Legacy Honey Crystal Shields obtained before this update should automatically be converted to the new shield level system based on their RepairCost.

Honey Crystal Shield has a chance of being disabled by axes instead of always being disabled by axes. Matches vanilla shield behavior.

##### Entities:
Fixed Honey Slime bounding boxes.

Fixed Honey Slime not following creative mode players with Sugar in hand.

##### Effects:
Protection of the Hive will no longer be removed when taking honey from blocks picking up/mining various honey blocks.
 HOWEVER, hitting a bee or mining/taking honey from Honeycomb Brood Blocks will still remove Protection of the Hive.

Hidden effect is added which is given to entities hiding in Pile of pollen.
 It reduces range of sight fo hostiles to see the hidden mob.

##### Teleportation:
Fixed Piston teleportation no longer working when `REQUIRED_BLOCKS_UNDER_HIVE_TO_TELEPORT` block tag is non-empty.
