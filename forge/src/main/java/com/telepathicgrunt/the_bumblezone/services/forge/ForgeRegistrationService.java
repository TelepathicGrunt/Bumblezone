package com.telepathicgrunt.the_bumblezone.services.forge;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.fluids.base.FluidInfoRegistry;
import com.telepathicgrunt.the_bumblezone.modinit.BzMenuTypes;
import com.telepathicgrunt.the_bumblezone.modinit.registry.CustomRegistryLookup;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.forge.ForgeCustomRegistry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.forge.ForgeFluidInfoRegistry;
import com.telepathicgrunt.the_bumblezone.modinit.registry.forge.ForgeResourcefulRegistry;
import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleSerializer;
import com.telepathicgrunt.the_bumblezone.modules.base.forge.ForgeModuleHolder;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.Packet;
import com.telepathicgrunt.the_bumblezone.packets.networking.base.PacketHandler;
import com.telepathicgrunt.the_bumblezone.platform.ModInfo;
import com.telepathicgrunt.the_bumblezone.services.PlatformService;
import com.telepathicgrunt.the_bumblezone.services.RegistrationService;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ForgeRegistrationService implements RegistrationService {

    public <T extends AbstractContainerMenu> MenuType<T> create(BzMenuTypes.MenuCreator<T> creator) {
        return new MenuType<>(creator::create, FeatureFlags.DEFAULT_FLAGS);
    }

    public <T extends CriterionTrigger<?>> T register(T criterionTrigger) {
        return CriteriaTriggers.register(criterionTrigger);
    }

    ////

    private static final List<CustomRegistryInfo<?>> CUSTOM_REGISTRIES = new ArrayList<>();

    public <T> ResourcefulRegistry<T> create(Registry<T> registry, String id) {
        return new ForgeResourcefulRegistry<>(registry.key(), id);
    }

    public <T, K extends Registry<T>> Pair<Supplier<CustomRegistryLookup<T>>, ResourcefulRegistry<T>> createCustomRegistryInternal(String modId, Class<T> type, ResourceKey<K> key, boolean save, boolean sync, boolean allowModification) {
        CustomRegistryInfo<T> info = new CustomRegistryInfo<>(new LateSupplier<>(), key, save, sync, allowModification);
        CUSTOM_REGISTRIES.add(info);
        return Pair.of(info.lookup(), new ForgeResourcefulRegistry<>(key, modId));
    }

    public static void onRegisterForgeRegistries(NewRegistryEvent event) {
        CUSTOM_REGISTRIES.forEach(registry -> registry.build(event));
    }

    public FluidInfoRegistry createFluidRegistry(String id) {
        return new ForgeFluidInfoRegistry(id);
    }

    public static class LateSupplier<T> implements Supplier<T> {
        private T value;
        private boolean initialized = false;

        public void set(T value) {
            this.value = value;
            this.initialized = true;
        }

        @Override
        public T get() {
            if (!initialized) {
                throw new IllegalStateException("LateSupplier not initialized");
            }
            return value;
        }
    }

    public record CustomRegistryInfo<T>(
            LateSupplier<CustomRegistryLookup<T>> lookup,
            ResourceKey<? extends Registry<T>> key,
            boolean save,
            boolean sync,
            boolean allowModification
    ) {

        public void build(NewRegistryEvent event) {
            lookup.set(new ForgeCustomRegistry<>(event.create(getBuilder())));
        }

        public RegistryBuilder<T> getBuilder() {
            RegistryBuilder<T> builder = new RegistryBuilder<>();
            builder.setName(key.location());
            if (!save) builder.disableSaving();
            if (!sync) builder.disableSync();
            if (allowModification) builder.allowModification();
            return builder;
        }
    }

    /////

    public <T extends Module<T>> ModuleHolder<T> createHolder(ModuleSerializer<T> serializer) {
        return ForgeModuleHolder.of(serializer);
    }

    public <T extends Module<T>> Optional<T> getModule(Entity entity, ModuleHolder<T> holder) {
        if (holder instanceof ForgeModuleHolder<T> forgeHolder) {
            return entity.getCapability(forgeHolder.capability()).resolve();
        }
        return Optional.empty();
    }

    /////

    public final Map<ResourceLocation, Channel> CHANNELS = new HashMap<>();

    public void registerChannel(ResourceLocation name) {
        ModInfo info = PlatformService.INSTANCE.getModInfo(Bumblezone.MODID, true);
        String protocolVersion = info.version();
        Channel channel = new Channel(0, NetworkRegistry.newSimpleChannel(name, () -> protocolVersion, protocolVersion::equals, protocolVersion::equals));
        CHANNELS.put(name, channel);
    }

    public <T extends Packet<T>> void registerS2CPacket(ResourceLocation name, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        Channel channel = CHANNELS.get(name);
        if (channel == null) {
            throw new IllegalStateException("Channel " + name + " not registered");
        }
        channel.channel.registerMessage(++channel.packets, packetClass, handler::encode, handler::decode, (msg, ctx) -> {
            NetworkEvent.Context context = ctx.get();
            Player sender = context.getSender();

            context.enqueueWork(() -> {
                Player player = null;
                if (sender == null) {
                    player = GeneralUtilsClient.getClientPlayer();
                }

                if (player != null) {
                    handler.handle(msg).apply(player, player.level());
                }
            });

            context.setPacketHandled(true);
        });
    }

    public <T extends Packet<T>> void registerC2SPacket(ResourceLocation name, ResourceLocation id, PacketHandler<T> handler, Class<T> packetClass) {
        Channel channel = CHANNELS.get(name);
        if (channel == null) {
            throw new IllegalStateException("Channel " + name + " not registered");
        }
        channel.channel.registerMessage(++channel.packets, packetClass, handler::encode, handler::decode, (msg, ctx) -> {
            NetworkEvent.Context context = ctx.get();
            Player player = context.getSender();

            context.enqueueWork(() -> {
                if (player != null) {
                    handler.handle(msg).apply(player, player.level());
                }
            });

            context.setPacketHandled(true);
        });
    }

    public <T extends Packet<T>> void sendToServer(ResourceLocation name, T packet) {
        Channel channel = CHANNELS.get(name);
        if (channel == null) {
            throw new IllegalStateException("Channel " + name + " not registered");
        }
        channel.channel.sendToServer(packet);
    }

    public <T extends Packet<T>> void sendToPlayer(ResourceLocation name, T packet, Player player) {
        Channel channel = CHANNELS.get(name);
        if (channel == null) {
            throw new IllegalStateException("Channel " + name + " not registered");
        }
        if (player instanceof ServerPlayer serverPlayer) {
            channel.channel.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
        }
    }

    private static final class Channel {
        private int packets;
        private final SimpleChannel channel;

        private Channel(int packets, SimpleChannel channel) {
            this.packets = packets;
            this.channel = channel;
        }
    }
    
    
}
