from os.path import exists
import os
import json
import re

data_folder = os.path.join('..', 'common', 'src', 'main', 'resources', 'data', 'the_bumblezone')

loot_tables_folder = os.path.join('loot_tables')
advancements_folder = os.path.join('advancements')

def createFile(input_path, output_path, regex_list):
    file_content = ''
    with open(input_path, 'r') as file:
        file_content = file.read()
        for i in range(len(regex_list)):
           file_content = file_content.replace(f'${i + 1}', regex_list[i])

    path = output_path
    if not exists(path):
        os.makedirs(os.path.dirname(path), exist_ok=True)
        with open(path, 'w') as jsonFile1:
            jsonFile1.write(file_content)

def traverseAndModify(dictionary, modifiers):  
    for key, value in dictionary.items():
        if isinstance(value, dict):
            traverseAndModify(value, modifiers)
            for modifier in modifiers:
                modifier(value)
        elif isinstance(value, list):
            for listEntry in value:
                if isinstance(listEntry, dict):
                    traverseAndModify(listEntry, modifiers)
                    for modifier in modifiers:
                        modifier(listEntry)

#--------------------------------------------------------------------------------------------

"""
        {
          "name": "minecraft:magenta_banner",
          "functions": [
            {
              "function": "set_nbt",
              "tag": "{BlockEntityTag:{Patterns:[{Pattern:bri,Color:15},{Pattern:ss,Color:2},{Pattern:gru,Color:15},{Pattern:gru,Color:2},{Pattern:mr,Color:15},{Pattern:br,Color:2},{Pattern:br,Color:15},{Pattern:bl,Color:2},{Pattern:bl,Color:15},{Pattern:bts,Color:2},{Pattern:gru,Color:10},{Pattern:bts,Color:15}]}}"
            }
          ]
        },

        
        {
          "name": "minecraft:magenta_banner",
          "functions": [
            {
              "function": "minecraft:set_banner_pattern",
              "patterns": [
                {
                  "pattern": "minecraft:base",
                  "color": "lime"
                }
              ]
            }
          ]
        },
""" and None

# Loot Table Updating
acceptedBasePotions = {
    "minecraft:water",
    "minecraft:mundane",
    "minecraft:thick",
    "minecraft:awkward",
    "minecraft:night_vision",
    "minecraft:long_night_vision",
    "minecraft:invisibility",
    "minecraft:long_invisibility",
    "minecraft:leaping",
    "minecraft:long_leaping",
    "minecraft:strong_leaping",
    "minecraft:fire_resistance",
    "minecraft:long_fire_resistance",
    "minecraft:swiftness",
    "minecraft:long_swiftness",
    "minecraft:strong_swiftness",
    "minecraft:slowness",
    "minecraft:long_slowness",
    "minecraft:strong_slowness",
    "minecraft:turtle_master",
    "minecraft:long_turtle_master",
    "minecraft:strong_turtle_master",
    "minecraft:water_breathing",
    "minecraft:long_water_breathing",
    "minecraft:healing",
    "minecraft:strong_healing",
    "minecraft:harming",
    "minecraft:strong_harming",
    "minecraft:poison",
    "minecraft:long_poison",
    "minecraft:strong_poison",
    "minecraft:regeneration",
    "minecraft:long_regeneration",
    "minecraft:strong_regeneration",
    "minecraft:strength",
    "minecraft:long_strength",
    "minecraft:strong_strength",
    "minecraft:weakness",
    "minecraft:long_weakness",
    "minecraft:luck",
    "minecraft:slow_falling",
    "minecraft:long_slow_falling",
    "minecraft:wind_charged",
    "minecraft:weaving",
    "minecraft:oozing",
    "minecraft:infested"
}
bannerPatternConversion = {
    "b": "minecraft:base",
    "bl": "minecraft:square_bottom_left",
    "br": "minecraft:square_bottom_right",
    "tl": "minecraft:square_top_left",
    "tr": "minecraft:square_top_right",
    "bs": "minecraft:stripe_bottom",
    "ts": "minecraft:stripe_top",
    "ls": "minecraft:stripe_left",
    "rs": "minecraft:stripe_right",
    "cs": "minecraft:stripe_center",
    "ms": "minecraft:stripe_middle",
    "drs": "minecraft:stripe_downright",
    "dls": "minecraft:stripe_downleft",
    "ss": "minecraft:small_stripes",
    "cr": "minecraft:cross",
    "sc": "minecraft:straight_cross",
    "bt": "minecraft:triangle_bottom",
    "tt": "minecraft:triangle_top",
    "bts": "minecraft:triangles_bottom",
    "tts": "minecraft:triangles_top",
    "ld": "minecraft:diagonal_left",
    "rd": "minecraft:diagonal_up_right",
    "lud": "minecraft:diagonal_up_left",
    "rud": "minecraft:diagonal_right",
    "mc": "minecraft:circle",
    "mr": "minecraft:rhombus",
    "vh": "minecraft:half_vertical",
    "hh": "minecraft:half_horizontal",
    "vhr": "minecraft:half_vertical_right",
    "hhb": "minecraft:half_horizontal_bottom",
    "bo": "minecraft:border",
    "cbo": "minecraft:curly_border",
    "gra": "minecraft:gradient",
    "gru": "minecraft:gradient_up",
    "bri": "minecraft:bricks",
    "glb": "minecraft:globe",
    "cre": "minecraft:creeper",
    "sku": "minecraft:skull",
    "flo": "minecraft:flower",
    "moj": "minecraft:mojang",
    "pig": "minecraft:piglin"
}
bannerColorConversion = {
    "0": "white",
    "1": "orange",
    "2": "magenta",
    "3": "light_blue",
    "4": "yellow",
    "5": "lime",
    "6": "pink",
    "7": "gray",
    "8": "light_gray",
    "9": "cyan",
    "10": "purple",
    "11": "blue",
    "12": "brown",
    "13": "green",
    "14": "red",
    "15": "black"
}
fireworkStarTypeConversion = {
    "0": "small_ball",
    "1": "large_ball",
    "2": "star",
    "3": "magenta",
    "4": "creeper",
    "5": "burst"
}
path = os.path.join(data_folder, loot_tables_folder)
for (subdir, dirs, files) in os.walk(path, topdown=True):
    for file in files:
        directory = subdir + os.sep
        filepath = directory + file

        if filepath.endswith(".json"):
            lootTable = {}
            with open(filepath, 'r') as file:
                lootTable = json.loads(file.read())

                def setPotionComponent(objectToModify):
                    if "function" in objectToModify \
                        and "tag" in objectToModify \
                        and (objectToModify["function"] == "minecraft:set_nbt" or objectToModify["function"] == "set_nbt") \
                        and "Potion" in objectToModify["tag"]:
                        
                        if match := re.search("\\\"([\w:]+)\\\"", objectToModify["tag"], re.IGNORECASE):
                            potionType = match.group(1)
                            if ":" not in potionType:
                                potionType = "minecraft:" + potionType

                            if potionType in acceptedBasePotions:
                                objectToModify["function"] = "minecraft:set_potion"
                                objectToModify["id"] = potionType
                                del objectToModify["tag"]
                            else:
                                objectToModify["function"] = "minecraft:set_components"
                                objectToModify["components"] = {
                                    "minecraft:potion_contents": {
                                        "custom_effects": [
                                            {
                                                "id": potionType,
                                                "amplifier": 1,
                                                "duration": 20,
                                                "ambient": False,
                                                "show_particles": True,
                                                "show_icon": True
                                            }
                                        ]
                                    }
                                }
                                del objectToModify["tag"]
                                
                def setBannerComponent(objectToModify):
                    if "function" in objectToModify \
                        and "tag" in objectToModify \
                        and (objectToModify["function"] == "minecraft:set_nbt" or objectToModify["function"] == "set_nbt") \
                        and "BlockEntityTag:" in objectToModify["tag"] \
                        and "Patterns:" in objectToModify["tag"]:
                        
                        objectToModify["function"] = "minecraft:set_banner_pattern"
                        objectToModify["append"] = True

                        bannerData = []
                        if matches := re.findall("Pattern:(\w+),Color:(\d+)", objectToModify["tag"], re.IGNORECASE):
                            for match in matches:
                                pattern = {}
                                pattern["pattern"] = bannerPatternConversion[match[0]]
                                pattern["color"] = bannerColorConversion[match[1]]
                                bannerData.append(pattern)

                        objectToModify["patterns"] = bannerData
                        del objectToModify["tag"]

                def setNestedLootTable(objectToModify):
                    if "type" in objectToModify \
                        and "name" in objectToModify \
                        and (objectToModify["type"] == "minecraft:loot_table" or objectToModify["type"] == "loot_table"):
                        
                        objectToModify["type"] = "minecraft:loot_table"
                        objectToModify["value"] = objectToModify["name"]
                        del objectToModify["name"]

                def setFireworkStarComponent(objectToModify):
                    if "function" in objectToModify \
                        and "tag" in objectToModify \
                        and (objectToModify["function"] == "minecraft:set_nbt" or objectToModify["function"] == "set_nbt") \
                        and "Explosion:" in objectToModify["tag"]:
                        
                        objectToModify["function"] = "minecraft:set_firework_explosion"

                        if match := re.search("\WColors:\[((?:(?:I;)?\d+,?)+)", objectToModify["tag"], re.IGNORECASE):
                            colorsStringArray = match.group(1).replace("I;", "").split(",")
                            colorsArray = [int(numeric_string) for numeric_string in colorsStringArray]
                            objectToModify["color"] = colorsArray

                        if match := re.search("FadeColors:\[((?:(?:I;)?\d+,?)+)", objectToModify["tag"], re.IGNORECASE):
                            colorsStringArray = match.group(1).replace("I;", "").split(",")
                            colorsArray = [int(numeric_string) for numeric_string in colorsStringArray]
                            objectToModify["fade_colors"] = colorsArray

                        if match := re.search("Trail:(\d+)", objectToModify["tag"], re.IGNORECASE):
                            objectToModify["trail"] = True if match.group(1) == "1" else False

                        if match := re.search("Flicker:(\d+)", objectToModify["tag"], re.IGNORECASE):
                            objectToModify["twinkle"] = True if match.group(1) == "1" else False

                        if match := re.search("Type:(\d+)", objectToModify["tag"], re.IGNORECASE):
                            objectToModify["shape"] = fireworkStarTypeConversion[match.group(1)]

                        del objectToModify["tag"]

                def updateItemNames(objectToModify):
                    if "name" in objectToModify and (objectToModify["name"] == "minecraft:scute" or objectToModify["name"] == "scute"):
                        objectToModify["name"] = "minecraft:turtle_scute"
                    if "name" in objectToModify and (objectToModify["name"] == "minecraft:stone_cutter" or objectToModify["name"] == "stone_cutter"):
                        objectToModify["name"] = "minecraft:stonecutter"
                        
                traverseAndModify(lootTable, [setPotionComponent, setBannerComponent, setNestedLootTable, setFireworkStarComponent, updateItemNames])

            with open(filepath, 'w') as file:
                json.dump(lootTable, file, indent = 2)
            continue
        else:
            continue

# Advancement Updating
path = os.path.join(data_folder, advancements_folder)
for (subdir, dirs, files) in os.walk(path, topdown=True):
    for file in files:
        directory = subdir + os.sep
        filepath = directory + file

        if filepath.endswith(".json"):
            lootTable = {}
            with open(filepath, 'r') as file:
                lootTable = json.loads(file.read())

                def setPotionComponent(objectToModify):
                    if "nbt" in objectToModify.keys() and "Potion" in objectToModify["nbt"]:
                        if match := re.search("\\\"([\w:]+)\\\"", objectToModify["nbt"], re.IGNORECASE):
                            potionType = match.group(1)
                            if ":" not in potionType:
                                potionType = "minecraft:" + potionType

                            if potionType in acceptedBasePotions:
                                objectToModify["components"] = {
                                    "minecraft:potion_contents": {
                                        "potion": potionType
                                    }
                                }
                            else:
                                objectToModify["components"] = {
                                    "minecraft:potion_contents": {
                                        "custom_effects": [
                                            {
                                                "id": potionType,
                                                "amplifier": 1,
                                                "duration": 20,
                                                "ambient": False,
                                                "show_particles": True,
                                                "show_icon": True
                                            }
                                        ]
                                    }
                                }
                            if "tag" in objectToModify:
                                del objectToModify["tag"]
                                
                def updateItemNames(objectToModify):
                    if "icon" in objectToModify and "item" in objectToModify["icon"]:
                        itemIcon = objectToModify["icon"]
                        itemIcon["id"] = itemIcon["item"]
                        del itemIcon["item"]
                        setPotionComponent(itemIcon)
                    if "location" in objectToModify:
                        structureLocation = objectToModify["location"]
                        if "structures" in structureLocation:
                            structureLocation["structures"] = structureLocation["structure"]
                            del structureLocation["structure"]
                        
                traverseAndModify(lootTable, [updateItemNames])

            with open(filepath, 'w') as file:
                json.dump(lootTable, file, indent = 2)
            continue
        else:
            continue

print('\n\nFINISHED!')
