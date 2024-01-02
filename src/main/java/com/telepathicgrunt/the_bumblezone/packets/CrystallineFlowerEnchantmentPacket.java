package com.telepathicgrunt.the_bumblezone.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.screens.CrystallineFlowerMenu;
import com.telepathicgrunt.the_bumblezone.screens.CrystallineFlowerScreen;
import com.telepathicgrunt.the_bumblezone.screens.EnchantmentSkeleton;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtilsClient;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceLocation;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CrystallineFlowerEnchantmentPacket {

    public static ResourceLocation PACKET_ID = new ResourceLocation(Bumblezone.MODID, "crystalline_flower_packet");
    public static Gson gson = new GsonBuilder().create();

    public static void registerPacket() {
        ClientPlayNetworking.registerGlobalReceiver(PACKET_ID,
                (client, handler, buf, responseSender) -> {
                    int containerId = buf.readInt();
                    List<EnchantmentSkeleton> enchantmentSkeletons = new ArrayList<>();
                    int elements = buf.readInt();
                    for (int i = 0; i < elements; i++) {
                        String jsonData = buf.readUtf();
                        enchantmentSkeletons.add(gson.fromJson(jsonData, EnchantmentSkeleton.class));
                    }
                    ResourceLocation selectedEnchantment = buf.readResourceLocation();

                    client.execute(() -> {
                        if(Minecraft.getInstance().player != null && Minecraft.getInstance().player.containerMenu.containerId == containerId){
                            Map<ResourceLocation, EnchantmentSkeleton> map = new HashMap<>();
                            for (EnchantmentSkeleton enchantmentSkeleton : enchantmentSkeletons) {
                                map.put(new ResourceLocation(enchantmentSkeleton.namespace, enchantmentSkeleton.path), enchantmentSkeleton);
                            }
                            CrystallineFlowerScreen.enchantmentsAvailable = map;

                            Language language = Language.getInstance();
                            CrystallineFlowerScreen.enchantmentsAvailableSortedList = map.keySet().stream().sorted((r1, r2) -> {
                                String s1 = language.has("enchantment."+r1.getNamespace()+"."+r1.getPath()) ?
                                        language.getOrDefault("enchantment."+r1.getNamespace()+"."+r1.getPath()) :
                                        r1.getPath();
                                String s2 = language.has("enchantment."+r2.getNamespace()+"."+r2.getPath()) ?
                                        language.getOrDefault("enchantment."+r2.getNamespace()+"."+r2.getPath()) :
                                        r2.getPath();
                                return s1.compareTo(s2);
                            }).collect(Collectors.toList());

                            if (GeneralUtilsClient.getClientPlayer().containerMenu instanceof CrystallineFlowerMenu crystallineFlowerMenu) {
                                crystallineFlowerMenu.selectedEnchantment = selectedEnchantment.equals(new ResourceLocation("minecraft", "empty")) ? null : selectedEnchantment;
                            }
                        }
                    });
                });
    }
}
