package com.telepathicgrunt.the_bumblezone.features;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.BzBlocks;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.fluids.BzFluids;
import com.telepathicgrunt.the_bumblezone.mixin.TemplateInvoker;
import com.telepathicgrunt.the_bumblezone.modCompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modCompat.ProductiveBeesRedirection;
import com.telepathicgrunt.the_bumblezone.modCompat.ResourcefulBeesRedirection;
import cy.jdkdigital.productivebees.common.tileentity.CombBlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.IClearable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.BitSetVoxelShapePart;
import net.minecraft.util.math.shapes.VoxelShapePart;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;

import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class SpiderInfestedBeeDungeon extends BeeDungeon{
    private static final BlockState HONEY_CRYSTAL = BzBlocks.HONEY_CRYSTAL.get().getDefaultState();

    public SpiderInfestedBeeDungeon(Codec<NoFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean generate(ISeedReader world, ChunkGenerator generator, Random random, BlockPos position, NoFeatureConfig config) {
        //affect rarity
        if (Bumblezone.BzDungeonsConfig.spiderInfestedBeeDungeonRarity.get() >= 1000 ||
            random.nextInt(Bumblezone.BzDungeonsConfig.spiderInfestedBeeDungeonRarity.get()) != 0) return false;

        BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable().setPos(position).move(-6, -2, -6);
        //Bumblezone.LOGGER.log(Level.INFO, "Bee Dungeon at X: "+position.getX() +", "+position.getY()+", "+position.getZ());

        boolean generated = generateShell(world, blockpos$Mutable);

        if(generated){
            for(int x = -8; x <= 12; x++) {
                for(int y = -6; y <= 10; y++) {
                    for(int z = -8; z <= 12; z++) {
                        blockpos$Mutable.setPos(position).move(x, y, z);
                        if(random.nextFloat() < 0.07f && world.getBlockState(blockpos$Mutable).getBlock() == Blocks.CAVE_AIR) {
                            world.setBlockState(blockpos$Mutable, Blocks.COBWEB.getDefaultState(), 3);
                        }
                    }
                }
            }
        }

        return generated;
    }

    /**
     * Adds blocks and entities from this structure to the given world.
     */
    @Override
    public void addBlocksToWorld(Template structure, IServerWorld world, BlockPos pos, PlacementSettings placementIn, int flags) {
        TemplateInvoker structureAccessor = ((TemplateInvoker) structure);
        if (!structureAccessor.getBlocks().isEmpty()) {
            List<Template.BlockInfo> list = placementIn.getRandomBlockInfos(structureAccessor.getBlocks(), pos).getAll();
            if ((!list.isEmpty() || !placementIn.getIgnoreEntities() && !structureAccessor.getEntities().isEmpty()) && structureAccessor.getSize().getX() >= 1 && structureAccessor.getSize().getY() >= 1 && structureAccessor.getSize().getZ() >= 1) {
                MutableBoundingBox mutableboundingbox = placementIn.getBoundingBox();
                List<BlockPos> list1 = Lists.newArrayListWithCapacity(placementIn.func_204763_l() ? list.size() : 0);
                List<Pair<BlockPos, CompoundNBT>> list2 = Lists.newArrayListWithCapacity(list.size());
                int x = Integer.MAX_VALUE;
                int y = Integer.MAX_VALUE;
                int z = Integer.MAX_VALUE;
                int l = Integer.MIN_VALUE;
                int i1 = Integer.MIN_VALUE;
                int j1 = Integer.MIN_VALUE;

                for (Template.BlockInfo template$blockinfo : Template.process(world, pos, pos, placementIn, list)) {
                    BlockPos blockpos = template$blockinfo.pos;
                    if (mutableboundingbox == null || mutableboundingbox.isVecInside(blockpos)) {
                        FluidState ifluidstate = placementIn.func_204763_l() ? world.getFluidState(blockpos) : null;
                        BlockState blockstate = template$blockinfo.state.mirror(placementIn.getMirror()).rotate(placementIn.getRotation());
                        if (template$blockinfo.nbt != null) {
                            TileEntity blockentity = world.getTileEntity(blockpos);
                            IClearable.clearObj(blockentity);
                            world.setBlockState(blockpos, Blocks.BARRIER.getDefaultState(), 20);
                        }

                        //converts the blockstate template to the correct block and gets if the block can replace air or not
                        Pair<Pair<BlockState, String>, Boolean> pair = blockConversion(world, blockpos, blockstate.getBlock(), world.getRandom());
                        blockstate = pair.getFirst().getFirst();
                        String type = pair.getFirst().getSecond();

                        if ((pair.getSecond() || world.getBlockState(blockpos).isSolid()) &&
                                world.setBlockState(blockpos, blockstate, flags)) {

                            if (blockstate.getBlock() == Blocks.SPAWNER) {
                                TileEntity blockentity = world.getTileEntity(blockpos);

                                if (blockentity instanceof MobSpawnerTileEntity) {
                                    ((MobSpawnerTileEntity) blockentity).getSpawnerBaseLogic()
                                            .setEntityType(world.getRandom().nextFloat() < 0.2f ? EntityType.CAVE_SPIDER : EntityType.SPIDER);
                                }
                            }

                            if(type != null){
                                TileEntity tileentity = world.getTileEntity(blockpos);
                                if(tileentity instanceof CombBlockTileEntity){
                                    ((CombBlockTileEntity) tileentity).setType(type);
                                }
                            }

                            x = Math.min(x, blockpos.getX());
                            y = Math.min(y, blockpos.getY());
                            z = Math.min(z, blockpos.getZ());
                            l = Math.max(l, blockpos.getX());
                            i1 = Math.max(i1, blockpos.getY());
                            j1 = Math.max(j1, blockpos.getZ());
                            list2.add(Pair.of(blockpos, template$blockinfo.nbt));
                            if (template$blockinfo.nbt != null) {
                                TileEntity blockentity1 = world.getTileEntity(blockpos);
                                if (blockentity1 != null) {
                                    template$blockinfo.nbt.putInt("x", blockpos.getX());
                                    template$blockinfo.nbt.putInt("y", blockpos.getY());
                                    template$blockinfo.nbt.putInt("z", blockpos.getZ());
                                    blockentity1.fromTag(template$blockinfo.state, template$blockinfo.nbt);
                                    blockentity1.mirror(placementIn.getMirror());
                                    blockentity1.rotate(placementIn.getRotation());
                                }
                            }

                            if (ifluidstate != null && blockstate.getBlock() instanceof ILiquidContainer) {
                                ((ILiquidContainer) blockstate.getBlock()).receiveFluid(world, blockpos, blockstate, ifluidstate);
                                if (!ifluidstate.isSource()) {
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

                        for (int k1 = 0; k1 < adirection.length && !ifluidstate2.isSource(); ++k1) {
                            BlockPos blockpos1 = blockpos3.offset(adirection[k1]);
                            FluidState ifluidstate1 = world.getFluidState(blockpos1);
                            if (ifluidstate1.getActualHeight(world, blockpos1) > ifluidstate2.getActualHeight(world, blockpos3) || ifluidstate1.isSource() && !ifluidstate2.isSource()) {
                                ifluidstate2 = ifluidstate1;
                                blockpos3 = blockpos1;
                            }
                        }

                        if (ifluidstate2.isSource()) {
                            BlockState blockstate2 = world.getBlockState(blockpos2);
                            Block block = blockstate2.getBlock();
                            if (block instanceof ILiquidContainer) {
                                ((ILiquidContainer) block).receiveFluid(world, blockpos2, blockstate2, ifluidstate2);
                                flag = true;
                                iterator.remove();
                            }
                        }
                    }
                }

                if (x <= l) {
                    if (!placementIn.func_215218_i()) {
                        VoxelShapePart voxelshapepart = new BitSetVoxelShapePart(l - x + 1, i1 - y + 1, j1 - z + 1);
                        
                        BeeDungeon.setVoxelShapeParts(world, flags, list2, x, y, z, voxelshapepart);
                    }

                    BeeDungeon.placeBlocks(world, placementIn, flags, list2);
                }


                if (!placementIn.getIgnoreEntities()) {
                    structureAccessor.invokeSpawnEntities(world,
                            pos,
                            placementIn);
                }

            }
        }
    }


    /**
     * Converts the incoming block to the blockstate needed
     *
     * @return - a pair of the blockstate to use and whether this block can replace air
     */
    protected static Pair<Pair<BlockState, String>, Boolean> blockConversion(IServerWorld world, BlockPos pos, Block block, Random random) {
        //////////////////////////////////////////////
        //Shell

        //main body
        if (block == Blocks.RED_TERRACOTTA || block == Blocks.PURPLE_TERRACOTTA) {

            if(ModChecker.productiveBeesPresent){
                if(random.nextFloat() < Bumblezone.BzModCompatibilityConfig.PBOreHoneycombSpawnRateSpiderBeeDungeon.get())
                {
                    return new Pair<>(ProductiveBeesRedirection.PBGetRandomHoneycomb(random,
                            Bumblezone.BzModCompatibilityConfig.PBGreatHoneycombRaritySpiderBeeDungeon.get()), false);
                }
                else if(Bumblezone.BzModCompatibilityConfig.spawnProductiveBeesHoneycombVariants.get() &&
                        random.nextFloat() < 0.5f)
                {
                    return new Pair<>(ProductiveBeesRedirection.PBGetRottenedHoneycomb(random), false);
                }
            }

            if(ModChecker.resourcefulBeesPresent){
                if(random.nextFloat() < Bumblezone.BzModCompatibilityConfig.RBOreHoneycombSpawnRateSpiderBeeDungeon.get())
                {
                    return new Pair<>(ResourcefulBeesRedirection.RBGetRandomHoneycomb(random,
                            Bumblezone.BzModCompatibilityConfig.RBGreatHoneycombRaritySpiderBeeDungeon.get()), false);
                }
                else if(Bumblezone.BzModCompatibilityConfig.spawnProductiveBeesHoneycombVariants.get() &&
                        random.nextFloat() < 0.5f)
                {
                    return new Pair<>(ResourcefulBeesRedirection.RBGetSpiderHoneycomb(random), false);
                }
            }



            if (random.nextFloat() < 0.15f) {
                return new Pair<>(new Pair<>(Blocks.HONEYCOMB_BLOCK.getDefaultState(), null), false);
            } else {
                return new Pair<>(new Pair<>(BzBlocks.POROUS_HONEYCOMB.get().getDefaultState(), null), false);
            }
        }

        //south wall
        else if (block == Blocks.ORANGE_TERRACOTTA) {
            if (random.nextFloat() < 0.6f) {
                return new Pair<>(new Pair<>(BzBlocks.EMPTY_HONEYCOMB_BROOD.get().getDefaultState()
                        .with(HoneycombBrood.FACING, Direction.SOUTH), null),
                        false);
            } else if (random.nextDouble() < Bumblezone.BzDungeonsConfig.spawnerRateSpiderBeeDungeon.get()) {
                return new Pair<>(new Pair<>(Blocks.SPAWNER.getDefaultState(), null), false);
            } else if(ModChecker.productiveBeesPresent &&
                    Bumblezone.BzModCompatibilityConfig.spawnProductiveBeesHoneycombVariants.get() &&
                    random.nextFloat() < 0.5f) {
                return new Pair<>(ProductiveBeesRedirection.PBGetRottenedHoneycomb(random), false);
            } else if(ModChecker.resourcefulBeesPresent &&
                    Bumblezone.BzModCompatibilityConfig.spawnResourcefulBeesHoneycombVariants.get() &&
                    random.nextFloat() < 0.5f) {
                return new Pair<>(ResourcefulBeesRedirection.RBGetSpiderHoneycomb(random), false);
            } else {
                return new Pair<>(new Pair<>(BzBlocks.POROUS_HONEYCOMB.get().getDefaultState(), null), false);
            }
        }

        //west wall
        else if (block == Blocks.YELLOW_TERRACOTTA) {
            if (random.nextFloat() < 0.6f) {
                return new Pair<>(new Pair<>(BzBlocks.EMPTY_HONEYCOMB_BROOD.get().getDefaultState()
                        .with(HoneycombBrood.FACING, Direction.WEST), null),
                        false);
            } else if (random.nextDouble() < Bumblezone.BzDungeonsConfig.spawnerRateSpiderBeeDungeon.get()) {
                return new Pair<>(new Pair<>(Blocks.SPAWNER.getDefaultState(), null), false);
            } else if(ModChecker.productiveBeesPresent &&
                    Bumblezone.BzModCompatibilityConfig.spawnProductiveBeesHoneycombVariants.get() &&
                    random.nextFloat() < 0.5f) {
                return new Pair<>(ProductiveBeesRedirection.PBGetRottenedHoneycomb(random), false);
            } else if(ModChecker.resourcefulBeesPresent &&
                    Bumblezone.BzModCompatibilityConfig.spawnResourcefulBeesHoneycombVariants.get() &&
                    random.nextFloat() < 0.5f) {
                return new Pair<>(ResourcefulBeesRedirection.RBGetSpiderHoneycomb(random), false);
            } else {
                return new Pair<>(new Pair<>(BzBlocks.POROUS_HONEYCOMB.get().getDefaultState(), null), false);
            }
        }

        //north wall
        else if (block == Blocks.LIME_TERRACOTTA) {
            if (random.nextFloat() < 0.6f) {
                return new Pair<>(new Pair<>(BzBlocks.EMPTY_HONEYCOMB_BROOD.get().getDefaultState()
                        .with(HoneycombBrood.FACING, Direction.NORTH), null),
                        false);
            } else if (random.nextDouble() < Bumblezone.BzDungeonsConfig.spawnerRateSpiderBeeDungeon.get()) {
                return new Pair<>(new Pair<>(Blocks.SPAWNER.getDefaultState(), null), false);
            } else if(ModChecker.productiveBeesPresent &&
                    Bumblezone.BzModCompatibilityConfig.spawnProductiveBeesHoneycombVariants.get() &&
                    random.nextFloat() < 0.5f) {
                return new Pair<>(ProductiveBeesRedirection.PBGetRottenedHoneycomb(random), false);
            } else if(ModChecker.resourcefulBeesPresent &&
                    Bumblezone.BzModCompatibilityConfig.spawnResourcefulBeesHoneycombVariants.get() &&
                    random.nextFloat() < 0.5f) {
                return new Pair<>(ResourcefulBeesRedirection.RBGetSpiderHoneycomb(random), false);
            } else {
                return new Pair<>(new Pair<>(BzBlocks.POROUS_HONEYCOMB.get().getDefaultState(), null), false);
            }
        }

        //east wall
        else if (block == Blocks.BLUE_TERRACOTTA) {
            if (random.nextFloat() < 0.6f) {
                return new Pair<>(new Pair<>(BzBlocks.EMPTY_HONEYCOMB_BROOD.get().getDefaultState()
                        .with(HoneycombBrood.FACING, Direction.EAST), null),
                        false);
            } else if (random.nextDouble() < Bumblezone.BzDungeonsConfig.spawnerRateSpiderBeeDungeon.get()) {
                return new Pair<>(new Pair<>(Blocks.SPAWNER.getDefaultState(), null), false);
            } else if(ModChecker.productiveBeesPresent &&
                    Bumblezone.BzModCompatibilityConfig.spawnProductiveBeesHoneycombVariants.get() &&
                    random.nextFloat() < 0.5f) {
                return new Pair<>(ProductiveBeesRedirection.PBGetRottenedHoneycomb(random), false);
            } else if(ModChecker.resourcefulBeesPresent &&
                    Bumblezone.BzModCompatibilityConfig.spawnResourcefulBeesHoneycombVariants.get() &&
                    random.nextFloat() < 0.5f) {
                return new Pair<>(ResourcefulBeesRedirection.RBGetSpiderHoneycomb(random), false);
            } else {
                return new Pair<>(new Pair<>(BzBlocks.POROUS_HONEYCOMB.get().getDefaultState(), null), false);
            }
        }

        //sugar water stream
        else if (block == BzFluids.SUGAR_WATER_BLOCK.get()) {
            return new Pair<>(new Pair<>(Blocks.CAVE_AIR.getDefaultState(), null), false);
        }

        //air
        else if (block.getDefaultState().getMaterial() == Material.AIR) {
            if (random.nextFloat() < 0.07f)
                return new Pair<>(new Pair<>(Blocks.COBWEB.getDefaultState(), null), false);
        }

        //Shell
        //////////////////////////////////////////////
        //Altar


        //base
        else if (block == Blocks.GREEN_TERRACOTTA) {
            boolean replaceAir = false;
            if (world.getBlockState(pos.up()).getMaterial() != Material.AIR && !world.getBlockState(pos.up()).isSolid())
                replaceAir = true;

            if(ModChecker.productiveBeesPresent &&
                    random.nextFloat() < Bumblezone.BzModCompatibilityConfig.PBOreHoneycombSpawnRateSpiderBeeDungeon.get())
            {
                return new Pair<>(ProductiveBeesRedirection.PBGetRandomHoneycomb(random,
                        Bumblezone.BzModCompatibilityConfig.PBGreatHoneycombRaritySpiderBeeDungeon.get()), replaceAir);
            }

            if (random.nextFloat() < 0.15f) {
                return new Pair<>(new Pair<>(Blocks.HONEYCOMB_BLOCK.getDefaultState(), null), replaceAir);
            } else {
                return new Pair<>(new Pair<>(BzBlocks.POROUS_HONEYCOMB.get().getDefaultState(), null), replaceAir);
            }
        }

        //outer ring
        else if (block == Blocks.GRAY_TERRACOTTA) {
            if (random.nextFloat() < 0.07f) {
                return new Pair<>(new Pair<>(Blocks.COBWEB.getDefaultState(), null), true);
            } else if (random.nextFloat() < 0.4f && HONEY_CRYSTAL.isValidPosition(world, pos)) {
                return new Pair<>(new Pair<>(HONEY_CRYSTAL, null), true);
            } else {
                return new Pair<>(new Pair<>(Blocks.CAVE_AIR.getDefaultState(), null), false);
            }
        }

        //inner ring
        else if (block == Blocks.CYAN_TERRACOTTA) {
            if (random.nextFloat() < 0.07f) {
                return new Pair<>(new Pair<>(Blocks.COBWEB.getDefaultState(), null), true);
            } else if (random.nextFloat() < 0.3f && HONEY_CRYSTAL.isValidPosition(world, pos)) {
                return new Pair<>(new Pair<>(HONEY_CRYSTAL, null), true);
            } else {
                return new Pair<>(new Pair<>(Blocks.CAVE_AIR.getDefaultState(), null), false);
            }
        }

        //center
        else if (block == Blocks.BLACK_TERRACOTTA) {
            if (random.nextFloat() < 0.07f) {
                return new Pair<>(new Pair<>(Blocks.COBWEB.getDefaultState(), null), true);
            } else if (random.nextFloat() < 0.4f && HONEY_CRYSTAL.isValidPosition(world, pos)) {
                return new Pair<>(new Pair<>(HONEY_CRYSTAL, null), true);
            } else {
                return new Pair<>(new Pair<>(Blocks.CAVE_AIR.getDefaultState(), null), false);
            }
        }

        //Altar
        //////////////////////////////////////////////
        //Misc/air

        return new Pair<>(new Pair<>(Blocks.CAVE_AIR.getDefaultState(), null), false);
    }

}
