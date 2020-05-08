package net.telepathicgrunt.bumblezone;


import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.telepathicgrunt.bumblezone.blocks.BzBlocks;
import net.telepathicgrunt.bumblezone.blocks.StickyHoneyResidue;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        
        if (event.includeClient()) {
            generator.addProvider(new BlockStates(generator, event.getExistingFileHelper()));
        }
    }

    public static class BlockStates extends BlockStateProvider
    {
        public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper)
        {
            super(gen, Bumblezone.MODID, exFileHelper);
        }

        
        @Override
        protected void registerStatesAndModels()
        {
            ModelFile stickyHoneyResidue = models().singleTexture("sticky_honey_residue", mcLoc(Bumblezone.MODID+":block/sticky_honey_residue"), "texture", mcLoc(Bumblezone.MODID+":block/sticky_honey_residue"));
            VariantBlockStateBuilder builder = getVariantBuilder(BzBlocks.STICKY_HONEY_RESIDUE.get());
            for (final boolean down : new boolean[] { false, true} )
        	for (final boolean up : new boolean[] { false, true} )
        	    for (final boolean west : new boolean[] { false, true} )
        		for (final boolean east : new boolean[] { false, true} )
        		    for (final boolean north : new boolean[] { false, true} )
        			for (final boolean south : new boolean[] { false, true} )
                                    builder.partialState()
                                            .with(StickyHoneyResidue.DOWN, down)
                                            .with(StickyHoneyResidue.UP, up)
                                            .with(StickyHoneyResidue.NORTH, north)
                                            .with(StickyHoneyResidue.SOUTH, south)
                                            .with(StickyHoneyResidue.WEST, west)
                                            .with(StickyHoneyResidue.EAST, east)
                                            .modelForState()
                                                .modelFile(stickyHoneyResidue)
                                            .nextModel()
                                                .modelFile(stickyHoneyResidue)
                                                .uvLock(true)
                                                .weight(100)
                                            .addModel();

            BlockModelBuilder residueBody = models().getBuilder("block/residue/residue_faces");
            residueBody.texture("residue", modLoc("block/sticky_honey_residue"));
            createResidueFaces(BzBlocks.STICKY_HONEY_RESIDUE.get(), residueBody);
        }

        private void createResidueFaces(Block block, BlockModelBuilder dimCellFrame) {
            BlockModelBuilder faceModel = models().getBuilder("block/residue/residue_faces")
                    .element().from(0, 0, 0).to(16, 0.8f, 16).face(Direction.DOWN).texture("#the_bumblezone:block/sticky_honey_residue").end().end()
                    .texture("single", modLoc("block/sticky_honey_residue"));

            MultiPartBlockStateBuilder bld = getMultipartBuilder(block);

            bld.part().modelFile(dimCellFrame).addModel();

            bld.part().modelFile(faceModel).addModel().condition(StickyHoneyResidue.DOWN, true);
            bld.part().modelFile(faceModel).rotationX(180).addModel().condition(StickyHoneyResidue.UP, true);
            bld.part().modelFile(faceModel).rotationX(90).addModel().condition(StickyHoneyResidue.SOUTH, true);
            bld.part().modelFile(faceModel).rotationX(270).addModel().condition(StickyHoneyResidue.NORTH, true);
            bld.part().modelFile(faceModel).rotationY(90).rotationX(90).addModel().condition(StickyHoneyResidue.WEST, true);
            bld.part().modelFile(faceModel).rotationY(270).rotationX(90).addModel().condition(StickyHoneyResidue.EAST, true);
        }
    }
}