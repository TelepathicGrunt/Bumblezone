package com.telepathicgrunt.the_bumblezone.world.structures;

import com.google.common.collect.Queues;
import com.mojang.datafixers.util.Pair;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.mixin.world.SinglePoolElementAccessor;
import com.telepathicgrunt.the_bumblezone.mixin.world.StructurePoolAccessor;
import com.telepathicgrunt.the_bumblezone.utils.BoxOctree;
import com.telepathicgrunt.the_bumblezone.utils.GeneralUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.EmptyPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.mutable.MutableObject;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Special thanks to YUNGNICKYOUNG for allowing me to use his piece count limiting jigsaw manager!
 * Some changes were done to make it more usable by multiple structures.
 * Source: https://github.com/yungnickyoung/YUNGs-Better-Strongholds/blob/fabric-1.16/src/main/java/com/yungnickyoung/minecraft/betterstrongholds/world/jigsaw/JigsawManager.java
 */
public class OptimizedJigsawManager {

    // Record for entries
    public record Entry(PoolElementStructurePiece piece, MutableObject<BoxOctree> boxOctreeMutableObject, int topYLimit, int depth) { }

    public static Optional<Structure.GenerationStub> assembleJigsawStructure(
            Structure.GenerationContext context,
            Holder<StructureTemplatePool> startPoolHolder,
            int size,
            ResourceLocation structureID,
            BlockPos startPos,
            boolean doBoundaryAdjustments,
            Optional<Heightmap.Types> heightmapType,
            int maxDistanceFromCenter,
            BiConsumer<StructurePiecesBuilder, List<PoolElementStructurePiece>> structureBoundsAdjuster
    ) {
        // Get jigsaw pool registry
        Registry<StructureTemplatePool> jigsawPoolRegistry = context.registryAccess().registryOrThrow(Registries.TEMPLATE_POOL);

        // Get a random orientation for the starting piece
        WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(0L));
        random.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
        Rotation rotation = Rotation.getRandom(random);

        // Get starting pool
        StructureTemplatePool startPool = startPoolHolder.value();
        if(startPool.size() == 0) {
            Bumblezone.LOGGER.warn("Bumblezone: Empty or nonexistent start pool in structure: {}  Crash is imminent", structureID);
            throw new RuntimeException("Bumblezone: Empty or nonexistent start pool in structure: " + structureID + " Crash is imminent");
        }

        // Grab a random starting piece from the start pool. This is just the piece design itself, without rotation or position information.
        // Think of it as a blueprint.
        StructurePoolElement startPieceBlueprint = startPool.getRandomTemplate(random);
        if (startPieceBlueprint == EmptyPoolElement.INSTANCE) {
            return Optional.empty();
        }

        // Instantiate a piece using the "blueprint" we just got.
        PoolElementStructurePiece startPiece = new PoolElementStructurePiece(
                context.structureTemplateManager(),
                startPieceBlueprint,
                startPos,
                startPieceBlueprint.getGroundLevelDelta(),
                rotation,
                startPieceBlueprint.getBoundingBox(context.structureTemplateManager(), startPos, rotation)
        );

        // Store center position of starting piece's bounding box
        BoundingBox pieceBoundingBox = startPiece.getBoundingBox();
        int pieceCenterX = (pieceBoundingBox.maxX() + pieceBoundingBox.minX()) / 2;
        int pieceCenterZ = (pieceBoundingBox.maxZ() + pieceBoundingBox.minZ()) / 2;
        int pieceCenterY = startPos.getY();

        if (heightmapType.isPresent()) {
            pieceCenterY += GeneralUtils.getLowestLand(
                context.chunkGenerator(),
                context.randomState(),
                pieceBoundingBox,
                context.heightAccessor(),
                true,
                heightmapType.get() == Heightmap.Types.OCEAN_FLOOR_WG
            ).getY();

            if (pieceCenterY >= GeneralUtils.getMaxTerrainLimit(context.chunkGenerator()) - pieceBoundingBox.maxY()) {
                return Optional.empty();
            }
        }

        int yAdjustment = pieceBoundingBox.minY() + startPiece.getGroundLevelDelta();
        startPiece.move(0, pieceCenterY - yAdjustment, 0);
        if (!context.validBiome().test(context.chunkGenerator().getBiomeSource().getNoiseBiome(QuartPos.fromBlock(pieceCenterX), QuartPos.fromBlock(pieceCenterY), QuartPos.fromBlock(pieceCenterZ), context.randomState().sampler()))) {
            return Optional.empty();
        }

        int finalPieceCenterY = pieceCenterY;
        return Optional.of(new Structure.GenerationStub(new BlockPos(pieceCenterX, pieceCenterY, pieceCenterZ), (structurePiecesBuilder) -> {
            List<PoolElementStructurePiece> components = new ArrayList<>();
            components.add(startPiece);
            components.clear();
            components.add(startPiece); // Add start piece to list of pieces

            if (size > 0) {
                AABB axisAlignedBB = new AABB(pieceCenterX - maxDistanceFromCenter, finalPieceCenterY - (maxDistanceFromCenter + 40), pieceCenterZ - maxDistanceFromCenter, pieceCenterX + maxDistanceFromCenter + 1, finalPieceCenterY + (maxDistanceFromCenter + 120), pieceCenterZ + maxDistanceFromCenter + 1);
                BoxOctree boxOctree = new BoxOctree(axisAlignedBB); // The maximum boundary of the entire structure
                boxOctree.addBox(AABB.of(pieceBoundingBox));
                Entry startPieceEntry = new Entry(startPiece, new MutableObject<>(boxOctree), finalPieceCenterY + 80, 0);

                Assembler assembler = new Assembler(jigsawPoolRegistry, size, context, components, random);
                assembler.availablePieces.addLast(startPieceEntry);

                while (!assembler.availablePieces.isEmpty()) {
                    Entry entry = assembler.availablePieces.removeFirst();
                    assembler.generatePiece(entry.piece, entry.boxOctreeMutableObject, entry.topYLimit, entry.depth, doBoundaryAdjustments, context.heightAccessor());
                }
            }

            components.forEach(structurePiecesBuilder::addPiece);
            structureBoundsAdjuster.accept(structurePiecesBuilder, components);

            // Do not generate if out of bounds
            if(structurePiecesBuilder.getBoundingBox().maxY() > context.heightAccessor().getMaxBuildHeight()) {
                structurePiecesBuilder.clear();
                return;
            }

            if(components.isEmpty()) {
                return;
            }

            Vec3i structureCenter = components.get(0).getBoundingBox().getCenter();
            int xOffset = startPos.getX() - structureCenter.getX();
            int zOffset = startPos.getZ() - structureCenter.getZ();

            for(StructurePiece structurePiece : components) {
                structurePiece.move(xOffset, 0, zOffset);
            }
        }));
    }
    
    public static final class Assembler {
        private final Registry<StructureTemplatePool> poolRegistry;
        private final int maxDepth;
        private final ChunkGenerator chunkGenerator;
        private final RandomState randomState;
        private final StructureTemplateManager structureTemplateManager;
        private final List<? super PoolElementStructurePiece> structurePieces;
        private final RandomSource random;
        public final Deque<Entry> availablePieces = Queues.newArrayDeque();

        public Assembler(Registry<StructureTemplatePool> poolRegistry,
                         int maxDepth,
                         Structure.GenerationContext context,
                         List<? super PoolElementStructurePiece> structurePieces,
                         RandomSource random
        ) {
            this.poolRegistry = poolRegistry;
            this.maxDepth = maxDepth;
            this.chunkGenerator = context.chunkGenerator();
            this.randomState = context.randomState();
            this.structureTemplateManager = context.structureTemplateManager();
            this.structurePieces = structurePieces;
            this.random = random;
        }

        public void generatePiece(PoolElementStructurePiece piece, 
                                  MutableObject<BoxOctree> boxOctree, 
                                  int minY, 
                                  int depth,
                                  boolean doBoundaryAdjustments,
                                  LevelHeightAccessor heightLimitView
        ) {
            // Collect data from params regarding piece to process
            StructurePoolElement pieceBlueprint = piece.getElement();
            BlockPos piecePos = piece.getPosition();
            Rotation pieceRotation = piece.getRotation();
            BoundingBox pieceBoundingBox = piece.getBoundingBox();
            int pieceMinY = pieceBoundingBox.minY();
            MutableObject<BoxOctree> parentOctree = new MutableObject<>();

            // Get list of all jigsaw blocks in this piece
            List<StructureTemplate.StructureBlockInfo> pieceJigsawBlocks = pieceBlueprint.getShuffledJigsawBlocks(this.structureTemplateManager, piecePos, pieceRotation, this.random);

            for (StructureTemplate.StructureBlockInfo jigsawBlock : pieceJigsawBlocks) {
                // Gather jigsaw block information
                Direction direction = JigsawBlock.getFrontFacing(jigsawBlock.state);
                BlockPos jigsawBlockPos = jigsawBlock.pos;
                BlockPos jigsawBlockTargetPos = jigsawBlockPos.relative(direction);

                // Get the jigsaw block's piece pool
                ResourceLocation jigsawBlockPool = new ResourceLocation(jigsawBlock.nbt.getString("pool"));
                Optional<StructureTemplatePool> poolOptional = this.poolRegistry.getOptional(jigsawBlockPool);

                // Only continue if we are using the jigsaw pattern registry and if it is not empty
                if (!(poolOptional.isPresent() && (poolOptional.get().size() != 0 || Objects.equals(jigsawBlockPool, Pools.EMPTY.location())))) {
                    Bumblezone.LOGGER.warn("Bumblezone: Empty or nonexistent pool: {} which is being called from {}", jigsawBlockPool, pieceBlueprint instanceof SinglePoolElement ? ((SinglePoolElementAccessor) pieceBlueprint).getTemplate().left().get() : "not a SinglePoolElement class");
                    continue;
                }

                // Get the jigsaw block's fallback pool (which is a part of the pool's JSON)
                Holder<StructureTemplatePool> fallbackOptional = poolOptional.get().getFallback();

                // Adjustments for if the target block position is inside the current piece
                boolean isTargetInsideCurrentPiece = pieceBoundingBox.isInside(jigsawBlockTargetPos);
                int targetPieceBoundsTop;
                MutableObject<BoxOctree> octreeToUse;
                if (isTargetInsideCurrentPiece) {
                    octreeToUse = parentOctree;
                    targetPieceBoundsTop = pieceMinY;
                    if (parentOctree.getValue() == null) {
                        parentOctree.setValue(new BoxOctree(AABB.of(pieceBoundingBox)));
                    }
                }
                else {
                    octreeToUse = boxOctree;
                    targetPieceBoundsTop = minY;
                }

                // Process the pool pieces, randomly choosing different pieces from the pool to spawn
                if (depth != this.maxDepth) {
                    StructurePoolElement generatedPiece = this.processList(new ArrayList<>(((StructurePoolAccessor)poolOptional.get()).getRawTemplates()), doBoundaryAdjustments, jigsawBlock, jigsawBlockTargetPos, pieceMinY, jigsawBlockPos, octreeToUse, piece, depth, targetPieceBoundsTop, heightLimitView);
                    if (generatedPiece != null) continue; // Stop here since we've already generated the piece
                }

                // Process the fallback pieces in the event none of the pool pieces work
                this.processList(new ArrayList<>(((StructurePoolAccessor)fallbackOptional.value()).getRawTemplates()), doBoundaryAdjustments, jigsawBlock, jigsawBlockTargetPos, pieceMinY, jigsawBlockPos, octreeToUse, piece, depth, targetPieceBoundsTop, heightLimitView);
            }
        }

        /**
         * Helper function. Searches candidatePieces for a suitable piece to spawn.
         * All other params are intended to be passed directly from {@link Assembler#generatePiece}
         * @return The piece genereated, or null if no suitable pieces were found.
         */
        private StructurePoolElement processList(
                List<Pair<StructurePoolElement, Integer>> candidatePieces,
                boolean doBoundaryAdjustments,
                StructureTemplate.StructureBlockInfo jigsawBlock,
                BlockPos jigsawBlockTargetPos,
                int pieceMinY,
                BlockPos jigsawBlockPos,
                MutableObject<BoxOctree> boxOctreeMutableObject,
                PoolElementStructurePiece piece,
                int depth,
                int targetPieceBoundsTop,
                LevelHeightAccessor heightLimitView
        ) {
            StructureTemplatePool.Projection piecePlacementBehavior = piece.getElement().getProjection();
            boolean isPieceRigid = piecePlacementBehavior == StructureTemplatePool.Projection.RIGID;
            int jigsawBlockRelativeY = jigsawBlockPos.getY() - pieceMinY;
            int surfaceHeight = -1; // The y-coordinate of the surface. Only used if isPieceRigid is false.

            int totalCount = candidatePieces.stream().mapToInt(Pair::getSecond).reduce(0, Integer::sum);

            while (candidatePieces.size() > 0) {
                Pair<StructurePoolElement, Integer> chosenPiecePair = candidatePieces.get(0);

                int chosenWeight = random.nextInt(totalCount) + 1;
                for (Pair<StructurePoolElement, Integer> candidate : candidatePieces) {
                    chosenWeight -= candidate.getSecond();
                    if (chosenWeight <= 0) {
                        chosenPiecePair = candidate;
                        break;
                    }
                }

                StructurePoolElement candidatePiece = chosenPiecePair.getFirst();

                // Vanilla check. Not sure on the implications of this.
                if (candidatePiece == EmptyPoolElement.INSTANCE) {
                    return null;
                }

                // Try different rotations to see which sides of the piece are fit to be the receiving end
                for (Rotation rotation : Rotation.getShuffled(this.random)) {
                    List<StructureTemplate.StructureBlockInfo> candidateJigsawBlocks = candidatePiece.getShuffledJigsawBlocks(this.structureTemplateManager, BlockPos.ZERO, rotation, this.random);
                    BoundingBox tempCandidateBoundingBox = candidatePiece.getBoundingBox(this.structureTemplateManager, BlockPos.ZERO, rotation);

                    // Some sort of logic for setting the candidateHeightAdjustments var if doBoundaryAdjustments.
                    // Not sure on this - personally, I never enable doBoundaryAdjustments.
                    int candidateHeightAdjustments;
                    if (doBoundaryAdjustments && tempCandidateBoundingBox.getYSpan() <= 16) {
                        candidateHeightAdjustments = candidateJigsawBlocks.stream().mapToInt((pieceCandidateJigsawBlock) -> {
                            if (!tempCandidateBoundingBox.isInside(pieceCandidateJigsawBlock.pos.relative(JigsawBlock.getFrontFacing(pieceCandidateJigsawBlock.state)))) {
                                return 0;
                            }
                            else {
                                ResourceLocation candidateTargetPool = new ResourceLocation(pieceCandidateJigsawBlock.nbt.getString("pool"));
                                Optional<StructureTemplatePool> candidateTargetPoolOptional = this.poolRegistry.getOptional(candidateTargetPool);
                                Optional<StructureTemplatePool> candidateTargetFallbackOptional = candidateTargetPoolOptional.flatMap((structureTemplatePool) -> Optional.of(structureTemplatePool.getFallback().value()));
                                int tallestCandidateTargetPoolPieceHeight = candidateTargetPoolOptional.map((p_242842_1_) -> p_242842_1_.getMaxSize(this.structureTemplateManager)).orElse(0);
                                int tallestCandidateTargetFallbackPieceHeight = candidateTargetFallbackOptional.map((p_242840_1_) -> p_242840_1_.getMaxSize(this.structureTemplateManager)).orElse(0);
                                return Math.max(tallestCandidateTargetPoolPieceHeight, tallestCandidateTargetFallbackPieceHeight);
                            }
                        }).max().orElse(0);
                    } 
                    else {
                        candidateHeightAdjustments = 0;
                    }

                    // Check for each of the candidate's jigsaw blocks for a match
                    for (StructureTemplate.StructureBlockInfo candidateJigsawBlock : candidateJigsawBlocks) {
                        if (GeneralUtils.canJigsawsAttach(jigsawBlock, candidateJigsawBlock)) {
                            BlockPos candidateJigsawBlockPos = candidateJigsawBlock.pos;
                            BlockPos candidateJigsawBlockRelativePos = new BlockPos(jigsawBlockTargetPos.getX() - candidateJigsawBlockPos.getX(), jigsawBlockTargetPos.getY() - candidateJigsawBlockPos.getY(), jigsawBlockTargetPos.getZ() - candidateJigsawBlockPos.getZ());

                            // Get the bounding box for the piece, offset by the relative position difference
                            BoundingBox candidateBoundingBox = candidatePiece.getBoundingBox(this.structureTemplateManager, candidateJigsawBlockRelativePos, rotation);

                            // Determine if candidate is rigid
                            StructureTemplatePool.Projection candidatePlacementBehavior = candidatePiece.getProjection();
                            boolean isCandidateRigid = candidatePlacementBehavior == StructureTemplatePool.Projection.RIGID;

                            // Determine how much the candidate jigsaw block is off in the y direction.
                            // This will be needed to offset the candidate piece so that the jigsaw blocks line up properly.
                            int candidateJigsawBlockRelativeY = candidateJigsawBlockPos.getY();
                            int candidateJigsawYOffsetNeeded = jigsawBlockRelativeY - candidateJigsawBlockRelativeY + JigsawBlock.getFrontFacing(jigsawBlock.state).getStepY();

                            // Determine how much we need to offset the candidate piece itself in order to have the jigsaw blocks aligned.
                            // Depends on if the placement of both pieces is rigid or not
                            int adjustedCandidatePieceMinY;
                            if (isPieceRigid && isCandidateRigid) {
                                adjustedCandidatePieceMinY = pieceMinY + candidateJigsawYOffsetNeeded;
                            }
                            else {
                                if (surfaceHeight == -1) {
                                    surfaceHeight = this.chunkGenerator.getFirstFreeHeight(jigsawBlockPos.getX(), jigsawBlockPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightLimitView, randomState);
                                }

                                adjustedCandidatePieceMinY = surfaceHeight - candidateJigsawBlockRelativeY;
                            }
                            int candidatePieceYOffsetNeeded = adjustedCandidatePieceMinY - candidateBoundingBox.minY();

                            // Offset the candidate's bounding box by the necessary amount
                            BoundingBox adjustedCandidateBoundingBox = candidateBoundingBox.moved(0, candidatePieceYOffsetNeeded, 0);

                            // Add this offset to the relative jigsaw block position as well
                            BlockPos adjustedCandidateJigsawBlockRelativePos = candidateJigsawBlockRelativePos.offset(0, candidatePieceYOffsetNeeded, 0);

                            // Final adjustments to the bounding box.
                            if (candidateHeightAdjustments > 0) {
                                int k2 = Math.max(candidateHeightAdjustments + 1, adjustedCandidateBoundingBox.maxY() - adjustedCandidateBoundingBox.minY());
                                adjustedCandidateBoundingBox.encapsulate(new BlockPos(adjustedCandidateBoundingBox.minX(), adjustedCandidateBoundingBox.minY() + k2, adjustedCandidateBoundingBox.minZ()));
                            }

                            AABB axisAlignedBB = AABB.of(adjustedCandidateBoundingBox);
                            AABB axisAlignedBBDeflated = axisAlignedBB.deflate(0.25D);
                            boolean validBounds = false;

                            // Make sure new piece fits within the chosen octree without intersecting any other piece.
                            if (boxOctreeMutableObject.getValue().boundaryContains(axisAlignedBBDeflated) && !boxOctreeMutableObject.getValue().intersectsAnyBox(axisAlignedBBDeflated)) {
                                boxOctreeMutableObject.getValue().addBox(axisAlignedBB);
                                validBounds = true;
                            }

                            if (validBounds) {

                                // Determine ground level delta for this new piece
                                int newPieceGroundLevelDelta = piece.getGroundLevelDelta();
                                int groundLevelDelta;
                                if (isCandidateRigid) {
                                    groundLevelDelta = newPieceGroundLevelDelta - candidateJigsawYOffsetNeeded;
                                }
                                else {
                                    groundLevelDelta = candidatePiece.getGroundLevelDelta();
                                }

                                // Create new piece
                                PoolElementStructurePiece newPiece = new PoolElementStructurePiece(
                                        this.structureTemplateManager,
                                        candidatePiece,
                                        adjustedCandidateJigsawBlockRelativePos,
                                        groundLevelDelta,
                                        rotation,
                                        adjustedCandidateBoundingBox
                                );

                                // Determine actual y-value for the new jigsaw block
                                int candidateJigsawBlockY;
                                if (isPieceRigid) {
                                    candidateJigsawBlockY = pieceMinY + jigsawBlockRelativeY;
                                }
                                else if (isCandidateRigid) {
                                    candidateJigsawBlockY = adjustedCandidatePieceMinY + candidateJigsawBlockRelativeY;
                                }
                                else {
                                    if (surfaceHeight == -1) {
                                        surfaceHeight = this.chunkGenerator.getFirstFreeHeight(jigsawBlockPos.getX(), jigsawBlockPos.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightLimitView, randomState);
                                    }

                                    candidateJigsawBlockY = surfaceHeight + candidateJigsawYOffsetNeeded / 2;
                                }

                                // Add the junction to the existing piece
                                piece.addJunction(
                                        new JigsawJunction(
                                                jigsawBlockTargetPos.getX(),
                                                candidateJigsawBlockY - jigsawBlockRelativeY + newPieceGroundLevelDelta,
                                                jigsawBlockTargetPos.getZ(),
                                                candidateJigsawYOffsetNeeded,
                                                candidatePlacementBehavior)
                                );

                                // Add the junction to the new piece
                                newPiece.addJunction(
                                        new JigsawJunction(
                                                jigsawBlockPos.getX(),
                                                candidateJigsawBlockY - candidateJigsawBlockRelativeY + groundLevelDelta,
                                                jigsawBlockPos.getZ(),
                                                -candidateJigsawYOffsetNeeded,
                                                piecePlacementBehavior)
                                );

                                // Add the piece
                                this.structurePieces.add(newPiece);
                                if (depth + 1 <= this.maxDepth) {
                                    this.availablePieces.addLast(new Entry(newPiece, boxOctreeMutableObject, targetPieceBoundsTop, depth + 1));
                                }
                                return candidatePiece;
                            }
                        }
                    }
                }
                totalCount -= chosenPiecePair.getSecond();
                candidatePieces.remove(chosenPiecePair);
            }
            return null;
        }
    }
}
