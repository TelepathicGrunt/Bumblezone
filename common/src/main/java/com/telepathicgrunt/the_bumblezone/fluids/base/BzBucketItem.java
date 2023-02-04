package com.telepathicgrunt.the_bumblezone.fluids.base;

import dev.architectury.injectables.targets.ArchitecturyTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BzBucketItem extends BucketItem implements FluidGetter {

    protected final FluidInfo info;

    public BzBucketItem(FluidInfo info, Properties properties) {
        super("forge".equals(ArchitecturyTarget.getCurrentTarget()) ? Fluids.FLOWING_WATER : info.source(), properties); //This gets replaced in a mixin because of the suppliers.
        info.setBucket(() -> this);
        this.info = info;
    }

    @Override
    protected void playEmptySound(@Nullable Player player, @NotNull LevelAccessor level, @NotNull BlockPos pos) {
        SoundEvent event = info.properties().sounds().getOrDefault("bucket_empty", () -> SoundEvents.BUCKET_EMPTY).get();
        level.playSound(player, pos, event, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, GameEvent.FLUID_PLACE, pos);
    }
}
