package com.telepathicgrunt.bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.mixin.world.StructureAccessor;
import com.telepathicgrunt.bumblezone.utils.GeneralUtils;
import com.telepathicgrunt.bumblezone.world.features.configs.NbtFeatureConfig;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class NbtFeature extends Feature<NbtFeatureConfig> {

    public NbtFeature(Codec<NbtFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos position, NbtFeatureConfig config) {
        Identifier nbtRL = GeneralUtils.getRandomEntry(config.nbtResourcelocationsAndWeights, random);

        StructureManager structureManager = world.toServerWorld().getStructureManager();
        Structure template = structureManager.getStructure(nbtRL);
        if(template == null){
            Bumblezone.LOGGER.error("Identifier to the specified nbt file was not found! : {}", nbtRL);
            return false;
        }
        BlockRotation rotation = BlockRotation.random(random);

        // For proper offsetting the feature so it rotate properly around position parameter.
        BlockPos halfLengths = new BlockPos(
                template.getSize().getX() / 2,
                template.getSize().getY() / 2,
                template.getSize().getZ() / 2);

        BlockPos.Mutable mutable = new BlockPos.Mutable().set(position);

        // offset the feature's position
        position = position.up(config.structureYOffset);

        StructurePlacementData placementsettings = (new StructurePlacementData()).setRotation(rotation).setPosition(halfLengths).setIgnoreEntities(false);
        Optional<StructureProcessorList> processor = world.toServerWorld().getServer().getRegistryManager().get(Registry.PROCESSOR_LIST_WORLDGEN).getOrEmpty(config.processor);
        processor.orElse(StructureProcessorLists.EMPTY).getList().forEach(placementsettings::addProcessor); // add all processors
        template.place(world, mutable.set(position).move(-halfLengths.getX(), 0, -halfLengths.getZ()), placementsettings, random);

        // Post-processors
        // For all processors that are sensitive to neighboring blocks such as vines.
        // Post processors will place the blocks themselves so we will not do anything with the return of Structure.process
        placementsettings.clearProcessors();
        Optional<StructureProcessorList> postProcessor = world.toServerWorld().getServer().getRegistryManager().get(Registry.PROCESSOR_LIST_WORLDGEN).getOrEmpty(config.postProcessor);
        postProcessor.orElse(StructureProcessorLists.EMPTY).getList().forEach(placementsettings::addProcessor); // add all post processors
        List<Structure.StructureBlockInfo> list = placementsettings.getRandomBlockInfos(((StructureAccessor)template).bz_getBlocks(), mutable).getAll();
        Structure.process(world, mutable, mutable, placementsettings, list);

        return true;
    }
}
