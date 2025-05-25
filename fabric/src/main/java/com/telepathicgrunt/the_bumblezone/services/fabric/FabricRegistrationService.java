package com.telepathicgrunt.the_bumblezone.services.fabric;

import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfoRegistry;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import com.telepathicgrunt.the_bumblezone.modinit.registry.CustomRegistryLookup;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.fabric.CustomFluidInfoRegistry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.fabric.CustomRegistry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.fabric.CustomResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleSerializer;
import com.telepathicgrunt.the_bumblezone.modules.base.fabric.FabricModuleHolder;
import com.telepathicgrunt.the_bumblezone.modules.fabric.ModuleComponent;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import com.telepathicgrunt.the_bumblezone.packets.networking.fabric.FabricClientPacketHelper;
import com.telepathicgrunt.the_bumblezone.platform.BzEnchantment;
import com.telepathicgrunt.the_bumblezone.platform.ItemExtension;
import com.telepathicgrunt.the_bumblezone.services.EnchantmentService;
import com.telepathicgrunt.the_bumblezone.services.RegistrationService;
import com.telepathicgrunt.the_bumblezone.utils.OptionalBoolean;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;
import java.util.function.Supplier;

public class FabricRegistrationService implements RegistrationService {

    public <T extends CriterionTrigger<?>> T register(T criterionTrigger) {
        return CriteriaTriggers.register(criterionTrigger);
    }

    public <T extends AbstractContainerMenu> MenuType<T> create(BzMenuTypes.MenuCreator<T> creator) {
        return new MenuType<>(creator::create, FeatureFlags.DEFAULT_FLAGS);
    }

    /////

    public <T> ResourcefulRegistry<T> create(Registry<T> registry, String id) {
        return new CustomResourcefulRegistry<>(registry, id);
    }

    public <T, K extends Registry<T>> Pair<Supplier<CustomRegistryLookup<T>>, ResourcefulRegistry<T>> createCustomRegistryInternal(String modId, Class<T> type, ResourceKey<K> key, boolean save, boolean sync, boolean allowModification) {
        FabricRegistryBuilder<T, MappedRegistry<T>> registry = FabricRegistryBuilder.createSimple(type, key.location());
        if (save) registry.attribute(RegistryAttribute.PERSISTED);
        if (sync) registry.attribute(RegistryAttribute.SYNCED);
        if (allowModification) registry.attribute(RegistryAttribute.MODDED);
        MappedRegistry<T> builtRegistry = registry.buildAndRegister();
        CustomRegistry<T> customRegistry = new CustomRegistry<>(builtRegistry);
        return Pair.of(() -> customRegistry, new CustomResourcefulRegistry<>(builtRegistry, modId));
    }

    public FluidInfoRegistry createFluidRegistry(String id) {
        return new CustomFluidInfoRegistry(id);
    }

    /////

    public <T extends Module<T>> ModuleHolder<T> createHolder(ModuleSerializer<T> serializer) {
        return FabricModuleHolder.of(serializer);
    }

    @SuppressWarnings("unchecked")
    public <T extends Module<T>> Optional<T> getModule(Entity entity, ModuleHolder<T> holder) {
        if (holder instanceof FabricModuleHolder<T> fabricHolder) {
            ModuleComponent<T> component = fabricHolder.key().getNullable(entity);
            return Optional.ofNullable(component).map(ModuleComponent::module);
        }
        return Optional.empty();
    }

    /////

    public void registerChannel(ResourceLocation channel) {
        //Do Nothing
    }

    public <T extends Packet<T>> void registerS2CPacket(ResourceLocation channel, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT)) {
            FabricClientPacketHelper.clientOnlyRegister(createChannelLocation(channel, id), handler);
        }
    }

    public <T extends Packet<T>> void registerC2SPacket(ResourceLocation channel, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        ServerPlayNetworking.registerGlobalReceiver(createChannelLocation(channel, id), (server, player, handler1, buf, responseSender) -> {
            T decode = handler.decode(buf);
            server.execute(() -> handler.handle(decode).apply(player, player.level()));
        });
    }

    public <T extends Packet<T>> void sendToServer(ResourceLocation channel, T packet) {
        if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT))
            FabricClientPacketHelper.sendToServerClientOnly(channel, packet);
    }

    public <T extends Packet<T>> void sendToPlayer(ResourceLocation channel, T packet, Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            packet.getHandler().encode(packet, buf);
            ServerPlayNetworking.send(serverPlayer, createChannelLocation(channel, packet.getID()), buf);
        }
    }

    private static ResourceLocation createChannelLocation(ResourceLocation channel, ResourceLocation id) {
        return new ResourceLocation(channel.getNamespace(), channel.getPath() + "/" + id.getNamespace() + "/" + id.getPath());
    }

    /////


}
