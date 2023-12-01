package com.telepathicgrunt.the_bumblezone.modules.fabric;

import com.telepathicgrunt.the_bumblezone.modules.base.Module;
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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ComponentInitializer implements EntityComponentInitializer {

    private static final List<FabricModuleHolder<?>> MODULES = new ArrayList<>();

    @Override
    public void registerEntityComponentFactories(@NotNull EntityComponentFactoryRegistry registry) {
        ModuleRegistry.register(new ModuleRegistrar() {
            @Override
            public <T extends Module<T>> void registerPlayerModule(ModuleHolder<T> holder, Supplier<T> factory, boolean unused) {
                if (holder instanceof FabricModuleHolder<T> fabricHolder) {
                    registry.registerForPlayers(
                            fabricHolder.key(),
                            (player) -> ModuleComponent.create(factory),
                            RespawnCopyStrategy.NEVER_COPY
                    );
                    MODULES.add(fabricHolder);
                }
            }

            @Override
            public <T extends Module<T>> void registerLivingEntityModule(ModuleHolder<T> holder, Supplier<T> factory, boolean unused) {
                if (holder instanceof FabricModuleHolder<T> fabricHolder) {
                    registry.registerFor(
                            LivingEntity.class,
                            fabricHolder.key(),
                            (entity) -> ModuleComponent.create(factory)
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
