package com.telepathicgrunt.the_bumblezone.modules.forge;

import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleFactory;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import com.telepathicgrunt.the_bumblezone.modules.base.forge.ForgeModuleHolder;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistrar;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.List;

public class ForgeModuleInitalizer {

    private static final List<ModuleRegistryValue<?, LivingEntity>> LIVING_ENTITY_MODULES = new ArrayList<>();
    private static final List<ModuleRegistryValue<?, Player>> PLAYER_ENTITY_MODULES = new ArrayList<>();

    public static void init() {
        ModuleRegistry.register(new ModuleRegistrar() {
            @Override
            public <T extends com.telepathicgrunt.the_bumblezone.modules.base.Module<T>> void registerPlayerModule(ModuleHolder<T> holder, ModuleFactory<Player, T> factory) {
                if (holder instanceof ForgeModuleHolder<T> forgeHolder) {
                    PLAYER_ENTITY_MODULES.add(new ModuleRegistryValue<>(forgeHolder, factory));
                }
            }

            @Override
            public <T extends com.telepathicgrunt.the_bumblezone.modules.base.Module<T>> void registerLivingEntityModule(ModuleHolder<T> holder, ModuleFactory<LivingEntity, T> factory) {
                if (holder instanceof ForgeModuleHolder<T> forgeHolder) {
                    LIVING_ENTITY_MODULES.add(new ModuleRegistryValue<>(forgeHolder, factory));
                }
            }
        });

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ForgeModuleInitalizer::onRegisterCapabilties);
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, ForgeModuleInitalizer::onEntityAttach);
        MinecraftForge.EVENT_BUS.addListener(ForgeModuleInitalizer::onPlayerClone);
    }

    public static void onRegisterCapabilties(RegisterCapabilitiesEvent event) {
        for (var value : LIVING_ENTITY_MODULES) {
            HackyCapabilityManager.register(value.holder().serializer().moduleClass());
        }
        for (var value : PLAYER_ENTITY_MODULES) {
            HackyCapabilityManager.register(value.holder().serializer().moduleClass());
        }
    }

    public static void onEntityAttach(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof ServerPlayer player) {
            for (var value : PLAYER_ENTITY_MODULES) {
                attachHolderOnEntity(event, player, value);
            }
        }
        if (event.getObject() instanceof LivingEntity livingEntity) {
            for (var value : LIVING_ENTITY_MODULES) {
                attachHolderOnEntity(event, livingEntity, value);
            }
        }
    }

    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getOriginal() instanceof ServerPlayer oldPlayer) {
            oldPlayer.reviveCaps();
            for (var value : PLAYER_ENTITY_MODULES) {
                copyData(value.holder.capability(), oldPlayer, player, !event.isWasDeath());
            }
            oldPlayer.invalidateCaps();
        }
    }

    private static <T extends Module<T>> void copyData(Capability<T> capability, ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean loseless) {
        oldPlayer.getCapability(capability).ifPresent(oldModule -> {
            newPlayer.getCapability(capability).ifPresent(newModule -> {
                newModule.serializer().onPlayerCopy(oldModule, newModule, newPlayer, loseless);
            });
        });
    }

    private static <T extends Module<T>, I> void attachHolderOnEntity(final AttachCapabilitiesEvent<Entity> event, I input, ModuleRegistryValue<T, I> value) {
        T o = value.factory().create(input);
        event.addCapability(value.holder().serializer().id(), new ModuleCapabilityProvider<>(value.holder(), o));
    }

    private record ModuleRegistryValue<T extends Module<T>, I>(ForgeModuleHolder<T> holder, ModuleFactory<I, T> factory) {

    }
}
