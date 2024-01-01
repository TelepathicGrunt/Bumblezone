package com.telepathicgrunt.the_bumblezone.modcompat.fabricbase;

import com.telepathicgrunt.the_bumblezone.modcompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modcompat.ModCompat;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;
import java.util.List;

public class SpectrumJetpackCompat implements ModCompat {
    private static TagKey<Item> SPECTRUM_JETPACKS;

    public SpectrumJetpackCompat() {
        SPECTRUM_JETPACKS = TagKey.create(Registries.ITEM, new ResourceLocation("spectrumjetpacks", "jetpacks"));

        // Keep at end so it is only set to true if no exceptions was thrown during setup
        ModChecker.createJetpackPresent = true;
    }

    @Override
    public EnumSet<Type> compatTypes() {
        return EnumSet.of(Type.HEAVY_AIR_RESTRICTED);
    }

    @Override
    public void restrictFlight(Entity entity, double extraGravity) {
        if (SPECTRUM_JETPACKS != null && entity instanceof Player player) {
            TrinketsApi.getTrinketComponent(player).ifPresent(trinketComponent -> {

                List<Tuple<SlotReference, ItemStack>> trinketComponentEquipped = trinketComponent
                        .getEquipped(itemStack -> itemStack.is(SPECTRUM_JETPACKS));

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
