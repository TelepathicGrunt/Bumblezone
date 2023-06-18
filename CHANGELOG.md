### **(V.6.7.24 Changes) (1.19.2 Minecraft)**

##### Blocks:
Cleaned up baked models for Porous Honeycomb Block and Empty Brood Block. Should work with ModernFix's Dynamic Resources option. 
 Special thanks to Embeddedt for the PR!

##### Items:
Removed Carvable Wax from forge:wax item tag to prevent recipe conflict with other mod's candle recipes.

Added configs to turn off Brewing Recipes

Added two new nbt that modpack makers can set for Bumble Bee Chestplate to change the flight time.
 Add "forcedMaxFlyingTickTime" int to the item's nbt to set the new maximum flight time for that item.
 Add "requiredGearCountForForcedFlyingTime" to make the above nbt only apply if player has the correct number of bee gear equipped.
 "forcedMaxFlyingTickTime", if present, will always apply if "requiredGearCountForForcedFlyingTime" is not present

Fixed one Bumble Bee Chestplate's wings being off when viewed from above and is flying.
