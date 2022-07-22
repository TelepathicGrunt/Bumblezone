package com.telepathicgrunt.the_bumblezone.entities.queentrades;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.item.Item;
import org.quiltmc.qsl.resource.loader.api.reloader.IdentifiableResourceReloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class QueensTradeManager extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloader{
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().setLenient().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();
    public static final QueensTradeManager QUEENS_TRADE_MANAGER = new QueensTradeManager();
    private final ResourceLocation QUEENS_TRADE_MANAGER_ID = new ResourceLocation(Bumblezone.MODID, "queens_trade_manager");

    public Map<Set<Item>, WeightedRandomList<TradeEntryReducedObj>> tradeReduced = new HashMap<>();
    public Map<List<TradeEntryObj>, List<TradeEntryObj>> tradeRaw = new HashMap<>();

    public QueensTradeManager() {
        super(GSON, "bz_bee_queen_trades");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> loader, ResourceManager manager, ProfilerFiller profiler) {
        tradeRaw.clear();
        loader.forEach((fileIdentifier, jsonElement) -> {
            try {
                QueenTradesCollectionObj tradesCollection = GSON.fromJson(jsonElement, QueenTradesCollectionObj.class);
                if (tradesCollection.wants.isEmpty() || tradesCollection.possible_rewards.isEmpty()) {
                    return;
                }
                tradeRaw.put(tradesCollection.wants, tradesCollection.possible_rewards);
            }
            catch (Exception e) {
                Bumblezone.LOGGER.error("Bumblezone Error: Couldn't parse bee queen trades file {}", fileIdentifier, e);
            }
        });
    }

    // KEEP THIS HERE BECAUSE ABOVE FIRES BEFORE TAGS ARE READY
    public void resolveQueenTrades() {
        if (tradeRaw.isEmpty()) {
            return;
        }

        ImmutableMap.Builder<Set<Item>, WeightedRandomList<TradeEntryReducedObj>> reducedTradeBuilder = ImmutableMap.builder();
        for (Map.Entry<List<TradeEntryObj>, List<TradeEntryObj>> entry : tradeRaw.entrySet()) {

            Set<Item> wants = new ObjectOpenHashSet<>();
            List<TradeEntryReducedObj> rewards = new ArrayList<>();
            entry.getKey().forEach((value) -> {
                Set<Item> items = null;
                if (value.id.startsWith("#")) {
                    ResourceLocation tagRl = new ResourceLocation(value.id.substring(1));
                    TagKey<Item> itemTag = TagKey.create(Registry.ITEM_REGISTRY, tagRl);
                    Optional<HolderSet.Named<Item>> taggedItems = Registry.ITEM.getTag(itemTag);
                    if (taggedItems.isPresent()) {
                        items = taggedItems.get().stream().map(Holder::value).collect(Collectors.toSet());
                    }
                    else if (value.isRequired()) {
                        Bumblezone.LOGGER.error("Bumblezone Error: Couldn't find want item tag {} in bee queen trades file", value.id);
                    }
                }
                else {
                    Optional<Item> item = Registry.ITEM.getOptional(new ResourceLocation(value.id));
                    if (item.isPresent()) {
                        items = Set.of(item.get());
                    }
                    else if (value.isRequired()) {
                        Bumblezone.LOGGER.error("Bumblezone Error: Couldn't find want item {} in bee queen trades file", value.id);
                    }
                }

                if (items == null) {
                    return;
                }

                wants.addAll(items);
            });

            entry.getValue().forEach((value) -> {
                if (value.id.startsWith("#")) {
                    ResourceLocation tagRl = new ResourceLocation(value.id.substring(1));
                    TagKey<Item> itemTag = TagKey.create(Registry.ITEM_REGISTRY, tagRl);
                    Optional<HolderSet.Named<Item>> taggedItems = Registry.ITEM.getTag(itemTag);
                    if (taggedItems.isPresent()) {
                        for (Holder<Item> itemHolder : taggedItems.get()) {
                            rewards.add(new TradeEntryReducedObj(itemHolder.value(), value.getCount(), value.getXpReward(), value.getWeight()));
                        }
                    }
                    else if (value.isRequired()) {
                        Bumblezone.LOGGER.error("Bumblezone Error: Couldn't find reward item tag {} in bee queen trades file", value.id);
                    }
                }
                else {
                    Optional<Item> item = Registry.ITEM.getOptional(new ResourceLocation(value.id));
                    if (item.isPresent()) {
                        rewards.add(new TradeEntryReducedObj(item.get(), value.getCount(), value.getXpReward(), value.getWeight()));
                    }
                    else if (value.isRequired()) {
                        Bumblezone.LOGGER.error("Bumblezone Error: Couldn't find reward item {} in bee queen trades file", value.id);
                    }
                }
            });

            if (wants.isEmpty()) {
                continue;
            }

            if (rewards.isEmpty()) {
                continue;
            }

            reducedTradeBuilder.put(wants, WeightedRandomList.create(rewards));
        }
        tradeReduced = reducedTradeBuilder.build();
        tradeRaw.clear();
    }

    @Override
    public ResourceLocation getQuiltId() {
        return QUEENS_TRADE_MANAGER_ID;
    }
}
