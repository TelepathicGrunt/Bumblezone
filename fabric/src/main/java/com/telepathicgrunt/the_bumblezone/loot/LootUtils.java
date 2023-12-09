package com.telepathicgrunt.the_bumblezone.loot;

import com.telepathicgrunt.the_bumblezone.mixin.fabric.loot.LootDataManagerAccessor;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Map;

public class LootUtils {
    public static MinecraftServer CURRENT_SERVER = null;
    public static Object2BooleanMap<LootTable> CACHED_IS_ENTITY_LOOT_TABLES = new Object2BooleanOpenHashMap<>();

    public static boolean isEntityLootTable(MinecraftServer minecraftServer, LootTable lootTable) {
        if (CURRENT_SERVER != minecraftServer) {
            CURRENT_SERVER = minecraftServer;
            CACHED_IS_ENTITY_LOOT_TABLES.clear();
        }

        if (CACHED_IS_ENTITY_LOOT_TABLES.containsKey(lootTable)) {
            return CACHED_IS_ENTITY_LOOT_TABLES.getBoolean(lootTable);
        }

        Map<LootDataId<?>, ?> map = ((LootDataManagerAccessor)minecraftServer.getLootData()).getElements();
        String lootTablePath = "";
        for (Map.Entry<LootDataId<?>, ?> lootDataEntry : map.entrySet()) {
            if (lootDataEntry.getValue().equals(lootTable)) {
                lootTablePath = lootDataEntry.getKey().location().getPath();
                break;
            }
        }

        boolean isEntityLootTable = lootTablePath.contains("entities/");
        CACHED_IS_ENTITY_LOOT_TABLES.put(lootTable, isEntityLootTable);
        return isEntityLootTable;
    }
}
