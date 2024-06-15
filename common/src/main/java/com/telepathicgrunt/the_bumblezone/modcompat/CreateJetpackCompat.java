package com.telepathicgrunt.the_bumblezone.modcompat;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

public class CreateJetpackCompat implements ModCompat {
    private static Item JETPACK;

    public CreateJetpackCompat() {
        JETPACK = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create_jetpack", "jetpack"));

       // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.createJetpackPresent = true;
    }

    @Override
    public EnumSet<Type> compatTypes() {
        return EnumSet.of(Type.HEAVY_AIR_RESTRICTED);
    }

    @Override
    public void restrictFlight(Entity entity, double extraGravity) {
        if (JETPACK != null && entity instanceof Player player) {
            ItemStack jetpack = player.getItemBySlot(EquipmentSlot.CHEST);
            if (jetpack.is(JETPACK)) {
                if (!player.getCooldowns().isOnCooldown(jetpack.getItem())) {
                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.displayClientMessage(Component.translatable("system.the_bumblezone.denied_jetpack")
                                .withStyle(ChatFormatting.ITALIC)
                                .withStyle(ChatFormatting.RED), true);
                    }
                }

                player.getCooldowns().addCooldown(jetpack.getItem(), 40);
            }
        }
    }
}
