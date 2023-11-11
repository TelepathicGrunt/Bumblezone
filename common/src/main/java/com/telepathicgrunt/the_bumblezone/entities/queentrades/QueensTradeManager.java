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
import com.telepathicgrunt.the_bumblezone.events.lifecycle.TagsUpdatedEvent;
import com.telepathicgrunt.the_bumblezone.mixin.util.WeightedRandomListAccessor;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.MainTradeRowInput;
import com.telepathicgrunt.the_bumblezone.modcompat.recipecategories.RandomizeTradeRowInput;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.item.Item;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class QueensTradeManager extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().setLenient().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();
    public static final QueensTradeManager QUEENS_TRADE_MANAGER = new QueensTradeManager();

    private final List<TradeCollection> rawTrades = new ArrayList<>();
    public Object2ObjectOpenHashMap<Item, WeightedRandomList<WeightedTradeResult>> queenTrades = new Object2ObjectOpenHashMap<>();
    public Object2ObjectOpenHashMap<Item, Object2ObjectOpenHashMap<SpecialDaysEntry, WeightedRandomList<WeightedTradeResult>>> specialDayQueenTrades = new Object2ObjectOpenHashMap<>();
    public List<RandomizeTradeRowInput> recipeViewerRandomizerTrades = new ArrayList<>();
    public List<Pair<MainTradeRowInput, WeightedRandomList<WeightedTradeResult>>> recipeViewerMainTrades = new ArrayList<>();

    public record TradeCollection(
        Optional<SpecialDaysEntry> specialDaysEntry,
        Optional<List<RawTradeInputEntry>> randomizerItems,
        Optional<List<RawTradeInputEntry>> wantItems,
        Optional<List<RawTradeOutputEntry>> resultItems,
        boolean randomizerTrade)
    {
        public static final Codec<TradeCollection> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                SpecialDaysEntry.CODEC.optionalFieldOf("special_days").forGetter(e -> e.specialDaysEntry),
                RawTradeInputEntry.CODEC.listOf().optionalFieldOf("randomizes").forGetter(e -> e.randomizerItems),
                RawTradeInputEntry.CODEC.listOf().optionalFieldOf("wants").forGetter(e -> e.wantItems),
                RawTradeOutputEntry.CODEC.listOf().optionalFieldOf("possible_rewards").forGetter(e -> e.resultItems),
                Codec.BOOL.fieldOf("is_color_randomizer_trade").orElse(false).forGetter(e -> e.randomizerTrade)
        ).apply(instance, instance.stable(TradeCollection::new)));
    }

    public record SpecialDaysEntry(Optional<String> dateAlgorithm, Optional<Integer> month, Optional<Integer> day, int daysLong, String specialMessage, ChatFormatting textColor) {
        public static final Codec<SpecialDaysEntry> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                Codec.STRING.optionalFieldOf("date_algorithm").forGetter(e -> e.dateAlgorithm),
                Codec.intRange(1, 12).optionalFieldOf("start_month").forGetter(e -> e.month),
                Codec.intRange(1, 31).optionalFieldOf("start_day").forGetter(e -> e.day),
                Codec.intRange(1, 364).fieldOf("lasts_days_long").forGetter(e -> e.daysLong),
                Codec.STRING.fieldOf("special_message").forGetter(e -> e.specialMessage),
                ChatFormatting.CODEC.fieldOf("text_color").forGetter(e -> e.textColor)
        ).apply(instance, instance.stable(SpecialDaysEntry::new)));
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
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("xp_reward").forGetter(e -> e.xpReward),
                ExtraCodecs.POSITIVE_INT.fieldOf("weight").forGetter(e -> e.weight)
        ).apply(instance, instance.stable(RawTradeOutputEntry::new)));
    }

    public record TradeWantEntry(Optional<TagKey<Item>> tagKey, HolderSet<Item> wantItems) {
        public static final Codec<TradeWantEntry> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                TagKey.codec(Registries.ITEM).optionalFieldOf("tagkey").forGetter(e -> e.tagKey),
                RegistryCodecs.homogeneousList(Registries.ITEM, BuiltInRegistries.ITEM.byNameCodec()).fieldOf("wantItems").forGetter(e -> e.wantItems)
        ).apply(instance, instance.stable(TradeWantEntry::new)));
    }

    public record TradeResultEntry(Optional<TagKey<Item>> tagKey, HolderSet<Item> resultItems, int count, int xpReward, int weight) {
        public static final Codec<TradeResultEntry> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                TagKey.codec(Registries.ITEM).optionalFieldOf("tagkey").forGetter(e -> e.tagKey),
                RegistryCodecs.homogeneousList(Registries.ITEM, BuiltInRegistries.ITEM.byNameCodec()).fieldOf("wantItems").forGetter(e -> e.resultItems),
                Codec.intRange(1, 64).fieldOf("count").forGetter(e -> e.count),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("xp_reward").forGetter(e -> e.xpReward),
                ExtraCodecs.POSITIVE_INT.fieldOf("weight").forGetter(e -> e.weight)
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
    public void resolveQueenTrades(final TagsUpdatedEvent event) {
        if (rawTrades.isEmpty()) {
            return;
        }

        List<RandomizeTradeRowInput> tempRecipeViewerRandomizerTrades = new ArrayList<>();
        List<TradeWantEntry> tempRecipeViewerMainTagTrades = new ArrayList<>();
        List<Pair<MainTradeRowInput, WeightedRandomList<WeightedTradeResult>>> tempRecipeViewerMainTrades = new ArrayList<>();

        // TagKey is to store if the key item was from a Tag or not.
        Object2ObjectOpenHashMap<Item, Pair<WeightedRandomList<WeightedTradeResult>, TagKey<Item>>> tempQueenTradesFirstPass = new Object2ObjectOpenHashMap<>();
        Object2ObjectOpenHashMap<Item, WeightedRandomList<WeightedTradeResult>> tempQueenTrades = new Object2ObjectOpenHashMap<>();
        Object2ObjectOpenHashMap<Item, Object2ObjectOpenHashMap<SpecialDaysEntry, WeightedRandomList<WeightedTradeResult>>> tempSpecialDaysQueenTrades = new Object2ObjectOpenHashMap<>();

        for (TradeCollection entry : rawTrades) {
            if (entry.specialDaysEntry().isPresent()) {
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

                    populateSpecialDaysQueenTrades(tempSpecialDaysQueenTrades, entry.specialDaysEntry().get(), tradeResultEntry, tradeWantEntry);
                }
                continue;
            }


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
                if (tradeResults.stream().anyMatch(r -> r.getItems().stream().anyMatch(t -> !wantSet.contains(t)))) {
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
        this.specialDayQueenTrades = tempSpecialDaysQueenTrades;
        this.recipeViewerRandomizerTrades = tempRecipeViewerRandomizerTrades;
        this.recipeViewerMainTrades = tempRecipeViewerMainTrades;
        this.rawTrades.clear();

        this.recipeViewerMainTrades.sort(Comparator.comparing(item -> BuiltInRegistries.ITEM.getKey(item.getFirst().item())));
    }

    private static TradeWantEntry getInputTradeEntry(RawTradeInputEntry rawTradeInputEntry) {
        if (rawTradeInputEntry.entry.startsWith("#")) {
            TagKey<Item> tagKey = TagKey.create(Registries.ITEM, new ResourceLocation(rawTradeInputEntry.entry.replace("#", "")));
            Optional<HolderSet.Named<Item>> tag = BuiltInRegistries.ITEM.getTag(tagKey);
            if (tag.isEmpty() && rawTradeInputEntry.required) {
                Bumblezone.LOGGER.error("Trade input entry is set to required but " + rawTradeInputEntry.entry + " tag does not exist.");
            }
            else if (tag.isPresent()) {
                return new TradeWantEntry(Optional.of(tagKey), tag.get());
            }
        }
        else {
            Optional<Holder.Reference<Item>> item = BuiltInRegistries.ITEM.getHolder(ResourceKey.create(Registries.ITEM, new ResourceLocation(rawTradeInputEntry.entry)));
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
                TagKey<Item> tagKey = TagKey.create(Registries.ITEM, new ResourceLocation(rawTradeOutputEntry.entry.replace("#", "")));
                Optional<HolderSet.Named<Item>> tag = BuiltInRegistries.ITEM.getTag(tagKey);
                if (tag.isEmpty() && rawTradeOutputEntry.required) {
                    Bumblezone.LOGGER.error("Trade result entry is set to required but " + rawTradeOutputEntry.entry + " tag does not exist.");
                }
                else tag.ifPresent(holders -> tradeResultEntries.add(new TradeResultEntry(Optional.of(tagKey), holders, rawTradeOutputEntry.count(), rawTradeOutputEntry.xpReward(), rawTradeOutputEntry.weight)));
            }
            else {
                Optional<Holder.Reference<Item>> item = BuiltInRegistries.ITEM.getHolder(ResourceKey.create(Registries.ITEM, new ResourceLocation(rawTradeOutputEntry.entry)));
                if (item.isEmpty() && rawTradeOutputEntry.required) {
                    Bumblezone.LOGGER.error("Trade result entry is set to required but " + rawTradeOutputEntry.entry + " item does not exist.");
                }
                else item.ifPresent(itemHolder -> tradeResultEntries.add(new TradeResultEntry(Optional.empty(), HolderSet.direct(itemHolder), rawTradeOutputEntry.count(), rawTradeOutputEntry.xpReward(), rawTradeOutputEntry.weight)));
            }
        }

        return tradeResultEntries;
    }

    private static void populateSpecialDaysQueenTrades(Object2ObjectOpenHashMap<Item, Object2ObjectOpenHashMap<SpecialDaysEntry, WeightedRandomList<WeightedTradeResult>>> tempSpecialDaysQueenTrades, SpecialDaysEntry specialDaysEntry, List<TradeResultEntry> tradeResultEntries, TradeWantEntry tradeWantEntry) {
        List<Item> wantItems = new ArrayList<>(tradeWantEntry.wantItems().stream().map(Holder::value).toList());
        for (Item item : wantItems) {
            List<WeightedTradeResult> resultItems = new ArrayList<>();
            for (TradeResultEntry tradeResultEntry : tradeResultEntries) {
                resultItems.add(new WeightedTradeResult(tradeResultEntry.tagKey(), Optional.of(tradeResultEntry.resultItems().stream().map(Holder::value).toList()), tradeResultEntry.count(), tradeResultEntry.xpReward(), tradeResultEntry.weight()));
            }

            Object2ObjectOpenHashMap<SpecialDaysEntry, WeightedRandomList<WeightedTradeResult>> temp = new Object2ObjectOpenHashMap<>();
            if (tempSpecialDaysQueenTrades.containsKey(item) && tempSpecialDaysQueenTrades.get(item).containsKey(specialDaysEntry)) {
                WeightedRandomList<WeightedTradeResult> existingTrades = tempSpecialDaysQueenTrades.get(item).get(specialDaysEntry);
                resultItems.addAll(existingTrades.unwrap());
            }

            temp.put(specialDaysEntry, WeightedRandomList.create(resultItems));
            tempSpecialDaysQueenTrades.put(item, temp);
        }
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
                existingTrades.add(new WeightedTradeResult(tradeResultEntry.tagKey, Optional.of(resultItems), tradeResultEntry.count(), tradeResultEntry.xpReward(), tradeResultEntry.weight()));
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
                existingTrades.add(new WeightedTradeResult(tradeRandomizeEntry.tagKey(), Optional.of(items), 1, 0 , 1));
                tempQueenTrades.put(item, Pair.of(WeightedRandomList.create(existingTrades), null));
            }
            else {
                tempQueenTrades.put(item, Pair.of(WeightedRandomList.create(new WeightedTradeResult(tradeRandomizeEntry.tagKey(), Optional.of(items), 1, 0 , 1)), tradeRandomizeEntry.tagKey.orElse(null)));
            }
        }
    }

    /////////////////////////////////////////////////////
    // Special day trades

    public Optional<Pair<SpecialDaysEntry, WeightedRandomList<WeightedTradeResult>>> getSpecialDayItems(Item item) {
        Object2ObjectOpenHashMap<SpecialDaysEntry, WeightedRandomList<WeightedTradeResult>> specialDayData = specialDayQueenTrades.get(item);
        if (specialDayData != null) {
            for (Map.Entry<SpecialDaysEntry, WeightedRandomList<WeightedTradeResult>> specialDaysEntry : specialDayData.entrySet()) {
                LocalDate dateNow = LocalDate.now();
                LocalDate specialDayThisYear = getDateForYear(dateNow.getYear(), specialDaysEntry.getKey()).minusDays(1);
                if (specialDayThisYear.isBefore(dateNow) && specialDayThisYear.plusDays(specialDaysEntry.getKey().daysLong() + 1).isAfter(dateNow)) {
                    return Optional.of(Pair.of(specialDaysEntry.getKey(), specialDaysEntry.getValue()));
                }

                LocalDate specialDayLastYear = getDateForYear(dateNow.getYear() - 1, specialDaysEntry.getKey()).minusDays(1);
                if (specialDayLastYear.isBefore(dateNow) && specialDayLastYear.plusDays(specialDaysEntry.getKey().daysLong() + 1).isAfter(dateNow)) {
                    return Optional.of(Pair.of(specialDaysEntry.getKey(), specialDaysEntry.getValue()));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<List<Item>> getSpecialDayItem() {
        List<Item> specialDayItems = new ArrayList<>();
        for (Map.Entry<Item, Object2ObjectOpenHashMap<SpecialDaysEntry, WeightedRandomList<WeightedTradeResult>>> entry : specialDayQueenTrades.entrySet()) {
            for (Map.Entry<SpecialDaysEntry, WeightedRandomList<WeightedTradeResult>> specialDaysEntry : entry.getValue().entrySet()) {
                LocalDate dateNow = LocalDate.now();
                LocalDate specialDayThisYear = getDateForYear(dateNow.getYear(), specialDaysEntry.getKey()).minusDays(1);
                if (specialDayThisYear.isBefore(dateNow) && specialDayThisYear.plusDays(specialDaysEntry.getKey().daysLong() + 1).isAfter(dateNow)) {
                    specialDayItems.add(entry.getKey());
                    break;
                }

                LocalDate specialDayLastYear = getDateForYear(dateNow.getYear() - 1, specialDaysEntry.getKey()).minusDays(1);
                if (specialDayLastYear.isBefore(dateNow) && specialDayLastYear.plusDays(specialDaysEntry.getKey().daysLong() + 1).isAfter(dateNow)) {
                    specialDayItems.add(entry.getKey());
                    break;
                }
            }
        }

        if (specialDayItems.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(specialDayItems);
    }

    private static LocalDate getDateForYear(int year, SpecialDaysEntry specialDaysEntry) {
        if (specialDaysEntry.month().isPresent() && specialDaysEntry.day().isPresent()) {
            return LocalDate.of(year, specialDaysEntry.month().get(), specialDaysEntry.day().get());
        }
        else if (specialDaysEntry.dateAlgorithm().isPresent()) {
            switch (specialDaysEntry.dateAlgorithm().get()){
                case "thanksgiving":
                    return thanksgivingEveAlgo(year);
                case "easter":
                    return gaussEaster(year).minusDays(1);
            }
        }

        return LocalDate.EPOCH.plusYears(1);
    }

    //Source: http://www.java2s.com/Tutorials/Java/Data_Type_How_to/Date/Get_thanks_giving_date_for_any_year.htm
    private static LocalDate thanksgivingEveAlgo(int year) {
        return Year.of(year).atMonth(Month.NOVEMBER).atDay(1).with(TemporalAdjusters.lastInMonth(DayOfWeek.WEDNESDAY));
    }

    //Source: https://www.geeksforgeeks.org/how-to-calculate-the-easter-date-for-a-given-year-using-gauss-algorithm/#
    private static LocalDate gaussEaster(int year) {
        float A, B, C, P, Q, M, N, D, E;

        // All calculations done
        // on the basis of
        // Gauss Easter Algorithm
        A = year % 19;
        B = year % 4;
        C = year % 7;
        P = (float)Math.floor(year / 100);
        Q = (float)Math.floor((13 + 8 * P) / 25);
        M = (int)(15 - Q + P - Math.floor(P / 4)) % 30;
        N = (int)(4 + P - Math.floor(P / 4)) % 7;
        D = (19 * A + M) % 30;
        E = (2 * B + 4 * C + 6 * D + N) % 7;
        int days = (int)(22 + D + E);

        try {
            // A corner case,
            // when D is 29
            if ((D == 29) && (E == 6)) {
                return LocalDate.of(year, 4, 19);
            }
            // Another corner case,
            // when D is 28
            else if ((D == 28) && (E == 6)) {
                return LocalDate.of(year, 4, 18);
            }
            else {

                // If days > 31, move to April
                // April = 4th Month
                if (days > 31) {
                    return LocalDate.of(year, 4, days - 31);
                }
                // Otherwise, stay on March
                // March = 3rd Month
                else {
                    return LocalDate.of(year, 3, days);
                }
            }
        }
        catch (Exception ignored) {}

        return LocalDate.EPOCH.plusYears(1);
    }
}
