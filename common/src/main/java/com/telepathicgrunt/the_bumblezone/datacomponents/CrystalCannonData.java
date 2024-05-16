package com.telepathicgrunt.the_bumblezone.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record CrystalCannonData(int crystalStored) {
    public static final Codec<CrystalCannonData> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("crystalStored").forGetter(CrystalCannonData::crystalStored)
    ).apply(instance, CrystalCannonData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CrystalCannonData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, CrystalCannonData::crystalStored,
            CrystalCannonData::new);

    public CrystalCannonData() {
        this(0);
    }
}
