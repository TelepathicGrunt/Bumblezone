package com.telepathicgrunt.the_bumblezone.services;

import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfoRegistry;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import com.telepathicgrunt.the_bumblezone.modinit.registry.CustomRegistryLookup;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleSerializer;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Contract;

import java.util.Optional;
import java.util.function.Supplier;

public interface RegistrationService {

    RegistrationService INSTANCE = GeneralUtils.loadService(RegistrationService.class);

    default <T extends CriterionTrigger<?>> T register(T criterionTrigger) {
        throw new NotImplementedException("RegistrationService create for CriterionTrigger is not implemented");
    }

    default <T extends AbstractContainerMenu> MenuType<T> create(BzMenuTypes.MenuCreator<T> creator) {
        throw new NotImplementedException("RegistrationService create for MenuCreator is not implemented");
    }

    /////

    default <T> ResourcefulRegistry<T> create(Registry<T> registry, String id) {
        throw new NotImplementedException();
    }

    default <T, K extends Registry<T>> Pair<Supplier<CustomRegistryLookup<T>>, ResourcefulRegistry<T>> createCustomRegistryInternal(String modId, Class<T> type, ResourceKey<K> key, boolean save, boolean sync, boolean allowModification) {
        throw new NotImplementedException();
    }

    default FluidInfoRegistry createFluidRegistry(String id) {
        throw new NotImplementedException();
    }

    /////

    default <T extends Module<T>> ModuleHolder<T> createHolder(ModuleSerializer<T> serializer) {
        throw new NotImplementedException("RegistrationService registerSerializer is not implemented on this platform.");
    }

    default <T extends Module<T>> Optional<T> getModule(Entity entity, ModuleHolder<T> holder) {
        throw new NotImplementedException("RegistrationService getModule is not implemented on this platform.");
    }

    /////

    default void registerChannel(ResourceLocation channel) {
        throw new NotImplementedException();
    }

    default <T extends Packet<T>> void registerS2CPacket(ResourceLocation channel, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        throw new NotImplementedException();
    }

    default <T extends Packet<T>> void registerC2SPacket(ResourceLocation channel, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        throw new NotImplementedException();
    }

    default <T extends Packet<T>> void sendToServer(ResourceLocation channel, T packet) {
        throw new NotImplementedException();
    }

    default <T extends Packet<T>> void sendToPlayer(ResourceLocation channel, T packet, Player player) {
        throw new NotImplementedException();
    }

}
