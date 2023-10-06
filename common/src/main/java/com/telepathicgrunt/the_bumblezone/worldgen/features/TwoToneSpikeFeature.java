package com.telepathicgrunt.the_bumblezone.worldgen.features;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.mixin.world.WorldGenRegionAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzBlocks;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import com.telepathicgrunt.the_bumblezone.utils.OpenSimplex2F;
import com.telepathicgrunt.the_bumblezone.worldgen.features.configs.TwoToneSpikeFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Optional;

public class TwoToneSpikeFeature extends Feature<TwoToneSpikeFeatureConfig> {
    protected long seed;
    protected static OpenSimplex2F noiseGen;

    public TwoToneSpikeFeature(Codec<TwoToneSpikeFeatureConfig> configFactory) {
        super(configFactory);
    }

    public void setSeed(long seed) {
        if (this.seed != seed || noiseGen == null) {
            noiseGen = new OpenSimplex2F(seed);
            this.seed = seed;
        }
    }

    @Override
    public boolean place(FeaturePlaceContext<TwoToneSpikeFeatureConfig> context) {
        setSeed(context.level().getSeed());

        BlockPos.MutableBlockPos blockpos$Mutable = new BlockPos.MutableBlockPos();
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        if (level.getBlockState(origin).canOcclude()) {
            return false;
        }

        Registry<Structure> structureRegistry = context.level().registryAccess().registryOrThrow(Registries.STRUCTURE);
        if (context.level() instanceof WorldGenRegion) {
            StructureManager structureManager = ((WorldGenRegionAccessor)context.level()).getStructureManager();

            if (origin.getY() > 130 && origin.getY() < 148) {
                Structure thronePillar = structureRegistry.get(new ResourceLocation(Bumblezone.MODID, "throne_pillar"));
                if (thronePillar != null && structureManager.getStructureAt(origin, thronePillar).isValid()) {
                    return false;
                }
            }

            Optional<HolderSet.Named<Structure>> optionalHolders = structureRegistry.getTag(BzTags.NO_GIANT_SPIKES);
            if (optionalHolders.isPresent()) {
                for (Holder<Structure> structureHolder : optionalHolders.get()) {
                    if (structureManager.getStructureAt(origin, structureHolder.value()).isValid()) {
                        return false;
                    }
                }
            }
        }

        boolean validSpot = false;
        boolean superSlant = false;
        Direction wallDirection = null;
        BlockState attachedState = null;
        for (Direction direction : Direction.Plane.VERTICAL) {
            blockpos$Mutable.set(origin).move(direction, 5);
            BlockState state = level.getBlockState(blockpos$Mutable);
            if (state.canOcclude()) {
                validSpot = true;
                attachedState = state;
                break;
            }
        }

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            blockpos$Mutable.set(origin).move(direction, 1);
            BlockState state = level.getBlockState(blockpos$Mutable);
            if (state.canOcclude()) {
                validSpot = true;
                attachedState = state;
            }
        }

        if (!validSpot) {
            return false;
        }

        if (!attachedState.is(context.config().allowedBaseBlockCopies)) {
            return false;
        }

        blockpos$Mutable.set(origin).move(Direction.UP, 5);
        int directionSign = level.getBlockState(blockpos$Mutable).canOcclude() ? -1 : 1;
        int currentY = origin.getY() - (directionSign * 5);
        int thickness = random.nextInt(3) + 4;
        int height = context.config().heightRange.sample(context.random());
        int slantAmountX = (random.nextInt(6) + 5) * (random.nextBoolean() ? -1 : 1);
        int slantAmountZ = (random.nextInt(6) + 5) * (random.nextBoolean() ? -1 : 1);
        if (random.nextInt(4) == 0) {
            slantAmountX = 0;
        }
        if (random.nextInt(4) == 0) {
            slantAmountZ = 0;
        }

        BlockState tipBlock = context.config().tipBlocks.get(context.random().nextInt(context.config().tipBlocks.size())).defaultBlockState();
        for (int layer = 0; layer < height; layer++) {
            float currentThickness = thickness;
            int currentXSlant = slantAmountX == 0 ? 0 : layer / slantAmountX;
            int currentZSlant = slantAmountZ == 0 ? 0 : layer / slantAmountZ;
            if (layer == 0) {
                currentThickness -= 2;
            }
            else if (layer == 1) {
                currentThickness -= 1;
            }
            else if (layer == height - 1) {
                currentThickness = 0.5f;
                currentXSlant = slantAmountX == 0 ? 0 : (layer - 1) / slantAmountX;
                currentZSlant = slantAmountZ == 0 ? 0 : (layer - 1) / slantAmountZ;
            }
            else {
                currentThickness *= Math.max(((height - layer) / (float)height), 0.2f);
                currentThickness = Math.max(currentThickness, 1.1f);
            }

            for (int x = (int) -currentThickness; x <= currentThickness; x++) {
                for (int z = (int) -currentThickness; z <= currentThickness; z++) {
                    if ((x * x) + (z * z) < (currentThickness * currentThickness)) {
                        blockpos$Mutable.set(
                                origin.getX() + x + currentXSlant,
                                currentY,
                                origin.getZ() + z + currentZSlant);

                        BlockState state = level.getBlockState(blockpos$Mutable);
                        if (!state.canOcclude() && !state.is(BzBlocks.CRYSTALLINE_FLOWER.get())) {
                            BlockState newState;
                            if (layer > ((height * 2) / 3f) +
                                    noiseGen.noise3_Classic(
                                            blockpos$Mutable.getX(),
                                            blockpos$Mutable.getY(),
                                            blockpos$Mutable.getZ()) * 2)
                            {
                                newState = tipBlock;
                            }
                            else {
                                newState = attachedState;
                            }
                            level.setBlock(blockpos$Mutable, newState, 3);
                        }
                    }
                }
            }

            currentY += directionSign;
        }

        return false;
    }
}