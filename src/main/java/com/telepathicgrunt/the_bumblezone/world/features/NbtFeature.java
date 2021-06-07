package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.mixin.world.TemplateAccessor;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import com.telepathicgrunt.the_bumblezone.world.features.configs.NbtFeatureConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.template.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class NbtFeature extends Feature<NbtFeatureConfig>{

    public NbtFeature(Codec<NbtFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean place(ISeedReader world, ChunkGenerator chunkGenerator, Random random, BlockPos position, NbtFeatureConfig config) {
        ResourceLocation nbtRL = GeneralUtils.getRandomEntry(config.nbtResourcelocationsAndWeights, random);

        TemplateManager structureManager = world.getLevel().getStructureManager();
        Template template = structureManager.get(nbtRL);
        if(template == null){
            Bumblezone.LOGGER.error("Identifier to the specified nbt file was not found! : {}", nbtRL);
            return false;
        }
        Rotation rotation = Rotation.getRandom(random);

        // For proper offsetting the feature so it rotate properly around position parameter.
        BlockPos halfLengths = new BlockPos(
                template.getSize().getX() / 2,
                template.getSize().getY() / 2,
                template.getSize().getZ() / 2);

        BlockPos.Mutable mutable = new BlockPos.Mutable().set(position);

        // offset the feature's position
        position = position.above(config.structureYOffset);

        PlacementSettings placementsettings = (new PlacementSettings()).setRotation(rotation).setRotationPivot(halfLengths).setIgnoreEntities(false);
        Optional<StructureProcessorList> processor = world.getLevel().getServer().registryAccess().registryOrThrow(Registry.PROCESSOR_LIST_REGISTRY).getOptional(config.processor);
        processor.orElse(ProcessorLists.EMPTY).list().forEach(placementsettings::addProcessor); // add all processors
        template.placeInWorld(world, mutable.set(position).move(-halfLengths.getX(), 0, -halfLengths.getZ()), placementsettings, random);

        // Post-processors
        // For all processors that are sensitive to neighboring blocks such as vines.
        // Post processors will place the blocks themselves so we will not do anything with the return of Structure.process
        placementsettings.clearProcessors();
        Optional<StructureProcessorList> postProcessor = world.getLevel().getServer().registryAccess().registryOrThrow(Registry.PROCESSOR_LIST_REGISTRY).getOptional(config.postProcessor);
        postProcessor.orElse(ProcessorLists.EMPTY).list().forEach(placementsettings::addProcessor); // add all post processors
        List<Template.BlockInfo> list = placementsettings.getRandomPalette(((TemplateAccessor)template).thebumblezone_getBlocks(), mutable).blocks();
        Template.processBlockInfos(world, mutable, mutable, placementsettings, list);

        return true;
    }
}
