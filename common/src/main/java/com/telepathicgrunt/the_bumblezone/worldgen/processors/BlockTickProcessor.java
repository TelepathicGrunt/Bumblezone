package com.telepathicgrunt.the_bumblezone.worldgen.processors;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.telepathicgrunt.the_bumblezone.modinit.BzProcessors;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.HashSet;

public class BlockTickProcessor extends StructureProcessor {

    public static final MapCodec<BlockTickProcessor> CODEC  = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                    BuiltInRegistries.BLOCK.byNameCodec().listOf()
                            .xmap(Sets::newHashSet, Lists::newArrayList)
                            .optionalFieldOf("blocks_to_schedule_tick", new HashSet<>())
                            .forGetter((config) -> config.blocksToScheduleTick))
            .apply(instance, instance.stable(BlockTickProcessor::new)));

    public final HashSet<Block> blocksToScheduleTick;

    private BlockTickProcessor(HashSet<Block> blocksToScheduleTick) {
        this.blocksToScheduleTick = blocksToScheduleTick;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings settings) {
        if (blocksToScheduleTick.contains(structureBlockInfoWorld.state().getBlock())) {
            if (GeneralUtils.isOutsideStructureAllowedBounds(settings, structureBlockInfoWorld.pos())) {
                return structureBlockInfoWorld;
            }

            if (structureBlockInfoWorld.pos().getY() > levelReader.getMinBuildHeight() && structureBlockInfoWorld.pos().getY() < levelReader.getMaxBuildHeight()) {
                ((LevelAccessor) levelReader).scheduleTick(structureBlockInfoWorld.pos(), structureBlockInfoWorld.state().getBlock(), 0);
            }
        }

        return structureBlockInfoWorld;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return BzProcessors.BLOCK_TICK_PROCESSOR.get();
    }
}