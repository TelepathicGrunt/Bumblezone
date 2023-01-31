package com.telepathicgrunt.the_bumblezone.modules.fabric;

import com.telepathicgrunt.the_bumblezone.modules.base.Module;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleFactory;
import com.telepathicgrunt.the_bumblezone.modules.base.ModuleHolder;
import com.telepathicgrunt.the_bumblezone.modules.base.fabric.FabricModuleHolder;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistrar;
import com.telepathicgrunt.the_bumblezone.modules.registry.ModuleRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.PlayerCopyCallback;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ComponentInitializer implements EntityComponentInitializer {

    private static final List<FabricModuleHolder<?>> MODULES = new ArrayList<>();

    @Override
    public void registerEntityComponentFactories(@NotNull EntityComponentFactoryRegistry registry) {
        ModuleRegistry.register(new ModuleRegistrar() {
            @Override
            public <T extends Module<T>> void registerPlayerModule(ModuleHolder<T> holder, ModuleFactory<Player, T> factory) {
                if (holder instanceof FabricModuleHolder<T> fabricHolder) {
                    registry.registerForPlayers(
                            fabricHolder.key(),
                            (player) -> ModuleComponent.create(player, factory),
                            RespawnCopyStrategy.NEVER_COPY
                    );
                    MODULES.add(fabricHolder);
                }
            }

            @Override
            public <T extends Module<T>> void registerLivingEntityModule(ModuleHolder<T> holder, ModuleFactory<LivingEntity, T> factory) {
                if (holder instanceof FabricModuleHolder<T> fabricHolder) {
                    registry.registerFor(
                            LivingEntity.class,
                            fabricHolder.key(),
                            (entity) -> ModuleComponent.create(entity, factory)
                    );
                    MODULES.add(fabricHolder);
                }
            }
        });

        PlayerCopyCallback.EVENT.register((oldPlayer, newPlayer, loseless) -> {
            for (FabricModuleHolder<?> holder : MODULES) {
                copy(oldPlayer, newPlayer, holder, loseless);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static <T extends Module<T>> void copy(ServerPlayer oldPlayer, ServerPlayer newPlayer, FabricModuleHolder<T> serializer, boolean isPersistent) {
        ModuleComponent<T> oldModule = serializer.key().getNullable(oldPlayer);
        ModuleComponent<T> newModule = serializer.key().getNullable(newPlayer);
        if (oldModule != null && newModule != null) {
            oldModule.module().serializer().onPlayerCopy(oldModule.module(), newModule.module(), newPlayer, isPersistent);
        }
    }


}
