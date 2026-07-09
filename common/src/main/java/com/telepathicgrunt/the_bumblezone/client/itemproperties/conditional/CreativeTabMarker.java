package com.telepathicgrunt.the_bumblezone.client.itemproperties.conditional;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record CreativeTabMarker() implements ConditionalItemModelProperty {
    public static final MapCodec<CreativeTabMarker> MAP_CODEC = MapCodec.unit(new CreativeTabMarker());

    private static final CompoundTag marker;
    static {
        marker = new CompoundTag();
        marker.putBoolean("isCreativeTabIcon", true);
    }

    @Override
    public boolean get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        return itemStack.getComponents().has(DataComponents.CUSTOM_DATA) &&
                itemStack.getComponents().get(DataComponents.CUSTOM_DATA).matchedBy(marker);
    }

    @Override
    public MapCodec<CreativeTabMarker> type() {
        return MAP_CODEC;
    }
}

