
import os

def main():
    prefix = input("Enter the prefix to remove: ").strip()

    if not prefix:
        print("No prefix provided.")
        return

    script_name = os.path.basename(__file__)
    renamed = 0
    skipped = 0

    for filename in os.listdir("."):
        # Skip directories
        if not os.path.isfile(filename):
            continue

        # Skip the script itself
        if filename == script_name:
            continue

        if filename.startswith(prefix):
            new_name = filename[len(prefix):]

            if not new_name:
                print(f"Skipping '{filename}' (new name would be empty)")
                skipped += 1
                continue

            if os.path.exists(new_name):
                print(f"Skipping '{filename}' -> '{new_name}' (target already exists)")
                skipped += 1
                continue

            os.rename(filename, new_name)
            print(f"Renamed: {filename} -> {new_name}")
            renamed += 1
        else:
            skipped += 1

    print(f"\nDone! Renamed {renamed} file(s), skipped {skipped}.")

if __name__ == "__main__":
    main()