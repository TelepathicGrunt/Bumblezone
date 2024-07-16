package com.telepathicgrunt.the_bumblezone.enchantments.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record PoisonMarker(
        double poisonLevelPerEnchantLevel,
        int bonusDurationAmount,
        int enchantLevelIntervalForBonusDuration,
        double poisonLevelPerEnchantLevelStingerSpear,
        int bonusDurationAmountStingerSpear,
        int enchantLevelIntervalForBonusDurationStingerSpear)
{
    public static final Codec<PoisonMarker> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.doubleRange(0, 256).fieldOf("poison_level_per_enchant_level").forGetter(PoisonMarker::poisonLevelPerEnchantLevel),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("bonus_duration_amount").forGetter(PoisonMarker::bonusDurationAmount),
            Codec.intRange(0, 256).fieldOf("enchant_level_interval_for_bonus_duration").forGetter(PoisonMarker::enchantLevelIntervalForBonusDuration),
            Codec.doubleRange(0, 256).fieldOf("poison_level_per_enchant_level_stinger_spear").forGetter(PoisonMarker::poisonLevelPerEnchantLevelStingerSpear),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("bonus_duration_amount_stinger_spear").forGetter(PoisonMarker::bonusDurationAmountStingerSpear),
            Codec.intRange(0, 256).fieldOf("enchant_level_interval_for_bonus_duration_stinger_spear").forGetter(PoisonMarker::enchantLevelIntervalForBonusDurationStingerSpear)
    ).apply(instance, PoisonMarker::new));
}
