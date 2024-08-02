package com.telepathicgrunt.the_bumblezone.client.rendering.essence;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.telepathicgrunt.the_bumblezone.client.utils.GeneralUtilsClient;
import com.telepathicgrunt.the_bumblezone.items.essence.KnowingEssence;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor;
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
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4d;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
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
    private static final Long2ObjectOpenHashMap<CachedChunkData> CACHED_CHUNK_DATA = new Long2ObjectOpenHashMap<>();
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

    public static void outlineLootBlocks(PoseStack poseStack, Camera camera, LevelRenderer levelRenderer) {
        Player player = GeneralUtilsClient.getClientPlayer();
        if (KnowingEssence.IsKnowingEssenceActive(player)) {
            Level level = player.level();
            Vec3 cameraPos = camera.getPosition();

            scanChunks(cameraPos, level);

            drawOutlines(poseStack, cameraPos);
        }
        else if (!CACHED_CHUNK_POS.isEmpty()) {
            CACHED_CHUNK_DATA.clear();
            CACHED_CHUNK_POS.clear();
        }
    }

    private static void scanChunks(Vec3 cameraPos, Level level) {
        long currentTime = System.currentTimeMillis();
        if (currentTime > targetScanTime) {
            targetScanTime = currentTime + targetScanTimeIncrement;

            BlockPos worldSpot = BlockPos.containing(cameraPos);
            ChunkPos centerChunkPos = new ChunkPos(worldSpot);
            int currentChunk = 0;
            HashSet<Long> copySet = new HashSet<>(CACHED_CHUNK_POS);
            for (int x = -chunkRadius; x <= chunkRadius; x++) {
                for (int z = -chunkRadius; z <= chunkRadius; z++) {
                    long chunkPosLong = ChunkPos.asLong(x + centerChunkPos.x, z + centerChunkPos.z);
                    copySet.remove(chunkPosLong);

                    currentChunk++;
                    if (currentChunk <= chunkPerBatch * currentScanIncrement || currentChunk > chunkPerBatch * (currentScanIncrement + 1)) {
                        continue;
                    }

                    LevelChunk chunk = level.getChunk(x + centerChunkPos.x, z + centerChunkPos.z);

                    // Reset cached data
                    CACHED_CHUNK_DATA.put(chunkPosLong, new CachedChunkData(new ObjectArrayList<>()));
                    CACHED_CHUNK_POS.add(chunkPosLong);

                    blockEntityScan(chunk, chunkPosLong);
                    blockScan(chunk, chunkPosLong);
                }
            }

            for (Long chunkPos : copySet) {
                CACHED_CHUNK_POS.remove(chunkPos);
                CACHED_CHUNK_DATA.remove(chunkPos.longValue());
            }

            currentScanIncrement++;
            if (currentScanIncrement >= chunkBatches) {
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
                int red = FastColor.ARGB32.red(colorInt);
                int green = FastColor.ARGB32.green(colorInt);
                int blue = FastColor.ARGB32.blue(colorInt);

                CACHED_CHUNK_DATA.get(chunkPosLong).cachedDrawData.add(
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
                int minSectionY = chunk.getMinBuildHeight() + (i * 16);
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
                                        sectionX + (chunk.getPos().x << 4),
                                        minSectionY + sectionY,
                                        sectionZ +  (chunk.getPos().z << 4));

                                int colorInt = block.defaultMapColor().col;
                                int red = FastColor.ARGB32.red(colorInt);
                                int green = FastColor.ARGB32.green(colorInt);
                                int blue = FastColor.ARGB32.blue(colorInt);

                                CACHED_CHUNK_DATA.get(chunkPosLong).cachedDrawData.add(
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

    private static void drawOutlines(PoseStack poseStack, Vec3 cameraPos) {
        if (!CACHED_CHUNK_DATA.isEmpty()) {
            boolean hasEntry = false;
            for (CachedChunkData cachedChunkData : CACHED_CHUNK_DATA.values()) {
                if (!cachedChunkData.cachedDrawData.isEmpty()) {
                    hasEntry = true;
                    break;
                }
            }
            if (!hasEntry) {
                return;
            }

            poseStack.pushPose();

            Tesselator tesselator = Tesselator.getInstance();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.disableDepthTest();
            BufferBuilder bufferbuilder = tesselator.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
            Matrix4f lastPose = poseStack.last().pose();
            CACHED_CHUNK_DATA.values().forEach(cachedChunkData ->
                    cachedChunkData.cachedDrawData.forEach(cachedDrawData ->
                            renderLineBox(
                                bufferbuilder,
                                lastPose,
                                (float) (cachedDrawData.minX - cameraPos.x()),
                                (float) (cachedDrawData.minY - cameraPos.y()),
                                (float) (cachedDrawData.minZ - cameraPos.z()),
                                (float) (cachedDrawData.maxX - cameraPos.x()),
                                (float) (cachedDrawData.maxY - cameraPos.y()),
                                (float) (cachedDrawData.maxZ - cameraPos.z()),
                                cachedDrawData.red,
                                cachedDrawData.green,
                                cachedDrawData.blue
                    )));
            BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
            poseStack.popPose();
            RenderSystem.enableDepthTest();
            RenderType.cutout().clearRenderState();
        }
    }

    private static void renderLineBox(BufferBuilder builder, Matrix4f pose, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, int red, int green, int blue) {
        builder.addVertex(pose, minX, minY, minZ).setColor(red, green, blue, 255).setNormal(1.0F, 0.0F, 0.0F);
        builder.addVertex(pose, maxX, minY, minZ).setColor(red, green, blue, 255).setNormal(1.0F, 0.0F, 0.0F);
        builder.addVertex(pose, minX, minY, minZ).setColor(red, green, blue, 255).setNormal(0.0F, 1.0F, 0.0F);
        builder.addVertex(pose, minX, maxY, minZ).setColor(red, green, blue, 255).setNormal(0.0F, 1.0F, 0.0F);
        builder.addVertex(pose, minX, minY, minZ).setColor(red, green, blue, 255).setNormal(0.0F, 0.0F, 1.0F);
        builder.addVertex(pose, minX, minY, maxZ).setColor(red, green, blue, 255).setNormal(0.0F, 0.0F, 1.0F);
        builder.addVertex(pose, maxX, minY, minZ).setColor(red, green, blue, 255).setNormal(0.0F, 1.0F, 0.0F);
        builder.addVertex(pose, maxX, maxY, minZ).setColor(red, green, blue, 255).setNormal(0.0F, 1.0F, 0.0F);
        builder.addVertex(pose, maxX, maxY, minZ).setColor(red, green, blue, 255).setNormal(-1.0F, 0.0F, 0.0F);
        builder.addVertex(pose, minX, maxY, minZ).setColor(red, green, blue, 255).setNormal(-1.0F, 0.0F, 0.0F);
        builder.addVertex(pose, minX, maxY, minZ).setColor(red, green, blue, 255).setNormal(0.0F, 0.0F, 1.0F);
        builder.addVertex(pose, minX, maxY, maxZ).setColor(red, green, blue, 255).setNormal(0.0F, 0.0F, 1.0F);
        builder.addVertex(pose, minX, maxY, maxZ).setColor(red, green, blue, 255).setNormal(0.0F, -1.0F, 0.0F);
        builder.addVertex(pose, minX, minY, maxZ).setColor(red, green, blue, 255).setNormal(0.0F, -1.0F, 0.0F);
        builder.addVertex(pose, minX, minY, maxZ).setColor(red, green, blue, 255).setNormal(1.0F, 0.0F, 0.0F);
        builder.addVertex(pose, maxX, minY, maxZ).setColor(red, green, blue, 255).setNormal(1.0F, 0.0F, 0.0F);
        builder.addVertex(pose, maxX, minY, maxZ).setColor(red, green, blue, 255).setNormal(0.0F, 0.0F, -1.0F);
        builder.addVertex(pose, maxX, minY, minZ).setColor(red, green, blue, 255).setNormal(0.0F, 0.0F, -1.0F);
        builder.addVertex(pose, minX, maxY, maxZ).setColor(red, green, blue, 255).setNormal(1.0F, 0.0F, 0.0F);
        builder.addVertex(pose, maxX, maxY, maxZ).setColor(red, green, blue, 255).setNormal(1.0F, 0.0F, 0.0F);
        builder.addVertex(pose, maxX, minY, maxZ).setColor(red, green, blue, 255).setNormal(0.0F, 1.0F, 0.0F);
        builder.addVertex(pose, maxX, maxY, maxZ).setColor(red, green, blue, 255).setNormal(0.0F, 1.0F, 0.0F);
        builder.addVertex(pose, maxX, maxY, minZ).setColor(red, green, blue, 255).setNormal(0.0F, 0.0F, 1.0F);
        builder.addVertex(pose, maxX, maxY, maxZ).setColor(red, green, blue, 255).setNormal(0.0F, 0.0F, 1.0F);
    }

    private record CachedChunkData(List<CachedDrawData> cachedDrawData){
    }

    private record CachedDrawData(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int red, int green, int blue) {
    }
}
