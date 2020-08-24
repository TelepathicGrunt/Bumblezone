package net.telepathicgrunt.bumblezone.features;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Clearable;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.BitSetVoxelSet;
import net.minecraft.util.shape.VoxelSet;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.blocks.HoneyCrystal;
import net.telepathicgrunt.bumblezone.blocks.HoneycombBrood;
import net.telepathicgrunt.bumblezone.mixin.StructureAccessorInvoker;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class BeeDungeon extends Feature<DefaultFeatureConfig>{

    private static final BlockState HONEY_CRYSTAL = BzBlocks.HONEY_CRYSTAL.getDefaultState();

    public BeeDungeon(Codec<DefaultFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos position, DefaultFeatureConfig config) {
        //affect rarity
        if (Bumblezone.BZ_CONFIG.BZDungeonsConfig.beeDungeonRarity >= 1000 ||
            random.nextInt(Bumblezone.BZ_CONFIG.BZDungeonsConfig.beeDungeonRarity) != 0) return false;

        BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable().set(position).move(-3, -2, -3);
        //Bumblezone.LOGGER.log(Level.INFO, "Bee Dungeon at X: "+position.getX() +", "+position.getY()+", "+position.getZ());

        return generateShell(world, blockpos$Mutable);
    }

    protected boolean generateShell(ServerWorldAccess world, BlockPos.Mutable blockpos$Mutable){

        StructureManager structureManager = world.toServerWorld().getStructureManager();
        Structure structure = structureManager.getStructureOrBlank(new Identifier(Bumblezone.MODID + ":bee_dungeon/shell"));
        if (structure == null) {
            Bumblezone.LOGGER.warn("bee_dungeon/shell NTB does not exist!");
            return false;
        }
        StructurePlacementData placementsettings = (new StructurePlacementData()).setMirror(BlockMirror.NONE).setRotation(BlockRotation.NONE).setIgnoreEntities(false).setChunkPosition(null);
        addBlocksToWorld(structure, world, blockpos$Mutable, placementsettings, 2);


        structure = structureManager.getStructureOrBlank(new Identifier(Bumblezone.MODID + ":bee_dungeon/altar"));
        if (structure == null) {
            Bumblezone.LOGGER.warn("bee_dungeon/altar NTB does not exist!");
            return false;
        }
        this.addBlocksToWorld(structure, world, blockpos$Mutable.move(0, 1, 0), placementsettings, 2);
        return true;
    }

    /**
     * Adds blocks and entities from this structure to the given world.
     */
    public void addBlocksToWorld(Structure structure, ServerWorldAccess world, BlockPos pos, StructurePlacementData placementIn, int flags) {
        StructureAccessorInvoker structureAccessor = ((StructureAccessorInvoker) structure);
        if (!structureAccessor.getBlocks().isEmpty()) {
            List<Structure.StructureBlockInfo> list = placementIn.getRandomBlockInfos(structureAccessor.getBlocks(), pos).getAll();
            if ((!list.isEmpty() || !placementIn.shouldIgnoreEntities() && !structureAccessor.getEntities().isEmpty()) && structureAccessor.getSize().getX() >= 1 && structureAccessor.getSize().getY() >= 1 && structureAccessor.getSize().getZ() >= 1) {
                BlockBox mutableboundingbox = placementIn.getBoundingBox();
                List<BlockPos> list1 = Lists.newArrayListWithCapacity(placementIn.shouldPlaceFluids() ? list.size() : 0);
                List<Pair<BlockPos, CompoundTag>> list2 = Lists.newArrayListWithCapacity(list.size());
                int x = Integer.MAX_VALUE;
                int y = Integer.MAX_VALUE;
                int z = Integer.MAX_VALUE;
                int l = Integer.MIN_VALUE;
                int i1 = Integer.MIN_VALUE;
                int j1 = Integer.MIN_VALUE;

                for (Structure.StructureBlockInfo template$blockinfo : Structure.process(world, pos, pos, placementIn, list)) {
                    BlockPos blockpos = template$blockinfo.pos;
                    if (mutableboundingbox == null || mutableboundingbox.contains(blockpos)) {
                        FluidState ifluidstate = placementIn.shouldPlaceFluids() ? world.getFluidState(blockpos) : null;
                        BlockState blockstate = template$blockinfo.state.mirror(placementIn.getMirror()).rotate(placementIn.getRotation());
                        if (template$blockinfo.tag != null) {
                            BlockEntity blockentity = world.getBlockEntity(blockpos);
                            Clearable.clear(blockentity);
                            world.setBlockState(blockpos, Blocks.BARRIER.getDefaultState(), 20);
                        }

                        //converts the blockstate template to the correct block and gets if the block can replace air or not
                        Pair<BlockState, Boolean> pair = blockConversion(world, blockpos, blockstate.getBlock(), world.getRandom());
                        blockstate = pair.getFirst();

                        if ((pair.getSecond() || world.getBlockState(blockpos).isOpaque()) && world.setBlockState(blockpos, blockstate, flags)) {
                            x = Math.min(x, blockpos.getX());
                            y = Math.min(y, blockpos.getY());
                            z = Math.min(z, blockpos.getZ());
                            l = Math.max(l, blockpos.getX());
                            i1 = Math.max(i1, blockpos.getY());
                            j1 = Math.max(j1, blockpos.getZ());
                            list2.add(Pair.of(blockpos, template$blockinfo.tag));
                            if (template$blockinfo.tag != null) {
                                BlockEntity blockentity1 = world.getBlockEntity(blockpos);
                                if (blockentity1 != null) {
                                    template$blockinfo.tag.putInt("x", blockpos.getX());
                                    template$blockinfo.tag.putInt("y", blockpos.getY());
                                    template$blockinfo.tag.putInt("z", blockpos.getZ());
                                    blockentity1.fromTag(template$blockinfo.state, template$blockinfo.tag);
                                    blockentity1.applyMirror(placementIn.getMirror());
                                    blockentity1.applyRotation(placementIn.getRotation());
                                }
                            }

                            if (ifluidstate != null && blockstate.getBlock() instanceof FluidFillable) {
                                ((FluidFillable) blockstate.getBlock()).tryFillWithFluid(world, blockpos, blockstate, ifluidstate);
                                if (!ifluidstate.isStill()) {
                                    list1.add(blockpos);
                                }
                            }
                        }
                    }
                }

                boolean flag = true;
                Direction[] adirection = new Direction[]{Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

                while (flag && !list1.isEmpty()) {
                    flag = false;
                    Iterator<BlockPos> iterator = list1.iterator();

                    while (iterator.hasNext()) {
                        BlockPos blockpos2 = iterator.next();
                        BlockPos blockpos3 = blockpos2;
                        FluidState ifluidstate2 = world.getFluidState(blockpos2);

                        for (int k1 = 0; k1 < adirection.length && !ifluidstate2.isStill(); ++k1) {
                            BlockPos blockpos1 = blockpos3.offset(adirection[k1]);
                            FluidState ifluidstate1 = world.getFluidState(blockpos1);
                            if (ifluidstate1.getHeight(world, blockpos1) > ifluidstate2.getHeight(world, blockpos3) || ifluidstate1.isStill() && !ifluidstate2.isStill()) {
                                ifluidstate2 = ifluidstate1;
                                blockpos3 = blockpos1;
                            }
                        }

                        if (ifluidstate2.isStill()) {
                            BlockState blockstate2 = world.getBlockState(blockpos2);
                            Block block = blockstate2.getBlock();
                            if (block instanceof FluidFillable) {
                                ((FluidFillable) block).tryFillWithFluid(world, blockpos2, blockstate2, ifluidstate2);
                                flag = true;
                                iterator.remove();
                            }
                        }
                    }
                }

                if (x <= l) {
                    if (!placementIn.shouldUpdateNeighbors()) {
                        VoxelSet voxelshapepart = new BitSetVoxelSet(l - x + 1, i1 - y + 1, j1 - z + 1);

                        setVoxelShapeParts(world, flags, list2, x, y, z, voxelshapepart);
                    }

                    placeBlocks(world, placementIn, flags, list2);
                }


                if (!placementIn.shouldIgnoreEntities()) {
                    structureAccessor.invokeSpawnEntities(world,
                            pos,
                            placementIn.getMirror(),
                            placementIn.getRotation(),
                            placementIn.getPosition(),
                            placementIn.getBoundingBox(),
                            placementIn.method_27265());
                }

            }
        }
    }

    protected static void placeBlocks(ServerWorldAccess world, StructurePlacementData placementIn, int flags, List<Pair<BlockPos, CompoundTag>> list2) {
        for (Pair<BlockPos, CompoundTag> pair : list2) {
            BlockPos blockpos4 = pair.getFirst();
            if (!placementIn.shouldUpdateNeighbors()) {
                BlockState blockstate1 = world.getBlockState(blockpos4);
                BlockState blockstate3 = Block.postProcessState(blockstate1, world, blockpos4);
                if (blockstate1 != blockstate3) {
                    world.setBlockState(blockpos4, blockstate3, flags & -2 | 16);
                }

                world.updateNeighbors(blockpos4, blockstate3.getBlock());
            }

            if (pair.getSecond() != null) {
                BlockEntity blockentity2 = world.getBlockEntity(blockpos4);
                if (blockentity2 != null) {
                    blockentity2.markDirty();
                }
            }
        }
    }

    protected static void setVoxelShapeParts(ServerWorldAccess world, int flags, List<Pair<BlockPos, CompoundTag>> list2, int x, int y, int z, VoxelSet voxelshapepart) {
        for (Pair<BlockPos, CompoundTag> pair1 : list2) {
            BlockPos blockpos5 = pair1.getFirst();
            voxelshapepart.set(blockpos5.getX() - x, blockpos5.getY() - y, blockpos5.getZ() - z, true, true);
        }

        Structure.updateCorner(world, flags, voxelshapepart, x, y, z);
    }


    /**
     * Converts the incoming block to the blockstate needed
     *
     * @return - a pair of the blockstate to use and whether this block can replace air
     */
    private static Pair<BlockState, Boolean> blockConversion(ServerWorldAccess world, BlockPos pos, Block block, Random random) {
        //////////////////////////////////////////////
        //Shell

        //main body
        if (block == Blocks.RED_TERRACOTTA || block == Blocks.PURPLE_TERRACOTTA) {

            if (random.nextFloat() < 0.4f) {
                return new Pair<>(Blocks.HONEYCOMB_BLOCK.getDefaultState(), false);
            } else {
                return new Pair<>(BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState(), false);
            }
        }

        //south wall
        else if (block == Blocks.ORANGE_TERRACOTTA) {
            if (random.nextFloat() < 0.6f) {
                return new Pair<>(BzBlocks.HONEYCOMB_BROOD.getDefaultState()
                        .with(HoneycombBrood.STAGE, random.nextInt(3))
                        .with(HoneycombBrood.FACING, Direction.SOUTH),
                        false);
            } else if (random.nextFloat() < 0.2f) {
                return new Pair<>(Blocks.HONEY_BLOCK.getDefaultState(), false);
            } else {
                return new Pair<>(BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState(), false);
            }
        }

        //west wall
        else if (block == Blocks.YELLOW_TERRACOTTA) {
            if (random.nextFloat() < 0.6f) {
                return new Pair<>(BzBlocks.HONEYCOMB_BROOD.getDefaultState()
                        .with(HoneycombBrood.STAGE, random.nextInt(3))
                        .with(HoneycombBrood.FACING, Direction.WEST),
                        false);
            } else if (random.nextFloat() < 0.2f) {
                return new Pair<>(Blocks.HONEY_BLOCK.getDefaultState(), false);
            } else {
                return new Pair<>(BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState(), false);
            }
        }

        //north wall
        else if (block == Blocks.LIME_TERRACOTTA) {
            if (random.nextFloat() < 0.6f) {
                return new Pair<>(BzBlocks.HONEYCOMB_BROOD.getDefaultState()
                        .with(HoneycombBrood.STAGE, random.nextInt(3))
                        .with(HoneycombBrood.FACING, Direction.NORTH),
                        false);
            } else if (random.nextFloat() < 0.2f) {
                return new Pair<>(Blocks.HONEY_BLOCK.getDefaultState(), false);
            } else {
                return new Pair<>(BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState(), false);
            }
        }

        //east wall
        else if (block == Blocks.BLUE_TERRACOTTA) {
            if (random.nextFloat() < 0.6f) {
                return new Pair<>(BzBlocks.HONEYCOMB_BROOD.getDefaultState()
                        .with(HoneycombBrood.STAGE, random.nextInt(3))
                        .with(HoneycombBrood.FACING, Direction.EAST),
                        false);
            } else if (random.nextFloat() < 0.2f) {
                return new Pair<>(Blocks.HONEY_BLOCK.getDefaultState(), false);
            } else {
                return new Pair<>(BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState(), false);
            }
        }

        //sugar water stream
        else if (block == BzBlocks.SUGAR_WATER_BLOCK) {
            if (random.nextFloat() < 0.1f && HONEY_CRYSTAL.canPlaceAt(world, pos)) {
                return new Pair<>(HONEY_CRYSTAL.with(HoneyCrystal.WATERLOGGED, true), false);
            } else {
                return new Pair<>(block.getDefaultState(), false);
            }
        }

        //Shell
        //////////////////////////////////////////////
        //Altar


        //base
        else if (block == Blocks.GREEN_TERRACOTTA) {
            boolean replaceAir = false;
            if (world.getBlockState(pos.up()).getMaterial() != Material.AIR && !world.getBlockState(pos.up()).isOpaque())
                replaceAir = true;

            if (random.nextFloat() < 0.4f) {
                return new Pair<>(Blocks.HONEYCOMB_BLOCK.getDefaultState(), replaceAir);
            } else {
                return new Pair<>(BzBlocks.FILLED_POROUS_HONEYCOMB.getDefaultState(), replaceAir);
            }
        }

        //outer ring
        else if (block == Blocks.GRAY_TERRACOTTA) {
            if (random.nextFloat() < 0.4f && HONEY_CRYSTAL.canPlaceAt(world, pos)) {
                return new Pair<>(HONEY_CRYSTAL, true);
            } else {
                if (random.nextFloat() < 0.2f && HONEY_CRYSTAL.canPlaceAt(world, pos)) {
                    return new Pair<>(HONEY_CRYSTAL, true);
                } else {
                    return new Pair<>(Blocks.CAVE_AIR.getDefaultState(), false);
                }
            }
        }

        //inner ring
        else if (block == Blocks.CYAN_TERRACOTTA) {
            if (random.nextFloat() < 0.35f && HONEY_CRYSTAL.canPlaceAt(world, pos)) {
                return new Pair<>(HONEY_CRYSTAL, true);
            } else {
                return new Pair<>(Blocks.CAVE_AIR.getDefaultState(), false);
            }
        }

        //center
        else if (block == Blocks.BLACK_TERRACOTTA) {
            if (random.nextFloat() < 0.6f && HONEY_CRYSTAL.canPlaceAt(world, pos)) {
                return new Pair<>(HONEY_CRYSTAL, true);
            } else {
                return new Pair<>(Blocks.CAVE_AIR.getDefaultState(), false);
            }
        }

        //Altar
        //////////////////////////////////////////////
        //Misc/air

        return new Pair<>(Blocks.CAVE_AIR.getDefaultState(), false);
    }

}
