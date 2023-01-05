package com.telepathicgrunt.the_bumblezone.world.features.decorators;

import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.mixin.world.WorldGenRegionAccessor;
import com.telepathicgrunt.the_bumblezone.modinit.BzPlacements;
import com.telepathicgrunt.the_bumblezone.modinit.BzTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class HoneycombHolePlacer extends PlacementModifier {
    private enum SliceState {NEITHER, AIR, SOLID}
    private static final HoneycombHolePlacer INSTANCE = new HoneycombHolePlacer();
    public static final Codec<HoneycombHolePlacer> CODEC = Codec.unit(() -> INSTANCE);

    public static HoneycombHolePlacer honeycombHolePlacer() {
        return INSTANCE;
    }

    @Override
    public PlacementModifierType<?> type() {
        return BzPlacements.HONEYCOMB_HOLE_PLACER.get();
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext, RandomSource random, BlockPos blockPos) {
        //Start at top
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(blockPos.getX() - 4, 236, blockPos.getZ() + 4);
        List<BlockPos> blockPosList = new ArrayList<>();
        boolean alternate = false;

        StructureManager structureManager;
        List<StructureStart> structureStarts = new ArrayList<>();
        List<StructureStart> structureStartsPiecewiseCheck = new ArrayList<>();
        if (placementContext.getLevel() instanceof WorldGenRegion worldGenRegion) {
            Registry<Structure> structureRegistry = worldGenRegion.registryAccess().registryOrThrow(Registries.STRUCTURE);
            structureManager = ((WorldGenRegionAccessor)worldGenRegion).getStructureManager();

            ChunkPos chunkPos = new ChunkPos(blockPos);
            structureStarts = structureManager.startsForStructure(chunkPos,
                    struct -> structureRegistry.getHolderOrThrow(structureRegistry.getResourceKey(struct).get()).is(BzTags.NO_HONEYCOMB_HOLES));
            structureStartsPiecewiseCheck = structureManager.startsForStructure(chunkPos,
                    struct -> structureRegistry.getHolderOrThrow(structureRegistry.getResourceKey(struct).get()).is(BzTags.NO_HONEYCOMB_HOLES_PIECEWISE));
        }

        //Repeats twice with an offset on second pass
        for (int repeat = 0; repeat < 2; repeat++) {
            //Makes 23 holes from y = 236 to y = 52
            for (int count = 0; count < 23; count++) {
                //Moves back and forth in z coordinate so the holes alternate to make this layer of holes
                if (alternate) {
                    mutableBlockPos.move(0, -8, -8);
                } else {
                    mutableBlockPos.move(0, -8, 8);
                }

                alternate = !alternate;

                //Makes sure the place for holes is valid
                boolean validSpot = true;
                for (StructureStart structureStart : structureStarts) {
                    if (structureStart.isValid() && structureStart.getBoundingBox().inflatedBy(8).isInside(mutableBlockPos)) {
                        validSpot = false;
                        break;
                    }
                }
                for (StructureStart structureStart : structureStartsPiecewiseCheck) {
                    for(StructurePiece structurePiece : structureStart.getPieces()) {
                        if (structurePiece.getBoundingBox().inflatedBy(8).isInside(mutableBlockPos)) {
                            validSpot = false;
                            break;
                        }
                    }
                }

                if (validSpot && isPlaceValid(placementContext, mutableBlockPos)) {
                    blockPosList.add(mutableBlockPos.immutable());
                }
            }

            //set it back to the top but with an offset for the second layer of holes
            mutableBlockPos.set(blockPos.getX() + 4, 236, blockPos.getZ() + 12);
        }

        return blockPosList.stream();
    }

    /**
     * Checks the entire body length of where the hole would go to make sure that any
     * circular slice within it is entirely solid (so we don't spawn holes in mid-air or
     * have the ends only be made in land. A good chunk of the hole's body must be made).
     * <p>
     * It also needs one slide to be invalid so that the hole is not
     * placed inside the terrain and cutoff from the outside.
     */
    private boolean isPlaceValid(PlacementContext world, BlockPos pos) {
        boolean completelySolidSlice = false;
        boolean airInSlice = false;

        for (int x = -3; x <= 3; x++) {
            SliceState state = StateOfThisSlice(world, pos.west(x));
            if (state == SliceState.SOLID) {
                completelySolidSlice = true;
            } else if (state == SliceState.AIR) {
                airInSlice = true;
            }
        }

        return completelySolidSlice && airInSlice;
    }

    /**
     * Checks if the circular slice here is entirely solid land.
     */
    private SliceState StateOfThisSlice(PlacementContext world, BlockPos pos) {
        BlockState blockState;
        double distanceSq;

        for (double z = -4.5; z <= 4.5; z++) {
            for (double y = -3.5; y <= 3.5; y++) {
                distanceSq = z * z + y * y;
                if (distanceSq > 5 && distanceSq < 18) {
                    blockState = world.getBlockState(pos.offset(0, y + 1, z));
                    if (!blockState.canOcclude()) {
                        //only count Air and not Cave Air so holes aren't placed in caves
                        if (blockState.getBlock() == Blocks.AIR)
                            return SliceState.AIR;
                        else
                            return SliceState.NEITHER;
                    }

                    //Visual debugging
                    //world.setBlockState(pos.add(0, y+1, z), Blocks.REDSTONE_BLOCK.defaultBlockState(), 2);
                }
            }
        }

        return SliceState.SOLID;
    }
}
