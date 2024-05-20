package com.telepathicgrunt.the_bumblezone.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record RagingEssenceCurrentTargetData(List<UUID> currentTargets) {
    public static final Codec<RagingEssenceCurrentTargetData> DIRECT_CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            UUIDUtil.CODEC.listOf().fieldOf("currentTargets").forGetter(RagingEssenceCurrentTargetData::currentTargets)
    ).apply(instance, RagingEssenceCurrentTargetData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, RagingEssenceCurrentTargetData> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.<ByteBuf, UUID>list().apply(UUIDUtil.STREAM_CODEC), RagingEssenceCurrentTargetData::currentTargets,
            RagingEssenceCurrentTargetData::new);

    public RagingEssenceCurrentTargetData() {
        this(new ArrayList<>());
    }
}
