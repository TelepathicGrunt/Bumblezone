package com.telepathicgrunt.the_bumblezone.entities.pollenpuffentityflowers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PollenPuffEntityPollinateManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().setLenient().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();
    public static final PollenPuffEntityPollinateManager POLLEN_PUFF_ENTITY_POLLINATE_MANAGER = new PollenPuffEntityPollinateManager();

    public Map<EntityType<?>, List<BlockState>> mobToFlowers = new Object2ObjectArrayMap<>();

    public PollenPuffEntityPollinateManager() {
        super(GSON, "bz_pollen_puff_entity_flowers");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> loader, ResourceManager manager, ProfilerFiller profiler) {
        mobToFlowers.clear();
        loader.forEach((fileIdentifier, jsonElement) -> {
            try {
                EntriesCollectionObj entries = GSON.fromJson(jsonElement, EntriesCollectionObj.class);
                for (EntriesObj entriesObj : entries.entries) {
                    Optional<EntityType<?>> entityTypeOptional = Registry.ENTITY_TYPE.getOptional(new ResourceLocation(entriesObj.entity_type));
                    entityTypeOptional.ifPresent(entityType -> {
                        List<BlockState> plants = new ObjectArrayList<>();
                        for (String plantNames : entriesObj.plants_to_spawn) {
                            Optional<Block> blockOptional = Registry.BLOCK.getOptional(new ResourceLocation(plantNames));
                            blockOptional.ifPresent(plant -> plants.add(plant.defaultBlockState()));
                        }
                        if (!plants.isEmpty()) {
                            mobToFlowers.put(entityType, plants);
                        }
                    });
                }
            }
            catch (Exception e) {
                Bumblezone.LOGGER.error("Bumblezone Error: Couldn't parse pollen puff entity to flower file {}", fileIdentifier, e);
            }
        });
    }
}
