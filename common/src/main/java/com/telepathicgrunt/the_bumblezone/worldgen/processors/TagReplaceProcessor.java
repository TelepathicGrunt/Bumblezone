package com.telepathicgrunt.the_bumblezone.worldgen.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TagReplaceProcessor extends StructureProcessor {

    public static final Codec<TagReplaceProcessor> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("input_block").forGetter(config -> config.inputBlock),
            TagKey.codec(Registries.BLOCK).fieldOf("output_block_tag").forGetter(config -> config.outputBlockTag),
            TagKey.codec(Registries.BLOCK).optionalFieldOf("blacklisted_output_block_tag").forGetter(config -> config.blacklistedOutputBlockTag),
            Codec.BOOL.fieldOf("double_tall_flower").orElse(false).forGetter(config -> config.doubleTallFlower),
            Codec.BOOL.fieldOf("same_throughout_piece").orElse(false).forGetter(config -> config.sameThroughoutPiece),
            Codec.INT.fieldOf("seed_random_addition").orElse(0).forGetter(config -> config.seedRandomAddition)
    ).apply(instance, instance.stable(TagReplaceProcessor::new)));

    private final Block inputBlock;
    private final TagKey<Block> outputBlockTag;
    private final Optional<TagKey<Block>> blacklistedOutputBlockTag;
    private final boolean doubleTallFlower;
    private final boolean sameThroughoutPiece;
    private final int seedRandomAddition;

    public TagReplaceProcessor(Block inputBlock,
                               TagKey<Block> outputBlockTag,
                               Optional<TagKey<Block>> blacklistedOutputBlockTag,
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
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldReader, BlockPos pos, BlockPos pos2, StructureTemplate.StructureBlockInfo infoIn1, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings settings) {
        StructureTemplate.StructureBlockInfo returnInfo = structureBlockInfoWorld;
        if(structureBlockInfoWorld.state().getBlock() == inputBlock &&
            (settings.getBoundingBox() == null ||
            settings.getBoundingBox().isInside(structureBlockInfoWorld.pos())))
        {
            Optional<HolderSet.Named<Block>> optionalBlocks = BuiltInRegistries.BLOCK.getTag(outputBlockTag);

            if(optionalBlocks.isPresent()) {
                RandomSource randomSource;
                if (sameThroughoutPiece) {
                    randomSource = settings.getRandom(pos.above(seedRandomAddition));
                }
                else {
                    randomSource = settings.getRandom(structureBlockInfoWorld.pos());
                }
                randomSource.nextBoolean(); // In curtain situations, we need to run randomsource once to get truly random values afterwards.

                List<Block> blockList = optionalBlocks
                        .map(holderSet -> holderSet
                                .stream()
                                .filter(block -> blacklistedOutputBlockTag
                                        .map(blockTagKey -> !block.is(blockTagKey))
                                        .orElse(true))
                                .map(Holder::value)
                                .toList()
                        )
                        .orElse(new ArrayList<>());

                if (doubleTallFlower) {
                    blockList = blockList.stream()
                            .filter(block -> block instanceof DoublePlantBlock)
                            .collect(Collectors.toList());
                }

                if (blockList.size() > 0) {
                    BlockState newBlockState = blockList.get(randomSource.nextInt(blockList.size())).defaultBlockState();
                    for(Property<?> property : structureBlockInfoWorld.state().getProperties()) {
                        if(newBlockState.hasProperty(property)) {
                            newBlockState = GeneralUtils.getStateWithProperty(newBlockState, structureBlockInfoWorld.state(), property);
                        }
                    }

                    if (newBlockState.getBlock() instanceof MultifaceBlock) {
                        for(Direction direction : Direction.values()) {
                            BooleanProperty faceProperty = MultifaceBlock.getFaceProperty(direction);
                            if (newBlockState.hasProperty(faceProperty)) {
                                newBlockState = newBlockState.setValue(faceProperty, direction == Direction.DOWN);
                            }
                        }
                    }

                    if (newBlockState.hasProperty(BlockStateProperties.FLOWER_AMOUNT)) {
                        newBlockState = newBlockState.setValue(BlockStateProperties.FLOWER_AMOUNT, 4);
                    }

                    if (newBlockState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                        newBlockState = newBlockState.setValue(BlockStateProperties.WATERLOGGED, !structureBlockInfoWorld.state().getFluidState().isEmpty());
                    }

                    ChunkAccess chunk = worldReader.getChunk(structureBlockInfoWorld.pos());

                    BlockPos mainPos = structureBlockInfoWorld.pos();
                    BlockPos groundPos = mainPos.below();
                    BlockState checkingState = newBlockState;

                    BlockState oldTopBlock = null;
                    BlockPos oldTopPos = null;
                    if (doubleTallFlower) {
                        if (newBlockState.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
                            oldTopPos = mainPos;
                            mainPos = mainPos.below(); // Bottom of double plant
                            groundPos = mainPos.below(); // Below bottom of double plant
                            checkingState = checkingState.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER);
                        }
                        else {
                            oldTopPos = structureBlockInfoWorld.pos().above();
                        }

                        oldTopBlock = chunk.getBlockState(oldTopPos);
                        chunk.setBlockState(oldTopPos, Blocks.AIR.defaultBlockState(), false);
                    }

                    BlockState oldBlockstate = chunk.getBlockState(mainPos);
                    BlockState belowGroundBlockstate = chunk.getBlockState(groundPos);

                    chunk.setBlockState(mainPos, Blocks.AIR.defaultBlockState(), false);
                    chunk.setBlockState(groundPos, Blocks.GRASS_BLOCK.defaultBlockState(), false);

                    if (checkingState.canSurvive(worldReader, mainPos)) {
                        returnInfo = new StructureTemplate.StructureBlockInfo(structureBlockInfoWorld.pos(), newBlockState, structureBlockInfoWorld.nbt());
                    }

                    if (oldTopBlock != null) {
                        chunk.setBlockState(oldTopPos, oldTopBlock, false);
                    }
                    chunk.setBlockState(mainPos, oldBlockstate, false);
                    chunk.setBlockState(groundPos, belowGroundBlockstate, false);
                }
            }
        }
        return returnInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.TAG_REPLACE_PROCESSOR.get();
    }
}
