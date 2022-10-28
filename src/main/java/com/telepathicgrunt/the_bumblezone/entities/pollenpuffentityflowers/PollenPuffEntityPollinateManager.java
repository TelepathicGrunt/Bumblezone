package com.telepathicgrunt.the_bumblezone.entities.pollenpuffentityflowers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.utils.BzNbtPredicate;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraftforge.fml.ModList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PollenPuffEntityPollinateManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().setLenient().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();
    public static final PollenPuffEntityPollinateManager POLLEN_PUFF_ENTITY_POLLINATE_MANAGER = new PollenPuffEntityPollinateManager();

    public record EntryObject(BzNbtPredicate nbtPredicate, WeightedStateProvider weightedStateProvider) {
        public static final Codec<EntryObject> ENTRY_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                BzNbtPredicate.CODEC.fieldOf("nbt_match").orElse(BzNbtPredicate.ANY).forGetter(config -> config.nbtPredicate),
                WeightedStateProvider.CODEC.fieldOf("plants_to_spawn").forGetter(config -> config.weightedStateProvider)
        ).apply(instance, instance.stable(EntryObject::new)));
    }

    public static final Codec<Map<EntityType<?>, List<EntryObject>>> CODEC =
            Codec.unboundedMap(ResourceLocation.CODEC.comapFlatMap(r -> {
                Optional<EntityType<?>> entityTypeOptional = Registry.ENTITY_TYPE.getOptional(r);
                if (entityTypeOptional.isPresent()) {
                    return DataResult.success(entityTypeOptional.get());
                }
                else if (ModList.get().isLoaded(r.getNamespace())) {
                    return DataResult.error("Bz Error - Unknown EntityType:  " + r + "  - ");
                }
                else {
                    return DataResult.error("Bz Error - Target mod not present");
                }
            }, Registry.ENTITY_TYPE::getKey), Codec.list(EntryObject.ENTRY_CODEC));

    public Map<EntityType<?>, List<EntryObject>> mobToPlants = new Object2ObjectArrayMap<>();

    public PollenPuffEntityPollinateManager() {
        super(GSON, "bz_pollen_puff_entity_flowers");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> loader, ResourceManager manager, ProfilerFiller profiler) {
        mobToPlants.clear();
        loader.forEach((fileIdentifier, jsonElement) -> {
            try {
                DataResult<Map<EntityType<?>, List<EntryObject>>> mapDataResult = CODEC.parse(JsonOps.INSTANCE, jsonElement);
                mapDataResult.error().ifPresent(e -> {
                    if (!e.message().contains("Bz Error - Target mod not present")) {
                        Bumblezone.LOGGER.error("Bumblezone Error: Couldn't parse pollen puff entity to flower file {} - Error: {}", fileIdentifier, e);
                    }
                 });
                Map<EntityType<?>, List<EntryObject>> newMap = mapDataResult.result().orElse(new HashMap<>());

                newMap.forEach((e, v) -> {
                    // Combine existing entries
                    if (mobToPlants.containsKey(e)) {
                        mobToPlants.get(e).addAll(v);
                    }
                    // Add new entries
                    else {
                        mobToPlants.put(e, v);
                    }
                });
            }
            catch (Exception e) {
                Bumblezone.LOGGER.error("Bumblezone Error: Couldn't parse pollen puff entity to flower file: {}", fileIdentifier, e);
            }
        });
    }

    public WeightedStateProvider getPossiblePlants(Entity entity) {
        if (this.mobToPlants.containsKey(entity.getType())) {
            for (EntryObject entryObject : mobToPlants.get(entity.getType())) {
                if (entryObject.nbtPredicate().matches(entity)) {
                    return entryObject.weightedStateProvider();
                }
            }
        }
        return null;
    }
}
