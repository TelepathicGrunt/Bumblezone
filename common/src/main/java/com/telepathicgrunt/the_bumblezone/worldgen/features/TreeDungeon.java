package com.telepathicgrunt.the_bumblezone.worldgen.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.configs.BzWorldgenConfigs;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import com.telepathicgrunt.the_bumblezone.worldgen.features.configs.TreeDungeonFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import java.util.Optional;


public class TreeDungeon extends NbtFeature<TreeDungeonFeatureConfig> {

    public TreeDungeon(Codec<TreeDungeonFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean place(FeaturePlaceContext<TreeDungeonFeatureConfig> context) {
        //affect rarity
        if (BzWorldgenConfigs.treeDungeonRarity >= 1000 ||
            context.random().nextInt(BzWorldgenConfigs.treeDungeonRarity) != 0) {
            return false;
        }

        if (isValidDungeonSpot(context)) {
            // generate dungeon
            super.place(context);

            WorldGenLevel level = context.level();
            BlockPos center = context.origin().above(context.config().structureYOffset + 3);

            level.setBlock(center, Blocks.CAVE_AIR.defaultBlockState(), 3);
            level.setBlock(center.above(), Blocks.CAVE_AIR.defaultBlockState(), 3);

            Optional<ConfiguredFeature<?, ?>> configuredFeature = level.registryAccess()
                    .registryOrThrow(Registries.CONFIGURED_FEATURE)
                    .getOptional(context.config().treeConfiguredFeature);

            configuredFeature.ifPresent(cf -> cf.place(level, context.chunkGenerator(), context.random(), center));
        }

        return true;
    }

    protected static boolean isValidDungeonSpot(FeaturePlaceContext<?> context) {
        if (!context.level().getBlockState(context.origin()).canOcclude()) {
            return false;
        }

        boolean validSpot = false;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        mutable.set(context.origin());

        //find a cave air spot
        for (Direction face : Direction.Plane.HORIZONTAL) {
            mutable.set(context.origin()).move(face, 3);

            BlockState state = context.level().getBlockState(mutable);
            if (state.is(Blocks.CAVE_AIR) || state.is(BzBlocks.PILE_OF_POLLEN.get())) {
                validSpot = true;
                break;
            }
        }

        //make sure we aren't too close to regular air
        for (int xOffset = -6; xOffset <= 6; xOffset += 6) {
            for (int zOffset = -6; zOffset <= 6; zOffset += 6) {
                for (int yOffset = -3; yOffset <= 9; yOffset += 3) {
                    mutable.set(context.origin()).move(xOffset, yOffset, zOffset);

                    if (context.level().getBlockState(mutable).is(Blocks.AIR))
                        validSpot = false;
                }
            }
        }

        return validSpot;
    }
}
