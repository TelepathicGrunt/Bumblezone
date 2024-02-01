package com.telepathicgrunt.the_bumblezone.utils;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.jetbrains.annotations.Nullable;

public class UnsafeBulkSectionAccess {
    private final LevelAccessor level;
    private final Long2ObjectMap<LevelChunkSection> acquiredSections = new Long2ObjectOpenHashMap<LevelChunkSection>();
    @Nullable
    private LevelChunkSection lastSection;
    private long lastSectionKey;

    public UnsafeBulkSectionAccess(LevelAccessor levelAccessor) {
        this.level = levelAccessor;
    }

    @Nullable
    public LevelChunkSection getSection(BlockPos blockPos) {
        int i = this.level.getSectionIndex(blockPos.getY());
        if (i < 0 || i >= this.level.getSectionsCount()) {
            return null;
        }
        long l2 = SectionPos.asLong(blockPos);
        if (this.lastSection == null || this.lastSectionKey != l2) {
            this.lastSection = this.acquiredSections.computeIfAbsent(l2, l -> {
                ChunkAccess chunkAccess = this.level.getChunk(SectionPos.blockToSectionCoord(blockPos.getX()), SectionPos.blockToSectionCoord(blockPos.getZ()));
                return chunkAccess.getSection(i);
            });
            this.lastSectionKey = l2;
        }
        return this.lastSection;
    }

    public BlockState getBlockState(BlockPos blockPos) {
        LevelChunkSection levelChunkSection = this.getSection(blockPos);
        if (levelChunkSection == null) {
            return Blocks.AIR.defaultBlockState();
        }
        int i = SectionPos.sectionRelative(blockPos.getX());
        int j = SectionPos.sectionRelative(blockPos.getY());
        int k = SectionPos.sectionRelative(blockPos.getZ());
        return levelChunkSection.getBlockState(i, j, k);
    }

    public void setBlockState(BlockPos blockPos, BlockState state, boolean lockSection) {
        this.getSection(blockPos).setBlockState(
                SectionPos.sectionRelative(blockPos.getX()),
                SectionPos.sectionRelative(blockPos.getY()),
                SectionPos.sectionRelative(blockPos.getZ()),
                state,
                lockSection);
    }
}

