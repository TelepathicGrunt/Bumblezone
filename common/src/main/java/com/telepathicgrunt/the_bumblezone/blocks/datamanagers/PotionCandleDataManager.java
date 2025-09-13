package com.telepathicgrunt.the_bumblezone.blocks.datamanagers;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.core.Holder;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffect;

import java.util.Map;
import java.util.Optional;

import static com.telepathicgrunt.the_bumblezone.Bumblezone.GSON;

public class PotionCandleDataManager extends SimpleJsonResourceReloadListener<PotionCandleDataManager.EffectData> {
    private static final FileToIdConverter ASSET_LISTER = FileToIdConverter.json("bz_potion_candle_data");
    public static final PotionCandleDataManager POTION_CANDLE_DATA_MANAGER = new PotionCandleDataManager();

    public record EffectData(Holder<MobEffect> effectHolder, OverrideData overrideData) {
        public static final Codec<EffectData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                MobEffect.CODEC.fieldOf("effect").forGetter(e -> e.effectHolder),
                OverrideData.CODEC.fieldOf("override").forGetter(i -> i.overrideData)
        ).apply(instance, instance.stable(EffectData::new)));

    }

    public record OverrideData(Integer maxLevelCap,
                               Integer minLevelCap,
                               Integer maxBurnDurationCap,
                               Integer minBurnDurationCap,
                               Optional<Integer> baseLingerTime)
    {
        public static final Codec<OverrideData> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
                ExtraCodecs.intRange(0, 100000000).fieldOf("max_level_cap").orElse(100000000).forGetter(i -> i.maxLevelCap),
                ExtraCodecs.intRange(0, 100000000).fieldOf("min_level_cap").orElse(0).forGetter(i -> i.minLevelCap),
                ExtraCodecs.intRange(1, 100000000).fieldOf("max_burn_duration_cap_in_seconds").orElse(100000000).forGetter(i -> i.maxBurnDurationCap),
                ExtraCodecs.intRange(1, 100000000).fieldOf("min_burn_duration_cap_in_seconds").orElse(1).forGetter(i -> i.maxBurnDurationCap),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("linger_base_duration_in_seconds").forGetter(i -> i.baseLingerTime)
        ).apply(instance, instance.stable(OverrideData::new)));
    }

    public final Map<MobEffect, OverrideData> effectToOverrideStats = new Object2ObjectArrayMap<>();

    public PotionCandleDataManager() {
        super(EffectData.CODEC, ASSET_LISTER);
    }

    @Override
    protected void apply(Map<ResourceLocation, EffectData> loader, ResourceManager manager, ProfilerFiller profiler) {
        effectToOverrideStats.clear();
        loader.forEach((fileIdentifier, effectData) -> effectToOverrideStats.put(effectData.effectHolder().value(), effectData.overrideData()));
    }
}
