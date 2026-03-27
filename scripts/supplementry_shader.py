from os.path import exists
import os
import shutil
import json
import jsbeautifier

options = jsbeautifier.default_options()
options.indent_size = 2
options.brace_style = "expand"

def write_emitters_file(options, blockIdentifier, rawColor):
    if not os.path.exists("../common/src/main/resources/resourcepacks/shader_emissive/assets/the_bumblezone/light"):
        os.makedirs("../common/src/main/resources/resourcepacks/shader_emissive/assets/the_bumblezone/light")
    emittersFile = open(f"../common/src/main/resources/resourcepacks/shader_emissive/assets/the_bumblezone/light/emitters.json", "r+")
    emittersFile.seek(0)
    emittersFileContents = json.load(emittersFile)
    if blockIdentifier not in emittersFileContents or emittersFileContents[blockIdentifier] != rawColor:
        emittersFileContents[blockIdentifier] = rawColor
        emittersFile.seek(0)
        emittersFile.write(jsbeautifier.beautify(json.dumps(emittersFileContents), options))
        emittersFile.truncate()

def write_shimmer_file(blockIdentifier, blockName, color):
    if (blockName != 'glistering_honey_crystal' and blockName != 'infinity_barrier'):
        if not os.path.exists("../common/src/main/resources/resourcepacks/shader_emissive/assets/shimmer"):
            os.makedirs("../common/src/main/resources/resourcepacks/shader_emissive/assets/shimmer")
        shimmerFile = open(f"../common/src/main/resources/resourcepacks/shader_emissive/assets/shimmer/shimmer.json", "r+")
        shimmerFile.seek(0)
        shimmerFileContents = json.load(shimmerFile)
        listOfLightBlocks = shimmerFileContents["LightBlock"]
        matchedEntry = next((blockObject for blockObject in listOfLightBlocks if blockObject["block"] == blockIdentifier), None)
        if matchedEntry is None:
            listOfLightBlocks.append({
                    "block": f"{blockIdentifier}",
                    "r": color[0], "g": color[1], "b": color[2], "a": 255,
                    "radius": 15
                })
            shimmerFile.seek(0)
            shimmerFile.write(json.dumps(shimmerFileContents, indent=2))
            shimmerFile.truncate()
        elif matchedEntry["r"] != color[0] or matchedEntry["g"] != color[1] or matchedEntry["b"] != color[2]:
            matchedEntry["r"] = color[0]
            matchedEntry["g"] = color[1]
            matchedEntry["b"] = color[2]
            shimmerFile.seek(0)
            shimmerFile.write(json.dumps(shimmerFileContents, indent=2))
            shimmerFile.truncate()

def write_supplemental_files(options, blockIdentifier, blockName):
    if not os.path.exists("../common/src/main/resources/resourcepacks/shader_emissive/assets/the_bumblezone/euphoria/colors"):
        os.makedirs("../common/src/main/resources/resourcepacks/shader_emissive/assets/the_bumblezone/euphoria/colors")
    lightColorFile = open(f"../common/src/main/resources/resourcepacks/shader_emissive/assets/the_bumblezone/euphoria/colors/{blockName}.json", "w+")
    lightColorFile.seek(0)
    lightMultiplier = 2 if blockName == 'infinity_barrier' else 4
    lightRedFraction = round(color[0] / 255, 3)
    lightGreenFraction = round(color[1] / 255, 3)
    lightBlueFraction = round(color[2] / 255, 3)
    lightColorData = {
        "code": f"vec4(vec3({lightRedFraction}, {lightGreenFraction}, {lightBlueFraction}) * {lightMultiplier}, 0.0)"
    }
    lightColorFile.write(jsbeautifier.beautify(json.dumps(lightColorData), options))

    if not os.path.exists("../common/src/main/resources/resourcepacks/shader_emissive/assets/the_bumblezone/euphoria/terrain"):
        os.makedirs("../common/src/main/resources/resourcepacks/shader_emissive/assets/the_bumblezone/euphoria/terrain")
    terrainFile = open(f"../common/src/main/resources/resourcepacks/shader_emissive/assets/the_bumblezone/euphoria/terrain/{blockName}.json", "w+")
    terrainFile.seek(0)
    terrainData = {
            "name": f"{blockName}",
            "glsl": f"{blockName}.glsl",
            "mat0": [
                f"{blockIdentifier}",
            ],
            "color": f"{blockIdentifier}",
            "held_lighting": True
        }
    terrainFile.write(jsbeautifier.beautify(json.dumps(terrainData), options))

    if not os.path.exists("../common/src/main/resources/resourcepacks/shader_emissive/assets/the_bumblezone/euphoria/terrain"):
        os.makedirs("../common/src/main/resources/resourcepacks/shader_emissive/assets/the_bumblezone/euphoria/terrain")
    shaderFile = open(f"../common/src/main/resources/resourcepacks/shader_emissive/assets/the_bumblezone/euphoria/terrain/{blockName}.glsl", "w+")
    shaderFile.seek(0)
    shader = "emission = 2 * pow2((color.r + color.g + color.b) / 3);"
    if blockName.endswith("_blue"):
        shader = "emission = color.b > 0.4 ? 2 * pow2(color.b) : 0.0;"
    if blockName.endswith("_red"):
        shader = "emission = color.r > 0.4 ? 2 * pow2(color.r) : 0.0;"
    if blockName.endswith("_green"):
        shader = "emission = color.g > 0.4 ? 2 * pow2(color.g) : 0.0;"
    if blockName.endswith("_yellow"):
        shader = "emission = (color.r + color.g) > 1.2 ? 2 * pow2((color.r + color.g) / 2) : 0.0;"
    if blockName.endswith("_purple"):
        shader = "emission = (color.r + color.b) > 1.2 ? 2 * pow2((color.r + color.b) / 2) : 0.0;"
    if blockName.endswith("_white"):
        shader = "emission = (color.r + color.g + color.b) > 2 ? 2 * pow2((color.r + color.g + color.b) / 3) : 0.0;"
    shaderFile.write(shader)

# BEGIN THE WRITING. This light colors file is the source of truth.
with open('../common/src/main/resources/resourcepacks/shader_emissive/assets/colored_lights/light_colors.json', mode="r", encoding="utf-8") as origin_file:
    source_block_color_data = json.load(origin_file)

    for blockIdentifier, rawColor in source_block_color_data['colors'].items():
        blockName = blockIdentifier.removeprefix('the_bumblezone:')
        strippedRawColor = rawColor.removeprefix('#')
        color = tuple(int(strippedRawColor[i:i+2], 16) for i in (0, 2, 4))

        write_supplemental_files(options, blockIdentifier, blockName)
        write_shimmer_file(blockIdentifier, blockName, color)
        write_emitters_file(options, blockIdentifier, rawColor)
        
