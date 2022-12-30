package com.telepathicgrunt.the_bumblezone.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.PileOfPollen;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeehemothEntity;
import com.telepathicgrunt.the_bumblezone.mixin.blocks.FallingBlockEntityAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.screens.CrystallineFlowerScreen;
import com.telepathicgrunt.the_bumblezone.screens.EnchantmentSkeleton;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CrystallineFlowerEnchantmentPacket {

    public static ResourceLocation PACKET_ID = new ResourceLocation(Bumblezone.MODID, "crystalline_flower_packet");
    public static Gson gson = new GsonBuilder().create();

    public static void registerPacket() {
        ClientPlayNetworking.registerGlobalReceiver(PACKET_ID,
                (client, handler, buf, responseSender) -> {
                    List<EnchantmentSkeleton> enchantmentSkeletons = new ArrayList<>();
                    int elements = buf.readInt();
                    for (int i = 0; i < elements; i++) {
                        String jsonData = buf.readUtf();
                        enchantmentSkeletons.add(gson.fromJson(jsonData, EnchantmentSkeleton.class));
                    }

                    client.execute(() -> {
                        CrystallineFlowerScreen.enchantmentsAvailable = enchantmentSkeletons;
                    });
                });
    }
}
