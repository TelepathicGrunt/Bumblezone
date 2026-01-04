package com.telepathicgrunt.the_bumblezone.fabric;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class FabricReloadListener implements IdentifiableResourceReloadListener {

    private final Identifier id;
    private final PreparableReloadListener listener;

    public FabricReloadListener(Identifier id, PreparableReloadListener listener) {
        this.id = id;
        this.listener = listener;
    }


    @Override
    public Identifier getFabricId() {
        return id;
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, Executor executor, Executor executor2) {
        return listener.reload(barrier, manager, executor, executor2);
    }
}
