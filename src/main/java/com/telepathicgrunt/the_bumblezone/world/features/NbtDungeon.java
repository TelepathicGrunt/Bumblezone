package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import com.telepathicgrunt.the_bumblezone.world.features.configs.NbtDungeonConfig;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.ChestType;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.template.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class NbtDungeon extends Feature<NbtDungeonConfig>{

    public NbtDungeon(Codec<NbtDungeonConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean generate(ISeedReader world, ChunkGenerator chunkGenerator, Random random, BlockPos position, NbtDungeonConfig config) {
        ResourceLocation nbtRL = GeneralUtils.getRandomEntry(config.nbtResourcelocationsAndWeights, random);

        TemplateManager structureManager = world.getWorld().getStructureTemplateManager();
        Template template = structureManager.getTemplate(nbtRL);
        if(template == null){
            Bumblezone.LOGGER.error("Identifier to the specified nbt file was not found! : {}", nbtRL);
            return false;
        }
        Rotation rotation = Rotation.randomRotation(random);

        // For proper offsetting the dungeon so it rotate properly around position parameter.
        BlockPos halfLengths = new BlockPos(
                template.getSize().getX() / 2,
                template.getSize().getY() / 2,
                template.getSize().getZ() / 2);

        // Rotated blockpos for the nbt's sizes to be used later.
        BlockPos fullLengths = new BlockPos(
                Math.abs(template.getSize().rotate(rotation).getX()),
                Math.abs(template.getSize().rotate(rotation).getY()),
                Math.abs(template.getSize().rotate(rotation).getZ()));

        // For post processing spawners and chests for rotated dungeon.
        BlockPos halfLengthsRotated = new BlockPos(
                fullLengths.getX() / 2,
                fullLengths.getY() / 2,
                fullLengths.getZ() / 2);

        BlockPos.Mutable mutable = new BlockPos.Mutable().setPos(position);

        // offset the dungeon such as ocean dungeons down 1
        position = position.up(config.structureYOffset);

        //UltraAmplifiedDimension.LOGGER.log(Level.INFO, nbtRL + " at X: "+position.getX() +", "+position.getY()+", "+position.getZ());
        PlacementSettings placementsettings = (new PlacementSettings()).setRotation(rotation).setCenterOffset(halfLengths).setIgnoreEntities(false);
        Optional<StructureProcessorList> processor = world.getWorld().getServer().func_244267_aX().getRegistry(Registry.STRUCTURE_PROCESSOR_LIST_KEY).getOptional(config.processor);
        processor.orElse(ProcessorLists.field_244101_a).func_242919_a().forEach(placementsettings::addProcessor); // add all processors
        template.func_237152_b_(world, mutable.setPos(position).move(-halfLengths.getX(), 0, -halfLengths.getZ()), placementsettings, random);

        return true;
    }
}
