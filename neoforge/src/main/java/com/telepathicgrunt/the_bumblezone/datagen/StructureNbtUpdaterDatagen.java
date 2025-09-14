package com.telepathicgrunt.the_bumblezone.datagen;

import com.telepathicgrunt.the_bumblezone.Bumblezone;
import net.minecraft.data.DataGenerator;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

// Source: https://github.com/BluSunrize/ImmersiveEngineering/blob/1.20.1/src/datagen/java/blusunrize/immersiveengineering/data/IEDataGenerator.java
@EventBusSubscriber(modid = Bumblezone.MODID)
public class StructureNbtUpdaterDatagen {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        ResourceManager resourceManager = event.getResourceManager(PackType.SERVER_DATA);
        DataGenerator gen = event.getGenerator();
        final var output = gen.getPackOutput();
        gen.addProvider(true, new StructureNbtUpdater("structure", Bumblezone.MODID, resourceManager, output));
    }
}
