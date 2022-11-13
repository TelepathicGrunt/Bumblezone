from os.path import exists
import os
import shutil
import json

directions = ["up", "down", "north", "south", "east", "west"]
stages = 4
texturesPerStage = 6

if not os.path.exists("output"):
    os.mkdir("output")

blockstateFile = open("output/honeycomb_brood_block.json", "w+")
blockstateFile.seek(0)
variants = {}
jsonData = {}
jsonData["variants"] = variants
for direction in directions:
    for stage in range(stages):
        models = []
        for texture in range(texturesPerStage):
            models.append({
                "model": f"the_bumblezone:block/honeycomb_brood_block/stage_{stage + 1}_honeycomb_brood_block_{direction}_{texture + 1}"
            })
        variants[f"facing={direction},age={stage}"] = models
blockstateFile.write(json.dumps(jsonData, indent=2))

if not os.path.exists("output/models"):
    os.mkdir("output/models")

for direction in directions:
    for stage in range(stages):
        for texture in range(texturesPerStage):
            modelFile = open(f"output/models/stage_{stage + 1}_honeycomb_brood_block_{direction}_{texture + 1}.json", "w+")
            modelFile.seek(0)
            modelJson = {
                "parent": "minecraft:block/block",
                "textures": {
                    "side": "the_bumblezone:block/honeycomb_brood_block/honeycomb_brood_block_side",
                    "front": f"the_bumblezone:block/honeycomb_brood_block/stage_{stage + 1}_honeycomb_brood_block_{texture + 1}",
                    "particle": "the_bumblezone:block/filled_porous_honeycomb_block"
                },
                "elements": [
                    {
                        "from": [ 0, 0, 0 ],
                        "to": [ 16, 16, 16 ],
                        "faces": {
                                "down": {
                                "texture": ("#front" if direction == "up" else "#side"),
                                "cullface": "down"
                            },
                                "up": {
                                "texture": ("#front" if direction == "down" else "#side"),
                                "cullface": "up"
                            },
                                "north": {
                                "texture": ("#front" if direction == "south" else "#side"),
                                "cullface": "north"
                            },
                                "south": {
                                "texture": ("#front" if direction == "north" else "#side"),
                                "cullface": "south"
                            },
                                "west": {
                                "texture": ("#front" if direction == "east" else "#side"),
                                "cullface": "west"
                            },
                                "east": {
                                "texture": ("#front" if direction == "west" else "#side"),
                                "cullface": "east"
                            }
                        }
                    }
                ]
            }
            modelFile.write(json.dumps(modelJson, indent=2))

if not os.path.exists("output/mcmeta"):
    os.mkdir("output/mcmeta")

frames = [
    [ 0, 1, 2, 3, 4, 5 ],
    [ 2, 3, 4, 5, 0, 1 ],
    [ 4, 5, 0, 1, 2, 3 ]
]
for stage in range(stages):
    for texture in range(texturesPerStage):
        modelFile = open(f"output/mcmeta/stage_{stage + 1}_honeycomb_brood_block_{texture + 1}.png.mcmeta", "w+")
        modelFile.seek(0)
        modelJson = {
            "animation": {
                "interpolate": False,
                "frametime": (6 if texture < 2 else 5),
                "frames": frames[texture % 3]
            }
        }
        modelFile.write(json.dumps(modelJson, indent=2))





