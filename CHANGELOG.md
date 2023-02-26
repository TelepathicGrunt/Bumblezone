### **(V.6.6.11 Changes) (1.19.3 Minecraft)**

##### Teleporting:
Removed the partial bounding box check for teleporting projectiles hitting entities wearing tagged armor that allows for teleporting to Bumblezone.
 So hitting top half of Armor Stand while it has a mod's Bee Hive Boots should teleport thrower to Bumblezone without fighting hitboxes.

(Fabric/Quilt): Added support for Travel Staff's Travel Staff and Teleportation enchantment to match the compat Forge has with Travel Anchor mod.

Fixed `the_bumblezone:dimension_teleportation\forced_allowed_teleportable_blocks` block tag so it actually works.
 Clearly no one ever actually used this tag... lol


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


### **(V.6.6.8 Changes) (1.19.3 Minecraft)**

##### Blocks:
Fix Crystalline Flower space checking failing on Dedicated Servers if menu for it is opened while flower is greater than 32,767 blocks from world origin.

##### Advancements:
Added a new advancements that is given to player when they are near a beehive to get them to look at 
 Bumblezone's advancements to know how to enter dimension. The Point of Interest used to determine if 
 the player is near a Beehive or Bee Nest (even modded ones) is controlled by this new `point_of_interest_type` tag:
 `the_bumblezone:is_near_beehive_advancement_trigger`

Made most Bumblezone advancements no longer announce to the chat to reduce chat spam.


### **(V.6.6.7 Changes) (1.19.3 Minecraft)**

##### Mod Compat:
(Forge): Adjusted the rates of Productive Bees's combs throughout Bumblezone a bit.

Cleaned up and improved the tags for Hanging Gardens to pull in more modded logs and flowers and disallow more non-flowers from other mods.

Removed Biome Makeover's Moth from spawning in Hanging Gardens until this PR gets merged and released: https://github.com/Lemonszz/Biome-Makeover/pull/202
 The entity spawning in Hanging Gardens is controlled by this entity type tag: `the_bumblezone:hanging_gardens/initial_spawn_entities`


### **(V.6.6.6 Changes) (1.19.3 Minecraft)**

##### Items:
(Forge): Fixed Bee armor's special abilities not working.

##### Blocks:
Made Crystalline Flower not reset fall distance calculations for entities falling into it.

##### Misc:
Slightly adjusted REI/JEI/EMI info on Crystalline Flower and Essence of the Bees


### **(V.6.6.5 Changes) (1.19.3 Minecraft)**

##### Entities:
Fixed Bee Queen still asking to do trades when angry.

##### Mod Compat:
(Fabric/Quilt): Blocked several Zenith enchants from Crystalline Flower for balance out of the box.
 Can be changed by `the_bumblezone:crystalline_flower/disallowed_enchantments` enchantment tag.

(Forge): Fixed crash when put on with another mod that jar-in-jars MixinExtras

(Fabric/Quilt): Added Bumblezone Honey and Royal Jelly fluids to `create:diving_fluids` fluid tag so that Copper Backtank works properly in those fluids for next Create update.


### **(V.6.6.4 Changes) (1.19.3 Minecraft)**

##### Structures:
Fixed structure heightmap checks not being in center of structure properly

##### Effects:
Made Bee Queen, Beehemoth, and other mod's mobs that have "bee" or "bumble_beast" in the name immune to Wrath of the Hive effect

##### Entities:
Beehemoth and Bee Queen are now immune to Sweet Berry Bush thorn damaging and slow effect.

##### Mod Compat:
(Forge): Fixed Pollen Puff compat with Honey Bucket mod's Moobloom. Should be spawning their Golden Bloom flower instead of Dandelion
 Also fixed pollen Puff not spawning Echoshrooms when hitting their Sculk Snail


### **(V.6.6.3 Changes) (1.19.3 Minecraft)**

##### Major:
Switched to using Arch in backend. Special thanks to ThatGravyBoat for doing the work!
 Please let me know if any bugs or issues arises as this porting took a large overhaul of the codebase and could have introduces some bugs.

(Fabric/Quilt): Continuity is no longer needed for connected textures in Bumblezone!

##### Dimension:
Renamed fogReducer config option to fogThickness and fixed it so it nows shows more of the dimension and scales the fog properly.
 Also fixes dimension rendering breaking when render distance is 30 or greater and a rendering optimizing mod is on.

##### Blocks:
Made XP Orbs no longer spawn pollen particles when moving through Pile of Pollen block.

Crystalline Flower can now be placed on Amethyst Blocks along with the Glistering Honey Crystal.
 This is controlled now by this block tag: `the_bumblezone:crystalline_flower/can_be_placed_on`
 Some modded crystal blocks are support. See tag for which. You can datapack the tag to add more blocks or replace the tag to remove entries.

Crystalline Flower won't attract xp orbs if at max tier. Xp orb attraction is slightly strengthened for when not at max tier.

Fixed crash that occurred when taking non-enchanted book out of Crystalline Flower UI after clicking an enchant.

##### Fluids:
Fixed it so Bees that spend too long in Honey Fluid and Royal Jelly Fluid will drown properly.

(Quilt/Fabric): Fixed Honey Fluid and Royal Jelly Fluid so mobs can float on top of it now.

##### Items:
Fixed thrown Pollen Puff able to multiply Flowering Azalea Leaves and other modded flowering leaves.

##### Entities:
Bee Queen will not get angry at attacking Players in Peaceful mode now.

When Bee Queen is angry, the translucent super trade item rendering will disappear until queen is calm again.

Bee Queen won't have many plank or plank-derivative blocks as part of the super trade system to reduce wood bloat in it.

Fixed default keybind for flying down on Beehemoth being Space instead of Caps Lock. Space is fly up. Caps Locks should be fly down.
 Go into keybind menu to fix this manually if this update doesn't automatically fix it for your game.

##### Effects:
Wrath of the Hive will not apply to players in Peaceful mode now.

##### Advancements:
Fixed Journey's End advancement to not include The Beginning advancement requirement. Was throwing off MC's advancement progress tracker.

##### Lang:
Added a few extra details on some blocks in the JEI/REI/EMI item descriptions.

##### Config:
(Forge): Moved the fog config options out of the dimension.toml file into the client.toml config file because fog is clientsided.

##### Mod Compat:
Made it so that Pollen Puff hitting baby Mooblooms from Bovines and Buttercups will spawn flowers too.

(Forge): Added Pollen Puff compat with Moobloom's (FlowerCow) mod on Forge.

(Forge): Added Pollen Puff compat with Honey Bucket mod's Moobloom and other mobs with mushrooms on them. 
 Their Golden Bloom flower will spawn in Hanging Gardens now

(Forge): Fixed crash with Productive Bees and also completely redid how natural worldgen comb spawning works.
 To add or remove specific combs from spawning, datapack replace this placed feature tag file:
 `data/the_bumblezone/tags/worldgen/placed_feature/productive_bees_combs.json`
 Otherwise, use the spawnProductiveBeesHoneycombVariants config option to turn them all off if desired.