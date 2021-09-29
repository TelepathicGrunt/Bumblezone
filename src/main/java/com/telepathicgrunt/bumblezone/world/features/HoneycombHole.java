package com.telepathicgrunt.bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.bumblezone.Bumblezone;
import com.telepathicgrunt.bumblezone.mixin.world.StructureAccessor;
import com.telepathicgrunt.bumblezone.utils.GeneralUtils;
import com.telepathicgrunt.bumblezone.world.features.configs.NbtFeatureConfig;
import net.minecraft.block.Block;
import net.minecraft.data.client.model.VariantSettings;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.List;
import java.util.Optional;
import java.util.Random;


public class HoneycombHole extends Feature<NbtFeatureConfig> {

    public HoneycombHole(Codec<NbtFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean generate(FeatureContext<NbtFeatureConfig> context) {
        Identifier nbtRL = GeneralUtils.getRandomEntry(context.getConfig().nbtResourcelocationsAndWeights, context.getRandom());

        StructureManager structureManager = context.getWorld().toServerWorld().getStructureManager();
        Structure template = structureManager.getStructure(nbtRL).orElseThrow(() -> {
            String errorMsg = "Identifier to the specified nbt file was not found! : " + nbtRL;
            Bumblezone.LOGGER.error(errorMsg);
            return new RuntimeException(errorMsg);
        });

        // For proper offsetting the feature.
        BlockPos halfLengths = new BlockPos(
                template.getSize().getX() / 2,
                template.getSize().getY() / 2,
                template.getSize().getZ() / 2);

        BlockPos.Mutable mutable = new BlockPos.Mutable().set(context.getOrigin());

        // offset the feature's position
        BlockPos position = context.getOrigin().up(context.getConfig().structureYOffset);

        StructurePlacementData structurePlacementData = (new StructurePlacementData()).setRotation(BlockRotation.NONE).setPosition(halfLengths).setIgnoreEntities(false);
        Optional<StructureProcessorList> processor = context.getWorld().toServerWorld().getServer().getRegistryManager().get(Registry.STRUCTURE_PROCESSOR_LIST_KEY).getOrEmpty(context.getConfig().processor);
        processor.orElse(StructureProcessorLists.EMPTY).getList().forEach(structurePlacementData::addProcessor); // add all processors
        mutable.set(position).move(-halfLengths.getX(), 0, -halfLengths.getZ());
        template.place(context.getWorld(), mutable, mutable, structurePlacementData, context.getRandom(), Block.NO_REDRAW);
        // Post-processors
        // For all processors that are sensitive to neighboring blocks such as vines.
        // Post processors will place the blocks themselves so we will not do anything with the return of Structure.process
        structurePlacementData.clearProcessors();
        Optional<StructureProcessorList> postProcessor = context.getWorld().toServerWorld().getServer().getRegistryManager().get(Registry.STRUCTURE_PROCESSOR_LIST_KEY).getOrEmpty(context.getConfig().postProcessor);
        postProcessor.orElse(StructureProcessorLists.EMPTY).getList().forEach(structurePlacementData::addProcessor); // add all post processors
        List<Structure.StructureBlockInfo> list = structurePlacementData.getRandomBlockInfos(((StructureAccessor)template).thebumblezone_getBlocks(), mutable).getAll();
        Structure.process(context.getWorld(), mutable, mutable, structurePlacementData, list);

        return true;
    }
}