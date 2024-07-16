package com.telepathicgrunt.the_bumblezone.enchantments.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record ParalyzeMarker(
        boolean applyOnUndead,
        int durabilityDrainOnValidTargetHit,
        int durationPerLevel)
{
    public static final Codec<ParalyzeMarker> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.BOOL.fieldOf("apply_on_undead").forGetter(ParalyzeMarker::applyOnUndead),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("durability_drain_on_valid_target_hit").forGetter(ParalyzeMarker::durabilityDrainOnValidTargetHit),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("duration_per_level").forGetter(ParalyzeMarker::durationPerLevel)
    ).apply(instance, ParalyzeMarker::new));
}
