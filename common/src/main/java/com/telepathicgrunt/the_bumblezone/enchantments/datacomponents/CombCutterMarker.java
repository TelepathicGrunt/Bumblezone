package com.telepathicgrunt.the_bumblezone.enchantments.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record CombCutterMarker(
        int mainTargetBlockBaseSpeedAddition,
        int lesserTargetBlockBaseSpeedAddition)
{
    public static final Codec<CombCutterMarker> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("main_target_block_base_speed_addition").forGetter(CombCutterMarker::mainTargetBlockBaseSpeedAddition),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("lesser_target_block_base_speed_addition").forGetter(CombCutterMarker::lesserTargetBlockBaseSpeedAddition)
    ).apply(instance, CombCutterMarker::new));
}
