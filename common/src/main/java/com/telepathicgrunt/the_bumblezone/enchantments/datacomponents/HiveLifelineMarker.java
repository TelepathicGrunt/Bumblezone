package com.telepathicgrunt.the_bumblezone.enchantments.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record HiveLifelineMarker(
        int armorCooldownTicks,
        int lifelineEntityRange,
        int lifelineEntityRangeIncreasePerAdditionalLevel)
{
    public static final Codec<HiveLifelineMarker> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.INT.fieldOf("armor_cooldown_ticks").forGetter(HiveLifelineMarker::armorCooldownTicks),
            Codec.INT.fieldOf("lifeline_entity_range").forGetter(HiveLifelineMarker::lifelineEntityRange),
            Codec.INT.fieldOf("lifeline_entity_range_increase_per_additional_level").forGetter(HiveLifelineMarker::lifelineEntityRangeIncreasePerAdditionalLevel)
    ).apply(instance, HiveLifelineMarker::new));
}
