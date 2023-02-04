package com.telepathicgrunt.the_bumblezone.quilt;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.resource.loader.api.reloader.IdentifiableResourceReloader;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@ParametersAreNonnullByDefault
public class QuiltReloadListener implements IdentifiableResourceReloader {

    private final ResourceLocation id;
    private final PreparableReloadListener listener;

    public QuiltReloadListener(ResourceLocation id, PreparableReloadListener listener) {
        this.id = id;
        this.listener = listener;
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, ProfilerFiller profiler, ProfilerFiller profiler2, Executor executor, Executor executor2) {
        return listener.reload(barrier, manager, profiler, profiler2, executor, executor2);
    }

    @Override
    public @NotNull ResourceLocation getQuiltId() {
        return id;
    }
}
