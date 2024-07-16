package com.telepathicgrunt.the_bumblezone.items.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record KnowingEssenceStructureData(String inStructures) {
    public static final Codec<KnowingEssenceStructureData> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.STRING.fieldOf("inStructures").forGetter(KnowingEssenceStructureData::inStructures)
    ).apply(instance, KnowingEssenceStructureData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, KnowingEssenceStructureData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, KnowingEssenceStructureData::inStructures,
            KnowingEssenceStructureData::new);

    public KnowingEssenceStructureData() {
        this("");
    }
}
