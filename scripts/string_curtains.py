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
    "center_bottom", 
    "center_middle", 
    "center_top", 
    "center_top_end", 
    "wall_bottom",
    "wall_middle",
    "wall_top",
    "wall_top_end"
]

if not os.path.exists("output"):
    os.mkdir("output")

for color in colors:
    blockstateFile = open(f"output/string_curtain_{color}.json", "w+")
    blockstateFile.seek(0)
    jsonData = {
        "variants": {
            "attached=true,center=false,facing=north,is_end=false": { "model": f"the_bumblezone:block/string_curtain/{color}_wall_top", "y": 270 },
            "attached=true,center=false,facing=south,is_end=false": { "model": f"the_bumblezone:block/string_curtain/{color}_wall_top", "y": 90 },
            "attached=true,center=false,facing=west,is_end=false": { "model": f"the_bumblezone:block/string_curtain/{color}_wall_top", "y": 180 },
            "attached=true,center=false,facing=east,is_end=false": { "model": f"the_bumblezone:block/string_curtain/{color}_wall_top" },
            "attached=true,center=true,facing=north,is_end=false": { "model": f"the_bumblezone:block/string_curtain/{color}_center_top" },
            "attached=true,center=true,facing=south,is_end=false": { "model": f"the_bumblezone:block/string_curtain/{color}_center_top" },
            "attached=true,center=true,facing=west,is_end=false": { "model": f"the_bumblezone:block/string_curtain/{color}_center_top", "y": 90 },
            "attached=true,center=true,facing=east,is_end=false": { "model": f"the_bumblezone:block/string_curtain/{color}_center_top", "y": 90 },

            "attached=true,center=false,facing=north,is_end=true": { "model": f"the_bumblezone:block/string_curtain/{color}_wall_top_end", "y": 270 },
            "attached=true,center=false,facing=south,is_end=true": { "model": f"the_bumblezone:block/string_curtain/{color}_wall_top_end", "y": 90 },
            "attached=true,center=false,facing=west,is_end=true": { "model": f"the_bumblezone:block/string_curtain/{color}_wall_top_end", "y": 180 },
            "attached=true,center=false,facing=east,is_end=true": { "model": f"the_bumblezone:block/string_curtain/{color}_wall_top_end" },
            "attached=true,center=true,facing=north,is_end=true": { "model": f"the_bumblezone:block/string_curtain/{color}_center_top_end" },
            "attached=true,center=true,facing=south,is_end=true": { "model": f"the_bumblezone:block/string_curtain/{color}_center_top_end" },
            "attached=true,center=true,facing=west,is_end=true": { "model": f"the_bumblezone:block/string_curtain/{color}_center_top_end", "y": 90 },
            "attached=true,center=true,facing=east,is_end=true": { "model": f"the_bumblezone:block/string_curtain/{color}_center_top_end", "y": 90 },

            "attached=false,center=false,facing=north,is_end=false": { "model": f"the_bumblezone:block/string_curtain/{color}_wall_middle", "y": 270 },
            "attached=false,center=false,facing=south,is_end=false": { "model": f"the_bumblezone:block/string_curtain/{color}_wall_middle", "y": 90 },
            "attached=false,center=false,facing=west,is_end=false": { "model": f"the_bumblezone:block/string_curtain/{color}_wall_middle", "y": 180 },
            "attached=false,center=false,facing=east,is_end=false": { "model": f"the_bumblezone:block/string_curtain/{color}_wall_middle" },
            "attached=false,center=true,facing=north,is_end=false": { "model": f"the_bumblezone:block/string_curtain/{color}_center_middle" },
            "attached=false,center=true,facing=south,is_end=false": { "model": f"the_bumblezone:block/string_curtain/{color}_center_middle" },
            "attached=false,center=true,facing=west,is_end=false": { "model": f"the_bumblezone:block/string_curtain/{color}_center_middle", "y": 90 },
            "attached=false,center=true,facing=east,is_end=false": { "model": f"the_bumblezone:block/string_curtain/{color}_center_middle", "y": 90 },

            "attached=false,center=false,facing=north,is_end=true": { "model": f"the_bumblezone:block/string_curtain/{color}_wall_bottom", "y": 270 },
            "attached=false,center=false,facing=south,is_end=true": { "model": f"the_bumblezone:block/string_curtain/{color}_wall_bottom", "y": 90 },
            "attached=false,center=false,facing=west,is_end=true": { "model": f"the_bumblezone:block/string_curtain/{color}_wall_bottom", "y": 180 },
            "attached=false,center=false,facing=east,is_end=true": { "model": f"the_bumblezone:block/string_curtain/{color}_wall_bottom" },
            "attached=false,center=true,facing=north,is_end=true": { "model": f"the_bumblezone:block/string_curtain/{color}_center_bottom" },
            "attached=false,center=true,facing=south,is_end=true": { "model": f"the_bumblezone:block/string_curtain/{color}_center_bottom" },
            "attached=false,center=true,facing=west,is_end=true": { "model": f"the_bumblezone:block/string_curtain/{color}_center_bottom", "y": 90 },
            "attached=false,center=true,facing=east,is_end=true": { "model": f"the_bumblezone:block/string_curtain/{color}_center_bottom", "y": 90 },
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
            "layer0": f"the_bumblezone:item/string_curtain/{color}"
        }
    }
    modelFile.write(json.dumps(modelJson, indent=2))


    if not os.path.exists("output/recipes"):
        os.mkdir("output/recipes")

    if not os.path.exists("output/recipes/string_curtain"):
        os.mkdir("output/recipes/string_curtain")

    modelFile = open(f"output/recipes/string_curtain/{color}.json", "w+")
    modelFile.seek(0)
    modelJson = {
        "type": "minecraft:crafting_shaped",
        "group": "the_bumblezone",
        "pattern": [
            "ttt",
            "sgs",
            "sds"
        ],
        "key": {
            "t": {
                "item": "minecraft:stick"
            },
            "s": {
                "item": "minecraft:string"
            },
            "g": {
                "item": "minecraft:glass"
            },
            "d": {
                "item": f"minecraft:{color}_dye"
            }
        },
        "result": {
            "item": f"the_bumblezone:string_curtain_{color}"
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
        textures["main"] = textures["main"].replace("string_curtain/", f"string_curtain/{color}_")
        textures["particle"] = textures["particle"].replace("string_curtain/", f"string_curtain/{color}_")
        modelFile.write(json.dumps(modelJson, indent=2))






