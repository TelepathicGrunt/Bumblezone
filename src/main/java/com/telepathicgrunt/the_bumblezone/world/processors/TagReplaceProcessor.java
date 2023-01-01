package com.telepathicgrunt.the_bumblezone.world.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TagReplaceProcessor extends StructureProcessor {

    public static final Codec<TagReplaceProcessor> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Registry.BLOCK.byNameCodec().fieldOf("input_block").forGetter(config -> config.inputBlock),
            TagKey.codec(Registry.BLOCK_REGISTRY).fieldOf("output_block_tag").forGetter(config -> config.outputBlockTag),
            TagKey.codec(Registry.BLOCK_REGISTRY).fieldOf("blacklisted_output_block_tag").forGetter(config -> config.blacklistedOutputBlockTag),
            Codec.BOOL.fieldOf("double_tall_flower").orElse(false).forGetter(config -> config.doubleTallFlower),
            Codec.BOOL.fieldOf("same_throughout_piece").orElse(false).forGetter(config -> config.sameThroughoutPiece),
            Codec.INT.fieldOf("seed_random_addition").orElse(0).forGetter(config -> config.seedRandomAddition)
    ).apply(instance, instance.stable(TagReplaceProcessor::new)));

    private final Block inputBlock;
    private final TagKey<Block> outputBlockTag;
    private final TagKey<Block> blacklistedOutputBlockTag;
    private final boolean doubleTallFlower;
    private final boolean sameThroughoutPiece;
    private final int seedRandomAddition;

    public TagReplaceProcessor(Block inputBlock,
                               TagKey<Block> outputBlockTag,
                               TagKey<Block> blacklistedOutputBlockTag,
                               boolean doubleTallFlower,
                               boolean sameThroughoutPiece,
                               int seedRandomAddition)
    {
        this.inputBlock = inputBlock;
        this.outputBlockTag = outputBlockTag;
        this.blacklistedOutputBlockTag = blacklistedOutputBlockTag;
        this.doubleTallFlower = doubleTallFlower;
        this.sameThroughoutPiece = sameThroughoutPiece;
        this.seedRandomAddition = seedRandomAddition;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldReader, BlockPos pos, BlockPos pos2, StructureTemplate.StructureBlockInfo infoIn1, StructureTemplate.StructureBlockInfo infoIn2, StructurePlaceSettings settings) {
        StructureTemplate.StructureBlockInfo returnInfo = infoIn2;
        if(infoIn2.state.getBlock() == inputBlock &&
            settings.getBoundingBox() != null &&
            settings.getBoundingBox().isInside(infoIn2.pos))
        {
            Optional<HolderSet.Named<Block>> optionalBlocks = Registry.BLOCK.getTag(outputBlockTag);

            if(optionalBlocks.isPresent()) {
                RandomSource randomSource;
                if (sameThroughoutPiece) {
                    randomSource = settings.getRandom(pos.above(seedRandomAddition));
                }
                else {
                    randomSource = settings.getRandom(infoIn2.pos);
                }

                List<Block> blockList = optionalBlocks.get().stream()
                        .map(Holder::value)
                        .filter(f -> !f.defaultBlockState().is(blacklistedOutputBlockTag))
                        .toList();

                if (doubleTallFlower) {
                    blockList = blockList.stream()
                            .filter(f -> f instanceof TallFlowerBlock)
                            .collect(Collectors.toList());
                }

                if (blockList.size() > 0) {
                    BlockState newBlockState = blockList.get(randomSource.nextInt(blockList.size())).defaultBlockState();
                    for(Property<?> property : infoIn2.state.getProperties()) {
                        if(newBlockState.hasProperty(property)) {
                            newBlockState = getStateWithProperty(newBlockState, infoIn2.state, property);
                        }
                    }

                    if (doubleTallFlower) {
                        returnInfo = new StructureTemplate.StructureBlockInfo(infoIn2.pos, newBlockState, infoIn2.nbt);
                    }
                    else {
                        ChunkAccess chunk = worldReader.getChunk(infoIn2.pos);
                        BlockState oldBlockstate = chunk.getBlockState(infoIn2.pos);
                        BlockState belowOldBlockstate = chunk.getBlockState(infoIn2.pos.below());

                        chunk.setBlockState(infoIn2.pos, Blocks.AIR.defaultBlockState(), false);
                        chunk.setBlockState(infoIn2.pos.below(), Blocks.GRASS_BLOCK.defaultBlockState(), false);

                        if (newBlockState.canSurvive(worldReader, infoIn2.pos)) {
                            returnInfo = new StructureTemplate.StructureBlockInfo(infoIn2.pos, newBlockState, infoIn2.nbt);
                        }

                        chunk.setBlockState(infoIn2.pos, oldBlockstate, false);
                        chunk.setBlockState(infoIn2.pos.below(), belowOldBlockstate, false);
                    }
                }
            }
        }
        return returnInfo;
    }

    private <T extends Comparable<T>> BlockState getStateWithProperty(BlockState state, BlockState stateToCopy, Property<T> property) {
        return state.setValue(property, stateToCopy.getValue(property));
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.TAG_REPLACE_PROCESSOR.get();
    }
}
