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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
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
        long posAsLong = SectionPos.asLong(blockPos);
        if (this.lastSection == null || this.lastSectionKey != posAsLong) {
            this.lastSection = this.acquiredSections.get(posAsLong);
            if (this.lastSection == null) {
                ChunkAccess chunkAccess = this.level.getChunk(SectionPos.blockToSectionCoord(blockPos.getX()), SectionPos.blockToSectionCoord(blockPos.getZ()));
                this.lastSection = chunkAccess.getSection(i);
                this.acquiredSections.put(posAsLong, this.lastSection);
            }
            this.lastSectionKey = posAsLong;
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

    public FluidState getFluidState(BlockPos blockPos) {
        LevelChunkSection levelChunkSection = this.getSection(blockPos);
        if (levelChunkSection == null) {
            return Fluids.EMPTY.defaultFluidState();
        }
        int i = SectionPos.sectionRelative(blockPos.getX());
        int j = SectionPos.sectionRelative(blockPos.getY());
        int k = SectionPos.sectionRelative(blockPos.getZ());
        return levelChunkSection.getFluidState(i, j, k);
    }

    public boolean setBlockState(BlockPos blockPos, BlockState state, boolean lockSection) {
        LevelChunkSection chunkSection = this.getSection(blockPos);
        if (chunkSection == null) {
            return false;
        }

        chunkSection.setBlockState(
                SectionPos.sectionRelative(blockPos.getX()),
                SectionPos.sectionRelative(blockPos.getY()),
                SectionPos.sectionRelative(blockPos.getZ()),
                state,
                lockSection);

        return true;
    }

    public BlockState setBlockStateAndGetOldState(BlockPos blockPos, BlockState state, boolean lockSection) {
        LevelChunkSection chunkSection = this.getSection(blockPos);
        if (chunkSection == null) {
            return null;
        }

        int x = SectionPos.sectionRelative(blockPos.getX());
        int y = SectionPos.sectionRelative(blockPos.getY());
        int z = SectionPos.sectionRelative(blockPos.getZ());

        BlockState oldState = chunkSection.getBlockState(x, y, z);
        chunkSection.setBlockState(x, y, z, state, lockSection);
        return oldState;
    }
}

