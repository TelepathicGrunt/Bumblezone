import json

#--------------------------------------------------------------------------------------------

org_file = "saplings.json"
diff_file = "newsapling.json"

entries = []

with open(org_file, 'r') as file:
    entries = list(set(json.load(file)))

entries.sort() 

with open(diff_file, 'w+') as file:
    file.write("{" + '\n')
    file.write("  \"replace\": false," + '\n')
    file.write("  \"values\": [" + '\n')
    for entry in entries:
        if entry.startswith('minecraft:'):
            file.write("    \"" + entry + '\",\n')
        else:
            file.write("    {" + '\n')
            file.write("      \"id\": \"" + entry + '\",\n')
            file.write("      \"required\": false" + '\n')
            file.write("    }," + '\n')
    file.write("  ]" + '\n')
    file.write("}")

print('\n\nFINISHED!')
