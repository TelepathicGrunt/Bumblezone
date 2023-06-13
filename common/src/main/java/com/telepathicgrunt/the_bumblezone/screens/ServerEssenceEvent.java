package com.telepathicgrunt.the_bumblezone.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.util.StringUtil;

public class ServerEssenceEvent extends ServerBossEvent {
    protected int endEventTimer;
    protected String translation;

    public ServerEssenceEvent(String translation, BossBarColor bossBarColor, BossBarOverlay bossBarOverlay) {
        super(Component.translatable(translation, "???"), bossBarColor, bossBarOverlay);
        this.endEventTimer = Integer.MAX_VALUE;
        this.translation = translation;
    }

    public int getEndEventTimer() {
        return endEventTimer;
    }

    public void setEndEventTimer(int endEventTimer) {
        this.endEventTimer = endEventTimer;
        this.setName(Component.translatable(this.translation, StringUtil.formatTickDuration(this.getEndEventTimer())));
    }
}
