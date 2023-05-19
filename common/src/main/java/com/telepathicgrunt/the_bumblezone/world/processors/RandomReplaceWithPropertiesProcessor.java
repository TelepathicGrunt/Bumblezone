package com.telepathicgrunt.the_bumblezone.world.processors;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;
import java.util.Optional;

/**
 * Replace blocks randomly but preserve the properties of the block
 */
public class RandomReplaceWithPropertiesProcessor extends StructureProcessor {

    public static final Codec<RandomReplaceWithPropertiesProcessor> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("input_block").forGetter(config -> config.inputBlock),
            BuiltInRegistries.BLOCK.byNameCodec().optionalFieldOf("output_block").forGetter(config -> config.outputBlock),
            BuiltInRegistries.BLOCK.byNameCodec().listOf().optionalFieldOf("output_blocks", ImmutableList.of()).forGetter(config -> config.outputBlocks),
            Codec.floatRange(0, 1).fieldOf("probability").forGetter(config -> config.probability)
    ).apply(instance, instance.stable(RandomReplaceWithPropertiesProcessor::new)));

    private final Block inputBlock;
    private final Optional<Block> outputBlock;
    private final List<Block> outputBlocks;
    private final float probability;

    public RandomReplaceWithPropertiesProcessor(Block inputBlock, Optional<Block> outputBlock, List<Block> outputBlocks, float probability) {
        this.inputBlock = inputBlock;
        this.outputBlock = outputBlock;
        this.outputBlocks = outputBlocks;
        this.probability = probability;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldReader, BlockPos pos, BlockPos pos2, StructureTemplate.StructureBlockInfo infoIn1, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings settings) {
        if(structureBlockInfoWorld.state().getBlock() == inputBlock) {
            BlockPos worldPos = structureBlockInfoWorld.pos();
            RandomSource random = RandomSource.create();
            int offSet = settings.getProcessors().indexOf(this) + 1;
            random.setSeed(worldPos.asLong() * worldPos.asLong() * offSet);
            if(random.nextFloat() < probability) {
                if(outputBlock.isPresent()) {
                    BlockState newBlockState = outputBlock.get().defaultBlockState();
                    for(Property<?> property : structureBlockInfoWorld.state().getProperties()) {
                        if(newBlockState.hasProperty(property)) {
                            newBlockState = GeneralUtils.getStateWithProperty(newBlockState, structureBlockInfoWorld.state(), property);
                        }
                    }
                    return new StructureTemplate.StructureBlockInfo(structureBlockInfoWorld.pos(), newBlockState, structureBlockInfoWorld.nbt());
                }
                else if(!outputBlocks.isEmpty()) {
                    BlockState newBlockState = outputBlocks.get(random.nextInt(outputBlocks.size())).defaultBlockState();
                    for(Property<?> property : structureBlockInfoWorld.state().getProperties()) {
                        if(newBlockState.hasProperty(property)) {
                            newBlockState = GeneralUtils.getStateWithProperty(newBlockState, structureBlockInfoWorld.state(), property);
                        }
                    }
                    return new StructureTemplate.StructureBlockInfo(structureBlockInfoWorld.pos(), newBlockState, structureBlockInfoWorld.nbt());
                }
                else{
                    Bumblezone.LOGGER.warn("The Bumblezone: the_bumblezone:random_replace_with_properties_processor in a processor file has no replacement block of any kind.");
                }
            }
        }
        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.RANDOM_REPLACE_WITH_PROPERTIES_PROCESSOR.get();
    }
}
