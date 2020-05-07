package net.telepathicgrunt.bumblezone;


import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelFile.UncheckedModelFile;
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
            ModelFile invisbleModel = new UncheckedModelFile(new ResourceLocation("builtin/generated"));
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
                                                .modelFile(invisbleModel)
                                            .nextModel()
                                                .modelFile(stickyHoneyResidue)
                                                .uvLock(true)
                                                .weight(100)
                                            .addModel();
        }
        
    }
}