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

randomizerLocation = os.path.join('..', 'common', 'src', 'main', 'resources', 'data', 'the_bumblezone', 'bz_bee_queen_trades', 'randomizer')

entriesToBlacklist = []
for (subdir, dirs, files) in os.walk(randomizerLocation, topdown=True):
    for file in files:
        directory = subdir + os.sep
        filepath = directory + file

        if filepath.endswith(".json"):
            tradeData = {}
            with open(filepath, 'r') as file:
                tradeData = json.loads(file.read())
                for entry in tradeData["randomizes"]:
                    entriesToBlacklist.append(entry["id"])

blacklistFile = open(f"blacklist.json", "w+")
blacklistFile.seek(0)
blacklistFileJsonData = {
  "replace": False,
  "values": []
}

for entry in entriesToBlacklist:
    if (not entry.startswith("#minecraft") and not entry.startswith("#c:") and not (entry.startswith("#the_bumblezone:") and not entry.startswith("#the_bumblezone:bee_queen/dedicated_trade_tags/modded"))):
        blacklistFileJsonData["values"].append({
            "id": entry,
            "required": False
        })

blacklistFile.write(jsbeautifier.beautify(json.dumps(blacklistFileJsonData), options))
blacklistFile.close()

print(f"Finished!\n")




