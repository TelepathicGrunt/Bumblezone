### **(V.7.2.5 Changes) (1.20.1 Minecraft)**

##### Blocks:
Fixed Heavy Air not pulling down players properly with increased gravity on client side.
 This issue was most noticeable on servers where if Elytra flying, the delay of when server syncs velocity
 could be too later and players dies instantly due to gravity increased velocity growing for too long.

##### Items:
Adjusted some code to make extra sure that killed bees with stingers only drop 1 Bee Stinger instead of multiple when other certain mods are present.

##### Advancements:
Removed some hacks and now use or hook into vanilla's `minecraft:recipe_crafted` advancement trigger for some Bumblezone advancements.

##### Enchantments:
Comb Cutter now treats negative Mining Fatigue as level 4 fatigue properly just like vanilla.

##### Configs:
Added essenceItemHUDVisualEffectLayers and essenceItemHUDVisualEffectSpeed, so you can change the HUD effect overlay when holding essence item in offhand slot.
 Set essenceItemHUDVisualEffectLayers to 0 to disable the HUD overlay entirely.
 Set essenceItemHUDVisualEffectSpeed to 0 to disable the spinning effect entirely for the HUD effect.