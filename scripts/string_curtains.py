from os.path import exists
import os
import shutil
import json

colors = [
    "black", 
    "blue", 
    "brown", 
    "cyan",
    "gray",
    "green",
    "light_blue",
    "light_gray",
    "lime",
    "magenta",
    "orange",
    "pink",
    "purple",
    "red",
    "white",
    "yellow"
]

modelTypes = [
    "north_attached", 
    "south_attached", 
    "west_attached", 
    "east_attached",
    "ns_attached",
    "ew_attached",
    "north_unattached",
    "south_unattached",
    "west_unattached",
    "east_unattached",
    "ns_unattached",
    "ew_unattached"
]

if not os.path.exists("output"):
    os.mkdir("output")

for color in colors:
    blockstateFile = open(f"output/string_curtain_{color}.json", "w+")
    blockstateFile.seek(0)
    jsonData = {
        "variants": {
            "attached=true,middle=false,facing=north": { "model": f"the_bumblezone:block/string_curtain/{color}_north_attached" },
            "attached=true,middle=false,facing=south": { "model": f"the_bumblezone:block/string_curtain/{color}_south_attached" },
            "attached=true,middle=false,facing=west": { "model": f"the_bumblezone:block/string_curtain/{color}_west_attached" },
            "attached=true,middle=false,facing=east": { "model": f"the_bumblezone:block/string_curtain/{color}_east_attached" },
            "attached=true,middle=true,facing=north": { "model": f"the_bumblezone:block/string_curtain/{color}_ns_attached" },
            "attached=true,middle=true,facing=south": { "model": f"the_bumblezone:block/string_curtain/{color}_ns_attached" },
            "attached=true,middle=true,facing=west": { "model": f"the_bumblezone:block/string_curtain/{color}_ew_attached" },
            "attached=true,middle=true,facing=east": { "model": f"the_bumblezone:block/string_curtain/{color}_ew_attached" },

            "attached=false,middle=false,facing=north": { "model": f"the_bumblezone:block/string_curtain/{color}_north_unattached" },
            "attached=false,middle=false,facing=south": { "model": f"the_bumblezone:block/string_curtain/{color}_south_unattached" },
            "attached=false,middle=false,facing=west": { "model": f"the_bumblezone:block/string_curtain/{color}_west_unattached" },
            "attached=false,middle=false,facing=east": { "model": f"the_bumblezone:block/string_curtain/{color}_east_unattached" },
            "attached=false,middle=true,facing=north": { "model": f"the_bumblezone:block/string_curtain/{color}_ns_unattached" },
            "attached=false,middle=true,facing=south": { "model": f"the_bumblezone:block/string_curtain/{color}_ns_unattached" },
            "attached=false,middle=true,facing=west": { "model": f"the_bumblezone:block/string_curtain/{color}_ew_unattached" },
            "attached=false,middle=true,facing=east": { "model": f"the_bumblezone:block/string_curtain/{color}_ew_unattached" }
        }
    }
    blockstateFile.write(json.dumps(jsonData, indent=2))

    if not os.path.exists("output/item_models"):
        os.mkdir("output/item_models")

    modelFile = open(f"output/item_models/string_curtain_{color}.json", "w+")
    modelFile.seek(0)
    modelJson = {
        "parent": "minecraft:item/generated",
        "textures": {
            "layer0": f"the_bumblezone:block/string_curtain/{color}_attached"
        }
    }
    modelFile.write(json.dumps(modelJson, indent=2))


    if not os.path.exists("output/loot_table"):
        os.mkdir("output/loot_table")

    modelFile = open(f"output/loot_table/string_curtain_{color}.json", "w+")
    modelFile.seek(0)
    modelJson = {
        "type": "minecraft:block",
        "pools": [
        {
            "rolls": 1,
            "entries": [
            {
                "type": "minecraft:alternatives",
                "children": [
                {
                    "name": f"the_bumblezone:string_curtain_{color}",
                    "type": "minecraft:item",
                    "conditions": [
                        {
                            "condition": "minecraft:block_state_property",
                            "block": f"the_bumblezone:string_curtain_{color}",
                            "properties": {
                                "attached": "true"
                            }
                        }
                    ]
                },
                {
                    "name": "minecraft:string",
                    "type": "minecraft:item"
                }
                ]
            }
            ],
            "conditions": [
            {
                "condition": "minecraft:survives_explosion"
            }
            ]
        }
        ]
    }
    modelFile.write(json.dumps(modelJson, indent=2))


    if not os.path.exists("output/block_models"):
        os.mkdir("output/block_models")

    if not os.path.exists("output/block_models/string_curtain"):
        os.mkdir("output/block_models/string_curtain")

    for modelType in modelTypes:
        modelTemplateFile = open(f"input/string_curtain/{modelType}.json")
        modelFile = open(f"output/block_models/string_curtain/{color}_{modelType}.json", "w+")
        modelFile.seek(0)
        modelJson = json.load(modelTemplateFile)
        textures = modelJson["textures"]
        textures["0"] = textures["0"].replace("string_curtain/", f"string_curtain/{color}_")
        textures["particle"] = textures["particle"].replace("string_curtain/", f"string_curtain/{color}_")
        modelFile.write(json.dumps(modelJson, indent=2))






