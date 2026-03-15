package com.telepathicgrunt.the_bumblezone.bossbars;

import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.util.Mth;

import java.util.UUID;

public class ServerEssenceEvent extends ServerBossEvent {
    protected final String translation;
    protected int endEventTimer;

    public ServerEssenceEvent(UUID uuid,  String translation, BossBarColor bossBarColor, BossBarOverlay bossBarOverlay) {
        super(uuid, Component.translatable(translation, "???"), bossBarColor, bossBarOverlay);
        this.endEventTimer = Integer.MAX_VALUE;
        this.translation = translation;
    }

    public int getEndEventTimer() {
        return endEventTimer;
    }

    public void setEndEventTimer(int endEventTimer, float tickrate) {
        this.endEventTimer = endEventTimer;
        this.setName(Component.translatable(this.translation, GeneralUtils.formatTickDurationNoMilliseconds(this.getEndEventTimer(), tickrate)));
    }
}
