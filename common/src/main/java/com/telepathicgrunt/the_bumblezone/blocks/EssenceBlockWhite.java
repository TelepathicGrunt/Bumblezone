package com.telepathicgrunt.the_bumblezone.blocks;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.blockentities.EssenceBlockEntity;
import com.telepathicgrunt.the_bumblezone.modinit.BzItems;
import com.telepathicgrunt.the_bumblezone.screens.ServerEssenceEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;


public class EssenceBlockWhite extends EssenceBlock {
    public EssenceBlockWhite() {
        super(Properties.of().mapColor(MapColor.SNOW));
    }

    @Override
    public ResourceLocation getArenaNbt() {
        return new ResourceLocation(Bumblezone.MODID, "essence/white_arena");
    }

    @Override
    public int getEventTimeFrame() {
        return 200;
    }

    @Override
    public ServerEssenceEvent getServerEssenceEvent() {
        return (ServerEssenceEvent) new ServerEssenceEvent(
                "essence.the_bumblezone.white_essence_event",
                BossEvent.BossBarColor.WHITE,
                BossEvent.BossBarOverlay.NOTCHED_10
        ).setDarkenScreen(true);
    }

    @Override
    public ItemStack getEssenceItemReward() {
        return BzItems.ESSENCE_WHITE.get().getDefaultInstance();
    }

    @Override
    public int getEssenceXpReward() {
        return 3000;
    }

    @Override
    public void performUniqueArenaTick(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, EssenceBlockEntity essenceBlockEntity) {
        essenceBlockEntity.getEventBar().setProgress((float)essenceBlockEntity.getEventTimer() / getEventTimeFrame());
    }
}
