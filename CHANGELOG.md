### **(V.6.6.14 Changes) (1.19.3 Minecraft)**

##### Items:
Fixed Honey Crystal Shard texture having 2 pixels not at 100% transparency

Fixed Bee Cannon and Crystal Cannon not being rotated correctly when viewed in 3rd person or on Armor Stands

##### Blocks:
Fixed waterlogged Super Candle wicks deleting the water when candle is broken.

##### Structures:
Fixed some structures spawning at world bottom instead of on land.


### **(V.6.6.13 Changes) (1.19.3 Minecraft)**

##### Teleportation:
Improved performance a bit with projectile impact code by ignoring projectiles with no owners.
 Also will help prevent potential issues with any non-owner projectiles from other mods.
 Fabric Botania's Mana Burst should no longer crash if Bumblezone and Twilight Forest is on together now.


### **(V.6.6.12 Changes) (1.19.3 Minecraft)**

##### Items:
Bee Stinger is now tagged as `minecraft:arrows` and my hacky mixins/events removed for it.
 Now any weapon that can take arrow tagged ammo should work with Bee Stinger too as ammo.
 Also fixes incompat where Bumblezone caused other mod's Bow/Crossbow ammo to be replaced with vanilla arrow when shooting in creative mode.


### **(V.6.6.11 Changes) (1.19.3 Minecraft)**

##### Teleporting:
Removed the partial bounding box check for teleporting projectiles hitting entities wearing tagged armor that allows for teleporting to Bumblezone.
 So hitting top half of Armor Stand while it has a mod's Bee Hive Boots should teleport thrower to Bumblezone without fighting hitboxes.

Fixed `the_bumblezone:dimension_teleportation\forced_allowed_teleportable_blocks` block tag so it actually works.
Clearly no one ever actually used this tag... lol

(Fabric/Quilt): Added support for Travel Staff's Travel Staff and Teleportation enchantment to match the compat Forge has with Travel Anchor mod.

(Fabric/Quilt): Fixed Bee Queen crash with Sodium by disabling the transparent item render on the mob if Sodium is installed.
 Now the Bee Queen's wanted item for bonus trades will be opaque instead of translucent if Sodium is on.

(Forge): Fixed v1.0.5 Dragon Enchants teleporting compat

##### Bee Aggression:
Changed default config value of strengthBoostLevel from 2 to 1 to make bees be a little less punishing. 
 Existing configs already generated will have to be edited by user or modpack maker as those will remain at 2 unless edited/deleted.

##### Misc:
(Fabric/Quilt): Now includes `No Indium?` mod within this jar so that users running Bumblezone with Sodium will get a message saying to download Indium.
 This is required because Sodium makes the Porous Honeycomb Block and Empty Brood Honeycomb Block invisible unless Indium is on.
 Reason is Sodium will break Fabric's rendering APIs but Indium adds back the Fabric API's rendering support back into Sodium.


### **(V.6.6.10 Changes) (1.19.3 Minecraft)**

##### Teleporting:
I recognized that having different modes for entering and exiting Bumblezone could get confusing along with beehive
 searching when exiting causing some delay on exiting. So instead, all teleportation modes have been removed
 in favor of exiting Bumblezone ALWAYS puts you back in the exact same spot you were at when you entered the
 dimension. If you entered the dimension by Enderpearl at a Beehive at 1000, 1000 coordinate in Overworld,
 you spawn around 250, 250 coordinate in Bumblezone. But no matter where you leave Bumblezone, you should
 now always be put back to 1000, 1000 coordinate in Overworld. No more searching for nearby Beehives.
 (Which should help speed up exiting Bumblezone too) Some config options were removed as they are no longer relevant.

Along with this change, some new tags are added to allow you to config how to enter Bumblezone with various items, projectiles, or enchantments!

- `the_bumblezone:dimension_teleportation/teleport_projectiles` entity type tag (Now you can add other projectiles besides Ender Pearls for teleporting to the dimension. Even Tridents or Snowballs!)

- `the_bumblezone:dimension_teleportation/item_right_clicked_beehive` item tag

- `the_bumblezone:dimension_teleportation/item_right_clicked_beehive_crouching` item tag

- `the_bumblezone:dimension_teleportation/do_item_right_click_check_earlier` item tag

- `the_bumblezone:dimension_teleportation/item_special_dedicated_compat` item tag (Only for Twilight Forest's Ender Bow behavior right now)

- `the_bumblezone:dimension_teleportation/any_item_right_clicked_beehive_with_enchant` enchantment tag

- `the_bumblezone:dimension_teleportation/any_item_right_clicked_beehive_with_enchant_crouching` enchantment tag
- 
- `the_bumblezone:dimension_teleportation/enchant_special_dedicated_compat` enchantment tag (Only for updated Dragon Enchants's End Step enchantment bhevaior right now)

Several existing teleporting based tags were moved into a `dimension_teleportation` folder to organize together all the teleportation stuff.

- `the_bumblezone:enderpearl_teleporting/target_entity_hit_anywhere` -> `the_bumblezone:dimension_teleportation/target_entity_hit_by_teleport_projectile_anywhere` entity type tag

- `the_bumblezone:enderpearl_teleporting/target_entity_hit_high` -> `the_bumblezone:dimension_teleportation/target_entity_hit_by_teleport_projectile_high` entity type tag

- `the_bumblezone:enderpearl_teleporting/target_entity_hit_low` -> `the_bumblezone:dimension_teleportation/target_entity_hit_by_teleport_projectile_low` entity type tag

- `the_bumblezone:enderpearl_teleporting/target_armor` -> `the_bumblezone:dimension_teleportation/target_armor_hit_by_teleport_projectile` item tag

- `the_bumblezone:enderpearl_teleporting/target_held_item` -> `the_bumblezone:dimension_teleportation/target_with_held_item_hit_by_teleport_projectile` item tag

Along with this, compat with several other mod's teleporting methods now also works to teleport the user into Bumblezone when used on Beehive/Bee Nest blocks!
The mods with this new compat are:

- Xtra Arrows's Ender Arrow variants when shot at block

- Evilcraft's Blood Pearl of Teleportation projectile

- Origins's Enderian ability to throw Ender Pearls

- Twilight Forest's Ender Bow but only when it shoots another entity tagged for allowing teleportation into Bumblezone such as Llamarama's Bumble Llama or Dreamland Biomes's Bumble Beast

- Dragon Enchants's End Step bow/crossbow enchantment will work if shooting at Beehive/Bee Nest. (Note: Update Dragon Enchant to newest version for this compat)

Adjusted teleportation to Bumblezone dimension so that it is far more likely to put you in a cave or on land instead of inside a wall.

Note to modders depending on Bumblezone, the BumblezoneAPI$runEnderpearlImpact method now takes a HitResult instead of a vec3.

##### Structures:
Pirate Ship structure now can have an Explorer Map to a few structures.

##### Mod Compat:
Llamarama's Mossy Llama now spawns Moss Carpet, Azalea, Flowering Azalea, or Blossom when hit with Pollen Puff

##### Lang:
Fixed small Honey Slime having wrong captions for some actions/sounds.

Replaced DrHesberus's Russian translation file due to incredibly poor translation and controversy surrounding him.
 Instead, new Russian translation is provided by MageInBlack! Special thanks to them!

If you know languages other than English and can do translations without using Google Translate or DeepL, etc,
 you can find my translation file here and submit new translations to me for your language of choice!
 https://github.com/TelepathicGrunt/Bumblezone/blob/1.19.3-Arch/common/src/main/resources/assets/the_bumblezone/lang/en_us.json
