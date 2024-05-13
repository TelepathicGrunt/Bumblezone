package com.telepathicgrunt.the_bumblezone.loot;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.loot.LootTable;

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

        ResourceLocation resourceLocation = minecraftServer
                .registryAccess()
                .registryOrThrow(Registries.LOOT_TABLE)
                .getKey(lootTable);

        boolean isEntityLootTable = (resourceLocation == null ? "" : resourceLocation.getPath()).contains("entities/");
        CACHED_IS_ENTITY_LOOT_TABLES.put(lootTable, isEntityLootTable);
        return isEntityLootTable;
    }
}
