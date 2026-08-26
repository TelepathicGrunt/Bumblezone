### **(V.7.16.0 Changes) (1.21.1 Minecraft)**

#### Teleportation:
Fixed bug where teleporting Bumblezone born entities out of Bumblezone does not put them at top land at 0, 0 spot in Overworld.

When teleporting Bumblezone born entities out of Bumblezone and 0, 0 spot in overworld has a roof at max height, the mod will 
 now try and search for a surface under that roof to teleport entity to.

#### Entities:
Tiny optimization for Sentry Watcher/Cosmic Crystal.

Reduced duration of Purple Spike's poison effect.

#### Items:
Fixed how damage is calculated for thrown Stinger Spear and ensure it always poisons non-undead mobs.

Fixed Carpenter Bee Boots to give mining boost from boot's Efficiency enchantments, not whatever is in hand.

Carpenter Bee Boots tooltip now clarifies mining enchantments applies to when mining by feet.

Honeybee Leggings now have a waist and one variant of the leggings will render the waist above Bumblezone's chestplates (but under all other chestplates)

Added Trials Spawners and Vaults as blocks that Knowing Essence can highlight.

Fixed thrown Dirt Pellet not being in `minecraft:redirectable_projectile` entity type tag.

#### Structures:
Slightly reduced enemy mob speed boosts from Blue and Red Essence Arenas.

#### Effects:
(NeoForge): Fixed Wrath of the Hive not changing music, not changing fog color, not respecting Peaceful mode, and not respecting allowWrathOfTheHiveOutsideBumblezone. Now all fixed.

#### Music/Sounds:
Lowered Bumblezone dimension's buzzing ambience sound a little

(NeoForge): Fixed a variety of music playing/stopping issues.

#### Mod Compat:
Fixed and improved compat with Forestry. Now Bumblezone dimension spawns with spikes that have Forestry's blocks, 
 cave surfaces have Forestry's blocks, and trees in hanging Gardens will have some of Forestry's blocks. In addition,
 Forestry's hive blocks are now usable to teleport into Bumblezone. The Forestry Compat datapack is now no longer
 force enabled, instead enabled by default but players can disable it during world creation screen or in-game.