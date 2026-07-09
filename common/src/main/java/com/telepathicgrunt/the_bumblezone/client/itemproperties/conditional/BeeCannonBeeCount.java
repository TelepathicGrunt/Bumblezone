package com.telepathicgrunt.the_bumblezone.client.itemproperties.conditional;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.items.BeeCannon;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record BeeCannonBeeCount(int beeCount) implements ConditionalItemModelProperty {
    public static final MapCodec<BeeCannonBeeCount> MAP_CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("bee_count", 0).forGetter(BeeCannonBeeCount::beeCount)
    ).apply(i, BeeCannonBeeCount::new));


    @Override
    public boolean get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        return BeeCannon.getNumberOfBees(itemStack) == beeCount;
    }

    @Override
    public MapCodec<BeeCannonBeeCount> type() {
        return MAP_CODEC;
    }
}

