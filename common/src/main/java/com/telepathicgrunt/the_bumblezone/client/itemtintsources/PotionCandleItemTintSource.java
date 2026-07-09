package com.telepathicgrunt.the_bumblezone.client.itemtintsources;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.PotionCandleBlockEntity;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public record PotionCandleItemTintSource(int defaultColor) implements ItemTintSource {
    public static final MapCodec<PotionCandleItemTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
        i -> i.group(ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(PotionCandleItemTintSource::defaultColor)
    ).apply(i, PotionCandleItemTintSource::new));

    public PotionCandleItemTintSource() {
        this(-1);
    }

    @Override
    public int calculate(final ItemStack itemStack, @Nullable final ClientLevel level, @Nullable final LivingEntity owner) {
        TypedEntityData<@NotNull BlockEntityType<?>> customData = itemStack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (customData != null) {
            CompoundTag tag = customData.copyTagWithoutId();
            return ARGB.opaque(tag.getInt(PotionCandleBlockEntity.COLOR_TAG).orElse(PotionCandleBlockEntity.DEFAULT_COLOR));
        }
        return ARGB.opaque(PotionCandleBlockEntity.DEFAULT_COLOR);
    }

    @Override
    public MapCodec<PotionCandleItemTintSource> type() {
        return MAP_CODEC;
    }
}
