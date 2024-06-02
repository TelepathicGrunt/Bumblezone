package com.telepathicgrunt.the_bumblezone.fluids.base;

import com.teamresourceful.resourcefullib.common.fluid.data.FluidData;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BzBucketItem extends BucketItem implements FluidGetter {

    protected final FluidData info;

    public BzBucketItem(FluidData info, Properties properties) {
        super(info.still().get(), properties);
        info.setBucket(() -> this);
        this.info = info;
    }

    @NotNull
    @Override
    public FlowingFluid getFluid() {
        return info.still().get();
    }

    @Override
    protected void playEmptySound(@Nullable Player player, @NotNull LevelAccessor level, @NotNull BlockPos pos) {
        SoundEvent event = info.properties().sounds().getOrDefault("bucket_empty", SoundEvents.BUCKET_EMPTY);
        level.playSound(player, pos, event, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, GameEvent.FLUID_PLACE, pos);
    }
}
