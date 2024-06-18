package com.telepathicgrunt.the_bumblezone.blocks;

import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.events.lifecycle.TagsUpdatedEvent;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.telepathicgrunt.the_bumblezone.Bumblezone.GSON;

public class CrystallineFlowerDataManager extends SimpleJsonResourceReloadListener {
    public static final CrystallineFlowerDataManager CRYSTALLINE_FLOWER_DATA_MANAGER = new CrystallineFlowerDataManager();

    public record ItemConsumeData(TagKey<Item> tag, int xp, boolean maxXp) {
        public static final Codec<ItemConsumeData> ENTRY_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                TagKey.codec(Registries.ITEM).fieldOf("tag").forGetter(i -> i.tag),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("xp_granted").orElse(1).forGetter(i -> i.xp),
                Codec.BOOL.fieldOf("max_xp_granted").orElse(false).forGetter(i -> i.maxXp)
        ).apply(instance, instance.stable(ItemConsumeData::new)));
    }

    public record FlowerData(List<ItemConsumeData> itemConsumeData, Optional<TagKey<Item>> disallowTag, Optional<Boolean> allowNormalConsumption) {
        public static final Codec<FlowerData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                ItemConsumeData.ENTRY_CODEC.listOf().fieldOf("item_consume_data").orElse(new ArrayList<>()).forGetter(e -> e.itemConsumeData),
                TagKey.codec(Registries.ITEM).optionalFieldOf("do_not_consume_items_override").forGetter(e -> e.disallowTag),
                Codec.BOOL.optionalFieldOf("allow_default_1_xp_item_consuming").forGetter(i -> i.allowNormalConsumption)
        ).apply(instance, instance.stable(FlowerData::new)));
    }

    public final List<FlowerData> cachedFlowerData = new ArrayList<>();
    public final Map<Item, Pair<Integer, Boolean>> itemToXp = new Object2ObjectArrayMap<>();
    public Set<Item> disallowConsume = new HashSet<>();
    public boolean allowNormalConsumption = true;


    public CrystallineFlowerDataManager() {
        super(GSON, "bz_crystalline_flower_data");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> loader, ResourceManager manager, ProfilerFiller profiler) {
        itemToXp.clear();
        disallowConsume.clear();
        loader.forEach((fileIdentifier, jsonElement) -> {
            try {
                DataResult<FlowerData> mapDataResult = FlowerData.CODEC.parse(JsonOps.INSTANCE, jsonElement);
                mapDataResult.resultOrPartial((s) -> {}).ifPresent(cachedFlowerData::add);
            }
            catch (Exception e) {
                Bumblezone.LOGGER.error("Bumblezone Error: Couldn't parse crystalline flower data file: {}", fileIdentifier, e);
            }
        });
    }

    // KEEP THIS HERE BECAUSE ABOVE FIRES BEFORE TAGS ARE READY
    public void resolveFlowerData(final TagsUpdatedEvent event) {
        for (FlowerData flowerData : cachedFlowerData) {
            flowerData.allowNormalConsumption().ifPresent(b -> allowNormalConsumption = b);

            flowerData.disallowTag().flatMap(BuiltInRegistries.ITEM::getTag)
                    .ifPresent(tagItems -> tagItems.forEach(itemHolder -> disallowConsume.add(itemHolder.value())));

            flowerData.itemConsumeData().forEach(itemConsumeData -> {
                BuiltInRegistries.ITEM.getTag(itemConsumeData.tag()).ifPresent(tagItems -> {
                    for (Holder<Item> itemHolder : tagItems) {
                        if (itemConsumeData.maxXp) {
                            itemToXp.put(itemHolder.value(), Pair.of(itemConsumeData.xp, true));
                            break;
                        }

                        Pair<Integer, Boolean> pair = itemToXp.get(itemHolder.value());
                        if (pair == null || (pair.getFirst() < itemConsumeData.xp && !pair.getSecond())) {
                            itemToXp.put(itemHolder.value(), Pair.of(itemConsumeData.xp, false));
                            break;
                        }
                    }
                });
            });
        }

        cachedFlowerData.clear();
    }
}
