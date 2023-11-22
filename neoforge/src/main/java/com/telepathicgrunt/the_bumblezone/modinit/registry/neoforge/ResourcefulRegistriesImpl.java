package com.telepathicgrunt.the_bumblezone.modinit.registry.neoforge;

import com.telepathicgrunt.the_bumblezone.modinit.registry.CustomRegistryLookup;
import com.telepathicgrunt.the_bumblezone.modinit.registry.ResourcefulRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ResourcefulRegistriesImpl {

    private static final List<CustomRegistryInfo<?, ?>> CUSTOM_REGISTRIES = new ArrayList<>();

    public static <T> ResourcefulRegistry<T> create(Registry<T> registry, String id) {
        return new NeoForgeResourcefulRegistry<>(registry, id);
    }

    public static <T, R extends T, K extends Registry<T>> Pair<Supplier<CustomRegistryLookup<T, R>>, ResourcefulRegistry<T>> createCustomRegistryInternal(String modId, ResourceKey<K> key, boolean save, boolean sync, boolean allowModification) {
        CustomRegistryInfo<T, R> info = new CustomRegistryInfo<>(new LateSupplier<>(), key, save, sync, allowModification);
        CUSTOM_REGISTRIES.add(info);
        return Pair.of(info.lookup(), new NeoForgeResourcefulRegistry<>(key, modId));
    }

    public static void onRegisterForgeRegistries(NewRegistryEvent event) {
        CUSTOM_REGISTRIES.forEach(registry -> registry.build(event));
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

    public record CustomRegistryInfo<T, K extends T>(
            LateSupplier<CustomRegistryLookup<T, K>> lookup,
            ResourceKey<? extends Registry<T>> key,
            boolean save,
            boolean sync,
            boolean allowModification
    ) {

        public void build(NewRegistryEvent event) {
            lookup.set(new NeoForgeCustomRegistry<>(event.create(getBuilder())));
        }

        public RegistryBuilder<T> getBuilder() {
            return new RegistryBuilder<>(key)
                    .disableRegistrationCheck()
                    .sync(sync);
        }
    }
}
