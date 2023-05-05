package com.telepathicgrunt.the_bumblezone.entities.queentrades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.mixin.util.WeightedRandomListAccessor;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.MainTradeRowInput;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.RandomizeTradeRowInput;
import com.telepathicgrunt.the_bumblezone.packets.QueenMainTradesSyncPacket;
import com.telepathicgrunt.the_bumblezone.packets.QueenRandomizerTradesSyncPacket;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.item.Item;
import org.quiltmc.qsl.resource.loader.api.reloader.IdentifiableResourceReloader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class QueensTradeManager extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloader{
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().setLenient().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();
    public static final QueensTradeManager QUEENS_TRADE_MANAGER = new QueensTradeManager();
    private final ResourceLocation QUEENS_TRADE_MANAGER_ID = new ResourceLocation(Bumblezone.MODID, "queens_trade_manager");

    private final List<TradeCollection> rawTrades = new ArrayList<>();
    public Object2ObjectOpenHashMap<Item, WeightedRandomList<WeightedTradeResult>> queenTrades = new Object2ObjectOpenHashMap<>();
    public List<RandomizeTradeRowInput> recipeViewerRandomizerTrades = new ArrayList<>();
    public List<Pair<MainTradeRowInput, WeightedRandomList<WeightedTradeResult>>> recipeViewerMainTrades = new ArrayList<>();

    public record TradeCollection(
        Optional<List<RawTradeInputEntry>> randomizerItems,
        Optional<List<RawTradeInputEntry>> wantItems,
        Optional<List<RawTradeOutputEntry>> resultItems,
        boolean randomizerTrade)
    {
        public static final Codec<TradeCollection> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                RawTradeInputEntry.CODEC.listOf().optionalFieldOf("randomizes").forGetter(e -> e.randomizerItems),
                RawTradeInputEntry.CODEC.listOf().optionalFieldOf("wants").forGetter(e -> e.wantItems),
                RawTradeOutputEntry.CODEC.listOf().optionalFieldOf("possible_rewards").forGetter(e -> e.resultItems),
                Codec.BOOL.fieldOf("is_color_randomizer_trade").orElse(false).forGetter(e -> e.randomizerTrade)
        ).apply(instance, instance.stable(TradeCollection::new)));
        //Error validate if want is added without reward and vice versa
        // No want or possible rewards fields if randomizer is on
    }

    public record RawTradeInputEntry(String entry, boolean required) {
        public static final Codec<RawTradeInputEntry> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.STRING.fieldOf("id").forGetter(e -> e.entry),
            Codec.BOOL.fieldOf("required").forGetter(e -> e.required)
        ).apply(instance, instance.stable(RawTradeInputEntry::new)));
    }

    public record RawTradeOutputEntry(String entry, boolean required, int count, int xpReward, int weight) {
        public static final Codec<RawTradeOutputEntry> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                Codec.STRING.fieldOf("id").forGetter(e -> e.entry),
                Codec.BOOL.fieldOf("required").forGetter(e -> e.required),
                Codec.intRange(1, 64).fieldOf("count").forGetter(e -> e.count),
                Codec.intRange(0, Integer.MAX_VALUE).fieldOf("xp_reward").forGetter(e -> e.xpReward),
                Codec.intRange(1, Integer.MAX_VALUE).fieldOf("weight").forGetter(e -> e.weight)
        ).apply(instance, instance.stable(RawTradeOutputEntry::new)));
    }

    public record TradeWantEntry(Optional<TagKey<Item>> tagKey, HolderSet<Item> wantItems) {
        public static final Codec<TradeWantEntry> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                TagKey.codec(Registry.ITEM_REGISTRY).optionalFieldOf("tagkey").forGetter(e -> e.tagKey),
                RegistryCodecs.homogeneousList(Registry.ITEM_REGISTRY, Registry.ITEM.byNameCodec()).fieldOf("wantItems").forGetter(e -> e.wantItems)
        ).apply(instance, instance.stable(TradeWantEntry::new)));
    }

    public record TradeResultEntry(Optional<TagKey<Item>> tagKey, HolderSet<Item> resultItems, int count, int xpReward, int weight) {
        public static final Codec<TradeResultEntry> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                TagKey.codec(Registry.ITEM_REGISTRY).optionalFieldOf("tagkey").forGetter(e -> e.tagKey),
                RegistryCodecs.homogeneousList(Registry.ITEM_REGISTRY, Registry.ITEM.byNameCodec()).fieldOf("wantItems").forGetter(e -> e.resultItems),
                Codec.intRange(1, 64).fieldOf("count").forGetter(e -> e.count),
                Codec.intRange(0, Integer.MAX_VALUE).fieldOf("xp_reward").forGetter(e -> e.xpReward),
                Codec.intRange(1, Integer.MAX_VALUE).fieldOf("weight").forGetter(e -> e.weight)
        ).apply(instance, instance.stable(TradeResultEntry::new)));
    }

    public QueensTradeManager() {
        super(GSON, "bz_bee_queen_trades");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> loader, ResourceManager manager, ProfilerFiller profiler) {
        rawTrades.clear();
        loader.forEach((fileIdentifier, jsonElement) -> {
            try {
                DataResult<TradeCollection> mapDataResult = TradeCollection.CODEC.parse(JsonOps.INSTANCE, jsonElement);
                mapDataResult.resultOrPartial((s) -> {}).ifPresent(rawTrades::add);
            }
            catch (Exception e) {
                Bumblezone.LOGGER.error("Bumblezone Error: Couldn't parse bee queen trades file {}", fileIdentifier, e);
            }
        });
    }

    // KEEP THIS HERE BECAUSE ABOVE FIRES BEFORE TAGS ARE READY
    public void resolveQueenTrades() {
        if (rawTrades.isEmpty()) {
            return;
        }

        List<RandomizeTradeRowInput> tempRecipeViewerRandomizerTrades = new ArrayList<>();
        List<TradeWantEntry> tempRecipeViewerMainTagTrades = new ArrayList<>();
        List<Pair<MainTradeRowInput, WeightedRandomList<WeightedTradeResult>>> tempRecipeViewerMainTrades = new ArrayList<>();

        // TagKey is to store if the key item was from a Tag or not.
        Object2ObjectOpenHashMap<Item, Pair<WeightedRandomList<WeightedTradeResult>, TagKey<Item>>> tempQueenTradesFirstPass = new Object2ObjectOpenHashMap<>();
        Object2ObjectOpenHashMap<Item, WeightedRandomList<WeightedTradeResult>> tempQueenTrades = new Object2ObjectOpenHashMap<>();

        for (TradeCollection entry : rawTrades) {

            // Populate actual trades.
            if (entry.randomizerTrade()) {
                if (entry.randomizerItems().isEmpty()) continue;
                for (RawTradeInputEntry rawTradeRandomizeEntry : entry.randomizerItems().get()) {
                    TradeWantEntry tradeRandomizeEntry = getInputTradeEntry(rawTradeRandomizeEntry);

                    if (tradeRandomizeEntry == null || tradeRandomizeEntry.wantItems().size() == 0) {
                        continue;
                    }

                    tempRecipeViewerRandomizerTrades.add(new RandomizeTradeRowInput(tradeRandomizeEntry.tagKey()));
                    populateRandomizedQueenTrades(tempQueenTradesFirstPass, tradeRandomizeEntry);
                }
            }
            else {
                if (entry.wantItems().isEmpty()) continue;
                if (entry.resultItems().isEmpty()) continue;
                for (RawTradeInputEntry rawTradeWantEntry : entry.wantItems().get()) {
                    TradeWantEntry tradeWantEntry = getInputTradeEntry(rawTradeWantEntry);

                    if (tradeWantEntry == null || tradeWantEntry.wantItems().size() == 0) {
                        continue;
                    }

                    List<TradeResultEntry> tradeResultEntry = getOutputTradeEntry(entry.resultItems().get());

                    if (tradeResultEntry.size() == 0) {
                        continue;
                    }

                    if (tradeWantEntry.tagKey.isPresent()) {
                        tempRecipeViewerMainTagTrades.add(tradeWantEntry);
                    }

                    populateMainQueenTrades(tempQueenTradesFirstPass, tradeResultEntry, tradeWantEntry);
                }
            }
        }

        // Do second parse for recipe viewers to set chances and stuff correctly
        tempRecipeViewerRandomizerTrades.removeIf(randomizerTrade -> {
            Set<Item> wantSet = randomizerTrade.getWantItems().stream().map(Holder::value).collect(Collectors.toUnmodifiableSet());
            for (Item item : wantSet) {
                if (!tempQueenTradesFirstPass.containsKey(item)) {
                    return true;
                }

                List<WeightedTradeResult> tradeResults = tempQueenTradesFirstPass.get(item).getFirst().unwrap();
                if (tradeResults.stream().anyMatch(r -> r.items.stream().anyMatch(t -> !wantSet.contains(t)))) {
                    for (Item item2 : wantSet) {
                        tempQueenTradesFirstPass.put(item2, Pair.of(tempQueenTradesFirstPass.get(item2).getFirst(), null));
                    }
                    return true;
                }
            }

            for (Item item : wantSet) {
                Pair<WeightedRandomList<WeightedTradeResult>, TagKey<Item>> pair = tempQueenTradesFirstPass.remove(item);
                tempQueenTrades.put(item, pair.getFirst());
            }

            return false;
        });

        Set<TagKey<Item>> collectedTag = new HashSet<>();
        for (Object2ObjectMap.Entry<Item, Pair<WeightedRandomList<WeightedTradeResult>, TagKey<Item>>> pairEntry : tempQueenTradesFirstPass.object2ObjectEntrySet()) {
            pairEntry.getValue().getFirst().unwrap().forEach(e -> e.setTotalWeight(((WeightedRandomListAccessor)pairEntry.getValue().getFirst()).getTotalWeight()));

            if (pairEntry.getValue().getSecond() == null || !collectedTag.contains(pairEntry.getValue().getSecond())) {
                tempRecipeViewerMainTrades.add(Pair.of(
                        new MainTradeRowInput(Optional.ofNullable(pairEntry.getValue().getSecond()), pairEntry.getKey()),
                        pairEntry.getValue().getFirst()
                ));

                if (pairEntry.getValue().getSecond() != null) {
                    collectedTag.add(pairEntry.getValue().getSecond());
                }
            }

            tempQueenTrades.put(pairEntry.getKey(), pairEntry.getValue().getFirst());
        }

        this.queenTrades = tempQueenTrades;
        this.recipeViewerRandomizerTrades = tempRecipeViewerRandomizerTrades;
        this.recipeViewerMainTrades = tempRecipeViewerMainTrades;
        this.rawTrades.clear();

        this.recipeViewerMainTrades.sort(Comparator.comparing(item -> Registry.ITEM.getKey(item.getFirst().item())));
    }

    private static TradeWantEntry getInputTradeEntry(RawTradeInputEntry rawTradeInputEntry) {
        if (rawTradeInputEntry.entry.startsWith("#")) {
            TagKey<Item> tagKey = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(rawTradeInputEntry.entry.replace("#", "")));
            Optional<HolderSet.Named<Item>> tag = Registry.ITEM.getTag(tagKey);
            if (tag.isEmpty() && rawTradeInputEntry.required) {
                Bumblezone.LOGGER.error("Trade input entry is set to required but " + rawTradeInputEntry.entry + " tag does not exist.");
            }
            else if (tag.isPresent()) {
                return new TradeWantEntry(Optional.of(tagKey), tag.get());
            }
        }
        else {
            Optional<Holder<Item>> item = Registry.ITEM.getHolder(ResourceKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(rawTradeInputEntry.entry)));
            if (item.isEmpty() && rawTradeInputEntry.required) {
                Bumblezone.LOGGER.error("Trade input entry is set to required but " + rawTradeInputEntry.entry + " item does not exist.");
            }
            else if (item.isPresent()) {
                return new TradeWantEntry(Optional.empty(), HolderSet.direct(item.get()));
            }
        }
        return null;
    }

    private List<TradeResultEntry> getOutputTradeEntry(List<RawTradeOutputEntry> rawTradeOutputEntries) {
        List<TradeResultEntry> tradeResultEntries = new ArrayList<>();

        for (RawTradeOutputEntry rawTradeOutputEntry : rawTradeOutputEntries) {
            if (rawTradeOutputEntry.entry.startsWith("#")) {
                TagKey<Item> tagKey = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(rawTradeOutputEntry.entry.replace("#", "")));
                Optional<HolderSet.Named<Item>> tag = Registry.ITEM.getTag(tagKey);
                if (tag.isEmpty() && rawTradeOutputEntry.required) {
                    Bumblezone.LOGGER.error("Trade result entry is set to required but " + rawTradeOutputEntry.entry + " tag does not exist.");
                }
                else tag.ifPresent(holders -> tradeResultEntries.add(new TradeResultEntry(Optional.of(tagKey), holders, rawTradeOutputEntry.count(), rawTradeOutputEntry.xpReward(), rawTradeOutputEntry.weight)));
            }
            else {
                Optional<Holder<Item>> item = Registry.ITEM.getHolder(ResourceKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(rawTradeOutputEntry.entry)));
                if (item.isEmpty() && rawTradeOutputEntry.required) {
                    Bumblezone.LOGGER.error("Trade result entry is set to required but " + rawTradeOutputEntry.entry + " item does not exist.");
                }
                else item.ifPresent(itemHolder -> tradeResultEntries.add(new TradeResultEntry(Optional.empty(), HolderSet.direct(itemHolder), rawTradeOutputEntry.count(), rawTradeOutputEntry.xpReward(), rawTradeOutputEntry.weight)));
            }
        }

        return tradeResultEntries;
    }


    private static void populateMainQueenTrades(Object2ObjectOpenHashMap<Item, Pair<WeightedRandomList<WeightedTradeResult>, TagKey<Item>>> tempQueenTrades, List<TradeResultEntry> tradeResultEntries, TradeWantEntry tradeWantEntry) {
        List<Item> wantItems = tradeWantEntry.wantItems().stream().map(Holder::value).toList();
        for (Item item : wantItems) {
            List<WeightedTradeResult> existingTrades = new ArrayList<>();
            TagKey<Item> key = tradeWantEntry.tagKey.orElse(null);
            boolean needsSorting = false;

            if (tempQueenTrades.containsKey(item)) {
                existingTrades.addAll(tempQueenTrades.get(item).getFirst().unwrap());
                key = null;
                needsSorting = true;
            }

            for (TradeResultEntry tradeResultEntry : tradeResultEntries) {
                List<Item> resultItems = tradeResultEntry.resultItems().stream().map(Holder::value).toList();
                existingTrades.add(new WeightedTradeResult(tradeResultEntry.tagKey, resultItems, tradeResultEntry.count(), tradeResultEntry.xpReward(), tradeResultEntry.weight()));
            }

            if (needsSorting) {
                existingTrades.sort((a, b) -> b.weight - a.weight);
            }

            tempQueenTrades.put(item, Pair.of(WeightedRandomList.create(existingTrades), key));
        }
    }

    private static void populateRandomizedQueenTrades(Object2ObjectOpenHashMap<Item, Pair<WeightedRandomList<WeightedTradeResult>, TagKey<Item>>> tempQueenTrades, TradeWantEntry tradeRandomizeEntry) {
        List<Item> items = tradeRandomizeEntry.wantItems().stream().map(Holder::value).toList();
        for (Item item : items) {
            if (tempQueenTrades.containsKey(item)) {
                List<WeightedTradeResult> existingTrades = new ArrayList<>(tempQueenTrades.get(item).getFirst().unwrap());
                existingTrades.add(new WeightedTradeResult(tradeRandomizeEntry.tagKey(), items, 1, 0 , 1));
                tempQueenTrades.put(item, Pair.of(WeightedRandomList.create(existingTrades), null));
            }
            else {
                tempQueenTrades.put(item, Pair.of(WeightedRandomList.create(new WeightedTradeResult(tradeRandomizeEntry.tagKey(), items, 1, 0 , 1)), tradeRandomizeEntry.tagKey.orElse(null)));
            }
        }
    }

    public static void syncRecipeViewerDataToClient(ServerPlayer player) {
        QueenRandomizerTradesSyncPacket.sendToClient(player, QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerRandomizerTrades);
        QueenMainTradesSyncPacket.sendToClient(player, QueensTradeManager.QUEENS_TRADE_MANAGER.recipeViewerMainTrades);
    }


    @Override
    public ResourceLocation getQuiltId() {
        return QUEENS_TRADE_MANAGER_ID;
    }
}
