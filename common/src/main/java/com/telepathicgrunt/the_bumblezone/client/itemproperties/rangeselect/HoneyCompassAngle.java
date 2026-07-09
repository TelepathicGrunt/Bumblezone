package com.telepathicgrunt.the_bumblezone.client.itemproperties.rangeselect;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class HoneyCompassAngle implements RangeSelectItemModelProperty {
    public static final MapCodec<HoneyCompassAngle> MAP_CODEC = HoneyCompassAngleState.MAP_CODEC.xmap(HoneyCompassAngle::new, c -> c.state);
    private final HoneyCompassAngleState state;

    public HoneyCompassAngle(boolean wobble) {
        this(new HoneyCompassAngleState(wobble));
    }

    private HoneyCompassAngle(HoneyCompassAngleState state) {
        this.state = state;
    }

    @Override
    public float get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        return this.state.get(itemStack, level, owner, seed);
    }

    @Override
    public MapCodec<HoneyCompassAngle> type() {
        return MAP_CODEC;
    }
}
