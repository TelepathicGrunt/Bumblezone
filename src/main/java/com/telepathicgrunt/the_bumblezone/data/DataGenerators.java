package com.telepathicgrunt.the_bumblezone.data;

import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

public class DataGenerators {

    public static void gatherData(GatherDataEvent evt) {
        evt.getGenerator().addProvider(new RecipeGenerators(evt.getGenerator()));
    }
}
