package com.telepathicgrunt.the_bumblezone.packets.handlers;

import com.telepathicgrunt.the_bumblezone.client.particles.TradeHintParticle;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import com.telepathicgrunt.the_bumblezone.packets.TradeHintParticleSpawnPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public record TradeHintParticleSpawnPacketHandler() {
    public static void handle(TradeHintParticleSpawnPacket message) {
        if (!BzClientConfigs.showBeeQueenSpeechBubble) {
            return;
        }

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }

        Entity queen = level.getEntity(message.queenId());
        if (queen == null) {
            return;
        }

        if (message.wantItem() == Items.AIR) {
            return;
        }

        List<ItemStack> rewardItems = new ArrayList<>(message.rewardItemStacks().size());
        for (ItemStack itemStack : message.rewardItemStacks()) {
            if (!itemStack.isEmpty()) {
                rewardItems.add(itemStack);
            }
        }
        if (rewardItems.isEmpty()) {
            return;
        }

        Minecraft.getInstance().particleEngine.add(new TradeHintParticle(
                Minecraft.getInstance().renderBuffers(),
                level,
                queen,
                message.wantItem(),
                rewardItems));
    }
}
