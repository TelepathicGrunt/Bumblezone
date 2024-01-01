package com.telepathicgrunt.the_bumblezone.modcompat.fabric;

import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;
import java.util.List;

public class SpectrumJetpackCompat implements ModCompat {
    private static Item GEMSTONE_JETPACK;
    private static Item BEDROCK_JETPACK;

    public SpectrumJetpackCompat() {
        GEMSTONE_JETPACK = BuiltInRegistries.ITEM.get(new ResourceLocation("spectrumjetpacks", "gemstone_jetpack"));
        BEDROCK_JETPACK = BuiltInRegistries.ITEM.get(new ResourceLocation("spectrumjetpacks", "bedrock_jetpack"));

       // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.createJetpackPresent = true;
    }

    @Override
    public EnumSet<Type> compatTypes() {
        return EnumSet.of(Type.HEAVY_AIR_RESTRICTED);
    }

    @Override
    public void restrictFlight(Entity entity, double extraGravity) {
        if ((GEMSTONE_JETPACK != null || BEDROCK_JETPACK != null) && entity instanceof Player player) {
            TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {

                List<Tuple<SlotReference, ItemStack>> trinketComponentEquipped = trinketComponent
                        .getEquipped(itemStack -> itemStack.is(GEMSTONE_JETPACK) || itemStack.is(BEDROCK_JETPACK));

                if (trinketComponentEquipped.size() > 0) {
                    for (Tuple<SlotReference, ItemStack> itemStackTuple : trinketComponentEquipped) {
                        if (!player.getCooldowns().isOnCooldown(itemStackTuple.getB().getItem())) {
                            if (player instanceof ServerPlayer serverPlayer) {
                                serverPlayer.displayClientMessage(Component.translatable("system.the_bumblezone.denied_jetpack")
                                        .withStyle(ChatFormatting.ITALIC)
                                        .withStyle(ChatFormatting.RED), true);
                            }
                        }

                        player.getCooldowns().addCooldown(itemStackTuple.getB().getItem(), 40);
                    }
                }
            });
        }
    }
}
