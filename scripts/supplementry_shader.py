from os.path import exists
import os
import shutil
import json
import jsbeautifier

options = jsbeautifier.default_options()
options.indent_size = 2

with open('../common/src/main/resources/resourcepacks/shader_emissive/assets/colored_lights/light_colors.json', mode="r", encoding="utf-8") as origin_file:
    source_block_color_data = json.load(origin_file)

    for blockIdentifier, rawColor in source_block_color_data['colors'].items():
        blockName = blockIdentifier.removeprefix('the_bumblezone:')
        strippedRawColor = rawColor.removeprefix('#')
        color = tuple(int(strippedRawColor[i:i+2], 16) for i in (0, 2, 4))

        if not os.path.exists("../common/src/main/resources/resourcepacks/shader_emissive/assets/the_bumblezone/euphoria/colors"):
            os.makedirs("../common/src/main/resources/resourcepacks/shader_emissive/assets/the_bumblezone/euphoria/colors")
        lightColorFile = open(f"../common/src/main/resources/resourcepacks/shader_emissive/assets/the_bumblezone/euphoria/colors/{blockName}.json", "w+")
        lightColorFile.seek(0)
        lightColorData = {
            "code": f"vec4(vec3({round(color[0] / 255, 3)}, {round(color[1] / 255, 3)}, {round(color[2] / 255, 3)}) * 4, 0.0)"
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

