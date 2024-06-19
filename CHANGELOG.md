### **(V.7.6.1 Changes) (1.21 Minecraft)**

##### Advancements:
Halved the requirements for many Queen's Desire advancements to reduce grind.

##### Mod Compat:
Fixed crash with Mekanism.

Fixed crash with JEI, EMI, and REI.


### **(V.7.6.0 Changes) (1.21 Minecraft)**

##### Major:
Ported to 1.21! Please report issues and bugs! There's bound to be some due to the workload to do this port.

##### Entities:
Bee Queen trading JSON files changed a bit with the result section to now allow specifying a result item's components.

##### Blocks:
Crystalline Flower now has a dedicated folder of `data/the_bumblezone/bz_crystalline_flower_data` where you can specify 
 exactly what xp amount a tag of items gives. You can now also disable the default 1xp item consuming too!

Potion Candle now has a dedicated folder of `data/the_bumblezone/bz_potion_candle_data` where you can specify limits
 to effects when attached to Potion Candle! Such as capping the maximum level of an effect, capping burn duration,
 or how long the effect lingers after leaving candle. Much easier now to balance Potion Candle for your modpack!

Potion Candle now is disallowed from having Hero of the Village effect, Bad Omen, Trial Omen, and Raid Omen. 
 Controlled by `the_bumblezone:potion_candle/disallowed_effects` effects tag.
