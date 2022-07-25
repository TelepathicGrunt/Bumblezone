package com.telepathicgrunt.the_bumblezone.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

// Source: https://github.com/thebrightspark/AsyncLocator/blob/master/src/main/java/brightspark/asynclocator/AsyncLocator.java
public class ThreadExecutor {
    private static ExecutorService LOCATING_EXECUTOR_SERVICE = null;
    private static final AtomicInteger runningSearches = new AtomicInteger(0);

    private static void setupExecutorService() {
        shutdownExecutorService();
        LOCATING_EXECUTOR_SERVICE = Executors.newFixedThreadPool(
                1,
                new ThreadFactory() {
                    private static final AtomicInteger poolNum = new AtomicInteger(1);
                    private final AtomicInteger threadNum = new AtomicInteger(1);
                    private final String namePrefix = "bumblezone_honey_compass_structure_locator-" + poolNum.getAndIncrement() + "-thread-";

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, namePrefix + threadNum.getAndIncrement());
                    }
                }
        );
    }

    private static void shutdownExecutorService() {
        if (LOCATING_EXECUTOR_SERVICE != null) {
            LOCATING_EXECUTOR_SERVICE.shutdown();
        }
    }

    public static void handleServerAboutToStartEvent() {
        setupExecutorService();
    }

    public static void handleServerStoppingEvent() {
        shutdownExecutorService();
    }

    public static boolean isRunningASearch() {
        return runningSearches.get() > 0;
    }

    public static LocateTask<BlockPos> locate(
            ServerLevel level,
            TagKey<Structure> structureTag,
            BlockPos pos,
            int searchRadius,
            boolean skipKnownStructures)
    {
        CompletableFuture<BlockPos> completableFuture = new CompletableFuture<>();
        Future<?> future = LOCATING_EXECUTOR_SERVICE.submit(
                () ->  {
                    runningSearches.getAndIncrement();
                    doLocateLevel(completableFuture, level, structureTag, pos, searchRadius, skipKnownStructures);
                }
        );
        return new LocateTask<>(level.getServer(), completableFuture, future);
    }

    private static void doLocateLevel(
            CompletableFuture<BlockPos> completableFuture,
            ServerLevel level,
            TagKey<Structure> structureTag,
            BlockPos pos,
            int searchRadius,
            boolean skipExistingChunks)
    {
        BlockPos foundPos = level.findNearestMapStructure(structureTag, pos, searchRadius, skipExistingChunks);
        completableFuture.complete(foundPos);
        runningSearches.getAndDecrement();
    }

    public record LocateTask<T>(MinecraftServer server, CompletableFuture<T> completableFuture, Future<?> taskFuture) {
        /**
         * Helper function that calls {@link CompletableFuture#thenAccept(Consumer)} with the given action.
         * Bear in mind that the action will be executed from the task's thread. If you intend to change any game data,
         * it's strongly advised you use {@link #thenOnServerThread(Consumer)} instead so that it's queued and executed
         * on the main server thread instead.
         */
        public LocateTask<T> then(Consumer<T> action) {
            completableFuture.thenAccept(action);
            return this;
        }

        /**
         * Helper function that calls {@link CompletableFuture#thenAccept(Consumer)} with the given action on the server
         * thread.
         */
        public LocateTask<T> thenOnServerThread(Consumer<T> action) {
            completableFuture.thenAccept(pos -> server.submit(() -> action.accept(pos)));
            return this;
        }

        /**
         * Helper function that cancels both completableFuture and taskFuture.
         */
        public void cancel() {
            taskFuture.cancel(true);
            completableFuture.cancel(false);
        }
    }
}
