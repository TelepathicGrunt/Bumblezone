package com.telepathicgrunt.the_bumblezone.packets;

import com.telepathicgrunt.the_bumblezone.client.particles.TradeHintParticle;
import com.telepathicgrunt.the_bumblezone.configs.BzClientConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public record TradeHintParticleSpawnPacketHandler() {
    public static void handle(TradeHintParticleSpawnPacket message, Entity queen, Level level) {
        if (!BzClientConfigs.showBeeQueenSpeechBubble) {
            return;
        }

        Item wantItem = BuiltInRegistries.ITEM.get(message.wantItem());
        if (wantItem == Items.AIR) {
            return;
        }

        List<Item> rewardItems = new ArrayList<>(message.rewardItems().size());
        for (ResourceLocation resourceLocation : message.rewardItems()) {
            Item item = BuiltInRegistries.ITEM.get(resourceLocation);
            if (item != Items.AIR) {
                rewardItems.add(item);
            }
        }
        if (rewardItems.isEmpty()) {
            return;
        }

        Minecraft.getInstance().particleEngine.add(new TradeHintParticle(
                Minecraft.getInstance().getItemRenderer(),
                Minecraft.getInstance().renderBuffers(),
                (ClientLevel) level,
                queen,
                wantItem,
                rewardItems));
    }
}
