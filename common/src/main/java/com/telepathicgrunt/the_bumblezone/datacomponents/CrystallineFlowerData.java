package com.telepathicgrunt.the_bumblezone.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

import java.util.UUID;

public record CrystallineFlowerData(int tier, int experience, UUID uuid) {
    public static final Codec<CrystallineFlowerData> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("tier").forGetter(CrystallineFlowerData::tier),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("experience").forGetter(CrystallineFlowerData::experience),
            UUIDUtil.CODEC.fieldOf("uuid").forGetter(CrystallineFlowerData::uuid)
    ).apply(instance, CrystallineFlowerData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, CrystallineFlowerData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, CrystallineFlowerData::tier,
            ByteBufCodecs.VAR_INT, CrystallineFlowerData::experience,
            UUIDUtil.STREAM_CODEC, CrystallineFlowerData::uuid,
            CrystallineFlowerData::new);

    public static UUID DEFAULT_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    public CrystallineFlowerData() {
        this(0, 0, UUID.randomUUID());
    }
}
