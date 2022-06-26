package com.telepathicgrunt.the_bumblezone.world.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.configs.BzConfig;
import com.telepathicgrunt.the_bumblezone.mixin.world.WorldGenRegionAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.world.features.configs.NbtFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;


public class BeeDungeon extends NbtFeature {

    public BeeDungeon(Codec<NbtFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean place(FeaturePlaceContext<NbtFeatureConfig> context) {
        //affect rarity
        if (BzConfig.beeDungeonRarity >= 1000 ||
            context.random().nextInt(BzConfig.beeDungeonRarity) != 0) {
            return false;
        }

        if (isValidDungeonSpot(context)) {
            // generate dungeon
            super.place(context);
        }

        return true;
    }

    protected static boolean isValidDungeonSpot(FeaturePlaceContext<?> context) {
        Registry<Structure> configuredStructureFeatureRegistry = context.level().registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY);
        StructureManager structureManager = ((WorldGenRegionAccessor)context.level()).getStructureManager();

        for (Holder<Structure> configuredStructureFeature : configuredStructureFeatureRegistry.getTagOrEmpty(BzTags.NO_DUNGEONS)) {
            StructureStart startForFeature = structureManager.getStructureAt(context.origin(), configuredStructureFeature.value());
            if (startForFeature.isValid()) {
                return false;
            }
        }

        boolean validSpot = false;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        mutable.set(context.origin());

        //find a cave air spot
        for (Direction face : Direction.Plane.HORIZONTAL) {
            mutable.set(context.origin()).move(face, 3);

            BlockState state = context.level().getBlockState(mutable);
            if (state.is(Blocks.CAVE_AIR) || state.is(BzBlocks.PILE_OF_POLLEN)) {
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

        return validSpot && context.level().getBlockState(mutable).canOcclude();
    }
}
