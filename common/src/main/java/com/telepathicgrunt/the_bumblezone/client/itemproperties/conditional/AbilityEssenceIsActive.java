package com.telepathicgrunt.the_bumblezone.client.itemproperties.conditional;

import com.mojang.serialization.MapCodec;
import com.telepathicgrunt.the_bumblezone.items.datacomponents.AbilityEssenceActivityData;
import com.telepathicgrunt.the_bumblezone.items.essence.AbilityEssenceItem;
import com.telepathicgrunt.the_bumblezone.modinit.BzDataComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record AbilityEssenceIsActive() implements ConditionalItemModelProperty {
    public static final MapCodec<AbilityEssenceIsActive> MAP_CODEC = MapCodec.unit(new AbilityEssenceIsActive());

    @Override
    public boolean get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        if (itemStack.getItem() instanceof AbilityEssenceItem) {
            AbilityEssenceActivityData abilityEssenceActivityData = itemStack.get(BzDataComponents.ABILITY_ESSENCE_ACTIVITY_DATA.get());
            return abilityEssenceActivityData.isActive();
        }
        return false;
    }

    @Override
    public MapCodec<AbilityEssenceIsActive> type() {
        return MAP_CODEC;
    }
}

