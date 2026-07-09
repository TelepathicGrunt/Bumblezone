package com.telepathicgrunt.the_bumblezone.client.itemproperties.conditional;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.items.CrystalCannon;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record CrystalCannonCrystalCount(int crystalCount) implements ConditionalItemModelProperty {
    public static final MapCodec<CrystalCannonCrystalCount> MAP_CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("crystal_count", 0).forGetter(CrystalCannonCrystalCount::crystalCount)
    ).apply(i, CrystalCannonCrystalCount::new));


    @Override
    public boolean get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        return CrystalCannon.getNumberOfCrystals(itemStack) == crystalCount;
    }

    @Override
    public MapCodec<CrystalCannonCrystalCount> type() {
        return MAP_CODEC;
    }
}

