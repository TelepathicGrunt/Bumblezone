import nbtlib as nbt
from pathlib import Path
import collections.abc
import os

# https://github.com/vberlier/nbtlib
#--------------------------------------------------------------------------------------------

blockPalette = {}

originalBiome = "blue"
newBiome = "yellow"
string_blacklist = []
conversion_partial_dict = {
}
conversion_exact_dict = {
    "minecraft:blue_orchid":                   "minecraft:dandelion",
    "the_bumblezone:luminescent_wax_channel_blue":  "the_bumblezone:luminescent_wax_channel_yellow",
    "the_bumblezone:luminescent_wax_corner_blue":  "the_bumblezone:luminescent_wax_corner_yellow",
    "the_bumblezone:luminescent_wax_node_blue":  "the_bumblezone:luminescent_wax_node_yellow",
    "the_bumblezone:sempiternal_sanctum/blue/exit_vent":  "the_bumblezone:sempiternal_sanctum/yellow/exit_vent",
    "the_bumblezone:sempiternal_sanctum/blue/rootmin":  "the_bumblezone:sempiternal_sanctum/yellow/rootmin",
    "the_bumblezone:sempiternal_sanctum/blue/sanctum_body_side":  "the_bumblezone:sempiternal_sanctum/yellow/sanctum_body_side",
    "the_bumblezone:sempiternal_sanctum/blue/welcome_chamber":  "the_bumblezone:sempiternal_sanctum/yellow/welcome_chamber"
}
#-------------------------------------------------------------------------------------------

def string_replacer(nbt_string):
    if nbt_string not in string_blacklist:
        for key, replacement in conversion_exact_dict.items():
            if nbt_string == key:
                replacementValue = nbt_string.replace(key, replacement)
                blockPalette[nbt_string] = replacementValue
                return nbt.String(replacementValue)
        for key, replacement in conversion_partial_dict.items():
            if key in nbt_string:
                replacementValue = nbt_string.replace(key, replacement)
                blockPalette[nbt_string] = replacementValue
                nbt_string = nbt.String(replacementValue)
    return nbt_string

def property_replacer(nbt_key, nbt_string, property_name, value_to_replace, new_value):
    if nbt_key == property_name:
        if nbt_string == value_to_replace:
            blockPalette[nbt_string] = new_value
            return new_value
    return nbt.String(nbt_string)



def traverse_dicts(nbt_list):
    if isinstance(nbt_list, collections.abc.Mapping):
        '''
        if 'size' in nbt_list:
            nbt_list['size'][1].value = 32
        '''
        
        if 'palette' in nbt_list:
            for entry in nbt_list['palette']:
                if entry.get('Name') not in blockPalette.keys():
                    blockPalette[entry.get('Name')] = entry.get('Name')

        '''
        for key, entry in nbt_list.items():
            if key == "pos" or key == "blockPos" or key == "size":
                entry[1].value = entry[1].value + 2
        '''

        nbt_list.pop('SleepingX', None)
        nbt_list.pop('SleepingY', None)
        nbt_list.pop('SleepingZ', None)
        nbt_list.pop('blockEntityUuid', None)
        
        if 'Attributes' in nbt_list:
            attributes = nbt_list['Attributes']
            for entry in attributes:
                if entry["Name"] == "forge:entity_gravity" or entry["Name"] == "forge:step_height_addition" or entry["Name"] == "forge:step_height":
                    nbt_list['Attributes'].remove(entry)
                if entry["Name"] == "neoforge:entity_gravity" or entry["Name"] == "neoforge:step_height":
                    nbt_list['Attributes'].remove(entry)
        
        for key, entry in nbt_list.items():

            if isinstance(entry, nbt.List) or isinstance(entry, nbt.Compound):
                traverse_dicts(entry)
            elif isinstance(entry, nbt.String):
                nbt_list[key] = string_replacer(entry)

            #property_replacer(key, entry, "PersistenceRequired", 0, 1)
            #property_replacer(key, entry, "waterlogged", "true", "false")
            #property_replacer(key, entry, "waterlogged", "false", "true")
            #property_replacer(key, entry, "joint", "rollable", "aligned")


    elif isinstance(nbt_list, nbt.List) or isinstance(nbt_list, nbt.Compound):
        for entry in nbt_list:
            if isinstance(entry, nbt.Int):
                continue

            if isinstance(nbt_list, nbt.List) or isinstance(entry, nbt.Compound):
                traverse_dicts(entry)
            elif isinstance(entry, nbt.String):
                nbt_list[key] = string_replacer(entry)

for (subdir, dirs, files) in os.walk("toconvert", topdown=True):
    for file in files:
        directory = subdir + os.sep
        filepath = directory + file

        if filepath.endswith(".nbt"): 
            nbtfile = nbt.load(filepath)
            traverse_dicts(nbtfile)

            directory = directory.replace("toconvert", "converted").replace(originalBiome, newBiome)
            Path(directory).mkdir(parents=True, exist_ok=True)
            newFile = directory + file.replace(originalBiome, newBiome)

            nbtfile.save(newFile)
            continue
        else:
            continue

for x in sorted(blockPalette.items()):
  printString = "    \""+x[0]+"\":"
  for y in range(max(40 - len(x[0]), 2)):
    printString = printString + " "
  printString = printString + "\""+x[1]+"\","
  print(printString)

print("FINISHED!")
input()

'''
# Oak
originalBiome = "plains"
newBiome = "oak"
string_blacklist = []
conversion_partial_dict = {
    "minecraft:village/plains/villagers":      "repurposed_structures:villages/oak/villagers",
    "minecraft:village/plains/zombie/villagers":      "repurposed_structures:villages/oak/zombie/villagers"
}
conversion_exact_dict = {   
    "minecraft:cobblestone":                   "minecraft:oak_wood",
    "minecraft:cobblestone_wall":              "minecraft:mossy_cobblestone_wall",
    "minecraft:oak_log":                       "minecraft:stripped_oak_log",
    "minecraft:oak_planks":                    "minecraft:oak_wood",
    "minecraft:red_carpet":                    "minecraft:red_carpet",
    "minecraft:stripped_oak_log":              "minecraft:stripped_oak_log",
    "minecraft:white_carpet":                  "minecraft:black_carpet",
    "minecraft:yellow_carpet":                 "minecraft:brown_carpet",
}

# Birch
originalBiome = "plains"
newBiome = "birch"
string_blacklist = []
conversion_partial_dict = {
    "minecraft:village/plains/villagers":      "repurposed_structures:villages/birch/villagers",
    "minecraft:village/plains/zombie/villagers":      "repurposed_structures:villages/birch/zombie/villagers"
}
conversion_exact_dict = {   
    "minecraft:oak_door":                      "minecraft:birch_door",
    "minecraft:oak_log":                       "minecraft:birch_log",
    "minecraft:oak_planks":                    "minecraft:birch_planks",
    "minecraft:oak_slab":                      "minecraft:birch_slab",
    "minecraft:oak_stairs":                    "minecraft:birch_stairs",
    "minecraft:oak_trapdoor":                  "minecraft:birch_trapdoor",
    "minecraft:red_carpet":                    "minecraft:yellow_carpet",
    "minecraft:stripped_oak_log":              "minecraft:stripped_birch_log",
    "minecraft:white_carpet":                  "minecraft:yellow_carpet",
    "minecraft:yellow_carpet":                 "minecraft:white_carpet",
}

# Dark Forest
originalBiome = "plains"
newBiome = "dark_forest"
string_blacklist = []
conversion_partial_dict = {
    "minecraft:village/plains/villagers":      "repurposed_structures:villages/dark_forest/villagers",
    "minecraft:village/plains/zombie/villagers":      "repurposed_structures:villages/dark_forest/zombie/villagers"
}
conversion_exact_dict = {   
    "minecraft:cobblestone":                   "minecraft:brown_terracotta",
    "minecraft:cobblestone_wall":              "minecraft:granite_wall",     
    "minecraft:mossy_cobblestone":             "minecraft:brown_terracotta",
    "minecraft:oak_door":                      "minecraft:dark_oak_door",
    "minecraft:oak_log":                       "minecraft:dark_oak_log",
    "minecraft:oak_planks":                    "minecraft:dark_oak_planks",
    "minecraft:oak_slab":                      "minecraft:dark_oak_slab",
    "minecraft:oak_stairs":                    "minecraft:dark_oak_stairs",
    "minecraft:oak_trapdoor":                  "minecraft:dark_oak_trapdoor",
    "minecraft:red_carpet":                    "minecraft:black_carpet",
    "minecraft:stripped_oak_log":              "minecraft:stripped_dark_oak_log",
    "minecraft:white_carpet":                  "minecraft:red_carpet",
    "minecraft:yellow_carpet":                 "minecraft:black_carpet",
}

# Mushroom
originalBiome = "plains"
newBiome = "mushroom"
string_blacklist = []
conversion_partial_dict = {
    "minecraft:village/plains/villagers":      "repurposed_structures:villages/mushroom/villagers"
}
conversion_exact_dict = {   
    "minecraft:cobblestone":                   "minecraft:mushroom_stem",
    "minecraft:cobblestone_wall":              "minecraft:andesite_wall",
    "minecraft:grass_block":                   "minecraft:mycelium",
    "minecraft:mossy_cobblestone":             "minecraft:mushroom_stem",
    "minecraft:oak_door":                      "minecraft:birch_door",
    "minecraft:oak_log":                       "minecraft:brown_mushroom_block",
    "minecraft:oak_planks":                    "minecraft:red_mushroom_block",
    "minecraft:oak_slab":                      "minecraft:red_mushroom_block",
    "minecraft:oak_stairs":                    "minecraft:andesite_stairs",
    "minecraft:oak_trapdoor":                  "minecraft:birch_trapdoor",
    "minecraft:red_carpet":                    "minecraft:pink_carpet",
    "minecraft:stripped_oak_log":              "minecraft:red_mushroom_block",
    "minecraft:white_carpet":                  "minecraft:purple_carpet",
    "minecraft:yellow_carpet":                 "minecraft:pink_carpet",
}

# Badlands
originalBiome = "desert"
newBiome = "badlands"
string_blacklist = []
conversion_partial_dict = {
    "minecraft:village/desert/villagers":      "repurposed_structures:villages/badlands/villagers",
    "minecraft:village/desert/zombie/villagers":      "repurposed_structures:villages/badlands/zombie/villagers"
}
conversion_exact_dict = {   
    "minecraft:chiseled_sandstone":            "minecraft:chiseled_red_sandstone",
    "minecraft:cut_sandstone":                 "minecraft:cut_red_sandstone",
    "minecraft:cut_sandstone_slab":            "minecraft:cut_red_sandstone_slab",
    "minecraft:jungle_planks":                 "minecraft:dark_oak_planks",
    "minecraft:potted_cactus":                 "minecraft:potted_cactus",
    "minecraft:red_terracotta":                "minecraft:red_terracotta",
    "minecraft:sand":                          "minecraft:red_sand",
    "minecraft:sandstone":                     "minecraft:red_sandstone",
    "minecraft:sandstone_slab":                "minecraft:red_sandstone_slab",
    "minecraft:sandstone_wall":                "minecraft:red_sandstone_wall",
    "minecraft:smooth_sandstone":              "minecraft:smooth_red_sandstone",
    "minecraft:smooth_sandstone_slab":         "minecraft:smooth_red_sandstone_slab",
    "minecraft:smooth_sandstone_stairs":       "minecraft:smooth_red_sandstone_stairs",
    "minecraft:terracotta":                    "minecraft:yellow_terracotta",
}

# Jungle
originalBiome = "savanna"
newBiome = "jungle"
string_blacklist = []
conversion_partial_dict = {
    "minecraft:village/savanna/villagers":      "repurposed_structures:villages/jungle/villagers",
    "minecraft:village/savanna/zombie/villagers":      "repurposed_structures:villages/jungle/zombie/villagers"
}
conversion_exact_dict = {   
    "minecraft:acacia_fence":                  "minecraft:jungle_fence",
    "minecraft:acacia_log":                    "minecraft:jungle_log",
    "minecraft:acacia_planks":                 "minecraft:jungle_planks",
    "minecraft:acacia_slab":                   "minecraft:jungle_slab",
    "minecraft:acacia_stairs":                 "minecraft:jungle_stairs",
    "minecraft:acacia_wood":                   "minecraft:jungle_wood",
    "minecraft:stripped_acacia_log":           "minecraft:stripped_jungle_log",
}

# Bamboo
originalBiome = "savanna"
newBiome = "bamboo"
string_blacklist = []
conversion_partial_dict = {
    "minecraft:village/savanna/villagers":      "repurposed_structures:villages/bamboo/villagers",
    "minecraft:village/savanna/zombie/villagers":      "repurposed_structures:villages/bamboo/zombie/villagers"
}
conversion_exact_dict = {   
    "minecraft:acacia_fence":                  "minecraft:bamboo_fence",
    "minecraft:acacia_log":                    "minecraft:mud_bricks",
    "minecraft:acacia_planks":                 "minecraft:bamboo_planks",
    "minecraft:acacia_slab":                   "minecraft:bamboo_slab",
    "minecraft:acacia_stairs":                 "minecraft:bamboo_stairs",
    "minecraft:acacia_wood":                   "minecraft:bamboo_wood",
    "minecraft:stripped_acacia_log":           "minecraft:bamboo_block",
}

# Giant Taiga
originalBiome = "snowy"
newBiome = "giant_taiga"
string_blacklist = []
conversion_partial_dict = {
    "minecraft:village/snowy/villagers":      "repurposed_structures:villages/giant_taiga/villagers",
    "minecraft:village/snowy/zombie/villagers":      "repurposed_structures:villages/giant_taiga/zombie/villagers"
}
conversion_exact_dict = {
    "minecraft:cobblestone_wall":              "minecraft:cobblestone_wall",
    "minecraft:snow":                          "minecraft:stone_pressure_plate",
    "minecraft:snow_block":                    "minecraft:cobblestone",
    "minecraft:spruce_log":                    "minecraft:cobblestone",
    "minecraft:stripped_spruce_log":           "minecraft:stone",
}

# Swamp
originalBiome = "snowy"
newBiome = "swamp"
string_blacklist = []
conversion_partial_dict = {
    "minecraft:village/snowy/villagers":      "repurposed_structures:villages/swamp/villagers",
    "minecraft:village/snowy/zombie/villagers":      "repurposed_structures:villages/swamp/zombie/villagers"
}
conversion_exact_dict = {
    "minecraft:cobblestone_wall":              "minecraft:mud_brick_wall",
    "minecraft:snow":                          "minecraft:mud",
    "minecraft:snow_block":                    "minecraft:mud",
    "minecraft:spruce_door":                   "minecraft:mangrove_door",
    "minecraft:spruce_log":                    "minecraft:mangrove_log",
    "minecraft:spruce_planks":                 "minecraft:mangrove_planks",
    "minecraft:spruce_slab":                   "minecraft:mangrove_slab",
    "minecraft:spruce_stairs":                 "minecraft:mangrove_stairs",
    "minecraft:spruce_trapdoor":               "minecraft:mangrove_trapdoor",
    "minecraft:stripped_spruce_log":           "minecraft:stripped_mangrove_log",
}

# Cherry
originalBiome = "taiga"
newBiome = "cherry"
string_blacklist = []
conversion_partial_dict = {
    "minecraft:village/taiga/villagers":      "repurposed_structures:villages/cherry/villagers",
    "minecraft:village/taiga/zombie/villagers":      "repurposed_structures:villages/cherry/zombie/villagers"
}
conversion_exact_dict = {
    "minecraft:allium":                        "minecraft:allium",
    "minecraft:cobblestone":                   "minecraft:bricks",
    "minecraft:cobblestone_slab":              "minecraft:brick_slab",
    "minecraft:cobblestone_stairs":            "minecraft:brick_stairs",
    "minecraft:cobblestone_wall":              "minecraft:brick_wall",
    "minecraft:lily_of_the_valley":            "minecraft:lily_of_the_valley",
    "minecraft:orange_tulip":                  "minecraft:poppy",
    "minecraft:pink_tulip":                    "minecraft:pink_tulip",
    "minecraft:purple_bed":                    "minecraft:pink_bed",
    "minecraft:purple_carpet":                 "minecraft:pink_carpet",
    "minecraft:purple_wool":                   "minecraft:pink_wool",
    "minecraft:spruce_door":                   "minecraft:cherry_door",
    "minecraft:spruce_log":                    "minecraft:cherry_log",
    "minecraft:spruce_planks":                 "minecraft:cherry_planks",
    "minecraft:spruce_slab":                   "minecraft:cherry_slab",
    "minecraft:spruce_stairs":                 "minecraft:cherry_stairs",
    "minecraft:spruce_trapdoor":               "minecraft:cherry_trapdoor",
    "minecraft:stripped_spruce_log":           "minecraft:stripped_cherry_log",
    "minecraft:wither_rose":                   "minecraft:pink_petals",
}

# Mountains
originalBiome = "taiga"
newBiome = "mountains"
string_blacklist = []
conversion_partial_dict = {
    "minecraft:village/taiga/villagers":      "repurposed_structures:villages/mountains/villagers",
    "minecraft:village/taiga/zombie/villagers":      "repurposed_structures:villages/mountains/zombie/villagers"
}
conversion_exact_dict = {
    "minecraft:allium":                        "minecraft:grass",
    "minecraft:cobblestone":                   "minecraft:stone_bricks",
    "minecraft:cobblestone_slab":              "minecraft:stone_brick_slab",
    "minecraft:cobblestone_stairs":            "minecraft:stone_brick_stairs",
    "minecraft:cobblestone_wall":              "minecraft:stone_brick_wall",
    "minecraft:lily_of_the_valley":            "minecraft:fern",
    "minecraft:orange_tulip":                  "minecraft:poppy",
    "minecraft:pink_tulip":                    "minecraft:grass",
    "minecraft:purple_bed":                    "minecraft:gray_bed",
    "minecraft:purple_carpet":                 "minecraft:gray_carpet",
    "minecraft:purple_wool":                   "minecraft:gray_wool",
    "minecraft:spruce_planks":                 "minecraft:spruce_wood",
    "minecraft:stripped_spruce_log":           "minecraft:spruce_log",
    "minecraft:wither_rose":                   "minecraft:poppy",
}
'''