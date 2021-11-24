### **(V.3.4.1 Changes) (1.17.1 Minecraft)**

##### Misc:
Updated Fabric API code usage to not use deprecated code to prevent blowing up in future fabric API updates. Hopefully.


### **(V.3.4.0 Changes) (1.17.1 Minecraft)**

##### Items:
Added Bee Bread which can be crafted from pollen puff and honey bottles (or 3 pollen puff + honey bucket)
 When fed to bees or Beehemoth, it will grant Beenergized effect and make them go faster for 5 minutes!
 Feed them multiple times to increase the flying speed boost up to level 3 where bees goes zoom!
 When player eats the item, it acts as a better food source than honey bottle but does not cure player of poison.
 Player will get Beenergized effect as well but only to level 1 and nausea for a few seconds.

##### Blocks:
Honeycomb Brood Blocks has a chance of dropping Bee Bread when broken.

##### Entities:
Beehemoth is now immune to block suffocation damage for a bit after teleporting to and from the bumblezone.
 (Uses portal cooldown to know when to be invincible to block suffocation)

Beehemoth now reads both Flying Speed attribute and flyingSpeed field when riding the mob or it is randomly flying around.
 (changes to flyingSpeed field overrides Flying Speed attribute changes)

Beehemoth will no longer become untamed when friendship reached 0 or below. Instead, it'll retain its owner but still
prevent any riding until it is feed to bring its friendship above 0.

##### Effects:
Beenergized effect is now added! This will increase the flying speed of any mob it is affecting.
If on player, it does not have any effect it seems.

#### Mod Compat:
Added support for Notify - https://www.curseforge.com/minecraft/mc-mods/notify
 Notify will let you know if a new update for this mod is out on the Mod Menu screen.


### **(V.3.3.2 Changes) (1.17.1 Minecraft)**

##### Teleportation:
Fixed bug where setting Teleportation Mode to 2 or 3 and trying to exit Bumblezone on an entity that originated from Bumblezone would spawn player under Bedrock in Overworld.


### **(V.3.3.1 Changes) (1.17.1 Minecraft)**

##### Entities:
Beehemoth will now stay still when crouch right-clicked like how you make dogs and cats sit.
 If it is sitting, shift right click with empty hand to remove saddle. Shift right click while holding any item to make it unsit (or ride it to unsit)
 To feed Beehemoth, just right click while holding any honey feeding item. No crouching needed.

Right clicking Beehemoth without crouching will always make you ride the mob. No longer need an empty hand.

##### Lang:
Added english translations for advancements to the other language files so the translation keys don't show.


### **(V.3.3.0 Changes) (1.17.1 Minecraft)**

##### Advancements:
Added new advancements for this mod to add some progression and high xp rewards!
 Note: Progress or conditions met before this update will not contribute towards the new advancements.

##### Entities:
Fixed Honey Slime not giving Honey Bottle when harvested with Glass Bottle.

Honey Slimes have a bit more health, a bit more attack power, and will jump faster and higher when angry as well as stay angry longer.

Killing adult Honey Slimes with looting enchantment may drop Sugar and/or Slime Balls.
 Before, it was the child that had this feature instead of the adult by mistake.

Fixed issue where trying to remove Pollen Puff from bees causes some logspam.

Fixed using items on Bees/Honey Slime may cause the item's use to continue pass the entity. 
 Such as feeding bees a Honey Bucket will place honey behind the entity at well.

##### Items:
Fixed durability bar on Honey Crystal Shield to correctly show how much durability is really left.

##### Effects:
Protection of the Hive on the player will now be removed if you hurt a Bee outside The Bumblezone dimension.

##### Recipes:
Renamed the_bumblezone:beeswax_planks_to_bee_nests recipe json to the_bumblezone:beehive_beeswax_to_bee_nest.