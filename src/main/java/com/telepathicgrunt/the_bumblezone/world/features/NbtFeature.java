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
    public boolean generate(ISeedReader world, ChunkGenerator chunkGenerator, Random random, BlockPos position, NbtFeatureConfig config) {
        ResourceLocation nbtRL = GeneralUtils.getRandomEntry(config.nbtResourcelocationsAndWeights, random);

        TemplateManager structureManager = world.getWorld().getStructureTemplateManager();
        Template template = structureManager.getTemplate(nbtRL);
        if(template == null){
            Bumblezone.LOGGER.error("Identifier to the specified nbt file was not found! : {}", nbtRL);
            return false;
        }
        Rotation rotation = Rotation.randomRotation(random);

        // For proper offsetting the feature so it rotate properly around position parameter.
        BlockPos halfLengths = new BlockPos(
                template.getSize().getX() / 2,
                template.getSize().getY() / 2,
                template.getSize().getZ() / 2);

        BlockPos.Mutable mutable = new BlockPos.Mutable().setPos(position);

        // offset the feature's position
        position = position.up(config.structureYOffset);

        PlacementSettings placementsettings = (new PlacementSettings()).setRotation(rotation).setCenterOffset(halfLengths).setIgnoreEntities(false);
        Optional<StructureProcessorList> processor = world.getWorld().getServer().func_244267_aX().getRegistry(Registry.STRUCTURE_PROCESSOR_LIST_KEY).getOptional(config.processor);
        processor.orElse(ProcessorLists.field_244101_a).func_242919_a().forEach(placementsettings::addProcessor); // add all processors
        template.func_237152_b_(world, mutable.setPos(position).move(-halfLengths.getX(), 0, -halfLengths.getZ()), placementsettings, random);

        // Post-processors
        // For all processors that are sensitive to neighboring blocks such as vines.
        // Post processors will place the blocks themselves so we will not do anything with the return of Structure.process
        placementsettings.clearProcessors();
        Optional<StructureProcessorList> postProcessor = world.getWorld().getServer().func_244267_aX().getRegistry(Registry.STRUCTURE_PROCESSOR_LIST_KEY).getOptional(config.postProcessor);
        postProcessor.orElse(ProcessorLists.field_244101_a).func_242919_a().forEach(placementsettings::addProcessor); // add all post processors
        List<Template.BlockInfo> list = placementsettings.func_237132_a_(((TemplateAccessor)template).bz_getBlocks(), mutable).func_237157_a_();
        Template.func_237145_a_(world, mutable, mutable, placementsettings, list);

        return true;
    }
}
