package com.telepathicgrunt.the_bumblezone.client.rendering.essence;

import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.items.essence.KnowingEssence;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector4d;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KnowingEssenceLootBlockOutlining {
    private static final double DRAW_RADIUS = 0.45D;
    private static final double MIN_CORNER = 0.5D - DRAW_RADIUS;
    private static final double MAX_CORNER = 0.5D + DRAW_RADIUS;
    private static final Vector4d VECTOR_4D_MIN = new Vector4d(MIN_CORNER, MIN_CORNER, MIN_CORNER, 1.0D);
    private static final Vector4d VECTOR_4D_MAX = new Vector4d(MAX_CORNER, MAX_CORNER, MAX_CORNER, 1.0D);
    private static final LinkedHashSet<Long> CACHED_CHUNK_POS = new LinkedHashSet<>();
    private static final Long2ObjectOpenHashMap<List<CachedDrawData>> CACHED_CHUNK_DATA = new Long2ObjectOpenHashMap<>();
    private static final Set<Block> CACHED_TARGET_BLOCKS = new ObjectOpenHashSet<>();
    private static final Set<Block> CACHED_NONTARGET_BLOCKS = new ObjectOpenHashSet<>();
    private static final int chunkRadius = 4;
    private static final int chunksToCheck = ((chunkRadius * 2) + 1) * ((chunkRadius * 2) + 1);
    private static final int chunkPerBatch = 3;
    private static final int chunkBatches = chunksToCheck / chunkPerBatch;
    private static final long scanAllChunkTimeframe = 1000;
    private static final long targetScanTimeIncrement = scanAllChunkTimeframe / chunkBatches;
    private static long targetScanTime = 0;
    private static long currentScanIncrement = 0;

    public static void resetTargetBlockCache() {
        CACHED_TARGET_BLOCKS.clear();
        CACHED_NONTARGET_BLOCKS.clear();
    }

    public static Long2ObjectOpenHashMap<List<CachedDrawData>> gatherLootBlocks(ClientLevel level, Camera camera, Frustum frustum) {
        Player player = GeneralUtilsClient.getClientPlayer();
        if (KnowingEssence.IsKnowingEssenceActive(player)) {
            scanChunks(camera.position(), level, frustum);
        }
        else if (!CACHED_CHUNK_POS.isEmpty()) {
            CACHED_CHUNK_DATA.clear();
            CACHED_CHUNK_POS.clear();
        }

        // Needed for ensuring threadsafety and not being passed by ref
        Long2ObjectOpenHashMap<List<CachedDrawData>> deepCopyChunkData = new Long2ObjectOpenHashMap<>(CACHED_CHUNK_DATA.size());
        for (Map.Entry<Long, List<CachedDrawData>> entry : CACHED_CHUNK_DATA.long2ObjectEntrySet()) {
            deepCopyChunkData.put(entry.getKey().longValue(), new ObjectArrayList<>(entry.getValue()));
        }
        return deepCopyChunkData;
    }

    public static void drawLootBlockOutlines(Long2ObjectOpenHashMap<List<CachedDrawData>> cachedChunkDatas, Vec3 cameraPos) {
        drawOutlines(cachedChunkDatas, cameraPos);
    }

    private static void scanChunks(Vec3 cameraPos, Level level, Frustum frustum) {
        long currentTime = System.currentTimeMillis();
        if (currentTime > targetScanTime) {
            targetScanTime = currentTime + targetScanTimeIncrement;

            BlockPos worldSpot = BlockPos.containing(cameraPos);
            ChunkPos centerChunkPos = ChunkPos.containing(worldSpot);
            int currentChunk = 0;
            HashSet<Long> copySet = new HashSet<>(CACHED_CHUNK_POS);
            for (int x = -chunkRadius; x <= chunkRadius; x++) {
                for (int z = -chunkRadius; z <= chunkRadius; z++) {
                    long chunkPosLong = ChunkPos.pack(x + centerChunkPos.x(), z + centerChunkPos.z());
                    copySet.remove(chunkPosLong);

                    currentChunk++;
                    if (currentChunk <= chunkPerBatch * currentScanIncrement || currentChunk > chunkPerBatch * (currentScanIncrement + 1)) {
                        continue;
                    }

                    int xInBlocks = SectionPos.sectionToBlockCoord(x + centerChunkPos.x());
                    int zInBlocks = SectionPos.sectionToBlockCoord(z + centerChunkPos.z());

                    if (!frustum.isVisible(new AABB(xInBlocks, level.getMinY(), zInBlocks, xInBlocks + 16, level.getMaxY(), zInBlocks + 16))) {
                        continue;
                    }

                    LevelChunk chunk = level.getChunk(x + centerChunkPos.x(), z + centerChunkPos.z());

                    // Reset cached data
                    CACHED_CHUNK_DATA.put(chunkPosLong, new ObjectArrayList<>());
                    CACHED_CHUNK_POS.add(chunkPosLong);

                    blockEntityScan(chunk, chunkPosLong);
                    blockScan(chunk, chunkPosLong);
                }
            }

            // remove cache positions outside radius scan.
            for (Long chunkPos : copySet) {
                CACHED_CHUNK_POS.remove(chunkPos);
                CACHED_CHUNK_DATA.remove(chunkPos.longValue());
            }

            currentScanIncrement++;
            if (currentScanIncrement > chunkBatches) {
                currentScanIncrement = 0;
            }
        }
    }

    private static void blockEntityScan(LevelChunk chunk, long chunkPosLong) {
        for (Map.Entry<BlockPos, BlockEntity> blockEntityEntry : chunk.getBlockEntities().entrySet()) {
            BlockEntity blockEntity = blockEntityEntry.getValue();
            BlockState blockState = blockEntity.getBlockState();
            Block block = blockState.getBlock();
            if (CACHED_NONTARGET_BLOCKS.contains(block)) {
                continue;
            }

            if (CACHED_TARGET_BLOCKS.contains(block) ||
                ((blockState.is(BzTags.KNOWING_BLOCK_ENTITY_FORCED_HIGHLIGHTING) ||
                blockEntity instanceof RandomizableContainerBlockEntity ||
                blockEntity instanceof BrushableBlockEntity ||
                blockEntity instanceof EnderChestBlockEntity ||
                blockEntity instanceof DecoratedPotBlockEntity ||
                block instanceof EnderChestBlock)
                && !blockState.is(BzTags.KNOWING_BLOCK_ENTITY_PREVENT_HIGHLIGHTING)))
            {
                CACHED_TARGET_BLOCKS.add(block);

                BlockPos lootBlockPos = blockEntityEntry.getKey();

                int colorInt = block.defaultMapColor().col;
                int red = ARGB.red(colorInt);
                int green = ARGB.green(colorInt);
                int blue = ARGB.blue(colorInt);

                CACHED_CHUNK_DATA.get(chunkPosLong).add(
                    new CachedDrawData(
                        VECTOR_4D_MIN.x() + lootBlockPos.getX(),
                        VECTOR_4D_MIN.y() + lootBlockPos.getY(),
                        VECTOR_4D_MIN.z() + lootBlockPos.getZ(),
                        VECTOR_4D_MAX.x() + lootBlockPos.getX(),
                        VECTOR_4D_MAX.y() + lootBlockPos.getY(),
                        VECTOR_4D_MAX.z() + lootBlockPos.getZ(),
                        red,
                        green,
                        blue));
            }
            else {
                CACHED_NONTARGET_BLOCKS.add(block);
            }
        }
    }

    private static void blockScan(LevelChunk chunk, long chunkPosLong) {
        for (int i = 0; i < chunk.getSectionsCount(); i++) {
            LevelChunkSection levelChunkSection = chunk.getSection(i);
            if (!levelChunkSection.hasOnlyAir() &&
                levelChunkSection.maybeHas(blockState ->
                    !CACHED_NONTARGET_BLOCKS.contains(blockState.getBlock()) &&
                    (CACHED_TARGET_BLOCKS.contains(blockState.getBlock()) ||
                        blockState.is(BzTags.KNOWING_BLOCK_FORCED_HIGHLIGHTING) ||
                        (blockState.getBlock() instanceof InfestedBlock && !blockState.is(BzTags.KNOWING_BLOCK_PREVENT_HIGHLIGHTING)))))
            {
                int minSectionY = chunk.getMinY() + (i * 16);
                for (int sectionX = 0; sectionX < 16; sectionX++) {
                    for (int sectionZ = 0; sectionZ < 16; sectionZ++) {
                        for (int sectionY = 0; sectionY < 16; sectionY++) {

                            BlockState blockState = levelChunkSection.getBlockState(sectionX, sectionY, sectionZ);
                            Block block = blockState.getBlock();

                            if (CACHED_NONTARGET_BLOCKS.contains(block)) {
                                continue;
                            }

                            if (CACHED_TARGET_BLOCKS.contains(blockState.getBlock()) ||
                                blockState.is(BzTags.KNOWING_BLOCK_FORCED_HIGHLIGHTING) ||
                                (blockState.getBlock() instanceof InfestedBlock && !blockState.is(BzTags.KNOWING_BLOCK_PREVENT_HIGHLIGHTING)))
                            {
                                CACHED_TARGET_BLOCKS.add(block);

                                BlockPos lootBlockPos = new BlockPos(
                                        sectionX + (chunk.getPos().x() << 4),
                                        minSectionY + sectionY,
                                        sectionZ +  (chunk.getPos().z() << 4));

                                int colorInt = block.defaultMapColor().col;
                                int red = ARGB.red(colorInt);
                                int green = ARGB.green(colorInt);
                                int blue = ARGB.blue(colorInt);

                                CACHED_CHUNK_DATA.get(chunkPosLong).add(
                                        new CachedDrawData(
                                                VECTOR_4D_MIN.x() + lootBlockPos.getX(),
                                                VECTOR_4D_MIN.y() + lootBlockPos.getY(),
                                                VECTOR_4D_MIN.z() + lootBlockPos.getZ(),
                                                VECTOR_4D_MAX.x() + lootBlockPos.getX(),
                                                VECTOR_4D_MAX.y() + lootBlockPos.getY(),
                                                VECTOR_4D_MAX.z() + lootBlockPos.getZ(),
                                                red,
                                                green,
                                                blue));
                            }
                            else {
                                CACHED_NONTARGET_BLOCKS.add(block);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void drawOutlines(Long2ObjectOpenHashMap<List<CachedDrawData>> cachedChunkDatas, Vec3 cameraPos) {
        if (!cachedChunkDatas.isEmpty()) {
            boolean hasEntry = false;
            for (List<CachedDrawData> cachedChunkData : cachedChunkDatas.values()) {
                if (!cachedChunkData.isEmpty()) {
                    hasEntry = true;
                    break;
                }
            }
            if (!hasEntry) {
                return;
            }

            cachedChunkDatas.values().forEach(cachedChunkData ->
                    cachedChunkData.forEach(cachedDrawData -> {

//                            if (!frustum.pointInFrustum(sectionX + chunk.getPos().getMinBlockX(), sectionY, sectionZ + chunk.getPos().getMinBlockZ())) {
//                                continue;
//                            }

                            int distTillMaxFade = 200;
                            int alpha = GeneralUtils.capBetween((int) (Math.pow(1 - (cameraPos.distanceToSqr(cachedDrawData.minX, cachedDrawData.minY, cachedDrawData.minZ) / (distTillMaxFade * distTillMaxFade)), 15) * 255), 10, 255);
                            renderLineBox(
                                cachedDrawData.minX,
                                cachedDrawData.minY,
                                cachedDrawData.minZ,
                                cachedDrawData.maxX,
                                cachedDrawData.maxY,
                                cachedDrawData.maxZ,
                                ARGB.color(alpha, cachedDrawData.red, cachedDrawData.green, cachedDrawData.blue));
                        }
                    ));
        }
    }

    private static void renderLineBox(
            double minX,
            double minY,
            double minZ,
            double maxX,
            double maxY,
            double maxZ,
            int color)
    {
        Gizmos.cuboid(
            new AABB(minX, minY, minZ, maxX, maxY, maxZ),
            GizmoStyle.stroke(color)
        ).setAlwaysOnTop();
    }

    public record CachedDrawData(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int red, int green, int blue) { }
}
