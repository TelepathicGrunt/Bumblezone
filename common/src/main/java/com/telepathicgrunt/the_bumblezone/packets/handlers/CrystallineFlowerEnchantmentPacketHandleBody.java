package com.telepathicgrunt.the_bumblezone.packets.handlers;

import com.telepathicgrunt.the_bumblezone.client.screens.CrystallineFlowerScreen;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.menus.CrystallineFlowerMenu;
import com.telepathicgrunt.the_bumblezone.menus.EnchantmentSkeleton;
import com.telepathicgrunt.the_bumblezone.packets.CrystallineFlowerEnchantmentPacket;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

public class CrystallineFlowerEnchantmentPacketHandleBody {
    public static void handle(CrystallineFlowerEnchantmentPacket message) {
        if (GeneralUtilsClient.getClientPlayer() != null && GeneralUtilsClient.getClientPlayer().containerMenu.containerId == message.containerId()) {
            if (GeneralUtilsClient.getClientPlayer().containerMenu instanceof CrystallineFlowerMenu crystallineFlowerMenu) {
                Map<Identifier, EnchantmentSkeleton> map = new HashMap<>();
                for (EnchantmentSkeleton enchantmentSkeleton : message.enchantmentSkeletons()) {
                    map.put(Identifier.fromNamespaceAndPath(enchantmentSkeleton.namespace, enchantmentSkeleton.path), enchantmentSkeleton);
                }
                crystallineFlowerMenu.selectedEnchantment = null;
                CrystallineFlowerScreen.enchantmentsAvailable = map;

                CrystallineFlowerScreen.SortAndAssignAvailableEnchants();

                crystallineFlowerMenu.selectedEnchantment = message.selectedIdentifier().equals(Identifier.fromNamespaceAndPath("minecraft", "empty")) ? null : message.selectedIdentifier();
                if (!CrystallineFlowerScreen.enchantmentsAvailable.containsKey(crystallineFlowerMenu.selectedEnchantment)) {
                    crystallineFlowerMenu.selectedEnchantment = CrystallineFlowerScreen.enchantmentsAvailable.keySet().stream().findFirst().orElse(null);
                }
            }
        }
    }
}