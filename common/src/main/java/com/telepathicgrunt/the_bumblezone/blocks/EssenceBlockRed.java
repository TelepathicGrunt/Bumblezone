package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.screens.ServerEssenceEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;


public class EssenceBlockRed extends EssenceBlock {
    public EssenceBlockRed() {
        super(Properties.of().mapColor(MapColor.COLOR_RED));
    }

    @Override
    public ResourceLocation getArenaNbt() {
        return null;
    }

    @Override
    public int getEventTimeFrame() {
        return 12000;
    }

    @Override
    public ServerEssenceEvent getServerEssenceEvent() {
        return (ServerEssenceEvent) new ServerEssenceEvent(
                "essence.the_bumblezone.red_essence_event",
                BossEvent.BossBarColor.RED,
                BossEvent.BossBarOverlay.NOTCHED_20
        ).setDarkenScreen(true);
    }

    @Override
    public void performUniqueArenaTick(Level level, BlockPos blockPos, BlockState blockState, EssenceBlockEntity essenceBlockEntity) {
        essenceBlockEntity.getEventBar().setProgress((float)essenceBlockEntity.getEventTimer() / getEventTimeFrame());
    }
}
