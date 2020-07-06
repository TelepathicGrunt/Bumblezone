package net.telepathicgrunt.bumblezone.features;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Clearable;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.BitSetVoxelSet;
import net.minecraft.util.shape.VoxelSet;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.telepathicgrunt.bumblezone.Bumblezone;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.blocks.HoneycombBrood;
import net.telepathicgrunt.bumblezone.mixin.StructureAccessorMixin;

import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class SpiderInfestedBeeDungeon extends Feature<DefaultFeatureConfig> {
    private static final BlockState HONEY_CRYSTAL = BzBlocks.HONEY_CRYSTAL.getDefaultState();

    public SpiderInfestedBeeDungeon(Codec<DefaultFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean generate(ServerWorldAccess world, StructureAccessor accessor, ChunkGenerator generator, Random random, BlockPos position, DefaultFeatureConfig config) {
        //affect rarity
        if (random.nextInt(Bumblezone.BZ_CONFIG.spiderInfestedBeeDungeonRarity) != 0) return false;

        BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable().set(position).move(-6, -2, -6);
        //Bumblezone.LOGGER.log(Level.INFO, "Spider Infested Bee Dungeon at X: "+position.getX() +", "+position.getY()+", "+position.getZ());

        StructureManager structureManager = ((ServerWorld) world.getWorld()).getStructureManager();
        Structure structure = structureManager.getStructureOrBlank(new Identifier(Bumblezone.MODID + ":bee_dungeon/shell"));

        if (structure == null) {
            Bumblezone.LOGGER.warn("bee_dungeon/shell NTB does not exist!");
            return false;
        }

        StructurePlacementData placementsettings = (new StructurePlacementData()).setMirror(BlockMirror.NONE).setRotation(BlockRotation.NONE).setIgnoreEntities(false).setChunkPosition((ChunkPos) null);
        addBlocksToWorld(structure, world, blockpos$Mutable, placementsettings, 2);


        structure = structureManager.getStructureOrBlank(new Identifier(Bumblezone.MODID + ":bee_dungeon/altar"));

        if (structure == null) {
            Bumblezone.LOGGER.warn("bee_dungeon/altar NTB does not exist!");
            return false;
        }

        addBlocksToWorld(structure, world, blockpos$Mutable.move(0, 1, 0), placementsettings, 2);


        for (int x = -8; x <= 16; x++) {
            for (int y = -3; y <= 10; y++) {
                for (int z = -8; z <= 16; z++) {
                    blockpos$Mutable.set(position).move(x, y, z);
                    if (random.nextFloat() < 0.07f && world.getBlockState(blockpos$Mutable).getBlock() == Blocks.CAVE_AIR) {
                        world.setBlockState(blockpos$Mutable, Blocks.COBWEB.getDefaultState(), 3);
                    }
                }
            }
        }


        return true;

    }

    /**
     * Adds blocks and entities from this structure to the given world.
     */
    @SuppressWarnings("deprecation")
    private static boolean addBlocksToWorld(Structure structure, ServerWorldAccess world, BlockPos pos, StructurePlacementData placementIn, int flags) {
        StructureAccessorMixin structureAccessor = ((StructureAccessorMixin) (Object) structure);
        if (structureAccessor.getBlocks().isEmpty()) {
            return false;
        } else {
            List<Structure.StructureBlockInfo> list = placementIn.getRandomBlockInfos(structureAccessor.getBlocks(), pos).getAll();
            if ((!list.isEmpty() || !placementIn.shouldIgnoreEntities() && !structureAccessor.getEntities().isEmpty()) && structureAccessor.getSize().getX() >= 1 && structureAccessor.getSize().getY() >= 1 && structureAccessor.getSize().getZ() >= 1) {
                BlockBox mutableboundingbox = placementIn.getBoundingBox();
                List<BlockPos> list1 = Lists.newArrayListWithCapacity(placementIn.shouldPlaceFluids() ? list.size() : 0);
                List<Pair<BlockPos, CompoundTag>> list2 = Lists.newArrayListWithCapacity(list.size());
                int i = Integer.MAX_VALUE;
                int j = Integer.MAX_VALUE;
                int k = Integer.MAX_VALUE;
                int l = Integer.MIN_VALUE;
                int i1 = Integer.MIN_VALUE;
                int j1 = Integer.MIN_VALUE;

                for (Structure.StructureBlockInfo template$blockinfo : structure.process(world, pos, pos, placementIn, list)) {
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
                            if (blockstate.getBlock() == Blocks.SPAWNER) {
                                BlockEntity blockentity = world.getBlockEntity(blockpos);

                                if (blockentity instanceof MobSpawnerBlockEntity) {
                                    ((MobSpawnerBlockEntity) blockentity).getLogic()
                                            .setEntityId(world.getRandom().nextFloat() < 0.2f ? EntityType.CAVE_SPIDER : EntityType.SPIDER);
                                }
                            }

                            i = Math.min(i, blockpos.getX());
                            j = Math.min(j, blockpos.getY());
                            k = Math.min(k, blockpos.getZ());
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

                if (i <= l) {
                    if (!placementIn.shouldUpdateNeighbors()) {
                        VoxelSet voxelshapepart = new BitSetVoxelSet(l - i + 1, i1 - j + 1, j1 - k + 1);
                        int l1 = i;
                        int i2 = j;
                        int j2 = k;

                        for (Pair<BlockPos, CompoundTag> pair1 : list2) {
                            BlockPos blockpos5 = pair1.getFirst();
                            voxelshapepart.set(blockpos5.getX() - l1, blockpos5.getY() - i2, blockpos5.getZ() - j2, true, true);
                        }

                        Structure.updateCorner(world, flags, voxelshapepart, l1, i2, j2);
                    }

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


                if (!placementIn.shouldIgnoreEntities()) {
                    structureAccessor.invokespawnEntities(world,
                            pos,
                            placementIn.getMirror(),
                            placementIn.getRotation(),
                            placementIn.getPosition(),
                            placementIn.getBoundingBox(),
                            placementIn.method_27265());
                }

                return true;
            } else {
                return false;
            }
        }
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
            if (random.nextFloat() < 0.15f) {
                return new Pair<>(Blocks.HONEYCOMB_BLOCK.getDefaultState(), new Boolean(false));
            } else {
                return new Pair<>(BzBlocks.POROUS_HONEYCOMB.getDefaultState(), new Boolean(false));
            }
        }

        //south wall
        else if (block == Blocks.ORANGE_TERRACOTTA) {
            if (random.nextFloat() < 0.6f) {
                return new Pair<>(BzBlocks.EMPTY_HONEYCOMB_BROOD.getDefaultState()
                        .with(HoneycombBrood.FACING, Direction.SOUTH),
                        new Boolean(false));
            } else if (random.nextDouble() < Bumblezone.BZ_CONFIG.spawnerRateSpiderBeeDungeon) {
                return new Pair<>(Blocks.SPAWNER.getDefaultState(), new Boolean(false));
            } else {
                return new Pair<>(BzBlocks.POROUS_HONEYCOMB.getDefaultState(), new Boolean(false));
            }
        }

        //west wall
        else if (block == Blocks.YELLOW_TERRACOTTA) {
            if (random.nextFloat() < 0.6f) {
                return new Pair<>(BzBlocks.EMPTY_HONEYCOMB_BROOD.getDefaultState()
                        .with(HoneycombBrood.FACING, Direction.WEST),
                        new Boolean(false));
            } else if (random.nextDouble() < Bumblezone.BZ_CONFIG.spawnerRateSpiderBeeDungeon) {
                return new Pair<>(Blocks.SPAWNER.getDefaultState(), new Boolean(false));
            } else {
                return new Pair<>(BzBlocks.POROUS_HONEYCOMB.getDefaultState(), new Boolean(false));
            }
        }

        //north wall
        else if (block == Blocks.LIME_TERRACOTTA) {
            if (random.nextFloat() < 0.6f) {
                return new Pair<>(BzBlocks.EMPTY_HONEYCOMB_BROOD.getDefaultState()
                        .with(HoneycombBrood.FACING, Direction.NORTH),
                        new Boolean(false));
            } else if (random.nextDouble() < Bumblezone.BZ_CONFIG.spawnerRateSpiderBeeDungeon) {
                return new Pair<>(Blocks.SPAWNER.getDefaultState(), new Boolean(false));
            } else {
                return new Pair<>(BzBlocks.POROUS_HONEYCOMB.getDefaultState(), new Boolean(false));
            }
        }

        //east wall
        else if (block == Blocks.BLUE_TERRACOTTA) {
            if (random.nextFloat() < 0.6f) {
                return new Pair<>(BzBlocks.EMPTY_HONEYCOMB_BROOD.getDefaultState()
                        .with(HoneycombBrood.FACING, Direction.EAST),
                        new Boolean(false));
            } else if (random.nextDouble() < Bumblezone.BZ_CONFIG.spawnerRateSpiderBeeDungeon) {
                return new Pair<>(Blocks.SPAWNER.getDefaultState(), new Boolean(false));
            } else {
                return new Pair<>(BzBlocks.POROUS_HONEYCOMB.getDefaultState(), new Boolean(false));
            }
        }

        //sugar water stream
        else if (block == BzBlocks.SUGAR_WATER_BLOCK) {
            return new Pair<>(Blocks.CAVE_AIR.getDefaultState(), new Boolean(false));
        }

        //air
        else if (block.getDefaultState().getMaterial() == Material.AIR) {
            if (random.nextFloat() < 0.07f)
                return new Pair<>(Blocks.COBWEB.getDefaultState(), new Boolean(false));
        }

        //Shell
        //////////////////////////////////////////////
        //Altar


        //base
        else if (block == Blocks.GREEN_TERRACOTTA) {
            boolean replaceAir = false;
            if (world.getBlockState(pos.up()).getMaterial() != Material.AIR && !world.getBlockState(pos.up()).isOpaque())
                replaceAir = true;

            if (random.nextFloat() < 0.15f) {
                return new Pair<>(Blocks.HONEYCOMB_BLOCK.getDefaultState(), new Boolean(replaceAir));
            } else {
                return new Pair<>(BzBlocks.POROUS_HONEYCOMB.getDefaultState(), new Boolean(replaceAir));
            }
        }

        //outer ring
        else if (block == Blocks.GRAY_TERRACOTTA) {
            if (random.nextFloat() < 0.07f) {
                return new Pair<>(Blocks.COBWEB.getDefaultState(), new Boolean(true));
            } else if (random.nextFloat() < 0.4f && HONEY_CRYSTAL.canPlaceAt(world, pos)) {
                return new Pair<>(HONEY_CRYSTAL, new Boolean(true));
            } else {
                return new Pair<>(Blocks.CAVE_AIR.getDefaultState(), new Boolean(false));
            }
        }

        //inner ring
        else if (block == Blocks.CYAN_TERRACOTTA) {
            if (random.nextFloat() < 0.07f) {
                return new Pair<>(Blocks.COBWEB.getDefaultState(), new Boolean(true));
            } else if (random.nextFloat() < 0.3f && HONEY_CRYSTAL.canPlaceAt(world, pos)) {
                return new Pair<>(HONEY_CRYSTAL, new Boolean(true));
            } else {
                return new Pair<>(Blocks.CAVE_AIR.getDefaultState(), new Boolean(false));
            }
        }

        //center
        else if (block == Blocks.BLACK_TERRACOTTA) {
            if (random.nextFloat() < 0.07f) {
                return new Pair<>(Blocks.COBWEB.getDefaultState(), new Boolean(true));
            } else if (random.nextFloat() < 0.4f && HONEY_CRYSTAL.canPlaceAt(world, pos)) {
                return new Pair<>(HONEY_CRYSTAL, new Boolean(true));
            } else {
                return new Pair<>(Blocks.CAVE_AIR.getDefaultState(), new Boolean(false));
            }
        }

        //Altar
        //////////////////////////////////////////////
        //Misc/air

        return new Pair<>(Blocks.CAVE_AIR.getDefaultState(), new Boolean(false));
    }

}
