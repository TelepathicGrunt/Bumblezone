from os.path import exists
import os
import json
import jsbeautifier

options = {
  "indent_size": 2,
  "indent_char": " ",
  "max_preserve_newlines": 5,
  "preserve_newlines": True,
  "keep_array_indentation": False,
  "break_chained_methods": False,
  "indent_scripts": "normal",
  "brace_style": "expand",
  "space_before_conditional": True,
  "unescape_strings": False,
  "jslint_happy": False,
  "end_with_newline": False,
  "wrap_line_length": 0,
  "indent_inner_html": False,
  "comma_first": False,
  "e4x": False,
  "indent_empty_lines": False
}

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

existingTag = input("useExistingTag? (y/n)\n").strip()

if existingTag == "y":
    print(f"--Existing Tag Mode--\n")

    restart = True
    while restart:
        modid = input("modid\n").strip()
        group = input("group\n").strip()
        
        if not os.path.exists("output"):
            os.mkdir("output")
        if not os.path.exists("output/trades"):
            os.mkdir("output/trades")

        tradesFile = open(f"output/trades/{modid}_{group}.json", "w+")
        tradesFile.seek(0)
        tradesFileJsonData = {
            "is_color_randomizer_trade": True,
            "randomizes": [
                {
                    "id": f"#{modid}:{group}",
                    "required": False
                }
            ]
        }

        tradesFile.write(jsbeautifier.beautify(json.dumps(tradesFileJsonData), options))
        tradesFile.close()

        print(f"Finished {modid}_{group}!\n")
else :
    print(f"--New Tag Mode--\n")

    restart = True
    while restart:
        modid = input("modid\n").strip()
        group = input("group\n").strip()
        groupColorEntry = input("groupColorEntry (color replaces $1)\n").strip()
        
        if not os.path.exists("output"):
            os.mkdir("output")
        if not os.path.exists("output/trades"):
            os.mkdir("output/trades")

        tradesFile = open(f"output/trades/{modid}_{group}.json", "w+")
        tradesFile.seek(0)
        tradesFileJsonData = {
            "is_color_randomizer_trade": True,
            "randomizes": [
                {
                    "id": f"#the_bumblezone:bee_queen/dedicated_trade_tags/modded/{modid}_{group}",
                    "required": False
                }
            ]
        }

        tradesFile.write(jsbeautifier.beautify(json.dumps(tradesFileJsonData), options))
        tradesFile.close()

        if not os.path.exists("output/tags"):
            os.mkdir("output/tags")
        if not os.path.exists("output/tags/the_bumblezone"):
            os.mkdir("output/tags/the_bumblezone")
        if not os.path.exists("output/tags/the_bumblezone/tags"):
            os.mkdir("output/tags/the_bumblezone/tags")
        if not os.path.exists("output/tags/the_bumblezone/tags/items"):
            os.mkdir("output/tags/the_bumblezone/tags/items")
        if not os.path.exists("output/tags/the_bumblezone/tags/items/modded"):
            os.mkdir("output/tags/the_bumblezone/tags/items/modded")

        tagsFile = open(f"output/tags/the_bumblezone/tags/items/modded/{modid}_{group}.json", "w+")
        tagsFile.seek(0)
        tagsFileJsonData = {
            "replace": False,
            "values": []
        }   

        for color in colors:
            tagsFileJsonData["values"].append({
                "id": f"{modid}:{groupColorEntry.replace("$1", color)}",
                "required": False
            })

        tagsFile.write(jsbeautifier.beautify(json.dumps(tagsFileJsonData), options))
        tagsFile.close()

        print(f"Finished {modid}_{group}!\n")




