from os.path import exists
import os
import shutil
import json
import jsbeautifier

options = jsbeautifier.default_options()
options.indent_size = 2

colors = [
    "", 
    "_red", 
    "_yellow", 
    "_green",
    "_blue",
    "_purple",
    "_white"
]

if not os.path.exists("output"):
    os.mkdir("output")

for color in colors:
    if not os.path.exists("output/blockstates"):
        os.mkdir("output/blockstates")

    if not os.path.exists("output/recipes"):
        os.mkdir("output/recipes")

    if not os.path.exists("output/recipes/luminescent_wax"):
        os.mkdir("output/recipes/luminescent_wax")

    filePath = f"output/recipes/luminescent_wax/channel_to_corner{color}_1.json"
    if color == "":
        filePath = "output/recipes/luminescent_wax/channel_to_corner_lightless_1.json"
    recipeFile = open(filePath, "w+")
    recipeFile.seek(0)
    recipeJson = {
        "type": "minecraft:crafting_shaped",
        "category": "building",
        "group": f"the_bumblezone:luminescent_wax_corner{color}",
        "pattern": [
            "w ",
            "ww"
        ],
        "key": {
            "w": {
                "item": f"the_bumblezone:luminescent_wax_channel{color}"
            }
        },
        "result": {
            "item": f"the_bumblezone:luminescent_wax_corner{color}",
            "count": 3
        }
    }
    recipeFile.write(jsbeautifier.beautify(json.dumps(recipeJson), options))


    filePath = f"output/recipes/luminescent_wax/channel_to_corner{color}_2.json"
    if color == "":
        filePath = "output/recipes/luminescent_wax/channel_to_corner_lightless_2.json"
    recipeFile = open(filePath, "w+")
    recipeFile.seek(0)
    recipeJson = {
        "type": "minecraft:crafting_shaped",
        "category": "building",
        "group": f"the_bumblezone:luminescent_wax_corner{color}",
        "pattern": [
            "ww",
            "w "
        ],
        "key": {
            "w": {
                "item": f"the_bumblezone:luminescent_wax_channel{color}"
            }
        },
        "result": {
            "item": f"the_bumblezone:luminescent_wax_corner{color}",
            "count": 3
        }
    }
    recipeFile.write(jsbeautifier.beautify(json.dumps(recipeJson), options))


    filePath = f"output/recipes/luminescent_wax/node_to_corner{color}_1.json"
    if color == "":
        filePath = "output/recipes/luminescent_wax/node_to_corner_lightless_1.json"
    recipeFile = open(filePath, "w+")
    recipeFile.seek(0)
    recipeJson = {
        "type": "minecraft:crafting_shaped",
        "category": "building",
        "group": f"the_bumblezone:luminescent_wax_corner{color}",
        "pattern": [
            "w ",
            "ww"
        ],
        "key": {
            "w": {
                "item": f"the_bumblezone:luminescent_wax_node{color}"
            }
        },
        "result": {
            "item": f"the_bumblezone:luminescent_wax_corner{color}",
            "count": 3
        }
    }
    recipeFile.write(jsbeautifier.beautify(json.dumps(recipeJson), options))


    filePath = f"output/recipes/luminescent_wax/node_to_corner{color}_2.json"
    if color == "":
        filePath = "output/recipes/luminescent_wax/node_to_corner_lightless_2.json"
    recipeFile = open(filePath, "w+")
    recipeFile.seek(0)
    recipeJson = {
        "type": "minecraft:crafting_shaped",
        "category": "building",
        "group": f"the_bumblezone:luminescent_wax_corner{color}",
        "pattern": [
            "ww",
            "w "
        ],
        "key": {
            "w": {
                "item": f"the_bumblezone:luminescent_wax_node{color}"
            }
        },
        "result": {
            "item": f"the_bumblezone:luminescent_wax_corner{color}",
            "count": 3
        }
    }
    recipeFile.write(jsbeautifier.beautify(json.dumps(recipeJson), options))


    filePath = f"output/recipes/luminescent_wax/corner_to_channel{color}.json"
    if color == "":
        filePath = "output/recipes/luminescent_wax/corner_to_channel_lightless.json"
    recipeFile = open(filePath, "w+")
    recipeFile.seek(0)
    recipeJson = {
        "type": "minecraft:crafting_shaped",
        "category": "building",
        "group": f"the_bumblezone:luminescent_wax_channel{color}",
        "pattern": [
            "www"
        ],
        "key": {
            "w": {
                "item": f"the_bumblezone:luminescent_wax_corner{color}"
            }
        },
        "result": {
            "item": f"the_bumblezone:luminescent_wax_channel{color}",
            "count": 3
        }
    }
    recipeFile.write(jsbeautifier.beautify(json.dumps(recipeJson), options))


    filePath = f"output/recipes/luminescent_wax/node_to_channel{color}.json"
    if color == "":
        filePath = "output/recipes/luminescent_wax/node_to_channel_lightless.json"
    recipeFile = open(filePath, "w+")
    recipeFile.seek(0)
    recipeJson = {
        "type": "minecraft:crafting_shaped",
        "category": "building",
        "group": f"the_bumblezone:luminescent_wax_channel{color}",
        "pattern": [
            "www"
        ],
        "key": {
            "w": {
                "item": f"the_bumblezone:luminescent_wax_node{color}"
            }
        },
        "result": {
            "item": f"the_bumblezone:luminescent_wax_channel{color}",
            "count": 3
        }
    }
    recipeFile.write(jsbeautifier.beautify(json.dumps(recipeJson), options))


    filePath = f"output/recipes/luminescent_wax/channel_to_node{color}_1.json"
    if color == "":
        filePath = "output/recipes/luminescent_wax/channel_to_node_lightless_1.json"
    recipeFile = open(filePath, "w+")
    recipeFile.seek(0)
    recipeJson = {
        "type": "minecraft:crafting_shaped",
        "category": "building",
        "group": f"the_bumblezone:luminescent_wax_node{color}",
        "pattern": [
            " w ",
            "   ",
            "w w"
        ],
        "key": {
            "w": {
                "item": f"the_bumblezone:luminescent_wax_channel{color}"
            }
        },
        "result": {
            "item": f"the_bumblezone:luminescent_wax_node{color}",
            "count": 3
        }
    }
    recipeFile.write(jsbeautifier.beautify(json.dumps(recipeJson), options))



    filePath = f"output/recipes/luminescent_wax/channel_to_node{color}_2.json"
    if color == "":
        filePath = "output/recipes/luminescent_wax/channel_to_node_lightless_2.json"
    recipeFile = open(filePath, "w+")
    recipeFile.seek(0)
    recipeJson = {
        "type": "minecraft:crafting_shaped",
        "category": "building",
        "group": f"the_bumblezone:luminescent_wax_node{color}",
        "pattern": [
            "w w",
            "   ",
            " w "
        ],
        "key": {
            "w": {
                "item": f"the_bumblezone:luminescent_wax_channel{color}"
            }
        },
        "result": {
            "item": f"the_bumblezone:luminescent_wax_node{color}",
            "count": 3
        }
    }
    recipeFile.write(jsbeautifier.beautify(json.dumps(recipeJson), options))



    filePath = f"output/recipes/luminescent_wax/corner_to_node{color}_1.json"
    if color == "":
        filePath = "output/recipes/luminescent_wax/corner_to_node_lightless_1.json"
    recipeFile = open(filePath, "w+")
    recipeFile.seek(0)
    recipeJson = {
        "type": "minecraft:crafting_shaped",
        "category": "building",
        "group": f"the_bumblezone:luminescent_wax_node{color}",
        "pattern": [
            " w ",
            "   ",
            "w w"
        ],
        "key": {
            "w": {
                "item": f"the_bumblezone:luminescent_wax_corner{color}"
            }
        },
        "result": {
            "item": f"the_bumblezone:luminescent_wax_node{color}",
            "count": 3
        }
    }
    recipeFile.write(jsbeautifier.beautify(json.dumps(recipeJson), options))



    filePath = f"output/recipes/luminescent_wax/corner_to_node{color}_2.json"
    if color == "":
        filePath = "output/recipes/luminescent_wax/corner_to_node_lightless_2.json"
    recipeFile = open(filePath, "w+")
    recipeFile.seek(0)
    recipeJson = {
        "type": "minecraft:crafting_shaped",
        "category": "building",
        "group": f"the_bumblezone:luminescent_wax_node{color}",
        "pattern": [
            "w w",
            "   ",
            " w "
        ],
        "key": {
            "w": {
                "item": f"the_bumblezone:luminescent_wax_corner{color}"
            }
        },
        "result": {
            "item": f"the_bumblezone:luminescent_wax_node{color}",
            "count": 3
        }
    }
    recipeFile.write(jsbeautifier.beautify(json.dumps(recipeJson), options))









