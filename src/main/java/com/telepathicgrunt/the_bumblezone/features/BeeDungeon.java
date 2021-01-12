package com.telepathicgrunt.the_bumblezone.features;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.telepathicgrunt.the_bumblezone.Bumblezone;
import com.telepathicgrunt.the_bumblezone.blocks.BzBlocks;
import com.telepathicgrunt.the_bumblezone.blocks.HoneyCrystal;
import com.telepathicgrunt.the_bumblezone.blocks.HoneycombBrood;
import com.telepathicgrunt.the_bumblezone.fluids.BzFluids;
import com.telepathicgrunt.the_bumblezone.mixin.TemplateInvoker;
import com.telepathicgrunt.the_bumblezone.modCompat.BuzzierBeesRedirection;
import com.telepathicgrunt.the_bumblezone.modCompat.ModChecker;
import com.telepathicgrunt.the_bumblezone.modCompat.ProductiveBeesRedirection;
import com.telepathicgrunt.the_bumblezone.modCompat.ResourcefulBeesRedirection;
import cy.jdkdigital.productivebees.common.tileentity.CombBlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.IClearable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.BitSetVoxelShapePart;
import net.minecraft.util.math.shapes.VoxelShapePart;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class BeeDungeon extends Feature<NoFeatureConfig>{

    private static final BlockState HONEY_CRYSTAL = BzBlocks.HONEY_CRYSTAL.get().getDefaultState();

    public BeeDungeon(Codec<NoFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean generate(ISeedReader world, ChunkGenerator generator, Random random, BlockPos position, NoFeatureConfig config) {
        //affect rarity
        if (Bumblezone.BzDungeonsConfig.beeDungeonRarity.get() >= 1000 ||
            random.nextInt(Bumblezone.BzDungeonsConfig.beeDungeonRarity.get()) != 0) return false;

        BlockPos.Mutable blockpos$Mutable = new BlockPos.Mutable().setPos(position).move(-6, -2, -6);
        //Bumblezone.LOGGER.log(Level.INFO, "Bee Dungeon at X: "+position.getX() +", "+position.getY()+", "+position.getZ());

        return generateShell(world, blockpos$Mutable);
    }

    protected boolean generateShell(IServerWorld world, BlockPos.Mutable blockpos$Mutable){

        TemplateManager structureManager = world.getWorld().getStructureTemplateManager();
        Template structure = structureManager.getTemplateDefaulted(new ResourceLocation(Bumblezone.MODID + ":bee_dungeon/shell"));
        PlacementSettings placementsettings = (new PlacementSettings()).setMirror(Mirror.NONE).setRotation(Rotation.NONE).setIgnoreEntities(false).setChunk(null);
        addBlocksToWorld(structure, world, blockpos$Mutable, placementsettings, 2);


        structure = structureManager.getTemplateDefaulted(new ResourceLocation(Bumblezone.MODID + ":bee_dungeon/altar"));
        this.addBlocksToWorld(structure, world, blockpos$Mutable.move(0, 1, 0), placementsettings, 2);
        return true;
    }

    /**
     * Adds blocks and entities from this structure to the given world.
     */
    public void addBlocksToWorld(Template structure, IServerWorld world, BlockPos pos, PlacementSettings placementIn, int flags) {
        TemplateInvoker structureAccessor = ((TemplateInvoker) structure);
        if (!structureAccessor.bz_getBlocks().isEmpty()) {
            List<Template.BlockInfo> list = placementIn.getRandomBlockInfos(structureAccessor.bz_getBlocks(), pos).getAll();
            if ((!list.isEmpty() || !placementIn.getIgnoreEntities() && !structureAccessor.bz_getEntities().isEmpty()) && structureAccessor.bz_getSize().getX() >= 1 && structureAccessor.bz_getSize().getY() >= 1 && structureAccessor.bz_getSize().getZ() >= 1) {
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

                        setVoxelShapeParts(world, flags, list2, x, y, z, voxelshapepart);
                    }

                    placeBlocks(world, placementIn, flags, list2);
                }


                if (!placementIn.getIgnoreEntities()) {
                    structureAccessor.bz_invokeSpawnEntities(world,
                            pos,
                            placementIn);
                }
            }
        }
    }

    protected static void placeBlocks(IServerWorld world, PlacementSettings placementIn, int flags, List<Pair<BlockPos, CompoundNBT>> list2) {
        for (Pair<BlockPos, CompoundNBT> pair : list2) {
            BlockPos blockpos4 = pair.getFirst();
            if (!placementIn.func_215218_i()) {
                BlockState blockstate1 = world.getBlockState(blockpos4);
                BlockState blockstate3 = Block.getValidBlockForPosition(blockstate1, world, blockpos4);
                if (blockstate1 != blockstate3) {
                    world.setBlockState(blockpos4, blockstate3, flags & -2 | 16);
                }

                world.updateNeighbors(blockpos4, blockstate3.getBlock());
            }

            if (pair.getSecond() != null) {
                TileEntity blockentity2 = world.getTileEntity(blockpos4);
                if (blockentity2 != null) {
                    blockentity2.markDirty();
                }
            }
        }
    }

    protected static void setVoxelShapeParts(IServerWorld world, int flags, List<Pair<BlockPos, CompoundNBT>> list2, int x, int y, int z, VoxelShapePart voxelshapepart) {
        for (Pair<BlockPos, CompoundNBT> pair1 : list2) {
            BlockPos blockpos5 = pair1.getFirst();
            voxelshapepart.setFilled(blockpos5.getX() - x, blockpos5.getY() - y, blockpos5.getZ() - z, true, true);
        }

        Template.func_222857_a(world, flags, voxelshapepart, x, y, z);
    }


    /**
     * Converts the incoming block to the blockstate needed
     *
     * @return - a pair of the blockstate to use and whether this block can replace air
     */
    private static Pair<Pair<BlockState, String>, Boolean> blockConversion(IServerWorld world, BlockPos pos, Block block, Random random) {
        //////////////////////////////////////////////
        //Shell

        //main body
        if (block == Blocks.RED_TERRACOTTA || block == Blocks.PURPLE_TERRACOTTA) {
            if(ModChecker.productiveBeesPresent &&
                random.nextFloat() < Bumblezone.BzModCompatibilityConfig.PBOreHoneycombSpawnRateBeeDungeon.get())
            {
                return new Pair<>(ProductiveBeesRedirection.PBGetRandomHoneycomb(random,
                        Bumblezone.BzModCompatibilityConfig.PBGreatHoneycombRarityBeeDungeon.get()), false);
            }

            if(ModChecker.resourcefulBeesPresent &&
                    random.nextFloat() < Bumblezone.BzModCompatibilityConfig.RBOreHoneycombSpawnRateBeeDungeon.get())
            {
                return new Pair<>(ResourcefulBeesRedirection.RBGetRandomHoneycomb(random,
                        Bumblezone.BzModCompatibilityConfig.RBGreatHoneycombRarityBeeDungeon.get()), false);
            }

            if (random.nextFloat() < 0.4f) {
                return new Pair<>(new Pair<>(Blocks.HONEYCOMB_BLOCK.getDefaultState(), null), false);
            }
            else {
                return new Pair<>(new Pair<>(BzBlocks.FILLED_POROUS_HONEYCOMB.get().getDefaultState(), null), false);
            }
        }

        //south wall
        else if (block == Blocks.ORANGE_TERRACOTTA) {
            if (random.nextFloat() < 0.6f) {
                return new Pair<>(new Pair<>(BzBlocks.HONEYCOMB_BROOD.get().getDefaultState()
                        .with(HoneycombBrood.STAGE, random.nextInt(3))
                        .with(HoneycombBrood.FACING, Direction.SOUTH), null),
                        false);
            }
            else if (random.nextFloat() < 0.2f) {
                return new Pair<>(new Pair<>(Blocks.HONEY_BLOCK.getDefaultState(), null), false);
            }
            else {
                return new Pair<>(new Pair<>(BzBlocks.FILLED_POROUS_HONEYCOMB.get().getDefaultState(), null), false);
            }
        }

        //west wall
        else if (block == Blocks.YELLOW_TERRACOTTA) {
            if (random.nextFloat() < 0.6f) {
                return new Pair<>(new Pair<>(BzBlocks.HONEYCOMB_BROOD.get().getDefaultState()
                        .with(HoneycombBrood.STAGE, random.nextInt(3))
                        .with(HoneycombBrood.FACING, Direction.WEST), null),
                        false);
            }
            else if (random.nextFloat() < 0.2f) {
                return new Pair<>(new Pair<>(Blocks.HONEY_BLOCK.getDefaultState(), null), false);
            }
            else {
                return new Pair<>(new Pair<>(BzBlocks.FILLED_POROUS_HONEYCOMB.get().getDefaultState(), null), false);
            }
        }

        //north wall
        else if (block == Blocks.LIME_TERRACOTTA) {
            if (random.nextFloat() < 0.6f) {
                return new Pair<>(new Pair<>(BzBlocks.HONEYCOMB_BROOD.get().getDefaultState()
                        .with(HoneycombBrood.STAGE, random.nextInt(3))
                        .with(HoneycombBrood.FACING, Direction.NORTH), null),
                        false);
            }
            else if (random.nextFloat() < 0.2f) {
                return new Pair<>(new Pair<>(Blocks.HONEY_BLOCK.getDefaultState(), null), false);
            }
            else {
                return new Pair<>(new Pair<>(BzBlocks.FILLED_POROUS_HONEYCOMB.get().getDefaultState(), null), false);
            }
        }

        //east wall
        else if (block == Blocks.BLUE_TERRACOTTA) {
            if (random.nextFloat() < 0.6f) {
                return new Pair<>(new Pair<>(BzBlocks.HONEYCOMB_BROOD.get().getDefaultState()
                        .with(HoneycombBrood.STAGE, random.nextInt(3))
                        .with(HoneycombBrood.FACING, Direction.EAST), null),
                        false);
            }
            else if (random.nextFloat() < 0.2f) {
                return new Pair<>(new Pair<>(Blocks.HONEY_BLOCK.getDefaultState(), null), false);
            }
            else {
                return new Pair<>(new Pair<>(BzBlocks.FILLED_POROUS_HONEYCOMB.get().getDefaultState(), null), false);
            }
        }

        //sugar water stream
        else if (block == BzFluids.SUGAR_WATER_BLOCK.get()) {
            if (random.nextFloat() < 0.1f && HONEY_CRYSTAL.isValidPosition(world, pos)) {
                return new Pair<>(new Pair<>(HONEY_CRYSTAL.with(HoneyCrystal.WATERLOGGED, true), null), false);
            }
            else {
                return new Pair<>(new Pair<>(block.getDefaultState(), null), false);
            }
        }

        //Shell
        //////////////////////////////////////////////
        //Altar


        //base
        else if (block == Blocks.GREEN_TERRACOTTA) {
            boolean replaceAir = false;
            if (world.getBlockState(pos.up()).getMaterial() != Material.AIR && !world.getBlockState(pos.up()).isSolid()){
                replaceAir = true;
            }

            if(ModChecker.productiveBeesPresent &&
                    random.nextFloat() < Bumblezone.BzModCompatibilityConfig.PBOreHoneycombSpawnRateBeeDungeon.get())
            {
                return new Pair<>(ProductiveBeesRedirection.PBGetRandomHoneycomb(random,
                        Bumblezone.BzModCompatibilityConfig.PBGreatHoneycombRarityBeeDungeon.get()), replaceAir);
            }

            if(ModChecker.resourcefulBeesPresent &&
                    random.nextFloat() < Bumblezone.BzModCompatibilityConfig.RBOreHoneycombSpawnRateBeeDungeon.get())
            {
                return new Pair<>(ResourcefulBeesRedirection.RBGetRandomHoneycomb(random,
                        Bumblezone.BzModCompatibilityConfig.RBGreatHoneycombRarityBeeDungeon.get()), replaceAir);
            }

            if (random.nextFloat() < 0.4f) {
                return new Pair<>(new Pair<>(Blocks.HONEYCOMB_BLOCK.getDefaultState(), null), replaceAir);
            }
            else {
                return new Pair<>(new Pair<>(BzBlocks.FILLED_POROUS_HONEYCOMB.get().getDefaultState(), null), replaceAir);
            }
        }

        //outer ring
        else if (block == Blocks.GRAY_TERRACOTTA) {
            if(ModChecker.buzzierBeesPresent && Bumblezone.BzModCompatibilityConfig.allowScentedCandlesBeeDungeon.get()) {
                if(random.nextFloat() < 0.25f && world.getBlockState(pos.down()).getMaterial() != Material.AIR){
                    return new Pair<>(new Pair<>(BuzzierBeesRedirection.BBGetRandomTier1Candle(
                            random,
                            random.nextInt(3)+1,
                            false,
                            true), null),
                            true);
                }
                else if(random.nextFloat() < 0.4f && HONEY_CRYSTAL.isValidPosition(world, pos)) {
                    return new Pair<>(new Pair<>(HONEY_CRYSTAL, null), true);
                }
            }
            else {
                if (random.nextFloat() < 0.6f && HONEY_CRYSTAL.isValidPosition(world, pos)) {
                    return new Pair<>(new Pair<>(HONEY_CRYSTAL, null), true);
                }
                else {
                    return new Pair<>(new Pair<>(Blocks.CAVE_AIR.getDefaultState(), null), false);
                }
            }
        }

        //inner ring
        else if (block == Blocks.CYAN_TERRACOTTA) {
            if(ModChecker.buzzierBeesPresent && Bumblezone.BzModCompatibilityConfig.allowScentedCandlesBeeDungeon.get()) {
                if(random.nextFloat() < 0.4f && world.getBlockState(pos.down()).getMaterial() != Material.AIR){
                    return new Pair<>(new Pair<>(BuzzierBeesRedirection.BBGetRandomTier2Candle(
                            random,
                            Bumblezone.BzModCompatibilityConfig.powerfulCandlesRarityBeeDungeon.get(),
                            random.nextInt(random.nextInt(3)+1)+1,
                            false,
                            true), null),
                            true);
                }
                else if(random.nextBoolean() && HONEY_CRYSTAL.isValidPosition(world, pos)) {
                    return new Pair<>(new Pair<>(HONEY_CRYSTAL, null), true);
                }
            }
            else {
                if (random.nextFloat() < 0.35f && HONEY_CRYSTAL.isValidPosition(world, pos)) {
                    return new Pair<>(new Pair<>(HONEY_CRYSTAL, null), true);
                }
                else {
                    return new Pair<>(new Pair<>(Blocks.CAVE_AIR.getDefaultState(), null), false);
                }
            }
        }

        //center
        else if (block == Blocks.BLACK_TERRACOTTA) {
            if(ModChecker.buzzierBeesPresent && Bumblezone.BzModCompatibilityConfig.allowScentedCandlesBeeDungeon.get()) {
                if(random.nextFloat() < 0.8f && world.getBlockState(pos.down()).getMaterial() != Material.AIR){
                    return new Pair<>(new Pair<>(BuzzierBeesRedirection.BBGetRandomTier3Candle(
                            random,
                            Bumblezone.BzModCompatibilityConfig.powerfulCandlesRarityBeeDungeon.get()+1,
                            random.nextInt(random.nextInt(random.nextInt(3)+1)+1)+1,
                            false,
                            true), null),
                            true);
                }
                else if(HONEY_CRYSTAL.isValidPosition(world, pos)) {
                    return new Pair<>(new Pair<>(HONEY_CRYSTAL, null), true);
                }
            }
            else {
                if (random.nextFloat() < 0.6f && HONEY_CRYSTAL.isValidPosition(world, pos)) {
                    return new Pair<>(new Pair<>(HONEY_CRYSTAL, null), true);
                }
                else {
                    return new Pair<>(new Pair<>(Blocks.CAVE_AIR.getDefaultState(), null), false);
                }
            }
        }

        //Altar
        //////////////////////////////////////////////
        //Misc/air

        return new Pair<>(new Pair<>(Blocks.CAVE_AIR.getDefaultState(), null), false);
    }

}
