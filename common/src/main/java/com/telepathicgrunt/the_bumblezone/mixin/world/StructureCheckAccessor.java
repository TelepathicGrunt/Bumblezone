package com.telepathicgrunt.the_bumblezone.mixin.world;

import com.mojang.datafixers.DataFixer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.storage.ChunkScanAccess;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.StructureCheck;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructureCheck.class)
public interface StructureCheckAccessor {
    @Accessor("storageAccess")
    ChunkScanAccess bumblezone$getStorageAccess();

    @Accessor("registryAccess")
    RegistryAccess bumblezone$getRegistryAccess();

    @Accessor("structureTemplateManager")
    StructureTemplateManager bumblezone$getStructureTemplateManager();

    @Accessor("dimension")
    ResourceKey<Level> bumblezone$getDimension();

    @Accessor("chunkGenerator")
    ChunkGenerator bumblezone$getChunkGenerator();

    @Accessor("randomState")
    RandomState bumblezone$getRandomState();

    @Accessor("heightAccessor")
    LevelHeightAccessor bumblezone$getHeightAccessor();

    @Accessor("biomeSource")
    BiomeSource bumblezone$getBiomeSource();

    @Accessor("seed")
    long bumblezone$getSeed();

    @Accessor
    DataFixer getFixerUpper();
}
