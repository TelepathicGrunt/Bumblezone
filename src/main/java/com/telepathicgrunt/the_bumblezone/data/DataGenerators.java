package com.telepathicgrunt.the_bumblezone.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

public class DataGenerators {

    public static void gatherData(GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        
        if (evt.includeServer()) {
            generator.addProvider(new RecipeGenerators(generator));
        }
        
        if (evt.includeClient()) {
            generator.addProvider(new BlockModelGenerators(generator, evt.getExistingFileHelper()));
        }
    }
}
